/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2005 compeople AG                         *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.treetable;

import org.eclipse.riena.ui.ridgets.tree.DynamicLoadTreeModel;

/**
 * Model of a dynamic tree table.
 *
 * @author Carsten Drossel
 */
public class DynamicLoadTreeTableModel extends DynamicLoadTreeModel implements ITreeTableModel {

	private ITreeTableModel treeTabelModelDelegate;

	/**
	 * Constructor.
	 *
	 * @param root The root of the tree.
	 * @param columnNames
	 */
	public DynamicLoadTreeTableModel( DynamicTreeTableNode root, String[] columnNames ) {
		super( root );
		treeTabelModelDelegate = new DefaultTreeTableModel( root, columnNames );
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.treetable.ITreeTableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return treeTabelModelDelegate.getColumnCount();
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.treetable.ITreeTableModel#getColumnName(int)
	 */
	public String getColumnName( int column ) {
		return treeTabelModelDelegate.getColumnName( column );
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.treetable.ITreeTableModel#getTreeColumnIndex()
	 */
	public int getTreeColumnIndex() {
		return treeTabelModelDelegate.getTreeColumnIndex();
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.treetable.ITreeTableModel#getValueAt(de.compeople.spirit.core.client.uibinding.adapter.treetable.ITreeTableNode, int)
	 */
	public Object getValueAt( ITreeTableNode node, int column ) {
		return treeTabelModelDelegate.getValueAt( node, column );
	}

}
