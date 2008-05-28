/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2005 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.tree;

import org.eclipse.riena.ui.core.uiprocess.IUISynchronizer;
import org.eclipse.riena.ui.core.uiprocess.UIProcess;

/**
 * Resets the children of the root element of a dynamic tree. All children will
 * be replaced with a dynamic tree node with a placeholder user element.
 * 
 * @author Carsten Drossel
 */
class ResetRootChildrenProcess extends DynamicTreeModificationProcess {

	private final DynamicTreeNode root;

	/**
	 * Constructor requires extension point.
	 * 
	 * @see UIProcess
	 * @param treeModel
	 *            The tree model.
	 */
	ResetRootChildrenProcess(final DynamicLoadTreeModel treeModel) {
		super(treeModel, ResetRootChildrenProcess.class.getSimpleName());
		this.root = (DynamicTreeNode) treeModel.getRoot();
	}

	/**
	 * Constructor.
	 * 
	 * @param treeModel
	 *            The tree model.
	 * @param syncher
	 *            the class so synchronize UI events (different for STW/Swing)
	 */
	ResetRootChildrenProcess(final DynamicLoadTreeModel treeModel, final IUISynchronizer syncher) {
		super(treeModel, ResetRootChildrenProcess.class.getSimpleName(), syncher);
		this.root = (DynamicTreeNode) treeModel.getRoot();
	}

	/**
	 * Removes all children of the root, resets the roots child iterator an adds
	 * a placeholder node that will trigger the new loading of the children when
	 * accessed.
	 * 
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.DynamicTreeModificationProcess#modifyTree()
	 */
	@Override
	protected void modifyTree() {
		for (int index = root.getChildCount() - 1; index >= 0; index--) {
			getTreeModel().removeNodeFromParent((DefaultTreeNode) root.getChildAt(index));
		}
		root.resetChildIterator();
		final DynamicTreeNode initialChildWithPlaceholder = root.createChildNode();
		initialChildWithPlaceholder.setUserObject(root.createPlaceholderUserElement());
		getTreeModel().addNode(initialChildWithPlaceholder, root);
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

}
