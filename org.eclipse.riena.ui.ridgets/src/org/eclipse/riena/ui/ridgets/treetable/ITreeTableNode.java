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

import org.eclipse.riena.ui.ridgets.tree.ITreeNode;

/**
 * A node in a tree table. A tree table is a tree with additional values per
 * node that is displayed as a table with a tree column.
 */
public interface ITreeTableNode extends ITreeNode {

	/**
	 * Returns the values of all columns except the tree column. The value of
	 * the tree column is the node itself.
	 * 
	 * @param column
	 *            The column index.
	 * @return The nodes value for the specified column
	 */
	Object getValueAt(int column);

}
