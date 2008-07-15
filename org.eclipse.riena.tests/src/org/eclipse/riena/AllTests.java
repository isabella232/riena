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
package org.eclipse.riena;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests all test cases within this bundle
 */
public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		suite.addTest(org.eclipse.riena.communication.core.ssl.AllTests.suite());
		suite.addTest(org.eclipse.riena.core.AllTests.suite());
		suite.addTest(org.eclipse.riena.exceptionmanager.AllTests.suite());
		suite.addTest(org.eclipse.riena.security.AllTests.suite());
		suite.addTest(org.eclipse.riena.objecttransaction.AllTests.suite());
		suite.addTest(org.eclipse.riena.navigation.ui.swt.AllTests.suite());
		suite.addTest(org.eclipse.riena.core.util.AllTests.suite());
		suite.addTest(org.eclipse.riena.internal.ui.ridgets.swt.AllTests.suite());
		suite.addTest(org.eclipse.riena.ui.ridgets.AllTests.suite());
		suite.addTest(org.eclipse.riena.ui.ridgets.marker.AllTests.suite());
		suite.addTest(org.eclipse.riena.ui.swt.AllTests.suite());
		return suite;
	}
}
