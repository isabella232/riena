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

import java.util.List;

import org.eclipse.core.databinding.BindingException;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.ValueDiff;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.runtime.Assert;
import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.ridgets.IChoiceRidget;
import org.eclipse.riena.ui.ridgets.ISingleChoiceRidget;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * TODO [ev] docs
 */
public class SingleChoiceRidget extends AbstractMarkableRidget implements ISingleChoiceRidget {

	/** A single selected bean Object. */
	private final WritableValue singleSelectionObservable;

	private IObservableList rowObservables;
	private String[] rowObservableLabels;
	private IObservableValue selectionObservable;

	public SingleChoiceRidget() {
		singleSelectionObservable = new SingleSelectionObservable();
	}

	@Override
	protected void bindUIControl() {
		Composite control = getUIControl();
		if (control != null && rowObservables != null) {
			disposeChildren(control);
			control.setBackground(control.getDisplay().getSystemColor(SWT.COLOR_CYAN));
			Object[] values = rowObservables.toArray();
			for (int i = 0; i < values.length; i++) {
				Object value = values[i];
				String caption = rowObservableLabels != null ? rowObservableLabels[i] : value.toString();

				Button button = new Button(control, SWT.RADIO);
				button.setText(caption);
				button.setData(value);
				if (value == singleSelectionObservable.getValue()) {
					button.setSelection(true);
				}
				button.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						setSelection(e.widget.getData());
					}
				});
			}
		}
	}

	@Override
	protected void checkUIControl(Object uiControl) {
		AbstractSWTRidget.assertType(uiControl, ChoiceComposite.class);
		if (uiControl != null) {
			ChoiceComposite composite = (ChoiceComposite) uiControl;
			Assert.isTrue(!composite.isMultipleSelection(), "expected single selection ChoiceComposite"); //$NON-NLS-1$
		}
	}

	@Override
	protected void unbindUIControl() {
		Composite control = getUIControl();
		disposeChildren(control);
	}

	private void disposeChildren(Composite control) {
		if (control != null) {
			for (Control child : control.getChildren()) {
				child.dispose();
			}
		}
	}

	// public methods
	// ///////////////

	@Override
	public ChoiceComposite getUIControl() {
		return (ChoiceComposite) super.getUIControl();
	}

	public void bindToModel(IObservableList listObservableValue, IObservableValue selectionObservableValue) {
		// TODO [ev] asserts
		Assert.isNotNull(listObservableValue, "Cannot be null: listObservableValue"); //$NON-NLS-1$
		bindToModel(listObservableValue, null, selectionObservableValue);
	}

	public void bindToModel(Object listBean, String listPropertyName, Object selectionBean, String selectionPropertyName) {
		// TODO [ev] asserts
		// Assert.isNotNull(listPropertyName, "Cannot be null: listPropertyName"); //$NON-NLS-1$
		// Assert.isNotNull(selectionPropertyName, "Cannot be null: selectionPropertyName"); //$NON-NLS-1$
		IObservableList list = BeansObservables.observeList(Realm.getDefault(), listBean, listPropertyName);
		IObservableValue value = BeansObservables.observeValue(selectionBean, selectionPropertyName);
		bindToModel(list, null, value);
	}

	public void bindToModel(List<? extends Object> options, List<String> optionLabels, Object selectionBean,
			String selectionPropertyName) {
		// TODO [ev] asserts
		Assert.isNotNull(selectionPropertyName, "Cannot be null: selectionPropertyName"); //$NON-NLS-1$
		IObservableList list = new WritableList(options, Object.class);
		IObservableValue value = BeansObservables.observeValue(selectionBean, selectionPropertyName);
		bindToModel(list, optionLabels, value);
	}

	public void bindToModel(IObservableList options, List<String> optionLabels, IObservableValue selectionObservable) {
		// TODO [ev] asserts
		Assert.isNotNull(options);
		if (optionLabels != null) {
			String msg = "Mismatch between number of options and optionLabels"; //$NON-NLS-1$
			Assert.isLegal(options.size() == optionLabels.size(), msg);
		}

		unbindUIControl();

		rowObservables = options;
		if (optionLabels != null) {
			rowObservableLabels = optionLabels.toArray(new String[optionLabels.size()]);
		} else {
			rowObservableLabels = null;
		}
		this.selectionObservable = selectionObservable;
		updateSelection();

		bindUIControl();
	}

	public Object getSelection() {
		return singleSelectionObservable.getValue();
	}

	@Deprecated
	public IObservableValue getSelectionObservable() {
		return singleSelectionObservable;
	}

	public void setSelection(Object newSelection) {
		assertIsBoundToModel();
		Object value = null;
		if (newSelection != null && rowObservables.contains(newSelection)) {
			value = newSelection;
		}
		singleSelectionObservable.setValue(value);
		// TODO [ev] use databinding
		ChoiceComposite composite = getUIControl();
		for (Control child : composite.getChildren()) {
			Button button = (Button) child;
			boolean isSelected = value != null && child.getData() == value;
			button.setSelection(isSelected);
		}
	}

	@Override
	public void updateFromModel() {
		super.updateFromModel();
		updateSelection();
	}

	public IObservableList getObservableList() {
		return rowObservables; // TODO [ev] ok?
	}

	@Override
	public final boolean isDisableMandatoryMarker() {
		return !(singleSelectionObservable.getValue() == null);
	}

	// helping methods
	// ////////////////

	private void assertIsBoundToModel() {
		if (rowObservables == null) {
			throw new BindingException("ridget not bound to model"); //$NON-NLS-1$
		}
	}

	private void updateSelection() {
		if (selectionObservable != null) {
			setSelection(selectionObservable.getValue());
		}
	}

	// helping classes
	// ////////////////

	/**
	 * Observable value bean for single selection. This class is used by this
	 * ridget to monitor and maintain the selection state for single selection
	 * and fire appropriate events.
	 */
	private final class SingleSelectionObservable extends WritableValue {

		SingleSelectionObservable() {
			super(null, Object.class);
		}

		@Override
		protected void fireValueChange(ValueDiff diff) {
			super.fireValueChange(diff);
			String key = IChoiceRidget.PROPERTY_SELECTION;
			Object oldValue = diff.getOldValue();
			Object newValue = diff.getNewValue();
			SingleChoiceRidget.this.firePropertyChange(key, oldValue, newValue);
			// TODO [ev] refactor
			boolean isMandatoryDisabled = isDisableMandatoryMarker();
			for (IMarker marker : getMarkersOfType(MandatoryMarker.class)) {
				MandatoryMarker mMarker = (MandatoryMarker) marker;
				mMarker.setDisabled(isMandatoryDisabled);
			}
			// TODO [ev] refactor
			if (selectionObservable != null) {
				selectionObservable.setValue(newValue);
			}
		}
	};

}
