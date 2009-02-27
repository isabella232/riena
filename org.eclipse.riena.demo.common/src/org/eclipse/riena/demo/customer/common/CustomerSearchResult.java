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
 * Bean that contains the search result from the CustomerDemoService (search)
 * 
 * 
 */
public class CustomerSearchResult {
	private CustomerRecordOverview[] ergebnis;
	private int ergebnismenge;
	private boolean fehler;

	/**
	 * @return boolean
	 */
	public boolean getFehler() {
		return fehler;
	}

	/**
	 * @param fehler
	 */
	public void setFehler(boolean fehler) {
		this.fehler = fehler;
	}

	/**
	 * @return Returns the ergebnis.
	 */
	public CustomerRecordOverview[] getErgebnis() {
		return ergebnis;
	}

	/**
	 * @param ergebnis
	 *            The ergebnis to set.
	 */
	public void setErgebnis(CustomerRecordOverview[] ergebnis) {
		this.ergebnis = ergebnis;
	}

	/**
	 * @return Returns the ergebnismenge.
	 */
	public int getErgebnismenge() {
		return ergebnismenge;
	}

	/**
	 * @param ergebnismenge
	 *            The ergebnismenge to set.
	 */
	public void setErgebnismenge(int ergebnismenge) {
		this.ergebnismenge = ergebnismenge;
	}
}
