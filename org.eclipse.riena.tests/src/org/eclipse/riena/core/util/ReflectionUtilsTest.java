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

import java.awt.Dimension;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.riena.tests.collect.NonUITestCase;

/**
 * Test the ReflectionUtils class.
 */
@NonUITestCase
public class ReflectionUtilsTest extends TestCase {

	/**
	 * Test creating an instance #1
	 */
	public void testNewInstance1() {
		Object o = ReflectionUtils.newInstance("java.lang.String", "Hello!");
		assertTrue(o instanceof String);
		assertEquals(o, "Hello!");
	}

	/**
	 * Test creating an instance #2
	 */
	public void testNewInstance2() {
		Object o = ReflectionUtils.newInstance("java.lang.String");
		assertTrue(o instanceof String);
		assertEquals(o, "");
	}

	/**
	 * Test creating an instance #3
	 */
	public void testNewInstance3() {
		Object o = ReflectionUtils.newInstance("java.awt.Dimension", Integer.valueOf(2), Integer.valueOf(2));
		assertTrue(o instanceof Dimension);
		Dimension dim = (Dimension) o;
		assertEquals(new Dimension(2, 2), dim);
	}

	/**
	 * Test creating an instance #4
	 */
	public void testNewInstance4() {
		String s = ReflectionUtils.newInstance(String.class, "Hello!");
		assertEquals(s, "Hello!");
	}

	/**
	 * Nomen est omen!
	 * 
	 * @throws MalformedURLException
	 * 
	 * @throws Throwable
	 */
	public void testInvokeHiddenInstance() throws MalformedURLException {

		URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		URL url = new URL("file:c:\\");
		ReflectionUtils.invokeHidden(sysloader, "addURL", url);
	}

	/**
	 * Nomen est omen!
	 * 
	 * @throws MalformedURLException
	 * 
	 * @throws Throwable
	 */
	public void testInvokeHiddenInstanceWithMalformedURLException() {

		Object object = new Thrower();
		try {
			ReflectionUtils.invokeHidden(object, "setUrl", MalformedURLException.class, "filez://murks.at");
			fail("MalformedURLException expected");
		} catch (MalformedURLException expected) {
			// ok, expected
		}
	}

	/**
	 * Tests the method {@code invokeHidden} with primitive and no-primitive
	 * types as arguments,
	 */
	public void testInvokeHidden() {

		ReflectionUtils.invokeHidden(TestClass.class, "setIntegerObject", 1);
		Integer outInteger = ReflectionUtils.invokeHidden(TestClass.class, "getIntegerObject");
		assertEquals(1, outInteger.intValue());

		ReflectionUtils.invokeHidden(TestClass.class, "setIntPrimitive", 23);
		int outInt = ReflectionUtils.invokeHidden(TestClass.class, "getIntPrimitive");
		assertEquals(23, outInt);

	}

	/**
	 * Nomen est omen!
	 * 
	 * @throws Throwable
	 */
	public void testInvokeHiddenClass() {

		ReflectionUtils.invokeHidden(TestClass.class, "setString", new Object[] { "hihi" });
		String out = (String) ReflectionUtils.invokeHidden(TestClass.class, "getString");
		assertEquals(out, "hihi");
	}

	/**
	 * Nomen est omen!
	 * 
	 * @throws Throwable
	 */
	public void testInvokeHiddenClassWithIOException() {

		try {
			ReflectionUtils.invokeHidden(Thrower.class, "throwIOException", IOException.class);
			fail();
		} catch (IOException e) {
			assertEquals("Yippie!", e.getMessage());
		}
	}

	/**
	 * Nomen est omen!
	 * 
	 * @throws Throwable
	 */
	public void testSetGetHiddenInstance() {

		String s = "Hallo";
		ReflectionUtils.setHidden(s, "count", new Integer(2));
		Integer count = (Integer) ReflectionUtils.getHidden(s, "count");
		assertEquals(2, count.intValue());
		assertEquals(s, "Ha");
	}

	/**
	 * Nomen est omen!
	 * 
	 * @throws Throwable
	 */
	public void testSetGetHiddenClass() {

		ReflectionUtils.setHidden(TestClass.class, "string", "hallo");
		String out = (String) ReflectionUtils.getHidden(TestClass.class, "string");
		assertEquals(out, "hallo");
	}

	/**
	 * Nomen est omen!
	 * 
	 * @throws Throwable
	 */
	public void testSetGetHiddenDeepField() {

		TestTestClass ttc = new TestTestClass("Hallo");
		ReflectionUtils.setHidden(ttc, "privateString", "hurz");
		String hurz = (String) ReflectionUtils.getHidden(ttc, "privateString");
		assertEquals("hurz", hurz);
	}

	/**
	 * Nomen est omen!
	 * 
	 * @throws Throwable
	 */
	public void testSetGetHiddenDeepClassField() throws Throwable {

		ReflectionUtils.setHidden(TestTestClass.class, "string", "hallo");
		String out = (String) ReflectionUtils.getHidden(TestTestClass.class, "string");
		assertEquals(out, "hallo");
	}

	/**
	 * Nomen est Omen!
	 */
	@SuppressWarnings("unchecked")
	public void testNewLazyInstanceByClass() {
		HaeshMaep.created = false;
		Map<String, String> map = ReflectionUtils.newLazyInstance(Map.class, HaeshMaep.class);
		assertFalse(HaeshMaep.created);
		map.put("hi", "there");
		assertTrue(HaeshMaep.created);
	}

	/**
	 * Nomen est Omen!
	 */
	@SuppressWarnings("unchecked")
	public void testNewLazyInstanceByString() {
		HaeshMaep.created = false;
		Map<String, String> map = ReflectionUtils.newLazyInstance(Map.class, HaeshMaep.class.getName());
		assertFalse(HaeshMaep.created);
		map.put("hi", "there");
		assertTrue(HaeshMaep.created);
	}

	private static class TestClass {

		@SuppressWarnings("unused")
		private String privateString;
		private static String string;
		private static Integer integerObject;
		private static int intPrimitive;

		private TestClass(String str) {
			privateString = str;
		}

		@SuppressWarnings("unused")
		private static void setString(String string) {
			TestClass.string = string;
		}

		@SuppressWarnings("unused")
		private static String getString() {
			return string;
		}

		@SuppressWarnings("unused")
		private static void setIntegerObject(Integer integerObject) {
			TestClass.integerObject = integerObject;
		}

		@SuppressWarnings("unused")
		private static Integer getIntegerObject() {
			return integerObject;
		}

		@SuppressWarnings("unused")
		private static void setIntPrimitive(int intPrimitive) {
			TestClass.intPrimitive = intPrimitive;
		}

		@SuppressWarnings("unused")
		private static int getIntPrimitive() {
			return intPrimitive;
		}

	}

	private static final class TestTestClass extends TestClass {
		private static String stringString;

		private TestTestClass(String str) {
			super(str);
		}

		@SuppressWarnings("unused")
		private static void setString(String string) {
			TestTestClass.stringString = string;
		}

		@SuppressWarnings("unused")
		private static String getString() {
			return stringString;
		}
	}

	private final static class Thrower {

		private Thrower() {
		}

		@SuppressWarnings("unused")
		private void setUrl(String url) throws MalformedURLException {
			new URL(url);
		}

		@SuppressWarnings("unused")
		private static String throwIOException() throws IOException {
			throw new IOException("Yippie!");
		}
	}

	public static class HaeshMaep<K, V> extends HashMap<K, V> {

		private static final long serialVersionUID = 1L;
		protected static boolean created;

		/**
		 * 
		 */
		public HaeshMaep() {
			super();
			created = true;
		}

	}
}