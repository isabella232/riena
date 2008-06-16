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

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.riena.ui.core.uiprocess.IUISynchronizer;

/**
 * Loads the value of a node in a dynamic tree. A placeholder user element will
 * be replaced with the actual user element from the nodes parent child
 * iterator.
 */
class LoadNodeProcess extends DynamicTreeModificationProcess {

	private final DynamicTreeNode node;
	private final DynamicTreeNode parent;
	private Collection<? extends IUserTreeElement> userTreeElements;

	/**
	 * Constructor requires extension point
	 * 
	 * @param treeModel
	 *            The tree model.
	 * @param node
	 *            The node to load.
	 */
	LoadNodeProcess(final DynamicLoadTreeModel treeModel, final DynamicTreeNode node) {
		super(treeModel, LoadNodeProcess.class.getSimpleName());
		this.node = node;
		parent = (DynamicTreeNode) node.getParent();
	}

	/**
	 * Constructor requires extension point
	 * 
	 * @param treeModel
	 *            The tree model.
	 * @param node
	 *            The node to load.
	 */
	LoadNodeProcess(final DynamicLoadTreeModel treeModel, final DynamicTreeNode node, final IUISynchronizer syncher) {
		super(treeModel, LoadNodeProcess.class.getSimpleName(), syncher);
		this.node = node;
		parent = (DynamicTreeNode) node.getParent();
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.DynamicTreeModificationProcess#isResettingTree()
	 */
	@Override
	boolean isResettingTree() {
		return false;
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.DynamicTreeModificationProcess#getModifiedNode()
	 */
	@Override
	protected DynamicTreeNode getModifiedNode() {
		return parent;
	}

	/**
	 * The children of a dynamic tree node are loaded one by one, so the value
	 * of the node to load can be aquired by getting the next child user element
	 * of his parent.
	 * 
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.DynamicTreeModificationProcess#prepareModifyTree()
	 */
	@Override
	void prepareModifyTree() {
		if (parent != null && parent.hasNextChildUserTreeElements()) {
			userTreeElements = parent.getNextChildUserTreeElements();
		}
	}

	/**
	 * If the user tree element could be retrieved use it to replace the
	 * placeholder. Otherwise delete the node with the placeholder user element.
	 * Add a new node with a placeholder user element if the parent has more
	 * children.
	 * 
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.DynamicTreeModificationProcess#modifyTree()
	 */
	@Override
	void modifyTree() {
		if (userTreeElements != null && !userTreeElements.isEmpty()) {
			final IUserTreeElementFilter filter = ((IUserTreeElement) parent.getUserObject()).getFilter();
			for (IUserTreeElement userTreeElement : userTreeElements) {
				userTreeElement.setFilter(filter);
			}
			final Iterator<? extends IUserTreeElement> userTreeElementsIterator = userTreeElements.iterator();
			node.setUserObject(userTreeElementsIterator.next());
			getTreeModel().nodeChanged(node);
			addPlaceholderNode(node);

			while (userTreeElementsIterator.hasNext()) {
				final DynamicTreeNode additionallyLoadedNode = parent.createChildNode();
				additionallyLoadedNode.setUserObject(userTreeElementsIterator.next());
				getTreeModel().addNode(additionallyLoadedNode, parent);
				addPlaceholderNode(additionallyLoadedNode);
			}

			addPlaceholderNode(parent);
		} else {
			if (parent != null) {
				getTreeModel().removeNodeFromParent(node);
			}
		}
	}

	private void addPlaceholderNode(final DynamicTreeNode aNode) {
		if (aNode.hasNextChildUserTreeElements()) {
			final DynamicTreeNode nextChildWithPlaceholder = aNode.createChildNode();
			nextChildWithPlaceholder.setUserObject(aNode.createPlaceholderUserElement());
			getTreeModel().addNode(nextChildWithPlaceholder, aNode);
		}
	}

	/**
	 * Returns the node to load.
	 * 
	 * @return The node to load.
	 */
	DynamicTreeNode getNode() {
		return node;
	}

}
