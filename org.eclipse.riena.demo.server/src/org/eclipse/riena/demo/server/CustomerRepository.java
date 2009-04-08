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
package org.eclipse.riena.demo.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.riena.demo.common.Customer;

/**
 * Implementation for accessing Customer Records
 */
public class CustomerRepository implements ICustomerRepository {

	private List<Customer> customers = new ArrayList<Customer>();
	
	public CustomerRepository() {
		System.out.println("repository started");
		init();
	}

	public List<Customer> search(String lastName) {
		return customers;
	}
	
	public void store(Customer customer) {
		customers.add(customer);
	}

	private void init() {
		try {
			Customer crv = new Customer();
			// 1.
			crv.setLastName("Mundl");
			crv.setFirstName("Josef");
			crv.setBirthDate(new Date(Date.parse("01/01/1950")));
			crv.getAddress().setStreet("Musterstr.1");
			crv.getAddress().setZipCode("12345");
			crv.getAddress().setCity("Musterstadt");
			customers.add(crv);
			// 2.
			crv = new Customer();
			crv.setLastName("Muster");
			crv.setFirstName("Robert");
			crv.setBirthDate(new Date(Date.parse("01/04/1955")));
			crv.getAddress().setStreet("Willibaldgasse");
			crv.getAddress().setZipCode("12366");
			crv.getAddress().setCity("Musterhaus");
			customers.add(crv);
			// 3.
			crv = new Customer();
			crv.setLastName("Muster-Maier");
			crv.setFirstName("Trulli");
			crv.setBirthDate(new Date(Date.parse("21/06/1964")));
			crv.getAddress().setStreet("Willibaldgasse");
			crv.getAddress().setZipCode("12345");
			crv.getAddress().setCity("Musterstadt");
			customers.add(crv);
			// 4.
			crv = new Customer();
			crv.setLastName("Mustermann");
			crv.setFirstName("Elfriede");
			crv.setBirthDate(new Date(Date.parse("01/04/1955")));
			crv.getAddress().setStreet("Musterstr.1");
			crv.getAddress().setZipCode("12345");
			crv.getAddress().setCity("Musterstadt");
			customers.add(crv);
			// 5.
			crv = new Customer();
			crv.setLastName("Mustermann");
			crv.setFirstName("Ingo");
			crv.setBirthDate(new Date(Date.parse("01/01/1950")));
			crv.getAddress().setStreet("Musterstr.1");
			crv.getAddress().setZipCode("12345");
			crv.getAddress().setCity("Musterstadt");
			customers.add(crv);

		} catch (IllegalArgumentException p) {
			System.out.println(p.getMessage());
		}
	}

}
