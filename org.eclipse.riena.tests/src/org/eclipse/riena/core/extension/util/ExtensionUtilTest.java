/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.extension.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.riena.tests.RienaTestCase;

public class ExtensionUtilTest extends RienaTestCase {

	public void testExceptionUtil1() {
		System.out.println("test");
		ITest[] tests = ExtensionUtility.readExtensions("core.test.extpoint", ITest.class);
		assertNotNull(tests);
		assertTrue("tests.length should be 3 but is " + tests.length, tests.length == 3);
		for (ITest test : tests) {
			assertNotNull(test.getString());
			if (test.getString().equals("test1")) {
				assertTrue(test.getBoolean());
				assertTrue(test.isBoolean());
				assertTrue(test.createExecutable() instanceof String);
			} else {
				if (test.getString().equals("test2")) {
					assertFalse(test.getBoolean());
					assertFalse(test.isBoolean());
					assertTrue(test.createExecutable() instanceof HashMap);
				} else {
					if (test.getString().equals("test3")) {
						assertTrue(test.getBoolean());
						assertTrue(test.isBoolean());
						assertTrue(test.createExecutable() instanceof ArrayList);
					}
				}
			}
		}
	}
}
