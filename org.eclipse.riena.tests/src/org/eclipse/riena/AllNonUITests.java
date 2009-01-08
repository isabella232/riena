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
package org.eclipse.riena;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests all test cases within this bundle NOT related to UI
 */
public class AllNonUITests extends TestCase {
	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		suite.addTest(org.eclipse.riena.communication.core.AllTests.suite());
		suite.addTest(org.eclipse.riena.communication.core.attachment.AllTests.suite());
		suite.addTest(org.eclipse.riena.communication.core.ssl.AllTests.suite());
		suite.addTest(org.eclipse.riena.core.AllTests.suite());
		suite.addTest(org.eclipse.riena.core.cache.AllTests.suite());
		suite.addTest(org.eclipse.riena.core.exception.AllTests.suite());
		suite.addTest(org.eclipse.riena.core.exceptionmanager.AllTests.suite());
		suite.addTest(org.eclipse.riena.core.extension.AllTests.suite());
		suite.addTest(org.eclipse.riena.core.logging.AllTests.suite());
		suite.addTest(org.eclipse.riena.core.marker.AllTests.suite());
		suite.addTest(org.eclipse.riena.core.service.AllTests.suite());
		suite.addTest(org.eclipse.riena.core.util.AllTests.suite());
		suite.addTest(org.eclipse.riena.monitor.client.AllTests.suite());
		suite.addTest(org.eclipse.riena.navigation.AllTests.suite());
		suite.addTest(org.eclipse.riena.objecttransaction.AllTests.suite());
		suite.addTest(org.eclipse.riena.security.AllTests.suite());
		return suite;
	}

}
