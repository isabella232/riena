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
 * Die Daten einer Message. Der Infotext ist parametrisierbar.<br>
 * Entweder durch {0}, {1}, ... oder durch benamte Platzhalter $Name, $Vorname
 */
public class MessageData implements Cloneable {
	private String mex;
	private MessageType typ;
	private String meldungsText;
	private String infoText;
	private String hinweis;
	private String ansprache;
	private String statusZeile;
	private String[] buttons;

	/**
	 * @param mex
	 * @param messageType
	 * @param infoText
	 * @param hinweis
	 * @param ansprache
	 * @param buttons
	 */
	public MessageData(String mex, MessageType messageType, String infoText, String hinweis, String ansprache,
			String[] buttons) {
		this(mex, messageType, null, infoText, hinweis, ansprache, buttons);
	}

	/**
	 * @param mex
	 * @param messageType
	 * @param meldungsText
	 * @param infoText
	 * @param hinweis
	 * @param ansprache
	 * @param buttons
	 */
	public MessageData(String mex, MessageType messageType, String meldungsText, String infoText, String hinweis,
			String ansprache, String[] buttons) {
		super();

		this.mex = mex;
		this.typ = messageType;
		this.meldungsText = meldungsText;
		this.infoText = infoText;
		this.hinweis = hinweis;
		this.ansprache = ansprache;
		this.buttons = buttons;
	}

	/**
	 * @param mex
	 * @param meldungsText
	 * @param statusZeile
	 */
	public MessageData(String mex, String meldungsText, String statusZeile) {
		super();

		this.mex = mex;
		this.meldungsText = meldungsText;
		this.statusZeile = statusZeile;
	}

	public Object clone() {
		Object clone = null;

		try {
			clone = super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}

		return clone;
	}

	/**
	 * @return Returns the ansprache.
	 */
	public String getAnsprache() {
		return ansprache;
	}

	/**
	 * @param buttons
	 *            The buttons to set.
	 */
	public void setButtons(String[] buttons) {
		this.buttons = buttons;
	}

	/**
	 * @return Returns the buttons.
	 */
	public String[] getButtons() {
		return buttons;
	}

	/**
	 * @return Returns the hinweis.
	 */
	public String getHinweis() {
		return hinweis;
	}

	/**
	 * @return Returns the infoText.
	 */
	public String getInfoText() {
		return infoText;
	}

	/**
	 * @return Returns the meldungsText.
	 */
	public String getMeldungsText() {
		return meldungsText;
	}

	/**
	 * @param meldungsText
	 *            The meldungsText to set.
	 */
	public void setMeldungsText(String meldungsText) {
		this.meldungsText = meldungsText;
	}

	/**
	 * @return Returns the messageType.
	 */
	public MessageType getTyp() {
		return typ;
	}

	/**
	 * @return Returns the mex.
	 */
	public String getMex() {
		return mex;
	}

	/**
	 * @return Returns the statusZeile.
	 */
	public String getStatusZeile() {
		return statusZeile;
	}

	/**
	 * @param statusZeile
	 *            The statusZeile to set.
	 */
	void setStatusZeile(String statusZeile) {
		this.statusZeile = statusZeile;
	}
}
