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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.NavigationProcessor;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.ui.swt.EmbeddedTitleBar;
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
		super.setUp();

		shell = new Shell();
		final NavigationProcessor navigationProcessor = new NavigationProcessor();
		node = new ModuleNode();
		view = new ModuleView(shell);
		ModuleGroupNode moduleGroupNode = new ModuleGroupNode();
		moduleGroupNode.addChild(node);
		view.setModuleGroupNode(moduleGroupNode);
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

		super.tearDown();
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

	public void testBlocking() {
		EmbeddedTitleBar title = ReflectionUtils.invokeHidden(view, "getTitle");
		Composite body = ReflectionUtils.invokeHidden(view, "getBody");
		Tree tree = ReflectionUtils.invokeHidden(view, "getTree");
		Cursor waitCursor = title.getDisplay().getSystemCursor(SWT.CURSOR_WAIT);

		node.setBlocked(true);

		assertSame(waitCursor, title.getCursor());
		assertSame(waitCursor, body.getCursor());
		assertFalse(title.isCloseable());
		assertFalse(tree.getEnabled());

		node.setBlocked(false);

		assertNotSame(waitCursor, title.getCursor());
		assertTrue(title.isCloseable());
		assertNotSame(waitCursor, body.getCursor());
		assertTrue(tree.getEnabled());
	}

}
