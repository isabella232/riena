/***************************************************************************************************
 * * Copyright (c) 2004 compeople AG * All rights reserved. The use of this program and the *
 * accompanying materials are subject to license terms. * *
 **************************************************************************************************/
package org.eclipse.riena.example.client.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.riena.ui.ridgets.util.beans.AbstractBean;

import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;

/**
 * PersonenSucheBean , Ralf van Riesen
 */
public class PersonenSucheBean extends AbstractBean {

	private String vorname;
	private String name;
	private String plz;
	private String ort;
	private String ortsteil;
	private String strasse;
	private String geburtsdatum;
	private String vbName;
	private String vbVorname;
	private String vertragsnummer;
	private String telefonnummer;
	private String kfzKennzeichen;
	private String partnergesellschaft;
	private Anrede anrede;

	private String vbNummer;
	private boolean sucheMitStruktur;
	private boolean sucheOhneStruktur = true;
	private boolean suchePhonetisch;
	private boolean sucheNachPersonendaten = true;
	private boolean sucheNachKundennummer;
	private boolean sucheNachVertragsnummer;
	private boolean sucheNachTelefonnummer;
	private boolean sucheNachKFZKennzeichen;
	private boolean sucheUneingeschraenkt = false;

	private boolean sucheMandantDvag = true;
	private boolean sucheMandantAllfinanz = true;

	private boolean sucheOhneEinschraenkung = true;
	private boolean sucheMitEinschraenkung;

	private boolean statusKunde;
	private boolean statusInteressent;
	private boolean statusAltkunde;

	private List<PersonenSucheErgebnisBean> kunden = new ArrayList<PersonenSucheErgebnisBean>();

	private String kundenNummer;
	private List<Message> searchErrorMessages = new ArrayList<Message>();

	private List<PersonenSucheErgebnisBean> selection = new ArrayList<PersonenSucheErgebnisBean>();

	//	private ArrayList<IPersonenSucheChangeHandler> personenSucheChangedHandler = new ArrayList<IPersonenSucheChangeHandler>();

	/**
	 * Konstruktor
	 * 
	 */
	public PersonenSucheBean() {
		super();
	}

	/**
	 * 
	 * @param handler
	 */
	public void addPersonenSucheChangedHandler(IPersonenSucheChangeHandler handler) {
		//			personenSucheChangedHandler.add(handler);
	}

	private void personenSucheChanged() {
		//			for (IPersonenSucheChangeHandler handler : personenSucheChangedHandler) {
		//				handler.personenSucheChanged();
		//			}
	}

	/**
	 * 
	 * @return Integer
	 */
	public Integer getNumericPlz() {
		if (plz == null || plz.trim().length() == 0) {
			return null;
		}

		try {
			return new Integer(Integer.parseInt(plz));
		} catch (NumberFormatException nfe) {
			return null;
		}
	}

	/**
	 * 
	 * @param newPlz
	 */
	public void setNumericPlz(Integer newPlz) {
		if (newPlz == null) {
			plz = null;
		} else {
			plz = Integer.toString(newPlz);
		}
	}

	private void addSearchErrorMessage(Message m) {
		searchErrorMessages.add(m);
	}

	private void clearSearchErrorMessages() {
		searchErrorMessages = new ArrayList<Message>();
	}

	private void trimAllBlanks() {
		if (vorname != null) {
			vorname = vorname.trim();
		}
		if (name != null) {
			name = name.trim();
		}
		if (plz != null) {
			plz = plz.trim();
		}
		if (ort != null) {
			ort = ort.trim();
		}
		if (strasse != null) {
			strasse = strasse.trim();
		}
		if (kundenNummer != null) {
			kundenNummer = kundenNummer.trim();
		}
		if (vertragsnummer != null) {
			vertragsnummer = vertragsnummer.trim();
		}
		if (telefonnummer != null) {
			telefonnummer = telefonnummer.trim();
		}
		if (kfzKennzeichen != null) {
			kfzKennzeichen = kfzKennzeichen.trim();
		}
	}

	/**
	 * 
	 * @return boolean
	 */
	public boolean hasValidSearchParam() {
		boolean result = true;

		trimAllBlanks();
		clearSearchErrorMessages();
		//		if (VBHolder.istInnendienst()) {
		//			if (!isStatusKunde() && !isStatusAltkunde() && !isStatusInteressent()) {
		//				addSearchErrorMessage(MessagesAkte.MEX_03_01_001_006);
		//				result = false;
		//			} else {
		//				if (getSucheNachKundennummer()) {
		//					if (getKundenNummer() == null || getKundenNummer().trim().length() == 0) {
		//						addSearchErrorMessage(MessagesAkte.MEX_03_01_001_007);
		//						result = false;
		//					}
		//				} else if (getSucheNachVertragsnummer()) {
		//					if (getPartnergesellschaft() == null || getPartnergesellschaft().trim().length() == 0
		//							|| getVertragsnummer() == null || getVertragsnummer().trim().length() == 0) {
		//						addSearchErrorMessage(MessagesAkte.MEX_03_01_001_025);
		//						result = false;
		//					}
		//				} else if (getSucheNachTelefonnummer()) {
		//					if (getTelefonnummer() == null || getTelefonnummer().trim().length() == 0) {
		//						addSearchErrorMessage(MessagesAkte.MEX_03_01_001_028);
		//						result = false;
		//					}
		//				} else if (getSucheNachKFZKennzeichen()) {
		//					if (getKfzKennzeichen() == null || getKfzKennzeichen().trim().length() == 0) {
		//						addSearchErrorMessage(MessagesAkte.MEX_03_01_001_029);
		//						result = false;
		//					}
		//				} else {
		//					if (!getSucheOhneEinschraenkung() && (getName() == null || getName().trim().length() == 0)) {
		//						addSearchErrorMessage(MessagesAkte.MEX_03_01_001_008);
		//						result = false;
		//					}
		//					if (getSucheOhneEinschraenkung()) {
		//						// Suche über alle VBs
		//						if ((getLengthOhneSteuerzeichen(getName()) <= 1)) {
		//							addSearchErrorMessage(MessagesAkte.MEX_03_01_001_010);
		//							result = false;
		//						}
		//						// OSC 13.08.07: Auf Anforderung von Herrn Bräuninger (KBV) muß auch die Suche nach Firmen möglich sein
		//						// Firmen haben allerdings manchmal keinen Vornamen!!!!
		//						if ((getLengthOhneSteuerzeichen(getVorname()) <= 0)) {
		//							setVorname(null);
		//							// addSearchErrorMessage( MessagesAkte.MEX_03_01_001_014 );
		//							// result = false;
		//						}
		//					}
		//					if ((!getSucheMitEinschraenkung()) && getSuchePhonetisch()) {
		//						addSearchErrorMessage(MessagesAkte.MEX_03_01_001_024);
		//						result = false;
		//					}
		//				}
		//			}
		//			// Die VbNummer kann null sein
		//			if (getVbNummer() != null) {
		//				if (getVbNummer().trim().length() != 7) {
		//					addSearchErrorMessage(MessagesAkte.MEX_03_01_001_004);
		//					result = false;
		//				} else {
		//					int tempVbNummer = -1;
		//					try {
		//						tempVbNummer = Integer.parseInt(getVbNummer());
		//					} catch (NumberFormatException ex) {
		//						//
		//					}
		//					if (tempVbNummer == -1) {
		//						addSearchErrorMessage(MessagesAkte.MEX_03_01_001_005);
		//						result = false;
		//					}
		//
		//				}
		//			}
		//
		//			if (getSucheMitEinschraenkung() && (getVbNummer() == null || getVbNummer().trim().length() == 0)) {
		//				addSearchErrorMessage(MessagesAkte.MEX_03_01_001_012);
		//				result = false;
		//			}
		//
		//		} else {
		//			if (getVbNummer() == null || getVbNummer().trim().length() == 0) {
		//				addSearchErrorMessage(MessagesAkte.MEX_03_01_001_003);
		//				result = false;
		//			} else {
		//				if (getVbNummer().trim().length() != 7) {
		//					addSearchErrorMessage(MessagesAkte.MEX_03_01_001_004);
		//					result = false;
		//				} else {
		//					int tempVbNummer = -1;
		//					try {
		//						tempVbNummer = Integer.parseInt(getVbNummer());
		//					} catch (NumberFormatException ex) {
		//						//
		//					}
		//					if (tempVbNummer == -1) {
		//						addSearchErrorMessage(MessagesAkte.MEX_03_01_001_005);
		//						result = false;
		//					} else {
		//						if (!isStatusKunde() && !isStatusAltkunde() && !isStatusInteressent()) {
		//							addSearchErrorMessage(MessagesAkte.MEX_03_01_001_006);
		//							result = false;
		//						} else {
		//							if (getSucheNachKundennummer()) {
		//								if (getKundenNummer() == null || getKundenNummer().trim().length() == 0) {
		//									addSearchErrorMessage(MessagesAkte.MEX_03_01_001_007);
		//									result = false;
		//								}
		//							} else if (getSucheNachVertragsnummer()) {
		//								if (getPartnergesellschaft() == null || getPartnergesellschaft().trim().length() == 0
		//										|| getVertragsnummer() == null || getVertragsnummer().trim().length() == 0) {
		//									addSearchErrorMessage(MessagesAkte.MEX_03_01_001_025);
		//									result = false;
		//								}
		//							} else if (getSucheNachTelefonnummer()) {
		//								if (getTelefonnummer() == null || getTelefonnummer().trim().length() == 0) {
		//									addSearchErrorMessage(MessagesAkte.MEX_03_01_001_028);
		//									result = false;
		//								}
		//							} else if (getSucheNachKFZKennzeichen()) {
		//								// TODO Falsche Message
		//								if (getKfzKennzeichen() == null || getKfzKennzeichen().trim().length() == 0) {
		//									addSearchErrorMessage(MessagesAkte.MEX_03_01_001_029);
		//									result = false;
		//								}
		//							} else {
		//								if ((getName() == null || getLengthOhneSteuerzeichen(getName()) == 0)
		//										&& (getPlz() == null || getPlz().length() == 0)) {
		//									addSearchErrorMessage(MessagesAkte.MEX_03_01_001_008);
		//									result = false;
		//								}
		//							}
		//						}
		//					}
		//				}
		//			}
		//		}
		//		addSearchErrorMessage(MessagesAkte.MEX_03_01_001_002);
		return result;
	}

	private int getLengthOhneSteuerzeichen(String suchString) {
		int count = 0;
		if (suchString == null) {
			return 2;
		}
		for (int i = 0; i < suchString.length(); i++) {
			if (suchString.charAt(i) != '*' && suchString.charAt(i) != '"' && suchString.charAt(i) != '?') {
				count++;
			}
		}
		return count;
	}

	//	private Integer getCurrentVbNummer() {
	//		return VBHolder.getVbNummer();
	//	}

	/**
	 * 
	 * 
	 */
	//	public void reset() {
	//		setVorname("");
	//		setName("");
	//		setGeburtsdatum("");
	//		setStrasse("");
	//		setPlz("");
	//		setOrt("");
	//		setKundenNummer("");
	//		setVertragsnummer(null);
	//		setTelefonnummer(null);
	//		setKfzKennzeichen(null);
	//		if (VBHolder.istInnendienst()) {
	//			vbNummer = null;
	//		} else {
	//			Integer currentVbNummer = getCurrentVbNummer();
	//			if (currentVbNummer != null) {
	//				vbNummer = currentVbNummer.toString();
	//			}
	//		}
	//		setVbName(VBHolder.getNachname());
	//		setVbVorname(VBHolder.getVorname());
	//		setSuchePhonetisch(false);
	//		setStatusKunde(true);
	//		setStatusInteressent(true);
	//		setStatusAltkunde(false);
	//		if (VBHolder.istInnendienst()) {
	//			setSucheMandantAllfinanz(false);
	//			setSucheMandantDvag(false);
	//			setSucheOhneStruktur(false);
	//			setSucheMitStruktur(true);
	//			setSucheOhneEinschraenkung(true);
	//			setSucheMitEinschraenkung(false);
	//		} else {
	//			setSucheOhneStruktur(true);
	//			setSucheMitStruktur(false);
	//		}
	//		setSelection(new ArrayList<PersonenSucheErgebnisBean>());
	//		setKunden(new ArrayList<PersonenSucheErgebnisBean>());
	//	}
	/**
	 * 
	 * @return boolean
	 */
	public boolean isDefaultSuchperson() {
		if (!(getPlz() == null || getPlz().equals(""))) {
			return false;
		}
		if (!(getOrt() == null || getOrt().equals(""))) {
			return false;
		}
		if (!(getStrasse() == null || getStrasse().equals(""))) {
			return false;
		}
		if (getSucheMitStruktur()) {
			return false;
		}
		if (!(isStatusKunde() && isStatusInteressent() && !isStatusAltkunde())) {
			return false;
		}
		if (!getVbNummer().equals(getVbNummer().toString())) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @return String
	 */
	public List<Message> getSearchErrorMessages() {
		return searchErrorMessages;
	}

	/**
	 * Erzeuge das Bean welches dem Such-Service übergeben wird
	 * 
	 * @return SuchPerson
	 */
	public SuchPerson getServiceBean() {
		SuchPerson serviceBean = new SuchPerson();
		if (getSucheNachKundennummer()) {
			if (getKundenNummer().length() > 0) {
				serviceBean.setKundenNummer(getKundenNummer());
			}
		} else if (getSucheNachVertragsnummer()) {
			serviceBean.setVertragsnummer(getVertragsnummer());
			serviceBean.setPartnergesellschaft(getPartnergesellschaft());
		} else if (getSucheNachTelefonnummer()) {
			serviceBean.setTelefonnummer(getTelefonnummer());
		} else if (getSucheNachKFZKennzeichen()) {
			serviceBean.setKfzKennzeichen(getKfzKennzeichen());
		} else {
			serviceBean.setVorname(getVorname());
			serviceBean.setNachname(getName());
			serviceBean.setStrasse(getStrasse());
			serviceBean.setPlz(getPlz());
			serviceBean.setOrt(getOrt());
			if (getGeburtsdatum() != null) {
				serviceBean.setGeburtsdatum(new Datum(getGeburtsdatum()));
			}
		}
		//		if (VBHolder.istInnendienst() && getVbNummer() == null) {
		//			serviceBean.setVbNummer(new Integer(8000000));
		//			serviceBean.setVornameSuchart(SuchArt.WILDCARD);
		//			serviceBean.setNachnameSuchart(SuchArt.WILDCARD);
		//		} else {
		//			serviceBean.setVbNummer(new Integer(getVbNummer()));
		//		}
		serviceBean.setKunde(isStatusKunde());
		serviceBean.setInteressent(isStatusInteressent());
		serviceBean.setAltKunde(isStatusAltkunde());
		serviceBean.setSucheMitStruktur(getSucheMitStruktur());
		serviceBean.setSuchePhonetisch(getSuchePhonetisch());
		serviceBean.setSucheMandantAllfinanz(getSucheMandantAllfinanz());
		serviceBean.setSucheMandantDvag(getSucheMandantDvag());
		return serviceBean;
	}

	/**
	 * @return Returns the statusAltkunde.
	 */
	public boolean isStatusAltkunde() {
		return statusAltkunde;
	}

	/**
	 * @param altKunde
	 *            The statusAltkunde to set.
	 */
	public void setStatusAltkunde(boolean altKunde) {
		this.statusAltkunde = altKunde;
	}

	/**
	 * @return Returns the statusInteressent.
	 */
	public boolean isStatusInteressent() {
		return statusInteressent;
	}

	/**
	 * @param interessent
	 *            The statusInteressent to set.
	 */
	public void setStatusInteressent(boolean interessent) {
		this.statusInteressent = interessent;
	}

	/**
	 * @return Returns the statusKunde.
	 */
	public boolean isStatusKunde() {
		return statusKunde;
	}

	/**
	 * @param kunde
	 *            The statusKunde to set.
	 */
	public void setStatusKunde(boolean kunde) {
		this.statusKunde = kunde;
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
	 * @return Returns the sucheMitStruktur.
	 */
	public boolean getSucheMitStruktur() {
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
	 * @return Returns the vbNummer.
	 */
	public String getVbNummer() {
		return vbNummer;
	}

	/**
	 * @param vbNummer
	 *            The vbNummer to set.
	 */
	public void setVbNummer(String vbNummer) {
		if ((this.vbNummer == null && vbNummer != null) || (this.vbNummer != null && (!this.vbNummer.equals(vbNummer)))) {
			this.vbNummer = vbNummer;
			personenSucheChanged();
		}
	}

	/**
	 * @param vbNr
	 *            The vbNummer to set.
	 */
	public void setBasicVbNummer(String vbNr) {
		if ((this.vbNummer == null && vbNr != null) || (this.vbNummer != null && (!this.vbNummer.equals(vbNr)))) {
			this.vbNummer = vbNr;
		}
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
	 * @return Returns the ortsteil.
	 */
	public String getOrtsteil() {
		return ortsteil;
	}

	/**
	 * @param ortsteil
	 *            The ortsteil to set.
	 */
	public void setOrtseil(String ortsteil) {
		this.ortsteil = ortsteil;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param nachName
	 *            The name to set.
	 */
	public void setName(String nachName) {
		this.name = nachName;
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
	 * @return Returns the vorname.
	 */
	public String getVorname() {
		return vorname;
	}

	/**
	 * @param vorName
	 *            The vorname to set.
	 */
	public void setVorname(String vorName) {
		this.vorname = vorName;
	}

	/**
	 * @return Returns the kunden.
	 */
	public List<PersonenSucheErgebnisBean> getKunden() {
		return kunden;
	}

	/**
	 * @param kunden
	 *            The kunden to set.
	 */
	public void setKunden(List<PersonenSucheErgebnisBean> kunden) {
		this.kunden = kunden;
	}

	/**
	 * @return String
	 */
	public String getKundenNummer() {
		return kundenNummer;
	}

	/**
	 * @param kundenNummer
	 */
	public void setKundenNummer(String kundenNummer) {
		this.kundenNummer = kundenNummer;
	}

	/**
	 * @return Returns the selection.
	 */
	public List<PersonenSucheErgebnisBean> getSelection() {
		return selection;
	}

	/**
	 * @param selection
	 *            The selection to set.
	 */
	public void setSelection(List<PersonenSucheErgebnisBean> selection) {
		this.selection = selection;
		firePropertyChanged("selection", this.selection, selection);
	}

	public void setSelection(PersonenSucheErgebnisBean bean) {
		List<PersonenSucheErgebnisBean> tempList = new ArrayList<PersonenSucheErgebnisBean>();
		tempList.add(bean);
		this.selection = tempList;
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
	 * @return boolean
	 */
	public boolean getSucheOhneStruktur() {
		return sucheOhneStruktur;
	}

	/**
	 * @param sucheOhneStruktur
	 */
	public void setSucheOhneStruktur(boolean sucheOhneStruktur) {
		this.sucheOhneStruktur = sucheOhneStruktur;
	}

	/**
	 * @return boolean
	 */
	public boolean getSuchePhonetisch() {
		return suchePhonetisch;
	}

	/**
	 * @param suchePhonetisch
	 */
	public void setSuchePhonetisch(boolean suchePhonetisch) {
		this.suchePhonetisch = suchePhonetisch;
	}

	/**
	 * @return String
	 */
	public String getVbName() {
		return vbName;
	}

	/**
	 * @param vbName
	 */
	public void setVbName(String vbName) {
		this.vbName = vbName;
	}

	/**
	 * @return String
	 */
	public String getVbVollstaendigerName() {
		if (getVbNummer() == null || getVbNummer().length() == 0) {
			return "";
		}
		StringBuffer nameBuffer = new StringBuffer();
		if (getVbVorname() != null) {
			nameBuffer.append(getVbVorname()).append(" ");
		}
		nameBuffer.append(getVbName());
		return nameBuffer.toString();
	}

	/**
	 * @return String
	 */
	public String getVbVorname() {
		return vbVorname;
	}

	/**
	 * @param vbVorname
	 */
	public void setVbVorname(String vbVorname) {
		this.vbVorname = vbVorname;
	}

	/**
	 * @return Returns the sucheNachKundennummer.
	 */
	public boolean getSucheNachKundennummer() {
		return sucheNachKundennummer;
	}

	/**
	 * @param sucheNachKundennummer
	 *            The sucheNachKundennummer to set.
	 */
	public void setSucheNachKundennummer(boolean sucheNachKundennummer) {
		this.sucheNachKundennummer = sucheNachKundennummer;
	}

	/**
	 * @return Returns the sucheNachPersonendaten.
	 */
	public boolean getSucheNachPersonendaten() {
		return sucheNachPersonendaten;
	}

	/**
	 * @param sucheNachPersonendaten
	 *            The sucheNachPersonendaten to set.
	 */
	public void setSucheNachPersonendaten(boolean sucheNachPersonendaten) {
		this.sucheNachPersonendaten = sucheNachPersonendaten;
	}

	/**
	 * @return Returns the sucheMitEinschraenkung.
	 */
	public boolean getSucheMitEinschraenkung() {
		return sucheMitEinschraenkung;
	}

	/**
	 * @param sucheMitEinschraenkung
	 *            The sucheMitEinschraenkung to set.
	 */
	public void setSucheMitEinschraenkung(boolean sucheMitEinschraenkung) {
		this.sucheMitEinschraenkung = sucheMitEinschraenkung;
	}

	/**
	 * @return Returns the sucheOhneEinschraenkung.
	 */
	public boolean getSucheOhneEinschraenkung() {
		return sucheOhneEinschraenkung;
	}

	/**
	 * @param sucheOhneEinschraenkung
	 *            The sucheOhneEinschraenkung to set.
	 */
	public void setSucheOhneEinschraenkung(boolean sucheOhneEinschraenkung) {
		this.sucheOhneEinschraenkung = sucheOhneEinschraenkung;
	}

	/**
	 * @return boolean
	 */
	public boolean getSucheNachVertragsnummer() {
		return sucheNachVertragsnummer;
	}

	/**
	 * @param sucheNachVertragsnummer
	 */
	public void setSucheNachVertragsnummer(boolean sucheNachVertragsnummer) {
		this.sucheNachVertragsnummer = sucheNachVertragsnummer;
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
	 * @return Anrede
	 */
	public Anrede getAnrede() {
		return anrede;
	}

	/**
	 * @param anrede
	 */
	public void setAnrede(Anrede anrede) {
		this.anrede = anrede;
	}

	/**
	 * @return Returns the sucheMandantAllfinanz.
	 */
	public boolean getSucheMandantAllfinanz() {
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
	public boolean getSucheMandantDvag() {
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
	 * @return Returns the sucheUneingeschraenkt.
	 */
	public boolean isSucheUneingeschraenkt() {
		return sucheUneingeschraenkt;
	}

	/**
	 * @param sucheUneingeschraenkt
	 *            The sucheUneingeschraenkt to set.
	 */
	public void setSucheUneingeschraenkt(boolean sucheUneingeschraenkt) {
		this.sucheUneingeschraenkt = sucheUneingeschraenkt;
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

	/**
	 * @return boolean
	 */
	public boolean getSucheNachTelefonnummer() {
		return sucheNachTelefonnummer;
	}

	/**
	 * @param sucheNachTelefonnummer
	 */
	public void setSucheNachTelefonnummer(boolean sucheNachTelefonnummer) {
		this.sucheNachTelefonnummer = sucheNachTelefonnummer;
	}

	/**
	 * @return boolean
	 */
	public boolean getSucheNachKFZKennzeichen() {
		return sucheNachKFZKennzeichen;
	}

	/**
	 * @param sucheNachKFZKennzeichen
	 */
	public void setSucheNachKFZKennzeichen(boolean sucheNachKFZKennzeichen) {
		this.sucheNachKFZKennzeichen = sucheNachKFZKennzeichen;
	}

}