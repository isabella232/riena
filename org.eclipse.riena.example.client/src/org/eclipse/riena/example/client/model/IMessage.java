package org.eclipse.riena.example.client.model;

/**
 * Das Interface einer Message.
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
