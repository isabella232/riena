/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.riena.internal.core.test.collect.NonGatherableTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.internal.core.test.collect.TestCollector;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.internal.tests.Activator;

/**
 * Tests all 'fast' test cases within this bundle.
 * <p>
 * All test cases taking more than 10 seconds are skipped. It removes about 20%
 * of the tests that take 80% of the time. Currently this suite runs in 75
 * seconds. You should not rely solely on the results of this suite, however it
 * should be useful if you want to run many tests quite frequently.
 */
@NonGatherableTestCase("This is not a ´TestCase´!")
public class AllFastTests extends TestCase {

	private static Set<String> LONG_TESTS;

	static {
		LONG_TESTS = new HashSet<String>();
		LONG_TESTS.add("DateTextRidgetTest");
		LONG_TESTS.add("DecimalTextRidgetTest");
		LONG_TESTS.add("NumericTextRidgetTest");
		LONG_TESTS.add("TextRidgetTest2");
		LONG_TESTS.add("DateTimeRidgetTest");
		// LONG_TESTS.add("TreeRidgetTest2");
		// LONG_TESTS.add("TreeTableRidgetTest");
		LONG_TESTS.add("CheckTestConstraintsTest");
	};

	@SuppressWarnings("unchecked")
	public static Test suite() {
		TestSuite collected = TestCollector.createTestSuiteWith(Activator.getDefault().getBundle(), null,
				UITestCase.class, NonUITestCase.class);
		Enumeration tests = collected.tests();
		TestSuite result = new TestSuite();
		while (tests.hasMoreElements()) {
			Test test = (Test) tests.nextElement();
			if (!LONG_TESTS.contains(getTestName(test))) {
				result.addTest(test);
			} else {
				System.err.println("...skipping long test: " + test.toString());
			}
		}
		return result;
	}

	// helping methods
	//////////////////

	private static String getTestName(Test test) {
		String result = test.toString();
		int lastDot = result.lastIndexOf('.');
		if (lastDot != -1) {
			result = result.substring(lastDot + 1);
		}
		return result;
	}
}
