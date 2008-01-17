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

import org.eclipse.riena.tests.RienaTestCase;

public class ExtensionUtilTest extends RienaTestCase {

	public void testExceptionUtil1() {
		System.out.println("test");
		ITest[] tests = ExtensionUtility.readExtensions("core.test.extpoint", ITest.class);
		assertNotNull(tests);
		assertTrue(tests[0].getString().equals("test1"));
		assertTrue(tests[0].getBoolean());
		assertTrue(tests[0].createExecutable() instanceof String);
	}
}
