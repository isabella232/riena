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

import org.eclipse.riena.ui.ridgets.tree.IUserTreeElement;

/**
 * Element used inside a dynamic tree table.
 */
public interface IUserTreeTableElement extends IUserTreeElement {

	/**
	 * Returns the values of all columns except the tree column. The value of
	 * the tree column is the node itself.
	 * 
	 * @param column
	 *            The column index.
	 * @return The value for the specified column
	 */
	Object getValueAt(int column);

}
