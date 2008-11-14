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
package org.eclipse.riena.demo.customer.client.model;

/**
 * KundenStatus
 * 
 * 
 */
public final class KundenStatus extends AkteEnum {
	// KTY 10

	/**
	 *
	 */
	public static final KundenStatus UNBEKANNT = new KundenStatus("-1", "", "", 0);

	/**
	 *
	 */
	public static final KundenStatus ALTKUNDE = new KundenStatus("1", "Altkunde", "A", 3);

	/**
	 *
	 */
	public static final KundenStatus INTERESSENT = new KundenStatus("2", "Interessent", "I", 1);

	/**
	 *
	 */
	public static final KundenStatus KUNDE = new KundenStatus("3", "Kunde", "K", 2);

	/**
	 * 
	 * @param pCode
	 * @param pValue
	 * @param altCode
	 * @param sortOrder
	 */
	public KundenStatus(String pCode, String pValue, String altCode, int sortOrder) {
		super(pCode, pValue, altCode);
		setSortOrder(sortOrder);
	}
}