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

import java.util.Date;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.DateAndTimeObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DateTime;

import org.eclipse.riena.ui.ridgets.IDateTimeRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractSWTRidget;

/**
 * Ridget for {@link DateTime} widgets.
 */
public class DateTimeRidget extends AbstractSWTRidget implements IDateTimeRidget {

	private IObservableValue value;
	private DataBindingContext dbc;
	private Binding dtBinding;

	@Override
	protected void checkUIControl(Object uiControl) {
		AbstractSWTWidgetRidget.assertType(uiControl, DateTime.class);
	}

	@Override
	protected void bindUIControl() {
		DateTime control = getUIControl();
		if (control != null && value != null) {
			dbc = new DataBindingContext();
			IObservableValue timeObservable;
			IObservableValue dateObservable;
			if ((control.getStyle() & SWT.TIME) != 0) {
				// it is a time widget
				timeObservable = WidgetProperties.selection().observe(control);
				timeObservable.setValue(value.getValue());
				dateObservable = new WritableValue(timeObservable.getRealm(), value.getValue(), Date.class);
			} else {
				// it is  date/calendar widget
				dateObservable = WidgetProperties.selection().observe(control);
				timeObservable = new WritableValue(dateObservable.getRealm(), value.getValue(), Date.class);
			}
			dtBinding = dbc.bindValue(new DateAndTimeObservableValue(dateObservable, timeObservable), value);
		}
	}

	@Override
	protected void unbindUIControl() {
		if (dtBinding != null) {
			dtBinding.dispose();
			dtBinding = null;
		}
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

		this.value = observableValue;

		bindUIControl();
	}

	public void bindToModel(Object pojo, String propertyName) {
		bindToModel(PojoObservables.observeValue(pojo, propertyName));
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
		if (value != null) {
			dtBinding.updateModelToTarget();
		}
	}

}
