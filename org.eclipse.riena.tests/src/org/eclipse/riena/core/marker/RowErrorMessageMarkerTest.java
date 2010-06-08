/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.marker;

import junit.framework.TestCase;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.ui.core.marker.RowErrorMessageMarker;

/**
 * Tests for {@link RowErrorMessageMarker}.
 */
@NonUITestCase
public class RowErrorMessageMarkerTest extends TestCase {

	public void testCons() {
		try {
			new RowErrorMessageMarker("", null);
			fail();
		} catch (RuntimeException rex) {
			// expected
		}
	}

	public void testGetMessage() {
		RowErrorMessageMarker marker1 = new RowErrorMessageMarker("msg", new Object());

		assertEquals("msg", marker1.getMessage());

		RowErrorMessageMarker marker2 = new RowErrorMessageMarker(null, new Object());

		assertEquals("", marker2.getMessage());

		RowErrorMessageMarker marker3 = new RowErrorMessageMarker("", new Object());

		assertEquals("", marker3.getMessage());
	}

	public void testGetRowValue() {
		Object value1 = new Object();
		RowErrorMessageMarker marker1 = new RowErrorMessageMarker("msg", value1);

		assertSame(value1, marker1.getRowValue());
	}

	public void testEquals() {
		Object value1 = new Object();
		RowErrorMessageMarker marker1 = new RowErrorMessageMarker("aaa", value1);
		RowErrorMessageMarker marker2 = new RowErrorMessageMarker("bbb", value1);
		RowErrorMessageMarker marker3 = new RowErrorMessageMarker(null, value1);

		assertEquals(marker1, marker2);
		assertEquals(marker2, marker3);
		assertEquals(marker1, marker2);

		Object value2 = new Object();
		RowErrorMessageMarker marker4 = new RowErrorMessageMarker("aaa", value2);
		RowErrorMessageMarker marker5 = new RowErrorMessageMarker(null, value2);

		assertFalse(marker1.equals(marker4));
		assertFalse(marker4.equals(marker1));
		assertFalse(marker3.equals(marker5));
		assertFalse(marker5.equals(marker3));
	}

	public void testHashCode() {
		Object value1 = new Object();
		RowErrorMessageMarker marker1 = new RowErrorMessageMarker("aaa", value1);
		RowErrorMessageMarker marker2 = new RowErrorMessageMarker("bbb", value1);
		RowErrorMessageMarker marker3 = new RowErrorMessageMarker(null, value1);

		int hc1 = marker1.hashCode();
		assertEquals(hc1, marker2.hashCode());
		assertEquals(hc1, marker3.hashCode());

		Object value2 = new Object();
		RowErrorMessageMarker marker4 = new RowErrorMessageMarker("aaa", value2);
		RowErrorMessageMarker marker5 = new RowErrorMessageMarker(null, value2);

		assertEquals(marker4.hashCode(), marker5.hashCode());

		assertFalse(hc1 == marker4.hashCode());
		assertFalse(hc1 == marker5.hashCode());
	}

}
