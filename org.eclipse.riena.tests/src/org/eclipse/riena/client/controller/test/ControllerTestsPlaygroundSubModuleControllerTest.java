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
package org.eclipse.riena.client.controller.test;

import org.eclipse.riena.example.client.controllers.ControllerTestsPlaygroundSubModuleController;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.ui.swt.controllers.AbstractSubModuleControllerTest;
import org.eclipse.riena.ui.ridgets.ISpinnerRidget;
import org.eclipse.riena.ui.ridgets.ITraverseRidget;

/**
 * Test for most of the existing ridgets.
 */
@NonUITestCase
public class ControllerTestsPlaygroundSubModuleControllerTest extends
		AbstractSubModuleControllerTest<ControllerTestsPlaygroundSubModuleController> {

	@Override
	protected ControllerTestsPlaygroundSubModuleController createController(ISubModuleNode node) {
		ControllerTestsPlaygroundSubModuleController newInst = new ControllerTestsPlaygroundSubModuleController();
		node.setNodeId(new NavigationNodeId("org.eclipse.riena.example.marker"));
		newInst.setNavigationNode(node);
		return newInst;
	}

	public void testScaleSpinner() {
		ITraverseRidget scale = getController().getRidget(ITraverseRidget.class, "celsiusScale");
		ISpinnerRidget fahrenheitSpinner = getController().getRidget(ISpinnerRidget.class, "fahrenheitSpinner");

		assertEquals(0, scale.getValue());
		assertEquals(32, fahrenheitSpinner.getValue());

		scale.setValue(5);
		scale.triggerListener();
		assertEquals(41, fahrenheitSpinner.getValue());

		fahrenheitSpinner.setValue(100);
		fahrenheitSpinner.triggerListener();
		assertEquals(38, scale.getValue());
	}

	public void testTable() {
		// TODO work in progress
	}

	// TODO MasterDetails has to be refactored to work with the controller tests
	public void testMasterDetails() {
		//		IMasterDetailsRidget master = getController().getRidget(IMasterDetailsRidget.class, "master");
	}

}
