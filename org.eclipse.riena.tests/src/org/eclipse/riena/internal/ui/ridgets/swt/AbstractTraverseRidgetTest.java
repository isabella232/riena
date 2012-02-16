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

import org.easymock.EasyMock;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.beans.common.TypedBean;
import org.eclipse.riena.internal.ui.swt.test.UITestHelper;
import org.eclipse.riena.ui.ridgets.ITraverseRidget;

/**
 * Tests for the class {@link AbstractTraverseRidget}.
 */
public abstract class AbstractTraverseRidgetTest extends AbstractSWTRidgetTest {

	@Override
	protected ITraverseRidget getRidget() {
		return (ITraverseRidget) super.getRidget();
	}

	@Override
	protected Control getWidget() {
		return (Control) super.getWidget();
	}

	protected abstract int getValue(Control control);

	protected abstract int getIncrement(Control control);

	protected abstract int getPageIncrement(Control control);

	protected abstract int getMinimum(Control control);

	protected abstract int getMaximum(Control control);

	protected abstract void setValue(Control control, int value);

	// testing methods
	// ////////////////

	public void testSetup() {
		final ITraverseRidget ridget = getRidget();
		final Control control = getWidget();

		assertPropertiesEqual(ridget, control);
	}

	protected void assertPropertiesEqual(final ITraverseRidget ridget, final Control control) {
		assertEquals(getValue(control), ridget.getValue());
		assertEquals(getIncrement(control), ridget.getIncrement());
		assertEquals(getMaximum(control), ridget.getMaximum());
		assertEquals(getMinimum(control), ridget.getMinimum());
		assertEquals(getPageIncrement(control), ridget.getPageIncrement());
	}

	public void testSetValue() {
		final ITraverseRidget ridget = getRidget();

		/*
		 * Test 1 - allowed values
		 */
		final Control control = getWidget();
		ridget.setMaximum(30);
		ridget.setMinimum(0);
		ridget.setValue(10);

		assertEquals(10, ridget.getValue());
		assertEquals(10, getValue(control));
		assertEquals(30, ridget.getMaximum());
		assertEquals(30, getMaximum(control));
		assertEquals(0, ridget.getMinimum());
		assertEquals(0, getMinimum(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 2 - value < 0
		 */
		ridget.setMaximum(30);
		ridget.setMinimum(10);
		ridget.setValue(-1);

		assertEquals(10, ridget.getValue());
		assertEquals(10, getValue(control));
		assertEquals(30, ridget.getMaximum());
		assertEquals(30, getMaximum(control));
		assertEquals(10, ridget.getMinimum());
		assertEquals(10, getMinimum(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 3 - value < min
		 */
		ridget.setMaximum(30);
		ridget.setMinimum(10);
		ridget.setValue(1);

		assertEquals(10, ridget.getValue());
		assertEquals(10, getValue(control));
		assertEquals(30, ridget.getMaximum());
		assertEquals(30, getMaximum(control));
		assertEquals(10, ridget.getMinimum());
		assertEquals(10, getMinimum(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 4 - min > value
		 */
		// initialize
		ridget.setMaximum(29);
		ridget.setMinimum(11);

		// test
		ridget.setValue(10);
		assertEquals(11, ridget.getValue());
		assertEquals(11, getValue(control));
		assertEquals(29, ridget.getMaximum());
		assertEquals(29, getMaximum(control));
		assertEquals(11, ridget.getMinimum());
		assertEquals(11, getMinimum(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 5 - min = value
		 */
		ridget.setMinimum(11);

		assertEquals(11, ridget.getValue());
		assertEquals(11, getValue(control));
		assertEquals(29, ridget.getMaximum());
		assertEquals(29, getMaximum(control));
		assertEquals(11, ridget.getMinimum());
		assertEquals(11, getMinimum(control));
		assertPropertiesEqual(ridget, control);

	}

	/**
	 * This tests are ridget specific tests. Currently the SliderRidget uses
	 * differing ones.
	 */
	public void testSetValueRidgetBased() {
		final Control control = getWidget();
		final ITraverseRidget ridget = getRidget();

		/*
		 * Test 1 - value > max
		 */
		ridget.setMaximum(30);
		ridget.setMinimum(10);
		ridget.setValue(40);

		assertEquals(30, ridget.getValue());
		assertEquals(30, getValue(control));
		assertEquals(30, ridget.getMaximum());
		assertEquals(30, getMaximum(control));
		assertEquals(10, ridget.getMinimum());
		assertEquals(10, getMinimum(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 2 - max < value
		 */
		ridget.setValue(20);

		assertEquals(20, ridget.getValue());
		assertEquals(20, getValue(control));
		assertEquals(30, ridget.getMaximum());
		assertEquals(30, getMaximum(control));
		assertEquals(10, ridget.getMinimum());
		assertEquals(10, getMinimum(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 3 - max = value
		 */
		ridget.setMaximum(29);
		ridget.setValue(29);

		assertEquals(29, ridget.getValue());
		assertEquals(29, getValue(control));
		assertEquals(29, ridget.getMaximum());
		assertEquals(29, getMaximum(control));
		assertEquals(10, ridget.getMinimum());
		assertEquals(10, getMinimum(control));
		assertPropertiesEqual(ridget, control);
	}

	public void testSetValueFiresEvents() {
		final ITraverseRidget ridget = getRidget();
		ridget.setMaximum(20);
		ridget.setValue(0);

		expectPropertyChangeEvents(new PropertyChangeEvent(ridget, "valueInternal", 0, 10), new PropertyChangeEvent(
				ridget, ITraverseRidget.PROPERTY_VALUE, 0, 10));
		ridget.setValue(10);
		verifyPropertyChangeEvents();

		expectNoPropertyChangeEvent();
		ridget.setValue(10);
		verifyPropertyChangeEvents();

	}

	/**
	 * This tests are ridget specific tests. Currently the SliderRidget uses
	 * differing ones.
	 */
	public void testSetValueFiresEventsRidgetBased() {
		final ITraverseRidget ridget = getRidget();
		ridget.setMaximum(20);
		ridget.setValue(0);
		assertEquals(20, ridget.getMaximum());
		assertEquals(0, ridget.getValue());

		// Test 1 - value > max
		expectPropertyChangeEvents(new PropertyChangeEvent(ridget, "valueInternal", 0, 20), new PropertyChangeEvent(
				ridget, ITraverseRidget.PROPERTY_VALUE, 0, 20));
		ridget.setValue(30);
		verifyPropertyChangeEvents();

		// Test 2 - value < min
		resetEasyMock();
		ridget.setMinimum(10);
		expectPropertyChangeEvents(new PropertyChangeEvent(ridget, "valueInternal", 20, 10), new PropertyChangeEvent(
				ridget, ITraverseRidget.PROPERTY_VALUE, 20, 10));
		ridget.setValue(1);
		verifyPropertyChangeEvents();
	}

	public void testSetIncrement() {
		final ITraverseRidget ridget = getRidget();
		final Control control = getWidget();

		/*
		 * Valid increment
		 */
		ridget.setMinimum(0);
		ridget.setMaximum(100);
		ridget.setIncrement(2);

		assertEquals(2, ridget.getIncrement());
		assertEquals(2, getIncrement(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 1 - increment < 0
		 */
		ridget.setIncrement(-1);
		assertEquals(1, ridget.getIncrement());
		assertEquals(1, getIncrement(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 2 - increment = 0
		 */
		ridget.setIncrement(0);
		assertEquals(1, ridget.getIncrement());
		assertEquals(1, getIncrement(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 3 - increment > pageIncrement
		 */
		ridget.setMaximum(20);
		ridget.setMinimum(0);
		assertEquals(20, ridget.getMaximum());
		assertEquals(0, ridget.getMinimum());
		ridget.setIncrement(10);
		ridget.setPageIncrement(5);
		assertEquals(10, ridget.getIncrement());
		assertEquals(10, getIncrement(control));
		assertEquals(5, ridget.getPageIncrement());
		assertEquals(5, getPageIncrement(control));
		assertPropertiesEqual(ridget, control);

	}

	/**
	 * This tests are ridget specific tests. Currently the SliderRidget uses
	 * differing ones.
	 * 
	 */
	public void testSetIncrementRidgetBased() {
		final ITraverseRidget ridget = getRidget();
		final Control control = getWidget();

		/*
		 * Test 1 - increment > max - min. Result: increment = max - min
		 */
		final int deltaMaxMin = 10 - 7;
		ridget.setMinimum(7);
		ridget.setMaximum(10);
		assertEquals(10, ridget.getMaximum());
		assertEquals(7, ridget.getMinimum());

		ridget.setIncrement(deltaMaxMin + 3);
		assertEquals(deltaMaxMin, ridget.getIncrement());
		assertEquals(deltaMaxMin, getIncrement(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 2 - increment = max - min. Result: increment = max - min
		 */
		ridget.setIncrement(deltaMaxMin);
		assertEquals(deltaMaxMin, ridget.getIncrement());
		assertEquals(deltaMaxMin, getIncrement(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 3 - increment < max - min
		 */
		ridget.setIncrement(deltaMaxMin - 1);
		assertEquals(deltaMaxMin - 1, ridget.getIncrement());
		assertEquals(deltaMaxMin - 1, getIncrement(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 4 - increment > max - min after decreasing max
		 */
		ridget.setMaximum(20);
		ridget.setMinimum(0);
		assertEquals(20, ridget.getMaximum());
		assertEquals(0, ridget.getMinimum());

		ridget.setIncrement(20);
		assertEquals(20, ridget.getIncrement());
		assertEquals(20, getIncrement(control));
		assertPropertiesEqual(ridget, control);

		ridget.setMaximum(10);
		assertEquals(10, ridget.getIncrement());
		assertEquals(10, getIncrement(control));
		assertPropertiesEqual(ridget, control);
	}

	public void testSetIncrementFiresEvents() {
		final ITraverseRidget ridget = getRidget();
		ridget.setIncrement(1);

		/*
		 * Test 1 default
		 */
		expectPropertyChangeEvent(ITraverseRidget.PROPERTY_INCREMENT, 1, 2);
		ridget.setIncrement(2);
		verifyPropertyChangeEvents();

		expectNoPropertyChangeEvent();
		ridget.setIncrement(2);
		verifyPropertyChangeEvents();

		/*
		 * Test 2 - increment < 0
		 */
		expectPropertyChangeEvent(ITraverseRidget.PROPERTY_INCREMENT, 2, 1);
		ridget.setIncrement(-1);
		verifyPropertyChangeEvents();

	}

	/**
	 * This tests are ridget specific tests. Currently the SliderRidget uses
	 * differing ones.
	 */
	public void testSetIncrementFiresEventsRidgetBased() {
		final ITraverseRidget ridget = getRidget();
		ridget.setIncrement(1);

		/*
		 * Test 1 - increment > max - min
		 */
		resetEasyMock();
		ridget.setMaximum(20);
		ridget.setMinimum(10);
		assertEquals(20, ridget.getMaximum());
		assertEquals(10, ridget.getMinimum());

		final int deltaMaxMin = 20 - 10;
		expectPropertyChangeEvent(ITraverseRidget.PROPERTY_INCREMENT, 1, deltaMaxMin);
		ridget.setIncrement(20);
		verifyPropertyChangeEvents();

	}

	/**
	 * This tests are ridget specific tests. Currently the SliderRidget uses
	 * differing ones.
	 */
	public void testSetMaximumRidgetBased() {
		final Control control = getWidget();
		final ITraverseRidget ridget = getRidget();

		ridget.setMaximum(40);
		ridget.setMinimum(5);
		ridget.setValue(21);
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 1 - valid max
		 */
		assertEquals(40, ridget.getMaximum());
		assertEquals(40, getMaximum(control));
		assertEquals(21, ridget.getValue());
		assertEquals(21, getValue(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 2 - max < value
		 */
		ridget.setMaximum(20);
		assertEquals(20, ridget.getMaximum());
		assertEquals(20, getMaximum(control));
		assertEquals(20, ridget.getValue());
		assertEquals(20, getValue(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 3 - max < min
		 */
		try {
			ridget.setMaximum(4);
			fail();
		} catch (final IllegalArgumentException e) {
			ok();
		}
		assertEquals(20, ridget.getValue());
		assertEquals(20, ridget.getMaximum());
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 4 - max = min
		 */
		try {
			ridget.setMaximum(5);
			fail();
		} catch (final IllegalArgumentException e) {
			ok();
		}
		assertEquals(20, ridget.getValue());
		assertEquals(20, ridget.getMaximum());
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 5 - max < 0
		 */
		try {
			ridget.setMaximum(-1);
			fail();
		} catch (final IllegalArgumentException e) {
			ok();
		}
		assertEquals(20, ridget.getValue());
		assertEquals(20, ridget.getMaximum());
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 6 - max = 1 and > min.
		 */
		ridget.setMinimum(0);
		ridget.setMaximum(1);
		assertEquals(1, ridget.getMaximum());
		assertEquals(1, getMaximum(control));
		assertEquals(1, ridget.getValue());
		assertEquals(1, getValue(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 7 - increment > max - min after decreasing max
		 */
		ridget.setMinimum(0);
		int maxIncrement = 20 - 1;
		ridget.setMaximum(20);
		ridget.setMinimum(1);
		// prepare
		ridget.setIncrement(20);
		assertEquals(maxIncrement, ridget.getIncrement());
		assertEquals(maxIncrement, getIncrement(control));
		assertPropertiesEqual(ridget, control);
		// test
		maxIncrement = 10 - 1;
		ridget.setMaximum(10);
		assertEquals(maxIncrement, ridget.getIncrement());
		assertEquals(maxIncrement, getIncrement(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 8 - pageIncrement > max - thumb - min after decreasing max
		 */
		int maxPageIncrement = 20 - 1;
		ridget.setMaximum(20);
		ridget.setMinimum(1);
		// prepare
		ridget.setPageIncrement(20);
		assertEquals(maxPageIncrement, ridget.getPageIncrement());
		assertEquals(maxPageIncrement, getPageIncrement(control));
		assertPropertiesEqual(ridget, control);
		// test
		maxPageIncrement = 10 - 1;
		ridget.setMaximum(10);
		assertEquals(maxPageIncrement, ridget.getPageIncrement());
		assertEquals(maxPageIncrement, getPageIncrement(control));
		assertPropertiesEqual(ridget, control);
	}

	public void testSetMaximumFiresEvents() {
		int max = 30;
		final int min = 5;
		final ITraverseRidget ridget = getRidget();
		ridget.setMaximum(max);
		ridget.setMinimum(min);
		ridget.setIncrement(1);
		ridget.setPageIncrement(1);
		assertEquals(max, ridget.getMaximum());
		assertEquals(min, ridget.getMinimum());
		assertEquals(1, ridget.getPageIncrement());
		assertEquals(1, ridget.getIncrement());

		/*
		 * Test 1 - fire maximum changed
		 */
		expectPropertyChangeEvent(ITraverseRidget.PROPERTY_MAXIMUM, max, 50);
		ridget.setMaximum(50);
		verifyPropertyChangeEvents();

		expectNoPropertyChangeEvent();
		max = 50;
		ridget.setMaximum(max);
		verifyPropertyChangeEvents();

		/*
		 * Test 2 - max = 0
		 */
		try {
			expectNoPropertyChangeEvent();
			ridget.setMaximum(0);
		} catch (final IllegalArgumentException e) {
			verifyPropertyChangeEvents();
		}

		/*
		 * Test 3 - max < min
		 */
		try {
			expectNoPropertyChangeEvent();
			ridget.setMaximum(ridget.getMinimum() - 1);
		} catch (final IllegalArgumentException e) {
			verifyPropertyChangeEvents();
		}

		/*
		 * Test 4 - max < 0 and min 0
		 */
		try {
			resetEasyMock();
			ridget.setMinimum(0);
			expectNoPropertyChangeEvent();
			ridget.setMaximum(-1);
		} catch (final IllegalArgumentException e) {
			verifyPropertyChangeEvents();
		}
	}

	/**
	 * This tests are ridget specific tests. Currently the SliderRidget uses
	 * differing ones.
	 */
	public void testSetMaximumFiresEventsRidgetBased() {
		final ITraverseRidget ridget = getRidget();

		/*
		 * Test 1 - increment and page increment > max - min by decreasing max
		 */
		resetEasyMock();
		final int max = 20;
		final int min = 10;
		ridget.setMaximum(max);
		ridget.setMinimum(min);
		assertEquals(max, ridget.getMaximum());
		assertEquals(min, ridget.getMinimum());

		final int deltaMaxMin = max - min;
		ridget.setIncrement(deltaMaxMin);
		ridget.setPageIncrement(deltaMaxMin);
		final int oldPageIncrement = ridget.getPageIncrement();
		expectPropertyChangeEvents(new PropertyChangeEvent(ridget, ITraverseRidget.PROPERTY_INCREMENT, deltaMaxMin,
				deltaMaxMin - 1), new PropertyChangeEvent(ridget, ITraverseRidget.PROPERTY_PAGE_INCREMENT,
				oldPageIncrement, deltaMaxMin - 1), new PropertyChangeEvent(ridget, ITraverseRidget.PROPERTY_MAXIMUM,
				max, max - 1));
		ridget.setMaximum(max - 1);
		verifyPropertyChangeEvents();

		/*
		 * Test 2 - max < value. Result: value = max.
		 */
		final int newMax = 30;
		resetEasyMock();
		ridget.setMaximum(50);
		ridget.setValue(40);
		expectPropertyChangeEvents(new PropertyChangeEvent(ridget, "valueInternal", 40, newMax),
				new PropertyChangeEvent(ridget, ITraverseRidget.PROPERTY_VALUE, 40, newMax), new PropertyChangeEvent(
						ridget, ITraverseRidget.PROPERTY_MAXIMUM, 50, newMax));
		ridget.setMaximum(newMax);
		verifyPropertyChangeEvents();

		expectNoPropertyChangeEvent();
		ridget.setMaximum(newMax);
		verifyPropertyChangeEvents();

	}

	protected void resetEasyMock() {
		EasyMock.reset(propertyChangeListenerMock);
	}

	public void testSetMinimum() {
		final ITraverseRidget ridget = getRidget();
		final Control control = getWidget();

		/*
		 * Test 1 - valid min
		 */
		ridget.setMaximum(50);
		ridget.setMinimum(0);
		ridget.setValue(5);
		assertEquals(0, ridget.getMinimum());
		assertEquals(0, getMinimum(control));
		assertEquals(5, ridget.getValue());
		assertEquals(5, getValue(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 2 - min > value.
		 */
		ridget.setMinimum(20);
		assertEquals(20, ridget.getMinimum());
		assertEquals(20, getMinimum(control));
		assertEquals(20, ridget.getValue());
		assertEquals(20, getValue(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 3 - min = value.
		 */
		ridget.setMinimum(20);
		assertEquals(20, ridget.getMinimum());
		assertEquals(20, getMinimum(control));
		assertEquals(20, ridget.getValue());
		assertEquals(20, getValue(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 4 - min < value.
		 */
		ridget.setMinimum(10);
		assertEquals(10, ridget.getMinimum());
		assertEquals(10, getMinimum(control));
		assertEquals(20, ridget.getValue());
		assertEquals(20, getValue(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 5 - min > max
		 */
		ridget.setMinimum(20);

		try {
			ridget.setMinimum(100);
			fail();
		} catch (final IllegalArgumentException e) {
			ok();
		}
		assertEquals(20, ridget.getMinimum());
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 6 - min = max
		 */
		try {
			ridget.setMinimum(50);
			fail();
		} catch (final IllegalArgumentException e) {
			ok();
		}
		assertEquals(20, ridget.getMinimum());
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 7 - min < 0
		 */
		try {
			ridget.setMinimum(-10);
			fail();
		} catch (final IllegalArgumentException e) {
			ok();
		}
		assertEquals(20, ridget.getMinimum());
		assertPropertiesEqual(ridget, control);
	}

	public void testSetMinimumFiresEvents() {
		final ITraverseRidget ridget = getRidget();
		ridget.setMaximum(30);
		ridget.setMinimum(0);
		ridget.setValue(10);

		/*
		 * Test 1 - default
		 */
		expectPropertyChangeEvents(new PropertyChangeEvent(ridget, ITraverseRidget.PROPERTY_MINIMUM, 0, 5));
		ridget.setMinimum(5);
		verifyPropertyChangeEvents();

		expectNoPropertyChangeEvent();
		ridget.setMinimum(5);
		verifyPropertyChangeEvents();

		resetEasyMock();
		ridget.setMinimum(0);
		ridget.setValue(0);

		expectPropertyChangeEvents(new PropertyChangeEvent(ridget, "valueInternal", 0, 5), new PropertyChangeEvent(
				ridget, ITraverseRidget.PROPERTY_VALUE, 0, 5), new PropertyChangeEvent(ridget,
				ITraverseRidget.PROPERTY_MINIMUM, 0, 5));
		ridget.setMinimum(5);
		verifyPropertyChangeEvents();

		expectNoPropertyChangeEvent();
		ridget.setMinimum(5);
		verifyPropertyChangeEvents();

		/*
		 * Test 2 - min = 0
		 */
		expectPropertyChangeEvents(new PropertyChangeEvent(ridget, ITraverseRidget.PROPERTY_MINIMUM, 5, 0));
		ridget.setMinimum(0);
		verifyPropertyChangeEvents();

		/*
		 * Test 3 - min < 0
		 */
		try {
			resetEasyMock();
			expectNoPropertyChangeEvent();
			ridget.setMinimum(-1);
		} catch (final IllegalArgumentException e) {
			verifyPropertyChangeEvents();
		}
	}

	/**
	 * This tests are ridget specific tests. Currently the SliderRidget uses
	 * differing ones.
	 */
	public void testSetMinimumFiresEventsRidgetBased() {
		final ITraverseRidget ridget = getRidget();

		/*
		 * Test 3 - increment and pageIncrement > max - min by increasing min
		 */
		final int max = 20;
		final int min = 10;
		resetEasyMock();
		ridget.setMaximum(max);
		ridget.setMinimum(min);
		ridget.setValue(min);
		assertEquals(max, ridget.getMaximum());
		assertEquals(min, ridget.getMinimum());
		assertEquals(min, ridget.getValue());

		final int deltaMaxMin = max - min;
		final int newMin = min + 1;
		ridget.setIncrement(deltaMaxMin);
		ridget.setPageIncrement(deltaMaxMin);
		expectPropertyChangeEvents(new PropertyChangeEvent(ridget, "valueInternal", min, newMin),
				new PropertyChangeEvent(ridget, ITraverseRidget.PROPERTY_VALUE, min, newMin), new PropertyChangeEvent(
						ridget, ITraverseRidget.PROPERTY_INCREMENT, deltaMaxMin, deltaMaxMin - 1),
				new PropertyChangeEvent(ridget, ITraverseRidget.PROPERTY_PAGE_INCREMENT, deltaMaxMin, deltaMaxMin - 1),
				new PropertyChangeEvent(ridget, ITraverseRidget.PROPERTY_MINIMUM, min, min + 1));
		ridget.setMinimum(newMin);
		verifyPropertyChangeEvents();

	}

	public void testSetPageIncrement() {
		final ITraverseRidget ridget = (ITraverseRidget) createRidget();
		final Control control = getWidget();
		ridget.setUIControl(control);

		ridget.setMinimum(1);
		ridget.setMaximum(100);

		/*
		 * Test 1 - valid pageIncrement
		 */
		ridget.setPageIncrement(5);
		assertEquals(5, ridget.getPageIncrement());
		assertEquals(5, getPageIncrement(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 2 - pageIncrement == 0
		 */
		ridget.setPageIncrement(0);
		assertEquals(1, ridget.getPageIncrement());
		assertEquals(1, getPageIncrement(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 3 - pageIncrement == -1
		 */
		ridget.setPageIncrement(-1);
		assertEquals(1, ridget.getPageIncrement());
		assertEquals(1, getPageIncrement(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 4 - pageIncrement < increment
		 */
		ridget.setMaximum(100);
		ridget.setMinimum(1);
		ridget.setIncrement(9);
		ridget.setPageIncrement(5);
		assertEquals(9, ridget.getIncrement());
		assertEquals(9, getIncrement(control));
		assertEquals(5, ridget.getPageIncrement());
		assertEquals(5, getPageIncrement(control));
		assertPropertiesEqual(ridget, control);

	}

	/**
	 * This tests are ridget specific tests. Currently the SliderRidget uses
	 * differing ones.
	 */
	public void testSetPageIncrementRidgetBased() {
		final ITraverseRidget ridget = getRidget();
		final Control control = getWidget();

		/*
		 * Test 1 - pageIncrement > max - min
		 */
		final int deltaMaxMin = 10 - 7;
		ridget.setMinimum(7);
		ridget.setMaximum(10);
		ridget.setPageIncrement(deltaMaxMin + 3);
		assertEquals(deltaMaxMin, ridget.getPageIncrement());
		assertEquals(deltaMaxMin, getPageIncrement(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 2 - pageIncrement = max - min
		 */
		ridget.setPageIncrement(deltaMaxMin);
		assertEquals(deltaMaxMin, ridget.getPageIncrement());
		assertEquals(deltaMaxMin, getPageIncrement(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 3 - pageIncrement < max - min
		 */
		ridget.setPageIncrement(deltaMaxMin - 1);
		assertEquals(deltaMaxMin - 1, ridget.getPageIncrement());
		assertEquals(deltaMaxMin - 1, getPageIncrement(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 4 - pageIncrement > max - min after decreasing max
		 */
		ridget.setMaximum(20);
		ridget.setMinimum(0);
		ridget.setPageIncrement(20);
		assertEquals(20, ridget.getPageIncrement());
		assertEquals(20, getPageIncrement(control));
		assertPropertiesEqual(ridget, control);

		ridget.setMaximum(10);
		assertEquals(10, ridget.getPageIncrement());
		assertEquals(10, getPageIncrement(control));
		assertPropertiesEqual(ridget, control);
	}

	public void testSetPageIncrementFiresEvents() {
		final ITraverseRidget ridget = getRidget();
		ridget.setPageIncrement(1);

		/*
		 * Test 1 default
		 */
		expectPropertyChangeEvent(ITraverseRidget.PROPERTY_PAGE_INCREMENT, 1, 2);
		ridget.setPageIncrement(2);
		verifyPropertyChangeEvents();

		expectNoPropertyChangeEvent();
		ridget.setPageIncrement(2);
		verifyPropertyChangeEvents();

		/*
		 * Test 2 - pageIncrement < 0
		 */
		expectPropertyChangeEvent(ITraverseRidget.PROPERTY_PAGE_INCREMENT, 2, 1);
		ridget.setPageIncrement(-1);
		verifyPropertyChangeEvents();

	}

	/**
	 * This tests are ridget specific tests. Currently the SliderRidget uses
	 * differing ones.
	 */
	public void testSetPageIncrementFiresEventsRidgetBased() {
		final ITraverseRidget ridget = getRidget();
		ridget.setPageIncrement(1);

		/*
		 * Test 1 - pageIncrement > max - min
		 */
		resetEasyMock();
		ridget.setMaximum(20);
		ridget.setMinimum(10);
		assertEquals(20, ridget.getMaximum());
		assertEquals(10, ridget.getMinimum());

		final int deltaMaxMin = 20 - 10;
		expectPropertyChangeEvent(ITraverseRidget.PROPERTY_PAGE_INCREMENT, 1, deltaMaxMin);
		ridget.setPageIncrement(20);
		verifyPropertyChangeEvents();

	}

	public void testSetToolTip() {
		final ITraverseRidget ridget = getRidget();
		final Control control = getWidget();
		ridget.setMinimum(0);
		ridget.setMaximum(100);
		ridget.setValue(5);

		ridget.setToolTipText("tooltip");

		assertEquals("tooltip", ridget.getToolTipText());
		assertEquals("tooltip", control.getToolTipText());

		/*
		 * Test 1 - changing the value using the ridget
		 */
		String pat1 = "my value is: " + ITraverseRidget.VALUE_PATTERN;
		ridget.setToolTipText(pat1);

		assertEquals("my value is: 5", ridget.getToolTipText());
		assertEquals("my value is: 5", control.getToolTipText());

		ridget.setValue(6);

		assertEquals("my value is: 6", ridget.getToolTipText());
		assertEquals("my value is: 6", control.getToolTipText());

		// special case -> If the given tooltip matches the normalized
		// patternTooltip, the patternToolTip is used further.
		ridget.setToolTipText("my value is: 6");

		assertEquals("my value is: 6", ridget.getToolTipText());
		assertEquals("my value is: 6", control.getToolTipText());

		ridget.setValue(7);

		assertEquals("my value is: 7", ridget.getToolTipText());
		assertEquals("my value is: 7", control.getToolTipText());

		// resets the pattern
		ridget.setToolTipText("my value is: 9");

		assertEquals("my value is: 9", ridget.getToolTipText());
		assertEquals("my value is: 9", control.getToolTipText());

		ridget.setValue(1);

		assertEquals("my value is: 9", control.getToolTipText());
		// special case end

		/*
		 * Test 2 - further tests
		 */
		ridget.setToolTipText("");

		assertEquals("", ridget.getToolTipText());
		assertEquals("", control.getToolTipText());

		ridget.setToolTipText("abc");
		ridget.setToolTipText(null);

		assertEquals(null, ridget.getToolTipText());
		assertEquals(null, control.getToolTipText());

		/*
		 * Test 3 - changing the value using the control
		 */
		setValue(control, 5);
		UITestHelper.fireSelectionEvent(control);
		pat1 = "my value is: " + ITraverseRidget.VALUE_PATTERN;
		ridget.setToolTipText(pat1);

		assertEquals("my value is: 5", ridget.getToolTipText());
		assertEquals("my value is: 5", control.getToolTipText());

		setValue(control, 6);
		UITestHelper.fireSelectionEvent(control);

		assertEquals("my value is: 6", ridget.getToolTipText());
		assertEquals("my value is: 6", control.getToolTipText());

		// special case -> If the given tooltip matches the normalized
		// patternTooltip, the patternToolTip is used further.
		ridget.setToolTipText("my value is: 6");

		assertEquals("my value is: 6", ridget.getToolTipText());
		assertEquals("my value is: 6", control.getToolTipText());

		setValue(control, 7);
		UITestHelper.fireSelectionEvent(control);

		assertEquals("my value is: 7", ridget.getToolTipText());
		assertEquals("my value is: 7", control.getToolTipText());

		// resets the pattern
		ridget.setToolTipText("my value is: 9");

		assertEquals("my value is: 9", ridget.getToolTipText());
		assertEquals("my value is: 9", control.getToolTipText());

		setValue(control, 1);
		UITestHelper.fireSelectionEvent(control);

		assertEquals("my value is: 9", control.getToolTipText());
		// special case end
	}

	public void testApplySettingsOnBind() {
		final ITraverseRidget ridget = getRidget();
		ridget.setMaximum(30);
		ridget.setMinimum(10);
		ridget.setValue(15);
		ridget.setIncrement(2);
		ridget.setPageIncrement(4);
		ridget.setToolTipText("aaa");

		final Control control = (Control) createWidget(getShell());
		assertNotSame(control, getWidget());

		ridget.setUIControl(control);

		assertEquals(10, getMinimum(control));
		assertEquals(30, getMaximum(control));
		assertEquals(15, getValue(control));
		assertEquals(2, getIncrement(control));
		assertEquals(4, getPageIncrement(control));
		assertEquals("aaa", control.getToolTipText());
	}

	public void testBind() {
		final ITraverseRidget ridget = getRidget();
		final Control control = getWidget();
		ridget.setMinimum(0);
		ridget.setValue(0);
		ridget.setMaximum(100);

		final TypedBean<Integer> bean = new TypedBean<Integer>(20);
		ridget.bindToModel(bean, TypedBean.PROP_VALUE);

		assertEquals(20, bean.getValue().intValue());
		assertEquals(0, ridget.getValue());
		assertEquals(0, getValue(control));
		assertPropertiesEqual(ridget, control);

		ridget.updateFromModel();

		assertEquals(20, bean.getValue().intValue());
		assertEquals(20, ridget.getValue());
		assertEquals(20, getValue(control));
		assertPropertiesEqual(ridget, control);

		ridget.setValue(30);

		assertEquals(30, bean.getValue().intValue());
		assertEquals(30, ridget.getValue());
		assertEquals(30, getValue(control));
		assertPropertiesEqual(ridget, control);

		setValue(control, 40);
		UITestHelper.fireSelectionEvent(control);

		assertEquals(40, bean.getValue().intValue());
		assertEquals(40, ridget.getValue());
		assertEquals(40, getValue(control));
		assertPropertiesEqual(ridget, control);
	}

	public void testAddListenerInvalid() {
		try {
			getRidget().addListener(null);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}
	}

	public void testAddListener() {
		final ITraverseRidget ridget = getRidget();
		final Widget control = getWidget();

		final FTActionListener listener1 = new FTActionListener();
		final FTActionListener listener2 = new FTActionListener();

		ridget.addListener(listener1);
		ridget.addListener(listener2);
		// listener2 will not be added again
		// if the same instance is already added
		ridget.addListener(listener2);

		UITestHelper.fireSelectionEvent(control);

		assertEquals(1, listener1.getCount());
		assertEquals(1, listener2.getCount());

		ridget.removeListener(listener1);
		UITestHelper.fireSelectionEvent(control);

		assertEquals(1, listener1.getCount());
		assertEquals(2, listener2.getCount());

		ridget.removeListener(listener2);
		UITestHelper.fireSelectionEvent(control);

		assertEquals(1, listener1.getCount());
		assertEquals(2, listener2.getCount());

		ridget.removeListener(listener2);
		UITestHelper.fireSelectionEvent(control);

		assertEquals(1, listener1.getCount());
		assertEquals(2, listener2.getCount());
	}

}
