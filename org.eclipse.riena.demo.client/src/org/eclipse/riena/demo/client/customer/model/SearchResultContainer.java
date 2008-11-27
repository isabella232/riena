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
package org.eclipse.riena.demo.client.customer.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.riena.demo.customer.common.CustomerRecordOverview;
import org.eclipse.riena.ui.ridgets.util.beans.AbstractBean;

/**
 * SearchResultContainer
 */
public class SearchResultContainer extends AbstractBean {

	private List<CustomerRecordOverview> customerList = new ArrayList<CustomerRecordOverview>();

	private List<CustomerRecordOverview> selectionList = new ArrayList<CustomerRecordOverview>();

	/**
	 * @return Returns the customerOverview.
	 */
	public List<CustomerRecordOverview> getCustomerList() {
		return customerList;
	}

	/**
	 * @param customerList
	 *            The customerRecordOverview to set.
	 */
	public void setCustomerList(List<CustomerRecordOverview> kunden) {
		this.customerList = kunden;
	}

}