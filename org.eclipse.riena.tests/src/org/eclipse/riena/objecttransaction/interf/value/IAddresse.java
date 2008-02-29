package org.eclipse.riena.objecttransaction.interf.value;

/**
 * TODO Fehlender Klassen-Kommentar
 * 
 * @author Christian Campo
 */
public interface IAddresse {
	/**
	 * @return Returns the ort.
	 */
	String getOrt();

	/**
	 * @param ort The ort to set.
	 */
	void setOrt( String ort );

	/**
	 * @return Returns the plz.
	 */
	String getPlz();

	/**
	 * @param plz The plz to set.
	 */
	void setPlz( String plz );

	/**
	 * @return Returns the strasse.
	 */
	String getStrasse();

	/**
	 * @param strasse The strasse to set.
	 */
	void setStrasse( String strasse );
}