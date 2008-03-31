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

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

import junit.framework.TestCase;

/**
 * Test the Iter class
 */
public class IterTest extends TestCase {

	/**
	 * Nomen est omen!
	 */
	public void testIteratorTyped() {
		Scanner scanner = new Scanner("this is a test");
		String result = "";
		for (String s : Iter.able(scanner)) {
			result = result + s;
		}
		assertEquals("thisisatest", result);
	}

	/**
	 * Nomen est omen!
	 */
	public void testIteratorUntyped() {
		UntypedIteratorReturner utr = new UntypedIteratorReturner(new String[] { "1", "2" });
		String result = "";
		for (String s : Iter.able(utr.getIterator(), String.class)) {
			result = result + s;
		}
		assertEquals("12", result);
	}

	/**
	 * Nomen est omen!
	 */
	public void testNullIterator() {
		String result = "";
		for (String s : Iter.able((Iterator<String>) null)) {
			result = result + s;
		}
		assertEquals("", result);
	}

	/**
	 * Nomen est omen!
	 */
	public void testEnumerationTyped() {
		Vector<String> v = new Vector<String>();
		v.add("1");
		v.add("2");
		String result = "";
		for (String o : Iter.able(v.elements())) {
			result = result + o;
		}
		assertEquals("12", result);
	}

	/**
	 * Nomen est omen!
	 */
	public void testEnumerationUntyped() {
		StringTokenizer st = new StringTokenizer("this is a test");
		String result = "";
		for (String str : Iter.able(st, String.class)) {
			result = result + str;
		}
		assertEquals("thisisatest", result);
	}

	/**
	 * Nomen est omen!
	 */
	public void testNullEnumeration() {
		String result = "";
		for (Object o : Iter.able((Enumeration<Object>) null)) {
			result = result + o;
		}
		assertEquals("", result);
	}

	/**
	 * Nomen est omen!
	 */
	public void testAndNowSomethingCompletlyDifferent() throws IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Enumeration<URL> enumeration = classLoader.getResources("META-INF/hivemodule.xml");
		for (URL jarUrl : Iter.able(enumeration)) {
			System.out.println(jarUrl);
		}
	}

	private static class UntypedIteratorReturner {
		private String[] strings;

		/**
		 * @param strings
		 */
		public UntypedIteratorReturner(String[] strings) {
			this.strings = strings;
		}

		/**
		 * Note: It is intended that the iterator is untyped!!!!
		 * 
		 * @return
		 */
		public Iterator getIterator() {
			return new Iterator() {
				private int i = 0;

				public boolean hasNext() {
					return i < strings.length;
				}

				public Object next() {
					return strings[i++];
				}

				public void remove() {
				}

			};
		}
	}
}
