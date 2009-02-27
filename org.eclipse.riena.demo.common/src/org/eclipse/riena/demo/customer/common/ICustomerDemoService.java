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
package org.eclipse.riena.demo.customer.common;

/**
 * service methods for the CustomerDemoService
 */
public interface ICustomerDemoService {
	public static final String ID = ICustomerDemoService.class.getName();
	public static final String WS_ID = "/CustomerDemoServiceWS"; //$NON-NLS-1$

	/**
	 * 
	 * @param personValueObject
	 * @return CustomerSearchResult
	 */
	CustomerSearchResult suche(CustomerSearchBean personValueObject);

}