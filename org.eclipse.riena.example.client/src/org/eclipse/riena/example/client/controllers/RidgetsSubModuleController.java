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
import org.eclipse.riena.navigation.ui.controllers.SubModuleNodeViewController;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;

/**
 * Controller of the sub module that shows a set of ridgets.
 */
public class RidgetsSubModuleController extends SubModuleNodeViewController {

	private IToggleButtonRidget toggleOne;
	private IToggleButtonRidget checkOne;
	private RidgetsModel model;

	public RidgetsSubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
		model = new RidgetsModel();
		model.setToggleOneSelected(true);
		model.setCheckOneSelected(true);
	}

	public IToggleButtonRidget getToggleOne() {
		return toggleOne;
	}

	public void setToggleOne(IToggleButtonRidget toggleOne) {
		this.toggleOne = toggleOne;
	}

	public IToggleButtonRidget getCheckOne() {
		return checkOne;
	}

	public void setCheckOne(IToggleButtonRidget checkOne) {
		this.checkOne = checkOne;
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.controllers.SubModuleNodeViewController#afterBind()
	 */
	@Override
	public void afterBind() {
		super.afterBind();
		initRidgets();
	}

	/**
	 * Binds and updates the ridgets.
	 */
	private void initRidgets() {
		toggleOne.setText("Toggle 1");
		if (toggleOne != null) {
			toggleOne.bindToModel(model, "toggleOneSelected"); //$NON-NLS-1$
			toggleOne.updateFromModel();
		}

		checkOne.setText("Check 1");
		if (checkOne != null) {
			checkOne.bindToModel(model, "checkOneSelected"); //$NON-NLS-1$
			checkOne.updateFromModel();
		}
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
