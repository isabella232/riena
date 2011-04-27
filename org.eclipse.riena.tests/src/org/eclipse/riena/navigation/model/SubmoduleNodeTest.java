/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.model;

import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;

/**
 * Tests the @see {@link SubModuleNode#setSelectable(boolean)} Feature
 */
@NonUITestCase
public class SubmoduleNodeTest extends RienaTestCase {

	private final static String ID_SUBMODULE_FIRST = "org.eclipse.riena.navigation.model.test.submodule.first";
	private final static String ID_SUBMODULE_SECOND = "org.eclipse.riena.navigation.model.test.submodule.second";
	private final static String ID_SUBMODULE_THIRD = "org.eclipse.riena.navigation.model.test.submodule.third";

	private final static String ID_SUBMODULE_CHILD = "org.eclipse.riena.navigation.model.test.subChild";
	private final static String ID_SUBMODULE_SUBCHILD = "org.eclipse.riena.navigation.model.test.subSubChild";
	private final static String ID_MODULEGROUP = "org.eclipse.riena.navigation.model.test.moduleGroup";
	private final static String ID_MODULE = "org.eclipse.riena.navigation.model.test.module";
	private final static String ID_APP = "org.eclipse.riena.navigation.model.test.application";
	private final static String ID_SUB_APP = "org.eclipse.riena.navigation.model.test.subApplication";

	private ApplicationNode applicationNode;
	private NavigationProcessor navigationProcessor;
	private SubApplicationNode subApplication;
	private SubModuleNode submoduleFirst;
	private SubModuleNode submoduleChild;
	private SubModuleNode submoduleSubChild;
	private ModuleGroupNode moduleGroup;
	private ModuleNode module;
	private SubModuleNode submoduleSecond;
	private SubModuleNode submoduleThird;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		applicationNode = new ApplicationNode(new NavigationNodeId(ID_APP));
		navigationProcessor = new NavigationProcessor();
		applicationNode.setNavigationProcessor(navigationProcessor);

		subApplication = new SubApplicationNode(new NavigationNodeId(ID_SUB_APP));
		applicationNode.addChild(subApplication);

		moduleGroup = new ModuleGroupNode(new NavigationNodeId(ID_MODULEGROUP));
		subApplication.addChild(moduleGroup);
		module = new ModuleNode(new NavigationNodeId(ID_MODULE));
		moduleGroup.addChild(module);

		// #############################
		submoduleFirst = new SubModuleNode(new NavigationNodeId(ID_SUBMODULE_FIRST));

		submoduleChild = new SubModuleNode(new NavigationNodeId(ID_SUBMODULE_CHILD));
		submoduleSubChild = new SubModuleNode(new NavigationNodeId(ID_SUBMODULE_SUBCHILD));
		submoduleChild.addChild(submoduleSubChild);
		submoduleFirst.addChild(submoduleChild);
		module.addChild(submoduleFirst);

		// #############################
		submoduleSecond = new SubModuleNode(new NavigationNodeId(ID_SUBMODULE_SECOND));
		submoduleThird = new SubModuleNode(new NavigationNodeId(ID_SUBMODULE_THIRD));
		module.addChild(submoduleSecond);
		module.addChild(submoduleThird);
	}

	/**
	 * Navigate over a not selectable node and make sure, that the first child
	 * is active
	 * 
	 * @throws Exception
	 */
	public void testNavigateOverNotSelectableSubmodule() throws Exception {
		submoduleChild.setSelectable(false);
		navigationProcessor.activate(submoduleFirst);
		assertFalse(submoduleSubChild.isActivated());
		submoduleFirst.navigate(new NavigationNodeId(ID_SUBMODULE_CHILD));
		assertTrue(submoduleSubChild.isActivated());
	}

	/**
	 * Activate a node with flag selectable = false and it's children with
	 * selectable = false too. Test should ensure that no exception is thrown.
	 * 
	 * @throws Exception
	 */
	public void testNavigateOverAllNotSelectableSubmodules() throws Exception {
		submoduleChild.setSelectable(false);
		submoduleSubChild.setSelectable(false);
		navigationProcessor.activate(submoduleFirst);
	}

	/**
	 * Default value for selectable is true, so the old code does not break!
	 * 
	 * @throws Exception
	 */
	public void testDefaultSelectableValue() throws Exception {
		assertTrue(submoduleFirst.isSelectable());
		assertTrue(submoduleChild.isSelectable());
		assertTrue(submoduleSubChild.isSelectable());
	}

	public void testAddNonSelectableNode() {
		final NavigationNodeId id = new NavigationNodeId("4711");
		final ISubModuleNode nonSelectableNode = new SubModuleNode(id);
		nonSelectableNode.setNavigationProcessor(navigationProcessor);
		nonSelectableNode.setSelectable(false);
		submoduleFirst.addChild(nonSelectableNode);
		assertFalse(nonSelectableNode.isEnabled());

		final NavigationNodeId id1 = new NavigationNodeId("4712");
		final ISubModuleNode nonSelectableNode2 = new SubModuleNode(id1);
		nonSelectableNode2.setNavigationProcessor(navigationProcessor);
		nonSelectableNode2.setSelectable(false);
		nonSelectableNode.addChild(nonSelectableNode2);
		assertFalse(nonSelectableNode.isEnabled());
		assertFalse(nonSelectableNode2.isEnabled());

		final NavigationNodeId id2 = new NavigationNodeId("0815");
		final ISubModuleNode node = new SubModuleNode(id2);
		node.setNavigationProcessor(navigationProcessor);
		nonSelectableNode2.addChild(node);
		assertTrue(nonSelectableNode.isEnabled());
		assertTrue(nonSelectableNode2.isEnabled());

		//test sibling non-selectable
		final NavigationNodeId id3 = new NavigationNodeId("4713");
		final ISubModuleNode nonSelectableNode3 = new SubModuleNode(id3);
		nonSelectableNode3.setNavigationProcessor(navigationProcessor);
		nonSelectableNode3.setSelectable(false);
		submoduleSecond.addChild(nonSelectableNode3);
		assertFalse(nonSelectableNode3.isEnabled());

		final NavigationNodeId id4 = new NavigationNodeId("4714");
		final ISubModuleNode nonSelectableNode4 = new SubModuleNode(id4);
		nonSelectableNode4.setNavigationProcessor(navigationProcessor);
		nonSelectableNode4.setSelectable(false);
		nonSelectableNode3.addChild(nonSelectableNode4);
		assertFalse(nonSelectableNode4.isEnabled());

		final NavigationNodeId id5 = new NavigationNodeId("0816");
		final ISubModuleNode node2 = new SubModuleNode(id5);
		node2.setNavigationProcessor(navigationProcessor);
		nonSelectableNode3.addChild(node2);
		assertTrue(nonSelectableNode3.isEnabled());
		assertFalse(nonSelectableNode4.isEnabled());
	}
}
