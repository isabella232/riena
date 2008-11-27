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
 * Tests of the class {@link SimpleWildcard}.
 */
public class SimpleWildcardTest extends TestCase {

	/**
	 * Tests the method {@code SimpleWildcard}
	 */
	public void testMatch() {

		assertFalse(SimpleWildcard.match(null, null));
		assertFalse(SimpleWildcard.match("", null));
		assertTrue(SimpleWildcard.match("", ""));
		// assertTrue(SimpleWildcard.match("hallo", ""));
		assertTrue(SimpleWildcard.match("Hello world!", "*world!"));
		assertTrue(SimpleWildcard.match("Hello world!", "Hello*"));
		assertTrue(SimpleWildcard.match("Hello world!", "*world*"));
		assertTrue(SimpleWildcard.match("Hello world!", "H*ll* w*rld!"));
		assertFalse(SimpleWildcard.match("Hello world!", "H?ll? w?rld!"));
		//assertTrue(SimpleWildcard.match("Hello world!", ""));
		assertTrue(SimpleWildcard.match("", "*"));
		assertFalse(SimpleWildcard.match("", "A"));
		assertFalse(SimpleWildcard.match("Hello world!", "Hello"));

	}

}
