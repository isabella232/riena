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
package org.eclipse.riena.navigation.model;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 */
public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.eclipse.riena.navigation.model");
		//$JUnit-BEGIN$
		suite.addTestSuite(NavigationProcessorTest.class);
		suite.addTestSuite(NavigationNodeTest.class);
		suite.addTestSuite(ModuleGroupNodeTest.class);
		suite.addTestSuite(ModuleNodeTest.class);
		//$JUnit-END$
		return suite;
	}

}
