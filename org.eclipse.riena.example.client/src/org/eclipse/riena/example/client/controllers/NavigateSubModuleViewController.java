/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2008 compeople AG                         *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.example.client.controllers;

import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.model.NavigationNodeId;
import org.eclipse.riena.navigation.ui.controllers.SubModuleNodeViewController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;
import org.eclipse.riena.ui.ridgets.uibinding.IInjectAllRidgetsAtOnce;

public class NavigateSubModuleViewController extends SubModuleNodeViewController implements IInjectAllRidgetsAtOnce {

	private ITextFieldRidget target;

	public NavigateSubModuleViewController(ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	public void configureRidgets() {

		target = (ITextFieldRidget) getRidget("target");

		IActionRidget navigate = (IActionRidget) getRidget("navigate");
		navigate.setText("Go");
		navigate.addListener(new NavigateListener());

		setDefaultButton(navigate);
	}

	private class NavigateListener implements IActionListener {

		/**
		 * @see org.eclipse.riena.ui.ridgets.IActionListener#callback()
		 */
		public void callback() {
			getNavigationNode().navigate(new NavigationNodeId(target.getText()));
		}

	}

}
