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

import org.eclipse.riena.navigation.IApplicationModel;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.tests.RienaTestCase;

/**
 * Tests for the NavigationProcessor.
 */
public class NavigationProcessorTest extends RienaTestCase {

	private NavigationProcessor navigationProcessor;
	private IApplicationModel applicationModel;
	private ISubApplicationNode subApplication;
	private IModuleGroupNode moduleGroup;
	private IModuleNode module;
	private ISubModuleNode subModule;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		addPluginXml(NavigationProcessorTest.class, "NavigationProcessorTest.xml");

		applicationModel = new ApplicationModel();
		navigationProcessor = new NavigationProcessor();
		applicationModel.setNavigationProcessor(navigationProcessor);

		subApplication = new SubApplicationNode();
		applicationModel.addChild(subApplication);
		moduleGroup = new ModuleGroupNode();
		subApplication.addChild(moduleGroup);
		module = new ModuleNode();
		moduleGroup.addChild(module);
		subModule = new SubModuleNode();
		module.addChild(subModule);
	}

	public void testActivateChildren() throws Exception {

		assertFalse(subApplication.isActivated());
		assertFalse(moduleGroup.isActivated());
		assertFalse(module.isActivated());
		assertFalse(subModule.isActivated());

		navigationProcessor.activate(subApplication);

		assertTrue(subApplication.isActivated());
		assertTrue(moduleGroup.isActivated());
		assertTrue(module.isActivated());
		assertTrue(subModule.isActivated());
	}

}
