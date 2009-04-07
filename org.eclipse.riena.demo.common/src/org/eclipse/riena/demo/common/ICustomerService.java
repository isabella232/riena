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
package org.eclipse.riena.demo.common;

import java.util.List;

/**
 * service methods for the CustomerDemoService
 */
public interface ICustomerService {
	public static final String ID = ICustomerService.class.getName();
	public static final String WS_ID = "/CustomerServiceWS"; //$NON-NLS-1$

	/**
	 * 
	 * @param lastName TODO
	 * @return CustomerSearchResult
	 */
	List<Customer> search(String lastName);
	
	boolean store(Customer customer);

}