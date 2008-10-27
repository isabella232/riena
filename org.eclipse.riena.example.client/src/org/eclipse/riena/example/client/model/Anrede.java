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

import java.util.ArrayList;
import java.util.List;

/**
 * Anrede des Kunden.
 * 
 * 
 */
public final class Anrede extends AkteEnum {

	// KTY = 1

	/**
	 * @return List
	 */
	public static List getStandardAnreden() {
		List result = new ArrayList();
		result.addAll(AkteEnum.asSortedList(Anrede.class));
		result.remove(Anrede.EHELEUTE);
		return result;
	}

	/**
	 * @return List
	 */
	public static List getStandardMagazinAnreden() {
		List result = new ArrayList();
		result.addAll(AkteEnum.asSortedList(Anrede.class));

		return result;
	}

	/**
	 *
	 */
	public static final Anrede UNBEKANNT = new Anrede("-1", "", 0);

	/**
	 *
	 */
	public static final Anrede HERR = new Anrede("1", "Herr", 1);

	/**
	 *
	 */
	public static final Anrede FRAU = new Anrede("2", "Frau", 2);

	/**
	 *
	 */
	public static final Anrede EHELEUTE = new Anrede("4", "Eheleute", 3);

	/**
	 *
	 */
	public static final Anrede FIRMA = new Anrede("5", "Firma", 4);

	/**
	 * 
	 * @param pCode
	 * @param pValue
	 * @param sortOrder
	 */
	public Anrede(String pCode, String pValue, int sortOrder) {
		super(pCode, pValue);
		setSortOrder(sortOrder);
	}
}