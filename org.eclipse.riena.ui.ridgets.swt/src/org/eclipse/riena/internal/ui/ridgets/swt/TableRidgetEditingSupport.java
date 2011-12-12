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

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener;
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.ui.ridgets.IEditableRidget;
import org.eclipse.riena.ui.ridgets.IValueRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Editing support fpr tables with data binding and Ridgets.
 */
public class TableRidgetEditingSupport extends EditingSupport {

	private final AbstractTableRidget tableRidget;
	private final PropertyDescriptor property;
	private final CellEditor cellEditor;
	private final ColumnViewerEditorActivationListenerHelper activationListener;
	private IValueRidget valueRidget;

	public TableRidgetEditingSupport(final AbstractTableRidget tableRidget, final PropertyDescriptor property,
			final int columnStyle) {
		super(tableRidget.getTableViewer());
		this.tableRidget = tableRidget;
		this.property = property;
		cellEditor = createCellEditort(columnStyle);
		activationListener = new ColumnViewerEditorActivationListenerHelper();
	}

	private CellEditor createCellEditort(final int style) {
		if (property == null) {
			return null;
		}
		final Composite table = (Composite) getViewer().getControl();
		final Class<?> type = property.getPropertyType();

		if (Boolean.class.equals(type) || Boolean.TYPE.equals(type)) {
			// final BooleanCellEditor editor = new BooleanCellEditor(table, style);
			// editor.setChangeOnActivation(true);
			// return editor;
			return null;
		} else {
			final TextCellEditor editor = new TextCellEditor(table, style);
			if (Integer.class.equals(type) || Integer.TYPE.equals(type)) {
				editor.getControl().setData(UIControlsFactory.KEY_TYPE, UIControlsFactory.TYPE_NUMERIC);
			} else if (Short.class.equals(type) || Short.TYPE.equals(type)) {
				editor.getControl().setData(UIControlsFactory.KEY_TYPE, UIControlsFactory.TYPE_NUMERIC);
			} else if (Long.class.equals(type) || Long.TYPE.equals(type)) {
				editor.getControl().setData(UIControlsFactory.KEY_TYPE, UIControlsFactory.TYPE_NUMERIC);
			} else if (BigInteger.class.equals(type)) {
				editor.getControl().setData(UIControlsFactory.KEY_TYPE, UIControlsFactory.TYPE_NUMERIC);
			} else if (Float.class.equals(type) || Float.TYPE.equals(type)) {
				editor.getControl().setData(UIControlsFactory.KEY_TYPE, UIControlsFactory.TYPE_DECIMAL);
			} else if (Double.class.equals(type) || Double.TYPE.equals(type)) {
				editor.getControl().setData(UIControlsFactory.KEY_TYPE, UIControlsFactory.TYPE_DECIMAL);
			} else if (BigDecimal.class.equals(type)) {
				editor.getControl().setData(UIControlsFactory.KEY_TYPE, UIControlsFactory.TYPE_DECIMAL);
			} else if (Date.class.equals(type)) {
				editor.getControl().setData(UIControlsFactory.KEY_TYPE, UIControlsFactory.TYPE_DECIMAL);
			}
			return editor;

		}

	}

	@Override
	protected CellEditor getCellEditor(final Object element) {
		return cellEditor;
	}

	@Override
	protected void initializeCellEditorValue(final CellEditor cellEditor, final ViewerCell cell) {

		valueRidget = (IValueRidget) SwtRidgetFactory.createRidget(cellEditor.getControl());

		final Object valueHolder = cell.getElement();
		valueRidget.bindToModel(valueHolder, property.getName());
		valueRidget.updateFromModel();

		getViewer().getColumnViewerEditor().addEditorActivationListener(activationListener);

	}

	@Override
	protected void saveCellEditorValue(final CellEditor cellEditor, final ViewerCell cell) {
		if (valueRidget instanceof IEditableRidget) {
			((IEditableRidget) valueRidget).revalidate();
		} else if (valueRidget instanceof ToggleButtonRidget) {
			((ToggleButtonRidget) valueRidget).getValueBindingSupport().updateFromTarget();
		}
	}

	@Override
	protected boolean canEdit(final Object element) {
		return getCellEditor(element) != null;
	}

	@Override
	protected Object getValue(final Object element) {
		return null;
	}

	@Override
	protected void setValue(final Object element, final Object value) {
	}

	private class ColumnViewerEditorActivationListenerHelper extends ColumnViewerEditorActivationListener {

		@Override
		public void beforeEditorActivated(final ColumnViewerEditorActivationEvent event) {
		}

		@Override
		public void afterEditorActivated(final ColumnViewerEditorActivationEvent event) {
		}

		@Override
		public void beforeEditorDeactivated(final ColumnViewerEditorDeactivationEvent event) {
		}

		@Override
		public void afterEditorDeactivated(final ColumnViewerEditorDeactivationEvent event) {

			//			if (event.eventType == ColumnViewerEditorDeactivationEvent.EDITOR_SAVED) {
			//			} else if (event.eventType == ColumnViewerEditorDeactivationEvent.EDITOR_CANCELED) {
			//			}

			tableRidget.updateFromModel();

			if (valueRidget != null) {
				valueRidget.setUIControl(null);
			}
			valueRidget = null;
		}

	}

}
