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
package org.eclipse.riena.ui.ridgets.validation.tests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.riena.tests.collect.NonGatherableTestCase;

/**
 * All tests of the package org.eclipse.riena.ui.ridgets.validation.
 */
@NonGatherableTestCase("This is not a ´TestCase´!")
public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		suite.addTestSuite(MaxLengthTest.class);
		suite.addTestSuite(MaxNumberLengthTest.class);
		suite.addTestSuite(MinLengthTest.class);
		suite.addTestSuite(RequiredFieldTest.class);
		suite.addTestSuite(ValidatorCollectionTest.class);
		suite.addTestSuite(ValidCharactersTest.class);
		suite.addTestSuite(ValidDateTest.class);
		suite.addTestSuite(ValidDecimalTest.class);
		suite.addTestSuite(ValidEmailAddressTest.class);
		suite.addTestSuite(ValidExpressionTest.class);
		suite.addTestSuite(ValidIntegerTest.class);
		suite.addTestSuite(ValidIntermediateDateTest.class);
		suite.addTestSuite(ValidRangeTest.class);
		return suite;
	}

}
