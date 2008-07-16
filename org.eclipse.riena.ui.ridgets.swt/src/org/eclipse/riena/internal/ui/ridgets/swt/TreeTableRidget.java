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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.riena.ui.ridgets.ITreeTableRidget;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

/**
 * TODO [ev] docs
 */
public class TreeTableRidget extends TreeRidget implements ITreeTableRidget {

	public void bindToModel(Object treeRoot, Class<? extends Object> treeElementClass, String childrenAccessor,
			String[] columnPropertyNames, String[] columnHeaders) {
		super.bindToModel(treeRoot, treeElementClass, childrenAccessor, columnPropertyNames);
		// TODO [ev] hack - remove
		Tree tree = getUIControl();
		if (tree != null) {
			TreeColumn[] columns = tree.getColumns();
			for (int i = 0; i < columns.length; i++) {
				columns[i].setText(columnPropertyNames[i]);
			}
		}
	}

	public int getSortedColumn() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isColumnSortable(int columnIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSortedAscending() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setColumnSortable(int columnIndex, boolean sortable) {
		// TODO Auto-generated method stub
	}

	public void setSortedAscending(boolean ascending) {
		// TODO Auto-generated method stub
	}

	public void setSortedColumn(int columnIndex) {
		// TODO Auto-generated method stub
	}

}
