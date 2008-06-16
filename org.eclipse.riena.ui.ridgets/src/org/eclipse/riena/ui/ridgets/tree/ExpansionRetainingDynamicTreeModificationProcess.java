/*******************************************************************************
 * Copyright (c) 2007, 2008 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.ridgets.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.riena.ui.core.uiprocess.IUISynchronizer;
import org.eclipse.riena.ui.core.uiprocess.UIProcess;
import org.eclipse.riena.ui.ridgets.obsolete.ITreeAdapter;

/**
 * A process that retrieves the expansion state of the dynamic tree, performs
 * some change on the underlying model (e.g. change the filter of the user
 * elements), resets the children of the root element and then attempts to
 * restore the expansion by the values of the nodes previously expanded. Nodes
 * that no longer exist due to the model change cannot be expanded.
 */
class ExpansionRetainingDynamicTreeModificationProcess extends DynamicTreeModificationProcess {

	private final Runnable action;
	private final ITreeAdapter tree;
	private DynamicTreeNode root;
	private ValueTreeNode loadedValuesState;
	private List<LoadNodeByValueSubProcess> subModificationProcesses;

	/**
	 * Constructor requires extension point.
	 * 
	 * @see UIProcess
	 * 
	 * @param treeModel
	 *            The tree model.
	 * @param action
	 *            The action changing the underlying model.
	 * @param tree
	 *            The tree.
	 */
	ExpansionRetainingDynamicTreeModificationProcess(final DynamicLoadTreeModel treeModel, final Runnable action,
			final ITreeAdapter tree) {
		super(treeModel, ExpansionRetainingDynamicTreeModificationProcess.class.getSimpleName());

		this.action = action;
		this.tree = tree;
		subModificationProcesses = new ArrayList<LoadNodeByValueSubProcess>(2);
		root = ((DynamicTreeNode) getTreeModel().getRoot());
	}

	/**
	 * Constructor.
	 * 
	 * @param treeModel
	 *            The tree model.
	 * @param action
	 *            The action changing the underlying model.
	 * @param tree
	 *            The tree.
	 * @param syncher
	 *            the class so synchronize UI events (different for STW/Swing)
	 */
	ExpansionRetainingDynamicTreeModificationProcess(final DynamicLoadTreeModel treeModel, final Runnable action,
			final ITreeAdapter tree, final IUISynchronizer syncher) {
		super(treeModel, ExpansionRetainingDynamicTreeModificationProcess.class.getSimpleName(), syncher);

		this.action = action;
		this.tree = tree;
		subModificationProcesses = new ArrayList<LoadNodeByValueSubProcess>(2);
		root = ((DynamicTreeNode) getTreeModel().getRoot());
	}

	/**
	 * Store the expansion state in the tree UI control, retrieves the loaded
	 * nodes by value, performs the action that modifies the underlying model
	 * (e.g. a filter change). This is done here rather than in
	 * prepareModifyTree() because in this method is called in a UI-thread-safe
	 * way.
	 * 
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.DynamicTreeModificationProcess#initialUpdateUI()
	 */
	@Override
	public void initialUpdateUI(final int totalWork) {
		super.initialUpdateUI(totalWork);
		tree.saveExpansionAndSelectionByValue();
		loadedValuesState = getTreeModel().getLoadedValuesState();
		action.run();
	}

	/**
	 * Resets the children of the root and then tries to load all nodes that
	 * where previously loaded based on the values retrieved in
	 * initialUpdateUI(). All modifications that have to be performed on the
	 * tree (delete nodes, add nodes) are queued.
	 * 
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.DynamicTreeModificationProcess#prepareModifyTree()
	 */
	@Override
	void prepareModifyTree() {
		root.resetChildIterator();
		queueLoadNodeByValueSubProcess(loadedValuesState, root);
	}

	private void queueLoadNodeByValueSubProcess(ValueTreeNode valuesToLoad, DynamicTreeNode parent) {
		LoadNodeByValueSubProcess loadNodeProcess = new LoadNodeByValueSubProcess(valuesToLoad, parent);
		subModificationProcesses.add(loadNodeProcess);
		loadNodeProcess.prepareModifyTree();
	}

	/**
	 * Updates the UI by performing all queued tree modifications. After the
	 * load state of the tree is restored the expansion state previously stored
	 * in the UI control is restored.
	 * 
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.DynamicTreeModificationProcess#modifyTree()
	 */
	@Override
	void modifyTree() {
		for (int index = root.getChildCount() - 1; index >= 0; index--) {
			getTreeModel().removeNodeFromParent((DefaultTreeNode) root.getChildAt(index));
		}
		for (LoadNodeByValueSubProcess subModificationProcess : subModificationProcesses) {
			subModificationProcess.modifyTree();
		}
		tree.restoreExpansionAndSelectionByValue();
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.DynamicTreeModificationProcess#isResettingTree()
	 */
	@Override
	boolean isResettingTree() {
		return true;
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.DynamicTreeModificationProcess#getModifiedNode()
	 */
	@Override
	protected DynamicTreeNode getModifiedNode() {
		return root;
	}

	private final class LoadNodeByValueSubProcess {

		private ValueTreeNode valuesToLoad;
		private Collection<DynamicTreeNode> nodes;
		private DynamicTreeNode parent;
		private Collection<? extends IUserTreeElement> userTreeElements;
		private Map<DynamicTreeNode, Boolean> addChildrenPlaceholder;
		private boolean addNextNodePlaceholder = false;

		private LoadNodeByValueSubProcess(ValueTreeNode valuesToLoad, DynamicTreeNode parent) {
			this.valuesToLoad = valuesToLoad;
			this.parent = parent;
			nodes = new ArrayList<DynamicTreeNode>();
			userTreeElements = new ArrayList<IUserTreeElement>();
			addChildrenPlaceholder = new HashMap<DynamicTreeNode, Boolean>();
		}

		private void prepareModifyTree() {
			if (parent != null && parent.hasNextChildUserTreeElements()) {
				userTreeElements = parent.getNextChildUserTreeElements();
				if (userTreeElements != null && !userTreeElements.isEmpty()) {
					IUserTreeElementFilter filter = ((IUserTreeElement) parent.getUserObject()).getFilter();
					for (IUserTreeElement userTreeElement : userTreeElements) {
						DynamicTreeNode node = parent.createChildNode();
						userTreeElement.setFilter(filter);
						node.setUserObject(userTreeElement);
						nodes.add(node);
						ValueTreeNode childValuesToLoad = valuesToLoad.getChild(userTreeElement.toString());
						if (childValuesToLoad != null) {
							if (childValuesToLoad.hasChildren()) {
								queueLoadNodeByValueSubProcess(childValuesToLoad, node);
								addChildrenPlaceholder.put(node, false);
							} else {
								addChildrenPlaceholder.put(node, true);
							}
							valuesToLoad.remove(childValuesToLoad);
						} else {
							addChildrenPlaceholder.put(node, true);
						}
					}

					if (parent.hasNextChildUserTreeElements()) {
						if (valuesToLoad.hasChildren()) {
							queueLoadNodeByValueSubProcess(valuesToLoad, parent);
						} else {
							addNextNodePlaceholder = true;
						}
					}
				}
			}
		}

		private void modifyTree() {
			if (nodes != null) {
				for (DynamicTreeNode node : nodes) {
					getTreeModel().addNode(node, parent);
					if (addChildrenPlaceholder.get(node)) {
						addPlaceholderNode(node);
					}
				}
				if (addNextNodePlaceholder) {
					addPlaceholderNode(parent);
				}
			}
		}

		private void addPlaceholderNode(DynamicTreeNode aNode) {
			if (aNode.hasNextChildUserTreeElements()) {
				DynamicTreeNode nextChildWithPlaceholder = aNode.createChildNode();
				nextChildWithPlaceholder.setUserObject(aNode.createPlaceholderUserElement());
				getTreeModel().addNode(nextChildWithPlaceholder, aNode);
			}
		}

	}

}
