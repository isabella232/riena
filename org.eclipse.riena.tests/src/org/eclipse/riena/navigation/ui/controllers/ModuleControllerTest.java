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
package org.eclipse.riena.navigation.ui.controllers;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.internal.ui.ridgets.swt.ShellRidget;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.NavigationProcessor;
import org.eclipse.riena.navigation.model.SubModuleNode;

/**
 * Tests of the class <code>ModuleController</code>.
 */
@UITestCase
public class ModuleControllerTest extends TestCase {

	public void testAfterBind() throws Exception {

		ModuleNode node = new ModuleNode();
		node.setClosable(true);
		node.setLabel("Hello");
		ModuleController controller = new ModuleController(node);
		ShellRidget shellRidget = new ShellRidget();
		Shell shell = new Shell();
		shellRidget.setUIControl(shell);
		controller.setWindowRidget(shellRidget);
		controller.afterBind();
		assertTrue(controller.isCloseable());
		assertEquals("Hello", shellRidget.getTitle());

		node.setClosable(false);
		controller.configureRidgets();
		assertFalse(controller.isCloseable());

		shell.dispose();

	}

	public void testSetLabel() throws Exception {

		// test setLabel() with one submodule
		ModuleNode node = new ModuleNode();
		SubModuleNode subNode1 = new SubModuleNode();
		node.addChild(subNode1);

		ModuleController controller = new ModuleController(node);
		SubModuleController subModuleController1 = new SubModuleController(subNode1);

		ShellRidget shellRidget = new ShellRidget();
		Shell shell = new Shell();
		shellRidget.setUIControl(shell);
		controller.setWindowRidget(shellRidget);

		ShellRidget subModuleShellRidget1 = new ShellRidget();
		Shell subModuleShell1 = new Shell();
		subModuleShellRidget1.setUIControl(subModuleShell1);
		subModuleController1.setWindowRidget(subModuleShellRidget1);

		node.setLabel("Hello");

		assertEquals("Hello", shellRidget.getTitle());
		assertEquals("Hello", subModuleShellRidget1.getTitle());

		shell.dispose();
		subModuleShell1.dispose();

		// test setLabel() with two submodules
		ModuleNode modNode = new ModuleNode();
		SubModuleNode subModNode1 = new SubModuleNode();
		SubModuleNode subModNode2 = new SubModuleNode();
		modNode.addChild(subModNode1);
		modNode.addChild(subModNode2);

		ModuleController modController = new ModuleController(modNode);
		SubModuleController subModController1 = new SubModuleController(subModNode1);
		SubModuleController subModController2 = new SubModuleController(subModNode2);

		ShellRidget shellRidgetMod = new ShellRidget();
		Shell shellMod = new Shell();
		shellRidgetMod.setUIControl(shellMod);
		modController.setWindowRidget(shellRidgetMod);

		ShellRidget subModShellRidget1 = new ShellRidget();
		Shell subModShell1 = new Shell();
		subModShellRidget1.setUIControl(subModShell1);
		subModController1.setWindowRidget(subModShellRidget1);

		ShellRidget subModShellRidget2 = new ShellRidget();
		Shell subModShell2 = new Shell();
		subModShellRidget2.setUIControl(subModShell2);
		subModController2.setWindowRidget(subModShellRidget2);

		modNode.setLabel("Module");
		subModNode1.setLabel("SubModule 1");
		subModNode2.setLabel("SubModule 2");

		assertEquals("Module", shellRidgetMod.getTitle());
		assertEquals("Module - SubModule 1", subModShellRidget1.getTitle());
		assertEquals("Module - SubModule 2", subModShellRidget2.getTitle());

		shellMod.dispose();
		subModShell1.dispose();
		subModShell2.dispose();

	}

	/**
	 * Tests the method {@code getVisibleChildren}.
	 */
	public void testGetVisibleChildren() {

		ModuleNode node = new ModuleNode();
		node.setNavigationProcessor(new NavigationProcessor());
		SubModuleNode sub1 = new SubModuleNode();
		node.addChild(sub1);
		SubModuleNode sub2 = new SubModuleNode();
		node.addChild(sub2);
		SubModuleNode sub3 = new SubModuleNode();
		node.addChild(sub3);

		ModuleController controller = new ModuleController(node);
		List<INavigationNode<?>> nodes = controller.getVisibleChildren(node);
		assertEquals(3, nodes.size());
		assertSame(sub1, nodes.get(0));
		assertSame(sub2, nodes.get(1));
		assertSame(sub3, nodes.get(2));

		sub1.setEnabled(false);
		sub2.setVisible(false);
		nodes = controller.getVisibleChildren(node);
		assertEquals(2, nodes.size());
		assertSame(sub1, nodes.get(0));
		assertSame(sub3, nodes.get(1));

	}

	/**
	 * Tests the method {@code hasSingleLeafChild}.
	 */
	public void testHasSingleLeafChild() {

		ModuleNode node = new ModuleNode();
		node.setNavigationProcessor(new NavigationProcessor());
		SubModuleNode sub1 = new SubModuleNode();
		node.addChild(sub1);
		SubModuleNode sub2 = new SubModuleNode();
		node.addChild(sub2);
		SubModuleNode sub3 = new SubModuleNode();
		sub2.addChild(sub3);

		ModuleController controller = new ModuleController(node);
		assertFalse(controller.hasSingleLeafChild());

		sub1.setVisible(false);
		assertFalse(controller.hasSingleLeafChild());

		sub1.setVisible(true);
		sub2.setVisible(false);
		assertTrue(controller.hasSingleLeafChild());

		sub2.setVisible(true);
		assertFalse(controller.hasSingleLeafChild());

		node.removeChild(sub2);
		assertTrue(controller.hasSingleLeafChild());

	}

}
