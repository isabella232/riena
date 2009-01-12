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
package org.eclipse.riena.ui.swt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.tests.collect.UITestCase;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Tests of the class {@link StatuslineTime}.
 */
@UITestCase
public class StatuslineTimeTest extends TestCase {

	private Shell shell;
	private StatuslineTime statusTime;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		shell = new Shell();
		statusTime = new StatuslineTime(shell, SWT.NONE);
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		SwtUtilities.disposeWidget(shell);
		SwtUtilities.disposeWidget(statusTime);
	}

	/**
	 * Tests the method {@code createContents()}.<br>
	 * <i>The method is already called by the constructor.</i>
	 */
	public void testCreateContents() {

		Control[] controls = statusTime.getChildren();
		assertEquals(1, controls.length);
		assertTrue(controls[0] instanceof Label);

	}

	/**
	 * Tests the method {@code updateTime()}.
	 * 
	 * @throws ParseException
	 */
	public void testUpdateTime() throws ParseException {

		Date dateBeforeUpdate = new Date();
		Calendar cal1 = GregorianCalendar.getInstance();
		cal1.setTime(dateBeforeUpdate);
		cal1.set(0, 0, 0);
		cal1.set(Calendar.SECOND, 0);

		ReflectionUtils.invokeHidden(statusTime, "updateTime");
		Control[] controls = statusTime.getChildren();
		Label label = (Label) controls[0];
		String timeString = label.getText();

		SimpleDateFormat format = ReflectionUtils.invokeHidden(statusTime, "getFormat");
		Date labelDate = format.parse(timeString);
		Calendar cal2 = GregorianCalendar.getInstance();
		cal2.setTime(labelDate);
		cal2.set(0, 0, 0);

		long diff = cal1.getTimeInMillis() - cal2.getTimeInMillis();
		assertTrue(diff >= 0);
		assertTrue(diff < 1000);

	}

}
