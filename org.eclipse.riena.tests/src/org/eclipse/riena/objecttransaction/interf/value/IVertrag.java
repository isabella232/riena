package org.eclipse.riena.objecttransaction.interf.value;

/**
 * TODO Fehlender Klassen-Kommentar
 * 
 * @author Christian Campo
 */
public interface IVertrag {
	/**
	 * @return Returns the vertragsNummer.
	 */
	String getVertragsNummer();

	/**
	 * @param vertragsNummer The vertragsNummer to set.
	 */
	void setVertragsNummer( String vertragsNummer );

	/**
	 * @return Returns the vertragsBeschreibung.
	 */
	String getVertragsBeschreibung();

	/**
	 * @param vertragsBeschreibung The vertragsBeschreibung to set.
	 */
	void setVertragsBeschreibung( String vertragsBeschreibung );

	/**
	 * @return Returns the vertragsSumme.
	 */
	Long getVertragsSumme();

	/**
	 * @param vertragsSumme The vertragsSumme to set.
	 */
	void setVertragsSumme( Long vertragsSumme );
}