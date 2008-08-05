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

import org.eclipse.riena.navigation.IApplicationModel;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.model.ApplicationModel;
import org.eclipse.riena.navigation.model.NavigationNodeId;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.tests.RienaTestCase;
import org.eclipse.swt.widgets.Composite;

/**
 * Tests for the SubModuleNodeView.
 */
public class SubModuleNodeViewTest extends RienaTestCase {

	private SubModuleNodeView<SubModuleController> subModuleNodeView;
	private SubModuleNode node;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		addPluginXml(SubModuleNodeViewTest.class, "SubModuleNodeViewTest.xml");

		subModuleNodeView = new TestView();
		node = new SubModuleNode() {
			@Override
			public <T> T getTypecastedAdapter(Class<T> clazz) {
				if (clazz.equals(IApplicationModel.class)) {
					return (T) new ApplicationModel();
				}
				return null;
			}
		};
		node.setPresentationId(new NavigationNodeId("testId"));
		node.setLabel("Test");
	}

	public void testCreateController() throws Exception {

		subModuleNodeView.createPartControl(null);

		assertTrue(subModuleNodeView.getController() instanceof SubModuleController);
		assertEquals(node, subModuleNodeView.getController().getNavigationNode());
	}

	private class TestView extends SubModuleNodeView<SubModuleController> {

		@Override
		protected ISubModuleNode getCurrentNode() {
			return node;
		}

		@Override
		protected void basicCreatePartControl(Composite parent) {
			// ignore
		}
	}

}
