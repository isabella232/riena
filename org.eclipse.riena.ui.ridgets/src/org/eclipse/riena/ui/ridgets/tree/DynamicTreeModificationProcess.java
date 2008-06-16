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

import org.eclipse.riena.ui.core.uiprocess.IUISynchronizer;
import org.eclipse.riena.ui.core.uiprocess.UIProcess;

/**
 * A UI process that modifies a dynamic tree.
 */
abstract class DynamicTreeModificationProcess extends UIProcess {

	private DynamicLoadTreeModel treeModel;

	/**
	 * Constructor requires extension point. Uses the name of this class (not of
	 * its subclass).
	 * 
	 * @see UIProcess
	 * 
	 * @param treeModel
	 *            The tree model.
	 */
	DynamicTreeModificationProcess(final DynamicLoadTreeModel treeModel) {
		// super constructor requires extension point
		super(DynamicTreeModificationProcess.class.getSimpleName());
		this.treeModel = treeModel;
	}

	/**
	 * Constructor requires extension point.
	 * 
	 * @see UIProcess
	 * 
	 * @param treeModel
	 *            The tree model.
	 * @param name
	 *            the name of the process
	 */
	DynamicTreeModificationProcess(final DynamicLoadTreeModel treeModel, final String name) {
		// super constructor requires extension point
		super(name);
		this.treeModel = treeModel;
	}

	/**
	 * Constructor. Uses the name of this class (not of its subclass).
	 * 
	 * @param treeModel
	 *            The tree model.
	 * @param syncher
	 *            the class so synchronize UI events (different for STW/Swing)
	 */
	DynamicTreeModificationProcess(final DynamicLoadTreeModel treeModel, final IUISynchronizer syncher) {
		this(treeModel, DynamicTreeModificationProcess.class.getSimpleName(), syncher);
	}

	/**
	 * Constructor.
	 * 
	 * @param treeModel
	 *            The tree model.
	 * @param name
	 *            the name of the process
	 * @param syncher
	 *            the class so synchronize UI events (different for STW/Swing)
	 */
	DynamicTreeModificationProcess(final DynamicLoadTreeModel treeModel, final String name,
			final IUISynchronizer syncher) {
		super(name, syncher);
		this.treeModel = treeModel;
	}

	/**
	 * Prepare the modification of the tree e.g. load node values. This method
	 * is executed asynchronous and must not modify the tree.
	 */
	void prepareModifyTree() {
		// empty default
	}

	/**
	 * Perform the modification of the tree by adding or removing nodes or by
	 * changing node values.
	 * 
	 * @see DefaultTreeModel#addNode(DefaultTreeNode, DefaultTreeNode)
	 * @see DefaultTreeModel#removeNodeFromParent(DefaultTreeNode)
	 * @see DefaultTreeModel#nodeChanged(ITreeNode)
	 */
	abstract void modifyTree();

	/**
	 * Indicates whether this process is changing the complete tree structure so
	 * that any other process that is already queued to be executed afterwards
	 * becomes obsolete. E.g. a process to load a specific node value becomes
	 * obsolete when queued after a process that removes all nodes but the root.
	 */
	abstract boolean isResettingTree();

	/**
	 * Returns the modified node that will be the source of the fired events.
	 * 
	 * @return The modified node.
	 */
	abstract protected DynamicTreeNode getModifiedNode();

	/**
	 * Returns the tree model.
	 * 
	 * @return The tree model.
	 */
	DynamicLoadTreeModel getTreeModel() {
		return treeModel;
	}

	/**
	 * @see de.compeople.spirit.core.client.process.UIProcess#initialUpdateUI()
	 */
	@Override
	public void initialUpdateUI(final int totalWork) {
		getTreeModel().fireEvent(getModifiedNode(), DynamicTreeReloadEvent.BEGIN);
	}

	/**
	 * @see de.compeople.spirit.core.base.process.IProcess#run()
	 */
	public void run() throws InterruptedException {
		prepareModifyTree();
	}

	/**
	 * @see de.compeople.spirit.core.client.process.UIProcess#finalUpdateUI()
	 */
	@Override
	public void finalUpdateUI() {
		if (isResettingTree()) {
			// remove other modification processes in the queue - they are
			// obsolete
			// because this one is resetting the tree...
			getTreeModel().clearTreeModificationProcessQueue();
			// new processes added during the modification will be started
			// automatically, because the queue is empty...
			finishTreeModification();
		} else {
			finishTreeModification();
			getTreeModel().startNextTreeModificationProcess();
		}
	}

	private void finishTreeModification() {
		modifyTree();
		getTreeModel().fireEvent(getModifiedNode(), DynamicTreeReloadEvent.END);
	}

}
