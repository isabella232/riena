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

import junit.framework.TestCase;

import org.eclipse.riena.ui.ridgets.validation.ValidDate;
import org.eclipse.riena.ui.ridgets.validation.ValidationFailure;

/**
 * Tests for the ValidDate rule.
 */
public class ValidDateTest extends TestCase {

	/**
	 * Checks for validity of an input string against the date format DDMMYYYY
	 * 
	 * @throws Exception
	 *             handled by JUnit.
	 */
	public final void testDateDDMMYYYYFormat() throws Exception {

		ValidDate rule = new ValidDate("dd.MM.yyyy");

		assertTrue(rule.validate("12.12.2004").isOK());
		assertTrue(rule.validate("30.09.1999").isOK());
		assertFalse(rule.validate("9.12.2004").isOK());
		assertFalse(rule.validate("44.12.2004").isOK());
		assertFalse(rule.validate("12.13.2004").isOK());
		assertFalse(rule.validate("12.2004").isOK());
	}

	/**
	 * Checks for validity of an input string against the date format MMYYYY
	 * 
	 * @throws Exception
	 *             handled by JUnit.
	 */
	public final void testDateMMYYYYFormat() throws Exception {

		ValidDate rule = new ValidDate("MM.yyyy");

		assertTrue(rule.validate("12.2004").isOK());
		assertTrue(rule.validate("09.1999").isOK());
		assertFalse(rule.validate("13.2004").isOK());
		assertFalse(rule.validate("2004").isOK());
	}

	/**
	 * Checks for validity of an input string against the date format YYYY
	 * 
	 * @throws Exception
	 *             handled by JUnit.
	 */
	public final void testDateYYYYFormat() throws Exception {

		ValidDate rule = new ValidDate("yyyy");

		assertTrue(rule.validate("2004").isOK());

		assertFalse(rule.validate("12.2004").isOK());
		assertFalse(rule.validate("004").isOK());
	}

	/**
	 * Test for problem report #651.
	 * 
	 * @throws Exception
	 *             handled by JUnit.
	 */
	public void testFebruary29th() throws Exception {
		ValidDate rule = new ValidDate("dd.MM");

		assertTrue(rule.validate("28.02").isOK());
		assertTrue(rule.validate("29.02").isOK());
		assertFalse(rule.validate("30.02").isOK());

		rule = new ValidDate("MM/dd");

		assertTrue(rule.validate("02/28").isOK());
		assertTrue(rule.validate("02/29").isOK());
		assertFalse(rule.validate("02/30").isOK());

		rule = new ValidDate("dd.MM.yyyy");

		assertTrue(rule.validate("28.02.2007").isOK());
		assertFalse(rule.validate("29.02.2007").isOK());
		assertFalse(rule.validate("30.02.2007").isOK());
	}

	public void testException() throws Exception {
		try {
			ValidDate rule = new ValidDate("dd.MM");
			rule.validate(new Object());
			fail("expected thrown ValidationFailure.");
		} catch (final ValidationFailure e) {
			// passed
		} catch (final RuntimeException e) {
			fail("expected ValidationFailure instead of " + e.getClass().getName());
		}
	}
}
