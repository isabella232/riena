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

import java.util.Vector;

import org.eclipse.core.runtime.Assert;

/**
 * A <code>DefaultTreeNode</code> is a general-purpose node in a tree data
 * structure.
 */
@SuppressWarnings("serial")
public class DefaultTreeNode implements ITreeNode {

	private DefaultTreeNode parent;
	protected Vector<DefaultTreeNode> children;
	private Object userObject;

	/**
	 * constructor.
	 * 
	 * Creates a tree node that has no parent and no children.
	 */
	public DefaultTreeNode() {

		this(null);

	} // end method

	/**
	 * constructor. Creates a tree node with no parent, no children and
	 * initializes it with the specified user object.
	 * 
	 * @param userObject
	 *            - an Object provided by the user that constitutes the node's
	 *            data.
	 */
	public DefaultTreeNode(Object userObject) {

		this(null, userObject);

	} // end method

	/**
	 * constructor. Creates a tree node with a parent and initializes it with
	 * the specified user object. But the tree node has no children.
	 * 
	 * @param parent
	 * @param userObject
	 *            - an Object provided by the user that constitutes the node's
	 *            data.
	 */
	public DefaultTreeNode(DefaultTreeNode parent, Object userObject) {
		super();

		children = new Vector<DefaultTreeNode>();

		setUserObject(userObject);
		setParent(parent);

	} // end method

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.ITreeNode#getChildAt(int)
	 * @pre children != null
	 */
	public ITreeNode getChildAt(int childIndex) {

		Assert.isNotNull(children, "node has no children"); //$NON-NLS-1$
		return children.elementAt(childIndex);
	} // end method

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.ITreeNode#getChildCount()
	 */
	public int getChildCount() {

		if (children == null) {
			return 0;
		} else {
			return children.size();
		} // end if
	} // end method

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.ITreeNode#getIndex(de.compeople.spirit.core.client.uibinding.adapter.tree.ITreeNode)
	 */
	public int getIndex(ITreeNode node) {

		Assert.isNotNull(node, "missing node argument"); //$NON-NLS-1$

		if (!isNodeChild(node)) {
			return -1;
		} // end if

		return children.indexOf(node); // linear search
	} // end method

	/**
	 * Returns true if <code>aNode</code> is a child of this node. If
	 * <code>aNode</code> is null, this method returns false.
	 * 
	 * @param aNode
	 *            - node to test.
	 * @return true if <code>aNode</code> is a child of this node; otherwise
	 *         false.
	 */
	public boolean isNodeChild(ITreeNode aNode) {

		boolean retValue;

		if (aNode == null) {
			retValue = false;
		} else {
			if (getChildCount() == 0) {
				retValue = false;
			} else {
				retValue = (aNode.getParent() == this);
			} // end if
		} // end if

		return retValue;

	} // end method

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.ITreeNode#isLeaf()
	 */
	public boolean isLeaf() {

		return getChildCount() == 0;

	} // end method

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

	/**
	 * Removes <code>newChild</code> from its parent (if it has a parent) and
	 * makes it a child of this node by adding it to the end of this node's
	 * child array.
	 * 
	 * @param newChild
	 *            - node to add as a child of this node.
	 */
	public void add(DefaultTreeNode newChild) {

		insert(newChild, children.size());

	} // end method

	/**
	 * Removes <code>newChild</code> from its present parent (if it has a
	 * parent), sets the child's parent to this node, and then adds the child to
	 * this node's child array at index childIndex. <code>newChild</code> must
	 * not be null and must not be an ancestor of this node.
	 * 
	 * @param newChild
	 *            - the tree node to insert under this node.
	 * @param childIndex
	 *            - the index in this node's child array where this node is to
	 *            be inserted.
	 */
	public void insert(DefaultTreeNode newChild, int childIndex) {

		DefaultTreeNode oldParent = (DefaultTreeNode) newChild.getParent();
		if (oldParent != null && oldParent != this) {
			oldParent.remove(newChild);
		} // end if

		newChild.setParent(this);

		children.insertElementAt(newChild, childIndex);

	} // end method

	/**
	 * Removes the child at the specified index from this node's children and
	 * sets that node's parent to null. The child node to remove must be a
	 * DefaultTreeNode.
	 * 
	 * @param childIndex
	 *            - the index in this node's child array of the child to remove.
	 */
	public void remove(int childIndex) {

		DefaultTreeNode child = (DefaultTreeNode) getChildAt(childIndex);
		children.removeElementAt(childIndex);
		child.setParent(null);

	} // end method

	/**
	 * Removes the subtree rooted at this node from the tree, giving this node a
	 * null parent.
	 */
	public void removeFromParent() {

		DefaultTreeNode myparent = (DefaultTreeNode) getParent();
		if (myparent != null) {
			myparent.remove(this);
		} // end if

	} // end method

	/**
	 * Removes <code>aChild</code> from this node's child array, giving it a
	 * null parent.
	 * 
	 * @param aChild
	 *            - the child of this node to remove.
	 * @pre aChild != null
	 * @pre isNodeChild( aChild )
	 */
	public void remove(DefaultTreeNode aChild) {

		Assert.isNotNull(aChild, "missing aChild argument"); //$NON-NLS-1$
		Assert.isTrue(isNodeChild(aChild), "aChild argument is not a child"); //$NON-NLS-1$

		remove(getIndex(aChild));
	} // end method

	/**
	 * Removes all of this node's children, setting their parents to null. If
	 * this node has no children, this method does nothing.
	 */
	public void removeAllChildren() {

		for (int i = getChildCount() - 1; i >= 0; i--) {
			remove(i);
		} // end for

	} // end if

	/**
	 * get object attached to the node.
	 * 
	 * @return Returns the userObject.
	 */
	public Object getUserObject() {

		return userObject;

	} // end method

	/**
	 * attach user object to the node.
	 * 
	 * @param userObject
	 *            The userObject to set.
	 */
	public void setUserObject(Object userObject) {

		this.userObject = userObject;

	} // end method

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.ITreeNode#getParent()
	 */
	public ITreeNode getParent() {

		return parent;

	} // end method

	/**
	 * set the parent node.
	 * 
	 * @param parent
	 *            The parent to set.
	 */
	public void setParent(DefaultTreeNode parent) {

		this.parent = parent;

	} // end method

	public int getLevel() {

		if (getParent() == null) {
			return 0;
		} else {
			return getParent().getLevel() + 1;
		}
	}
} // end class
