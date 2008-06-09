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

import java.text.DecimalFormat;
import java.util.Locale;

import junit.framework.TestCase;

import org.eclipse.riena.ui.ridgets.validation.MaxNumberLength;
import org.eclipse.riena.ui.ridgets.validation.ValidationFailure;

/**
 * Tests for the MaxNumberLength rule.
 */
public class MaxNumberLengthTest extends TestCase {

	/**
	 * @throws Exception
	 *             Handled by JUnit.
	 */
	public void testUSLocale() throws Exception {
		// locale has comma as grouping-separator and leading minus
		final MaxNumberLength maxNumberLength = new MaxNumberLength(7, Locale.US);

		try {
			maxNumberLength.validate(new Object());
			fail("expected thrown " + ValidationFailure.class.getName());
		} catch (final ValidationFailure e) {
			assert true : "passed test";
		} catch (final RuntimeException e) {
			fail("expected thrown " + ValidationFailure.class.getName());
		}

		assertTrue(maxNumberLength.validate(null).isOK());

		assertTrue(maxNumberLength.validate("1234567").isOK());
		assertTrue(maxNumberLength.validate("1,234,567").isOK());

		assertTrue(maxNumberLength.validate("1234567").isOK());
		assertFalse(maxNumberLength.validate("12,345,678").isOK());

		assertTrue(maxNumberLength.validate("-1234567").isOK());
		assertTrue(maxNumberLength.validate("-1,234,567").isOK());

		assertTrue(maxNumberLength.validate("- 1234567").isOK());
		assertTrue(maxNumberLength.validate("- 1,234,567").isOK());

		assertFalse(maxNumberLength.validate("-12345678").isOK());
		assertFalse(maxNumberLength.validate("-12,345.678").isOK());

		assertTrue(maxNumberLength.validate("- 1234567").isOK());
		assertTrue(maxNumberLength.validate("- 1,234,567").isOK());

		assertFalse(maxNumberLength.validate("- 12345678").isOK());
		assertFalse(maxNumberLength.validate("- 12,345.678").isOK());

	}

	public void testGermanLocale() throws Exception {
		// locale has dot as grouping-separator and leading minus
		final MaxNumberLength maxNumberLength = new MaxNumberLength(7, Locale.GERMANY);

		try {
			maxNumberLength.validate(new Object());
			fail("expected thrown " + ValidationFailure.class.getName());
		} catch (final ValidationFailure e) {
			assert true : "passed test";
		} catch (final RuntimeException e) {
			fail("expected thrown " + ValidationFailure.class.getName());
		}

		assertTrue(maxNumberLength.validate(null).isOK());

		assertTrue(maxNumberLength.validate("1234567").isOK());
		assertTrue(maxNumberLength.validate("1.234.567").isOK());

		assertTrue(maxNumberLength.validate("1234567").isOK());
		assertFalse(maxNumberLength.validate("12.345.678").isOK());

		assertTrue(maxNumberLength.validate("-1234567").isOK());
		assertTrue(maxNumberLength.validate("-1.234.567").isOK());

		assertTrue(maxNumberLength.validate("- 1234567").isOK());
		assertTrue(maxNumberLength.validate("- 1.234.567").isOK());

		assertFalse(maxNumberLength.validate("-12345678").isOK());
		assertFalse(maxNumberLength.validate("-12.345.678").isOK());

		assertFalse(maxNumberLength.validate("- 12345678").isOK());
		assertFalse(maxNumberLength.validate("- 12.345.678").isOK());
	}

	public void testFrenchLocale() throws Exception {
		// locale has char 0xA0 as grouping separator (which may be a blank
		// aswell) and leading minus
		final MaxNumberLength maxNumberLength = new MaxNumberLength(7, Locale.FRANCE);

		try {
			maxNumberLength.validate(new Object());
			fail("expected thrown " + ValidationFailure.class.getName());
		} catch (final ValidationFailure e) {
			assert true : "passed test";
		} catch (final RuntimeException e) {
			fail("expected thrown " + ValidationFailure.class.getName());
		}

		assertTrue(maxNumberLength.validate(null).isOK());

		assertTrue(maxNumberLength.validate("1234567").isOK());
		assertTrue(maxNumberLength.validate("1 234 567").isOK());

		assertTrue(maxNumberLength.validate("1234567").isOK());
		assertFalse(maxNumberLength.validate("12 345 678").isOK());

		assertTrue(maxNumberLength.validate("-1234567").isOK());
		assertTrue(maxNumberLength.validate("-1 234 567").isOK());

		assertTrue(maxNumberLength.validate("- 1234567").isOK());
		assertTrue(maxNumberLength.validate("- 1 234 567").isOK());

		assertFalse(maxNumberLength.validate("-12345678").isOK());
		assertFalse(maxNumberLength.validate("-12 345 678").isOK());

		assertFalse(maxNumberLength.validate("- 12345678").isOK());
		assertFalse(maxNumberLength.validate("- 12 345 678").isOK());

		// uses the 0xa0 grouping-separator character:
		assertTrue(maxNumberLength.validate(DecimalFormat.getInstance(Locale.FRANCE).format(1234567)).isOK());
		assertFalse(maxNumberLength.validate(DecimalFormat.getInstance(Locale.FRANCE).format(12345678)).isOK());
	}

	public void testArabLocale() throws Exception {
		// locale has a comma as grouping-separator character and
		// features a trailing minus instead of a leading one

		final MaxNumberLength maxNumberLength = new MaxNumberLength(7, new Locale("ar", "AE"));

		try {
			maxNumberLength.validate(new Object());
			fail("expected thrown " + ValidationFailure.class.getName());
		} catch (final ValidationFailure e) {
			assert true : "passed test";
		} catch (final RuntimeException e) {
			fail("expected thrown " + ValidationFailure.class.getName());
		}

		assertTrue(maxNumberLength.validate(null).isOK());

		assertTrue(maxNumberLength.validate("1234567").isOK());
		assertTrue(maxNumberLength.validate("1,234,567").isOK());

		assertTrue(maxNumberLength.validate("1234567").isOK());
		assertFalse(maxNumberLength.validate("12,345,678").isOK());

		assertTrue(maxNumberLength.validate("1234567-").isOK());
		assertTrue(maxNumberLength.validate("1,234,567-").isOK());

		assertTrue(maxNumberLength.validate("1234567 -").isOK());
		assertTrue(maxNumberLength.validate("1,234,567 -").isOK());

		assertFalse(maxNumberLength.validate("12345678-").isOK());
		assertFalse(maxNumberLength.validate("12,345,678-").isOK());

		assertFalse(maxNumberLength.validate("12345678 -").isOK());
		assertFalse(maxNumberLength.validate("12,345,678 -").isOK());

		assertTrue(maxNumberLength.validate(DecimalFormat.getInstance(new Locale("ar", "AE")).format(1234567)).isOK());
		assertFalse(maxNumberLength.validate(DecimalFormat.getInstance(new Locale("ar", "AE")).format(12345678)).isOK());

	}
}
