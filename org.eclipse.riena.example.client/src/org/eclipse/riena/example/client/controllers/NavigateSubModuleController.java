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
package org.eclipse.riena.example.client.controllers;

import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.internal.example.client.beans.PersonModificationBean;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;

/*
 * implements
 * IInjectAllRidgets
 */
public class NavigateSubModuleController extends SubModuleController {

	public NavigateSubModuleController() {
		this(null);
	}

	public NavigateSubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	@Override
	public void configureRidgets() {

		IActionRidget comboAndList = getRidget(IActionRidget.class, "comboAndList"); //$NON-NLS-1$
		comboAndList.setText("Combo and List (SubApplication 1)"); //$NON-NLS-1$
		comboAndList.addListener(new ComboAndListListener());

		IActionRidget tableTextAndTree = getRidget(IActionRidget.class, "tableTextAndTree"); //$NON-NLS-1$
		tableTextAndTree.setText("Table, Text and Tree (SubApplication 2)"); //$NON-NLS-1$
		tableTextAndTree.addListener(new TableTextAndTreeListener());

		final PersonModificationBean bean = new PersonModificationBean();
		bean.setPerson(new Person("Doe", "Jane")); //$NON-NLS-1$ //$NON-NLS-2$
		IActionRidget navigateRidget = getRidget(IActionRidget.class, "btnNavigateToRidget"); //$NON-NLS-1$
		navigateRidget.addListener(new IActionListener() {
			public void callback() {
				getNavigationNode().navigate(new NavigationNodeId("org.eclipse.riena.example.combo"), //$NON-NLS-1$
						new NavigationArgument(bean, "textFirst")); //$NON-NLS-1$

			}
		});

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
