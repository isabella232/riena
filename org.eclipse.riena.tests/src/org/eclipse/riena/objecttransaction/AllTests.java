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
package org.eclipse.riena.objecttransaction;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.riena.objecttransaction.interf.ObjectTransactionInterfTest;
import org.eclipse.riena.objecttransaction.list.ObjectTransactionListTest;
import org.eclipse.riena.objecttransaction.noreg.ObjectTransactionVariousNoRegTest;
import org.eclipse.riena.objecttransaction.noreg.ObjectTransactionWithoutTransactionTest;
import org.eclipse.riena.objecttransaction.simple.ObjectTransactionDisallowRegisterTest;
import org.eclipse.riena.objecttransaction.simple.ObjectTransactionSimpleTest;
import org.eclipse.riena.objecttransaction.simple.ObjectTransactionVariousSimpleTest;
import org.eclipse.riena.objecttransaction.simple.StateMachineTest;
import org.eclipse.riena.tests.collect.NonGatherableTestCase;

/**
 * Tests all test cases within package:
 * 
 * org.eclipse.riena.core.exceptionmanager.test.internal
 */
@NonGatherableTestCase("This is not a ´TestCase´!")
public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		suite.addTestSuite(ObjectTransactionInterfTest.class);
		suite.addTestSuite(ObjectTransactionListTest.class);
		suite.addTestSuite(ObjectTransactionVariousNoRegTest.class);
		suite.addTestSuite(ObjectTransactionWithoutTransactionTest.class);
		suite.addTestSuite(ObjectTransactionDisallowRegisterTest.class);
		suite.addTestSuite(ObjectTransactionSimpleTest.class);
		suite.addTestSuite(ObjectTransactionVariousSimpleTest.class);
		suite.addTestSuite(StateMachineTest.class);
		return suite;
	}

}
