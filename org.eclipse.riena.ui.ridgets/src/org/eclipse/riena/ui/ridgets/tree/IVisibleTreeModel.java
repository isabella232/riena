/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.ridgets.tree;

/**
 * The interface that defines a suitable data model for a
 * <code>TreeAdapter</code> for trees which may have invisible nodes.
 */
public interface IVisibleTreeModel {

	/**
	 * Returns the child of <code>parent</code> at given index in the parent's
	 * visible children.
	 * 
	 * @param parent
	 *            - a node in the tree, obtained from this data source.
	 * @param index
	 *            - the <code>index</code> of the child.
	 * @return child with <code>index</code>.
	 * @pre parent != null
	 * @pre parent instanceof IVisibleTreeNode
	 */
	Object getVisibleChild(Object parent, int index);

	/**
	 * Returns the number of visible children of <code>parent</code>.<br>
	 * Returns 0 if the node is a leaf or if it has no children.
	 * 
	 * @param parent
	 *            - a node in the tree, obtained from this data source.
	 * @return number of children.
	 * @pre parent != null
	 * @pre parent instanceof IVisibleTreeNode
	 */
	int getVisibleChildCount(Object parent);

	/**
	 * Returns the index of visible <code>child</code> in <code>parent</code>.
	 * If <code>parent</code> is null or <code>child</code> is null, returns -1.
	 * 
	 * @param parent
	 *            - a node in the tree, obtained from this data source.
	 * @param child
	 *            - the node we are interested in.
	 * @return the index of the <code>child</code> in the <code>parent</code>.
	 * @pre parent != null
	 * @pre parent instanceof IVisibleTreeNode
	 * @pre child != null
	 * @pre child instanceof IVisibleTreeNode
	 */
	int getIndexOfVisibleChild(Object parent, Object child);

	/**
	 * Returns whether the given <code>node</code> is a leaf (i.e.
	 * <code>node</code> has no visible children) or not.
	 * 
	 * @param node
	 *            - a node in the tree.
	 * @return true if <code>node</code> is a leaf; otherwise false.
	 * @pre node != null
	 * @pre node instanceof IVisibleTreeNode
	 */
	boolean isVisibleLeaf(Object node);
}
