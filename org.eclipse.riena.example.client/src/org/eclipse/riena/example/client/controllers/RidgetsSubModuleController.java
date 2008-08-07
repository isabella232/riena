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
package org.eclipse.riena.example.client.controllers;

import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;

/**
 * Controller of the sub module that shows a set of ridgets.
 */
public class RidgetsSubModuleController extends SubModuleController {

	private final static String PLUGIN_ID = "org.eclipse.riena.example.client:"; //$NON-NLS-1$
	private final static String ICON_SAMPLE = PLUGIN_ID + "/icons/sample.gif"; //$NON-NLS-1$
	private final static String ICON_RED = PLUGIN_ID + "/icons/ledred.png"; //$NON-NLS-1$

	private RidgetsModel model;

	public RidgetsSubModuleController() {
		this(null);
	}

	public RidgetsSubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
		model = new RidgetsModel();
		model.setToggleOneSelected(true);
		model.setCheckOneSelected(true);
	}

	public IActionRidget getButtonWithImage() {
		return (IActionRidget) getRidget("buttonWithImage"); //$NON-NLS-1$
	}

	public IToggleButtonRidget getToggleOne() {
		return (IToggleButtonRidget) getRidget("toggleOne"); //$NON-NLS-1$
	}

	public IToggleButtonRidget getCheckOne() {
		return (IToggleButtonRidget) getRidget("checkOne"); //$NON-NLS-1$
	}

	/**
	 * Binds and updates the ridgets.
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#configureRidgets()
	 */
	public void configureRidgets() {
		getToggleOne().setText("&Toggle 1"); //$NON-NLS-1$
		getToggleOne().setIcon(ICON_SAMPLE);
		getToggleOne().bindToModel(model, "toggleOneSelected"); //$NON-NLS-1$
		getToggleOne().updateFromModel();

		getCheckOne().setText("C&heck 1"); //$NON-NLS-1$
		getCheckOne().bindToModel(model, "checkOneSelected"); //$NON-NLS-1$
		getCheckOne().updateFromModel();

		getButtonWithImage().setText("Text"); //$NON-NLS-1$
		getButtonWithImage().setIcon(ICON_SAMPLE);
		getButtonWithImage().addListener(new IActionListener() {
			public void callback() {
				getButtonWithImage().setIcon(ICON_RED);
			}
		});
	}

	/**
	 * The model of this sub module controller.
	 */
	private class RidgetsModel {

		private boolean toggleOneSelected;
		private boolean checkOneSelected;

		public boolean isToggleOneSelected() {
			return toggleOneSelected;
		}

		public void setToggleOneSelected(boolean toggleOneSelected) {
			this.toggleOneSelected = toggleOneSelected;
		}

		public boolean isCheckOneSelected() {
			return checkOneSelected;
		}

		public void setCheckOneSelected(boolean checkOneSelected) {
			this.checkOneSelected = checkOneSelected;
		}

	}

}
