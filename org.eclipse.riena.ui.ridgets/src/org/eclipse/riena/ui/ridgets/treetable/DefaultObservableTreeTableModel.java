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

import org.eclipse.riena.ui.ridgets.tree.DefaultObservableTreeModel;
import org.eclipse.riena.ui.ridgets.tree.DefaultObservableTreeNode;

/**
 * An extension of the DefaultTreeModel to be used as a data model for tree
 * tables.
 */
public class DefaultObservableTreeTableModel extends DefaultObservableTreeModel implements IObservableTreeTableModel {

	private static final int DEFAULT_TREE_COLUMN_INDEX = 0;

	private String[] columnNames;

	/**
	 * Constructor.
	 * 
	 * @param root
	 *            The root of the tree.
	 * @param columnNames
	 */
	public DefaultObservableTreeTableModel(DefaultObservableTreeNode root, String[] columnNames) {
		super(root);
		this.columnNames = new String[columnNames.length];
		System.arraycopy(columnNames, 0, this.columnNames, 0, columnNames.length);
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.treetable.ITreeTableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return columnNames.length;
	}

	/**
	 * By default the first column is the tree column. This method may be
	 * overridden to change that.
	 * 
	 * @see de.compeople.spirit.core.client.uibinding.adapter.treetable.ITreeTableModel#getTreeColumnIndex()
	 */
	public int getTreeColumnIndex() {
		return DEFAULT_TREE_COLUMN_INDEX;
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.treetable.ITreeTableModel#getColumnName(int)
	 */
	public String getColumnName(int column) {
		return columnNames[column];
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.treetable.ITreeTableModel#getValueAt(de.compeople.spirit.core.client.uibinding.adapter.treetable.ITreeTableNode,
	 *      int)
	 */
	public Object getValueAt(ITreeTableNode node, int columnIndex) {
		if (columnIndex == getTreeColumnIndex()) {
			return node;
		}
		return node.getValueAt(columnIndex);
	}

}
