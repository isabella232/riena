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
package org.eclipse.riena.navigation.ui.swt.component;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.listener.SubApplicationNodeListener;
import org.eclipse.riena.navigation.model.ApplicationNode;
import org.eclipse.riena.navigation.model.NavigationProcessor;
import org.eclipse.riena.navigation.model.SubApplicationNode;
import org.eclipse.riena.ui.core.marker.DisabledMarker;
import org.eclipse.riena.ui.core.marker.HiddenMarker;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link SubApplicationSwitcherWidget}.
 */
@UITestCase
public class SubApplicationSwitcherWidgetTest extends TestCase {

	private SubApplicationSwitcherWidget switcher;
	private Shell shell;

	@Override
	protected void setUp() throws Exception {
		shell = new Shell();
		final IApplicationNode node = new ApplicationNode();
		switcher = new SubApplicationSwitcherWidget(shell, SWT.NONE, node);
	}

	@Override
	protected void tearDown() throws Exception {
		SwtUtilities.dispose(switcher);
		SwtUtilities.dispose(shell);
	}

	/**
	 * Tests the constructor of {@link SubApplicationSwitcherWidget}.
	 */
	public void testCreate() {

		final IApplicationNode node = new ApplicationNode();
		final SubApplicationNode subNode1 = new SubApplicationNode(new NavigationNodeId("sap", "sub1"), "sub1");
		subNode1.setIcon("icon1");
		List<SubApplicationNodeListener> listeners = ReflectionUtils.invokeHidden(subNode1, "getListeners",
				(Object[]) null);
		assertNotNull(listeners);
		assertTrue(listeners.isEmpty());
		node.addChild(subNode1);
		final SubApplicationNode subNode2 = new SubApplicationNode(new NavigationNodeId("sap", "sub2"), "sub2");
		subNode2.setIcon("icon2");
		node.addChild(subNode2);
		switcher = new SubApplicationSwitcherWidget(shell, SWT.NONE, node);

		final List<SubApplicationItem> items = ReflectionUtils.invokeHidden(switcher, "getItems", (Object[]) null);
		assertNotNull(items);
		assertEquals(2, items.size());
		assertEquals("sub1", items.get(0).getLabel());
		assertEquals("sub2", items.get(1).getLabel());
		assertEquals("icon1", items.get(0).getIcon());
		assertEquals("icon2", items.get(1).getIcon());

		listeners = ReflectionUtils.invokeHidden(subNode1, "getListeners", (Object[]) null);
		assertNotNull(listeners);
		assertFalse(listeners.isEmpty());
		assertEquals(1, listeners.size());

		Listener[] swtListeners = switcher.getListeners(SWT.Paint);
		assertEquals(1, swtListeners.length);

		swtListeners = switcher.getListeners(SWT.MouseDown);
		assertEquals(1, swtListeners.length);

	}

	/**
	 * Tests the method {@code isTabEnabled}.
	 */
	public void testIsTabEnabled() {

		final SubApplicationNode node = new SubApplicationNode();
		node.setNavigationProcessor(new NavigationProcessor());
		final SubApplicationItem item = new SubApplicationItem(switcher, node);
		boolean ret = ReflectionUtils.invokeHidden(switcher, "isTabEnabled", new Object[] { item });
		assertTrue(ret);

		final DisabledMarker disabledMarker = new DisabledMarker();
		node.addMarker(disabledMarker);
		ret = ReflectionUtils.invokeHidden(switcher, "isTabEnabled", new Object[] { item });
		assertFalse(ret);

		final HiddenMarker hiddenMarker = new HiddenMarker();
		node.removeMarker(disabledMarker);
		node.addMarker(hiddenMarker);
		ret = ReflectionUtils.invokeHidden(switcher, "isTabEnabled", new Object[] { item });
		assertFalse(ret);

		node.removeMarker(hiddenMarker);
		ret = ReflectionUtils.invokeHidden(switcher, "isTabEnabled", new Object[] { item });
		assertTrue(ret);

	}

	/**
	 * Tests the method {@code getItem(Point)}.
	 */
	public void testGetItemPoint() {

		final SubApplicationNode node = new SubApplicationNode();
		final SubApplicationItem item = new SubApplicationItem(switcher, node);
		final List<SubApplicationItem> items = ReflectionUtils.invokeHidden(switcher, "getItems", (Object[]) null);

		Point point = new Point(0, 0);
		SubApplicationItem retItem = ReflectionUtils.invokeHidden(switcher, "getItem", new Object[] { point });
		assertNull(retItem);

		items.add(item);
		retItem = ReflectionUtils.invokeHidden(switcher, "getItem", new Object[] { point });
		assertNull(retItem);

		point = new Point(5, 5);
		item.setBounds(new Rectangle(0, 0, 10, 10));
		retItem = ReflectionUtils.invokeHidden(switcher, "getItem", new Object[] { point });
		assertNotNull(retItem);
		assertSame(item, retItem);

		final SubApplicationNode node2 = new SubApplicationNode();
		final SubApplicationItem item2 = new SubApplicationItem(switcher, node2);
		items.add(item2);
		item2.setBounds(new Rectangle(20, 20, 10, 10));
		point = new Point(20, 20);
		retItem = ReflectionUtils.invokeHidden(switcher, "getItem", new Object[] { point });
		assertNotNull(retItem);
		assertSame(item2, retItem);

		point = new Point(31, 31);
		retItem = ReflectionUtils.invokeHidden(switcher, "getItem", new Object[] { point });
		assertNull(retItem);

	}

	/**
	 * Tests the method {@code getItem(char)}.
	 */
	public void testGetItemChar() {

		final SubApplicationNode node1 = new SubApplicationNode("abcd");
		ReflectionUtils.invokeHidden(switcher, "registerSubApplication", node1);
		final SubApplicationNode node2 = new SubApplicationNode("e&fgh");
		ReflectionUtils.invokeHidden(switcher, "registerSubApplication", node2);
		final SubApplicationNode node3 = new SubApplicationNode("IJ&K&L");
		ReflectionUtils.invokeHidden(switcher, "registerSubApplication", node3);

		SubApplicationItem retItem = ReflectionUtils.invokeHidden(switcher, "getItem", new Object[] { 'a' });
		assertNull(retItem);

		retItem = ReflectionUtils.invokeHidden(switcher, "getItem", new Object[] { 'f' });
		assertSame(node2, retItem.getSubApplicationNode());

		retItem = ReflectionUtils.invokeHidden(switcher, "getItem", new Object[] { 'F' });
		assertSame(node2, retItem.getSubApplicationNode());

		retItem = ReflectionUtils.invokeHidden(switcher, "getItem", new Object[] { 'k' });
		assertSame(node3, retItem.getSubApplicationNode());

		retItem = ReflectionUtils.invokeHidden(switcher, "getItem", new Object[] { 'K' });
		assertSame(node3, retItem.getSubApplicationNode());

		retItem = ReflectionUtils.invokeHidden(switcher, "getItem", new Object[] { 'L' });
		assertNull(retItem);

	}

	public void testDisposeSubApplication() throws Exception {

		final IApplicationNode node = new ApplicationNode();
		final SubApplicationNode subNode1 = new SubApplicationNode(new NavigationNodeId("sm", "sub1"), "sub1");
		subNode1.setIcon("icon1");
		node.addChild(subNode1);
		final SubApplicationNode subNode2 = new SubApplicationNode(new NavigationNodeId("sm", "sub2"), "sub2");
		subNode2.setIcon("icon2");
		node.addChild(subNode2);
		switcher = new SubApplicationSwitcherWidget(shell, SWT.NONE, node);
		final List<SubApplicationItem> items = ReflectionUtils.invokeHidden(switcher, "getItems", (Object[]) null);

		subNode1.dispose();

		assertEquals(1, items.size());

		subNode2.dispose();

		assertTrue(items.isEmpty());
	}

	public void testRemoveSubApplication() throws Exception {

		final IApplicationNode node = new ApplicationNode();
		final SubApplicationNode subNode1 = new SubApplicationNode(new NavigationNodeId("sm", "sub1"), "sub1");
		subNode1.setIcon("icon1");
		node.addChild(subNode1);
		final SubApplicationNode subNode2 = new SubApplicationNode(new NavigationNodeId("sm", "sub2"), "sub2");
		subNode2.setIcon("icon2");
		node.addChild(subNode2);
		switcher = new SubApplicationSwitcherWidget(shell, SWT.NONE, node);
		final List<SubApplicationItem> items = ReflectionUtils.invokeHidden(switcher, "getItems", (Object[]) null);

		node.removeChild(subNode1);

		assertEquals(1, items.size());

		node.removeChild(subNode2);

		assertTrue(items.isEmpty());
	}

	public void testAddSubApplication() throws Exception {

		final IApplicationNode node = new ApplicationNode();
		final SubApplicationNode subNode1 = new SubApplicationNode(new NavigationNodeId("sm", "sub1"), "sub1");
		subNode1.setIcon("icon1");
		node.addChild(subNode1);
		final SubApplicationNode subNode2 = new SubApplicationNode(new NavigationNodeId("sm", "sub2"), "sub2");
		subNode2.setIcon("icon2");
		node.addChild(subNode2);
		switcher = new SubApplicationSwitcherWidget(shell, SWT.NONE, node);
		final List<SubApplicationItem> items = ReflectionUtils.invokeHidden(switcher, "getItems", (Object[]) null);

		final SubApplicationNode subNode3 = new SubApplicationNode("sub3");
		subNode2.setIcon("icon3");
		node.addChild(subNode3);

		assertEquals(3, items.size());
	}

}
