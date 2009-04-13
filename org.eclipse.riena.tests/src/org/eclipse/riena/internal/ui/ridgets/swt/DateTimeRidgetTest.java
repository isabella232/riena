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

import java.util.Calendar;
import java.util.Date;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;

import org.eclipse.riena.beans.common.TypedBean;
import org.eclipse.riena.tests.TestUtils;
import org.eclipse.riena.tests.UITestHelper;
import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.IDateTimeRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;

/**
 * Tests for the class {@link DateTextRidget}.
 */
public class DateTimeRidgetTest extends AbstractSWTRidgetTest {

	@Override
	protected IRidget createRidget() {
		return new DateTimeRidget();
	}

	@Override
	protected IDateTimeRidget getRidget() {
		return (IDateTimeRidget) super.getRidget();
	}

	@Override
	protected Control createWidget(Composite parent) {
		return new DateTime(getShell(), SWT.DATE | SWT.MEDIUM);
	}

	@Override
	protected DateTime getWidget() {
		return (DateTime) super.getWidget();
	}

	// test methods
	///////////////

	public void testRidgetMapping() {
		SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();
		assertSame(DateTimeRidget.class, mapper.getRidgetClass(getWidget()));
	}

	public void testSetDate() {
		IDateTimeRidget ridget = getRidget();
		DateTime control = getWidget();
		TypedBean<Date> dateBean = new TypedBean<Date>(null);
		ridget.bindToModel(dateBean, TypedBean.PROP_VALUE);

		assertEquals(null, ridget.getDate());
		assertEquals(null, dateBean.getValue());

		Date date2001 = createDate(2001, 12, 2);
		ridget.setDate(date2001);

		assertEquals(date2001, ridget.getDate());
		assertEquals(date2001, dateBean.getValue());
		assertEquals("12/2/2001", getDate(control));

		Date date1700 = createDate(1700, 1, 1);
		ridget.setDate(date1700);

		assertEquals(date1700, ridget.getDate());
		assertEquals(date1700, dateBean.getValue());
		assertEquals("1/1/1700", getDate(control));
	}

	public void testSetDateNull() {
		IDateTimeRidget ridget = getRidget();
		DateTime control = getWidget();
		TypedBean<Date> dateBean = new TypedBean<Date>(new Date());
		ridget.bindToModel(dateBean, TypedBean.PROP_VALUE);
		ridget.updateFromModel();

		ridget.setDate(null);

		assertEquals(null, ridget.getDate());
		assertEquals(asStringEmptyDate(), getDate(control));
		assertEquals(null, dateBean.getValue());
	}

	public void testUpdateFromModel() {
		IDateTimeRidget ridget = getRidget();
		DateTime control = getWidget();
		Date date2000 = createDate(2000, 1, 1);
		Date date2001 = createDate(2001, 12, 2);
		TypedBean<Date> dateBean = new TypedBean<Date>(date2000);
		ridget.bindToModel(dateBean, TypedBean.PROP_VALUE);

		assertEquals(null, ridget.getDate());
		assertEquals(asStringEmptyDate(), getDate(control));

		ridget.updateFromModel();

		assertEquals(date2000, ridget.getDate());
		assertEquals("1/1/2000", getDate(control));

		dateBean.setValue(date2001);

		assertEquals(date2000, ridget.getDate());
		assertEquals("1/1/2000", getDate(control));

		ridget.updateFromModel();

		assertEquals(date2001, ridget.getDate());
		assertEquals("12/2/2001", getDate(control));

		dateBean.setValue(null);
		ridget.updateFromModel();

		assertEquals(null, ridget.getDate());
		assertEquals(asStringEmptyDate(), getDate(control));
	}

	public void testMandatoryMarker() {
		IDateTimeRidget ridget = getRidget();
		ridget.setMandatory(true);

		assertTrue(ridget.isDisableMandatoryMarker());
		TestUtils.assertMandatoryMarker(ridget, 1, true);
	}

	public void testControlEnablement() {
		IDateTimeRidget ridget = getRidget();
		DateTime control = getWidget();

		assertTrue(control.isEnabled());

		ridget.setEnabled(false);

		assertFalse(control.isEnabled());

		ridget.setEnabled(true);

		assertTrue(control.isEnabled());

		ridget.setOutputOnly(true);
		ridget.setEnabled(false);
		ridget.setEnabled(true);

		assertFalse(control.isEnabled());

		ridget.setEnabled(false);
		ridget.setOutputOnly(false);

		assertFalse(control.isEnabled());

		ridget.setEnabled(true);

		assertTrue(control.isEnabled());
	}

	public void testValidateOnUpdate_OnUpdateRules() {
		handleValidateOnUpdate_UpdateFromModel(ValidationTime.ON_UPDATE_TO_MODEL);
	}

	public void testValidateOnUpdate_OnEditRules() {
		handleValidateOnUpdate_UpdateFromModel(ValidationTime.ON_UI_CONTROL_EDIT);
	}

	public void testValidateOnUpdate_SetDate_OnUpdateRules() {
		handleValidateOnUpdate_SetDate(ValidationTime.ON_UPDATE_TO_MODEL);
	}

	public void testValidateOnUpdate_SetDate_OnEditRules() {
		handleValidateOnUpdate_SetDate(ValidationTime.ON_UI_CONTROL_EDIT);
	}

	public void testValidateOnWidgetModification_onUpdateRules() {
		handleValidateOnWidgetModification(ValidationTime.ON_UPDATE_TO_MODEL);
	}

	public void testValidateOnWidgetModification_OnEditRules() {
		handleValidateOnWidgetModification(ValidationTime.ON_UI_CONTROL_EDIT);
	}

	public void testRevalidate() {
		IDateTimeRidget ridget = getRidget();
		FTValidator validator = new FTValidator(new Date(99));
		ridget.addValidationRule(validator, ValidationTime.ON_UPDATE_TO_MODEL);
		ridget.setDate(new Date(99));

		assertTrue(ridget.isErrorMarked());

		ridget.removeValidationRule(validator);

		assertTrue(ridget.isErrorMarked());

		ridget.revalidate();

		assertFalse(ridget.isErrorMarked());
	}

	// helping methods
	//////////////////

	private String asString(Date date) {
		String result = "null";
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.clear();
			cal.setTime(date);
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			int day = cal.get(Calendar.DAY_OF_MONTH);
			result = String.format("%s/%s/%s", month, day, year);
		}
		return result;
	}

	private String asStringEmptyDate() {
		return asString(new Date(0));
	}

	private Date createDate(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month - 1, day, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	private String getDate(DateTime control) {
		int year = control.getYear();
		int month = control.getMonth() + 1;
		int day = control.getDay();
		return String.format("%s/%s/%s", month, day, year);
	}

	private void handleValidateOnUpdate_UpdateFromModel(ValidationTime time) {
		IDateTimeRidget ridget = getRidget();
		FTValidator validator = new FTValidator(new Date(99));
		ridget.addValidationRule(validator, time);
		TypedBean<Date> dateBean = new TypedBean<Date>(new Date(0));
		ridget.bindToModel(dateBean, TypedBean.PROP_VALUE);

		int count = validator.count;

		ridget.updateFromModel();

		assertEquals(count + 1, validator.count);
		assertFalse(ridget.isErrorMarked());

		dateBean.setValue(new Date(99));
		ridget.updateFromModel();

		assertEquals(count + 2, validator.count);
		assertTrue(ridget.isErrorMarked());
		assertEquals(new Date(99), ridget.getDate());

		dateBean.setValue(new Date(0));
		ridget.updateFromModel();

		assertEquals(count + 3, validator.count);
		assertFalse(ridget.isErrorMarked());
	}

	private void handleValidateOnUpdate_SetDate(ValidationTime time) {
		IDateTimeRidget ridget = getRidget();
		FTValidator validator = new FTValidator(new Date(99));
		ridget.addValidationRule(validator, time);

		assertEquals(0, validator.count);

		ridget.setDate(new Date(0));

		assertEquals(1, validator.count);
		assertFalse(ridget.isErrorMarked());

		ridget.setDate(new Date(99));

		assertEquals(2, validator.count);
		assertTrue(ridget.isErrorMarked());
		assertEquals(new Date(99), ridget.getDate());

		ridget.setDate(new Date(0));

		assertEquals(3, validator.count);
		assertFalse(ridget.isErrorMarked());
	}

	private void handleValidateOnWidgetModification(ValidationTime time) {
		IDateTimeRidget ridget = getRidget();
		FTValidator validator = new FTValidator("4/10/2009");
		DateTime control = getWidget();
		ridget.addValidationRule(validator, time);
		TypedBean<Date> dateBean = new TypedBean<Date>(new Date(0));
		ridget.bindToModel(dateBean, TypedBean.PROP_VALUE);

		int count = validator.count;
		assertEquals(false, ridget.isErrorMarked());

		control.setFocus();
		UITestHelper.sendString(control.getDisplay(), "4/10/2009");

		System.out.println(control);
		assertEquals(count + 3, validator.count);
		assertEquals(true, ridget.isErrorMarked());
	}

	private final class FTValidator implements IValidator {

		int count = 0;
		private Date errorDate;
		private String errorString;

		FTValidator(Date errorValue) {
			this.errorDate = errorValue;
		}

		FTValidator(String errorValue) {
			this.errorString = errorValue;
		}

		public IStatus validate(Object value) {
			count++;
			IStatus result = ValidationStatus.ok();
			if (errorDate != null && errorDate.equals(value)) {
				result = ValidationStatus.error("error");
			}
			if (errorString != null && errorString.equals(asString((Date) value))) {
				result = ValidationStatus.error("error");
			}
			// System.out.println("validate: " + count + " " + value + " " + result);
			return result;
		}
	}
}
