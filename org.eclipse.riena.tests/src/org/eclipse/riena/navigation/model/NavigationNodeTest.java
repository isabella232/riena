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
package org.eclipse.riena.navigation.model;

import junit.framework.TestCase;

import org.eclipse.riena.navigation.NavigationNodeId;

/**
 * Tests of the class {@code NavigationNode}.
 */
public class NavigationNodeTest extends TestCase {

	/**
	 * Tests the constructor of the
	 */
	public void testNavigationNode() {

		NavigationNodeId id = new NavigationNodeId("4711");
		NaviNode node = new NaviNode(id);

		assertSame(id, node.getNodeId());

		assertNotNull(node.getListeners());
		assertTrue(node.getListeners().isEmpty());

		assertNotNull(node.getActions());
		assertTrue(node.getActions().isEmpty());

		assertNotNull(node.getChildren());
		assertTrue(node.getChildren().isEmpty());

		assertNotNull(node.getMarkable());

		assertTrue(node.isVisible());

		assertTrue(node.isCreated());
		assertFalse(node.isActivated());
		assertFalse(node.isDeactivated());
		assertFalse(node.isDisposed());

	}

	private class NaviNode extends SubModuleNode {

		public NaviNode(NavigationNodeId nodeId) {
			super(nodeId);
		}

	}

}
