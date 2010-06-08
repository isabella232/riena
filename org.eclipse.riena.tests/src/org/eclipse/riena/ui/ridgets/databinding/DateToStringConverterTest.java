/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.ridgets.databinding;

import java.util.Date;

import junit.framework.TestCase;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Tests for the classes {@link DateToStringConverter} and
 * {@link StringToDateConverter}.
 */
@NonUITestCase
public class DateToStringConverterTest extends TestCase {

	public void testConvertToString() {
		DateToStringConverter converter = new DateToStringConverter("MM/dd/yyyy");

		Object result = converter.convert(null);
		assertEquals(null, result); //$NON-NLS-1$

		result = converter.convert(new Date(0L));
		assertEquals("01/01/1970", result); //$NON-NLS-1$

		Date date = new Date(1221011682194L);
		result = converter.convert(date);
		assertEquals("09/10/2008", result);
	}

	public void testConvertToDateNoTimezone() {
		// Problem: in what timezone is MM/dd/yyyy ?
		StringToDateConverter converter = new StringToDateConverter("MM/dd/yyyy");

		Date result = (Date) converter.convert(null);
		assertEquals(null, result); //$NON-NLS-1$

		result = (Date) converter.convert("01/01/1970");
		assertEquals(0, result.getTime());
	}

	public void testConvertToDateGMT() {
		StringToDateConverter converter = new StringToDateConverter("MM/dd/yyyy zzz");

		Date result = (Date) converter.convert(null);
		assertEquals(null, result); //$NON-NLS-1$

		result = (Date) converter.convert("01/01/1970 GMT");
		assertEquals(0, result.getTime());
	}

	public void testConvertToDateGMTPlusTwo() {
		StringToDateConverter converter = new StringToDateConverter("MM/dd/yyyy zzzzz");

		Date result = (Date) converter.convert(null);
		assertEquals(null, result); //$NON-NLS-1$

		result = (Date) converter.convert("01/01/1970 +0200");
		assertEquals(3600000 * -2, result.getTime());
	}

	public void testConvertToDateExact() {
		StringToDateConverter converter = new StringToDateConverter("MM/dd/yyyy HH:mm:ss:SSS zzz");

		Date result1 = (Date) converter.convert("07/10/1996 16:05:28:046 PDT");
		assertEquals(837039928046L, result1.getTime());

		Date result2 = (Date) converter.convert("01/01/1970 00:00:00:000 GMT");
		assertEquals(0L, result2.getTime());
	}

	public void testToStringAndBack() {
		DateToStringConverter toString = new DateToStringConverter("MM/dd/yyyy");
		StringToDateConverter toDate = new StringToDateConverter("MM/dd/yyyy");

		Date input = new Date(0);
		Date result = (Date) toDate.convert(toString.convert(input));
		assertEquals(input.getTime(), result.getTime());
	}

	public void testToDateAndBack() {
		DateToStringConverter toString = new DateToStringConverter("MM/dd/yyyy");
		StringToDateConverter toDate = new StringToDateConverter("MM/dd/yyyy");

		String result = (String) toString.convert(toDate.convert("01/01/1970"));
		assertEquals("01/01/1970", result);
	}
}
