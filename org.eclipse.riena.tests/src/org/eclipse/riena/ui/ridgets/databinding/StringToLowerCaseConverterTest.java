/*******************************************************************************
 * Copyright (c) 2007, 2012 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.ridgets.databinding;

import junit.framework.TestCase;

import org.eclipse.core.databinding.conversion.IConverter;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Tests for the class {@link StringToLowerCaseConverter}.
 */
@NonUITestCase
public class StringToLowerCaseConverterTest extends TestCase {

	private IConverter converter;

	@Override
	protected void setUp() throws Exception {
		converter = new StringToLowerCaseConverter();
	}

	public void testStringToLowerCase() {
		assertEquals("abcd", converter.convert("ABCD"));
		assertEquals("hello_world!", converter.convert("Hello_World!"));
		assertEquals("����", converter.convert("����"));
		assertEquals("1337", converter.convert("1337"));
		assertEquals("\t\r\n", converter.convert("\t\r\n"));
	}

	public void testNullToUpperCase() {
		assertEquals(null, converter.convert(null));
	}

	public void testWrongType() {
		try {
			converter.convert(Integer.valueOf(1337));
			fail();
		} catch (final RuntimeException rex) {
			// expected
		}
	}
}
