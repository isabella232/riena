/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.demo.client.controllers;

import org.eclipse.riena.core.service.Service;
import org.eclipse.riena.demo.common.Customer;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.listener.SubModuleNodeListener;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.filter.IUIFilter;
import org.eclipse.riena.ui.filter.IUIFilterProvider;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IDateTextRidget;
import org.eclipse.riena.ui.ridgets.IDecimalTextRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;

/**
 *
 */
public class CustomerOverviewController extends SubModuleController {

	private final IUIFilter assistent = Service.get(IUIFilterProvider.class).provideFilter("demo.assistent") //$NON-NLS-1$
			.getFilter();

	private final IUIFilter mandatory = Service.get(IUIFilterProvider.class).provideFilter("demo.mandatory") //$NON-NLS-1$
			.getFilter();

	private Customer customer;

	@Override
	public void configureRidgets() {

		// store context in parent node (Module) so that all Submodule can access it
		customer = (Customer) getNavigationNode().getParent().getContext("demo.customer"); //$NON-NLS-1$

		if (customer == null) { // we came here from a navigation from the search
			final NavigationArgument navigationArgument = getNavigationNode().getNavigationArgument();
			if (navigationArgument != null) {
				customer = (Customer) navigationArgument.getParameter();
			} else {
				customer = new Customer();
				this.getNavigationNode().getParent().setLabel("new Customer"); //$NON-NLS-1$
			}
			getNavigationNode().getParent().setContext("demo.customer", customer); //$NON-NLS-1$
		}

		final ITextRidget firstName = getRidget("firstname"); //$NON-NLS-1$
		firstName.bindToModel(customer, "firstName"); //$NON-NLS-1$
		firstName.setMandatory(true);
		final ITextRidget lastName = getRidget("lastname"); //$NON-NLS-1$
		lastName.setMandatory(true);
		lastName.bindToModel(customer, "lastName"); //$NON-NLS-1$

		((ITextRidget) getRidget("zipcode")).bindToModel(customer.getAddress(), "zipCode"); //$NON-NLS-1$ //$NON-NLS-2$
		((ITextRidget) getRidget("street")).bindToModel(customer.getAddress(), "street"); //$NON-NLS-1$ //$NON-NLS-2$
		((ITextRidget) getRidget("city")).bindToModel(customer.getAddress(), "city"); //$NON-NLS-1$ //$NON-NLS-2$
		((ITextRidget) getRidget("emailaddress")).bindToModel(customer, "emailAddress"); //$NON-NLS-1$ //$NON-NLS-2$
		((IDateTextRidget) getRidget("birthdate")).bindToModel(customer, "birthDate"); //$NON-NLS-1$ //$NON-NLS-2$
		((ITextRidget) getRidget("salary")).bindToModel(customer, "salary"); //$NON-NLS-1$ //$NON-NLS-2$
		((IDecimalTextRidget) getRidget("salary")).setPrecision(2); //$NON-NLS-1$

		updateAllRidgetsFromModel();

		((IActionRidget) getRidget("openEmailsAction")).addListener(new IActionListener() { //$NON-NLS-1$
					public void callback() {
						final String emailAddress = customer.getEmailAddress();
						getNavigationNode().jump(
								new NavigationNodeId("riena.demo.client.customermailfolders.mails", emailAddress), //$NON-NLS-1$
								new NavigationArgument(customer));

					}
				});

		((IActionRidget) getRidget("assistent")).addListener(new IActionListener() { //$NON-NLS-1$

					public void callback() {
						final IApplicationNode applNode = getNavigationNode().getParentOfType(IApplicationNode.class);
						if (((IToggleButtonRidget) getRidget("assistent")).isSelected()) { //$NON-NLS-1$
							applNode.addFilter(assistent);
						} else {
							applNode.removeFilter(assistent);
						}
					}
				});

		((IActionRidget) getRidget("mandatory")).addListener(new IActionListener() { //$NON-NLS-1$

					public void callback() {
						final IApplicationNode applNode = getNavigationNode().getParentOfType(IApplicationNode.class);
						if (((IToggleButtonRidget) getRidget("mandatory")).isSelected()) { //$NON-NLS-1$
							applNode.addFilter(mandatory);
						} else {
							applNode.removeFilter(mandatory);
						}
					}
				});

		getNavigationNode().addListener(new SubModuleNodeListener() {
			@Override
			public void activated(final ISubModuleNode source) {
				updateAllRidgetsFromModel();
			}
		});
	}
}
