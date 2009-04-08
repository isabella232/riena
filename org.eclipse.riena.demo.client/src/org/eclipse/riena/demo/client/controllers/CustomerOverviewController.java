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
package org.eclipse.riena.demo.client.controllers;

import org.eclipse.riena.demo.common.Customer;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.listener.SubModuleNodeListener;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.filter.IUIFilter;
import org.eclipse.riena.ui.filter.impl.UIFilterProviderAccessor;
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

	private IUIFilter assistent = UIFilterProviderAccessor.getFilterProvider().provideFilter("demo.assistent")
			.getFilter();

	private IUIFilter mandatory = UIFilterProviderAccessor.getFilterProvider().provideFilter("demo.mandatory")
			.getFilter();

	@Override
	public void configureRidgets() {

		Customer customer = (Customer) getNavigationNode().getParent().getContext(
				NavigationArgument.CONTEXT_KEY_PARAMETER);
		if (customer == null) {
			customer = new Customer();
			getNavigationNode().getParent().setLabel("new Customer");
			getNavigationNode().getParent().setContext(NavigationArgument.CONTEXT_KEY_PARAMETER, customer);
		}

		ITextRidget firstName = (ITextRidget) getRidget("firstname");
		firstName.bindToModel(customer, "firstName");
		firstName.setMandatory(true);
		ITextRidget lastName = (ITextRidget) getRidget("lastname");
		lastName.setMandatory(true);
		lastName.bindToModel(customer, "lastName");

		((ITextRidget) getRidget("zipcode")).bindToModel(customer.getAddress(), "zipCode");
		((ITextRidget) getRidget("street")).bindToModel(customer.getAddress(), "street");
		((ITextRidget) getRidget("city")).bindToModel(customer.getAddress(), "city");
		((IDateTextRidget) getRidget("birthdate")).bindToModel(customer, "birthDate");
		((ITextRidget) getRidget("salary")).bindToModel(customer, "salary");
		((IDecimalTextRidget) getRidget("salary")).setPrecision(2);

		updateAllRidgetsFromModel();

		((IActionRidget) getRidget("assistent")).addListener(new IActionListener() {

			public void callback() {
				IApplicationNode applNode = getNavigationNode().getParentOfType(IApplicationNode.class);
				if (((IToggleButtonRidget) getRidget("assistent")).isSelected()) {
					applNode.addFilter(assistent);
				} else {
					applNode.removeFilter(assistent);
				}
			}
		});

		((IActionRidget) getRidget("mandatory")).addListener(new IActionListener() {

			public void callback() {
				IApplicationNode applNode = getNavigationNode().getParentOfType(IApplicationNode.class);
				if (((IToggleButtonRidget) getRidget("mandatory")).isSelected()) {
					applNode.addFilter(mandatory);
				} else {
					applNode.removeFilter(mandatory);
				}
			}
		});

		getNavigationNode().addListener(new SubModuleNodeListener() {
			public void activated(ISubModuleNode source) {
				updateAllRidgetsFromModel();
			}
		});
	}

}
