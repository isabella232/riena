/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2005 compeople AG                         *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.treetable;

import org.eclipse.riena.ui.ridgets.tree.DefaultObservableTreeModel;
import org.eclipse.riena.ui.ridgets.tree.DefaultObservableTreeNode;

/**
 * An extension of the DefaultTreeModel to be used as a data model for tree
 * tables.
 * 
 * @author Carsten Drossel
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
