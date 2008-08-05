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
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.uibinding.IInjectAllRidgetsAtOnce;

public class NavigateSubModuleController extends SubModuleController implements IInjectAllRidgetsAtOnce {

	public NavigateSubModuleController() {
		this(null);
	}

	public NavigateSubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	public void configureRidgets() {

		IActionRidget comboAndList = (IActionRidget) getRidget("comboAndList"); //$NON-NLS-1$
		comboAndList.setText("Combo and List (SubApplication 1)"); //$NON-NLS-1$
		comboAndList.addListener(new ComboAndListListener());

		IActionRidget tableTextAndTree = (IActionRidget) getRidget("tableTextAndTree"); //$NON-NLS-1$
		tableTextAndTree.setText("Table, Text and Tree (SubApplication 2)"); //$NON-NLS-1$
		tableTextAndTree.addListener(new TableTextAndTreeListener());
	}

	private class ComboAndListListener implements IActionListener {

		/**
		 * @see org.eclipse.riena.ui.ridgets.IActionListener#callback()
		 */
		public void callback() {
			getNavigationNode().navigate(new NavigationNodeId("org.eclipse.riena.example.navigate.comboAndList")); //$NON-NLS-1$
		}

	}

	private class TableTextAndTreeListener implements IActionListener {

		/**
		 * @see org.eclipse.riena.ui.ridgets.IActionListener#callback()
		 */
		public void callback() {
			getNavigationNode().navigate(new NavigationNodeId("org.eclipse.riena.example.navigate.tableTextAndTree")); //$NON-NLS-1$
		}

	}

}
