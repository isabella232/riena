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
package org.eclipse.riena.monitor.client;

import junit.framework.TestCase;

import org.eclipse.riena.monitor.client.Range.ParseException;

/**
 * Test the {@code Range} class.
 */
public class RangeTest extends TestCase {

	public void testEmptyRange() throws ParseException {
		Range range = new Range("");
		assertFalse(range.matches(1));
		assertFalse(range.matches(2));
		assertFalse(range.matches(3));
	}

	public void testSingleValue() throws ParseException {
		Range range = new Range("2");
		assertFalse(range.matches(1));
		assertTrue(range.matches(2));
		assertFalse(range.matches(3));
	}

	public void testDoubleValue() throws ParseException {
		Range range = new Range("2 9");
		assertFalse(range.matches(1));
		assertTrue(range.matches(2));
		assertFalse(range.matches(3));
		assertTrue(range.matches(9));
	}

	public void testSingleRangeOpenOpen() throws ParseException {
		Range range = new Range("1 3 ()");
		assertFalse(range.matches(1));
		assertTrue(range.matches(2));
		assertFalse(range.matches(3));
	}

	public void testSingleRangeCloseClose() throws ParseException {
		Range range = new Range("1 3 []");
		assertFalse(range.matches(0));
		assertTrue(range.matches(1));
		assertTrue(range.matches(2));
		assertTrue(range.matches(3));
		assertFalse(range.matches(4));
	}

	public void testSingleRangeOpenClose() throws ParseException {
		Range range = new Range("1 3 (]");
		assertFalse(range.matches(0));
		assertFalse(range.matches(1));
		assertTrue(range.matches(2));
		assertTrue(range.matches(3));
		assertFalse(range.matches(4));
	}

	public void testSingleRangeCloseOpen() throws ParseException {
		Range range = new Range("1 3 [)");
		assertFalse(range.matches(0));
		assertTrue(range.matches(1));
		assertTrue(range.matches(2));
		assertFalse(range.matches(3));
		assertFalse(range.matches(4));
	}

	public void testRangeOpenOpenPlusValue() throws ParseException {
		Range range = new Range("1 3 () 5");
		assertFalse(range.matches(1));
		assertTrue(range.matches(2));
		assertFalse(range.matches(3));
		assertFalse(range.matches(4));
		assertTrue(range.matches(5));
	}

	public void testValuePlusRangeOpenOpenPlusValue() throws ParseException {
		Range range = new Range("-2 1 3 () 5");
		assertTrue(range.matches(-2));
		assertFalse(range.matches(-1));
		assertFalse(range.matches(0));
		assertFalse(range.matches(1));
		assertTrue(range.matches(2));
		assertFalse(range.matches(3));
		assertFalse(range.matches(4));
		assertTrue(range.matches(5));
	}

	public void testSingleRangeOpenOpenPlusRangeCloseClose() throws ParseException {
		Range range = new Range("1 3 () 10 12 []");
		assertFalse(range.matches(1));
		assertTrue(range.matches(2));
		assertFalse(range.matches(3));
		assertTrue(range.matches(10));
		assertTrue(range.matches(11));
		assertTrue(range.matches(12));
	}

	public void testSingleValueError() {
		try {
			new Range("n");
			fail();
		} catch (ParseException t) {
			System.out.println(t);
		}
	}

	public void testIncompleteInterval() {
		try {
			new Range("1 ()");
			fail();
		} catch (ParseException t) {
			System.out.println(t);
		}
	}

}
