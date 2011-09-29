/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.ui.ridgets.swt.optional.GridRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;

/**
 *
 */
public class GridRidgetTest extends AbstractTableListRidgetTest {

	@Override
	protected Grid createWidget(final Composite parent) {
		final Grid grid = new Grid(parent, SWT.MULTI | SWT.BORDER);
		grid.setHeaderVisible(true);
		new GridColumn(grid, SWT.NONE);
		new GridColumn(grid, SWT.NONE);
		return grid;
	}

	@Override
	protected ITableRidget createRidget() {
		return new GridRidget();
	}

	@Override
	protected Grid getWidget() {
		return (Grid) super.getWidget();
	}

	@Override
	protected GridRidget getRidget() {
		return (GridRidget) super.getRidget();
	}

	@Override
	protected void bindRidgetToModel() {
		getRidget().bindToModel(manager, "persons", Person.class, new String[] { "firstname", "lastname" }, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				new String[] { "First Name", "Last Name" }); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	protected void clearUIControlRowSelection() {
		getWidget().deselectAll();
		fireSelectionEvent();
	}

	@Override
	protected int getUIControlSelectedRowCount() {
		return getWidget().getSelectionCount();
	}

	@Override
	protected int getUIControlSelectedRow() {
		return getWidget().getSelectionIndex();
	}

	@Override
	protected Object getRowValue(final int i) {
		final IObservableList rowObservables = ReflectionUtils.invokeHidden(getRidget(), "getRowObservables"); //$NON-NLS-1$
		return rowObservables.get(i);
	}

	@Override
	protected int[] getSelectedRows() {
		final IObservableList rowObservables = ReflectionUtils.invokeHidden(getRidget(), "getRowObservables"); //$NON-NLS-1$
		final Object[] elements = getRidget().getMultiSelectionObservable().toArray();
		final int[] result = new int[elements.length];
		for (int i = 0; i < elements.length; i++) {
			final Object element = elements[i];
			result[i] = rowObservables.indexOf(element);
		}
		return result;
	}

	@Override
	protected int[] getUIControlSelectedRows() {
		return getWidget().getSelectionIndices();
	}

	@Override
	protected void setUIControlRowSelection(final int[] indices) {
		getWidget().setSelection(indices);
		fireSelectionEvent();
	}

	@Override
	protected void setUIControlRowSelectionInterval(final int start, final int end) {
		getWidget().setSelection(start, end);
		fireSelectionEvent();
	}

	@Override
	protected boolean supportsMulti() {
		return true;
	}

}
