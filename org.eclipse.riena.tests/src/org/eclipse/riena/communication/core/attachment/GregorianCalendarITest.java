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
package org.eclipse.riena.communication.core.attachment;

import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.factory.Register;
import org.eclipse.riena.sample.app.common.calendar.ITestGregorianCalendar;
import org.eclipse.riena.tests.RienaTestCase;
import org.eclipse.riena.tests.collect.ManualTestCase;

/**
 * Test the hessian bug (we use our own deserializer now) when transferring
 * {@code GregorianCalendar}.
 * 
 */
@ManualTestCase
public final class GregorianCalendarITest extends RienaTestCase {

	private ITestGregorianCalendar calendarService;
	private IRemoteServiceRegistration regCalenderService;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp() throws Exception {
		super.setUp();
		regCalenderService = Register.remoteProxy(ITestGregorianCalendar.class).usingUrl(
				"http://localhost:8080/hessian/TestGregorianCalendarWS").withProtocol("hessian").andStart(getContext());
		calendarService = (ITestGregorianCalendar) getContext().getService(
				getContext().getServiceReference(ITestGregorianCalendar.class.getName()));
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	public void tearDown() throws Exception {
		super.tearDown();
		regCalenderService.unregister();
		calendarService = null;
	}

	public void testDiffGregorian() {
		GregorianCalendar from = new GregorianCalendar(2007, 1, 6);
		GregorianCalendar till = new GregorianCalendar(2007, 1, 7);
		long diff = calendarService.diffTimes1(from, till);
		assertEquals(24L * 60L * 60L * 1000L, diff);
	}

	public void testDiffDate() {
		Date from = new Date(2007, 1, 6);
		Date till = new Date(2007, 1, 7);
		long diff = calendarService.diffTimes2(from, till);
		assertEquals(24L * 60L * 60L * 1000L, diff);
	}

}