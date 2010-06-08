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
package org.eclipse.riena.internal.navigation.ui.swt.handlers;

import org.easymock.EasyMock;

import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.navigation.ISubApplicationNode;

/**
 * Tests for the class {@link SwitchSubApplication}.
 */
@NonUITestCase
public class SwitchSubApplicationTest extends RienaTestCase {

	private SwitchSubApplication handler;
	private ISubApplicationNode nodeA;
	private ISubApplicationNode nodeB;
	private ISubApplicationNode nodeC;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		handler = new SwitchSubApplication();
		nodeA = EasyMock.createMock(ISubApplicationNode.class);
		nodeB = EasyMock.createMock(ISubApplicationNode.class);
		nodeC = EasyMock.createMock(ISubApplicationNode.class);
	}

	// find next

	public void testFindNextSubApplicationAtoB() {
		EasyMock.expect(nodeA.isSelected()).andReturn(true);
		EasyMock.replay(nodeA);
		ISubApplicationNode[] nodes = { nodeA, nodeB, nodeC };

		assertSame(nodeB, handler.findNextNode(nodes));
	}

	public void testFindNextSubApplicationCtoA() {
		EasyMock.expect(nodeA.isSelected()).andReturn(false).times(2);
		EasyMock.replay(nodeA);
		EasyMock.expect(nodeC.isSelected()).andReturn(true);
		EasyMock.replay(nodeC);
		ISubApplicationNode[] nodes = { nodeA, nodeB, nodeC };

		assertSame(nodeA, handler.findNextNode(nodes));
	}

	public void testFindNextSubApplicationEmpty() {
		ISubApplicationNode[] nodes = {};

		assertNull(handler.findNextNode(nodes));
	}

	public void testFindNextSubApplicationAtoA() {
		EasyMock.expect(nodeA.isSelected()).andReturn(true);
		EasyMock.replay(nodeA);
		ISubApplicationNode[] nodes = {};

		assertNull(handler.findNextNode(nodes));
	}

	public void testFindNextSubApplicationNoneSelected() {
		ISubApplicationNode[] nodes = { nodeA, nodeB, nodeC };

		assertNull(handler.findNextNode(nodes));
	}

	public void testFindNextSubApplicationTwoSelected() {
		EasyMock.expect(nodeA.isSelected()).andReturn(true);
		EasyMock.replay(nodeA);
		EasyMock.expect(nodeB.isSelected()).andReturn(true);
		EasyMock.replay(nodeB);
		ISubApplicationNode[] nodes = { nodeA, nodeB, nodeC };

		assertNull(handler.findNextNode(nodes));
	}

	// find previous

	public void testFindPrevSubApplicationCtoB() {
		EasyMock.expect(nodeC.isSelected()).andReturn(true);
		EasyMock.replay(nodeC);
		ISubApplicationNode[] nodes = { nodeA, nodeB, nodeC };

		assertSame(nodeB, handler.findPreviousNode(nodes, true));
	}

	public void testFindPrevSubApplicationAtoC() {
		EasyMock.expect(nodeA.isSelected()).andReturn(true);
		EasyMock.replay(nodeA);
		EasyMock.expect(nodeC.isSelected()).andReturn(false).times(2);
		EasyMock.replay(nodeC);
		ISubApplicationNode[] nodes = { nodeA, nodeB, nodeC };

		assertSame(nodeC, handler.findPreviousNode(nodes, true));
	}

	public void testFindPrevSubApplicationEmpty() {
		ISubApplicationNode[] nodes = {};
		assertNull(handler.findPreviousNode(nodes, true));
	}

	public void testFindPrevSubApplicationAtoA() {
		EasyMock.expect(nodeA.isSelected()).andReturn(true);
		EasyMock.replay(nodeA);
		ISubApplicationNode[] nodes = {};

		assertNull(handler.findPreviousNode(nodes, true));
	}

	public void testFindPrevSubApplicationNoneSelected() {
		ISubApplicationNode[] nodes = { nodeA, nodeB, nodeC };

		assertNull(handler.findPreviousNode(nodes, true));
	}

	public void testFindPrevSubApplicationTwoSelected() {
		EasyMock.expect(nodeA.isSelected()).andReturn(true);
		EasyMock.replay(nodeA);
		EasyMock.expect(nodeB.isSelected()).andReturn(true);
		EasyMock.replay(nodeB);
		ISubApplicationNode[] nodes = { nodeA, nodeB, nodeC };

		assertNull(handler.findPreviousNode(nodes, true));
	}

}