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
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.BindingException;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import org.eclipse.riena.internal.ui.ridgets.swt.TableRidget.ITableRidgetDelegate;
import org.eclipse.riena.ui.ridgets.AbstractCompositeRidget;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.IMasterDetailsDelegate;
import org.eclipse.riena.ui.ridgets.IMasterDetailsRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractSWTRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractSWTWidgetRidget;
import org.eclipse.riena.ui.swt.MasterDetailsComposite;

/**
 * A ridget for the {@link MasterDetailsComposite}.
 */
public class MasterDetailsRidget extends AbstractCompositeRidget implements IMasterDetailsRidget {

	private IObservableList rowObservables;

	private IMasterDetailsDelegate delegate;
	private DataBindingContext dbc;

	/*
	 * The object we are currently editing; null if not editing
	 */
	private Object editable;

	/*
	 * Markable ridgets from the details area.
	 */
	private List<IMarkableRidget> detailRidgets;

	public MasterDetailsRidget() {
		addPropertyChangeListener(null, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (delegate == null
						|| editable == null
						// ignore these events:
						|| IMarkableRidget.PROPERTY_MARKER.equals(evt.getPropertyName())
						|| IRidget.PROPERTY_ENABLED.equals(evt.getPropertyName())
						|| IMarkableRidget.PROPERTY_OUTPUT_ONLY.equals(evt.getPropertyName())) {
					return;
				}
				boolean isChanged = delegate.isChanged(editable, delegate.getWorkingCopy());
				// System.out.println(isChanged + " : " + evt.getPropertyName() + " " + evt.getNewValue());
				getApplyButtonRidget().setEnabled(isChanged);
			}
		});
	}

	public void setDelegate(IMasterDetailsDelegate delegate) {
		Assert.isLegal(this.delegate == null, "setDelegate can only be called once"); //$NON-NLS-1$
		Assert.isLegal(delegate != null, "delegate cannot be null"); //$NON-NLS-1$
		this.delegate = delegate;
		delegate.configureRidgets(this);
	}

	/**
	 * Non API (not part of the interface); public for testing only.
	 */
	public IMasterDetailsDelegate getDelegate() {
		return this.delegate;
	}

	@Override
	public MasterDetailsComposite getUIControl() {
		return (MasterDetailsComposite) super.getUIControl();
	}

	@Override
	public void setUIControl(Object uiControl) {
		AbstractSWTRidget.assertType(uiControl, MasterDetailsComposite.class);
		super.setUIControl(uiControl);
		if (uiControl != null) {
			// TODO [ev] experimental code:
			//			final Table table = ((MasterDetailsComposite) uiControl).getTable();
			//			table.addSelectionListener(new SelectionAdapter() {
			//				private int oldIndex = -1;
			//
			//				public void widgetSelected(SelectionEvent e) {
			//					if (oldIndex > 4) {
			//						System.out.println("oldIndex= " + oldIndex + " new: " + oldIndex);
			//						table.setSelection(oldIndex);
			//					} else {
			//						System.out.println("oldIndex= " + oldIndex + " new: " + table.getSelectionIndex());
			//						oldIndex = table.getSelectionIndex();
			//					}
			//				}
			//			});
		}
	}

	public void bindToModel(IObservableList rowObservables, Class<? extends Object> rowClass,
			String[] columnPropertyNames, String[] columnHeaders) {
		this.rowObservables = rowObservables;
		getTableRidget().bindToModel(this.rowObservables, rowClass, columnPropertyNames, columnHeaders);
	}

	public void bindToModel(Object listHolder, String listPropertyName, Class<? extends Object> rowClass,
			String[] columnPropertyNames, String[] headerNames) {
		IObservableList rowObservableList;
		if (AbstractSWTWidgetRidget.isBean(rowClass)) {
			rowObservableList = BeansObservables.observeList(listHolder, listPropertyName);
		} else {
			rowObservableList = PojoObservables.observeList(listHolder, listPropertyName);
		}
		bindToModel(rowObservableList, rowClass, columnPropertyNames, headerNames);
	}

	@Override
	public final void configureRidgets() {
		((TableRidget) getTableRidget()).setDelegate(new TablePreparer());

		getNewButtonRidget().addListener(new IActionListener() {
			public void callback() {
				if (canAdd()) {
					handleAdd();
				}
			}
		});
		getRemoveButtonRidget().addListener(new IActionListener() {
			public void callback() {
				handleRemove();
			}
		});
		getApplyButtonRidget().addListener(new IActionListener() {
			public void callback() {
				if (canApply()) {
					handleApply();
				}
			}
		});

		detailRidgets = getDetailRidgets();
		setEnabled(false, false);

		final IObservableValue viewerSelection = getTableRidget().getSingleSelectionObservable();
		viewerSelection.addValueChangeListener(new IValueChangeListener() {
			public void handleValueChange(ValueChangeEvent event) {
				handleSelectionChange(event.getObservableValue().getValue());
			}
		});

		Assert.isLegal(dbc == null);
		dbc = new DataBindingContext();
		bindEnablementToValue(dbc, getRemoveButtonRidget(), new ComputedValue(Boolean.TYPE) {
			@Override
			protected Object calculate() {
				return Boolean.valueOf(viewerSelection.getValue() != null);
			}
		});
	}

	public Object getSelection() {
		return getTableRidget().getSingleSelectionObservable().getValue();
	}

	public void setSelection(Object newSelection) {
		getTableRidget().setSelection(newSelection);
		MasterDetailsComposite control = getUIControl();
		if (control != null) {
			control.getTable().showSelection();
		}
	}

	@Override
	public void updateFromModel() {
		super.updateFromModel();
		ITableRidget tableRidget = getTableRidget();
		if (tableRidget != null) {
			tableRidget.updateFromModel();
		}
	}

	// protected methods
	//////////////////

	@Override
	protected final void updateToolTipText() {
		MasterDetailsComposite control = getUIControl();
		if (control != null) {
			control.setToolTipText(getToolTipText());
		}
	}

	@Override
	protected final void updateVisible() {
		MasterDetailsComposite control = getUIControl();
		if (control != null) {
			control.setVisible(!markedHidden);
		}
	}

	@Override
	protected boolean isUIControlVisible() {
		return getUIControl().isVisible();
	}

	@Override
	protected final void updateEnabled() {
		MasterDetailsComposite control = getUIControl();
		if (control != null) {
			control.setEnabled(isEnabled());
		}
	}

	// helping methods
	//////////////////

	private void assertIsBoundToModel() {
		if (rowObservables == null) {
			throw new BindingException("ridget not bound to model"); //$NON-NLS-1$
		}
	}

	private void bindEnablementToValue(DataBindingContext dbc, IRidget ridget, IObservableValue value) {
		Assert.isNotNull(ridget);
		Assert.isNotNull(value);
		dbc.bindValue(BeansObservables.observeValue(ridget, IRidget.PROPERTY_ENABLED), value, null, null);
	}

	private boolean canAdd() {
		boolean result = true;
		boolean isChanged = delegate.isChanged(editable, delegate.getWorkingCopy());
		if (isChanged) {
			result = getUIControl().confirmDiscardChanges();
		}
		return result;
	}

	private boolean canApply() {
		String reason = delegate.isValid(this);
		if (reason != null) {
			getUIControl().warnApplyFailed(reason);
		}
		return reason == null;
	}

	private void clearSelection() {
		updateDetails(delegate.createWorkingCopy());
		editable = null;
	}

	private ITableRidget getTableRidget() {
		return (ITableRidget) getRidget(MasterDetailsComposite.BIND_ID_TABLE);
	}

	private IActionRidget getNewButtonRidget() {
		return (IActionRidget) getRidget(MasterDetailsComposite.BIND_ID_NEW);
	}

	private IActionRidget getRemoveButtonRidget() {
		return (IActionRidget) getRidget(MasterDetailsComposite.BIND_ID_REMOVE);
	}

	private IActionRidget getApplyButtonRidget() {
		return (IActionRidget) getRidget(MasterDetailsComposite.BIND_ID_APPLY);
	}

	private List<IMarkableRidget> getDetailRidgets() {
		List<IMarkableRidget> result = new ArrayList<IMarkableRidget>();
		for (IRidget ridget : getRidgets()) {
			if (ridget instanceof IMarkableRidget) {
				result.add((IMarkableRidget) ridget);
			}
		}
		result.remove(getNewButtonRidget());
		result.remove(getRemoveButtonRidget());
		result.remove(getApplyButtonRidget());
		result.remove(getTableRidget());
		return result;
	}

	private void setEnabled(boolean applyEnabled, boolean detailsEnabled) {
		getApplyButtonRidget().setEnabled(applyEnabled);
		for (IMarkableRidget ridget : detailRidgets) {
			ridget.setEnabled(detailsEnabled);
		}
	}

	private void updateDetails(Object bean) {
		Assert.isNotNull(bean);
		delegate.copyBean(bean, delegate.getWorkingCopy());
		delegate.updateDetails(this);
	}

	/**
	 * Non API; public for testing only.
	 */
	public void handleAdd() {
		getTableRidget().clearSelection();
		editable = delegate.createWorkingCopy();
		setEnabled(false, true);
		updateDetails(editable);
		getUIControl().getDetails().setFocus();
	}

	/**
	 * Non API; public for testing only.
	 */
	public void handleRemove() {
		assertIsBoundToModel();
		Object selection = getSelection();
		Assert.isNotNull(selection);
		rowObservables.remove(selection);
		getTableRidget().clearSelection();
		clearSelection();
		setEnabled(false, false);
	}

	/**
	 * Non API; public for testing only.
	 */
	public void handleApply() {
		assertIsBoundToModel();
		Assert.isNotNull(editable);
		delegate.copyBean(delegate.getWorkingCopy(), editable);
		if (!rowObservables.contains(editable)) { // add to table
			rowObservables.add(editable);
			getTableRidget().updateFromModel();
			setSelection(editable);
		} else { // update
			getTableRidget().updateFromModel();
		}
		setEnabled(false, false);
		Table table = getUIControl().getTable();
		/*
		 * Fix for bug 283694: if only one element is in the table, remove the
		 * selection on apply, so it can be selected again for editing.
		 */
		if (table.getItemCount() == 1) {
			getTableRidget().clearSelection();
		} else {
			table.select(table.getSelectionIndex());
		}
		table.setFocus();
	}

	private void handleSelectionChange(Object newSelection) {
		if (newSelection != null) { // selection changed
			editable = newSelection;
			setEnabled(false, true);
			updateDetails(editable);
		} else { // nothing selected
			clearSelection();
			setEnabled(false, false);
		}
	}

	// helping classes
	//////////////////

	private static final class TablePreparer implements ITableRidgetDelegate {
		public void prepareTable(Table control, int expectedCols) {
			int actualCols = control.getColumnCount() == 0 ? 1 : control.getColumnCount();
			if (actualCols != expectedCols) {
				for (TableColumn column : control.getColumns()) {
					column.dispose();
				}
				TableColumnLayout layout = new TableColumnLayout();
				for (int i = 0; i < expectedCols; i++) {
					TableColumn column = new TableColumn(control, SWT.NONE);
					layout.setColumnData(column, new ColumnWeightData(10));
				}
				Composite tableComposite = control.getParent();
				tableComposite.setLayout(layout);
				tableComposite.layout(true);
			}
		}
	}

}
