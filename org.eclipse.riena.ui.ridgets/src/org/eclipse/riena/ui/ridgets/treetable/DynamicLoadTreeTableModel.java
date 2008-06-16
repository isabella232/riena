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

import org.eclipse.riena.ui.ridgets.tree.DynamicLoadTreeModel;

/**
 * Model of a dynamic tree table.
 */
public class DynamicLoadTreeTableModel extends DynamicLoadTreeModel implements ITreeTableModel {

	private ITreeTableModel treeTabelModelDelegate;

	/**
	 * Constructor.
	 * 
	 * @param root
	 *            The root of the tree.
	 * @param columnNames
	 */
	public DynamicLoadTreeTableModel(DynamicTreeTableNode root, String[] columnNames) {
		super(root);
		treeTabelModelDelegate = new DefaultTreeTableModel(root, columnNames);
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
	public String getColumnName(int column) {
		return treeTabelModelDelegate.getColumnName(column);
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.treetable.ITreeTableModel#getTreeColumnIndex()
	 */
	public int getTreeColumnIndex() {
		return treeTabelModelDelegate.getTreeColumnIndex();
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.treetable.ITreeTableModel#getValueAt(de.compeople.spirit.core.client.uibinding.adapter.treetable.ITreeTableNode,
	 *      int)
	 */
	public Object getValueAt(ITreeTableNode node, int column) {
		return treeTabelModelDelegate.getValueAt(node, column);
	}

}
