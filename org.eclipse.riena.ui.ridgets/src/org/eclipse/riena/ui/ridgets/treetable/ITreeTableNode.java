/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2005 compeople AG                         *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.treetable;

import org.eclipse.riena.ui.ridgets.tree.ITreeNode;

/**
 * A node in a tree table. A tree table is a tree with additional values per node
 * that is displayed as a table with a tree column.
 *
 * @author Carsten Drossel
 */
public interface ITreeTableNode extends ITreeNode {

	/**
	 * Returns the values of all columns except the tree column. The value of the
	 * tree column is the node itself.
	 *
	 * @param column The column index.
	 * @return The nodes value for the specified column
	 */
	Object getValueAt( int column );

}
