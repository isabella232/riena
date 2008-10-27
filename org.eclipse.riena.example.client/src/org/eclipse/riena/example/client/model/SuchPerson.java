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
 * Hilfsobject für die Personensuche
 *
 * 
 */
/**
 * 
 *
 */
public class SuchPerson {

	private String vorname;
	private SuchArt vornameSuchart = SuchArt.EXAKT;
	private String nachname;
	private SuchArt nachnameSuchart = SuchArt.EXAKT;
	private String kundenNummer;
	private Integer vbNummer;
	private String strasse;
	private SuchArt strasseSuchart = SuchArt.EXAKT;
	private String plz;
	private String ort;
	private SuchArt ortSuchart = SuchArt.EXAKT;
	private Integer max = new Integer(102);
	private Datum geburtsdatum;
	private String vertragsnummer;
	private String partnergesellschaft;
	private String telefonnummer;
	private String kfzKennzeichen;
	private boolean interessent;
	private boolean kunde;
	private boolean altKunde;
	private boolean sucheMitStruktur;
	private boolean suchePhonetisch;
	private boolean sucheMandantDvag;
	private boolean sucheMandantAllfinanz;

	/**
	 * @param mandant
	 */
	public void setMandant(Integer mandant) {
		//		if (VBHolder.istInnendienst()) {
		//			if (mandant != null && mandant.intValue() == 4400001) {
		//				setSucheMandantDvag(true);
		//				setSucheMandantAllfinanz(false);
		//				return;
		//			} else if (mandant != null && mandant.intValue() == 4400003) {
		//				setSucheMandantAllfinanz(true);
		//				setSucheMandantDvag(false);
		//				return;
		//			} else {
		//				setSucheMandantDvag(true);
		//				setSucheMandantAllfinanz(true);
		//				return;
		//			}
		//		}
		setSucheMandantDvag(false);
		setSucheMandantAllfinanz(false);
	}

	/**
	 * @return Integer
	 */
	public Integer getMandant() {
		//		if (VBHolder.istInnendienst()) {
		//			if (isSucheMandantDvag() && isSucheMandantAllfinanz()) {
		//				return null;
		//			} else if (isSucheMandantDvag()) {
		//				return new Integer(4400001);
		//			} else if (isSucheMandantAllfinanz()) {
		//				return new Integer(4400003);
		//			}
		//		}
		return null;
	}

	/**
	 * @return boolean
	 */
	public boolean isSuchePhonetisch() {
		return suchePhonetisch;
	}

	/**
	 * @param suchePhonetisch
	 */
	public void setSuchePhonetisch(boolean suchePhonetisch) {
		this.suchePhonetisch = suchePhonetisch;
	}

	/**
	 * @return Returns the kundenNummer.
	 */
	public String getKundenNummer() {
		return kundenNummer;
	}

	/**
	 * @param kundenNummer
	 *            The kundenNummer to set.
	 */
	public void setKundenNummer(String kundenNummer) {
		this.kundenNummer = kundenNummer;
	}

	/**
	 * @return Returns the nachname.
	 */
	public String getNachname() {
		return nachname;
	}

	/**
	 * @param nachname
	 *            The nachname to set.
	 */
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	/**
	 * @return Returns the ort.
	 */
	public String getOrt() {
		return ort;
	}

	/**
	 * @param ort
	 *            The ort to set.
	 */
	public void setOrt(String ort) {
		this.ort = ort;
	}

	/**
	 * @return Returns the plz.
	 */
	public String getPlz() {
		return plz;
	}

	/**
	 * @param plz
	 *            The plz to set.
	 */
	public void setPlz(String plz) {
		this.plz = plz;
	}

	/**
	 * @return Returns the strasse.
	 */
	public String getStrasse() {
		return strasse;
	}

	/**
	 * @param strasse
	 *            The strasse to set.
	 */
	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	/**
	 * @return Returns the vbNummer.
	 */
	public Integer getVbNummer() {
		return vbNummer;
	}

	/**
	 * @param vbNummer
	 *            The vbNummer to set.
	 */
	public void setVbNummer(Integer vbNummer) {
		this.vbNummer = vbNummer;
	}

	/**
	 * @return Returns the vorname.
	 */
	public String getVorname() {
		return vorname;
	}

	/**
	 * @param vorname
	 *            The vorname to set.
	 */
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	/**
	 * @return Returns the altKunde.
	 */
	public boolean isAltKunde() {
		return altKunde;
	}

	/**
	 * @param altKunde
	 *            The altKunde to set.
	 */
	public void setAltKunde(boolean altKunde) {
		this.altKunde = altKunde;
	}

	/**
	 * @return Returns the interessent.
	 */
	public boolean isInteressent() {
		return interessent;
	}

	/**
	 * @param interessent
	 *            The interessent to set.
	 */
	public void setInteressent(boolean interessent) {
		this.interessent = interessent;
	}

	/**
	 * @return Returns the kunde.
	 */
	public boolean isKunde() {
		return kunde;
	}

	/**
	 * @param kunde
	 *            The kunde to set.
	 */
	public void setKunde(boolean kunde) {
		this.kunde = kunde;
	}

	/**
	 * @return Returns the sucheMitStruktur.
	 */
	public boolean isSucheMitStruktur() {
		return sucheMitStruktur;
	}

	/**
	 * @param sucheMitStruktur
	 *            The sucheMitStruktur to set.
	 */
	public void setSucheMitStruktur(boolean sucheMitStruktur) {
		this.sucheMitStruktur = sucheMitStruktur;
	}

	/**
	 * @return Returns the nachnameSuchart.
	 */
	public SuchArt getNachnameSuchart() {
		return nachnameSuchart;
	}

	/**
	 * @param nachnameSuchart
	 *            The nachnameSuchart to set.
	 */
	public void setNachnameSuchart(SuchArt nachnameSuchart) {
		this.nachnameSuchart = nachnameSuchart;
	}

	/**
	 * @return Returns the vornameSuchart.
	 */
	public SuchArt getVornameSuchart() {
		return vornameSuchart;
	}

	/**
	 * @param vornameSuchart
	 *            The vornameSuchart to set.
	 */
	public void setVornameSuchart(SuchArt vornameSuchart) {
		this.vornameSuchart = vornameSuchart;
	}

	/**
	 * @return Returns the strasseSuchart.
	 */
	public SuchArt getStrasseSuchart() {
		return strasseSuchart;
	}

	/**
	 * @param strasseSuchart
	 *            The strasseSuchart to set.
	 */
	public void setStrasseSuchart(SuchArt strasseSuchart) {
		this.strasseSuchart = strasseSuchart;
	}

	/**
	 * @return Returns the ortSuchart.
	 */
	public SuchArt getOrtSuchart() {
		return ortSuchart;
	}

	/**
	 * @param ortSuchart
	 *            The ortSuchart to set.
	 */
	public void setOrtSuchart(SuchArt ortSuchart) {
		this.ortSuchart = ortSuchart;
	}

	/**
	 * @return Returns the max.
	 */
	public Integer getMax() {
		return max;
	}

	/**
	 * @param max
	 *            The max to set.
	 */
	public void setMax(Integer max) {
		this.max = max;
	}

	/**
	 * @return Returns the geburtsdatum.
	 */
	public Datum getGeburtsdatum() {
		return geburtsdatum;
	}

	/**
	 * @param geburtsdatum
	 *            The geburtsdatum to set.
	 */
	public void setGeburtsdatum(Datum geburtsdatum) {
		this.geburtsdatum = geburtsdatum;
	}

	/**
	 * @param anderePerson
	 * @return boolean
	 */
	public boolean isNameVornameGeburtsdatumEqualTo(SuchPerson anderePerson) {
		boolean istGleich = true;

		istGleich &= ((getNachname() == null && anderePerson.getNachname() == null) || (getNachname() != null && getNachname()
				.equals(anderePerson.getNachname())));
		istGleich &= ((getVorname() == null && anderePerson.getVorname() == null) || (getVorname() != null && getVorname()
				.equals(anderePerson.getVorname())));
		istGleich &= ((getGeburtsdatum() == null && anderePerson.getGeburtsdatum() == null) || (getGeburtsdatum() != null && getGeburtsdatum()
				.equals(anderePerson.getGeburtsdatum())));

		return istGleich;
	}

	/**
	 * @return String
	 */
	public String getPartnergesellschaft() {
		return partnergesellschaft;
	}

	/**
	 * @param partnergesellschaft
	 */
	public void setPartnergesellschaft(String partnergesellschaft) {
		this.partnergesellschaft = partnergesellschaft;
	}

	/**
	 * @return String
	 */
	public String getVertragsnummer() {
		return vertragsnummer;
	}

	/**
	 * @param vertragsnummer
	 */
	public void setVertragsnummer(String vertragsnummer) {
		this.vertragsnummer = vertragsnummer;
	}

	/**
	 * @return Returns the sucheMandantAllfinanz.
	 */
	public boolean isSucheMandantAllfinanz() {
		return sucheMandantAllfinanz;
	}

	/**
	 * @param sucheMandantAllfinanz
	 *            The sucheMandantAllfinanz to set.
	 */
	public void setSucheMandantAllfinanz(boolean sucheMandantAllfinanz) {
		this.sucheMandantAllfinanz = sucheMandantAllfinanz;
	}

	/**
	 * @return Returns the sucheMandantDvag.
	 */
	public boolean isSucheMandantDvag() {
		return sucheMandantDvag;
	}

	/**
	 * @param sucheMandantDvag
	 *            The sucheMandantDvag to set.
	 */
	public void setSucheMandantDvag(boolean sucheMandantDvag) {
		this.sucheMandantDvag = sucheMandantDvag;
	}

	/**
	 * @return String
	 */
	public String getTelefonnummer() {
		return telefonnummer;
	}

	/**
	 * @param telefonnummer
	 */
	public void setTelefonnummer(String telefonnummer) {
		this.telefonnummer = telefonnummer;
	}

	/**
	 * @return String
	 */
	public String getKfzKennzeichen() {
		return kfzKennzeichen;
	}

	/**
	 * @param kfzKennzeichen
	 */
	public void setKfzKennzeichen(String kfzKennzeichen) {
		this.kfzKennzeichen = kfzKennzeichen;
	}
}