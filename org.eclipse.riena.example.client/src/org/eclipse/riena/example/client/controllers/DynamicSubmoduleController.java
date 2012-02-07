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
package org.eclipse.riena.example.client.controllers;

import org.eclipse.riena.example.client.views.DynamicSubmoduleView;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ILabelRidget;

/**
 *
 */
public class DynamicSubmoduleController extends SubModuleController {
	ILabelRidget label;
	IActionRidget button;

	@Override
	public void configureRidgets() {
		super.configureRidgets();
		label = getRidget(DynamicSubmoduleView.BINDING_ID_LABEL);
		button = getRidget(DynamicSubmoduleView.BINDING_ID_BUTTON);
		label.setText("child" + getNavigationNode().getNodeId().getInstanceId()); //$NON-NLS-1$

		button.addListener(new IActionListener() {

			public void callback() {
				getNavigationNode().dispose();
			}
		});
	}
}