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
package org.eclipse.riena.navigation.model;

import org.osgi.framework.Bundle;

import org.eclipse.core.runtime.IConfigurationElement;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleGroupNodeExtension;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.IModuleNodeExtension;
import org.eclipse.riena.navigation.INavigationAssembler;
import org.eclipse.riena.navigation.INavigationAssemblyExtension;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubApplicationNodeExtension;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ISubModuleNodeExtension;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.NavigationNodeId;

/**
 * Tests of the class {@link SimpleNavigationNodeProvider}.
 */
@NonUITestCase
public class SimpleNavigationNodeProviderTest extends RienaTestCase {

	private IApplicationNode applicationNode;
	private ISubApplicationNode subApplication;
	private IModuleGroupNode moduleGroup;
	private IModuleNode module;
	private ISubModuleNode subModule;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		applicationNode = new ApplicationNode(new NavigationNodeId(
				"org.eclipse.riena.navigation.model.test.application"));

		applicationNode.setNavigationProcessor(new NavigationProcessor());

		subApplication = new SubApplicationNode(new NavigationNodeId(
				"org.eclipse.riena.navigation.model.test.subApplication"));
		applicationNode.addChild(subApplication);
		moduleGroup = new ModuleGroupNode(new NavigationNodeId("org.eclipse.riena.navigation.model.test.moduleGroup"));
		subApplication.addChild(moduleGroup);
		module = new ModuleNode(new NavigationNodeId("org.eclipse.riena.navigation.model.test.module"));
		moduleGroup.addChild(module);
		subModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.navigation.model.test.subModule"));
		module.addChild(subModule);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		applicationNode = null;
	}

	/**
	 * Tests the method {@code provideNode}.
	 */
	public void testProvideNode() {

		subModule.activate();

		assertEquals(1, subApplication.getChildren().size());
		assertTrue(subApplication.isActivated());

		SimpleNavigationNodeProvider provider = new SimpleNavigationNodeProvider();
		INavigationAssembler assembler = new TestSecondModuleGroupNodeAssembler();
		provider.registerNavigationAssembler("myTestAssemberId", assembler);

		NavigationArgument naviArg = new NavigationArgument(null, new NavigationNodeId(
				"org.eclipse.riena.navigation.model.test.subApplication"));
		INavigationNode<?> node = provider.provideNode(subModule, new NavigationNodeId(
				"org.eclipse.riena.navigation.model.test.secondModuleGroup"), naviArg);
		assertNotNull(node);
		assertSame("org.eclipse.riena.navigation.model.test.secondModuleGroup", node.getNodeId().getTypeId());
		assertEquals(2, subApplication.getChildren().size());
		INavigationNode<?> subModuleChild = node.getChild(0).getChild(0);
		assertFalse(subModuleChild.isPrepared());

		subApplication.removeChild(node);
		assertEquals(1, subApplication.getChildren().size());

		naviArg.setPrepareAll(true);
		node = provider.provideNode(subModule, new NavigationNodeId(
				"org.eclipse.riena.navigation.model.test.secondModuleGroup"), naviArg);
		assertNotNull(node);
		assertSame("org.eclipse.riena.navigation.model.test.secondModuleGroup", node.getNodeId().getTypeId());
		assertEquals(2, subApplication.getChildren().size());
		subModuleChild = node.getChild(0).getChild(0);
		assertTrue(subModuleChild.isPrepared());

	}

	/**
	 * Tests the method {@code register(INavigationAssemblyExtension)}.
	 */
	public void testRegister() {

		SimpleNavigationNodeProvider provider = new SimpleNavigationNodeProvider();
		MyAssemblyExtension assembly = new MyAssemblyExtension();
		provider.register(assembly);
		INavigationAssembler assembler = provider.getNavigationAssembler(assembly.getId());
		assertTrue(assembler instanceof GenericNavigationAssembler);
		assertSame(assembly, assembler.getAssembly());

		provider.cleanUp();
		INavigationAssembler naviAssembler = new TestSecondModuleGroupNodeAssembler();
		assembly.setNaviAssembler(naviAssembler);
		provider.register(assembly);
		assembler = provider.getNavigationAssembler(assembly.getId());
		assertSame(naviAssembler, assembler);
		assertSame(assembly, assembler.getAssembly());

	}

	/**
	 * Tests the method {@code findNode(INavigationNode<?>, NavigationNodeId)}.
	 */
	public void testFindNode() {

		MyProvider provider = new MyProvider();
		INavigationNode<?> node = provider.findNode(applicationNode, new NavigationNodeId(
				"org.eclipse.riena.navigation.model.test.module"));
		assertSame(module, node);

		node = provider.findNode(applicationNode, new NavigationNodeId("dummy"));
		assertNull(node);

		node = provider.findNode(applicationNode, null);
		assertNull(node);

	}

	/**
	 * Tests the <i>private</i> method {@code prepareAll(INavigationNode<?>)}.
	 */
	public void testPrepareAll() {

		SimpleNavigationNodeProvider provider = new SimpleNavigationNodeProvider();
		ReflectionUtils.invokeHidden(provider, "prepareAll", subApplication);
		assertFalse(applicationNode.isPrepared());
		assertTrue(subApplication.isPrepared());
		assertTrue(moduleGroup.isPrepared());
		assertTrue(module.isPrepared());
		assertTrue(subModule.isPrepared());

	}

	/**
	 * Tests the <i>private</i> method {@code
	 * getParentTypeId(NavigationArgument, INavigationAssembler)}.
	 */
	public void testGetParentTypeId() {

		SimpleNavigationNodeProvider provider = new SimpleNavigationNodeProvider();
		NavigationArgument naviArg = new NavigationArgument(null, new NavigationNodeId("one"));
		INavigationAssembler naviAssembler = new TestSecondModuleGroupNodeAssembler();
		MyAssemblyExtension assembly = new MyAssemblyExtension();
		assembly.setParentTypeId("parentId");
		naviAssembler.setAssembly(assembly);
		NavigationNodeId parentId = ReflectionUtils.invokeHidden(provider, "getParentTypeId", naviArg, naviAssembler);
		assertEquals("one", parentId.getTypeId());

		naviArg = new NavigationArgument();
		parentId = ReflectionUtils.invokeHidden(provider, "getParentTypeId", naviArg, naviAssembler);
		assertEquals("parentId", parentId.getTypeId());

	}

	/**
	 * This class only makes some protected methods public for testing.
	 */
	private static class MyProvider extends SimpleNavigationNodeProvider {

		/**
		 * {@inheritDoc}
		 * <p>
		 * Visibility changed.
		 */
		@Override
		public INavigationNode<?> findNode(INavigationNode<?> node, NavigationNodeId targetId) {
			return super.findNode(node, targetId);
		}

	}

	/**
	 * Implementation of {@link INavigationAssemblyExtension} only for simple
	 * unit tests.
	 */
	private static class MyAssemblyExtension implements INavigationAssemblyExtension {

		private INavigationAssembler naviAssembler;
		private String parentTypeId;

		public IConfigurationElement getConfigurationElement() {
			return null;
		}

		public Bundle getContributingBundle() {
			return null;
		}

		public INavigationAssembler createNavigationAssembler() {
			return getNaviAssembler();
		}

		public String getNavigationAssembler() {
			return null;
		}

		public String getParentTypeId() {
			return parentTypeId;
		}

		public String getId() {
			return "myId";
		}

		public int getAutostartSequence() {
			return 0;
		}

		public ISubApplicationNodeExtension getSubApplicationNode() {
			return null;
		}

		public IModuleGroupNodeExtension getModuleGroupNode() {
			return null;
		}

		public IModuleNodeExtension getModuleNode() {
			return null;
		}

		public ISubModuleNodeExtension getSubModuleNode() {
			return null;
		}

		public String getRef() {
			return null;
		}

		public void setNaviAssembler(INavigationAssembler naviAssembler) {
			this.naviAssembler = naviAssembler;
		}

		public INavigationAssembler getNaviAssembler() {
			return naviAssembler;
		}

		public void setParentTypeId(String parentTypeId) {
			this.parentTypeId = parentTypeId;
		}

	}

}
