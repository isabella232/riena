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

import org.eclipse.riena.ui.ridgets.IDateTextFieldRidget;
import org.eclipse.riena.ui.ridgets.validation.ValidIntermediateDate;

/**
 * Tests for the ValidIntermediateDate rule.
 */
public class ValidIntermediateDateTest extends TestCase {

	/**
	 * Test for problem report #484.
	 * 
	 * @throws Exception
	 *             handled by JUnit.
	 */
	public void testFirstMillenniumPartiallyInvalid() throws Exception {

		ValidIntermediateDate rule = new ValidIntermediateDate("dd.MM.yyyy");

		assertTrue(rule.validate("01.01.").isOK());
		assertTrue(rule.validate("01.01.0").isOK());
		assertTrue(rule.validate("01.01.01").isOK());
		assertFalse(rule.validate("01.01.010").isOK());
		assertFalse(rule.validate("01.01.0101").isOK());

		assertTrue(rule.validate("02.02.").isOK());
		assertTrue(rule.validate("02.02.2").isOK());
		assertTrue(rule.validate("02.02.22").isOK());
		assertTrue(rule.validate("02.02.222").isOK());
		assertTrue(rule.validate("02.02.2222").isOK());
	}

	/**
	 * Tests dates of format IDateTextFieldAdapter.FORMAT_DDMMYYYY
	 * 
	 * @throws Exception
	 *             handled by JUnit.
	 */
	public void testDatesDDMMYYYY() throws Exception {

		ValidIntermediateDate rule = new ValidIntermediateDate(IDateTextFieldRidget.FORMAT_DDMMYYYY);

		assertTrue(rule.validate("").isOK());
		assertTrue(rule.validate("..").isOK());
		assertTrue(rule.validate("11..").isOK());
		assertTrue(rule.validate("11.1.").isOK());
		assertTrue(rule.validate("11.1.1").isOK());
		assertFalse(rule.validate("55.1.1").isOK());
	}

	/**
	 * Tests dates of format IDateTextFieldAdapter.FORMAT_DDMMYYYYHHMM
	 * 
	 * @throws Exception
	 *             handled by JUnit.
	 */
	public void testDatesDDMMYYYYHHMM() throws Exception {

		ValidIntermediateDate rule = new ValidIntermediateDate(IDateTextFieldRidget.FORMAT_DDMMYYYYHHMM);

		assertTrue(rule.validate("11.1.1 11:11").isOK());
		assertTrue(rule.validate("11.01.01 :").isOK());
		assertTrue(rule.validate("11.1.1 :").isOK());
		assertFalse(rule.validate("11.1.1 55:11").isOK());
	}

	/**
	 * Tests dates of format IDateTextFieldAdapter.FORMAT_HHMM
	 * 
	 * @throws Exception
	 *             handled by JUnit.
	 */
	public void testDatesHHMM() throws Exception {

		ValidIntermediateDate rule = new ValidIntermediateDate(IDateTextFieldRidget.FORMAT_HHMM);

		assertTrue(rule.validate("11:11").isOK());
		assertTrue(rule.validate("11:00").isOK());
		assertTrue(rule.validate("11:0").isOK());
		assertTrue(rule.validate("11:").isOK());
		assertFalse(rule.validate("55:11").isOK());
	}

	/**
	 * Test for problem report #601.
	 * 
	 * @throws Exception
	 *             handled by JUnit.
	 */
	public void test601() throws Exception {

		ValidIntermediateDate rule = new ValidIntermediateDate("HH:mm");

		assertTrue(rule.validate("11:").isOK());
		assertFalse(rule.validate("55:").isOK());
	}

	/**
	 * Test for problem report #651.
	 * 
	 * @throws Exception
	 *             handled by JUnit.
	 */
	public void testFebruary29th() throws Exception {
		ValidIntermediateDate rule = new ValidIntermediateDate("dd.MM");

		assertTrue(rule.validate("28.02").isOK());
		assertTrue(rule.validate("29.02").isOK());
		assertFalse(rule.validate("30.02").isOK());

		rule = new ValidIntermediateDate("MM/dd");

		assertTrue(rule.validate("02/28").isOK());
		assertTrue(rule.validate("02/29").isOK());
		assertFalse(rule.validate("02/30").isOK());

		rule = new ValidIntermediateDate("dd.MM.yyyy");

		assertTrue(rule.validate("28.02.2007").isOK());
		assertFalse(rule.validate("29.02.2007").isOK());
		assertFalse(rule.validate("30.02.2007").isOK());
	}

}
