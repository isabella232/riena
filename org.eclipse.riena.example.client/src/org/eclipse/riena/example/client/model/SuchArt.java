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