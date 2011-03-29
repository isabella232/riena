/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
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

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.navigation.NavigationNodeId;

/**
 * Tests of the class {@link ModuleNode}.
 */
@NonUITestCase
public class ModuleNodeTest extends TestCase {

	/**
	 * Tests the method {@code calcDepth()}
	 */
	public void testCalcDepth() {

		final ModuleNode m = new ModuleNode();
		assertEquals(0, m.calcDepth());

		final SubModuleNode sm = new SubModuleNode(new NavigationNodeId("module1"));
		m.addChild(sm);
		assertEquals(0, m.calcDepth());

		final SubModuleNode sm2 = new SubModuleNode(new NavigationNodeId("module2"));
		m.addChild(sm2);
		assertEquals(2, m.calcDepth());

		final SubModuleNode sm21 = new SubModuleNode(new NavigationNodeId("module21"));
		sm21.setNavigationProcessor(new NavigationProcessor());
		sm2.addChild(sm21);
		assertEquals(2, m.calcDepth());
		sm2.setExpanded(true);
		assertEquals(3, m.calcDepth());

		sm21.setEnabled(false);
		assertEquals(3, m.calcDepth());
		sm21.setVisible(false);
		assertEquals(2, m.calcDepth());

		/*
		 * If there are 2 children of Module and Child 1 is not visible then the
		 * tree shell not be rendered
		 */
		sm.setVisible(false);
		assertEquals(0, m.calcDepth());

	}

}
