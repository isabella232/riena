package org.eclipse.riena.example.client.model;

/**
 * Abstrakte Implementierung. Nach aussen entspricht die API dem Interface
 * IMessage. Die Werte werden einem MessageData-Objekt entnommen, das eine ein
 * konkrete Implementierung zurückgibt.
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
