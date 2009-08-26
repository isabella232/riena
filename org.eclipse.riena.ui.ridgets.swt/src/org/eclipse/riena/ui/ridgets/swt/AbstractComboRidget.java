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

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.BindingException;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateListStrategy;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.ui.ridgets.swt.MarkerSupport;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.IRidget;

/**
 * Superclass of ComboRidget that does not depend on the Combo SWT control. May
 * be reused for custom Combo controls.
 */
public abstract class AbstractComboRidget extends AbstractSWTRidget implements IComboRidget {
	/** List of available options (ridget). */
	private final IObservableList rowObservables;
	/** The selected option (ridget). */
	private final IObservableValue selectionObservable;
	/**
	 * Converts from objects (rowObsservables) to strings (Combo) using the
	 * renderingMethod.
	 */
	private final Converter objToStrConverter;
	/** Convers from strings (Combo) to objects (rowObservables). */
	private final Converter strToObjConverter;
	/** Selection validator that allows or cancels a selection request. */
	private final SelectionBindingValidator selectionValidator;
	/** IValueChangeListener that allows or cancels a value change */
	private final IValueChangeListener valueChangeValidator;

	/** If this item is selected, treat it as if nothing is selected */
	private Object emptySelection;

	/** List of available options (model). */
	private IObservableList optionValues;
	/** The selected option (model). */
	private IObservableValue selectionValue;
	/** A string used for converting from Object to String */
	private String renderingMethod;

	/**
	 * Binding between the list of choices in the combo and the rowObservables.
	 * May be null, when there is no control or model.
	 */
	private Binding listBindingInternal;
	/**
	 * Binding between the rowObservables and the list of choices from the
	 * model. May be null, when there is no model.
	 */
	private Binding listBindingExternal;
	/**
	 * Binding between the selection in the combo and the selectionObservable.
	 * May be null, when there is no control or model.
	 */
	private Binding selectionBindingInternal;
	/**
	 * Binding between the selectionObservable and the selection in the model.
	 * May be null, when there is no model.
	 */
	private Binding selectionBindingExternal;

	public AbstractComboRidget() {
		super();
		rowObservables = new WritableList();
		selectionObservable = new WritableValue();
		selectionObservable.addValueChangeListener(new IValueChangeListener() {
			public void handleValueChange(ValueChangeEvent event) {
				Object oldValue = event.diff.getOldValue();
				Object newValue = event.diff.getNewValue();
				firePropertyChange(IComboRidget.PROPERTY_SELECTION, oldValue, newValue);
				disableMandatoryMarkers(hasInput());
			}
		});
		objToStrConverter = new ObjectToStringConverter();
		strToObjConverter = new StringToObjectConverter();
		selectionValidator = new SelectionBindingValidator();
		valueChangeValidator = new ValueChangeValidator();
		addPropertyChangeListener(IRidget.PROPERTY_ENABLED, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				applyEnabled();
			}
		});
	}

	@Override
	protected void bindUIControl() {
		if (optionValues != null) {
			// These bindings are only necessary when we have a model
			DataBindingContext dbc = new DataBindingContext();
			if (getUIControl() != null) {
				// These bindings are only necessary when we have a Combo
				listBindingInternal = dbc.bindList(getUIControlItemsObservable(), rowObservables,
						new UpdateListStrategy(UpdateListStrategy.POLICY_ON_REQUEST).setConverter(strToObjConverter),
						new UpdateListStrategy(UpdateValueStrategy.POLICY_ON_REQUEST).setConverter(objToStrConverter));
				listBindingInternal.updateModelToTarget();
				applyEnabled();
			}
			listBindingExternal = dbc
					.bindList(rowObservables, optionValues,
							new UpdateListStrategy(UpdateListStrategy.POLICY_ON_REQUEST), new UpdateListStrategy(
									UpdateListStrategy.POLICY_ON_REQUEST));
			selectionBindingExternal = dbc.bindValue(selectionObservable, selectionValue, new UpdateValueStrategy(
					UpdateValueStrategy.POLICY_UPDATE).setAfterGetValidator(selectionValidator),
					new UpdateValueStrategy(UpdateValueStrategy.POLICY_ON_REQUEST));

		}
	}

	@Override
	protected void unbindUIControl() {
		super.unbindUIControl();
		disposeBinding(listBindingInternal);
		listBindingInternal = null;
		disposeBinding(listBindingExternal);
		listBindingExternal = null;
		disposeBinding(selectionBindingInternal);
		selectionBindingInternal = null;
		disposeBinding(selectionBindingExternal);
		selectionBindingExternal = null;
	}

	@Override
	public void updateFromModel() {
		assertIsBoundToModel();
		super.updateFromModel();
		// disable the selection binding, because updating the combo items
		// causes the selection to change temporarily
		selectionValidator.enableBinding(false);
		listBindingExternal.updateModelToTarget();
		if (listBindingInternal != null) {
			listBindingInternal.updateModelToTarget();
		}
		selectionValidator.enableBinding(true);
		selectionBindingExternal.updateModelToTarget();
		if (selectionBindingInternal != null) {
			selectionBindingInternal.updateModelToTarget();
		}
	}

	public void bindToModel(IObservableList optionValues, Class<? extends Object> rowClass, String renderingMethod,
			IObservableValue selectionValue) {
		unbindUIControl();

		this.optionValues = optionValues;
		this.renderingMethod = renderingMethod;
		this.selectionValue = selectionValue;

		bindUIControl();
	}

	public void bindToModel(Object listHolder, String listPropertyName, Class<? extends Object> rowClass,
			String renderingMethod, Object selectionHolder, String selectionPropertyName) {
		IObservableList listObservableValue;
		if (AbstractSWTWidgetRidget.isBean(rowClass)) {
			listObservableValue = BeansObservables.observeList(listHolder, listPropertyName);
		} else {
			listObservableValue = PojoObservables.observeList(listHolder, listPropertyName);
		}
		IObservableValue selectionObservableValue = PojoObservables
				.observeValue(selectionHolder, selectionPropertyName);
		bindToModel(listObservableValue, rowClass, renderingMethod, selectionObservableValue);
	}

	public Object getEmptySelectionItem() {
		return emptySelection;
	}

	// TODO [ev] should method return null when not bound? See ListRidget#getObservableList()
	public IObservableList getObservableList() {
		return rowObservables;
	}

	public Object getSelection() {
		Object selection = selectionObservable.getValue();
		return selection == emptySelection ? null : selection;
	}

	public int getSelectionIndex() {
		int result = -1;
		Object selection = selectionObservable.getValue();
		if (emptySelection != selection) {
			result = rowObservables.indexOf(selection);
		}
		return result;
	}

	/** Not implemented. */
	public boolean isAddable() {
		throw new UnsupportedOperationException(); // TODO implement
	}

	@Override
	public boolean isDisableMandatoryMarker() {
		return hasInput();
	}

	/** Not implemented. */
	public boolean isListMutable() {
		throw new UnsupportedOperationException(); // TODO implement
	}

	/** Not implemented. */
	public boolean isReadonly() {
		throw new UnsupportedOperationException(); // TODO implement
	}

	/** Not implemented. */
	public void setAddable(boolean addable) {
		throw new UnsupportedOperationException(); // TODO implement
	}

	public void setEmptySelectionItem(Object emptySelection) {
		this.emptySelection = emptySelection;
	}

	/** Not implemented. */
	public void setListMutable(boolean mutable) {
		throw new UnsupportedOperationException(); // TODO implement
	}

	/** Not implemented. */
	public void setReadonly(boolean readonly) {
		throw new UnsupportedOperationException(); // TODO implement
	}

	public void setSelection(Object newSelection) {
		assertIsBoundToModel();
		Object oldSelection = selectionObservable.getValue();
		if (oldSelection != newSelection) {
			if (newSelection == null || !rowObservables.contains(newSelection)) {
				if (getUIControl() != null) {
					clearUIControlListSelection();
				}
				selectionObservable.setValue(null);
			} else {
				selectionObservable.setValue(newSelection);
			}
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

	// helping methods
	// ////////////////

	/**
	 * @return Returns an observable observing the items attribute of the
	 *         control.
	 */
	protected abstract IObservableList getUIControlItemsObservable();

	/**
	 * @return Returns an observable observing the selection attribute of the
	 *         control.
	 */
	protected abstract ISWTObservableValue getUIControlSelectionObservable();

	/**
	 * Deselects all selected items in the controls list.
	 */
	protected abstract void clearUIControlListSelection();

	private void applyEnabled() {
		if (isEnabled()) {
			bindControlToSelectionAndUpdate();
		} else {
			unbindControlFromSelectionAndClear();
		}
	}

	private void assertIsBoundToModel() {
		if (optionValues == null) {
			throw new BindingException("ridget not bound to model"); //$NON-NLS-1$
		}
	}

	/**
	 * Restores the list of items / selection in the combo, when the ridget is
	 * enabled.
	 */
	private void bindControlToSelectionAndUpdate() {
		if (getUIControl() != null && listBindingInternal != null) {
			/* update list of items in combo */
			listBindingInternal.updateModelToTarget();
			/* re-create selectionBinding */
			ISWTObservableValue controlSelection = getUIControlSelectionObservable();
			controlSelection.addValueChangeListener(valueChangeValidator);
			DataBindingContext dbc = new DataBindingContext();
			selectionBindingInternal = dbc.bindValue(controlSelection, selectionObservable, new UpdateValueStrategy(
					UpdateValueStrategy.POLICY_UPDATE).setConverter(strToObjConverter).setAfterGetValidator(
					selectionValidator), new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE)
					.setConverter(objToStrConverter));
			/* update selection in combo */
			selectionBindingInternal.updateModelToTarget();
		}
	}

	private void disposeBinding(Binding binding) {
		if (binding != null && !binding.isDisposed()) {
			binding.dispose();
		}
	}

	private String getItemFromValue(Object value) {
		Object valueObject = value;
		if (value != null && renderingMethod != null) {
			valueObject = ReflectionUtils.invoke(value, renderingMethod, (Object[]) null);
		}
		if (valueObject == null || valueObject.toString() == null) {
			throw new NullPointerException("The item value for a model element is null"); //$NON-NLS-1$
		}
		return valueObject.toString();
	}

	private Object getValueFromItem(String item) {
		String[] items = getUIControlItems();
		for (int i = 0; i < items.length; i++) {
			if (items[i].equals(item)) {
				return rowObservables.get(i);
			}
		}
		return item;
	}

	/**
	 * @return The items of the controls list. May be an empty array.
	 */
	protected abstract String[] getUIControlItems();

	private boolean hasInput() {
		Object selection = selectionObservable.getValue();
		return selection != null && selection != emptySelection;
	}

	/**
	 * Clears the list of items in the combo, when the ridget is disabled.
	 */
	private void unbindControlFromSelectionAndClear() {
		if (getUIControl() != null && !isEnabled()) {
			/* dispose selectionBinding to avoid sync */
			disposeBinding(selectionBindingInternal);
			selectionBindingInternal = null;
			/* clear combo */
			if (MarkerSupport.HIDE_DISABLED_RIDGET_CONTENT) {
				removeAllFromUIControl();
			}
		}
	}

	/**
	 * Removes all of the items from the controls list and clears the controls
	 * text field.
	 */
	protected abstract void removeAllFromUIControl();

	/**
	 * @return The index of the item in the controls list or -1 if no such item
	 *         is found.
	 */
	protected abstract int indexOfInUIControl(String item);

	/**
	 * Selects the item in the controls list.
	 */
	protected abstract void selectInUIControl(int index);

	// helping classes
	// ////////////////

	/**
	 * Convert from model object to combo box items (strings).
	 */
	private final class ObjectToStringConverter extends Converter {
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
	private final class StringToObjectConverter extends Converter {
		public StringToObjectConverter() {
			super(String.class, Object.class);
		}

		public Object convert(Object fromObject) {
			return getValueFromItem((String) fromObject);
		}
	}

	/**
	 * This validator can be used to interrupt an update request
	 */
	private final class SelectionBindingValidator implements IValidator {

		private boolean isEnabled = true;

		public IStatus validate(Object value) {
			IStatus result = Status.OK_STATUS;
			// disallow control to ridget update, isEnabled == false || output
			if (!isEnabled || isOutputOnly()) {
				result = Status.CANCEL_STATUS;
			}
			return result;
		}

		void enableBinding(final boolean isEnabled) {
			this.isEnabled = isEnabled;
		}
	}

	/**
	 * Ensures the user cannot change the Combo when isOutputOnly is enabled.
	 * 
	 * @see IMarkableRidget#setOutputOnly(boolean)
	 */
	private final class ValueChangeValidator implements IValueChangeListener {

		private volatile boolean changing = false;

		public void handleValueChange(ValueChangeEvent event) {
			if (changing || !isOutputOnly()) {
				return;
			}
			changing = true;
			try {
				String oldValue = (String) event.diff.getOldValue();
				int index = oldValue != null ? indexOfInUIControl(oldValue) : -1;
				if (index > -1) {
					selectInUIControl(index);
				} else {
					clearUIControlListSelection();
				}
			} finally {
				changing = false;
			}
		}

	}

}
