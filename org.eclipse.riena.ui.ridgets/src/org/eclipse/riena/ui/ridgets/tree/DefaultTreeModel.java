/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.core.runtime.Assert;
import org.eclipse.riena.ui.ridgets.UIBindingFailure;

/**
 * A simple tree data model that uses <code>DefaultTreeNode</code>.
 * 
 * @author Thorsten Schenkel
 */
public class DefaultTreeModel extends AbstractObservableValue implements ITreeModel {

	protected ITreeNode root;
	protected List<ITreeModelListener> listenerList = new ArrayList<ITreeModelListener>(1);

	/**
	 * Creates a tree in which any node can have children.
	 * 
	 * @param root -
	 *            the root of the tree.
	 */
	public DefaultTreeModel(ITreeNode root) {

		this.root = root;

	} // end method

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.ITreeModel#getChild(java.lang.Object,
	 *      int)
	 */
	public Object getChild(Object parent, int index) {

		Assert.isNotNull(parent, "parent is null");
		Assert.isTrue(parent instanceof ITreeNode, "parent is not an instance of ITreeNode");

		return ((ITreeNode) parent).getChildAt(index);

	} // end method

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.ITreeModel#getChildCount(java.lang.Object)
	 */
	public int getChildCount(Object parent) {

		Assert.isNotNull(parent, "parent is null");
		Assert.isTrue(parent instanceof ITreeNode, "parent is not an instance of ITreeNode");

		return ((ITreeNode) parent).getChildCount();

	} // end method

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.ITreeModel#getRoot()
	 */
	public Object getRoot() {

		return root;

	} // end method

	public Object doGetValue() {
		return this;
	}

	public Object getValueType() {
		return null;
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.ITreeModel#getIndexOfChild(java.lang.Object,
	 *      java.lang.Object)
	 */
	public int getIndexOfChild(Object parent, Object child) {

		Assert.isNotNull(parent, "parent is null");
		Assert.isTrue(parent instanceof ITreeNode, "parent is not an instance of ITreeNode");
		Assert.isNotNull(child, "child is null");
		Assert.isTrue(child instanceof ITreeNode, "child is not an instance of ITreeNode");

		return ((ITreeNode) parent).getIndex((ITreeNode) child);

	} // end method

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.ITreeModel#isLeaf(java.lang.Object)
	 */
	public boolean isLeaf(Object node) {

		Assert.isNotNull(node, "node is null");
		Assert.isTrue(node instanceof ITreeNode, "node is not an instance of ITreeNode");

		return ((ITreeNode) node).isLeaf();

	} // end method

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.ITreeModel#
	 *      addTreeModelListener(de.compeople.spirit.core.client.uibinding.adapter.tree.ITreeModelListener)
	 */
	public void addTreeModelListener(ITreeModelListener l) {

		listenerList.add(l);

	} // end method

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.ITreeModel#
	 *      removeTreeModelListener(de.compeople.spirit.core.client.uibinding.adapter.tree.ITreeModelListener)
	 */
	public void removeTreeModelListener(ITreeModelListener l) {

		listenerList.remove(l);

	} // end method

	/**
	 * Invoke this to insert newChild at location index of the parent's children
	 * list.
	 * 
	 * @param newChild
	 * @param parent
	 * @param index
	 */
	public void insertNodeInto(DefaultTreeNode newChild, DefaultTreeNode parent, int index) {

		parent.insert(newChild, index);

		int[] newIndexs = new int[1];
		newIndexs[0] = index;

		Serializable[] newChildren = new Serializable[1];
		newChildren[0] = newChild;

		fireTreeNodesInserted(this, parent, newIndexs, newChildren);

	} // end method

	/**
	 * Invoke this to append newChild to the end of the parent's children list.
	 * 
	 * @param newChild
	 * @param parent
	 */
	public void addNode(DefaultTreeNode newChild, DefaultTreeNode parent) {

		insertNodeInto(newChild, parent, getChildCount(newChild, parent));
	} // end method

	protected int getChildCount(DefaultTreeNode newChild, DefaultTreeNode parent) {

		return getChildCount(parent);
	}

	/**
	 * remove an existing tree node from the parent's children list.
	 * 
	 * @param node
	 *            node reference.
	 */
	public void removeNodeFromParent(DefaultTreeNode node) {

		DefaultTreeNode parent = (DefaultTreeNode) node.getParent();

		if (parent == null) {
			throw new UIBindingFailure("node does not have a parent.");
		} // end if

		int[] childIndex = new int[1];
		Serializable[] removedArray = new Serializable[1];

		childIndex[0] = getIndex(parent, node);
		removedArray[0] = node;
		parent.remove(childIndex[0]);

		fireTreeNodesRemoved(this, parent, childIndex, removedArray);

	} // end method

	protected int getIndex(DefaultTreeNode parent, DefaultTreeNode node) {

		return getIndexOfChild(parent, node);
	}

	/**
	 * Invoke this method after the specified node has changed.
	 * 
	 * @param node
	 *            The changed node.
	 */
	public void nodeChanged(ITreeNode node) {

		if (listenerList != null && node != null) {

			ITreeNode parent = node.getParent();

			if (parent != null) {

				int anIndex = -1;
				if (parent instanceof IVisibleTreeNode) {
					IVisibleTreeNode changeVisibilityParent = (IVisibleTreeNode) parent;
					anIndex = changeVisibilityParent.getVisibleIndex((IVisibleTreeNode) node);
				} else {
					anIndex = parent.getIndex(node);
				}
				if (anIndex != -1) {
					int[] cIndexs = new int[1];
					cIndexs[0] = anIndex;
					nodesChanged(parent, cIndexs);
				} // end if

			} else if (node == getRoot()) {

				nodesChanged(node, null);

			} // end if

		} // end if

	} // end method

	/**
	 * Invoke this method after the specified nodes have changed.
	 * 
	 * @param node
	 *            The parent of the changed nodes.
	 * @param childIndices
	 *            The indices of the changed children.
	 */
	public void nodesChanged(ITreeNode node, int[] childIndices) {

		if (node != null) {

			if (childIndices != null) {

				if (childIndices.length > 0) {
					Serializable[] cChildren = getChildren(node, childIndices);
					fireTreeNodesChanged(this, node, childIndices, cChildren);
				} // end if

			} else if (node == getRoot()) {

				fireTreeNodesChanged(this, node, null, null);

			} // end if

		} // end if

	} // end method

	/**
	 * Invoke this method after new nodes have been added.
	 * 
	 * @param node
	 *            The parent of the added nodes.
	 * @param childIndices
	 *            The indices of the added children.
	 */
	public void nodesAdded(ITreeNode node, int[] childIndices) {
		if (node != null && childIndices != null) {
			fireTreeNodesInserted(this, node, childIndices, getChildren(node, childIndices));
		}
	}

	/**
	 * Invoke this method after nodes have been removed.
	 * 
	 * @param node
	 *            The parent of the removed nodes.
	 * @param childIndices
	 *            The former indices of the removed children.
	 */
	public void nodesRemoved(ITreeNode node, int[] childIndices) {
		if (node != null && childIndices != null) {
			fireTreeNodesRemoved(this, node, childIndices, getChildren(node, childIndices));
		}
	}

	private Serializable[] getChildren(ITreeNode node, int[] childIndices) {
		int cCount = childIndices.length;

		Serializable[] cChildren = new Serializable[cCount];
		for (int counter = 0; counter < cCount; counter++) {
			cChildren[counter] = node.getChildAt(childIndices[counter]);
		} // end for
		return cChildren;
	}

	/**
	 * Refreshs the model.<br>
	 * Notifies all listeners that the structure of the whole model has changed.
	 */
	public void refresh() {

		fireTreeStructureChanged(this, (ITreeNode) getRoot(), null, null);

	}

	/**
	 * Notifies all listeners that have registered interest for notification on
	 * this event type.
	 * 
	 * @param source
	 * @param node
	 * @param childIndices
	 * @param children
	 */
	protected void fireTreeStructureChanged(IObservable source, ITreeNode node, int[] childIndices,
			Serializable[] children) {
		TreeModelEvent e = null;

		for (ITreeModelListener listener : listenerList) {
			if (e == null) {
				e = TreeModelEvent.createStructureChangedInstance(source, node, childIndices, children);
			} // end if
			listener.treeStructureChanged(e);
		} // end for

	} // end method

	/**
	 * Notifies all listeners that have registered interest for notification on
	 * this event type.
	 * 
	 * @param source
	 * @param node
	 * @param childIndices
	 * @param children
	 */
	protected void fireTreeNodesInserted(IObservable source, ITreeNode node, int[] childIndices, Serializable[] children) {
		TreeModelEvent e = null;

		for (ITreeModelListener listener : listenerList) {
			if (e == null) {
				e = TreeModelEvent.createNodesInsertedInstance(source, node, childIndices, children);
			} // end if
			listener.treeNodesInserted(e);
		} // end for

	} // end method

	/**
	 * Notifies all listeners that have registered interest for notification on
	 * this event type.
	 * 
	 * @param source
	 * @param node
	 * @param childIndices -
	 *            the indices of the new elements
	 * @param children -
	 *            the new elements
	 */
	protected void fireTreeNodesRemoved(IObservable source, ITreeNode node, int[] childIndices, Serializable[] children) {
		TreeModelEvent e = null;

		for (ITreeModelListener listener : listenerList) {
			if (e == null) {
				e = TreeModelEvent.createNodesRemovedInstance(source, node, childIndices, children);
			} // end if
			listener.treeNodesRemoved(e);
		} // end for

	} // end method

	/**
	 * Notifies all listeners that have registered interest for notification on
	 * this event type.
	 * 
	 * @param source
	 * @param node
	 * @param childIndices -
	 *            the indices of the removed elements
	 * @param children -
	 *            the removed elements
	 */
	protected void fireTreeNodesChanged(IObservable source, ITreeNode node, int[] childIndices, Serializable[] children) {
		TreeModelEvent e = null;

		for (ITreeModelListener listener : listenerList) {
			if (e == null) {
				e = TreeModelEvent.createValueDiffInstance(source, node, childIndices, children);
			} // end if
			listener.treeNodesChanged(e);
		} // end for

	} // end method

} // end class
