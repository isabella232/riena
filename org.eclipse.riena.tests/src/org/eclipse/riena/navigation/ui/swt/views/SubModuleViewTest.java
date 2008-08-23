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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.ApplicationModel;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubApplicationNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.ModuleController;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.tests.RienaTestCase;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Tests for the SubModuleNodeView.
 */
public class SubModuleViewTest extends RienaTestCase {

	private SubModuleView<SubModuleController> subModuleNodeView;
	private SubModuleNode node;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		addPluginXml(SubModuleViewTest.class, "SubModuleViewTest.xml");

		ApplicationModel appModel = new ApplicationModel();
		SubApplicationNode subAppNode = new SubApplicationNode();
		appModel.addChild(subAppNode);
		ModuleGroupNode mgNode = new ModuleGroupNode(null);
		subAppNode.addChild(mgNode);
		IModuleNode parent = new ModuleNode(null, "TestModuleLabel");
		mgNode.addChild(parent);

		subModuleNodeView = new TestView();
		node = new SubModuleNode(new NavigationNodeId("testId"), "TestSubModuleLabel");
		parent.setPresentation(new ModuleController(parent));
		parent.addChild(node);
		subModuleNodeView.createPartControl(new Shell());
		node.activate();
	}

	public void testCreateController() throws Exception {

		assertTrue(subModuleNodeView.getController() instanceof SubModuleController);
		assertEquals(node, subModuleNodeView.getController().getNavigationNode());
	}

	public void testBlocking() {
		node.setBlocked(true);
		Composite parentComposite = ReflectionUtils.invokeHidden(subModuleNodeView, "getParentComposite");
		Composite contentComposite = ReflectionUtils.invokeHidden(subModuleNodeView, "getContentComposite");
		assertFalse(contentComposite.isEnabled());
		assertSame(waitCursor, parentComposite.getCursor());
		node.setBlocked(false);
		assertTrue(contentComposite.isEnabled());
		assertSame(arrowCursor, parentComposite.getCursor());
	}

	private Cursor waitCursor;
	private Cursor arrowCursor;

	private class TestView extends SubModuleView<SubModuleController> {

		@Override
		protected Cursor createWaitCursor() {
			waitCursor = Display.getDefault().getSystemCursor(SWT.CURSOR_WAIT);
			return waitCursor;
		}

		@Override
		protected Cursor createArrowCursor() {
			arrowCursor = Display.getDefault().getSystemCursor(SWT.CURSOR_ARROW);
			return arrowCursor;
		}

		@Override
		protected ISubModuleNode getCurrentNode() {
			return node;
		}

		@Override
		protected void activate(ISubModuleNode source) {
		}

		@Override
		protected void basicCreatePartControl(Composite parent) {
		}
	}

}
