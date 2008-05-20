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
package org.eclipse.riena.internal.core.config;

import java.lang.reflect.Field;
import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.core.internal.variables.StringVariableManager;
import org.eclipse.riena.tests.RienaTestCase;
import org.osgi.service.cm.ConfigurationException;

/**
 * Test the <code>ConfigSymbolReplace</code> class.
 */
public class ConfigSymbolReplaceTest extends RienaTestCase {

	private Dictionary<String, String> dictionary;
	private ConfigSymbolReplace translator;
	private Dictionary<String, String> book;
	private static Field fgManager;
	private static final String REF_KEY = "ref";
	private static final String WWW_ECLIPSE_ORG = "www.eclipse.org";

	static {
		try {
			fgManager = StringVariableManager.class.getDeclaredField("fgManager");
			fgManager.setAccessible(true);
		} catch (Throwable e) {
			fail("Could not access field ´fgManager´ from StringVariableManager!");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.tests.RienaTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		dictionary = new Hashtable<String, String>();
		book = new Hashtable<String, String>();
		translator = new ConfigSymbolReplace();
		// Need every time a fresh manager
		fgManager.set(null, null);
	}

	public void testUpdatedWrongKey() throws ConfigurationException {
		dictionary.put("host", "${host");
		translator.updated(dictionary);
		book.put(REF_KEY, "${host}");
		translator.modifyConfiguration(null, book);
		assertEquals("${host", book.get(REF_KEY));
	}

	public void testUpdatedRecursiveKeyDirectly() throws ConfigurationException {
		dictionary.put("host", "${host}");
		try {
			translator.updated(dictionary);
			book.put(REF_KEY, "${host}");
			translator.modifyConfiguration(null, book);
			fail();
		} catch (RuntimeException e) {
			assertTrue(e.getMessage().contains("${host}"));
		}
	}

	public void testUpdatedRecursiveKeyIndirectly1() throws ConfigurationException {
		dictionary.put("a", "${b}");
		dictionary.put("b", "${a}");
		try {
			translator.updated(dictionary);
			book.put(REF_KEY, "${a}");
			translator.modifyConfiguration(null, book);
			fail();
		} catch (RuntimeException e) {
			assertTrue(e.getMessage().contains("${a}"));
		}
	}

	public void testUpdatedRecursiveKeyIndirectly2() throws ConfigurationException {
		dictionary.put("a", "${b}");
		dictionary.put("b", "${c}");
		dictionary.put("c", "${a}");
		try {
			translator.updated(dictionary);
			book.put(REF_KEY, "${a}");
			translator.modifyConfiguration(null, book);
			fail();
		} catch (RuntimeException e) {
			assertTrue(e.getMessage().contains("${a}"));
		}
	}

	public void testModifyWrongKey() throws ConfigurationException {
		dictionary.put("host", WWW_ECLIPSE_ORG);
		translator.updated(dictionary);

		book.put(REF_KEY, "${host");
		translator.modifyConfiguration(null, book);
		assertEquals("${host", book.get(REF_KEY));
	}

	public void testModifyEmptyKey() throws ConfigurationException {
		dictionary.put("", WWW_ECLIPSE_ORG);
		translator.updated(dictionary);

		book.put(REF_KEY, "${}");
		translator.modifyConfiguration(null, book);

		assertEquals(WWW_ECLIPSE_ORG, book.get(REF_KEY));
	}

	public void testModifySingle() throws ConfigurationException {
		dictionary.put("host", WWW_ECLIPSE_ORG);
		translator.updated(dictionary);

		book.put(REF_KEY, "${host}");
		translator.modifyConfiguration(null, book);

		assertEquals(WWW_ECLIPSE_ORG, book.get(REF_KEY));
	}

	public void testModifyDoubleSequentialy() throws ConfigurationException {
		dictionary.put("host", WWW_ECLIPSE_ORG);
		translator.updated(dictionary);

		book.put(REF_KEY, "${host}${host}");
		translator.modifyConfiguration(null, book);

		assertEquals(WWW_ECLIPSE_ORG + WWW_ECLIPSE_ORG, book.get(REF_KEY));
	}

	public void testModifyDoubleNested() throws ConfigurationException {
		dictionary.put("url", "http://${host}/path");
		dictionary.put("host", WWW_ECLIPSE_ORG);
		translator.updated(dictionary);

		book.put(REF_KEY, "${url}");
		translator.modifyConfiguration(null, book);

		assertEquals("http://" + WWW_ECLIPSE_ORG + "/path", book.get(REF_KEY));
	}
}
