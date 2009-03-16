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
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.databinding.UnboundPropertyWritableList;
import org.eclipse.riena.ui.swt.MasterDetailsComposite;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;

/**
 * A ridget for the {@link MasterDetailsComposite}.
 */
public class MasterDetailsRidget extends AbstractCompositeRidget implements IMasterDetailsRidget {

	private IObservableList rowObservables;
	private Class<? extends Object> rowBeanClass;
	private int numColumns;

	private IMasterDetailsDelegate delegate;
	private DataBindingContext dbc;

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
		System.out.println("MasterDetailsRidget.setUIControl()");
		AbstractSWTRidget.assertType(uiControl, MasterDetailsComposite.class);
		unbindUIControl();
		super.setUIControl(uiControl);
		bindUIControl();
	}

	public void bindToModel(IObservableList rowBeansObservable, Class<? extends Object> rowBeanClass,
			String[] columnPropertyNames, String[] columnHeaders) {
		System.out.println("MasterDetailsRidget.bindToModel()");

		unbindUIControl();

		this.rowBeanClass = rowBeanClass;
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
		System.out.println("MasterDetailsRidget.configureRidgets()");

		getAddButtonRidget().addListener(new IActionListener() {
			public void callback() {
				clearDetails();
				getUpdateButtonRidget().setEnabled(true);
			}
		});
		getRemoveButtonRidget().addListener(new IActionListener() {
			public void callback() {
				removeSelection();
			}
		});
		getUpdateButtonRidget().addListener(new IActionListener() {
			public void callback() {
				copyFromDetailsToMaster();
			}
		});

		final IObservableValue viewerSelection = getTableRidget().getSingleSelectionObservable();
		IObservableValue hasSelection = new ComputedValue(Boolean.TYPE) {
			@Override
			protected Object calculate() {
				return Boolean.valueOf(viewerSelection.getValue() != null);
			}
		};
		viewerSelection.addValueChangeListener(new IValueChangeListener() {
			public void handleValueChange(ValueChangeEvent event) {
				updateDetails();
			}
		});

		Assert.isLegal(dbc == null);
		dbc = new DataBindingContext();
		bindEnablementToValue(dbc, getRemoveButtonRidget(), hasSelection);
		bindEnablementToValue(dbc, getUpdateButtonRidget(), hasSelection);
	}

	public void addToMaster() {
		assertIsBoundToModel();
		Object newValue = delegate.createWorkingCopy();
		copyBean(getWorkingCopy(), newValue);
		rowObservables.add(newValue);
		getTableRidget().setSelection((Object) newValue);
		MasterDetailsComposite control = getUIControl();
		if (control != null) {
			control.getTable().showSelection();
		}
	}

	public void clearDetails() {
		getTableRidget().clearSelection();
		copyBean(null, getWorkingCopy());
		updateDetails();
	}

	public boolean copyFromDetailsToMaster() {
		Object selection = getSelection();
		if (selection != null) {
			copyBean(getWorkingCopy(), selection);
		} else {
			addToMaster();
		}
		return true;
	}

	public void copyFromMasterToDetails() {
		updateDetails();
	}

	public Object getWorkingCopy() {
		return delegate.getWorkingCopy();
	}

	public boolean isDetailsChanged() {
		// TODO [ev] Auto-generated method stub
		return false;
	}

	public boolean isInputValid() {
		return true;
	}

	public Object getSelection() {
		return getTableRidget().getSingleSelectionObservable().getValue();
	}

	public void setSelection(Object newSelection) {
		getTableRidget().setSelection(newSelection);
	}

	/**
	 * Non API. Public for testing only.
	 */
	public void removeSelection() {
		assertIsBoundToModel();
		Object selection = getSelection();
		Assert.isNotNull(selection);
		rowObservables.remove(selection);
		clearDetails();
	}

	public final Object createWorkingCopy() {
		return delegate.createWorkingCopy();
	}

	public final Object copyBean(final Object source, final Object target) {
		return delegate.copyBean(source, target);
	}

	public final void updateDetails() {
		Object selection = getSelection();
		copyBean(selection, getWorkingCopy());
		delegate.updateDetails(this);
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
	///////////////////

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
		System.out.println("MasterDetailsRidget.bindUIControl()");
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

}
