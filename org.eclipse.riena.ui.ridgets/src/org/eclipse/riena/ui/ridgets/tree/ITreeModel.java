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
 * <code>TreeAdapter</code>.
 */
public interface ITreeModel {

	/**
	 * Returns the child of <code>parent</code> at given index in the parent's
	 * child array.
	 * 
	 * @param parent
	 *            - a node in the tree, obtained from this data source.
	 * @param index
	 *            - the index of the child.
	 * @return child the requested child.
	 * @pre parent != null
	 * @pre parent instanceof ITreeNode
	 */
	Object getChild(Object parent, int index);

	/**
	 * Returns the number of children of <code>parent</code>.<br>
	 * Returns 0 if the node is a leaf or if it has no children.
	 * 
	 * @param parent
	 *            - a node in the tree, obtained from this data source.
	 * @return number of children
	 * @pre parent != null
	 * @pre parent instanceof ITreeNode
	 */
	int getChildCount(Object parent);

	/**
	 * Returns the root of the tree.
	 * 
	 * @return root of the tree.
	 */
	Object getRoot();

	/**
	 * Returns the index of <code>child</code> in <code>parent</code>. If
	 * <code>parent</code> is null or <code>child</code> is null, returns -1.
	 * 
	 * @param parent
	 *            - a node in the tree, obtained from this data source.
	 * @param child
	 *            - the node we are interested in.
	 * @return the index of the <code>child</code> in the <code>parent</code>.
	 * @pre parent != null
	 * @pre parent instanceof ITreeNode
	 * @pre child != null
	 * @pre child instanceof ITreeNode
	 */
	int getIndexOfChild(Object parent, Object child);

	/**
	 * Returns whether the given <code>node</code> is a leaf or not.
	 * 
	 * @param node
	 *            - a node in the tree.
	 * @return true if <code>node</code> is a leaf; otherwise false.
	 * @pre node != null
	 * @pre node instanceof ITreeNode
	 */
	boolean isLeaf(Object node);

	/**
	 * Adds a listener for the <code>TreeModelEvent</code> posted after the tree
	 * changes.
	 * <p>
	 * Adding the same listener several times has no effect.
	 * 
	 * @param l
	 *            - the listener to add (non-null).
	 * @throws RuntimeException
	 *             if listener {@code l} is null
	 * @see #removeTreeModelListener(ITreeModelListener)
	 */
	void addTreeModelListener(ITreeModelListener l);

	/**
	 * Removes a listener previously added with
	 * <code>addTreeModelListener</code>.
	 * 
	 * @param l
	 *            - the listener to remove (non-null).
	 * @throws RuntimeException
	 *             if listener {@code l} is null
	 * @see #addTreeModelListener(ITreeModelListener)
	 */
	void removeTreeModelListener(ITreeModelListener l);

	/**
	 * Refresh the model.
	 */
	void refresh();

} // end interface
