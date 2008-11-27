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

import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.ITextRidget;

/**
 *
 */
public class CustomerPersonalDataController extends SubModuleController {

	private Customer customer = new Customer(CustomerLoader.getFirstName(), CustomerLoader.getLastName());

	@Override
	public void configureRidgets() {

		((ITextRidget) getRidget("firstname")).bindToModel(customer, "firstName");
		((ITextRidget) getRidget("lastname")).bindToModel(customer, "lastName");

	}

	@Override
	public void afterBind() {
		// TODO Auto-generated method stub
		super.afterBind();
		((ITextRidget) getRidget("firstname")).updateFromModel();
		((ITextRidget) getRidget("lastname")).updateFromModel();
	}

	class Customer {

		String firstName;
		String lastName;

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
