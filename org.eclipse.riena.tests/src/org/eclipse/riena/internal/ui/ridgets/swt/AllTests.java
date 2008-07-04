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
package org.eclipse.riena.internal.ui.ridgets.swt;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests all test cases within package:
 * <code>org.eclipse.riena.ui.ridgets.swt</code>
 */
public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		suite.addTestSuite(ActionRidgetTest.class);
		suite.addTestSuite(ComboRidgetTest.class);
		suite.addTestSuite(LabelRidgetTest.class);
		suite.addTestSuite(ListRidgetTest.class);
		suite.addTestSuite(MarkableRidgetTest.class);
		suite.addTestSuite(TableRidgetTest.class);
		suite.addTestSuite(TextRidgetTest.class);
		suite.addTestSuite(TextRidgetTest2.class);
		suite.addTestSuite(TreeRidgetTest.class);
		suite.addTestSuite(ToggleButtonRidgetTest.class);
		return suite;
	}

}
