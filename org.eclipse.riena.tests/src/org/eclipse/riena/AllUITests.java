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
package org.eclipse.riena;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests all test cases within this bundle related to UI
 */
public class AllUITests extends TestCase {
	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		suite.addTest(org.eclipse.riena.navigation.ui.AllTests.suite());
		suite.addTest(org.eclipse.riena.navigation.ui.swt.AllTests.suite());
		suite.addTest(org.eclipse.riena.internal.ui.ridgets.swt.AllTests.suite());
		suite.addTest(org.eclipse.riena.ui.ridgets.AllTests.suite());
		suite.addTest(org.eclipse.riena.ui.ridgets.marker.AllTests.suite());
		suite.addTest(org.eclipse.riena.ui.swt.AllTests.suite());
		return suite;
	}

}
