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
package org.eclipse.riena.ui.core.marker;

import junit.framework.TestCase;

import org.eclipse.riena.tests.collect.NonUITestCase;

/**
 * Tests of the class {@link UIProcessFinishedMarker}.
 */
@NonUITestCase
public class UIProcessFinishedMarkerTest extends TestCase {

	/**
	 * Tests the constructor {@code UIProcessFinishedMarker() }.
	 */
	public void testUIProcessFinishedMarker() {

		UIProcessFinishedMarker marker = new UIProcessFinishedMarker();
		assertEquals(10, marker.getCounterMaximum());

	}

	/**
	 * Tests the constructor {@code UIProcessFinishedMarker(int) }.
	 */
	public void testUIProcessFinishedMarkerInt() {

		UIProcessFinishedMarker marker = new UIProcessFinishedMarker(2);
		assertEquals(2, marker.getCounterMaximum());
		assertFalse(marker.isActivated());
		assertTrue(marker.isOn());

	}

	/**
	 * Tests the method {@code isOn()}.
	 */
	public void testIsOn() {

		UIProcessFinishedMarker marker = new UIProcessFinishedMarker(2);

		marker.activate();
		assertTrue(marker.isOn());

		marker.setOn(false);
		assertFalse(marker.isOn());

		marker.increase();
		marker.increase();
		assertTrue(marker.isOn());

	}

	/**
	 * Tests the method {@code isFlashing()}.
	 */
	public void testIsFlashing() {

		UIProcessFinishedMarker marker = new UIProcessFinishedMarker(2);
		assertFalse(marker.isFlashing());

		marker.activate();
		assertTrue(marker.isFlashing());

		marker.increase();
		assertTrue(marker.isFlashing());

		marker.increase();
		assertFalse(marker.isFlashing());

	}

	/**
	 * Tests the method {@code isActivated()}.
	 */
	public void testIsActivated() {

		UIProcessFinishedMarker marker = new UIProcessFinishedMarker();
		assertFalse(marker.isActivated());

		marker.activate();
		assertTrue(marker.isActivated());

	}

}
