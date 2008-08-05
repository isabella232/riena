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
package org.eclipse.riena.navigation.ui.swt.presentation;

import org.eclipse.riena.navigation.model.NavigationNodeId;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.tests.RienaTestCase;

/**
 * Tests for the SwtPresentationManager.
 */
public class SwtPresentationManagerTest extends RienaTestCase {

	private SwtPresentationManager swtPresentationManager;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		addPluginXml(SwtPresentationManagerTest.class, "SwtPresentationManagerTest.xml");

		swtPresentationManager = new SwtPresentationManager();
	}

	public void testGetSwtViewId() throws Exception {

		SubModuleNode node = new SubModuleNode();
		node.setPresentationId(new NavigationNodeId("testId"));

		SwtViewId swtViewId = swtPresentationManager.getSwtViewId(node);

		assertEquals("org.eclipse.riena.navigation.ui.swt.views.TestView", swtViewId.getId());
		assertEquals("shared", swtViewId.getSecondary());
	}
}
