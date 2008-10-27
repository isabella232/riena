/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.example.client.model;

/**
 * Anrede des Kunden.
 * 
 * 
 */
public final class SuchArt extends AkteEnum {

	/**
	 *  
	 */
	public static final SuchArt PHONETISCH = new SuchArt("1", "phonetisch", 0);

	/**
	 *  
	 */
	public static final SuchArt WILDCARD = new SuchArt("2", "wildcard", 1);

	/**
	 *  
	 */
	public static final SuchArt EXAKT = new SuchArt("3", "exakt", 2);

	/**
	 * 
	 * @param pCode
	 * @param pValue
	 * @param sortOrder
	 */
	public SuchArt(String pCode, String pValue, int sortOrder) {
		super(pCode, pValue);
		setSortOrder(sortOrder);
	}

}