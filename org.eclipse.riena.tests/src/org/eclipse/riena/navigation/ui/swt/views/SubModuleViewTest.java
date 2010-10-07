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
package org.eclipse.riena.navigation.ui.swt.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.ApplicationNode;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubApplicationNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.ModuleController;

/**
 * Tests for the SubModuleNodeView.
 */
@UITestCase
public class SubModuleViewTest extends RienaTestCase {

	private SubModuleView subModuleNodeView;
	private SubModuleNode node;
	private SubModuleNode anotherNode;
	private SubModuleNode anotherNodeSameView;
	private List<SubModuleNode> nodesBoundToView;
	private ApplicationNode appNode;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		addPluginXml(SubModuleViewTest.class, "SubModuleViewTest.xml");

		appNode = new ApplicationNode();
		final SubApplicationNode subAppNode = new SubApplicationNode();
		appNode.addChild(subAppNode);
		final ModuleGroupNode mgNode = new ModuleGroupNode(null);
		subAppNode.addChild(mgNode);
		final IModuleNode parent = new ModuleNode(null, "TestModuleLabel");
		mgNode.addChild(parent);

		anotherNode = new SubModuleNode(new NavigationNodeId("testId2", "2"), "TestSubModuleLabel2");
		parent.addChild(anotherNode);
		anotherNodeSameView = new SubModuleNode(new NavigationNodeId("testId", "1"), "TestSubModuleLabel3");
		parent.addChild(anotherNodeSameView);
		nodesBoundToView = new ArrayList<SubModuleNode>();

		subModuleNodeView = new TestView();
		node = new SubModuleNode(new NavigationNodeId("testId", "0"), "TestSubModuleLabel");
		parent.setNavigationNodeController(new ModuleController(parent));
		parent.addChild(node);
		subModuleNodeView.createPartControl(new Shell());
		node.activate();
	}

	@Override
	protected void tearDown() throws Exception {
		node.deactivate(null);
		removeExtension("sub.module.view.test");
		super.tearDown();
	}

	public void testBlocking() {
		node.setBlocked(true);
		final Composite parentComposite = ReflectionUtils.invokeHidden(subModuleNodeView, "getParentComposite");
		final Composite contentComposite = ReflectionUtils.invokeHidden(subModuleNodeView, "getContentComposite");
		assertFalse(contentComposite.isEnabled());
		final Cursor waitCursor = parentComposite.getDisplay().getSystemCursor(SWT.CURSOR_WAIT);
		assertSame(waitCursor, parentComposite.getCursor());
		node.setBlocked(false);
		assertTrue(contentComposite.isEnabled());
		final Cursor arrowCursor = parentComposite.getDisplay().getSystemCursor(SWT.CURSOR_ARROW);
		assertSame(arrowCursor, parentComposite.getCursor());
	}

	// FIXME this fails when tested in the build, subModuleNodeView.getController() returns null
	public void xxx_testCreateController() throws Exception {
		assertNotNull(subModuleNodeView.getController());
		assertEquals(node, subModuleNodeView.getController().getNavigationNode());
	}

	public void testBindOnActivate() throws Exception {
		nodesBoundToView.clear();

		anotherNode.activate();

		assertTrue(nodesBoundToView.isEmpty());

		anotherNodeSameView.activate();

		assertTrue(nodesBoundToView.isEmpty());

		node.activate();
		assertEquals(1, nodesBoundToView.size());
		assertSame(node, nodesBoundToView.get(0));
	}

	// helping classes
	//////////////////

	private final class TestView extends SubModuleView {
		@Override
		public void bind(final SubModuleNode node) {
			nodesBoundToView.add(node);
		}

		@Override
		public SubModuleNode getNavigationNode() {
			return node;
		}

		@Override
		protected void basicCreatePartControl(final Composite parent) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.riena.navigation.ui.swt.views.SubModuleView#getAppNode()
		 */
		@Override
		protected IApplicationNode getAppNode() {
			return appNode;
		}
	}

}
