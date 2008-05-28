/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2005 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.treetable;

/**
 * A node in a tree table that allows its column values to be modified.
 *
 * @author Carsten Drossel
 */
public interface IWritableTreeTableNode extends ITreeTableNode {

	/**
	 * Indicates whether the nodes value for the specified column is editable.
	 * 
	 * @param column The column index.
	 * @return true if the specified cell is editable, false otherwise.
	 */
	boolean isColumnEditable( int column );

	/**
	 * Sets the nodes value for the specified column.
	 * 
	 * @param value The new value for the specified column.
	 * @param column The column index.
	 */
	void setValueAt( Object value, int column );

}
