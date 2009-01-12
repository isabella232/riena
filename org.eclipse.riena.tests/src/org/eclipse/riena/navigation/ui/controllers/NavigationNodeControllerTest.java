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
package org.eclipse.riena.navigation.ui.controllers;

import java.beans.PropertyChangeSupport;
import java.util.Collection;

import junit.framework.TestCase;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.ui.ridgets.swt.LabelRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.TextRidget;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.model.NavigationProcessor;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.tests.collect.NonUITestCase;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.HiddenMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.core.marker.OutputMarker;
import org.eclipse.riena.ui.ridgets.AbstractCompositeRidget;
import org.eclipse.swt.widgets.Display;

/**
 * Tests of the class {@link NavigationNodeController}.
 */
@NonUITestCase
public class NavigationNodeControllerTest extends TestCase {

	private MyNavigationNodeContoller controller;
	private SubModuleNode node;

	@Override
	protected void setUp() throws Exception {

		Display display = Display.getDefault();
		Realm realm = SWTObservables.getRealm(display);
		assertNotNull(realm);
		ReflectionUtils.invokeHidden(realm, "setDefault", realm);

		node = new SubModuleNode();
		node.setNavigationProcessor(new NavigationProcessor());
		controller = new MyNavigationNodeContoller(node);
	}

	@Override
	protected void tearDown() throws Exception {
		controller = null;
		node = null;
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
		assertEquals(1, support.getPropertyChangeListeners().length);

	}

	/**
	 * Tests the method {@code updateNavigationNodeMarkers()}.
	 */
	public void testUpdateNavigationNodeMarkers() {

		node.addMarker(new HiddenMarker());
		controller.updateNavigationNodeMarkers();
		assertTrue(node.getMarkers().isEmpty());

		node.addMarker(new HiddenMarker());
		TextRidget ridget = new TextRidget();
		ridget.addMarker(new ErrorMarker());
		controller.addRidget("4711", ridget);
		controller.updateNavigationNodeMarkers();
		assertFalse(node.getMarkers().isEmpty());
		assertTrue(node.getMarkersOfType(HiddenMarker.class).isEmpty());
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

		LabelRidget ridget = new LabelRidget();
		controller.addRidget("4711", ridget);
		LabelRidget ridget2 = new LabelRidget();
		controller.addRidget("0815", ridget2);

		Collection<IMarker> markers = ReflectionUtils.invokeHidden(controller, "getRidgetMarkers", null);
		assertNotNull(markers);

		ErrorMarker errorMarker = new ErrorMarker();
		ridget.addMarker(errorMarker);
		OutputMarker outputMarker = new OutputMarker();
		ridget2.addMarker(outputMarker);
		markers = ReflectionUtils.invokeHidden(controller, "getRidgetMarkers", null);
		assertNotNull(markers);
		assertEquals(2, markers.size());
		assertTrue(markers.contains(errorMarker));
		assertTrue(markers.contains(outputMarker));

		CompositeRidget compositeRidget = new CompositeRidget();
		LabelRidget ridget3 = new LabelRidget();
		compositeRidget.addRidget("label3", ridget3);
		controller.addRidget("comp", compositeRidget);
		MandatoryMarker mandatoryMarker = new MandatoryMarker();
		ridget3.addMarker(mandatoryMarker);
		markers = ReflectionUtils.invokeHidden(controller, "getRidgetMarkers", null);
		assertNotNull(markers);
		assertEquals(3, markers.size());
		assertTrue(markers.contains(mandatoryMarker));

	}

	private class MyNavigationNodeContoller extends SubModuleController {

		public MyNavigationNodeContoller(ISubModuleNode navigationNode) {
			super(navigationNode);
		}

		@Override
		public void updateNavigationNodeMarkers() {
			super.updateNavigationNodeMarkers();
		}

	}

	private class CompositeRidget extends AbstractCompositeRidget {
	}

}
