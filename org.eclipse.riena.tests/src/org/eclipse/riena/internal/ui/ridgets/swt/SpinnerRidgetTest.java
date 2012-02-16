/*******************************************************************************
 * Copyright (c) 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *    Florian Pirchner - added tests
 *******************************************************************************/
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.beans.PropertyChangeEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.ui.ridgets.ISpinnerRidget;
import org.eclipse.riena.ui.ridgets.ITraverseRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;

/**
 * Tests for the class {@link SpinnerRidget}.
 */
public class SpinnerRidgetTest extends AbstractTraverseRidgetTest {

	@Override
	protected Widget createWidget(final Composite parent) {
		return new Spinner(parent, SWT.NONE);
	}

	@Override
	protected ISpinnerRidget createRidget() {
		return new SpinnerRidget();
	}

	@Override
	protected Spinner getWidget() {
		return (Spinner) super.getWidget();
	}

	@Override
	protected ISpinnerRidget getRidget() {
		return (ISpinnerRidget) super.getRidget();
	}

	@Override
	protected int getIncrement(final Control control) {
		return ((Spinner) control).getIncrement();
	}

	@Override
	protected int getMaximum(final Control control) {
		return ((Spinner) control).getMaximum();
	}

	@Override
	protected int getMinimum(final Control control) {
		return ((Spinner) control).getMinimum();
	}

	@Override
	protected int getPageIncrement(final Control control) {
		return ((Spinner) control).getPageIncrement();
	}

	@Override
	protected int getValue(final Control control) {
		return ((Spinner) control).getSelection();
	}

	@Override
	protected void setValue(final Control control, final int value) {
		((Spinner) control).setSelection(value);
	}

	protected int getTextLimit(final Control control) {
		return ((Spinner) control).getTextLimit();
	}

	protected int getDigits(final Control control) {
		return ((Spinner) control).getDigits();
	}

	// helping methods
	// ////////////////

	public void testRidgetMapping() {
		final SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();
		assertSame(SpinnerRidget.class, mapper.getRidgetClass(getWidget()));
	}

	public void testSetDigits() {
		final ISpinnerRidget ridget = getRidget();
		final Spinner control = getWidget();

		/*
		 * Test 1 - initial values
		 */
		assertEquals(0, ridget.getDigits());
		assertEquals(0, control.getDigits());
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 2 - valid values
		 */
		ridget.setTextLimit(4);
		ridget.setDigits(2);
		assertEquals(2, ridget.getDigits());
		assertEquals(2, control.getDigits());
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 3 - digits > textLimit
		 */
		try {
			ridget.setTextLimit(4);
			ridget.setDigits(5);
			fail();
		} catch (final IllegalArgumentException e) {
			ok();
		}
		assertEquals(2, ridget.getDigits());
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 4 - digits > textLimit by descreasing textLimit
		 */
		ridget.setTextLimit(4);
		ridget.setDigits(3);

		ridget.setTextLimit(2);
		assertEquals(2, ridget.getDigits());
		assertEquals(2, control.getDigits());
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 5 - digits = 0
		 */
		ridget.setDigits(0);
		assertEquals(0, ridget.getDigits());
		assertEquals(0, control.getDigits());
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 6 - digits < 0
		 */
		ridget.setDigits(-1);
		assertEquals(0, ridget.getDigits());
		assertEquals(0, control.getDigits());
		assertPropertiesEqual(ridget, control);
	}

	public void testSetDigitsFiresEvents() {
		final ISpinnerRidget ridget = getRidget();
		ridget.setTextLimit(5);
		ridget.setDigits(1);

		/*
		 * Test 1 - default case
		 */
		expectPropertyChangeEvent(ISpinnerRidget.PROPERTY_DIGITS, 1, 3);
		ridget.setDigits(3);
		verifyPropertyChangeEvents();

		expectNoPropertyChangeEvent();
		ridget.setDigits(3);
		verifyPropertyChangeEvents();

		/*
		 * Test 2 - digits > textLimit
		 */
		try {
			resetEasyMock();
			expectNoPropertyChangeEvent();
			ridget.setDigits(ridget.getTextLimit() + 1);
		} catch (final IllegalArgumentException e) {
			verifyPropertyChangeEvents();
		}

		/*
		 * Test 3 - digits < 0
		 */
		resetEasyMock();
		ridget.setDigits(1);
		expectPropertyChangeEvent(ISpinnerRidget.PROPERTY_DIGITS, 1, 0);
		ridget.setDigits(-1);
		verifyPropertyChangeEvents();

		/*
		 * Test 4 - digits = 0
		 */
		resetEasyMock();
		ridget.setDigits(1);
		expectPropertyChangeEvent(ISpinnerRidget.PROPERTY_DIGITS, 1, 0);
		ridget.setDigits(0);
		verifyPropertyChangeEvents();
	}

	public void testSetTextLimit() {
		final ISpinnerRidget ridget = getRidget();
		final Spinner control = getWidget();
		//		assertEquals(SpinnerRidget.LIMIT, ridget.getTextLimit());

		/*
		 * Test 1 - valid values
		 */
		ridget.setTextLimit(9);
		assertEquals(9, ridget.getTextLimit());
		assertEquals(9, control.getTextLimit());
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 2 - textLimit == Spinner.LIMIT
		 */
		ridget.setTextLimit(SpinnerRidget.LIMIT);
		assertEquals(SpinnerRidget.LIMIT, ridget.getTextLimit());
		assertEquals(SpinnerRidget.LIMIT, control.getTextLimit());
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 2 - textLimit > Spinner.LIMIT
		 */
		ridget.setTextLimit(SpinnerRidget.LIMIT + 1);
		assertEquals(SpinnerRidget.LIMIT, ridget.getTextLimit());
		assertEquals(SpinnerRidget.LIMIT, control.getTextLimit());
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 3 - textLimit < 0
		 */
		ridget.setTextLimit(9);
		try {
			ridget.setTextLimit(-1);
			fail();
		} catch (final IllegalArgumentException e) {
			ok();
		}
		assertEquals(9, ridget.getTextLimit());
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 4 - textLimit == 0
		 */
		try {
			ridget.setTextLimit(0);
			fail();
		} catch (final IllegalArgumentException e) {
			ok();
		}
		assertEquals(9, ridget.getTextLimit());
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 5 - textLimit > digits
		 */
		ridget.setTextLimit(10);
		ridget.setDigits(5);
		assertEquals(10, ridget.getTextLimit());
		assertEquals(10, control.getTextLimit());
		assertEquals(5, ridget.getDigits());
		assertEquals(5, control.getDigits());
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 6 - textLimit < digits
		 */
		ridget.setTextLimit(2);
		assertEquals(2, ridget.getTextLimit());
		assertEquals(2, control.getTextLimit());
		assertEquals(2, ridget.getDigits());
		assertEquals(2, control.getDigits());
		assertPropertiesEqual(ridget, control);

	}

	@Override
	protected void assertPropertiesEqual(final ITraverseRidget ridget, final Control control) {
		super.assertPropertiesEqual(ridget, control);
		final SpinnerRidget spinnerRidget = (SpinnerRidget) ridget;
		assertEquals(getTextLimit(control), spinnerRidget.getTextLimit());
		assertEquals(getDigits(control), spinnerRidget.getDigits());
	}

	public void testSetTextLimitFiresEvents() {
		final ISpinnerRidget ridget = getRidget();
		ridget.setTextLimit(2);

		/*
		 * Test 1 - default case
		 */
		expectPropertyChangeEvent(ISpinnerRidget.PROPERTY_TEXT_LIMIT, 2, 4);
		ridget.setTextLimit(4);
		verifyPropertyChangeEvents();

		expectNoPropertyChangeEvent();
		ridget.setTextLimit(4);
		verifyPropertyChangeEvents();

		/*
		 * Test 2 - digits > textLimit
		 */
		resetEasyMock();
		ridget.setTextLimit(4);
		ridget.setDigits(3);
		expectPropertyChangeEvents(new PropertyChangeEvent(ridget, ISpinnerRidget.PROPERTY_DIGITS, 3, 2),
				new PropertyChangeEvent(ridget, ISpinnerRidget.PROPERTY_TEXT_LIMIT, 4, 2));
		ridget.setTextLimit(2);
		verifyPropertyChangeEvents();

		/*
		 * Test 3 - textLimit = 0
		 */
		try {
			resetEasyMock();
			expectNoPropertyChangeEvent();
			ridget.setTextLimit(0);
		} catch (final IllegalArgumentException e) {
			verifyPropertyChangeEvents();
		}
		/*
		 * Test4 - textLimit < 0
		 */
		try {
			resetEasyMock();
			expectNoPropertyChangeEvent();
			ridget.setTextLimit(-1);
			verifyPropertyChangeEvents();
		} catch (final IllegalArgumentException e) {
			verifyPropertyChangeEvents();
		}

		/*
		 * Test 5 - textLimit > LIMIT
		 */
		resetEasyMock();
		ridget.setTextLimit(4);
		expectPropertyChangeEvents(new PropertyChangeEvent(ridget, ISpinnerRidget.PROPERTY_TEXT_LIMIT, 4,
				SpinnerRidget.LIMIT));
		ridget.setTextLimit(SpinnerRidget.LIMIT + 1);
		verifyPropertyChangeEvents();
	}

	public void testApplyDigitAndTextLimitOnBind() {
		final ISpinnerRidget ridget = getRidget();
		final Spinner control = (Spinner) createWidget(getShell());
		assertNotSame(control, ridget.getUIControl());
		ridget.setTextLimit(4);
		ridget.setDigits(2);

		ridget.setUIControl(control);
		assertEquals(2, control.getDigits());
		assertEquals(4, control.getTextLimit());
	}
}
