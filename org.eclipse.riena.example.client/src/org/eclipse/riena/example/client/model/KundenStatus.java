/***************************************************************************************************
 * * Copyright (c) 2004 compeople AG * All rights reserved. The use of this program and the *
 * accompanying materials are subject to license terms. * *
 **************************************************************************************************/
package org.eclipse.riena.example.client.model;

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