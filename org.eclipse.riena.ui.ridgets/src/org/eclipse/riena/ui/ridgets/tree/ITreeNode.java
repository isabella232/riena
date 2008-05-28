/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.tree;

import java.io.Serializable;

/**
 * Defines the requirements for an object that can be used as a tree node inside a <code>TreeAdapter</code>.
 * 
 * @author Thorsten Schenkel
 */
public interface ITreeNode extends Serializable {

	/**
	 * Returns the child <code>ITreeNode</code> at the given index.
	 * <p>
	 * <b>Annotation </b>: Is the index negative, than an exception is thrown, because this is an error that is not expected. Otherwise the index is greater
	 * than the (real) count of the children, than the return value is null. This is an error that can be possible, if the tree datas are not accurate. The tree
	 * is still useable, only the not exisiting child node is not displayed.
	 * 
	 * @param childIndex - index of the child.
	 * @return child at the given index.
	 *  
	 */
	ITreeNode getChildAt( int childIndex );

	/**
	 * Returns the number of children <code>ITreeNode</code> the receiver contains.
	 * 
	 * @return number of children.
	 */
	int getChildCount();

	/**
	 * Returns the parent tree node <code>ITreeNode</code> of the receiver.
	 * 
	 * @return parent
	 */
	ITreeNode getParent();

	/**
	 * Returns the index of given node in the receiver's children. If the receiver does not contain node, -1 will be returned.
	 * 
	 * @param node
	 * @return index of the node
	 * @pre node != null
	 */
	int getIndex( ITreeNode node );

	/**
	 * Returns whether the node is a leaf or not.
	 * 
	 * @return true, if the node is a leaf; otherwise false.
	 */
	boolean isLeaf();

	/**
	 * The level of a node <code>n</code>:
	 * <ul>
	 * <li><code>n</code> is root, then <code>level = 0</code>.
	 * <li><code>n</code> is not root and level of parent is <code>l</code>, then <code>level = l + 1</code>.
	 * </ul>
	 * 
	 * @return level of the node in the tree.
	 */
	int getLevel();
} // end interface
