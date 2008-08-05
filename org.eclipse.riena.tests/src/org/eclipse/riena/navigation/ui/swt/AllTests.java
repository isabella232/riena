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
package org.eclipse.riena.navigation.ui.swt;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.riena.navigation.ui.swt.binding.DefaultSwtControlRidgetMapperTest;
import org.eclipse.riena.navigation.ui.swt.component.ModuleItemTest;
import org.eclipse.riena.navigation.ui.swt.lnf.AbstractLnfResourceTest;
import org.eclipse.riena.navigation.ui.swt.lnf.LnfManagerTest;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.ShellBorderRendererTest;
import org.eclipse.riena.navigation.ui.swt.lnf.rienadefault.EmbeddedBorderRendererTest;
import org.eclipse.riena.navigation.ui.swt.lnf.rienadefault.EmbeddedTitlebarRendererTest;
import org.eclipse.riena.navigation.ui.swt.lnf.rienadefault.RienaDefaultLnfTest;

/**
 * Tests all test cases within package:
 * 
 * org.eclipse.riena.navigation.ui.swt
 */
public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());

		suite.addTest(org.eclipse.riena.navigation.ui.swt.presentation.AllTests.suite());
		suite.addTest(org.eclipse.riena.navigation.ui.swt.views.AllTests.suite());

		suite.addTestSuite(DefaultSwtControlRidgetMapperTest.class);
		suite.addTestSuite(AbstractLnfResourceTest.class);
		suite.addTestSuite(LnfManagerTest.class);
		suite.addTestSuite(EmbeddedBorderRendererTest.class);
		suite.addTestSuite(EmbeddedTitlebarRendererTest.class);
		suite.addTestSuite(RienaDefaultLnfTest.class);
		suite.addTestSuite(ModuleItemTest.class);
		suite.addTestSuite(ShellBorderRendererTest.class);
		return suite;
	}

}
