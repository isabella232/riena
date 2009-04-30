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
package org.eclipse.riena.ui.ridgets.validation.tests;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

import org.eclipse.riena.tests.RienaTestCase;
import org.eclipse.riena.tests.TestUtils;
import org.eclipse.riena.tests.collect.NonUITestCase;
import org.eclipse.riena.ui.ridgets.validation.ValidRange;

/**
 * Tests for the MaxLength rule.
 */
@NonUITestCase
public class ValidRangeTest extends RienaTestCase {

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

		if (!TestUtils.isArabLocaleAvailable()) {
			System.err
					.println(getClass().getName()
							+ ".testRangeArabLocale(): Skipping test because no Arab locale is available. Use international JRE to run all tests.");
			return;
		}

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
			ok("passed test");
		}
		try {
			new ValidRange(10, 1000d);
			fail("expected some RuntimeException");
		} catch (final RuntimeException e) {
			ok("passed test");
		}
		// min greater max:
		try {
			new ValidRange(100, 10);
			fail("expected some RuntimeException");
		} catch (final RuntimeException e) {
			ok("passed test");
		}
		// null parameter
		try {
			new ValidRange(null, 10);
			fail("expected some RuntimeException");
		} catch (final RuntimeException e) {
			ok("passed test");
		}
		try {
			new ValidRange(100, null);
			fail("expected some RuntimeException");
		} catch (final RuntimeException e) {
			ok("passed test");
		}
		try {
			new ValidRange(null, null);
			fail("expected some RuntimeException");
		} catch (final RuntimeException e) {
			ok("passed test");
		}
		try {
			new ValidRange(10, 100, null);
			fail("expected some RuntimeException");
		} catch (final RuntimeException e) {
			ok("passed test");
		}
	}

	public void testSetInitializationData() throws Exception {

		ValidRange rule = new ValidRange();
		assertTrue(rule.validate("0").isOK());
		assertFalse(rule.validate("10").isOK());

		rule = new ValidRange();
		rule.setInitializationData(null, null, "1");
		assertFalse(rule.validate("1").isOK());
		assertFalse(rule.validate("10").isOK());

		rule = new ValidRange();
		rule.setInitializationData(null, null, "1,10");
		assertTrue(rule.validate("1").isOK());
		assertTrue(rule.validate("10").isOK());
		assertFalse(rule.validate("0").isOK());
		assertFalse(rule.validate("11").isOK());

		rule = new ValidRange();
		rule.setInitializationData(null, null, "1.1,10.1");
		assertFalse(rule.validate("1").isOK());
		assertTrue(rule.validate("2").isOK());
		assertTrue(rule.validate("10").isOK());
		assertFalse(rule.validate("0").isOK());
		assertFalse(rule.validate("11").isOK());

		rule = new ValidRange();
		String localString = Locale.US.getLanguage() + "," + Locale.US.getCountry();
		rule.setInitializationData(null, null, "1.1,10.1," + localString);
		assertFalse(rule.validate("1").isOK());
		assertTrue(rule.validate("1.1").isOK());

		rule = new ValidRange();
		localString = Locale.GERMANY.getLanguage() + "," + Locale.GERMANY.getCountry();
		rule.setInitializationData(null, null, "1.1,10.1," + localString);
		assertFalse(rule.validate("1").isOK());
		assertTrue(rule.validate("1,1").isOK());

	}

}
