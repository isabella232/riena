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
		applicationModel.setPresentationId(new NavigationNodeId("org.eclipse.riena.navigation.model.test.application"));
		navigationProcessor = new NavigationProcessor();
		applicationModel.setNavigationProcessor(navigationProcessor);

		subApplication = new SubApplicationNode();
		subApplication
				.setPresentationId(new NavigationNodeId("org.eclipse.riena.navigation.model.test.subApplication"));
		applicationModel.addChild(subApplication);
		moduleGroup = new ModuleGroupNode();
		moduleGroup.setPresentationId(new NavigationNodeId("org.eclipse.riena.navigation.model.test.moduleGroup"));
		subApplication.addChild(moduleGroup);
		module = new ModuleNode();
		module.setPresentationId(new NavigationNodeId("org.eclipse.riena.navigation.model.test.module"));
		moduleGroup.addChild(module);
		subModule = new SubModuleNode();
		subModule.setPresentationId(new NavigationNodeId("org.eclipse.riena.navigation.model.test.subModule"));
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

	public void testNavigate() throws Exception {

		subModule.activate();

		assertEquals(1, applicationModel.getChildren().size());
		assertTrue(subApplication.isActivated());

		subModule.navigate(new NavigationNodeId("org.eclipse.riena.navigation.model.test.secondModuleGroup"));

		assertEquals(2, applicationModel.getChildren().size());
		assertFalse(subApplication.isActivated());
		ISubApplicationNode secondSubApplication = applicationModel.getChild(1);
		assertEquals(new NavigationNodeId("org.eclipse.riena.navigation.model.test.secondSubApplication"),
				secondSubApplication.getNodeId());
		assertTrue(secondSubApplication.isActivated());
		assertEquals(1, secondSubApplication.getChildren().size());
		IModuleGroupNode secondModuleGroup = secondSubApplication.getChild(0);
		assertEquals(new NavigationNodeId("org.eclipse.riena.navigation.model.test.secondModuleGroup"),
				secondModuleGroup.getNodeId());
		assertTrue(secondModuleGroup.isActivated());
		IModuleNode secondModule = secondModuleGroup.getChild(0);
		ISubModuleNode secondSubModule = secondModule.getChild(0);
		assertTrue(secondSubModule.isActivated());

		secondSubModule.navigateBack();

		assertFalse(secondSubApplication.isActivated());
		assertFalse(secondSubModule.isActivated());
		assertTrue(subApplication.isActivated());
		assertTrue(subModule.isActivated());

		subModule.navigate(new NavigationNodeId("org.eclipse.riena.navigation.model.test.secondModuleGroup"));

		assertFalse(subApplication.isActivated());
		assertFalse(subModule.isActivated());
		assertEquals(2, applicationModel.getChildren().size());
		assertSame(secondSubApplication, applicationModel.getChild(1));
		assertTrue(secondSubApplication.isActivated());
		assertEquals(1, secondSubApplication.getChildren().size());
		assertSame(secondModuleGroup, secondSubApplication.getChild(0));
		assertTrue(secondModuleGroup.isActivated());
		assertTrue(secondSubModule.isActivated());
	}

}
