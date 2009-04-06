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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.NavigationProcessor;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.tests.RienaTestCase;
import org.eclipse.riena.tests.collect.UITestCase;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests for the ModuleView.
 */
@UITestCase
public class ModuleViewTest extends RienaTestCase {

	private ModuleView view;
	private ModuleNode node;
	private SubModuleNode subNode;
	private SubModuleNode subSubNode;
	private SubModuleNode subSubSubNode;
	private Shell shell;

	@Override
	protected void setUp() throws Exception {

		shell = new Shell();
		view = new ModuleView(shell);
		final NavigationProcessor navigationProcessor = new NavigationProcessor();
		node = new ModuleNode();
		node.setNavigationProcessor(navigationProcessor);
		subNode = new SubModuleNode();
		subNode.setNavigationProcessor(navigationProcessor);
		node.addChild(subNode);
		subSubNode = new SubModuleNode();
		subSubNode.setNavigationProcessor(navigationProcessor);
		subNode.addChild(subSubNode);
		subSubSubNode = new SubModuleNode();
		subSubSubNode.setNavigationProcessor(navigationProcessor);
		subSubNode.addChild(subSubSubNode);
		view.bind(node);
	}

	@Override
	protected void tearDown() throws Exception {
		view.dispose();
		SwtUtilities.disposeWidget(shell);
		node = null;
	}

	/**
	 * Test for bug 269221
	 */
	public void testSetActivatedSubModuleExpanded() throws Exception {

		subNode.activate();

		assertTrue(node.isActivated());
		assertTrue(subNode.isActivated());
		assertFalse(subSubNode.isActivated());
		assertFalse(subSubSubNode.isActivated());
		assertFalse(subNode.isExpanded());
		assertFalse(subSubNode.isExpanded());

		subSubSubNode.activate();

		assertTrue(node.isActivated());
		assertFalse(subNode.isActivated());
		assertFalse(subSubNode.isActivated());
		assertTrue(subSubSubNode.isActivated());
		assertTrue(subNode.isExpanded());
		assertTrue(subSubNode.isExpanded());
	}

}
