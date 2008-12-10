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
package org.eclipse.riena.core.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests cases within package: <code>org.eclipse.riena.core.util</code>
 */
public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		// TODO fails!
		// suite.addTestSuite(ContainerModelTest.class);
		suite.addTestSuite(ArraysUtilTest.class);
		suite.addTestSuite(IterTest.class);
		suite.addTestSuite(ListenerListTest.class);
		suite.addTestSuite(LiteralTest.class);
		suite.addTestSuite(MillisTest.class);
		suite.addTestSuite(PropertiesUtilsTest.class);
		suite.addTestSuite(ReflectionUtilsTest.class);
		suite.addTestSuite(StringUtilsTest.class);
		suite.addTestSuite(VariableManagerUtilTest.class);
		suite.addTestSuite(StringMatcherTest.class);
		return suite;
	}

}
