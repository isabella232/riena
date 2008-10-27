/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.example.client.model;


/**
 * TODO missing class comment
 * 
 * 
 */
public interface IPersonenAkteUebersicht extends IPersistentObject {
	/**
	 * 
	 * @return String
	 */
	Datum getGeburtsdatum();

	/**
	 * 
	 * @param geburtsdatum
	 */
	void setGeburtsdatum(Datum geburtsdatum);

	/**
	 * 
	 * @return String
	 */
	Long getKundenNummer();

	/**
	 * 
	 * @param kundenNummer
	 */
	void setKundenNummer(Long kundenNummer);

	/**
	 * 
	 * @return boolean
	 */
	boolean getMehrfachBetreuung();

	/**
	 * 
	 * @param mehrfachBetreuung
	 */
	void setMehrfachBetreuung(boolean mehrfachBetreuung);

	/**
	 * 
	 * @return String
	 */
	String getName();

	/**
	 * 
	 * @param nachName
	 */
	void setName(String nachName);

	/**
	 * 
	 * @return String
	 */
	String getOrt();

	/**
	 * 
	 * @param ort
	 */
	void setOrt(String ort);

	/**
	 * 
	 * @return String
	 */
	String getPlz();

	/**
	 * 
	 * @param plz
	 */
	void setPlz(String plz);

	/**
	 * 
	 * @return String
	 */
	KundenStatus getStatus();

	/**
	 * 
	 * @param status
	 */
	void setStatus(KundenStatus status);

	/**
	 * 
	 * @return String
	 */
	String getStrasse();

	/**
	 * 
	 * @param strasse
	 */
	void setStrasse(String strasse);

	/**
	 * 
	 * @return String
	 */
	String getTelefonNummer();

	/**
	 * 
	 * @param telefonNummer
	 */
	void setTelefonNummer(String telefonNummer);

	/**
	 * 
	 * @return String
	 */
	String getVorname();

	/**
	 * 
	 * @param vorname
	 */
	void setVorname(String vorname);

	/**
	 * 
	 * @return IPersistentOid
	 */
	IPersistentOid getHaushaltPoid();

	/**
	 * 
	 * @param haushaltpoid
	 */
	void setHaushaltPoid(IPersistentOid haushaltpoid);

	/**
	 * @return Returns the adressePoid.
	 */
	IPersistentOid getAdressePoid();

	/**
	 * @param adressePoid
	 *            The adressePoid to set.
	 */
	void setAdressePoid(IPersistentOid adressePoid);

	/**
	 * @return Returns the mitarbeiternr.
	 */
	Integer getVbNummer();

	/**
	 * @param mitarbeiternr
	 *            The mitarbeiternr to set.
	 */
	void setVbNummer(Integer mitarbeiternr);

	/**
	 * @return Returns the mitarbeiterPoid.
	 */
	IPersistentOid getMitarbeiterPoid();

	/**
	 * @param mitarbeiterPoid
	 *            The mitarbeiterPoid to set.
	 */
	void setMitarbeiterPoid(IPersistentOid mitarbeiterPoid);

	/**
	 * @return Returns the anrede.
	 */
	Anrede getAnrede();

	/**
	 * @param anrede
	 *            The anrede to set.
	 */
	void setAnrede(Anrede anrede);

	/**
	 * @return the isExklusivkunde
	 */
	boolean isExklusivkunde();

	/**
	 * @param isExklusivkunde
	 *            the isExklusivkunde to set
	 */
	void setExklusivkunde(boolean isExklusivkunde);

	/**
	 * @return Integer
	 */
	Integer getMandant();

	/**
	 * @param mandant
	 */
	void setMandant(Integer mandant);
}