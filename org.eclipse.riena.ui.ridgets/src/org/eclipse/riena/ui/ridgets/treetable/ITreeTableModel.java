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
package org.eclipse.riena.ui.ridgets.treetable;

import org.eclipse.riena.ui.ridgets.tree.ITreeModel;

/**
 * The data model for a tree table. A tree table is a tree with additional
 * values per node that is displayed as a table with a tree column.
 */
public interface ITreeTableModel extends ITreeModel {

	/**
	 * Returns the number of columns including the tree column.
	 * 
	 * @return The number of columns including the tree column.
	 */
	int getColumnCount();

	/**
	 * Returns the index of the tree column.
	 * 
	 * @return The index of the tree column.
	 */
	int getTreeColumnIndex();

	/**
	 * Returns the name of the specified column that will be used in the table
	 * header.
	 * 
	 * @param column
	 *            The column index.
	 * @return The name of the column.
	 */
	String getColumnName(int column);

	/**
	 * Returns the value of a table cell. The row index is specified by a tree
	 * node. The actual index will vary depending on the expansion state of the
	 * tree. The value of the tree column is node itself.
	 * 
	 * @param node
	 *            The node representing the table row.
	 * @param column
	 *            The column index.
	 * @return The value of a table cell.
	 */
	Object getValueAt(ITreeTableNode node, int column);

	/**
	 * @param node
	 * @return
	 */
	boolean isLeaf(Object node);

}
