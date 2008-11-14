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
 * Interface of a message
 * 
 */
public interface IMessage {
	/**
	 * Gibt den Typ der Meldung zurück.
	 * 
	 * @return MessageType
	 */
	MessageType getTyp();

	/**
	 * Gibt die Nummer der Meldung zurück.
	 * 
	 * @return String
	 */
	String getMex();

	/**
	 * Gibt den Meldungstext zurück.
	 * 
	 * @return String
	 */
	String getMeldungsText();

	/**
	 * Gibt den Infotext zurück.
	 * 
	 * @return String
	 */
	String getInfoText();

	/**
	 * Gibt den Hinweistext zurück.
	 * 
	 * @return String
	 */
	String getHinweis();

	/**
	 * Gibt den Text für die Ansprache zurück.
	 * 
	 * @return String
	 */
	String getAnsprache();

	/**
	 * Gibt den Text für die Statuszeile zurück.
	 * 
	 * @return String
	 */
	String getStatusZeile();

	/**
	 * Gibt die Texte für die Schaltflächen zurück.
	 * 
	 * @return String
	 */
	String[] getButtons();

	/**
	 * Setzt die Texte für die Schaltflächen.
	 * 
	 * @param buttons
	 */
	void setButtons(String[] buttons);
}
