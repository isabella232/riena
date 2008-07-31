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
package org.eclipse.riena.ui.swt;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests all test cases within package:
 * 
 * org.eclipse.riena.ui.swt
 */
public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		suite.addTestSuite(StatuslineTest.class);
		suite.addTestSuite(StatuslineTimeTest.class);
		suite.addTestSuite(StatuslineMessageTest.class);
		suite.addTestSuite(StatuslineNumberTest.class);
		return suite;
	}

}
