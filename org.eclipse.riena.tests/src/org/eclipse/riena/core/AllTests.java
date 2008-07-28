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
package org.eclipse.riena.core;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.riena.core.config.ConfigTest;
import org.eclipse.riena.core.exception.ExceptionHandlerManagerTest;
import org.eclipse.riena.core.extension.ExtensionInjectorTest;
import org.eclipse.riena.core.extension.HeterogeneousInjectingTest;
import org.eclipse.riena.core.extension.util.ExtensionUtilTest;
import org.eclipse.riena.core.service.ServiceInjectorTest;

/**
 * Tests all test cases within package:
 * 
 * org.eclipse.riena.core
 */
public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		suite.addTestSuite(ConfigTest.class);
		suite.addTestSuite(ExceptionHandlerManagerTest.class);
		suite.addTestSuite(ExtensionUtilTest.class);
		suite.addTestSuite(ServiceInjectorTest.class);
		suite.addTestSuite(ExtensionInjectorTest.class);
		suite.addTestSuite(HeterogeneousInjectingTest.class);
		return suite;
	}

}
