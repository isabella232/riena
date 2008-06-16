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

import java.io.Serializable;

/**
 * Defines the requirements for an object that can be used as a tree node inside
 * a <code>TreeSwingAdapter</code> and may be visible or invisible.
 */
public interface IVisibleTreeNode extends Serializable {

	/**
	 * Returns the visible child <code>ITreeNode</code> at the given index.
	 * 
	 * @param childIndex -
	 *            index of the visible child.
	 * @return visible child at the given index.
	 */
	IVisibleTreeNode getVisibleChildAt(int childIndex);

	/**
	 * Returns the number of visible children <code>ITreeNode</code> the
	 * receiver contains.
	 * 
	 * @return number of visible children.
	 */
	int getVisibleChildCount();

	/**
	 * Returns the index of given node in the receiver's visible children. If
	 * the receiver does not contain node, -1 will be returned.
	 * 
	 * @param node
	 * @return index of the node
	 * @pre node != null
	 */
	int getVisibleIndex(IVisibleTreeNode node);

	/**
	 * Returns whether the node is a leaf (i.e. node has no visible children) or
	 * not.
	 * 
	 * @return true, if the node is a leaf; otherwise false.
	 */
	boolean isVisibleLeaf();

	/**
	 * Returns whether the node is visible or not.
	 * 
	 * @return true, if the node is visible; otherwise false.
	 */
	boolean isVisible();

	/**
	 * Sets the visibility of the node to true or false.
	 * 
	 * @param visible
	 */
	void setVisible(boolean visible);
}