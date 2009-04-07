package org.eclipse.riena.demo.client.controllers;

import java.util.List;

import org.eclipse.riena.demo.common.Customer;

public class SearchResult {
	
	private List<Customer> customers;

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}

	public List<Customer> getCustomers() {
		return customers;
	}
	
	public int getHits() {
		if (customers==null) {
			return 0;
		}
		return customers.size();
	}

}
