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
package org.eclipse.riena.playground;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;

import org.eclipse.riena.internal.tests.Activator;
import org.eclipse.riena.tests.collect.NonUITestCase;
import org.eclipse.riena.tests.collect.TestCollector;

/**
 *
 */
public final class TestCollectorTest extends TestCase {
	private static final String ORG_ECLIPSE_RIENA = "org.eclipse.riena";

	public void testThis() {
		List<Class<? extends TestCase>> testClasses = TestCollector.collectWith((Activator.getDefault().getBundle()),
				null, NonUITestCase.class);
		System.out.println("NonUITestCases:");
		for (Class<?> clazz : testClasses) {
			System.out.println(clazz);
		}
		System.out.println("NonMarkedTestCases:");
		testClasses = TestCollector.collectUnmarked(Activator.getDefault().getBundle(), null);
		for (Class<?> clazz : testClasses) {
			System.out.println(clazz);
		}
	}

	@SuppressWarnings("unchecked")
	public static Test suite() {
		return TestCollector.createTestSuiteWith((Activator.getDefault().getBundle()), TestCollectorTest.class
				.getPackage(), NonUITestCase.class);
	}
}
