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
package org.eclipse.riena.ui.ridgets.swt;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.BindingException;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidget;
import org.eclipse.riena.ui.ridgets.AbstractCompositeRidget;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.IMasterDetailsDelegate;
import org.eclipse.riena.ui.ridgets.IMasterDetailsRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.databinding.UnboundPropertyWritableList;
import org.eclipse.riena.ui.swt.MasterDetailsComposite;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;

/**
 * A ridget for the {@link MasterDetailsComposite}.
 */
public class MasterDetailsRidget extends AbstractCompositeRidget implements IMasterDetailsRidget {

	private IObservableList rowObservables;
	private int numColumns;

	private IMasterDetailsDelegate delegate;
	private DataBindingContext dbc;

	/*
	 * The object we are currently editing; null if not editing
	 */
	private Object editable;

	public MasterDetailsRidget() {
		addPropertyChangeListener(null, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				// ignore these events
				if (delegate == null || editable == null
						|| ISelectableRidget.PROPERTY_SELECTION.equals(evt.getPropertyName())
						|| IMarkableRidget.PROPERTY_MARKER.equals(evt.getPropertyName())
						|| IMarkableRidget.PROPERTY_ENABLED.equals(evt.getPropertyName())
						|| IMarkableRidget.PROPERTY_OUTPUT_ONLY.equals(evt.getPropertyName())) {
					return;
				}
				// TODO [ev] quire the delagate - there's an ordering bug in the text ridget
				// that prevents this from working
				// boolean isChanged = delegate.isChanged(editable, delegate.getWorkingCopy());
				// getUpdateButtonRidget().setEnabled(isChanged);
				getUpdateButtonRidget().setEnabled(true);
			}
		});
	}

	public void setDelegate(IMasterDetailsDelegate delegate) {
		Assert.isLegal(this.delegate == null, "setDelegate can only be called once"); //$NON-NLS-1$
		Assert.isLegal(delegate != null, "delegate cannot be null"); //$NON-NLS-1$
		this.delegate = delegate;
		delegate.configureRidgets(this);
	}

	@Override
	public MasterDetailsComposite getUIControl() {
		return (MasterDetailsComposite) super.getUIControl();
	}

	@Override
	public void setUIControl(Object uiControl) {
		AbstractSWTRidget.assertType(uiControl, MasterDetailsComposite.class);
		unbindUIControl();
		super.setUIControl(uiControl);
		bindUIControl();
	}

	public void bindToModel(IObservableList rowBeansObservable, Class<? extends Object> rowBeanClass,
			String[] columnPropertyNames, String[] columnHeaders) {
		unbindUIControl();

		rowObservables = rowBeansObservable;
		numColumns = columnPropertyNames.length;
		getTableRidget().bindToModel(rowObservables, rowBeanClass, columnPropertyNames, columnHeaders);

		bindUIControl();
	}

	public void bindToModel(Object listBean, String listPropertyName, Class<? extends Object> rowBeanClass,
			String[] columnPropertyNames, String[] headerNames) {
		IObservableList listObservableValue = new UnboundPropertyWritableList(listBean, listPropertyName);
		bindToModel(listObservableValue, rowBeanClass, columnPropertyNames, headerNames);
	}

	@Override
	public final void configureRidgets() {
		getAddButtonRidget().addListener(new IActionListener() {
			public void callback() {
				handleAdd();
			}
		});
		getRemoveButtonRidget().addListener(new IActionListener() {
			public void callback() {
				handleRemove();
			}
		});
		getUpdateButtonRidget().addListener(new IActionListener() {
			public void callback() {
				handleUpdate();
			}
		});
		getUpdateButtonRidget().setEnabled(false);

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

	public boolean isDetailsChanged() {
		// TODO [ev] Auto-generated method stub
		return false;
	}

	public boolean isInputValid() {
		// TODO [ev] Auto-generated method stub
		return true;
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
			control.setVisible(isVisible());
		}
	}

	// helping methods
	//////////////////

	private void assertIsBoundToModel() {
		if (rowObservables == null) {
			throw new BindingException("ridget not bound to model"); //$NON-NLS-1$
		}
	}

	private void bindEnablementToValue(DataBindingContext dbc, IMarkableRidget ridget, IObservableValue value) {
		Assert.isNotNull(ridget);
		Assert.isNotNull(value);
		dbc.bindValue(BeansObservables.observeValue(ridget, IMarkableRidget.PROPERTY_ENABLED), value, null, null);
	}

	private void bindUIControl() {
		MasterDetailsComposite mdComposite = getUIControl();
		if (mdComposite != null && rowObservables != null) {
			prepareTable(mdComposite);
			for (Object element : mdComposite.getUIControls()) {
				Control control = (Control) element;
				String bindingProperty = SWTBindingPropertyLocator.getInstance().locateBindingProperty(control);
				getRidget(bindingProperty).setUIControl(control);
			}
		}
	}

	private void clearSelection() {
		updateDetails(delegate.createWorkingCopy());
		editable = null;
		getUpdateButtonRidget().setEnabled(false);
	}

	private void unbindUIControl() {
		for (IRidget ridget : getRidgets()) {
			ridget.setUIControl(null);
		}
	}

	private ITableRidget getTableRidget() {
		return (ITableRidget) getRidget(MasterDetailsComposite.BIND_ID_TABLE);
	}

	private IActionRidget getAddButtonRidget() {
		return (IActionRidget) getRidget(MasterDetailsComposite.BIND_ID_ADD);
	}

	private IActionRidget getRemoveButtonRidget() {
		return (IActionRidget) getRidget(MasterDetailsComposite.BIND_ID_REMOVE);
	}

	private IActionRidget getUpdateButtonRidget() {
		return (IActionRidget) getRidget(MasterDetailsComposite.BIND_ID_UPDATE);
	}

	private void prepareTable(MasterDetailsComposite control) {
		Table tableWidget = control.getTable();
		if (tableWidget.getColumnCount() != numColumns) {
			for (TableColumn column : tableWidget.getColumns()) {
				column.dispose();
			}
			TableColumnLayout layout = new TableColumnLayout();
			for (int i = 0; i < numColumns; i++) {
				TableColumn column = new TableColumn(tableWidget, SWT.NONE);
				layout.setColumnData(column, new ColumnWeightData(10));
			}
			Composite tableComposite = tableWidget.getParent();
			tableComposite.setLayout(layout);
			tableComposite.layout(true);
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
		updateDetails(editable);
		getUpdateButtonRidget().setEnabled(false);
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
	}

	/**
	 * Non API; public for testing only.
	 */
	public void handleUpdate() {
		assertIsBoundToModel();
		Assert.isNotNull(editable);
		delegate.copyBean(delegate.getWorkingCopy(), editable);
		if (!rowObservables.contains(editable)) { // add
			rowObservables.add(editable);
			setSelection(editable);
		} else { // update
			getUpdateButtonRidget().setEnabled(false);
		}
	}

	private void handleSelectionChange(Object newSelection) {
		if (newSelection != null) {
			editable = newSelection;
			updateDetails(editable);
			getUpdateButtonRidget().setEnabled(false);
		} else {
			clearSelection();
		}
	}

}
