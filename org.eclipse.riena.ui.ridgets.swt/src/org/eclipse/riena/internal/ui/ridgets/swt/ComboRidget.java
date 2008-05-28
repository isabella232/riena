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

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.BindingException;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateListStrategy;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.ridgets.IComboBoxRidget;
import org.eclipse.riena.ui.ridgets.databinding.UnboundPropertyWritableList;
import org.eclipse.riena.ui.ridgets.util.IComboBoxEntryFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;

/**
 * Ridget for {@link Combo} widgets.
 */
public class ComboRidget extends AbstractMarkableRidget implements IComboBoxRidget {

	private final Converter objToStrConverter;
	private final Converter strToObjConverter;
	private final SelectionBindingValidator selectionValidator;

	/* List of Strings (combo items) */
	private IObservableList rowItems;
	/* List of Objects */
	private IObservableList rowObservables;
	/* The current selection */
	private Object selection;
	/* If this item is selected, treat it as if nothing is selected */
	private Object emptySelection;

	/* A string (combo selection) */
	private IObservableValue selectionObservable;

	private Binding listBinding;
	private Binding selectionBinding;

	private String renderingMethod;

	public ComboRidget() {
		super();
		objToStrConverter = new ObjectToStringConverter();
		strToObjConverter = new StringToObjectConverter();
		selectionValidator = new SelectionBindingValidator();
	}

	@Override
	public Combo getUIControl() {
		return (Combo) super.getUIControl();
	}

	@Override
	protected void checkUIControl(Object uiControl) {
		AbstractSWTRidget.assertType(uiControl, Combo.class);
		if (uiControl != null) {
			int style = ((Combo) uiControl).getStyle();
			if ((style & SWT.READ_ONLY) == 0) {
				throw new BindingException("Combo must (currently) be READ_ONLY"); //$NON-NLS-1$
			}
		}
	}

	@Override
	protected void bindUIControl() {
		Combo control = getUIControl();
		if (control != null) {
			rowItems = SWTObservables.observeItems(control);
			selectionObservable = SWTObservables.observeSelection(control);
			updateSelection();
			new SelectionSynchronizer(control);
		}
	}

	@Override
	protected void unbindUIControl() {
		if (listBinding != null) {
			listBinding.dispose();
		}
		if (selectionBinding != null) {
			selectionBinding.dispose();
		}
	}

	@Override
	public void updateFromModel() {
		super.updateFromModel();
		if (listBinding != null && listBinding.getModel() instanceof UnboundPropertyWritableList) {
			((UnboundPropertyWritableList) listBinding.getModel()).updateFromBean();
		}
		// disable the selection binding, because updating the combo items
		// causes the selection to change temporarily
		selectionValidator.enableBinding(false);
		listBinding.updateModelToTarget();
		selectionValidator.enableBinding(true);
		// comboBoxModel.resetValueCache();
		// comboBoxModel.fireContentsChanged(comboBoxModel, 0,
		// comboBoxModel.getSize());
		selectionBinding.updateModelToTarget();
	}

	public void bindToModel(IObservableList listObservableValue, Class<? extends Object> rowBeanClass,
			String renderingMethod, IObservableValue selectionObservableValue) {
		unbindUIControl();

		DataBindingContext dbc = new DataBindingContext();
		rowObservables = listObservableValue;
		listBinding = dbc.bindList(rowItems, rowObservables, new UpdateListStrategy(UpdateListStrategy.POLICY_UPDATE),
				new UpdateListStrategy(UpdateListStrategy.POLICY_ON_REQUEST).setConverter(objToStrConverter));
		selectionBinding = dbc.bindValue(selectionObservable, selectionObservableValue, new UpdateValueStrategy(
				UpdateValueStrategy.POLICY_UPDATE).setConverter(strToObjConverter).setAfterGetValidator(
				selectionValidator), new UpdateValueStrategy(UpdateValueStrategy.POLICY_ON_REQUEST)
				.setConverter(objToStrConverter));
		this.renderingMethod = renderingMethod;
		// comboBoxModel.setData(rowBeans);
	}

	public void bindToModel(Object listBean, String listPropertyName, Class<? extends Object> rowBeanClass,
			String renderingMethod, Object selectionBean, String selectionPropertyName) {
		IObservableList listObservableValue = new UnboundPropertyWritableList(listBean, listPropertyName);
		IObservableValue selectionObservableValue = BeansObservables.observeValue(selectionBean, selectionPropertyName);

		bindToModel(listObservableValue, rowBeanClass, renderingMethod, selectionObservableValue);
	}

	public Object getEmptySelectionItem() {
		return emptySelection;
	}

	public IObservableList getObservableList() {
		return rowItems;
	}

	public Object getSelection() {
		return emptySelection == selection ? null : selection;
	}

	public int getSelectionIndex() {
		int result = -1;
		if (emptySelection != selection) {
			result = rowObservables.indexOf(selection);
		}
		return result;
	}

	/**
	 * @deprecated use BeansObservables.observeValue(this,
	 *             IComboBoxRidget.PROPERTY_SELECTION);
	 */
	public IObservableValue getSelectionObservable() {
		return BeansObservables.observeValue(this, IComboBoxRidget.PROPERTY_SELECTION);
	}

	public void setEmptySelectionItem(Object emptySelectionItem) {
		this.emptySelection = emptySelectionItem;
	}

	public void setSelection(Object newSelection) {
		Object newValue = rowObservables.contains(newSelection) ? newSelection : null;
		if (selection != newValue) {
			Object oldValue = selection;
			selection = newValue;
			updateSelection();
			firePropertyChange(IComboBoxRidget.PROPERTY_SELECTION, oldValue, newValue);
		}
	}

	public void setSelection(int index) {
		if (index == -1) {
			setSelection(null);
		} else {
			Object newSelection = rowObservables.get(index);
			setSelection(newSelection);
		}
	}

	public void setListMutable(boolean mutable) {
		throw new UnsupportedOperationException(); // TODO implement
	}

	public void setEditable(boolean editable) {
		throw new UnsupportedOperationException(); // TODO implement
	}

	public boolean isListMutable() {
		throw new UnsupportedOperationException(); // TODO implement
	}

	public boolean isEditable() {
		throw new UnsupportedOperationException(); // TODO implement
	}

	public void bindToModel(Object listBean, String listPropertyName, Class<? extends Object> rowBeanClass,
			String renderingMethod, Object selectionBean, String selectionPropertyName,
			IComboBoxEntryFactory entryFactory) {
		throw new UnsupportedOperationException(); // TODO implement

	}

	public boolean isAddable() {
		throw new UnsupportedOperationException(); // TODO implement
	}

	public boolean isReadonly() {
		throw new UnsupportedOperationException(); // TODO implement
	}

	public void setAddable(boolean addable) {
		throw new UnsupportedOperationException(); // TODO implement
	}

	public void setReadonly(boolean readonly) {
		throw new UnsupportedOperationException(); // TODO implement
	}

	// helping methods
	// ////////////////

	private Object getItemFromValueUsingRenderingMethod(Object value) {
		return ReflectionUtils.invoke(value, renderingMethod, (Object[]) null);
	}

	private String getItemFromValue(Object value) {
		Object valueObject = value;
		if (value != null && renderingMethod != null) {
			valueObject = getItemFromValueUsingRenderingMethod(value);
		}
		return String.valueOf(valueObject);
	}

	private Object getValueFromItem(String item) {
		String[] items = getUIControl().getItems();
		for (int i = 0; i < items.length; i++) {
			if (items[i].equals(item)) {
				return rowObservables.get(i);
			}
		}
		return item;
	}

	private void updateSelection() {
		Combo uiControl = getUIControl();
		if (uiControl != null) {
			if (selection == null) {
				uiControl.deselectAll();
			} else {
				String itemFromValue = getItemFromValue(selection);
				String[] items = uiControl.getItems();
				for (int i = 0; i < items.length; i++) {
					String item = items[i];
					if (item.equals(itemFromValue)) {
						uiControl.select(i);
						break;
					}
				}
			}
		}
	}

	// helping classes
	// ////////////////

	/**
	 * Convert from model object to combo box items (strings).
	 */
	private class ObjectToStringConverter extends Converter {
		public ObjectToStringConverter() {
			super(Object.class, String.class);
		}

		public Object convert(Object fromObject) {
			return getItemFromValue(fromObject);
		}
	}

	/**
	 * Convert from combo box items (strings) to model objects.
	 */
	private class StringToObjectConverter extends Converter {
		public StringToObjectConverter() {
			super(String.class, Object.class);
		}

		public Object convert(Object fromObject) {
			return getValueFromItem((String) fromObject);
		}
	}

	/**
	 * This class a single {@link IComboBoxRidget#PROPERTY_SELECTION} change
	 * event when the value in the underliyng control changes. This is necessary
	 * because SWT controls:
	 * <ul>
	 * <li>fire modify events when {@link Combo#select(int)} is invoked</li>
	 * <li>fire modify AND selection events when the user selects a combo item</li>
	 * </ul>
	 * 
	 */
	private class SelectionSynchronizer extends SelectionAdapter implements SelectionListener, ModifyListener {

		private int index;

		SelectionSynchronizer(Combo combo) {
			combo.addSelectionListener(this);
			combo.addModifyListener(this);
			index = combo.getSelectionIndex();
		}

		public void widgetSelected(SelectionEvent e) {
			Combo combo = (Combo) e.widget;
			processEvent(combo);
		}

		public void modifyText(ModifyEvent e) {
			Combo combo = (Combo) e.widget;
			processEvent(combo);
		}

		private void processEvent(Combo combo) {
			if (rowObservables != null) {
				int newIndex = combo.getSelectionIndex();
				if (index != newIndex) {
					Object oldValue = index == -1 ? null : rowObservables.get(index);
					Object newValue = newIndex == -1 ? null : rowObservables.get(newIndex);
					index = newIndex;
					// System.out.println("fPC: " + oldValue + " -> " +
					// newValue);
					selection = newValue;
					firePropertyChange(IComboBoxRidget.PROPERTY_SELECTION, oldValue, newValue);
				}
			}
		}
	}

	/**
	 * This validator can be used to interrupt an update request.
	 */
	private class SelectionBindingValidator implements IValidator {

		private boolean isEnabled = true;

		public IStatus validate(Object value) {
			return isEnabled ? Status.OK_STATUS : Status.CANCEL_STATUS;
		}

		void enableBinding(final boolean isEnabled) {
			this.isEnabled = isEnabled;
		}
	}

}
