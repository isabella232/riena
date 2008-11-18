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

import junit.framework.TestCase;

import org.eclipse.riena.tests.TestUtils;

/**
 * Tests for the {@link NumericString} class.
 */
public class NumericStringTest extends TestCase {

	public void testCreateNumericString() {
		NumericString ns;

		ns = new NumericString("1234", false);
		assertEqualsNS("1234", ns);

		ns = new NumericString("1234", true);
		assertEqualsNS("1.234", ns);

		try {
			new NumericString(null, false);
			fail();
		} catch (RuntimeException rex) {
			// expected
		}
	}

	public void testDelete() {
		NumericString ns;
		int cursor;

		ns = createNumericString("1.234,", true);
		cursor = ns.delete(1, 2, (char) 127);
		assertEqualsNS("134,", ns);
		assertEquals(1, cursor);

		ns = createNumericString("1.234,", true);
		cursor = ns.delete(0, 1, (char) 127);
		assertEqualsNS("234,", ns);
		assertEquals(0, cursor);

		ns = createNumericString("12.345,", true);
		cursor = ns.delete(2, 3, (char) 127);
		assertEqualsNS("1.245,", ns);
		assertEquals(3, cursor);

		ns = createNumericString("1.234.567,", true);
		cursor = ns.delete(5, 6, (char) 127);
		assertEqualsNS("123.467,", ns);
		assertEquals(5, cursor);

		ns = createNumericString("1.234.567,", true);
		cursor = ns.delete(7, 8, (char) 127);
		assertEqualsNS("123.457,", ns);
		assertEquals(cursor, 6);

		ns = createNumericString("-1.234,", true);
		cursor = ns.delete(2, 3, (char) 127);
		assertEqualsNS("-134,", ns);
		assertEquals(cursor, 2);

		ns = createNumericString("-1.234,", true);
		cursor = ns.delete(1, 2, (char) 127);
		assertEqualsNS("-234,", ns);
		assertEquals(1, cursor);

		ns = createNumericString("-1.234.567,", true);
		cursor = ns.delete(8, 9, (char) 127);
		assertEqualsNS("-123.457,", ns);
		assertEquals(7, cursor);
	}

	public void testDeleteNoGrouping() {
		NumericString ns;
		int cursor;

		ns = createNumericString("1234,", false);
		cursor = ns.delete(1, 2, (char) 127);
		assertEqualsNS("134,", ns);
		assertEquals(1, cursor);

		ns = createNumericString("1234,", false);
		cursor = ns.delete(0, 1, (char) 127);
		assertEqualsNS("234,", ns);
		assertEquals(0, cursor);

		ns = createNumericString("12345,", false);
		cursor = ns.delete(2, 3, (char) 127);
		assertEqualsNS("1245,", ns);
		assertEquals(2, cursor);

		ns = createNumericString("1234567,", false);
		cursor = ns.delete(4, 5, (char) 127);
		assertEqualsNS("123467,", ns);
		assertEquals(4, cursor);

		ns = createNumericString("1234567,", false);
		cursor = ns.delete(5, 6, (char) 127);
		assertEqualsNS("123457,", ns);
		assertEquals(cursor, 5);

		ns = createNumericString("-1234,", false);
		cursor = ns.delete(2, 3, (char) 127);
		assertEqualsNS("-134,", ns);
		assertEquals(cursor, 2);

		ns = createNumericString("-1234,", false);
		cursor = ns.delete(1, 2, (char) 127);
		assertEqualsNS("-234,", ns);
		assertEquals(1, cursor);

		ns = createNumericString("-1234567,", false);
		cursor = ns.delete(6, 7, (char) 127);
		assertEqualsNS("-123457,", ns);
		assertEquals(6, cursor);
	}

	public void testBackspace() {
		NumericString ns;
		int cursor;

		ns = createNumericString("123.456", true);
		cursor = ns.delete(3, 4, '\b');
		assertEqualsNS("12.456", ns);
		assertEquals(2, cursor);

		ns = createNumericString("1.456", true);
		cursor = ns.delete(1, 2, '\b');
		assertEqualsNS("456", ns);
		assertEquals(0, cursor);

		ns = createNumericString("1.234.567", true);
		cursor = ns.delete(5, 6, '\b');
		assertEqualsNS("123.567", ns);
		assertEquals(3, cursor);

		ns = createNumericString("1.234", true);
		cursor = ns.delete(3, 4, '\b');
		assertEqualsNS("124", ns);
		assertEquals(2, cursor);

		ns = createNumericString("1.234.567", true);
		cursor = ns.delete(7, 8, '\b');
		assertEqualsNS("123.457", ns);
		assertEquals(6, cursor);

		ns = createNumericString("-1.234", true);
		cursor = ns.delete(4, 5, '\b');
		assertEqualsNS("-124", ns);
		assertEquals(3, cursor);

		ns = createNumericString("-1.234", true);
		cursor = ns.delete(1, 2, '\b');
		assertEqualsNS("-234", ns);
		assertEquals(1, cursor);

		ns = createNumericString("-1.234.567", true);
		cursor = ns.delete(8, 9, '\b');
		assertEqualsNS("-123.457", ns);
		assertEquals(7, cursor);
	}

	// helping methods
	//////////////////

	private void assertEqualsNS(String value, NumericString ns) {
		assertEquals(TestUtils.getLocalizedNumber(value), ns.toString());
	}

	private NumericString createNumericString(String value, boolean withGrouping) {
		return new NumericString(TestUtils.getLocalizedNumber(value), withGrouping);
	}
}
