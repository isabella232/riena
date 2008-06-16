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
package org.eclipse.riena.ui.ridgets.treetable;

/**
 * A node in a tree table that allows its column values to be modified.
 */
public interface IWritableTreeTableNode extends ITreeTableNode {

	/**
	 * Indicates whether the nodes value for the specified column is editable.
	 * 
	 * @param column
	 *            The column index.
	 * @return true if the specified cell is editable, false otherwise.
	 */
	boolean isColumnEditable(int column);

	/**
	 * Sets the nodes value for the specified column.
	 * 
	 * @param value
	 *            The new value for the specified column.
	 * @param column
	 *            The column index.
	 */
	void setValueAt(Object value, int column);

}
