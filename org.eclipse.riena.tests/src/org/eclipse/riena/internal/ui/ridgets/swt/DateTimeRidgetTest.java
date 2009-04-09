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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;

import org.eclipse.riena.beans.common.TypedBean;
import org.eclipse.riena.tests.TestUtils;
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
		TypedBean<Date> dateBean = new TypedBean<Date>(new Date(0));
		ridget.bindToModel(dateBean, TypedBean.PROP_VALUE);

		Date date2001 = createDate(2001, 12, 2);
		ridget.setDate(date2001);

		assertEquals(date2001, ridget.getDate());
		assertEquals(date2001, dateBean.getValue());
		assertEquals(date2001, getDate(control));

		Date date1700 = createDate(1700, 1, 1);
		ridget.setDate(date1700);

		assertEquals(date1700, ridget.getDate());
		assertEquals(date1700, dateBean.getValue());
		assertEquals(date1700, getDate(control));
	}

	public void testSetDateNull() {
		IDateTimeRidget ridget = getRidget();
		TypedBean<Date> dateBean = new TypedBean<Date>(new Date(0));
		ridget.bindToModel(dateBean, TypedBean.PROP_VALUE);

		ridget.setDate(null);

		assertEquals(null, ridget.getDate());
		// TODO [ev] wait for https://bugs.eclipse.org/bugs/show_bug.cgi?id=271720
		// what should be in the control?
		assertEquals(null, dateBean.getValue());
	}

	public void testUpdateFromModel() {
		IDateTimeRidget ridget = getRidget();
		DateTime control = getWidget();
		Date date2000 = createDate(2000, 1, 1);
		Date date2001 = createDate(2001, 12, 2);

		assertEquals(new Date(0), ridget.getDate());

		TypedBean<Date> dateBean = new TypedBean<Date>(date2000);
		ridget.bindToModel(dateBean, TypedBean.PROP_VALUE);

		assertEquals(new Date(0), ridget.getDate());
		// assertEquals(null, ridget.getDate());
		// TODO [ev] wait for https://bugs.eclipse.org/bugs/show_bug.cgi?id=271720
		// assertEquals(new Date(0), getDate(control));

		ridget.updateFromModel();

		assertEquals(date2000, ridget.getDate());
		assertEquals(date2000, getDate(control));

		dateBean.setValue(date2001);

		assertEquals(date2000, ridget.getDate());
		assertEquals(date2000, getDate(control));

		ridget.updateFromModel();

		assertEquals(date2001, ridget.getDate());
		assertEquals(date2001, getDate(control));

		dateBean.setValue(null);
		ridget.updateFromModel();

		assertEquals(null, ridget.getDate());
		// TODO [ev] wait for https://bugs.eclipse.org/bugs/show_bug.cgi?id=271720
		// assertEquals(new Date(0), getDate(control));
	}

	public void testMandatoryMarker() {
		IDateTimeRidget ridget = getRidget();
		ridget.setMandatory(true);

		assertTrue(ridget.isDisableMandatoryMarker());
		TestUtils.assertMandatoryMarker(ridget, 1, true);
	}

	// helping methods
	//////////////////

	private Date createDate(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month - 1, day);
		return cal.getTime();
	}

	private Date getDate(DateTime control) {
		int year = control.getYear();
		int month = control.getMonth();
		int day = control.getDay();
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

}
