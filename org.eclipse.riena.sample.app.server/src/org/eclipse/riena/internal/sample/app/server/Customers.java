/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.sample.app.server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.riena.sample.app.common.model.Address;
import org.eclipse.riena.sample.app.common.model.BankData;
import org.eclipse.riena.sample.app.common.model.Birth;
import org.eclipse.riena.sample.app.common.model.Customer;
import org.eclipse.riena.sample.app.common.model.CustomersPermission;
import org.eclipse.riena.sample.app.common.model.ICustomerSearch;
import org.eclipse.riena.sample.app.common.model.ICustomers;
import org.eclipse.riena.sample.app.common.model.Offer;

/**
 * Customers Service Class that is exposed as Webservice. It implements
 * ICustomers (the customer maintaince interface) and ICustomerSearch (the
 * interface for search on the customer data)
 */
public class Customers implements ICustomers, ICustomerSearch {

	private Map<Integer, Customer> customers;
	private Map<Integer, Set<Offer>> offers;
	private int nextUniqueCustomerNumber;

	public Customers() {

		customers = new HashMap<Integer, Customer>();
		offers = new HashMap<Integer, Set<Offer>>();
		nextUniqueCustomerNumber = 1;

		initializeCustomers();
		initializeOffers();
	}

	/**
	 * @see org.eclipse.riena.sample.app.common.model.ICustomers#getNextUniqueCustomerNumber()
	 */
	public Integer getNextUniqueCustomerNumber() {

		return nextUniqueCustomerNumber++;
	}

	/**
	 * @see org.eclipse.riena.sample.app.common.model.ICustomers#store(org.eclipse.riena.sample.app.common.model.Customer)
	 */
	public void store(Customer customer) {
		SecurityManager sm = System.getSecurityManager();
		if (sm != null) {
			sm.checkPermission(new CustomersPermission("riena.sample", "store"));
		}
		storeInternal(customer);
	}

	/**
	 * This method is also used internally to store customers. No security is
	 * checked.
	 * 
	 * @param customer
	 */
	private void storeInternal(Customer customer) {
		customer.setId(customer.getCustomerNumber());
		customers.put(customer.getCustomerNumber(), customer);

	}

	/**
	 * @see org.eclipse.riena.sample.app.common.model.ICustomerSearch#findCustomer(org.eclipse.riena.sample.app.common.model.Customer)
	 */
	public Customer[] findCustomer(Customer searchedCustomer) {
		SecurityManager sm = System.getSecurityManager();
		if (sm != null) {
			sm.checkPermission(new CustomersPermission("riena.sample", "find"));
		}

		List<Customer> l = new ArrayList<Customer>();

		for (Customer c : customers.values()) {
			if (isIdentical(c, searchedCustomer)) {
				l.add(c);
			}
		}

		return l.toArray(new Customer[l.size()]);
	}

	/**
	 * @see org.eclipse.riena.sample.app.common.model.ICustomers#getOffers(java.lang.Integer)
	 */
	public Offer[] getOffers(Integer customerNumber) {

		Set<Offer> customerOffers = offers.get(customerNumber);
		if (customerOffers == null) {
			return new Offer[0];
		} else {
			return customerOffers.toArray(new Offer[customerOffers.size()]);
		}
	}

	private boolean isIdentical(Customer customer, Customer searchedCustomer) {

		if (searchedCustomer.getCustomerNumber() != null
				&& !searchedCustomer.getCustomerNumber().equals(customer.getCustomerNumber())) {
			return false;
		}

		if (!contains(customer.getLastName(), searchedCustomer.getLastName())) {
			return false;
		}

		if (!contains(customer.getFirstName(), searchedCustomer.getFirstName())) {
			return false;
		}

		return true;
	}

	private boolean contains(String original, String other) {

		if (other == null || other.equals("")) {
			return true;
		}

		return original.toUpperCase().contains(other.toUpperCase());
	}

	private void initializeCustomers() {

		Customer customer = new Customer();
		customer.setFirstName("Han");
		customer.setLastName("Solo");
		Address address = new Address();
		address.setCity("Frankfurt am Main");
		address.setStreet("Am Main 233");
		address.setZipCode("61236");
		address.setCountry("Germany");
		customer.setAddress(address);

		customer.setBirth(new Birth());
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		try {
			customer.getBirth().setBirthDay(format.parse("01.04.1962"));
		} catch (ParseException e) {
			// TODO Throw exception
			e.printStackTrace();
		}
		customer.getBirth().setBirthPlace("Frankfurt");
		customer.setBankData(new ArrayList<BankData>());
		initializeCustomerNumber(customer);

		customer = new Customer();
		customer.setFirstName("Luke");
		customer.setLastName("Skywalker");
		address = new Address();
		address.setCity("Washington");
		address.setStreet("Any Road 845");
		address.setZipCode("98123898");
		address.setCountry("USA");
		customer.setAddress(address);
		customer.setBirth(new Birth());
		try {
			customer.getBirth().setBirthDay(format.parse("01.04.1963"));
		} catch (ParseException e) {
			// TODO Throw exception
			e.printStackTrace();
		}
		customer.getBirth().setBirthPlace("Frankfurt");
		customer.setBankData(new ArrayList<BankData>());
		initializeCustomerNumber(customer);

		customer = new Customer();
		customer.setFirstName("Frodo");
		customer.setLastName("Baggins");
		address = new Address();
		address.setCity("Hanau");
		address.setStreet("Grüner Weg 3");
		address.setZipCode("62342");
		address.setCountry("Germany");
		customer.setAddress(address);
		customer.setBirth(new Birth());
		try {
			customer.getBirth().setBirthDay(format.parse("01.04.1964"));
		} catch (ParseException e) {
			// TODO Throw exception
			e.printStackTrace();
		}
		customer.getBirth().setBirthPlace("Frankfurt");
		customer.setBankData(new ArrayList<BankData>());
		initializeCustomerNumber(customer);
	}

	private void initializeOffers() {

		Offer offer = new Offer(1, 1);
		addOffer(offer);
		offer = new Offer(1, 2);
		addOffer(offer);
	}

	private void addOffer(Offer offer) {

		Set<Offer> customerOffers = offers.get(offer.getCustomerNumber());
		if (customerOffers == null) {
			customerOffers = new HashSet<Offer>();
			offers.put(offer.getCustomerNumber(), customerOffers);
		}
		customerOffers.add(offer);
	}

	private void initializeCustomerNumber(Customer customer) {

		customer.setCustomerNumber(getNextUniqueCustomerNumber());
		storeInternal(customer);
	}
}
