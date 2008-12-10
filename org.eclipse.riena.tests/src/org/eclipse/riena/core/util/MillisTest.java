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
package org.eclipse.riena.core.util;

import junit.framework.TestCase;

/**
 * Test the {@code Milli} class.
 */
public class MillisTest extends TestCase {

	public void testSeconds() {
		assertEquals(1000, Millis.seconds(1));
	}

	public void testMinutes() {
		assertEquals(60 * 1000, Millis.minutes(1));
	}

	public void testHours() {
		assertEquals(60 * 60 * 1000, Millis.hours(1));
	}

	public void testDays() {
		assertEquals(24 * 60 * 60 * 1000, Millis.days(1));
	}
}
