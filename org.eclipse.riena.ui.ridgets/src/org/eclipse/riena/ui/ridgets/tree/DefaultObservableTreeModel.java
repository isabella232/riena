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

import org.eclipse.core.databinding.observable.ObservableEvent;

/**
 * 
 */
public class DefaultObservableTreeModel extends AbstractObservableTreeModel {

	protected ITreeNode root;

	/**
	 * 
	 */
	public DefaultObservableTreeModel(DefaultObservableTreeNode root) {

		this.root = root;
		root.setModel(this);
	}

	/*
	 * @see org.eclipse.riena.ui.ridgets.tree.ITreeModel#getChild(java.lang.Object,
	 *      int)
	 */
	public Object getChild(Object parent, int index) {

		return ((ITreeNode) parent).getChildAt(index);
	}

	/*
	 * @see org.eclipse.riena.ui.ridgets.tree.ITreeModel#getChildCount(java.lang.Object)
	 */
	public int getChildCount(Object parent) {

		return ((ITreeNode) parent).getChildCount();
	}

	/*
	 * @see org.eclipse.riena.ui.ridgets.tree.ITreeModel#getIndexOfChild(java.lang.Object,
	 *      java.lang.Object)
	 */
	public int getIndexOfChild(Object parent, Object child) {

		return ((ITreeNode) parent).getIndex((ITreeNode) child);
	}

	/*
	 * @see org.eclipse.riena.ui.ridgets.tree.ITreeModel#getRoot()
	 */
	public Object getRoot() {

		return root;
	}

	/*
	 * @see org.eclipse.riena.ui.ridgets.tree.ITreeModel#isLeaf(java.lang.Object)
	 */
	public boolean isLeaf(Object node) {
		return ((ITreeNode) node).isLeaf();
	}

	public void setUserObject(DefaultObservableTreeNode node, Object obj) {

		if (node.getModel() == this) {
			Object oldValue = node.getUserObject();
			node.setUserObject(obj);
			fireEvent(TreeModelEvent.createValueDiffInstance(this, node, oldValue, obj));
		} else {
			// TODO: wrong model exception
			throw new RuntimeException();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.tree.ITreeModel#refresh()
	 */
	public void refresh() {
		fireEvent(TreeModelEvent.createStructureChangedInstance(this, (ITreeNode) getRoot(), null, null));
	}

	protected void fireEvent(DefaultObservableTreeNode who, ObservableEvent event) {
		// TODO: need to check for correct model?
		fireEvent(event);
	}
}
