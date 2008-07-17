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

/**
 * Ridget for SWT @link {@link Tree} widgets, that shows a tree with multiple
 * columns.
 */
public class TreeTableRidget extends TreeRidget implements ITreeTableRidget {

	// ITreeTableRidget methods
	// /////////////////////////

	public void bindToModel(Object[] treeRoots, Class<? extends Object> treeElementClass, String childrenAccessor,
			String parentAccessor, String[] valueAccessors, String[] columnHeaders) {
		super.bindToModel(treeRoots, treeElementClass, childrenAccessor, parentAccessor, valueAccessors, columnHeaders);
	}

	// ISortableByColumn methods
	// //////////////////////////

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
