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
package org.eclipse.riena.internal.ui.ridgets.swt;

import junit.framework.TestCase;

import org.eclipse.riena.ui.ridgets.IDateTextFieldRidget;

/**
 * Tests for the {@link SegmentedString} class.
 */
// TODO [ev] add to test suite
public class SegmentedStringTest extends TestCase {

	public void testCreateSegmentedString() {
		SegmentedString ss;

		ss = new SegmentedString(IDateTextFieldRidget.FORMAT_DDMMYYYY);
		assertEquals("dd|dd|dddd", ss.getPattern());
		assertEquals("  .  .    ", ss.toString());

		ss = new SegmentedString("MM/dd/yyyy");
		assertEquals("dd|dd|dddd", ss.getPattern());
		assertEquals("  /  /    ", ss.toString());

		ss = new SegmentedString(IDateTextFieldRidget.FORMAT_HHMM);
		assertEquals("dd|dd", ss.getPattern());
		assertEquals("  :  ", ss.toString());

		ss = new SegmentedString(IDateTextFieldRidget.FORMAT_DDMMYYYYHHMM);
		assertEquals("dd|dd|dddd|dd|dd", ss.getPattern());
		assertEquals("  .  .       :  ", ss.toString());

		try {
			new SegmentedString("ddabcMM");
			fail();
		} catch (RuntimeException rex) {
			// expected
		}
	}

	public void testInsert() {
		SegmentedString ss;
		final String pat = IDateTextFieldRidget.FORMAT_DDMMYYYY;

		ss = new SegmentedString(pat, "  .  .    ");
		ss.insert(1, "01102008");
		assertEquals("01.10.2008", ss.toString());

		ss = new SegmentedString(pat, "  .  .    ");
		ss.insert(1, "01.10.2008");
		assertEquals("01.10.2008", ss.toString());

		ss = new SegmentedString(pat, "  .10.2008");
		ss.insert(1, "1208");
		assertEquals("12.10.2008", ss.toString());

		ss = new SegmentedString(pat, "  .  .2008");
		ss.insert(1, "1208");
		assertEquals("12.08.2008", ss.toString());

		ss = new SegmentedString(pat, "  .  .2008");
		ss.insert(4, "1208");
		assertEquals("  .12.2008", ss.toString());

		ss = new SegmentedString(pat, "01.  .2008");
		ss.insert(6, "10");
		assertEquals("01.  .2008", ss.toString());

		ss = new SegmentedString(pat, "  .  .    ");
		ss.insert(6, "2008");
		assertEquals("  .  .2008", ss.toString());

		ss = new SegmentedString(pat, "  .  .    ");
		ss.insert(3, "102008");
		assertEquals("  .10.2008", ss.toString());
	}

	public void testDelete() {
		SegmentedString ss;
		final String pat = IDateTextFieldRidget.FORMAT_DDMMYYYY;

		ss = new SegmentedString(pat, "01.10.2008");
		ss.delete(5, 5);
		assertEquals("01.10.2008", ss.toString());

		ss = new SegmentedString(pat, "01.10.2008");
		ss.delete(5, 6);
		assertEquals("01.10. 008", ss.toString());

		ss = new SegmentedString(pat, "01.10.2008");
		ss.delete(4, 5);
		assertEquals("01. 1.2008", ss.toString());

		ss = new SegmentedString(pat, "01.10.2008");
		ss.delete(4, 6);
		assertEquals("01. 1. 008", ss.toString());

		ss = new SegmentedString(pat, "01.10.2008");
		ss.delete(0, 9);
		assertEquals("  .  .    ", ss.toString());

		ss = new SegmentedString(pat, "01.10.2008");
		ss.delete(0, 6);
		assertEquals("  .  . 008", ss.toString());
	}

	public void testReplace() {
		SegmentedString ss;
		final String pat = IDateTextFieldRidget.FORMAT_DDMMYYYY;

		ss = new SegmentedString(pat, "01.10.2008");
		ss.replace(5, 5, "1");
		assertEquals("01.10.2008", ss.toString());

		ss = new SegmentedString(pat, "01.10.2008");
		ss.replace(5, 6, "1");
		assertEquals("01.10.1008", ss.toString());

		ss = new SegmentedString(pat, "01.10.2008");
		ss.replace(4, 5, "1");
		assertEquals("01.11.2008", ss.toString());

		ss = new SegmentedString(pat, "01.10.2008");
		ss.replace(4, 6, "1");
		assertEquals("01.11. 008", ss.toString());

		ss = new SegmentedString(pat, "01.10.2008");
		ss.replace(0, 6, "3");
		assertEquals(" 3.  . 008", ss.toString());

		ss = new SegmentedString(pat, "01.10.2008");
		ss.replace(0, 9, "3");
		assertEquals(" 3.  .    ", ss.toString());
	}
}
