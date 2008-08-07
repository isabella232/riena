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
package org.eclipse.riena.ui.ridgets;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.riena.ui.ridgets.databinding.GregorianCalendarToStringConverterTest;
import org.eclipse.riena.ui.ridgets.databinding.RidgetUpdateValueStrategyTest;
import org.eclipse.riena.ui.ridgets.databinding.StringToGregorianCalendarConverterTest;
import org.eclipse.riena.ui.ridgets.uibinding.InjectBindingManagerTest;
import org.eclipse.riena.ui.ridgets.validation.tests.MaxLengthTest;
import org.eclipse.riena.ui.ridgets.validation.tests.MaxNumberLengthTest;
import org.eclipse.riena.ui.ridgets.validation.tests.MinLengthTest;
import org.eclipse.riena.ui.ridgets.validation.tests.RequiredFieldTest;
import org.eclipse.riena.ui.ridgets.validation.tests.ValidCharactersTest;
import org.eclipse.riena.ui.ridgets.validation.tests.ValidDateTest;
import org.eclipse.riena.ui.ridgets.validation.tests.ValidDecimalTest;
import org.eclipse.riena.ui.ridgets.validation.tests.ValidEmailAddressTest;
import org.eclipse.riena.ui.ridgets.validation.tests.ValidExpressionTest;
import org.eclipse.riena.ui.ridgets.validation.tests.ValidIntegerTest;
import org.eclipse.riena.ui.ridgets.validation.tests.ValidIntermediateDateTest;
import org.eclipse.riena.ui.ridgets.validation.tests.ValidRangeTest;
import org.eclipse.riena.ui.ridgets.validation.tests.ValidatorCollectionTest;

/**
 * 
 */
public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		suite.addTestSuite(ValueBindingSupportTest.class);
		suite.addTestSuite(GregorianCalendarToStringConverterTest.class);
		suite.addTestSuite(StringToGregorianCalendarConverterTest.class);
		suite.addTestSuite(RidgetUpdateValueStrategyTest.class);
		suite.addTestSuite(InjectBindingManagerTest.class);
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
