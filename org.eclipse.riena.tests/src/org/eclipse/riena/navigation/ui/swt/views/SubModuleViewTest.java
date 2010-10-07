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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.ApplicationNode;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubApplicationNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.ModuleController;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;

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
	private IModuleNode moduleNode;
	private ArrayList<SubModuleNode> nodesBoundToSharedView;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		addPluginXml(SubModuleViewTest.class, "SubModuleViewTest.xml");

		appNode = new ApplicationNode();
		final SubApplicationNode subAppNode = new SubApplicationNode();
		appNode.addChild(subAppNode);
		final ModuleGroupNode mgNode = new ModuleGroupNode(null);
		subAppNode.addChild(mgNode);
		moduleNode = new ModuleNode(null, "TestModuleLabel");
		mgNode.addChild(moduleNode);

		anotherNode = new SubModuleNode(new NavigationNodeId("testId2", "2"), "TestSubModuleLabel2");
		moduleNode.addChild(anotherNode);
		anotherNodeSameView = new SubModuleNode(new NavigationNodeId("testId", "1"), "TestSubModuleLabel3");
		moduleNode.addChild(anotherNodeSameView);
		nodesBoundToView = new ArrayList<SubModuleNode>();
		nodesBoundToSharedView = new ArrayList<SubModuleNode>();

		subModuleNodeView = new TestView();
		node = new SubModuleNode(new NavigationNodeId("testId", "0"), "TestSubModuleLabel");
		moduleNode.setNavigationNodeController(new ModuleController(moduleNode));
		moduleNode.addChild(node);
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

	public void testShared() {
		subModuleNodeView.dispose();
		final SubModuleNode s1 = new SubModuleNode(new NavigationNodeId("s", "1"));
		final SubModuleNode s2 = new SubModuleNode(new NavigationNodeId("s", "2"));
		moduleNode.addChild(s1);
		moduleNode.addChild(s2);
		final TestSharedView subModuleNodeView = new TestSharedView();
		subModuleNodeView.s1 = s1;
		subModuleNodeView.s2 = s2;
		subModuleNodeView.createPartControl(new Shell());
		assertNotNull(s1.getNavigationNodeController());
		s1.activate();
		final SubModuleController s1c = (SubModuleController) s1.getNavigationNodeController();
		assertNotNull(s1c.getRidget("button"));
		assertEquals(nodesBoundToSharedView.get(0), s1);
		s2.activate();
		assertEquals(nodesBoundToSharedView.get(2), s2);
		s1.dispose();
		assertFalse(subModuleNodeView.unbindActiveCalled);
		s2.dispose();
		assertTrue(subModuleNodeView.unbindActiveCalled);
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

	private final class TestSharedView extends SubModuleView {

		SubModuleNode s1;
		SubModuleNode s2;
		Button button;

		boolean unbindActiveCalled;

		@Override
		public void bind(final SubModuleNode node) {
			if (node.getNavigationNodeController() == null) {
				node.setNavigationNodeController(createController(node));
			}
			ReflectionUtils.setHidden(this, "currentController", node.getNavigationNodeController());
			nodesBoundToSharedView.add(node);
		}

		@Override
		public SubModuleNode getNavigationNode() {
			return s1.isActivated() ? s1 : s2.isActivated() ? s2 : s1;
		}

		@Override
		protected void basicCreatePartControl(final Composite parent) {
			button = new Button(parent, SWT.NONE);
			addUIControl(button, "button");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.riena.navigation.ui.swt.views.SubModuleView#
		 * unbindActiveController()
		 */
		@Override
		protected void unbindActiveController() {
			unbindActiveCalled = true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.riena.navigation.ui.swt.views.SubModuleView#createController
		 * (org.eclipse.riena.navigation.ISubModuleNode)
		 */
		@Override
		protected SubModuleController createController(final ISubModuleNode node) {
			return new SubModuleController(node);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.riena.navigation.ui.swt.views.SubModuleView#getSecondaryId
		 * ()
		 */
		@Override
		protected String getSecondaryId() {
			return SubModuleView.SHARED_ID;
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
