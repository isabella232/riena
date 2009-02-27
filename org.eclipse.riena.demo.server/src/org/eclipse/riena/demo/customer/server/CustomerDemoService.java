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
package org.eclipse.riena.demo.customer.server;

import org.eclipse.riena.demo.customer.common.CustomerRecordOverview;
import org.eclipse.riena.demo.customer.common.CustomerSearchBean;
import org.eclipse.riena.demo.customer.common.CustomerSearchResult;
import org.eclipse.riena.demo.customer.common.ICustomerDemoService;

/**
 *
 */
public class CustomerDemoService implements ICustomerDemoService {

	private ICustomerRepository repository;

	// private ObjectContainer oc;

	/**
	 * 
	 * @param repository
	 */
	public CustomerDemoService() {
	}

	public void bind(ICustomerRepository repository) {
		this.repository = repository;
	}

	public void unbind(ICustomerRepository repository) {
		this.repository = null;
	}

	/**
	 * 
	 * @see ICustomerDemoService.compeople.xxx.ki.akte.base.service.IPersonenService#suche(CustomerSearchBean.compeople.xxx.ki.akte.base.service.SuchPerson)
	 */
	public CustomerSearchResult suche(CustomerSearchBean personValueObject) {
		CustomerSearchResult ergebnis = new CustomerSearchResult();
		if (!hasValidSearchParam(personValueObject)) {
			ergebnis.setFehler(true);
		} else {
			CustomerRecordOverview[] suchergebnis = repository.suche(personValueObject);
			ergebnis.setErgebnis(suchergebnis);
			ergebnis.setErgebnismenge(suchergebnis.length);
		}
		return ergebnis;
	}

	/**
	 * @param personValueObject
	 * @return boolean
	 */
	private boolean hasValidSearchParam(CustomerSearchBean personValueObject) {
		return true;
	}

}