/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.ui.controllers;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;

import org.eclipse.core.databinding.BindingException;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.internal.ui.ridgets.swt.LabelRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.TextRidget;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.model.NavigationProcessor;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.ui.core.marker.DisabledMarker;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.HiddenMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.core.marker.OutputMarker;
import org.eclipse.riena.ui.ridgets.AbstractCompositeRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.listener.IFocusListener;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link NavigationNodeController}.
 */
@UITestCase
public class NavigationNodeControllerTest extends RienaTestCase {

	private MyNavigationNodeController controller;
	private SubModuleNode node;
	private Shell shell;

	@Override
	protected void setUp() throws Exception {

		Display display = Display.getDefault();
		shell = new Shell(display);
		shell.pack();
		shell.setVisible(true);

		Realm realm = SWTObservables.getRealm(display);
		assertNotNull(realm);
		ReflectionUtils.invokeHidden(realm, "setDefault", realm);

		node = new SubModuleNode();
		node.setNavigationProcessor(new NavigationProcessor());
		controller = new MyNavigationNodeController(node);
	}

	@Override
	protected void tearDown() throws Exception {
		controller = null;
		node = null;
		SwtUtilities.disposeWidget(shell);
	}

	/**
	 * Tests the method {@code addRidget}.
	 */
	public void testAddRidget() {

		LabelRidget ridget = new LabelRidget();
		controller.addRidget("4711", ridget);
		assertNotNull(controller.getRidgets());
		assertEquals(1, controller.getRidgets().size());

		PropertyChangeSupport support = ReflectionUtils.getHidden(ridget, "propertyChangeSupport");
		assertNotNull(support.getPropertyChangeListeners());
		assertEquals(2, support.getPropertyChangeListeners().length);

	}

	/**
	 * Tests the method {@code updateNavigationNodeMarkers()}.
	 */
	public void testUpdateNavigationNodeMarkers() {

		TextRidget ridget = new TextRidget();
		ridget.setUIControl(new Text(shell, 0));
		ridget.addMarker(new ErrorMarker());
		controller.addRidget("4711", ridget);
		controller.updateNavigationNodeMarkers();
		assertFalse(node.getMarkers().isEmpty());
		assertFalse(node.getMarkersOfType(ErrorMarker.class).isEmpty());

		ridget.addMarker(new MandatoryMarker());
		controller.updateNavigationNodeMarkers();
		assertFalse(node.getMarkers().isEmpty());
		assertFalse(node.getMarkersOfType(ErrorMarker.class).isEmpty());
		assertFalse(node.getMarkersOfType(MandatoryMarker.class).isEmpty());

		ridget.setText("testtext");
		controller.updateNavigationNodeMarkers();
		assertFalse(node.getMarkers().isEmpty());
		assertFalse(node.getMarkersOfType(ErrorMarker.class).isEmpty());
		// the mandatory marker of the ridget is disabled because the text is not empty
		assertTrue(node.getMarkersOfType(MandatoryMarker.class).isEmpty());

	}

	/**
	 * Tests the private method {@code getRidgetMarkers()}.
	 */
	public void testGetRidgetMarkers() {

		TextRidget ridget = new TextRidget();
		ridget.setUIControl(new Text(shell, 0));
		controller.addRidget("4711", ridget);
		TextRidget ridget2 = new TextRidget();
		ridget2.setUIControl(new Text(shell, 0));
		controller.addRidget("0815", ridget2);

		Collection<IMarker> markers = ReflectionUtils.invokeHidden(controller, "getRidgetMarkers", (Object[]) null);
		assertNotNull(markers);

		ErrorMarker errorMarker = new ErrorMarker();
		ridget.addMarker(errorMarker);
		OutputMarker outputMarker = new OutputMarker();
		ridget2.addMarker(outputMarker);
		markers = ReflectionUtils.invokeHidden(controller, "getRidgetMarkers", (Object[]) null);
		assertNotNull(markers);
		assertEquals(2, markers.size());
		assertTrue(markers.contains(errorMarker));
		assertTrue(markers.contains(outputMarker));

		CompositeRidget compositeRidget = new CompositeRidget();
		TextRidget ridget3 = new TextRidget();
		ridget3.setUIControl(new Text(shell, 0));
		compositeRidget.addRidget("label3", ridget3);
		controller.addRidget("comp", compositeRidget);
		MandatoryMarker mandatoryMarker = new MandatoryMarker();
		ridget3.addMarker(mandatoryMarker);
		markers = ReflectionUtils.invokeHidden(controller, "getRidgetMarkers", (Object[]) null);
		assertNotNull(markers);
		assertEquals(3, markers.size());
		assertTrue(markers.contains(mandatoryMarker));

	}

	public void testGetRidget() throws Exception {
		IRidget ridget = controller.getRidget(MockRidget.class, "myMock");
		assertEquals(MockRidget.class, ridget.getClass());

		try {
			ridget = controller.getRidget(IMockRidget.class, "myMockInterface");
			fail("BindingException expected");
		} catch (BindingException e) {
			ok("BindingException expected");
		}
	}

	/**
	 * Test for bug 269131.
	 */
	public void testHiddenAndDisabledMarkersBlockAllRidgetMarkers() throws Exception {

		TextRidget ridget = new TextRidget();
		ridget.setUIControl(new Text(shell, 0));
		controller.addRidget("4711", ridget);
		TextRidget ridget2 = new TextRidget();
		ridget2.setUIControl(new Text(shell, 0));
		controller.addRidget("0815", ridget2);

		IMarker errorMarker = new ErrorMarker();
		IMarker mandatoryMarker = new MandatoryMarker();
		IMarker hiddenMarker = new HiddenMarker();
		IMarker disabledMarker = new DisabledMarker();

		ridget.addMarker(errorMarker);
		ridget.addMarker(mandatoryMarker);

		Collection<IMarker> markers = ReflectionUtils.invokeHidden(controller, "getRidgetMarkers", (Object[]) null);
		assertEquals(2, markers.size());
		assertTrue(markers.contains(errorMarker));
		assertTrue(markers.contains(mandatoryMarker));

		ridget.addMarker(hiddenMarker);

		markers = ReflectionUtils.invokeHidden(controller, "getRidgetMarkers", (Object[]) null);
		assertTrue(markers.isEmpty());

		ridget2.addMarker(errorMarker);
		ridget2.addMarker(mandatoryMarker);

		markers = ReflectionUtils.invokeHidden(controller, "getRidgetMarkers", (Object[]) null);
		assertEquals(2, markers.size());
		assertTrue(markers.contains(errorMarker));
		assertTrue(markers.contains(mandatoryMarker));

		ridget2.addMarker(disabledMarker);

		markers = ReflectionUtils.invokeHidden(controller, "getRidgetMarkers", (Object[]) null);
		assertTrue(markers.isEmpty());

		ridget.removeMarker(hiddenMarker);

		markers = ReflectionUtils.invokeHidden(controller, "getRidgetMarkers", (Object[]) null);
		assertEquals(2, markers.size());
		assertTrue(markers.contains(errorMarker));
		assertTrue(markers.contains(mandatoryMarker));
	}

	public static class MyNavigationNodeController extends SubModuleController {

		public MyNavigationNodeController(ISubModuleNode navigationNode) {
			super(navigationNode);
		}

		@Override
		public void updateNavigationNodeMarkers() {
			super.updateNavigationNodeMarkers();
		}

	}

	private static class CompositeRidget extends AbstractCompositeRidget {
	}

	private interface IMockRidget extends IRidget {

	}

	/**
	 * Mock implementation of ridget.
	 */
	public static class MockRidget implements IMockRidget {

		public Object getUIControl() {
			return null;
		}

		public void setUIControl(Object uiControl) {
		}

		public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		}

		public void addPropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener) {
		}

		public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		}

		public void removePropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener) {
		}

		public boolean isVisible() {
			return false;
		}

		public void setVisible(boolean visible) {
		}

		public boolean isEnabled() {
			return false;
		}

		public void setEnabled(boolean enabled) {
		}

		public void addFocusListener(IFocusListener listener) {
		}

		public void removeFocusListener(IFocusListener listener) {
		}

		public void updateFromModel() {
		}

		public void requestFocus() {
		}

		public boolean hasFocus() {
			return false;
		}

		public boolean isFocusable() {
			return false;
		}

		public void setFocusable(boolean focusable) {
		}

		public String getToolTipText() {
			return null;
		}

		public void setToolTipText(String toolTipText) {
		}

		public boolean isBlocked() {
			return false;
		}

		public void setBlocked(boolean blocked) {
		}

		public String getID() {
			return null;
		}
	}

}
