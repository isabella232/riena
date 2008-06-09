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
package org.eclipse.riena.ui.ridgets.validation.tests;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

import junit.framework.TestCase;

import org.eclipse.riena.ui.ridgets.validation.ValidRange;

/**
 * Tests for the MaxLength rule.
 */
public class ValidRangeTest extends TestCase {

	/**
	 * @throws Exception
	 *             Handled by JUnit.
	 */
	public void testRangeUSlocale() throws Exception {
		ValidRange rule = new ValidRange(0, 10, Locale.US);
		assertTrue(rule.validate(null).isOK());
		assertTrue(rule.validate("").isOK());
		assertTrue(rule.validate("0").isOK());
		assertTrue(rule.validate("2.5").isOK());
		assertTrue(rule.validate("5").isOK());
		assertTrue(rule.validate("10").isOK());

		assertFalse(rule.validate("-0.0000001").isOK());
		assertFalse(rule.validate("10.0000001").isOK());

		rule = new ValidRange(-5000, 5000, Locale.US);
		assertTrue(rule.validate("-5000").isOK());
		assertTrue(rule.validate("-5,000").isOK());
		assertTrue(rule.validate("- 5,000").isOK());
		assertFalse(rule.validate("-5000.0001").isOK());
		assertFalse(rule.validate("-5,000.0001").isOK());

		rule = new ValidRange(10, 20, Locale.US);
		assertFalse(rule.validate(null).isOK());
		assertFalse(rule.validate("").isOK());
	}

	/**
	 * @throws Exception
	 *             Handled by JUnit.
	 */
	public void testRangeGermanLocale() throws Exception {
		ValidRange rule = new ValidRange(0, 10, Locale.GERMANY);
		assertTrue(rule.validate(null).isOK());
		assertTrue(rule.validate("").isOK());
		assertTrue(rule.validate("0").isOK());
		assertTrue(rule.validate("2,5").isOK());
		assertTrue(rule.validate("5").isOK());
		assertTrue(rule.validate("10").isOK());
		assertFalse(rule.validate("-0,0000001").isOK());
		assertFalse(rule.validate("10,0000001").isOK());

		rule = new ValidRange(-5000, 5000, Locale.GERMANY);
		assertTrue(rule.validate("-5000").isOK());
		assertTrue(rule.validate("-5.000").isOK());
		assertTrue(rule.validate("- 5.000").isOK());
		assertFalse(rule.validate("-5000,0001").isOK());
		assertFalse(rule.validate("-5.000,0001").isOK());

		rule = new ValidRange(10, 20, Locale.GERMANY);
		assertFalse(rule.validate(null).isOK());
		assertFalse(rule.validate("").isOK());
	}

	/**
	 * @throws Exception
	 *             Handled by JUnit.
	 */
	public void testRangeArabLocale() throws Exception {
		// Arab locales have a trailing minus
		ValidRange rule = new ValidRange(0, 10, new Locale("ar", "AE"));
		assertTrue(rule.validate(null).isOK());
		assertTrue(rule.validate("").isOK());
		assertTrue(rule.validate("0").isOK());
		assertTrue(rule.validate("2.5").isOK());
		assertTrue(rule.validate("5").isOK());
		assertTrue(rule.validate("10").isOK());
		assertFalse(rule.validate("0.0000001-").isOK());
		assertFalse(rule.validate("10.0000001").isOK());

		rule = new ValidRange(-5000, 5000, new Locale("ar", "AE"));
		assertTrue(rule.validate("5000-").isOK());
		assertTrue(rule.validate("5,000-").isOK());
		assertTrue(rule.validate("5,000 -").isOK());
		assertFalse(rule.validate("5000.0001-").isOK());
		assertFalse(rule.validate("5,000.0001 -").isOK());

		rule = new ValidRange(10, 20, new Locale("ar", "AE"));
		assertFalse(rule.validate(null).isOK());
		assertFalse(rule.validate("").isOK());
	}

	public void testConstructorInitTypes() throws Exception {
		ValidRange rule = new ValidRange((byte) -10, (byte) 10);
		assertTrue(rule.validate("10").isOK());
		rule = new ValidRange((short) -10, (short) 10);
		assertTrue(rule.validate("10").isOK());
		rule = new ValidRange((long) -10, (long) 10);
		assertTrue(rule.validate("10").isOK());
		rule = new ValidRange((float) -10, (float) 10);
		assertTrue(rule.validate("10").isOK());
		rule = new ValidRange((double) -10, (double) 10);
		assertTrue(rule.validate("10").isOK());
		rule = new ValidRange(BigInteger.ZERO, BigInteger.TEN);
		assertTrue(rule.validate("10").isOK());
		rule = new ValidRange(BigDecimal.ZERO, BigDecimal.TEN);
		assertTrue(rule.validate("10").isOK());
	}

	public void testUnparseableNumbers() throws Exception {
		final ValidRange rule = new ValidRange(0, 10, Locale.US);
		assertFalse(rule.validate("A10").isOK());
		assertFalse(rule.validate("1A0").isOK());
		assertFalse(rule.validate("10A").isOK());
	}

	/**
	 * @throws Exception
	 *             Handled by JUnit.
	 */
	public void testConstructorSanity() throws Exception {
		// different types:
		try {
			new ValidRange((byte) 10, (short) 10);
			fail("expected some RuntimeException");
		} catch (final RuntimeException e) {
			assert true : "test passed";
		}
		try {
			new ValidRange(10, 1000d);
			fail("expected some RuntimeException");
		} catch (final RuntimeException e) {
			assert true : "test passed";
		}
		// min greater max:
		try {
			new ValidRange(100, 10);
			fail("expected some RuntimeException");
		} catch (final RuntimeException e) {
			assert true : "test passed";
		}
		// null parameter
		try {
			new ValidRange(null, 10);
			fail("expected some RuntimeException");
		} catch (final RuntimeException e) {
			assert true : "test passed";
		}
		try {
			new ValidRange(100, null);
			fail("expected some RuntimeException");
		} catch (final RuntimeException e) {
			assert true : "test passed";
		}
		try {
			new ValidRange(null, null);
			fail("expected some RuntimeException");
		} catch (final RuntimeException e) {
			assert true : "test passed";
		}
		try {
			new ValidRange(10, 100, null);
			fail("expected some RuntimeException");
		} catch (final RuntimeException e) {
			assert true : "test passed";
		}
	}

}
