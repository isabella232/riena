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
package org.eclipse.riena.ui.ridgets.databinding;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

import org.eclipse.riena.tests.collect.NonUITestCase;

/**
 * Tests of the class <code>StringToGregorianCalendarConverter</code>.
 */
@NonUITestCase
public class StringToGregorianCalendarConverterTest extends TestCase {

	public void testConvert() throws Exception {

		StringToGregorianCalendarConverter converter = new StringToGregorianCalendarConverter();

		assertNull(converter.convert(null));
		assertNull(converter.convert("")); //$NON-NLS-1$

		try {
			converter.convert("somthing");
			fail("expected exception was not thrown!");
		} catch (RuntimeException e) {
			assertTrue(e instanceof ConversionFailure);
		}

		GregorianCalendar calendar = new GregorianCalendar(2007, 4, 16);
		String input = SimpleDateFormat.getDateInstance().format(calendar.getTime());
		assertEquals(calendar, converter.convert(input));

	}

}
