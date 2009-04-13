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
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.DateAndTimeObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DateTime;

import org.eclipse.riena.ui.ridgets.IDateTimeRidget;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;

/**
 * Ridget for {@link DateTime} widgets.
 */
public class DateTimeRidget extends AbstractEditableRidget implements IDateTimeRidget, ITextRidget {

	/**
	 * Holds the date value for this ridget.
	 * <p>
	 * Do not access directly. Use {@link #getRidgetObservable()}.
	 */
	private IObservableValue ridgetObservable;
	private DataBindingContext dbc;
	private Binding controlBinding;

	public DateTimeRidget() {
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
			final Date nonNullDate = getNonNullDate(getDate());
			if (isTimeControl(control)) {
				// it is a time widget
				timeObservable = WidgetProperties.selection().observe(control);
				timeObservable.setValue(nonNullDate);
				dateObservable = new WritableValue(timeObservable.getRealm(), nonNullDate, Date.class);
			} else {
				// it is  date/calendar widget
				dateObservable = WidgetProperties.selection().observe(control);
				dateObservable.setValue(nonNullDate);
				timeObservable = new WritableValue(dateObservable.getRealm(), nonNullDate, Date.class);
			}
			controlBinding = dbc.bindValue(new DateAndTimeObservableValue(dateObservable, timeObservable) {
				@Override
				protected void doSetValue(Object value) {
					super.doSetValue(getNonNullDate((Date) value));
				}
			}, getRidgetObservable(), new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE),
					new UpdateValueStrategy(UpdateValueStrategy.POLICY_ON_REQUEST));
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
	protected IObservableValue getRidgetObservable() {
		if (ridgetObservable == null) {
			ridgetObservable = new WritableValue(null, Date.class);
		}
		return ridgetObservable;
	}

	@Override
	public DateTime getUIControl() {
		return (DateTime) super.getUIControl();
	}

	@Override
	public void bindToModel(IObservableValue observableValue) {
		unbindUIControl();

		Assert.isNotNull(observableValue);
		super.bindToModel(observableValue);

		bindUIControl();
	}

	@Override
	public void bindToModel(Object pojo, String propertyName) {
		bindToModel(PojoObservables.observeValue(pojo, propertyName));
	}

	/** Not supported. */
	public int getAlignment() {
		throw new UnsupportedOperationException();
	}

	public Date getDate() {
		return (Date) getRidgetObservable().getValue();
	}

	public String getText() {
		Date date = getDate();
		return date != null ? SimpleDateFormat.getInstance().format(date) : ""; //$NON-NLS-1$
	}

	public boolean isDirectWriting() {
		return true;
	}

	@Override
	public boolean isDisableMandatoryMarker() {
		return true;
	}

	public boolean revalidate() {
		Date date = getDate();
		IStatus onUpdate = checkOnUpdateRules(date);
		validationRulesChecked(suppressBlockWithFlash(onUpdate));
		if (onUpdate.isOK()) {
			getValueBindingSupport().updateFromTarget();
		}
		return !isErrorMarked();
	}

	/** Not supported. */
	public void setAlignment(int alignment) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Implementation note: since the underlying DateTime widget cannot be
	 * empty, a {@code null} date value will cause the widget to show the
	 * 'empty' date, but getDate() will correctly return null.
	 */
	public void setDate(Date date) {
		getRidgetObservable().setValue(date);
		if (controlBinding != null) {
			controlBinding.updateModelToTarget(); // update widget
		}
		IStatus onUpdate = checkOnUpdateRules(date);
		validationRulesChecked(suppressBlockWithFlash(onUpdate));
		if (onUpdate.isOK()) {
			getValueBindingSupport().updateFromTarget();
			// TODO [EV] fire-event and test 
		}
	}

	/** Not supported. */
	public void setDirectWriting(boolean directWriting) {
		throw new UnsupportedOperationException();
	}

	/** Not supported. */
	public void setText(String text) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateFromModel() {
		super.updateFromModel();
		if (controlBinding != null) {
			controlBinding.updateModelToTarget(); // updateWidget
		}
		IStatus onUpdate = checkOnUpdateRules(getDate());
		validationRulesChecked(suppressBlockWithFlash(onUpdate));
	}

	// helping methods
	//////////////////

	private Date getNonNullDate(Date date) {
		return date != null ? date : new Date(0);
	}

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
