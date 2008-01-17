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
package org.eclipse.riena.security;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.riena.security.authorizationservice.FilePermissionStoreTest;
import org.eclipse.riena.security.common.Callback2CredentialConverterTest;

/**
 * Tests all test cases within package:
 * 
 * org.eclipse.riena.exceptionmanager.test.internal
 */
public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		suite.addTestSuite(FilePermissionStoreTest.class);
		suite.addTestSuite(Callback2CredentialConverterTest.class);
		return suite;
	}

}
