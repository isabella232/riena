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

import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISimpleNavigationNodeListener;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.ui.core.marker.DisabledMarker;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.HiddenMarker;
import org.eclipse.riena.ui.core.marker.OutputMarker;
import org.eclipse.riena.ui.filter.IUIFilter;

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

		List<?> listeners = ReflectionUtils.invokeHidden(node, "getListeners", null);
		assertNotNull(listeners);
		assertTrue(listeners.isEmpty());

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

	/**
	 * Tests the method {@code isEnabled()} and {@code
	 * isEnabled(INavigationNode<?>)}.
	 */
	public void testIsEnabled() {

		NavigationNodeId id = new NavigationNodeId("4711");
		NaviNode node = new NaviNode(id);
		node.setNavigationProcessor(new NavigationProcessor());

		assertTrue(node.isEnabled());

		DisabledMarker disabledMarker = new DisabledMarker();
		node.addMarker(disabledMarker);
		assertFalse(node.isEnabled());

		node.removeMarker(disabledMarker);
		node.addMarker(new HiddenMarker());
		assertTrue(node.isEnabled());

		NavigationNodeId id2 = new NavigationNodeId("0815");
		NaviNode node2 = new NaviNode(id2);
		node2.setParent(node);
		assertTrue(node.isEnabled());

		node.addMarker(disabledMarker);
		assertFalse(node.isEnabled());

	}

	/**
	 * Tests the method {@code isVisible()} and {@code
	 * isVisible(INavigationNode<?>)}.
	 */
	public void testIsVisible() {

		NavigationNodeId id = new NavigationNodeId("4711");
		NaviNode node = new NaviNode(id);
		node.setNavigationProcessor(new NavigationProcessor());

		assertTrue(node.isVisible());

		HiddenMarker hiddenMarker = new HiddenMarker();
		node.addMarker(hiddenMarker);
		assertFalse(node.isVisible());

		node.removeMarker(hiddenMarker);
		node.addMarker(new DisabledMarker());
		assertTrue(node.isVisible());

		NavigationNodeId id2 = new NavigationNodeId("0815");
		NaviNode node2 = new NaviNode(id2);
		node2.setParent(node);
		assertTrue(node.isVisible());

		node.addMarker(hiddenMarker);
		assertFalse(node.isVisible());

	}

	/**
	 * Tests the method {@code setEnabled}.
	 */
	public void testSetEnabled() {

		NavigationNodeId id = new NavigationNodeId("4711");
		NaviNode node = new NaviNode(id);
		node.setNavigationProcessor(new NavigationProcessor());

		assertTrue(node.isEnabled());

		node.setEnabled(false);
		assertFalse(node.isEnabled());
		Collection<DisabledMarker> markers = node.getMarkersOfType(DisabledMarker.class);
		assertNotNull(markers);
		assertTrue(markers.size() == 1);
		IMarker marker1 = markers.iterator().next();

		node.setEnabled(false);
		assertFalse(node.isEnabled());
		markers = node.getMarkersOfType(DisabledMarker.class);
		assertNotNull(markers);
		assertTrue(markers.size() == 1);
		assertSame(marker1, markers.iterator().next());

		node.setEnabled(true);
		assertTrue(node.isEnabled());
		markers = node.getMarkersOfType(DisabledMarker.class);
		assertNotNull(markers);
		assertTrue(markers.isEmpty());

		node.setEnabled(false);
		assertFalse(node.isEnabled());
		markers = node.getMarkersOfType(DisabledMarker.class);
		assertNotNull(markers);
		assertTrue(markers.size() == 1);
		assertSame(marker1, markers.iterator().next());

	}

	/**
	 * Tests the method {@code setVisible}.
	 */
	public void testSetVisible() {

		NavigationNodeId id = new NavigationNodeId("4711");
		NaviNode node = new NaviNode(id);
		node.setNavigationProcessor(new NavigationProcessor());

		assertTrue(node.isVisible());

		node.setVisible(false);
		assertFalse(node.isVisible());
		Collection<HiddenMarker> markers = node.getMarkersOfType(HiddenMarker.class);
		assertNotNull(markers);
		assertTrue(markers.size() == 1);
		IMarker marker1 = markers.iterator().next();

		node.setVisible(false);
		assertFalse(node.isVisible());
		markers = node.getMarkersOfType(HiddenMarker.class);
		assertNotNull(markers);
		assertTrue(markers.size() == 1);
		assertSame(marker1, markers.iterator().next());

		node.setVisible(true);
		assertTrue(node.isVisible());
		markers = node.getMarkersOfType(HiddenMarker.class);
		assertNotNull(markers);
		assertTrue(markers.isEmpty());

		node.setVisible(false);
		assertFalse(node.isVisible());
		markers = node.getMarkersOfType(HiddenMarker.class);
		assertNotNull(markers);
		assertTrue(markers.size() == 1);
		assertSame(marker1, markers.iterator().next());

	}

	/**
	 * Tests the method {@code addMarker(INavigationContext, IMarker)}.
	 */
	public void testAddMarker() {

		NavigationNodeId id = new NavigationNodeId("4711");
		NaviNode node = new NaviNode(id);
		node.setNavigationProcessor(new NavigationProcessor());

		NavigationNodeId id2 = new NavigationNodeId("0815");
		NaviNode node2 = new NaviNode(id2);
		node2.setParent(node);
		node.addChild(node2);

		node.reset();
		node2.reset();
		IMarker hiddenMarker = new HiddenMarker();
		node.addMarker(null, hiddenMarker);
		assertTrue(node.getMarkersOfType(HiddenMarker.class).size() == 1);
		assertTrue(node2.getMarkersOfType(HiddenMarker.class).size() == 1);
		assertTrue(node.isMarkersChangedCalled());
		assertTrue(node2.isMarkersChangedCalled());

		node.reset();
		node2.reset();
		IMarker disabledMarker = new DisabledMarker();
		node.addMarker(null, disabledMarker);
		assertTrue(node.getMarkersOfType(DisabledMarker.class).size() == 1);
		assertTrue(node2.getMarkersOfType(DisabledMarker.class).size() == 1);
		assertTrue(node.isMarkersChangedCalled());
		assertTrue(node2.isMarkersChangedCalled());

		node.reset();
		node2.reset();
		IMarker outputMarker = new OutputMarker();
		node.addMarker(null, outputMarker);
		assertTrue(node.getMarkersOfType(OutputMarker.class).size() == 1);
		assertTrue(node2.getMarkersOfType(OutputMarker.class).isEmpty());
		assertTrue(node.isMarkersChangedCalled());
		assertFalse(node2.isMarkersChangedCalled());

		node.reset();
		node2.reset();
		IMarker errorMarker = new ErrorMarker();
		node.addMarker(null, errorMarker);
		assertTrue(node.getMarkersOfType(ErrorMarker.class).size() == 1);
		assertTrue(node2.getMarkersOfType(ErrorMarker.class).isEmpty());
		assertTrue(node.isMarkersChangedCalled());
		assertFalse(node2.isMarkersChangedCalled());

	}

	/**
	 * Tests the method {@code removeMarker(INavigationContext, IMarker)}.
	 */
	public void testRemoveMarker() {

		NavigationNodeId id = new NavigationNodeId("4711");
		NaviNode node = new NaviNode(id);
		node.setNavigationProcessor(new NavigationProcessor());

		NavigationNodeId id2 = new NavigationNodeId("0815");
		NaviNode node2 = new NaviNode(id2);
		node2.setParent(node);
		node.addChild(node2);

		// add markers; they are removed later
		IMarker hiddenMarker = new HiddenMarker();
		node.addMarker(null, hiddenMarker);
		assertTrue(node.getMarkersOfType(HiddenMarker.class).size() == 1);
		assertTrue(node2.getMarkersOfType(HiddenMarker.class).size() == 1);

		IMarker disabledMarker = new DisabledMarker();
		node.addMarker(null, disabledMarker);
		assertTrue(node.getMarkersOfType(DisabledMarker.class).size() == 1);
		assertTrue(node2.getMarkersOfType(DisabledMarker.class).size() == 1);

		IMarker outputMarker = new OutputMarker();
		node.addMarker(null, outputMarker);
		assertTrue(node.getMarkersOfType(OutputMarker.class).size() == 1);
		assertTrue(node2.getMarkersOfType(OutputMarker.class).isEmpty());

		IMarker errorMarker = new ErrorMarker();
		node.addMarker(null, errorMarker);
		assertTrue(node.getMarkersOfType(ErrorMarker.class).size() == 1);
		assertTrue(node2.getMarkersOfType(ErrorMarker.class).isEmpty());

		// remove markers
		node.reset();
		node2.reset();
		node.removeMarker(hiddenMarker);
		assertTrue(node.getMarkersOfType(HiddenMarker.class).isEmpty());
		assertTrue(node2.getMarkersOfType(HiddenMarker.class).isEmpty());
		assertTrue(node.getMarkersOfType(DisabledMarker.class).size() == 1);
		assertTrue(node2.getMarkersOfType(DisabledMarker.class).size() == 1);
		assertTrue(node.getMarkersOfType(OutputMarker.class).size() == 1);
		assertTrue(node.getMarkersOfType(ErrorMarker.class).size() == 1);
		assertTrue(node.isMarkersChangedCalled());
		assertTrue(node2.isMarkersChangedCalled());

		node.reset();
		node2.reset();
		node.removeMarker(disabledMarker);
		assertTrue(node.getMarkersOfType(DisabledMarker.class).isEmpty());
		assertTrue(node2.getMarkersOfType(DisabledMarker.class).isEmpty());
		assertTrue(node.getMarkersOfType(OutputMarker.class).size() == 1);
		assertTrue(node.getMarkersOfType(ErrorMarker.class).size() == 1);
		assertTrue(node.isMarkersChangedCalled());
		assertTrue(node2.isMarkersChangedCalled());

		node.reset();
		node2.reset();
		node.removeMarker(outputMarker);
		assertTrue(node.getMarkersOfType(OutputMarker.class).isEmpty());
		assertTrue(node.getMarkersOfType(ErrorMarker.class).size() == 1);
		assertTrue(node.isMarkersChangedCalled());
		assertFalse(node2.isMarkersChangedCalled());

		node.reset();
		node2.reset();
		node.removeMarker(errorMarker);
		assertTrue(node.getMarkersOfType(ErrorMarker.class).isEmpty());
		assertTrue(node.isMarkersChangedCalled());
		assertFalse(node2.isMarkersChangedCalled());

		// add and remove
		node.addMarker(errorMarker);
		node2.addMarker(errorMarker);

		node.reset();
		node2.reset();
		node.removeMarker(errorMarker);
		assertTrue(node.getMarkersOfType(ErrorMarker.class).isEmpty());
		assertTrue(node2.getMarkersOfType(ErrorMarker.class).size() == 1);
		assertTrue(node.isMarkersChangedCalled());
		assertFalse(node2.isMarkersChangedCalled());

	}

	/**
	 * Tests the method {@code findNode(NavigationNodeId)}.
	 */
	public void testFindNode() {

		NavigationNodeId id = new NavigationNodeId("4711");
		NaviNode node = new NaviNode(id);
		assertSame(node, node.findNode(id));

		assertNull(node.findNode(null));

		assertNull(node.findNode(new NavigationNodeId("someId")));

		NavigationNodeId id2 = new NavigationNodeId("0815");
		NaviNode node2 = new NaviNode(id2);
		node2.setParent(node);
		node.addChild(node2);
		assertSame(node2, node.findNode(id2));

	}

	/**
	 * Tests the method {@code addNode}.
	 */
	public void testAddChild() {

		NavigationNodeId id = new NavigationNodeId("4711");
		NaviNode node = new NaviNode(id);
		node.reset();
		node.addChild(null);
		assertTrue(node.getChildren().isEmpty());
		assertFalse(node.isChildAddedCalled());

		NavigationNodeId id2 = new NavigationNodeId("2");
		NaviNode node2 = new NaviNode(id2);
		node.reset();
		node.addChild(node2);
		assertTrue(node.getChildren().size() == 1);
		assertSame(node2, node.getChildren().get(0));
		assertSame(node, node2.getParent());
		assertTrue(node.isChildAddedCalled());

		node.reset();
		node.addChild(node2);
		assertTrue(node.getChildren().size() == 1);
		assertSame(node2, node.getChildren().get(0));
		assertSame(node, node2.getParent());
		assertFalse(node.isChildAddedCalled());

		NavigationNodeId id3 = new NavigationNodeId("3");
		NaviNode node3 = new NaviNode(id3);
		node3.setNavigationProcessor(new NavigationProcessor());
		node3.dispose();
		node.reset();
		node.addChild(node3);
		assertTrue(node.getChildren().size() == 1);
		assertSame(node2, node.getChildren().get(0));
		assertNull(node3.getParent());
		assertFalse(node.isChildAddedCalled());

	}

	/**
	 * Tests the method {@code removeNode}.
	 */
	public void testRemoveChild() {

		NavigationNodeId id = new NavigationNodeId("4711");
		NaviNode node = new NaviNode(id);

		NavigationNodeId id2 = new NavigationNodeId("2");
		NaviNode node2 = new NaviNode(id2);
		node.addChild(node2);

		NavigationNodeId id3 = new NavigationNodeId("3");
		NaviNode node3 = new NaviNode(id3);
		node.addChild(node3);

		node.reset();
		node2.reset();
		node3.reset();
		node.removeChild(null, null);
		assertTrue(node.getChildren().size() == 2);
		assertSame(node, node2.getParent());
		assertSame(node, node3.getParent());
		assertFalse(node.isChildRemovedCalled());
		assertFalse(node2.isChildRemovedCalled());
		assertFalse(node3.isChildRemovedCalled());

		// try to remove a node
		node.reset();
		node2.reset();
		node3.reset();
		node.removeChild(null, node3);
		assertTrue(node.getChildren().size() == 1);
		assertSame(node, node2.getParent());
		assertNull(node3.getParent());
		assertTrue(node.isChildRemovedCalled());
		assertFalse(node2.isChildRemovedCalled());
		assertFalse(node3.isChildRemovedCalled());

		// try to remove an activated node
		node.reset();
		node2.reset();
		node3.reset();
		node2.activate(null);
		node.removeChild(null, node2);
		assertTrue(node.getChildren().size() == 1);
		assertSame(node, node2.getParent());
		assertNull(node3.getParent());
		assertFalse(node.isChildRemovedCalled());
		assertFalse(node2.isChildRemovedCalled());
		assertFalse(node3.isChildRemovedCalled());

		// try to remove a deactivated node
		node.reset();
		node2.reset();
		node3.reset();
		node2.deactivate(null);
		node.removeChild(null, node2);
		assertTrue(node.getChildren().isEmpty());
		assertNull(node2.getParent());
		assertNull(node3.getParent());
		assertTrue(node.isChildRemovedCalled());
		assertFalse(node2.isChildRemovedCalled());
		assertFalse(node3.isChildRemovedCalled());

		// try to remove the same node again
		node.reset();
		node2.reset();
		node3.reset();
		node.removeChild(null, node2);
		assertTrue(node.getChildren().isEmpty());
		assertNull(node2.getParent());
		assertNull(node3.getParent());
		assertFalse(node.isChildRemovedCalled());
		assertFalse(node2.isChildRemovedCalled());
		assertFalse(node3.isChildRemovedCalled());

	}

	private class NaviNode extends SubModuleNode implements ISimpleNavigationNodeListener {

		private boolean markersChangedCalled;
		private boolean childAddedCalled;
		private boolean childRemovedCalled;

		public NaviNode(NavigationNodeId nodeId) {
			super(nodeId);
			addSimpleListener(this);
		}

		public void reset() {
			markersChangedCalled = false;
			childAddedCalled = false;
			childRemovedCalled = false;
		}

		public void activated(INavigationNode<?> source) {
		}

		public void afterActivated(INavigationNode<?> source) {
		}

		public void afterDeactivated(INavigationNode<?> source) {
		}

		public void afterDisposed(INavigationNode<?> source) {
		}

		public void beforeActivated(INavigationNode<?> source) {
		}

		public void beforeDeactivated(INavigationNode<?> source) {
		}

		public void beforeDisposed(INavigationNode<?> source) {
		}

		public void block(INavigationNode<?> source, boolean block) {
		}

		public void childAdded(INavigationNode<?> source, INavigationNode<?> childAdded) {
			childAddedCalled = true;
		}

		public void childRemoved(INavigationNode<?> source, INavigationNode<?> childRemoved) {
			childRemovedCalled = true;
		}

		public void deactivated(INavigationNode<?> source) {
		}

		public void disposed(INavigationNode<?> source) {
		}

		public void expandedChanged(INavigationNode<?> source) {
		}

		public void filterAdded(INavigationNode<?> source, IUIFilter filter) {
		}

		public void filterRemoved(INavigationNode<?> source, IUIFilter filter) {
		}

		public void iconChanged(INavigationNode<?> source) {
		}

		public void labelChanged(INavigationNode<?> source) {
		}

		public void markersChanged(INavigationNode<?> source) {
			markersChangedCalled = true;
		}

		public void parentChanged(INavigationNode<?> source) {
		}

		public void presentationChanged(INavigationNode<?> source) {
		}

		public void selectedChanged(INavigationNode<?> source) {
		}

		public void stateChanged(INavigationNode<?> source,
				org.eclipse.riena.navigation.INavigationNode.State oldState,
				org.eclipse.riena.navigation.INavigationNode.State newState) {
		}

		public boolean isMarkersChangedCalled() {
			return markersChangedCalled;
		}

		public boolean isChildAddedCalled() {
			return childAddedCalled;
		}

		public boolean isChildRemovedCalled() {
			return childRemovedCalled;
		}

	}

}
