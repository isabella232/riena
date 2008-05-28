/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2005 compeople AG                         *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.treetable;

import org.eclipse.riena.ui.ridgets.tree.IUserTreeElement;

/**
 * Element used inside a dynamic tree table.
 *
 * @author Carsten Drossel
 */
public interface IUserTreeTableElement extends IUserTreeElement {

	/**
	 * Returns the values of all columns except the tree column. The value of the
	 * tree column is the node itself.
	 *
	 * @param column The column index.
	 * @return The value for the specified column
	 */
	Object getValueAt( int column );

}
