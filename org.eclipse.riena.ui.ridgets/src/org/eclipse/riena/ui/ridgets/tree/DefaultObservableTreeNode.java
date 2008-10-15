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
import java.util.List;

import org.eclipse.core.databinding.observable.Realm;

/**
 * 
 */
public class DefaultObservableTreeNode extends AbstractObservableTreeNode {

	private DefaultObservableTreeModel model;
	private ITreeNode parent;
	protected List<DefaultObservableTreeNode> children;
	private Object userObject;

	/**
	 * @param realm
	 * @param parent
	 */
	public DefaultObservableTreeNode() {

		super();
		children = new ArrayList<DefaultObservableTreeNode>();
	}

	/**
	 * @param realm
	 * @param parent
	 * @param userObject
	 */
	public DefaultObservableTreeNode(Object userObject) {

		this(Realm.getDefault(), userObject);
	}

	/**
	 * @param realm
	 * @param parent
	 * @param userObject
	 */
	public DefaultObservableTreeNode(Realm realm, Object userObject) {

		super(realm);
		this.userObject = userObject;
		children = new ArrayList<DefaultObservableTreeNode>();
	}

	public DefaultObservableTreeModel getModel() {

		return model;
	}

	void setModel(DefaultObservableTreeModel model) {

		this.model = model;
		for (DefaultObservableTreeNode child : children) {
			child.setModel(model);
		}
	}

	/*
	 * @see org.eclipse.riena.ui.internal.ridgets.tree.ITreeNode#getChildAt(int)
	 */
	public ITreeNode getChildAt(int childIndex) {
		return children.get(childIndex);
	}

	public void addChild(DefaultObservableTreeNode newChild) {

		addChild(getChildCount(), newChild);
	}

	public void addChild(int index, DefaultObservableTreeNode child) {

		children.add(index, child);
		child.setParent(this);
		child.setModel(this.getModel());
		if (getModel() != null) {
			getModel().fireEvent(
					this,
					TreeModelEvent.createNodesInsertedInstance(getModel(), this, new int[] { index },
							new DefaultObservableTreeNode[] { child }));
		}
	}

	public int removeFromParent() {

		if (getParent() != null) {
			return ((DefaultObservableTreeNode) getParent()).removeChild(this);
		} else {
			// orphan node
			return -1;
		}
	}

	public DefaultObservableTreeNode removeChild(int childIndex) {

		DefaultObservableTreeNode removed = children.remove(childIndex);
		if (getModel() != null) {
			getModel().fireEvent(
					this,
					TreeModelEvent.createNodesRemovedInstance(getModel(), this, new int[] { childIndex },
							new DefaultObservableTreeNode[] { removed }));
		}
		removed.setParent(null);
		removed.setModel(null);

		return removed;
	}

	public int removeChild(DefaultObservableTreeNode child) {

		int childIndex = children.indexOf(child);
		if (childIndex >= 0) {
			removeChild(childIndex);
		}

		return childIndex;
	}

	public void removeAllChildren() {

		if (children.isEmpty()) {
			return;
		}
		int[] indexes = new int[getChildCount()];
		DefaultObservableTreeNode[] oldChildren = new DefaultObservableTreeNode[getChildCount()];
		for (int i = 0; i < indexes.length; i++) {
			indexes[i] = i;
			oldChildren[i] = (DefaultObservableTreeNode) getChildAt(i);
		}
		children.clear();
		if (getModel() != null) {
			getModel().fireEvent(this,
					TreeModelEvent.createNodesRemovedInstance(getModel(), this, indexes, oldChildren));
		}
		for (DefaultObservableTreeNode removed : oldChildren) {
			removed.setParent(null);
			removed.setModel(null);
		}
	}

	/*
	 * @see org.eclipse.riena.ui.internal.ridgets.tree.ITreeNode#getChildCount()
	 */
	public int getChildCount() {
		return children.size();
	}

	/*
	 * @see
	 * org.eclipse.riena.ui.internal.ridgets.tree.ITreeNode#getIndex(org.eclipse.riena
	 * .ui.ridgets.tree.ITreeNode)
	 */
	public int getIndex(ITreeNode node) {
		return children.indexOf(node);
	}

	/*
	 * @see org.eclipse.riena.ui.internal.ridgets.tree.ITreeNode#getParent()
	 */
	public ITreeNode getParent() {
		return parent;
	}

	void setParent(ITreeNode parent) {
		this.parent = parent;
	}

	public Object getUserObject() {

		return userObject;
	}

	void setUserObject(Object obj) {

		userObject = obj;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		if (userObject == null) {
			return ""; //$NON-NLS-1$
		} else {
			return userObject.toString();
		} // end if

	} // end method
}
