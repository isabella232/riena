/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.navigation.ui.swt.handlers;

import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.listener.ISubModuleNodeListener;
import org.eclipse.riena.navigation.model.ApplicationNode;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.NavigationModelFailure;
import org.eclipse.riena.navigation.model.NavigationNode;
import org.eclipse.riena.navigation.model.SubApplicationNode;
import org.eclipse.riena.navigation.model.SubModuleNode;

/**
 * Tests of the class {@link NavigationSourceProvider}.
 */
@NonUITestCase
public class NavigationSourceProviderTest extends TestCase {

	private NavigationSourceProvider provider;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		provider = new NavigationSourceProvider();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		provider = null;
		ApplicationNodeManager.clear();
	}

	/**
	 * Tests the method {@code getPovidesSourceName}.
	 */
	public void testGetPovidesSourceName() {

		assertNull(provider.getPovidesSourceName(null));

		assertNotNull(provider.getPovidesSourceName(new SubModuleNode()));
		assertNotNull(provider.getPovidesSourceName(new ModuleNode()));
		assertNotNull(provider.getPovidesSourceName(new ModuleGroupNode()));
		assertNotNull(provider.getPovidesSourceName(new SubApplicationNode()));

		try {
			provider.getPovidesSourceName(new NavigationNode<ISubModuleNode, ISubModuleNode, ISubModuleNodeListener>(
					null) {
				public Class<ISubModuleNode> getValidChildType() {
					return null;
				}
			});
			fail("Expected NavigationModelFailure wasn't throw!");
		} catch (NavigationModelFailure failure) {
			Nop.reason("Expected failure");
		}

	}

	/**
	 * Tests the method <i>private</i> {@code getTypeNodeId}.
	 */
	public void testGetTypeNodeId() {

		SubModuleNode subModuleNode = new SubModuleNode();
		String ret = ReflectionUtils.invokeHidden(provider, "getTypeNodeId", subModuleNode);
		assertEquals("", ret);

		subModuleNode = new SubModuleNode(new NavigationNodeId("helloID"));
		ret = ReflectionUtils.invokeHidden(provider, "getTypeNodeId", subModuleNode);
		assertEquals("helloID", ret);

	}

	/**
	 * Tests the method {@code getCurrentState()}.
	 */
	public void testGetCurrentState() {

		// no navigation model
		Map<String, String> state = provider.getCurrentState();
		assertEquals(4, state.keySet().size());
		assertEquals(4, state.values().size());
		for (String value : state.values()) {
			assertTrue(StringUtils.isEmpty(value));
		}

		// navigation model but no node is active
		IApplicationNode appNode = new ApplicationNode(new NavigationNodeId("app1"));
		ApplicationNodeManager.registerApplicationNode(appNode);
		ISubApplicationNode subApp = new SubApplicationNode(new NavigationNodeId("subApp1"));
		appNode.addChild(subApp);
		IModuleGroupNode mg = new ModuleGroupNode(new NavigationNodeId("mg1"));
		subApp.addChild(mg);
		IModuleNode mod = new ModuleNode(new NavigationNodeId("mod1"));
		mg.addChild(mod);
		ISubModuleNode subMod = new SubModuleNode(new NavigationNodeId("subMod1"));
		mod.addChild(subMod);
		ISubModuleNode subMod2 = new SubModuleNode(new NavigationNodeId("subMod2"));
		mod.addChild(subMod2);

		state = provider.getCurrentState();
		assertEquals(4, state.values().size());
		for (String value : state.values()) {
			assertTrue(StringUtils.isEmpty(value));
		}

		// navigation model with active nodes
		subMod.activate();
		state = provider.getCurrentState();
		assertEquals(4, state.keySet().size());
		assertTrue(state.values().contains("subMod1"));
		assertTrue(state.values().contains("mod1"));
		assertTrue(state.values().contains("mg1"));
		assertTrue(state.values().contains("subApp1"));
		assertFalse(state.values().contains("app1"));
		assertFalse(state.values().contains("subMod2"));

	}

}
