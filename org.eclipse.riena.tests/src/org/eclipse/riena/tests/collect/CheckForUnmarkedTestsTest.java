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
package org.eclipse.riena.tests.collect;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.riena.internal.tests.Activator;

/**
 * Check for all {@code TestCase}s that are not marked with any {@code
 * TestCollector} specific annotation.
 */
@NonUITestCase
public class CheckForUnmarkedTestsTest extends TestCase {

	public void testUnmarkedTests() {
		List<Class<? extends TestCase>> unmarked = TestCollector.collectUnmarked(Activator.getDefault().getBundle(),
				null);
		assertEquals("Unmarked test(s) found: " + unmarked, 0, unmarked.size());
	}
}
