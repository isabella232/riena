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
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.ui.ridgets.ISliderRidget;
import org.eclipse.riena.ui.ridgets.ITraverseRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;

/**
 * Tests for the class {@link SliderRidget}.
 */
public class SliderRidgetTest extends AbstractTraverseRidgetTest {

	@Override
	protected Widget createWidget(final Composite parent) {
		return new Slider(parent, SWT.NONE);
	}

	@Override
	protected ISliderRidget createRidget() {
		return new SliderRidget();
	}

	@Override
	protected Slider getWidget() {
		return (Slider) super.getWidget();
	}

	@Override
	protected ISliderRidget getRidget() {
		return (ISliderRidget) super.getRidget();
	}

	@Override
	protected int getIncrement(final Control control) {
		return ((Slider) control).getIncrement();
	}

	@Override
	protected int getMaximum(final Control control) {
		return ((Slider) control).getMaximum();
	}

	@Override
	protected int getMinimum(final Control control) {
		return ((Slider) control).getMinimum();
	}

	@Override
	protected int getPageIncrement(final Control control) {
		return ((Slider) control).getPageIncrement();
	}

	@Override
	protected int getValue(final Control control) {
		return ((Slider) control).getSelection();
	}

	protected int getThumb(final Control control) {
		return ((Slider) control).getThumb();
	}

	@Override
	protected void setValue(final Control control, final int value) {
		((Slider) control).setSelection(value);
	}

	// test methods
	// ////////////

	@Override
	protected void assertPropertiesEqual(final ITraverseRidget ridget, final Control control) {
		super.assertPropertiesEqual(ridget, control);

		assertEquals(getThumb(control), ((ISliderRidget) ridget).getThumb());
	}

	public void testRidgetMapping() {
		final SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();
		assertSame(SliderRidget.class, mapper.getRidgetClass(getWidget()));
	}

	/**
	 * Slider specific tests. They differ in the calculation of maximum by
	 * internalMaximum = maximum - thumb.
	 */
	@Override
	public void testSetValueRidgetBased() {
		final Control control = getWidget();
		final ISliderRidget ridget = getRidget();

		/*
		 * Test 1 - value > max - thumb: Result value = max - thumb = 29
		 */
		ridget.setMaximum(30);
		ridget.setMinimum(10);
		ridget.setThumb(1);
		ridget.setValue(40);

		assertEquals(29, ridget.getValue());
		assertEquals(29, getValue(control));
		assertEquals(30, ridget.getMaximum());
		assertEquals(30, getMaximum(control));
		assertEquals(10, ridget.getMinimum());
		assertEquals(10, getMinimum(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 2 - value < max - thumb. Result: value will be set to 20.
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
		 * Test 3 - value = max - thumb: Result: value will be set to max -
		 * thumb = 29
		 */
		ridget.setValue(30);
		assertEquals(29, ridget.getValue());
		assertEquals(29, getValue(control));
		assertEquals(30, ridget.getMaximum());
		assertEquals(30, getMaximum(control));
		assertEquals(10, ridget.getMinimum());
		assertEquals(10, getMinimum(control));
		assertPropertiesEqual(ridget, control);
	}

	/**
	 * Slider specific tests. They differ in the calculation of maximum by
	 * internalMaximum = maximum - thumb.
	 */
	@Override
	public void testSetValueFiresEventsRidgetBased() {
		final ISliderRidget ridget = getRidget();
		ridget.setMaximum(30);
		ridget.setValue(0);

		// Test 1 - value > max
		final int maxValue = ridget.getMaximum() - ridget.getThumb();
		expectPropertyChangeEvents(new PropertyChangeEvent(ridget, "valueInternal", 0, maxValue),
				new PropertyChangeEvent(ridget, ITraverseRidget.PROPERTY_VALUE, 0, maxValue));
		ridget.setValue(30);
		verifyPropertyChangeEvents();

		// Test 2 - value < min
		resetEasyMock();
		ridget.setMinimum(10);
		expectPropertyChangeEvents(new PropertyChangeEvent(ridget, "valueInternal", maxValue, 10),
				new PropertyChangeEvent(ridget, ITraverseRidget.PROPERTY_VALUE, maxValue, 10));
		ridget.setValue(1);
		verifyPropertyChangeEvents();

		// Test 3 - value < min and min = max - thumb - 1
		resetEasyMock();
		ridget.setMinimum(19);
		expectNoPropertyChangeEvent();
		ridget.setValue(1);
		verifyPropertyChangeEvents();
	}

	/**
	 * Slider specific tests. They differ in the calculation of maximum by
	 * internalMaximum = maximum - thumb.
	 */
	@Override
	public void testSetMaximumRidgetBased() {

		// do not call super. This ridget additionally uses the thumb to
		// calculate the maximum

		final Control control = getWidget();
		final ISliderRidget ridget = getRidget();
		/*
		 * Test 1 - max = value with thumb = 1
		 */
		int max = 40;
		int thumb = 1;
		int maxValue = max - thumb;
		// prepare
		ridget.setMinimum(0);
		ridget.setThumb(thumb);
		assertPropertiesEqual(ridget, control);
		ridget.setMaximum(100);
		ridget.setValue(max);
		assertPropertiesEqual(ridget, control);

		// test
		ridget.setMaximum(max);
		assertEquals(max, ridget.getMaximum());
		assertEquals(max, getMaximum(control));
		assertEquals(maxValue, ridget.getValue());
		assertEquals(maxValue, getValue(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 2 - max = value with thumb = 10 and max - thumb > min. Result:
		 * decrease value to max - thumb
		 */
		max = 40;
		thumb = 10;
		maxValue = max - thumb;
		// prepare
		ridget.setMinimum(0);
		ridget.setMaximum(100);
		ridget.setThumb(thumb);
		ridget.setValue(max);
		assertPropertiesEqual(ridget, control);

		// test
		ridget.setMaximum(max);
		assertEquals(max, ridget.getMaximum());
		assertEquals(max, getMaximum(control));
		assertEquals(maxValue, ridget.getValue());
		assertEquals(maxValue, getValue(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 3 - max = value with thumb = 10 and max - thumb = min. Result:
		 * ignore set
		 */
		max = 20;
		thumb = 10;
		maxValue = max - thumb;
		// prepare
		ridget.setMaximum(100);
		ridget.setMinimum(10);
		ridget.setThumb(thumb);
		ridget.setValue(30);
		assertPropertiesEqual(ridget, control);

		// test
		try {
			ridget.setMaximum(max);
			fail();
		} catch (final IllegalArgumentException e) {
			ok();
		}
		assertEquals(100, ridget.getMaximum());
		assertEquals(100, getMaximum(control));
		assertEquals(30, ridget.getValue());
		assertEquals(30, getValue(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 4 - max = value with thumb = 10 and max - thumb < min. Result:
		 * ignore set
		 */
		max = 19;
		thumb = 10;
		maxValue = max - thumb;
		// prepare
		ridget.setMaximum(100);
		ridget.setMinimum(10);
		ridget.setThumb(thumb);
		ridget.setValue(30);
		assertPropertiesEqual(ridget, control);

		// test
		try {
			ridget.setMaximum(max);
			fail();
		} catch (final IllegalArgumentException e) {
			ok();
		}
		assertEquals(100, ridget.getMaximum());
		assertEquals(100, getMaximum(control));
		assertEquals(30, ridget.getValue());
		assertEquals(30, getValue(control));
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
		assertEquals(100, ridget.getMaximum());
		assertEquals(100, getMaximum(control));
		assertEquals(30, ridget.getValue());
		assertEquals(30, getValue(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 6 - max < value + thumb by decreasing max. max - thumb > min.
		 * Result: decrease value
		 */
		max = 21;
		thumb = 10;
		maxValue = max - thumb;
		// prepare
		ridget.setMaximum(100);
		ridget.setMinimum(10);
		ridget.setThumb(thumb);
		ridget.setValue(30);
		assertPropertiesEqual(ridget, control);

		// test
		ridget.setMaximum(max);
		assertEquals(max, ridget.getMaximum());
		assertEquals(max, getMaximum(control));
		assertEquals(maxValue, ridget.getValue());
		assertEquals(maxValue, getValue(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 7 - max < value + thumb by decreasing max. max - thumb = min.
		 * Result: ignore set operation
		 */
		max = 20;
		thumb = 10;
		maxValue = max - thumb;
		// prepare
		ridget.setMaximum(100);
		ridget.setMinimum(10);
		ridget.setThumb(thumb);
		ridget.setValue(30);
		assertPropertiesEqual(ridget, control);

		// test
		try {
			ridget.setMaximum(max);
			fail();
		} catch (final IllegalArgumentException e) {
			ok();
		}
		assertEquals(100, ridget.getMaximum());
		assertEquals(100, getMaximum(control));
		assertEquals(30, ridget.getValue());
		assertEquals(30, getValue(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 8 - max < value + thumb by decreasing max. max - thumb < min.
		 * Result: ignore set operation
		 */
		max = 19;
		thumb = 10;
		maxValue = max - thumb;
		// prepare
		ridget.setMaximum(100);
		ridget.setMinimum(10);
		ridget.setThumb(thumb);
		ridget.setValue(30);
		assertPropertiesEqual(ridget, control);

		// test
		try {
			ridget.setMaximum(max);
			fail();
		} catch (final IllegalArgumentException e) {
			ok();
		}
		assertEquals(100, ridget.getMaximum());
		assertEquals(100, getMaximum(control));
		assertEquals(30, ridget.getValue());
		assertEquals(30, getValue(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 9 - increment > max - thumb - min after decreasing max
		 */
		ridget.setMinimum(0);
		int maxIncrement = 20 - 2 - 1;
		ridget.setMaximum(20);
		ridget.setMinimum(1);
		ridget.setThumb(2);
		// prepare
		ridget.setIncrement(20);
		assertEquals(maxIncrement, ridget.getIncrement());
		assertEquals(maxIncrement, getIncrement(control));
		assertPropertiesEqual(ridget, control);
		// test
		maxIncrement = 10 - 2 - 1;
		ridget.setMaximum(10);
		assertEquals(maxIncrement, ridget.getIncrement());
		assertEquals(maxIncrement, getIncrement(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 10 - pageIncrement > max - thumb - min after decreasing max
		 */
		int maxPageIncrement = 20 - 2 - 1;
		ridget.setMaximum(20);
		ridget.setMinimum(1);
		ridget.setThumb(2);
		// prepare
		ridget.setPageIncrement(20);
		assertEquals(maxPageIncrement, ridget.getPageIncrement());
		assertEquals(maxPageIncrement, getPageIncrement(control));
		assertPropertiesEqual(ridget, control);
		// test
		maxPageIncrement = 10 - 2 - 1;
		ridget.setMaximum(10);
		assertEquals(maxPageIncrement, ridget.getPageIncrement());
		assertEquals(maxPageIncrement, getPageIncrement(control));
		assertPropertiesEqual(ridget, control);
	}

	/**
	 * Slider specific tests. They differ in the calculation of maximum by
	 * internalMaximum = maximum - thumb.
	 */
	@Override
	public void testSetMaximumFiresEventsRidgetBased() {
		final ISliderRidget ridget = getRidget();

		/*
		 * Test 1 - increment and page increment > max - thumb - min by
		 * decreasing max
		 */
		resetEasyMock();
		final int max = 20;
		final int min = 10;
		final int thumb = 1;
		ridget.setMaximum(max);
		ridget.setThumb(thumb);
		ridget.setMinimum(min);
		// assert set values
		assertEquals(20, ridget.getMaximum());
		assertEquals(10, ridget.getMinimum());
		assertEquals(max, ridget.getMaximum());
		assertEquals(min, ridget.getMinimum());
		assertEquals(thumb, ridget.getThumb());
		// proceed test
		final int deltaMaxMin = max - thumb - min;
		ridget.setIncrement(deltaMaxMin);
		ridget.setPageIncrement(deltaMaxMin);
		final int oldPageIncrement = ridget.getPageIncrement();
		expectPropertyChangeEvents(new PropertyChangeEvent(ridget, ITraverseRidget.PROPERTY_INCREMENT, deltaMaxMin,
				deltaMaxMin - 1), new PropertyChangeEvent(ridget, ITraverseRidget.PROPERTY_PAGE_INCREMENT,
				oldPageIncrement, deltaMaxMin - 1), new PropertyChangeEvent(ridget, ITraverseRidget.PROPERTY_MAXIMUM,
				max, max - 1));
		ridget.setMaximum(max - 1);
		verifyPropertyChangeEvents();

	}

	/**
	 * Slider specific tests. They differ in the calculation of maximum by
	 * internalMaximum = maximum - thumb.
	 */
	public void testSetMinimumRidgetBased() {
		final ISliderRidget ridget = getRidget();
		final Control control = getWidget();

		/*
		 * Test 1 - increment > max - thumb - min after increasing min
		 */
		int maxIncrement = 20 - 2 - 1;
		ridget.setMaximum(20);
		ridget.setMinimum(1);
		ridget.setThumb(2);
		// prepare
		ridget.setIncrement(20);
		assertEquals(maxIncrement, ridget.getIncrement());
		assertEquals(maxIncrement, getIncrement(control));
		assertPropertiesEqual(ridget, control);
		// test
		maxIncrement = 20 - 2 - 5;
		ridget.setMinimum(5);
		assertEquals(maxIncrement, ridget.getIncrement());
		assertEquals(maxIncrement, getIncrement(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 2 - pageIncrement > max - thumb - min after decreasing max
		 */
		int maxPageIncrement = 20 - 2 - 1;
		ridget.setMaximum(20);
		ridget.setMinimum(1);
		ridget.setThumb(2);
		// prepare
		ridget.setPageIncrement(20);
		assertEquals(maxPageIncrement, ridget.getPageIncrement());
		assertEquals(maxPageIncrement, getPageIncrement(control));
		assertPropertiesEqual(ridget, control);
		// test
		maxPageIncrement = 20 - 2 - 5;
		ridget.setMinimum(5);
		assertEquals(maxPageIncrement, ridget.getPageIncrement());
		assertEquals(maxPageIncrement, getPageIncrement(control));
		assertPropertiesEqual(ridget, control);
	}

	/**
	 * Slider specific tests. They differ in the calculation of maximum by
	 * internalMaximum = maximum - thumb.
	 */
	@Override
	public void testSetMinimumFiresEventsRidgetBased() {
		final ISliderRidget ridget = getRidget();

		/*
		 * Test 1 - increment and page increment > max - thumb - min by
		 * decreasing max
		 */
		resetEasyMock();
		final int max = 20;
		final int min = 10;
		final int thumb = 1;
		ridget.setMaximum(max);
		ridget.setThumb(thumb);
		ridget.setMinimum(min);
		// assert set values
		assertEquals(max, ridget.getMaximum());
		assertEquals(min, ridget.getMinimum());
		assertEquals(thumb, ridget.getThumb());
		// proceed test
		final int deltaMaxMin = max - thumb - min;
		ridget.setIncrement(deltaMaxMin);
		ridget.setPageIncrement(deltaMaxMin);
		final int oldPageIncrement = ridget.getPageIncrement();
		final int oldValue = ridget.getValue();
		expectPropertyChangeEvents(new PropertyChangeEvent(ridget, "valueInternal", oldValue, min + 1),
				new PropertyChangeEvent(ridget, ITraverseRidget.PROPERTY_VALUE, oldValue, min + 1),
				new PropertyChangeEvent(ridget, ITraverseRidget.PROPERTY_INCREMENT, deltaMaxMin, deltaMaxMin - 1),
				new PropertyChangeEvent(ridget, ITraverseRidget.PROPERTY_PAGE_INCREMENT, oldPageIncrement,
						deltaMaxMin - 1), new PropertyChangeEvent(ridget, ITraverseRidget.PROPERTY_MINIMUM, min,
						min + 1));
		ridget.setMinimum(min + 1);
		verifyPropertyChangeEvents();
	}

	/**
	 * Slider specific tests. They differ in the calculation of maximum by
	 * internalMaximum = maximum - thumb.
	 */
	@Override
	public void testSetIncrementRidgetBased() {
		final ISliderRidget ridget = getRidget();
		final Control control = getWidget();

		/*
		 * Test 1 - increment > max - thumb - min. Result: increment = max - min
		 */
		final int deltaMaxMin = 100 - 1 - 7;
		ridget.setMaximum(100);
		ridget.setMinimum(7);
		ridget.setThumb(1);
		ridget.setIncrement(deltaMaxMin + 3);
		assertEquals(deltaMaxMin, ridget.getIncrement());
		assertEquals(deltaMaxMin, getIncrement(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 2 - increment < max - thumb - min.
		 */
		ridget.setIncrement(deltaMaxMin - 1);
		assertEquals(deltaMaxMin - 1, ridget.getIncrement());
		assertEquals(deltaMaxMin - 1, getIncrement(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 3 - increment = max - thumb - min. Result: increment = max - min
		 */
		ridget.setIncrement(deltaMaxMin);
		assertEquals(deltaMaxMin, ridget.getIncrement());
		assertEquals(deltaMaxMin, getIncrement(control));
		assertPropertiesEqual(ridget, control);
	}

	/**
	 * Slider specific tests. They differ in the calculation of maximum by
	 * internalMaximum = maximum - thumb.
	 */
	@Override
	public void testSetIncrementFiresEventsRidgetBased() {
		final ISliderRidget ridget = getRidget();
		ridget.setIncrement(1);

		/*
		 * Test 1 - increment > max - thumb - min
		 */
		resetEasyMock();
		final int max = 20;
		final int min = 10;
		final int thumb = 1;
		ridget.setMaximum(max);
		ridget.setThumb(thumb);
		ridget.setMinimum(min);
		// assert set values
		assertEquals(max, ridget.getMaximum());
		assertEquals(min, ridget.getMinimum());
		assertEquals(thumb, ridget.getThumb());
		// proceed test
		final int deltaMaxMin = max - thumb - min;
		expectPropertyChangeEvent(ITraverseRidget.PROPERTY_INCREMENT, 1, deltaMaxMin);
		ridget.setIncrement(max);
		verifyPropertyChangeEvents();

	}

	/**
	 * Slider specific tests. They differ in the calculation of maximum by
	 * internalMaximum = maximum - thumb.
	 */
	@Override
	public void testSetPageIncrementRidgetBased() {
		final ISliderRidget ridget = getRidget();
		final Control control = getWidget();

		/*
		 * Test 1 - pageIncrement > max - thumb - min. Result: pageIncrement =
		 * max - min
		 */
		final int deltaMaxMin = 100 - 1 - 7;
		ridget.setMaximum(100);
		ridget.setMinimum(7);
		ridget.setThumb(1);
		ridget.setPageIncrement(deltaMaxMin + 3);
		assertEquals(deltaMaxMin, ridget.getPageIncrement());
		assertEquals(deltaMaxMin, getPageIncrement(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 2 - pageIncrement < max - thumb - min.
		 */
		ridget.setPageIncrement(deltaMaxMin - 1);
		assertEquals(deltaMaxMin - 1, ridget.getPageIncrement());
		assertEquals(deltaMaxMin - 1, getPageIncrement(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 3 - pageIncrement = max - thumb - min. Result: pageIncrement =
		 * max - min
		 */
		ridget.setPageIncrement(deltaMaxMin);
		assertEquals(deltaMaxMin, ridget.getPageIncrement());
		assertEquals(deltaMaxMin, getPageIncrement(control));
		assertPropertiesEqual(ridget, control);

	}

	/**
	 * Slider specific tests. They differ in the calculation of maximum by
	 * internalMaximum = maximum - thumb.
	 */
	@Override
	public void testSetPageIncrementFiresEventsRidgetBased() {
		final ISliderRidget ridget = getRidget();
		ridget.setPageIncrement(1);

		/*
		 * Test 1 - increment > max - thumb - min
		 */
		resetEasyMock();
		ridget.setMaximum(20);
		ridget.setThumb(1);
		ridget.setMinimum(10);
		assertEquals(20, ridget.getMaximum());
		assertEquals(10, ridget.getMinimum());

		final int deltaMaxMin = 20 - 1 - 10;
		expectPropertyChangeEvent(ITraverseRidget.PROPERTY_PAGE_INCREMENT, 1, deltaMaxMin);
		ridget.setPageIncrement(20);
		verifyPropertyChangeEvents();
	}

	@Override
	public void testSetMinimum() {
		super.testSetMinimum();

		final ISliderRidget ridget = getRidget();
		final Control control = getWidget();

		/*
		 * Test 1 - min < max - thumb by increasing min. Result: increase value
		 */
		int max = 100;
		int min = 20;
		int thumb = 10;
		// prepare
		ridget.setMaximum(max);
		ridget.setMinimum(10);
		ridget.setThumb(thumb);
		ridget.setValue(20);
		assertPropertiesEqual(ridget, control);

		// test
		ridget.setMinimum(min);
		assertEquals(min, ridget.getMinimum());
		assertEquals(min, getMinimum(control));
		assertEquals(min, ridget.getValue());
		assertEquals(min, getValue(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 2 - min = max - thumb by increasing min. Result: the set
		 * operation will be ignored
		 */
		max = 40;
		min = 30;
		thumb = 10;
		// prepare
		ridget.setMaximum(max);
		ridget.setMinimum(10);
		ridget.setThumb(thumb);
		ridget.setValue(20);
		assertPropertiesEqual(ridget, control);

		// test
		try {
			ridget.setMinimum(min);
			fail();
		} catch (final IllegalArgumentException e) {
			ok();
		}
		assertEquals(10, ridget.getMinimum());
		assertEquals(10, getMinimum(control));
		assertEquals(20, ridget.getValue());
		assertEquals(20, getValue(control));
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 3 - min > max - thumb by increasing min. Result: the set
		 * operation will be ignored
		 */
		max = 40;
		min = 31;
		thumb = 10;
		// prepare
		ridget.setMaximum(max);
		ridget.setMinimum(10);
		ridget.setThumb(thumb);
		ridget.setValue(20);
		assertPropertiesEqual(ridget, control);

		// test
		try {
			ridget.setMinimum(min);
			fail();
		} catch (final IllegalArgumentException e) {
			ok();
		}
		assertEquals(10, ridget.getMinimum());
		assertEquals(10, getMinimum(control));
		assertEquals(20, ridget.getValue());
		assertEquals(20, getValue(control));
		assertPropertiesEqual(ridget, control);
	}

	public void testSetThumb() {
		final ISliderRidget ridget = getRidget();
		final Slider control = getWidget();
		/*
		 * Test 1 - valid thumb
		 */
		ridget.setMaximum(100);
		ridget.setMinimum(10);
		ridget.setThumb(20);
		assertEquals(20, ridget.getThumb());
		assertEquals(20, control.getThumb());
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 2 - thumb < 1
		 */
		try {
			ridget.setThumb(0);
			fail();
		} catch (final IllegalArgumentException e) {
			ok();
		}
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 3 - thumb > max - min
		 */
		try {
			ridget.setThumb(500);
			fail();
		} catch (final IllegalArgumentException e) {
			ok();
		}
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 4 - thumb = max - min
		 */
		try {
			ridget.setThumb(90);
			fail();
		} catch (final IllegalArgumentException e) {
			ok();
		}
		assertPropertiesEqual(ridget, control);

		/*
		 * Test 5 - thumb < max - min.
		 */
		ridget.setThumb(89);
		assertEquals(89, ridget.getThumb());
		assertEquals(89, control.getThumb());
		assertPropertiesEqual(ridget, control);

	}

	public void testSetThumbFiresEvents() {
		final ISliderRidget ridget = getRidget();
		ridget.setMinimum(0);
		ridget.setMaximum(100);
		ridget.setThumb(25);

		expectPropertyChangeEvent(ISliderRidget.PROPERTY_THUMB, 25, 50);
		ridget.setThumb(50);
		verifyPropertyChangeEvents();

		expectNoPropertyChangeEvent();
		ridget.setThumb(50);
		verifyPropertyChangeEvents();
	}

	public void testApplyThumbOnBind() {
		final ISliderRidget ridget = getRidget();
		final Slider control = (Slider) createWidget(getShell());
		ridget.setMinimum(0);
		ridget.setMaximum(100);
		ridget.setThumb(25);

		ridget.setUIControl(control);

		assertEquals(25, control.getThumb());
	}

}
