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

import java.util.List;

import org.eclipse.riena.ui.core.resource.IIconManager;
import org.eclipse.riena.ui.core.resource.IconManagerAccessor;

/**
 * PersonenSucheErgebnisBean
 * 
 * 
 */
public class PersonenSucheErgebnisBean implements Comparable {
	private IPersistentOid personPoid;
	private IPersistentOid mitarbeiterPoid;
	private IPersistentOid adressePoid;
	private IPersistentOid haushaltPoid;
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
			return this.getVorName().toLowerCase()
					.compareTo(((PersonenSucheErgebnisBean) o).getVorName().toLowerCase());
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
	 * @return Returns the adressePoid.
	 */
	public IPersistentOid getAdressePoid() {
		return adressePoid;
	}

	/**
	 * @param adressePoid
	 *            The adressePoid to set.
	 */
	public void setAdressePoid(IPersistentOid adressePoid) {
		this.adressePoid = adressePoid;
	}

	/**
	 * @return Returns the mitarbeiterPoid.
	 */
	public IPersistentOid getMitarbeiterPoid() {
		return mitarbeiterPoid;
	}

	/**
	 * @param mitarbeiterPoid
	 *            The mitarbeiterPoid to set.
	 */
	public void setMitarbeiterPoid(IPersistentOid mitarbeiterPoid) {
		this.mitarbeiterPoid = mitarbeiterPoid;
	}

	/**
	 * @return Returns the personPoid.
	 */
	public IPersistentOid getPersonPoid() {
		return personPoid;
	}

	/**
	 * @param personPoid
	 *            The personPoid to set.
	 */
	public void setPersonPoid(IPersistentOid personPoid) {
		this.personPoid = personPoid;
	}

	/**
	 * @return Returns the haushaltPoid.
	 */
	public IPersistentOid getHaushaltPoid() {
		return haushaltPoid;
	}

	/**
	 * @param haushaltPoid
	 *            The haushaltPoid to set.
	 */
	public void setHaushaltPoid(IPersistentOid haushaltPoid) {
		this.haushaltPoid = haushaltPoid;
	}

	/*
	 * public ILabelAdapter getIcon() { ILabelAdapter kiIcon =
	 * AdapterFactory.createLabelAdapter( "LABEL" + getPersonPoid().toString()
	 * ); if ( mehrfachBetreuung ) { kiIcon.setIcon( ICON_MANAGER.getIconID(
	 * IAkteIconMapper.MEHRFACH_BETREUT, IconSize.A ) ); kiIcon.setText(
	 * "mehrfach betreut" ); } else { kiIcon.setIcon( ICON_MANAGER.getIconID(
	 * IAkteIconMapper.LEER_ICON, IconSize.A ) ); } return kiIcon; }
	 */

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

	/*
	 * 
	 * public ILabelAdapter getPersonIcon() {
	 * 
	 * if ( personIcon == null ) { if ( isKommissarisch() ) { ILabelAdapter
	 * kiIcon = AdapterFactory.createLabelAdapter( "LABEL2" +
	 * getPersonPoid().toString() );
	 * kiIcon.setToolTipText("Kommissarisch betreut"); if ( getAnrede().equals(
	 * Anrede.FIRMA ) ) { kiIcon.setIcon( ICON_MANAGER.getIconID(
	 * IAkteIconMapper.KUNDE_KOMMISSARISCH_BETREUT_FIRMA, IconSize.A ) ); } else
	 * { if ( getAnrede().equals( Anrede.FRAU ) ) { kiIcon.setIcon(
	 * ICON_MANAGER.getIconID(
	 * IAkteIconMapper.KUNDE_KOMMISSARISCH_BETREUT_WEIBLICH, IconSize.A ) ); }
	 * else { kiIcon.setIcon( ICON_MANAGER.getIconID(
	 * IAkteIconMapper.KUNDE_KOMMISSARISCH_BETREUT_MAENNLICH, IconSize.A ) ); }
	 * } setPersonIcon( kiIcon ); } else if ( getStatus() != null &&
	 * KundenStatus.INTERESSENT.getValue().equals( getStatus() ) ) {
	 * ILabelAdapter kiIcon = AdapterFactory.createLabelAdapter( "LABEL2" +
	 * getPersonPoid().toString() ); kiIcon.setToolTipText("Interessent"); if (
	 * getAnrede().equals( Anrede.FIRMA ) ) { kiIcon.setIcon(
	 * ICON_MANAGER.getIconID( IAkteIconMapper.INTERESSENT_FIRMA, IconSize.A )
	 * ); } else { if ( getAnrede().equals( Anrede.FRAU ) ) { kiIcon.setIcon(
	 * ICON_MANAGER.getIconID( IAkteIconMapper.INTERESSENT_WEIBLICH, IconSize.A
	 * ) ); } else { kiIcon.setIcon( ICON_MANAGER.getIconID(
	 * IAkteIconMapper.INTERESSENT_MAENNLICH, IconSize.A ) ); } } setPersonIcon(
	 * kiIcon ); } else if ( getStatus() != null &&
	 * KundenStatus.KUNDE.getValue().equals( getStatus() ) ) { ILabelAdapter
	 * kiIcon = AdapterFactory.createLabelAdapter( "LABEL2" +
	 * getPersonPoid().toString() ); kiIcon.setToolTipText("Kunde"); if (
	 * getAnrede().equals( Anrede.FIRMA ) ) { kiIcon.setIcon(
	 * ICON_MANAGER.getIconID( IAkteIconMapper.KUNDE_FIRMA, IconSize.A ) ); }
	 * else { if ( getAnrede().equals( Anrede.FRAU ) ) { kiIcon.setIcon(
	 * ICON_MANAGER.getIconID( IAkteIconMapper.KUNDE_WEIBLICH, IconSize.A ) ); }
	 * else { kiIcon.setIcon( ICON_MANAGER.getIconID(
	 * IAkteIconMapper.KUNDE_MAENNLICH, IconSize.A ) ); } } setPersonIcon(
	 * kiIcon ); } else if ( getStatus() != null &&
	 * KundenStatus.ALTKUNDE.getValue().equals( getStatus() ) ) { ILabelAdapter
	 * kiIcon = AdapterFactory.createLabelAdapter( "LABEL2" +
	 * getPersonPoid().toString() ); kiIcon.setToolTipText("Altkunde"); if (
	 * getAnrede().equals( Anrede.FIRMA ) ) { kiIcon.setIcon(
	 * ICON_MANAGER.getIconID( IAkteIconMapper.ALTKUNDE_FIRMA, IconSize.A ) ); }
	 * else { if ( getAnrede().equals( Anrede.FRAU ) ) { kiIcon.setIcon(
	 * ICON_MANAGER.getIconID( IAkteIconMapper.ALTKUNDE_WEIBLICH, IconSize.A )
	 * ); } else { kiIcon.setIcon( ICON_MANAGER.getIconID(
	 * IAkteIconMapper.ALTKUNDE_MAENNLICH, IconSize.A ) ); } } setPersonIcon(
	 * kiIcon ); } if ( personIcon != null ) { personIcon.setText( getStatus()
	 * ); } } return personIcon; }
	 */

	/*
	 * public void setPersonIcon( ILabelAdapter icon2 ) { this.personIcon =
	 * icon2; }
	 */

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

	/*
	 * public ILabelAdapter getMandantIcon(){ ILabelAdapter mandantIcon = null;
	 * if(getMandant() != null && getMandant().equals(new Integer(4400003))){
	 * mandantIcon = AdapterFactory.createLabelAdapter( "MandantIcon" +
	 * getPersonPoid().toString() ); mandantIcon.setIcon(
	 * ICON_MANAGER.getIconID( IAkteIconMapper.MANDANT_ALLFINANZ, IconSize.A )
	 * ); } return mandantIcon; }
	 */
}
