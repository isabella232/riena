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
 * Abstrakte Implementation.
 * 
 */
public abstract class AbstractMessage implements IMessage {
	protected AbstractMessage() {
		super();
	}

	public MessageType getTyp() {
		return getMessageData().getTyp();
	}

	public String getAnsprache() {
		return getMessageData().getAnsprache();
	}

	public String getHinweis() {
		return getMessageData().getHinweis();
	}

	public String getInfoText() {
		return getMessageData().getInfoText();
	}

	public String getMeldungsText() {
		return getMessageData().getMeldungsText();
	}

	public String getMex() {
		return getMessageData().getMex();
	}

	public String getStatusZeile() {
		return getMessageData().getStatusZeile();
	}

	public String[] getButtons() {
		return getMessageData().getButtons();
	}

	public void setButtons(String[] buttons) {
		getMessageData().setButtons(buttons);
	}

	protected abstract MessageData getMessageData();
}
