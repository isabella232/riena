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
package org.eclipse.riena.navigation;

import junit.framework.TestCase;

import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubModuleNode;

/**
 * Tests of the class {@link NavigationNodeUtility}.
 */
public class NavigationNodeUtilityTest extends TestCase {

	/**
	 * Tests the method {@code getLongNodeId}.
	 */
	public void testGetLongNodeId() {

		ISubModuleNode sm = new SubModuleNode(new NavigationNodeId("sm"));
		assertEquals("/sm", NavigationNodeUtility.getNodeLongId(sm));

		ISubModuleNode sm2 = new SubModuleNode(new NavigationNodeId("sm2"));
		sm.setParent(sm2);
		assertEquals("/sm2/sm", NavigationNodeUtility.getNodeLongId(sm));

		IModuleNode m = new ModuleNode();
		sm2.setParent(m);
		assertEquals("//sm2/sm", NavigationNodeUtility.getNodeLongId(sm));

		IModuleGroupNode mg = new ModuleGroupNode(new NavigationNodeId("group"));
		m.setParent(mg);
		assertEquals("/group//sm2/sm", NavigationNodeUtility.getNodeLongId(sm));

	}

}
