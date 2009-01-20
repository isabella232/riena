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
package org.eclipse.riena.demo.client.customer.controllers;

import org.eclipse.riena.demo.customer.common.CustomerRecordOverview;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.ITextRidget;

/**
 *
 */
public class CustomerPersonalDataController extends SubModuleController {

	private Customer customer;

	@Override
	public void configureRidgets() {

		CustomerRecordOverview customerRecord = (CustomerRecordOverview) getNavigationNode().getContext(
				NavigationArgument.CONTEXT_KEY_PARAMETER);
		customer = new Customer(customerRecord.getFirstName(), customerRecord.getLastName());

		((ITextRidget) getRidget("firstname")).bindToModel(customer, "firstName"); //$NON-NLS-1$ //$NON-NLS-2$
		((ITextRidget) getRidget("lastname")).bindToModel(customer, "lastName"); //$NON-NLS-1$ //$NON-NLS-2$

	}

	@Override
	public void afterBind() {
		super.afterBind();
		((ITextRidget) getRidget("firstname")).updateFromModel(); //$NON-NLS-1$
		((ITextRidget) getRidget("lastname")).updateFromModel(); //$NON-NLS-1$
	}

	static class Customer {

		private String firstName;
		private String lastName;

		Customer(String firstName, String lastName) {
			this.firstName = firstName;
			this.lastName = lastName;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

	}
}
