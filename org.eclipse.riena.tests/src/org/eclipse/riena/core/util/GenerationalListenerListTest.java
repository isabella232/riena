/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.util;

import java.util.Iterator;

import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Test the {@code GenerationalListenerList}
 */
@NonUITestCase
public class GenerationalListenerListTest extends RienaTestCase {

	private final GenerationalListenerList<String> list = new GenerationalListenerList<String>();
	private final static String[] NOTHING = new String[0];

	public void testIsEmpty() {
		assertTrue(list.isEmpty());
	}

	public void testIsEmptyAfterAddAndRemove() {
		list.add("one");
		assertEquals(1, list.size());
		list.remove("one");
		assertTrue(list.isEmpty());
		assertFalse(list.contains("one"));
		expect(list.iterator(), NOTHING);
	}

	public void testAddIterateRemoveIterateAdd() {
		list.add("one");
		final Iterator<String> iteratorAfterAdd = list.iterator();
		list.remove("one");
		list.printDebugList("t0");
		list.printList("t0");
		final Iterator<String> iteratorAfterRemove = list.iterator();
		list.add("one");
		list.printDebugList("t1");
		list.printList("t1");
		expect(iteratorAfterAdd, "one");
		expect(iteratorAfterRemove, NOTHING);
		list.printDebugList("t2");
		list.printList("t2");
	}

	public void testAddAddAddIterateRemoveWhileIteratingIterate() {
		list.add("one");
		list.add("two");
		list.add("three");
		final Iterator<String> iteratorAfterAdd = list.iterator();
		boolean once = true;
		for (final String s : list) {
			if (once) {
				once = false;
				list.remove("two");
			}
		}
		expect(iteratorAfterAdd, "one", "two", "three");
		final Iterator<String> iteratorAfterRemove = list.iterator();
		expect(iteratorAfterRemove, "one", "three");
	}

	public void testGCedList() {
		final GenerationalListenerList<String> gcList = new GenerationalListenerList<String>(1, 0.0f, 1);

		gcList.add("one");
		final Iterator<String> iteratorAfterAdd = gcList.iterator();
		gcList.remove("one");
		gcList.printDebugList("t0");
		gcList.printList("t0");
		final Iterator<String> iteratorAfterRemove = gcList.iterator();
		gcList.add("one");
		gcList.printDebugList("t1");
		gcList.printList("t1");
		expect(iteratorAfterAdd, "one");
		expect(iteratorAfterRemove, NOTHING);
		gcList.printDebugList("t2");
		gcList.printList("t2");
	}

	private void expect(final Iterator<String> iterator, final String... expectations) {
		int i = 0;
		for (final String s : Iter.able(iterator)) {
			assertEquals(expectations[i], s);
			i++;
		}
		assertEquals(expectations.length, i);
	}
}
