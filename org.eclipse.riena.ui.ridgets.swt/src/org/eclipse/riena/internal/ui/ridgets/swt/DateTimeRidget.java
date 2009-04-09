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
import java.util.Date;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.DateAndTimeObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DateTime;

import org.eclipse.riena.ui.ridgets.IDateTimeRidget;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractSWTRidget;

/**
 * Ridget for {@link DateTime} widgets.
 */
public class DateTimeRidget extends AbstractSWTRidget implements IDateTimeRidget {

	private IObservableValue modelValue;
	private final IObservableValue ridgetValue;
	private DataBindingContext dbc;
	private Binding modelBinding;
	private Binding controlBinding;

	public DateTimeRidget() {
		ridgetValue = new WritableValue(new Date(0), Date.class);
		addPropertyChangeListener(IMarkableRidget.PROPERTY_OUTPUT_ONLY, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				updateEditable();
			}
		});
		addPropertyChangeListener(IMarkableRidget.PROPERTY_ENABLED, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				updateEditable();
			}
		});
	}

	@Override
	protected void checkUIControl(Object uiControl) {
		AbstractSWTWidgetRidget.assertType(uiControl, DateTime.class);
	}

	@Override
	protected void bindUIControl() {
		final DateTime control = getUIControl();
		if (control != null) {
			updateEditable();

			dbc = new DataBindingContext();
			final IObservableValue timeObservable;
			final IObservableValue dateObservable;
			if (isTimeControl(control)) {
				// it is a time widget
				timeObservable = WidgetProperties.selection().observe(control);
				timeObservable.setValue(ridgetValue.getValue());
				dateObservable = new WritableValue(timeObservable.getRealm(), ridgetValue.getValue(), Date.class);
			} else {
				// it is  date/calendar widget
				dateObservable = WidgetProperties.selection().observe(control);
				timeObservable = new WritableValue(dateObservable.getRealm(), ridgetValue.getValue(), Date.class);
			}
			if (modelValue != null) {
				modelBinding = dbc.bindValue(ridgetValue, modelValue, new UpdateValueStrategy(
						UpdateValueStrategy.POLICY_UPDATE), new UpdateValueStrategy(
						UpdateValueStrategy.POLICY_ON_REQUEST));
			}
			controlBinding = dbc.bindValue(new DateAndTimeObservableValue(dateObservable, timeObservable), ridgetValue);
		}
	}

	@Override
	protected void unbindUIControl() {
		if (dbc != null) {
			dbc.dispose();
			dbc = null;
		}
	}

	@Override
	public DateTime getUIControl() {
		return (DateTime) super.getUIControl();
	}

	@Override
	public boolean isDisableMandatoryMarker() {
		return true;
	}

	public void bindToModel(IObservableValue observableValue) {
		unbindUIControl();

		Assert.isNotNull(observableValue);
		this.modelValue = observableValue;
		this.ridgetValue.setValue(new Date(0));

		bindUIControl();
	}

	public void bindToModel(Object pojo, String propertyName) {
		bindToModel(PojoObservables.observeValue(pojo, propertyName));
	}

	public Date getDate() {
		return (Date) ridgetValue.getValue();
	}

	public void setDate(Date date) {
		ridgetValue.setValue(date);
		if (controlBinding != null) {
			controlBinding.updateModelToTarget(); // update widget
		}
		if (modelBinding != null) {
			modelBinding.updateTargetToModel(); // update modelValue
		}
		// TODO [EV] fire-event and test 
	}

	public IConverter getModelToUIControlConverter() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setModelToUIControlConverter(IConverter converter) {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateFromModel() {
		super.updateFromModel();
		if (modelValue != null) {
			setDate((Date) modelValue.getValue());
		}
	}

	// helping methods
	//////////////////

	private boolean isTimeControl(DateTime control) {
		return (control.getStyle() & SWT.TIME) != 0;
	}

	private void updateEditable() {
		DateTime control = getUIControl();
		if (control != null && !control.isDisposed()) {
			control.setEnabled(isOutputOnly() || !isEnabled() ? false : true);
		}
	}

}
