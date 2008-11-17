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

import java.util.List;

import org.eclipse.riena.demo.customer.common.Anrede;
import org.eclipse.riena.demo.customer.common.IPersonenAkteUebersicht;
import org.eclipse.riena.ui.core.resource.IIconManager;
import org.eclipse.riena.ui.core.resource.IconManagerAccessor;

/**
 * PersonenSucheErgebnisBean
 * 
 * 
 */
public class PersonenSucheErgebnisBean implements Comparable {
	private String kundenNummer;
	private String vbNummer;
	private String vorName;
	private String nachName;
	private String geburtsdatum;
	private String strasse;
	private String plz;
	private String ort;

	// private ILabelAdapter personIcon;
	private String rufNummer;
	private String status;
	private boolean mehrfachBetreuung;
	private boolean kommissarisch;
	private List kommunikationsverbindungen;
	private Anrede anrede;
	private Integer mandant;
	private IPersonenAkteUebersicht uebersicht;

	private final static IIconManager ICON_MANAGER = IconManagerAccessor.fetchIconManager();

	/**
	 * @return Returns the vbNummer.
	 */
	public String getVbNummer() {
		return vbNummer;
	}

	/**
	 * @param nummer
	 *            The vbNummer to set.
	 */
	public void setVbNummer(String nummer) {
		vbNummer = nummer;
	}

	/**
	 * @return Returns the kommissarisch.
	 */
	public boolean isKommissarisch() {
		return kommissarisch;
	}

	/**
	 * @param kommissarisch
	 *            The kommissarisch to set.
	 */
	public void setKommissarisch(boolean kommissarisch) {
		this.kommissarisch = kommissarisch;
	}

	/**
	 * @return Returns the mehrfachBetreuung.
	 */
	public boolean isMehrfachBetreuung() {
		return mehrfachBetreuung;
	}

	/**
	 * @param mehrfachBetreuung
	 *            The mehrfachBetreuung to set.
	 */
	public void setMehrfachBetreuung(boolean mehrfachBetreuung) {
		this.mehrfachBetreuung = mehrfachBetreuung;
	}

	/**
	 * @return Returns the rufNummer.
	 */
	public String getRufNummer() {
		return rufNummer;
	}

	/**
	 * @param rufNummer
	 *            The rufNummer to set.
	 */
	public void setRufNummer(String rufNummer) {
		this.rufNummer = rufNummer;
	}

	/**
	 * @return Returns the status.
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            The status to set.
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return Returns the geburtsdatum.
	 */
	public String getGeburtsdatum() {
		return geburtsdatum;
	}

	/**
	 * @param geburtsdatum
	 *            The geburtsdatum to set.
	 */
	public void setGeburtsdatum(String geburtsdatum) {
		this.geburtsdatum = geburtsdatum;
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
	 * @return Returns the nachName.
	 */
	public String getNachName() {
		return nachName;
	}

	/**
	 * @param nachName
	 *            The nachName to set.
	 */
	public void setNachName(String nachName) {
		this.nachName = nachName;
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
	 * @return Returns the vorName.
	 */
	public String getVorName() {
		return vorName;
	}

	/**
	 * @param vorName
	 *            The vorName to set.
	 */
	public void setVorName(String vorName) {
		this.vorName = vorName;
	}

	private String transformStringForCompare(String compareString) {
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < compareString.length(); i++) {
			char temp = compareString.charAt(i);
			if (temp == 'ä') {
				b.append("ae");
			} else {
				if (temp == 'ü') {
					b.append("ue");
				} else {
					if (temp == 'ö') {
						b.append("oe");
					} else {
						b.append(temp);
					}

				}
			}

		}
		return b.toString();
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		String compareString = this.getNachName().toLowerCase();
		compareString = transformStringForCompare(compareString);

		String otherString = ((PersonenSucheErgebnisBean) o).getNachName().toLowerCase();
		otherString = transformStringForCompare(otherString);

		int result = compareString.compareTo(otherString);
		if (result == 0) {
			if (this.getVorName() == null || ((PersonenSucheErgebnisBean) o).getVorName() == null) {
				return -1;
			}
			return this.getVorName().toLowerCase().compareTo(((PersonenSucheErgebnisBean) o).getVorName().toLowerCase());
		} else {
			return result;
		}

	}

	/**
	 * @return Returns the telefonAdressen.
	 */
	public List getKommunikationsverbindungen() {
		return kommunikationsverbindungen;
	}

	/**
	 * @param kommunikationsverbindungen
	 */
	public void setKommunikationsverbindungen(List kommunikationsverbindungen) {
		this.kommunikationsverbindungen = kommunikationsverbindungen;
	}

	/**
	 * @return the uebersicht
	 */
	public IPersonenAkteUebersicht getUebersicht() {
		return uebersicht;
	}

	/**
	 * @param uebersicht
	 *            the uebersicht to set
	 */
	public void setUebersicht(IPersonenAkteUebersicht uebersicht) {
		this.uebersicht = uebersicht;
	}

	/**
	 * @return Returns the anrede.
	 */
	public Anrede getAnrede() {
		return anrede;
	}

	/**
	 * @param anrede
	 *            The anrede to set.
	 */
	public void setAnrede(Anrede anrede) {
		this.anrede = anrede;
	}

	/**
	 * @return Returns the mandant.
	 */
	public Integer getMandant() {
		return mandant;
	}

	/**
	 * @param mandant
	 *            The mandant to set.
	 */
	public void setMandant(Integer mandant) {
		this.mandant = mandant;
	}

}
