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

/**
 * Konstanten für Meldungen
 */
public interface MessagesAkte {
	/**
	 * Bitte prüfen
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul universal
	 * @Bereich
	 * @Autor UNBEKANNT
	 * @Dokument
	 * @Name Infotext
	 * @Beschreibung
	 * @Bemerkung Meldung unklar, nach unserer standardisierten Definition,
	 *            handelt es sich nicht um eine Information. Entweder ist eine
	 *            Eingabe falsch, dann handelt es sich um einen Fehler oder die
	 *            Eingabe hat zur Folge, dass das Handlungsziel verfehlt werden
	 *            kann, dann handelt es sich um eine Warnung. Desweiteren fehlt
	 *            ein Hinweis, was an einer Eingabe bemängelt wird.
	 * @MEX 03.01.999.001
	 * @StatusRE ungültig
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_001 = new Message("03.01.999.001", MessageType.INFO, "Bitte prüfen",
			"Bitte überprüfen Sie ihre Eingabe.", "", "", new String[] { "Ok" });

	/**
	 * Bitte prüfen
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.002
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_002 = new Message("03.01.999.002", MessageType.INFO, "Bitte prüfen",
			"Bitte machen Sie in allen gelben Pflichtfeldern eine Angabe.", "", "", new String[] { "Ok" });

	/**
	 * Daten gespeichert
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.004
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_003 = new Message("03.01.999.004", MessageType.ERROR, "Daten nicht gespeichert",
			"Die Daten konnten nicht gespeichert werden! Bitte versuchen Sie es noch einmal.", "", "",
			new String[] { "Ok" });

	/**
	 * Daten gespeichert
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.004
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_004 = new Message("03.01.999.004", MessageType.INFO, "Daten gespeichert",
			"Die Daten wurden erfolgreich gespeichert.", "", "", new String[] { "Ok" });

	/**
	 * Eingeschränkte Rechte
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.007
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_007 = new Message("03.01.999.007", MessageType.INFO, "Eingeschränkte Rechte",
			"Die angemeldete Person darf keine neue Person oder einen neuen Haushalt anlegen.", "", "",
			new String[] { "Ok" });

	/**
	 * Es sind Änderungen vorhanden, die noch nicht übernommen wurden
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.008
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_008 = new Message("03.01.999.008", MessageType.QUESTION,
			"Es sind Änderungen vorhanden, die noch nicht übernommen wurden",
			"Möchten Sie diese Änderungen übernehmen oder verwerfen?", "", "", new String[] { "Jetzt übernehmen",
					"Später übernehmen", "Verwerfen", "Abbrechen" });

	/**
	 * Es sind Änderungen für die bisher ausgewählte Person vorhanden
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.010
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_010 = new Message("03.01.999.010", MessageType.QUESTION,
			"Änderungen für die ausgewählte Person wurden nicht in die Tabelle übernommen",
			"Änderungen werden nur wirksam, wenn sie in die Tabelle übernommen werden", "",
			"Möchten Sie die Änderungen übernehmen?",
			new String[] { "Änderungen übernehmen", "Verwerfen", "Abbrechen" });

	/**
	 * In diesem Haushalt haben sich Daten geändert
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.009
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_009 = new Message("03.01.999.009", MessageType.QUESTION, "Änderungen sind nicht gespeichert",
			"Sie haben Änderungen an den Daten der Akte vorgenommen.", "",
			"Möchten Sie die Änderungen vor dem Schliessen speichern?", new String[] { "Speichern", "Verwerfen",
					"Abbrechen" });

	/**
	 * Personenanlage
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.020
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_020 = new Message("03.01.999.020", MessageType.ERROR, "Personenanlage",
			"Bitte legen Sie zunächst eine Person an.", "", "", new String[] { "Ok" });

	/**
	 * Keine Änderungen vorhanden
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.021
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_021 = new Message("03.01.999.021", MessageType.INFO, "Keine Änderungen vorhanden",
			"Es sind keine Änderungen vorhanden.", "", "", new String[] { "Ok" });

	/**
	 * Daten unvollständig
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.022
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_022 = new Message("03.01.999.022", MessageType.ERROR, "Daten unvollständig",
			"Ihre Daten sind noch unvollständig.", "", "", new String[] { "Ok" });

	/**
	 * Berechnung nur in einem Haushalt möglich
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.023
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_023 = new Message("03.01.999.023", MessageType.INFO,
			"Berechnung nur in einem Haushalt möglich",
			"Bitte aktivieren Sie zunächst einen Haushalt für Ihre Berechnung.", "", "", new String[] { "Ok" });

	/**
	 * Technischer Fehler
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.027
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_027 = new Message("03.01.999.027", MessageType.ERROR, "Technischer Fehler",
			"Noch nicht verfügbar.", "", "", new String[] { "Ok" });

	/**
	 * Technische Information
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.028
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_028 = new Message("03.01.999.028", MessageType.INFO, "Technische Information",
			"XML Datenbank nachgeladen.", "", "", new String[] { "Ok" });

	/**
	 * Sicherheitsabfrage
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.029
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_029 = new Message(
			"03.01.999.029",
			MessageType.QUESTION,
			"Verträge löschen",
			"Sind Sie sicher, dass Sie folgende Verträge löschen möchten ?{0}",
			"{1}Die Verträge sowie dazu gehörige Aufgaben in PIM werden beim Speichern des Haushaltes endgültig gelöscht.",
			"", new String[] { "Ja", "Nein" });

	/**
	 * Der Haushalt ist unvollständig
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.030
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_030 = new Message("03.01.999.030", MessageType.ERROR, "Der Haushalt ist unvollständig",
			"Bitte erfassen Sie zunächst mindestens eine Person und die Haushaltsadresse.", "", "",
			new String[] { "Ok" });

	/**
	 * Vertragsablauf liegt vor dem Vertragsbeginn
	 * 
	 * @Projekt OKI-Haushalt/Vertrag
	 * @Modul Vertrag
	 * @Bereich Vertragsdatum
	 * @Autor MHB
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.031
	 * @StatusRE Entwurf
	 * @StatusFA ungeprüft
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_999_031 = new Message("03.01.999.031", MessageType.ERROR,
			"Vertragsablauf liegt vor dem Vertragsbeginn", "Bitte überprüfen Sie Ihre Eingabe", "", "",
			new String[] { "OK" });

	/**
	 * Wiedervorlagedatum liegt in der Vergangenheit
	 * 
	 * @Projekt OKI-Haushalt/Vertrag
	 * @Modul Vertrag
	 * @Bereich Wiedervorlagedatum
	 * @Autor MHB
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.032
	 * @StatusRE Entwurf
	 * @StatusFA ungeprüft
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_999_032 = new Message("03.01.999.032", MessageType.ERROR,
			"Wiedervorlagedatum liegt in der Vergangenheit", "Bitte überprüfen Sie Ihre Eingabe", "", "",
			new String[] { "OK" });

	/**
	 * Die Bewertung benoetigt eine Bezeichnung
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.033
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_033 = new Message("03.01.999.033", MessageType.ERROR,
			"Die Bewertung benoetigt eine Bezeichnung", "Bitte überprüfen Sie Ihre Eingabe", "", "",
			new String[] { "OK" });

	/**
	 * Sicherheitsabfrage
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.034
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_034 = new Message("03.01.999.034", MessageType.QUESTION, "Sicherheitsabfrage",
			"Sind Sie sicher, dass Sie diesen Freistellungsauftrag löschen möchten ?", "", "", new String[] { "Ja",
					"Nein" });

	/**
	 * Freistellungsauftrag konnte nicht angelegt werden
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.035
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_035 = new Message("03.01.999.035", MessageType.QUESTION,
			"Freistellungsauftrag konnte nicht angelegt werden", "Es sind nicht alle Pflichtfelder korrekt gefüllt",
			"", "Wollen Sie Ihre Eingaben überprüfen oder den Freistellungsauftrag unerledigt verlassen?",
			new String[] { "Eingaben überprüfen", "Freistellungsauftrag verlassen" });

	/**
	 * Freistellungsauftrag existiert bereits
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.036
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_036 = new Message("03.01.999.036", MessageType.INFO,
			"Freistellungsauftrag existiert bereits",
			"Ihr Kunde hat bereits einen Freistellungsauftrag für die gleiche Gesellschaft.", "",
			"Ein weiterer Freistellungsauftrag kann deshalb nicht erstellt werden.", new String[] { "OK" });

	/**
	 * Tarifart bereits vorhanden
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.037
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_037 = new Message("03.01.999.037", MessageType.INFO, "Tarifart bereits vorhanden",
			"Ihr Kunde hat bereits diese Tarifart.", "", "Ein Eintrag kann deshalb nicht erstellt werden.",
			new String[] { "OK" });

	/**
	 * Der Kunde ist durch eine andere Bestandsfunktion gesperrt
	 * 
	 * @Projekt
	 * @Modul Akte
	 * @Bereich Aktenzusammenführung
	 * @Autor ORE
	 * @Dokument UseCase
	 * @Name Akten zusammenführen
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.038
	 * @StatusRE Neu
	 * @StatusFA Neu
	 * @StatusDEV Neu
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_999_038 = new Message("03.01.999.038", MessageType.INFO, "Der ausgewählte Kunde ist gesperrt",
			"Der von Ihnen ausgewählte Kunde $Kundeninfo ist aufgrund einer laufenden Bestandsdatenänderung gesperrt.",
			"", "Die Bearbeitung des Kunden ist für Sie in wenigen Stunden (ca. 1 bis 3 Stunden) wieder möglich.",
			new String[] { "OK" });

	/**
	 * Bitte prüfen
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.039.001
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_039_001 = new Message("03.01.039.001", MessageType.ERROR, "Bitte prüfen",
			"Die Adresse ist nicht vollständig.", "", "", new String[] { "Ok" });

	/**
	 * Die Strasse weicht ab
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.039.002
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_039_002 = new Message("03.01.039.002", MessageType.QUESTION, "Die Strasse weicht ab",
			"Die eingebene Strasse {0} weicht von Ergebnis {1} ab. Soll das Ergebnis übernommen werden ?", "", "",
			new String[] { "Ja", "Nein" });

	/**
	 * Die Adresse ist unbekannt
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.039.003
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_039_003 = new Message("03.01.039.003", MessageType.QUESTION, "Die Adresse ist unbekannt",
			"Der Straßenname ist in Kombination mit der eingegebenen PLZ und dem Ort unbekannt.", "",
			"Möchten Sie ihre Eingabe ändern?", new String[] { "Adressdaten ändern", "Nicht ändern", "Abbrechen" });

	/**
	 * Die Adresse ist unbekannt
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.039.004
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_039_004 = new Message(
			"03.01.039.004",
			MessageType.QUESTION,
			"Die Adresse ist unbekannt",
			"Der Straßenname ist in Kombination mit der eingegebenen PLZ und dem Ort unbekannt. Möchten Sie ihre Eingabe überprüfen ?",
			"", "", new String[] { "Ja", "Nein, ich möchte die Eingabe belassen" });

	/**
	 * Die Adresse ist unbekannt
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.039.005
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_039_005 = new Message(
			"03.01.039.005",
			MessageType.QUESTION,
			"Die Adresse ist unbekannt",
			"Die Hausnummer ist in Kombination mit der eingegebenen PLZ unbekannt, möglicherweise fehlt der Ortsteil. Möchten Sie ihre Eingabe überprüfen ?",
			"", "", new String[] { "Ja", "Nein, ich möchte die Eingabe belassen" });

	/**
	 * Technischer Fehler
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.039.901
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_039_901 = new Message("03.01.039.901", MessageType.ERROR, "Technischer Fehler",
			"Die Postalische Prüfung steht zur Zeit aus technischen Gründen nicht zur Verfügung.", "", "",
			new String[] { "Ok" });

	/**
	 * Fehler in der Suche
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.001
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_001 = new Message("03.01.001.001", MessageType.ERROR, "Fehler in der Suche",
			"<dynamischer Inhalt>", "", "", new String[] { "Ok" });

	/**
	 * Fehler in der Suche
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.002
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_002 = new Message("03.01.001.002", MessageType.ERROR, "Fehler in der Suche",
			"Bitte verändern Sie Ihre Suchkriterien.", "", "", new String[] { "Ok" });

	/**
	 * Keine VB-Nummer vorhanden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor UNBEKANNT
	 * @Dokument
	 * @Name Suche, VB-Nr. fehlt
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.003
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_003 = new Message("03.01.001.003", MessageType.ERROR, "Keine VB-Nummer vorhanden",
			"Bitte geben Sie eine 7-stellige VB-Nummer ein.", "", "", new String[] { "Ok" });

	/**
	 * VB-Nummer falsch
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor UNBEKANNT
	 * @Dokument
	 * @Name Suche, VB-Nr.
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.004
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_004 = new Message("03.01.001.004", MessageType.ERROR, "VB-Nummer falsch",
			"Bitte geben Sie eine 7-stellige VB-Nummer ein.", "", "", new String[] { "Ok" });

	/**
	 * Fehler in der Suche
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.005
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_005 = new Message("03.01.001.005", MessageType.ERROR, "Fehler in der Suche",
			"Die VB-Nummer ist inkorrekt.", "", "", new String[] { "Ok" });

	/**
	 * Fehler in der Suche
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.006
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_006 = new Message("03.01.001.006", MessageType.ERROR, "Fehler in der Suche",
			"Sie haben keinen Kundenstatus angegeben.", "", "", new String[] { "Ok" });

	/**
	 * Fehler in der Suche
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.007
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_007 = new Message("03.01.001.007", MessageType.ERROR, "Fehler in der Suche",
			"Für die Suche nach Kundennummer ist die Kundennummer notwendig.", "", "", new String[] { "Ok" });

	/**
	 * Keine Suchkriterien vorhanden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor UNBEKANNT
	 * @Dokument
	 * @Name Suche, keinerlei Eingabe vorhanden
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.008
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_008 = new Message("03.01.001.008", MessageType.ERROR, "Keine Suchkriterien vorhanden",
			"Bitte geben Sie mindestens den Namen der gesuchten Person oder eine PLZ an.", "", "",
			new String[] { "Ok" });

	/**
	 * VB-Nummer ungültig
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.009
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_009 = new Message("03.01.001.009", MessageType.ERROR, "VB-Nummer ungültig",
			"Bitte geben Sie eine gültige VB-Nummer ein.", "", "", new String[] { "Ok" });

	/**
	 * Suchkriterien unvollständig
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.011
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_011 = new Message("03.01.001.011", MessageType.ERROR, "Suchkriterien unvollständig",
			"Bitte geben Sie zu dem Vornamen auch einen Namen an.", "", "", new String[] { "Ok" });

	/**
	 * Suchkriterien unvollständig
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.012
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_012 = new Message("03.01.001.012", MessageType.ERROR, "Suchkriterien unvollständig",
			"Bei einer eingeschränkten Suche müssen Sie eine VB-Nummer angeben.", "", "", new String[] { "Ok" });

	/**
	 * Keine Suchkriterien vorhanden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor UNBEKANNT
	 * @Dokument
	 * @Name Suche, keinerlei Eingabe vorhanden
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.013
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_013 = new Message("03.01.001.013", MessageType.ERROR, "Keine Suchkriterien vorhanden",
			"Bitte füllen Sie mindestens eines der Felder PLZ, Ort oder Straße.", "", "", new String[] { "Ok" });

	/**
	 * Fehler in der Suche
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich Suche
	 * @Autor DKI
	 * @Dokument UseCase
	 * @Name 03.001
	 * @Beschreibung Valide suchanfrage eines Innendienstmitarbeiters
	 * @Bemerkung USE CASE Akte Suchen 03.001 Absch. 6.9: mind 2 Buchstaben Name
	 *            und Vorname müssen bestimmt werden
	 * @MEX 03.01.001.014
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_014 = new Message("03.01.001.014", MessageType.INFO, "Fehler in der Suche",
			"Bitte geben sie mindestens zwei Buchstaben für den Vornamen an.", "", "", new String[] { "OK" });

	/**
	 * VB-Nummer ungültig
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.015
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_015 = new Message("03.01.001.015", MessageType.ERROR, "VB-Nummer ungültig",
			"Die von Ihnen genannte VB-Nummer ist unbekannt oder Sie sind für diese VB-Nummer nicht berechtigt.", "",
			"", new String[] { "Ok" });

	/**
	 * Keine EMail Adresse gefunden
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.016
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_016 = new Message("03.01.001.016", MessageType.QUESTION, "Keine E-Mail Adresse vorhanden",
			"{0} hat keine E-Mail Adresse.", "", "Möchten Sie die Akte öffnen, um eine E-Mail Adresse anzulegen?",
			new String[] { "Akte öffnen", "Abbrechen" });

	/**
	 * Fehler in der Suche
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.901
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_901 = new Message("03.01.001.901", MessageType.ERROR, "Fehler in der Suche",
			"Suchparameter sind ungültig", "", "", new String[] { "Ok" });

	/**
	 * Keine Ergebnisse
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.020
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_020 = new Message("03.01.001.020", MessageType.INFO, "Keine Treffer",
			"Ihre Suche ergab keine Treffer. Bitte überprüfen Sie Ihre Eingabe.", "", "", new String[] { "Ok" });

	/**
	 * Mehr als 100 Treffer
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.021
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_021 = new Message(
			"03.01.001.021",
			MessageType.WARNING,
			"Mehr als 100 Treffer",
			"Ihre Suche ergab mehr als 100 Treffer. Bitte grenzen Sie Ihre Suche durch Veränderung der Suchkriterien weiter ein.",
			"", "", new String[] { "Ok" });

	/**
	 * Keine Person gefunden.
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul
	 * @Bereich
	 * @Autor HFI
	 * @Dokument UseCase
	 * @Name Erfolglose Suche mitr Kundennummer
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.023
	 * @StatusRE OK
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_001_023 = new Message("03.01.001.023", MessageType.INFO, "Keine Person gefunden.",
			"Zu der eingegebenen Kundennummer wurde keine Person gefunden.\n"
					+ "Bitte überprüfen Sie die eingegebene Kundennummer und versuchen Sie es erneut.", "", "",
			new String[] { "OK" });

	/**
	 * Suchparameter ungültig
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.024
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_024 = new Message(
			"03.01.001.024",
			MessageType.ERROR,
			"Suchparameter ungültig",
			"Eine phonetische Suche innerhalb der gesamten Struktur ist leider nicht möglich. Bitte suchen Sie im Bestand eines bestimmten VBs oder nicht phonetisch.",
			"", "", new String[] { "Ok" });

	/**
	 * Suchparameter ungültig
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.025
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_025 = new Message(
			"03.01.001.025",
			MessageType.ERROR,
			"Suchparameter ungültig",
			"Für die Suche nach Vertragsnummer sind die Partnergesellschaft und die zur Gesellschaft passende Vertragsnummer notwendig.",
			"", "", new String[] { "Ok" });

	/**
	 * Keine Ergebnisse
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.026
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_026 = new Message("03.01.001.026", MessageType.INFO, "Keine Ergebnisse",
			"Die gesuchte Vertragsnummer wurde in Kombination mit der Partnergesellschaft nicht gefunden", "", "",
			new String[] { "OK" });

	/**
	 * Gelöschte Nachrichten wurden in den Papierkorb verschoben
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Beispiel
	 * @Bereich Beispiel
	 * @Autor RDE
	 * @Dokument
	 * @Name Beispiel: Informationsmeldung
	 * @Beschreibung Beispiel einer Informationsmeldung
	 * @Bemerkung
	 * @MEX 99.99.001.001
	 * @StatusRE Entwurf
	 * @StatusFA ungeprüft
	 * @StatusDEV Neu
	 * @StatusIAD ungeprüft
	 */
	Message MEX_99_99_001_001 = new Message("99.99.001.001", MessageType.INFO,
			"Gelöschte Nachrichten wurden in den Papierkorb verschoben", "",
			"Um sie endgültig zu Löschen, müssen Sie diese Nachrichten aus\n" + "dem Papierkorb löschen.", "",
			new String[] { "OK" });

	/**
	 * Der Straßenname ist unbekannt
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Beispiel
	 * @Bereich Beispiel
	 * @Autor RDE
	 * @Dokument
	 * @Name Beispiel: Fragemeldung 1
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 99.99.001.002
	 * @StatusRE Entwurf
	 * @StatusFA ungeprüft
	 * @StatusDEV Neu
	 * @StatusIAD ungeprüft
	 */
	Message MEX_99_99_001_002 = new Message("99.99.001.002", MessageType.QUESTION, "Der Straßenname ist unbekannt",
			"Der Straßenname ist in Kombination mit der eingegebenen PLZ und dem Ort unbekannt.",
			"Unbekannte Adressen können zu Verzögerungen bei der Antragsbearbeitung führen.",
			"Möchten Sie den Straßennamen ändern?", new String[] { "Straßennamen ändern", "Straßennamen nicht ändern",
					"Abbrechen" });

	/**
	 * Gelöschte Akten können nicht wiederhergestellt werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Beispiel
	 * @Bereich Beispiel
	 * @Autor RDE
	 * @Dokument
	 * @Name Beispiel: Warnmeldung
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 99.99.001.003
	 * @StatusRE Entwurf
	 * @StatusFA ungeprüft
	 * @StatusDEV Neu
	 * @StatusIAD ungeprüft
	 */
	Message MEX_99_99_001_003 = new Message("99.99.001.003", MessageType.WARNING,
			"Gelöschte Akten können nicht wiederhergestellt werden",
			"Möchten Sie das Löschen der markierten Akten abbrechen?", "", "", new String[] { "Löschen abbrechen",
					"Löschen" });

	/**
	 * Es sind nicht alle Pflichtfelder ausgefüllt
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Beispiel
	 * @Bereich Beispiel
	 * @Autor RDE
	 * @Dokument
	 * @Name Beispiel: Fehlermeldung
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 99.99.001.004
	 * @StatusRE Entwurf
	 * @StatusFA ungeprüft
	 * @StatusDEV Neu
	 * @StatusIAD ungeprüft
	 */
	Message MEX_99_99_001_004 = new Message("99.99.001.004", MessageType.ERROR,
			"Es sind nicht alle Pflichtfelder ausgefüllt",
			"Um eine Akte speichern zu können, müssen alle Pflichtfelder ausgefüllt sein.		",
			"Nicht ausgefüllte Pflichtfelder sind gelb hinterlegt.",
			"Möchten Sie Ihre Eingaben jetzt vervollständigen",
			new String[] { "Eingabe vervollständigen", "Abbrechen" });

	/**
	 * Fehlende Daten
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich Druck
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name UC 03.01.058 Akte Drucken
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.058.001
	 * @StatusRE Entwurf
	 * @StatusFA ungeprüft
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_058_001 = new Message("03.01.058.001", MessageType.ERROR, "Fehlende Daten",
			"Ein Druckdokument konnte nicht erzeugt werden, weil keine Daten für Ihre Auswahl vorhanden sind.", "", "",
			new String[] { "Auswahl überprüfen" });

	/**
	 * Kein Vertrag ausgewählt
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich Druck
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name UC 03.01.058 Akte Drucken
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.058.002
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_058_002 = new Message("03.01.058.002", MessageType.ERROR, "Kein Vertrag ausgewählt",
			"Es wurden keine druckbaren Verträge gefunden.", "", "", new String[] { "Vertrag auswählen" });

	/**
	 * Technischer Fehler
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich Druck
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Technischer Fehler in der Entwicklungsphase
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.058.003
	 * @StatusRE Entwurf
	 * @StatusFA ungeprüft
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_058_003 = new Message("03.01.058.003", MessageType.ERROR, "Technischer Fehler",
			"Das Druckdokument konnte aufgrund eines technischen Fehlers nicht erzeugt werden.", "", "",
			new String[] { "Ok" });

	/**
	 * @deprecated Keine Person ausgewählt
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich Druck
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name UC 03.01.058 Akte Drucken
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.058.004
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_058_004 = new Message("03.01.058.004", MessageType.ERROR, "Keine Person ausgewählt",
			"Der Druckauftrag könnte nicht ausgeführt werden, da keine Person(en) ausgewählt wurde(n).", "", "",
			new String[] { "Person auswählen" });

	/**
	 * Acrobat Reader nicht gefunden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich Druck
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Acrobat Reader nicht gefunden
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.058.005
	 * @StatusRE Entwurf
	 * @StatusFA ungeprüft
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_058_005 = new Message("03.01.058.005", MessageType.ERROR,
			"Die Funktion kann nicht ausgeführt werden",
			"Acrobat Reader ist nicht vorhanden oder nicht korrekt installiert.", "", "", new String[] { "Ok" });

	/**
	 * Acrobat Reader nicht gefunden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich Druck
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Acrobat Reader nicht gefunden
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.058.006
	 * @StatusRE Entwurf
	 * @StatusFA ungeprüft
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_058_006 = new Message("03.01.058.006", MessageType.ERROR,
			"Die Erstellung des Druckdokumentes konnte nicht gestartet werden", "{0}", "",
			"Möchten Sie Ihre Eingaben jetzt vervollständigen?",
			new String[] { "Eingabe vervollständigen", "Abbrechen" });

	/**
	 * Gelöschte Bemerkungen gehen unwiederbringlich verloren
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich Bemerkungen
	 * @Autor UHO
	 * @Dokument UseCase
	 * @Name Bemerkungen bearbeiten
	 * @Beschreibung Der Aktuer hat die Möglichkeit in der Akte Bemerkungen zu
	 *               erfassen und zu löschen. Dies ist die Nachfrage beim
	 *               Löschen.
	 * @Bemerkung
	 * @MEX 03.01.059.001
	 * @StatusRE OK
	 * @StatusFA OK
	 * @StatusDEV Neu
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_059_001 = new Message("03.01.059.001", MessageType.WARNING,
			"Gelöschte Bemerkungen gehen unwiederbringlich verloren", "Möchten Sie die Bemerkung dennoch löschen ?",
			"", "", new String[] { "Bemerkungen behalten", "Abbrechen", "Bemerkung löschen" });

	/**
	 * Die Bemerkungen wurden erfolgreich gespeichert
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich Bemerkungen
	 * @Autor UHO
	 * @Dokument UseCase
	 * @Name Bemerkungen bearbeiten
	 * @Beschreibung Der Aktuer hat die Möglichkeit in der Akte Bemerkungen zu
	 *               erfassen und zu speichern. Dies ist die Erfolgsmeldung beim
	 *               Speichern.
	 * @Bemerkung
	 * @MEX 03.01.059.002
	 * @StatusRE OK
	 * @StatusFA OK
	 * @StatusDEV Neu
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_059_002 = new Message("03.01.059.002", MessageType.INFO,
			"Die Bemerkungen wurden erfolgreich gespeichert", "", "", "", new String[] { "OK" });

	/**
	 * Der aktuelle Bearbeitungsstand der Bemerkungen ist nicht gespeichert
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich Bemerkungen
	 * @Autor UHO
	 * @Dokument UseCase
	 * @Name Bemerkungen bearbeiten
	 * @Beschreibung Beenden der Bearbeitung der Bemerkungen. Der aktuelle
	 *               BEarbeiungsstand ist nicht gespeichert.
	 * @Bemerkung
	 * @MEX 03.01.059.003
	 * @StatusRE OK
	 * @StatusFA OK
	 * @StatusDEV Neu
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_059_003 = new Message("03.01.059.003", MessageType.QUESTION,
			"Der aktuelle Bearbeitungsstand der Bemerkungen ist nicht gespeichert",
			"Sie haben das Beenden gewählt. Wenn Sie die Bemerkungen nicht speichern, gehen die\n"
					+ "Änderungen verloren. ", "",
			"Möchten Sie die Bemerkungen jetzt speichern oder die Bearbeitung der Bemerkungen\n"
					+ "ohne Speichern beenden?\n" + "\n", new String[] { "Bemerkungen speichern und beenden",
					"Bemerkungen nicht speichern und beenden", "Abbrechen" });

	/**
	 * Bitte überprüfen Sie ihre Eingabe.
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich DatenerfassungPerson
	 * @Autor DKI
	 * @Dokument UseCase
	 * @Name 03.01.006
	 * @Beschreibung Bitte überprüfen Sie ihre Eingabe.
	 * @Bemerkung
	 * @MEX 03.01.006.001
	 * @StatusRE Entwurf
	 * @StatusFA ungeprüft
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_006_001 = new Message("03.01.006.001", MessageType.INFO, "Bitte überprüfen Sie ihre Eingabe.",
			"", "", "", new String[] { "OK" });

	/**
	 * Fehler in der Suche
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich Suche
	 * @Autor DKI
	 * @Dokument UseCase
	 * @Name 03.001
	 * @Beschreibung Valide suchanfrage eines Innendienstmitarbeiters
	 * @Bemerkung USE CASE Akte Suchen 03.001 Absch. 6.9: mind 2 Buchstaben Name
	 *            und Vorname müssen bestimmt werden
	 * @MEX 03.01.001.010
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_010 = new Message("03.01.001.010", MessageType.INFO, "Fehler in der Suche",
			"Bitte geben sie mindestens zwei Buchstaben für den Namen an", "", "", new String[] { "OK" });

	/**
	 * BLZ unbekannt.
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich Bank
	 * @Autor DKI
	 * @Dokument UseCase
	 * @Name 03.01.006
	 * @Beschreibung
	 * @Bemerkung Eigentlich MEX 03.01.002.001 diese ist aber bereits belegt
	 *            gewesen
	 * @MEX 03.01.002.101
	 * @StatusRE Entwurf
	 * @StatusFA ungeprüft
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_101 = new Message("03.01.002.101", MessageType.INFO, "BLZ unbekannt.",
			"Zu der eingegebenen BLZ konnte keine Bank ermittelt werden.", "", "", new String[] { "OK" });

	/**
	 * Geänderte Haushaltsadresse liegt bereits als zusätzliche Personenadresse
	 * vor.
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich DatenerfassungPerson
	 * @Autor DKI
	 * @Dokument UseCase
	 * @Name Haushalt bearbeiten 03.01.006
	 * @Beschreibung Geänderte HH Adresse liegt bereits als Zusatzadresse vor
	 * @Bemerkung
	 * @MEX 03.01.006.002
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_006_002 = new Message("03.01.006.002", MessageType.INFO,
			"Geänderte Haushaltsadresse liegt bereits als zusätzliche Personenadresse vor.",
			"Die entsprechende zusätzliche Personenadresse wird gelöscht.", "", "", new String[] { "OK" });

	/**
	 * Eingabedaten für Person können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Person, Anrede
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.001
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_001 = new Message("03.01.002.001", MessageType.ERROR,
			"Eingabedaten für Person können nicht übernommen werden",
			"Anrede fehlt, bitte füllen Sie alle gelb markierten Pflichtfelder.", "", "", new String[] {
					"Eingabe ergänzen", "Abbrechen" });

	/**
	 * Eingabedaten für Person können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.002
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_002 = new Message("03.01.002.002", MessageType.ERROR,
			"Eingabedaten für Person können nicht übernommen werden",
			"Name fehlt, bitte füllen Sie alle gelb markierten Pflichtfelder.", "", "", new String[] {
					"Eingabe ergänzen", "Abbrechen" });

	/**
	 * Eingabedaten für Person können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Person, Vorname
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.003
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_003 = new Message("03.01.002.003", MessageType.ERROR,
			"Eingabedaten für Person können nicht übernommen werden",
			"Vorname fehlt, bitte füllen Sie alle gelb markierten Pflichtfelder.", "", "", new String[] {
					"Eingabe ergänzen", "Abbrechen" });

	/**
	 * Eingabedaten für Person können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Person, Geburtsdatum
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.004
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_004 = new Message("03.01.002.004", MessageType.ERROR, "Das eingegebene Datum ist ungültig",
			"Bitte geben Sie ein gültiges Datum ein.", "", "", new String[] { "Eingabe ändern", "Ignorieren" });

	/**
	 * Eingabedaten für Kommunikation können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Kommunikationsnummer
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.005
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV geändert
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_005 = new Message("03.01.002.005", MessageType.ERROR,
			"Eingabedaten für Kommunikation können nicht übernommen werden",
			"Nummer fehlt, bitte füllen Sie alle gelb markierten Pflichtfelder.", "", "", new String[] {
					"Eingabe ändern", "Abbrechen" });

	/**
	 * Eingabedaten für Kommunikation können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Kommunikationsart
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.006
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_006 = new Message("03.01.002.006", MessageType.ERROR,
			"Eingabedaten für Kommunikation können nicht übernommen werden",
			"Sie haben eine Kommunikationsart ausgewählt, für diese aber keine oder ungültige Daten eingegeben.", "",
			"", new String[] { "Eingabe ändern", "Abbrechen" });

	/**
	 * Eingabedaten für Bank können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Bank, Kontonummer
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.007
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_007 = new Message("03.01.002.007", MessageType.ERROR,
			"Eingabedaten für Bank können nicht übernommen werden",
			"Kontonummer fehlt, bitte füllen Sie alle gelb markierten Pflichtfelder.", "", "", new String[] {
					"Eingabe ergänzen", "Abbrechen" });

	/**
	 * Eingabedaten für Steuerberater können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Steuerberater, Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.008
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_008 = new Message("03.01.002.008", MessageType.ERROR,
			"Eingabedaten für Steuerberater können nicht übernommen werden",
			"Das Feld \"Name\" ist leer. Bitte geben Sie einen Namen ein.", "", "", new String[] { "Namen eingeben",
					"Abbrechen" });

	/**
	 * Eingabedaten für Steuerberater können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Steuerberater, ungültige Telefonnummer
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.009
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_009 = new Message("03.01.002.009", MessageType.ERROR,
			"Eingabedaten für Steuerberater können nicht übernommen werden",
			"Ungültige Telefonnummer, bitte ändern Sie Ihre Eingabe.", "", "", new String[] { "Eingabe ändern",
					"Abbrechen" });

	/**
	 * Eingabedaten für Hobby können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Hobby, fehlt
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.010
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_010 = new Message("03.01.002.010", MessageType.ERROR,
			"Eingabedaten für Hobby können nicht übernommen werden",
			"Das Feld \"Hobby\" ist leer. Bitte geben Sie ein Hobby ein.", "", "", new String[] { "Eingabe ergänzen",
					"Abbrechen" });

	/**
	 * Eingabedaten für Arbeitgeber können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Arbeitgeber, Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.011
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_011 = new Message("03.01.002.011", MessageType.ERROR,
			"Eingabedaten für Arbeitgeber können nicht übernommen werden",
			"Das Feld \"Name\" ist leer. Bitte geben Sie einen Namen ein.", "", "", new String[] { "Namen eingeben",
					"Abbrechen" });

	/**
	 * Eingabedaten für Beruf - Rente können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Beruf/Rente, Berufsgruppe
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.012
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_012 = new Message("03.01.002.012", MessageType.ERROR,
			"Eingabedaten für Beruf - Rente können nicht übernommen werden", "Bitte wählen Sie eine Berufsgruppe aus.",
			"", "", new String[] { "Berufsgruppe auswählen", "Abbrechen" });

	/**
	 * Eingabedaten für Beruf - Rente können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Rente/Beruf, Derzeitige Tätigkeit ungültig
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.013
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_013 = new Message("03.01.002.013", MessageType.ERROR,
			"Eingabedaten für Beruf - Rente können nicht übernommen werden",
			"\"Derzeitige Tätigkeit\" ist ungültig. Bitte ändern Sie Ihre Eingabe.", "", "", new String[] {
					"Eingabe ändern", "Abbrechen" });

	/**
	 * Eingabedaten für Beruf - Rente können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Beruf/Rente, Nebenberuf
	 * @Beschreibung
	 * @Bemerkung Diese Meldung konnte ich bislang nicht provozieren. Auch
	 *            völliger Blödsinn wurde übernommen, selbst Zahlen und
	 *            Fragezeichen. Wann ist dann aber der Nebenberuf ungültig? AKT
	 * @MEX 03.01.002.014
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_014 = new Message("03.01.002.014", MessageType.ERROR,
			"Eingabedaten für Beruf - Rente können nicht übernommen werden",
			"Ungültiger \"Nebenberuf\", bitte geben Sie einen gültigen Nebenberuf ein.", "", "", new String[] {
					"Eingabe ändern", "Abbrechen" });

	/**
	 * Eingabedaten für Beruf - Rente können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Rente/Beruf, Datum für Eintritt ins Berufsleben
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.015
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_015 = new Message("03.01.002.015", MessageType.ERROR,
			"Eingabedaten für Beruf - Rente können nicht übernommen werden",
			"Datum für \"Eintritt ins Berufsleben\" ungültig, bitte ändern Sie Ihre Eingabe.", "", "", new String[] {
					"Eingabe ändern", "Abbrechen" });

	/**
	 * Eingabedaten für Beruf - Rente können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Rente/Beruf, Datum für Eintritt ins Beamtenverhältnis
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.016
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_016 = new Message("03.01.002.016", MessageType.ERROR,
			"Eingabedaten für Beruf - Rente können nicht übernommen werden",
			"Datum für \"Eintritt ins Beamtenverhältnis\" ungültig, bitte ändern Sie Ihre Eingabe.", "", "",
			new String[] { "Eingabe ändern", "Abbrechen" });

	/**
	 * Eingabedaten für Personreferenz können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Personreferenz, Ausweisart
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.017
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_017 = new Message("03.01.002.017", MessageType.ERROR,
			"Eingabedaten für Personreferenz können nicht übernommen werden",
			"Ausweisart fehlt, bitte wählen Sie eine Ausweisart.", "", "", new String[] { "Ausweisart wählen",
					"Abbrechen" });

	/**
	 * Eingabedaten für Personreferenz können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Personreferenz, Ausweisnummer fehlt
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.018
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_018 = new Message("03.01.002.018", MessageType.ERROR,
			"Eingabedaten für Personreferenz können nicht übernommen werden",
			"Ausweisnummer fehlt, bitte geben Sie eine Ausweisnummer ein.", "", "", new String[] {
					"Ausweisnummer eingeben", "Abbrechen" });

	/**
	 * Eingabedaten für Personreferenz können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Personreferenz, Ausgestellt am Datum falsch
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.019
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_019 = new Message("03.01.002.019", MessageType.ERROR,
			"Eingabedaten für Personreferenz können nicht übernommen werden",
			"Datum für \"Ausgestellt am\" ungültig, bitte ändern Sie Ihre Eingabe.", "", "", new String[] {
					"Eingabe ändern", "Abbrechen" });

	/**
	 * Eingabedaten für Personreferenz können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Personreferenz, Ausgestellt bis Datum falsch
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.020
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_020 = new Message("03.01.002.020", MessageType.ERROR,
			"Eingabedaten für Personreferenz können nicht übernommen werden",
			"Datum für \"Gültig bis\" ungültig, bitte ändern Sie Ihre Eingabe.", "", "", new String[] {
					"Eingabe ändern", "Abbrechen" });

	/**
	 * Eingabedaten für Zusatzinfo können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Zusatzinfo, Zusatzinfokategorie nicht gesetzt
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.021
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_021 = new Message("03.01.002.021", MessageType.ERROR,
			"Eingabedaten für Zusatzinfo können nicht übernommen werden",
			"\"Zusatzinfo-Gruppe\" fehlt, bitte wählen Sie eine Zusatzinfo-Gruppe aus.", "", "", new String[] {
					"Eingabe ergänzen", "Abbrechen" });

	/**
	 * Eingabedaten für Zusatzinfo können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Zusatzinfo, Zusatzinfo nicht befüllt
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.022
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_022 = new Message("03.01.002.022", MessageType.ERROR,
			"Eingabedaten für Zusatzinfo können nicht übernommen werden",
			"\"Zusatzinfo fehlt\", bitte geben Sie eine Zusatzinfo ein.", "", "", new String[] { "Eingabe ergänzen",
					"Abbrechen" });

	/**
	 * Eingabedaten für Verein können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich DatenerfassungPerson
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Verein MasterDetail Prüfung
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.023
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_023 = new Message("03.01.002.023", MessageType.ERROR,
			"Eingabedaten für Verein können nicht übernommen werden",
			"Das Feld \"Verein\" ist leer. Bitte füllen Sie das Feld  \"Verein\".", "", "", new String[] {
					"Eingabe ergänzen", "Abbrechen" });

	/**
	 * Eingabedaten für Bemerkung können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich DatenerfassungPerson
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Verein MasterDetail Prüfung
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.024
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_024 = new Message("03.01.002.024", MessageType.ERROR,
			"Eingabedaten für Bemerkung können nicht übernommen werden",
			"Das Feld \"Bemerkungen\" ist leer. Bitte geben Sie Ihre Bemerkungen ein.", "", "", new String[] {
					"Eingabe ergänzen", "Abbrechen" });

	/**
	 * Eingabedaten für Ziele können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich DatenerfassungPerson
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Ziele MasterDetail Prüfung
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.025
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_025 = new Message("03.01.002.025", MessageType.ERROR,
			"Eingabedaten für Ziele können nicht übernommen werden",
			"Zeitraum fehlt, bitte geben Sie einen Zeitraum ein.", "", "", new String[] { "Zeitraum angeben",
					"Abbrechen" });

	/**
	 * Eingabedaten für Ziele können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich DatenerfassungPerson
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Verein MasterDetail Prüfung
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.026
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_026 = new Message("03.01.002.026", MessageType.ERROR,
			"Eingabedaten für Ziele können nicht übernommen werden",
			"Das Feld \"Wünsche\" ist leer. Bitte geben Sie mindestens einen Wunsch ein.", "", "", new String[] {
					"Wünsche eingeben", "Abbrechen" });

	/**
	 * Eingabedaten für Ziele können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich DatenerfassungPerson
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Verein MasterDetail Prüfung
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.027
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_027 = new Message("03.01.002.027", MessageType.ERROR,
			"Eingabedaten für Ziele können nicht übernommen werden",
			"Datum ungültig, bitte geben Sie ein gültiges Datum ein.", "", "", new String[] { "Datum ändern",
					"Abbrechen" });

	/**
	 * Eingabedaten für Sparbetrag können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich DatenerfassungPerson
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Sparbetrag MasterDetail Prüfung
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.028
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_028 = new Message("03.01.002.028", MessageType.ERROR,
			"Eingabedaten für Sparbetrag können nicht übernommen werden",
			"Betrag fehlt, bitte geben Sie mindestens einen Sparbetrag ein.", "", "", new String[] {
					"Sparbetrag eingeben", "Abbrechen" });

	/**
	 * Eingabedaten für Einnahmen können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich DatenerfassungPerson
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Sparbetrag MasterDetail Prüfung
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.029
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_029 = new Message("03.01.002.029", MessageType.ERROR,
			"Eingabedaten für Einnahmen können nicht übernommen werden",
			"Betrag fehlt, bitte geben Sie mindestens einen Betrag ein.", "", "", new String[] { "Betrag eingeben",
					"Abbrechen" });

	/**
	 * Keine Beträge für Miete vorhanden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich DatenerfassungPerson
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Miete MasterDetail Prüfung
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.030
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_030 = new Message("03.01.002.030", MessageType.ERROR, "Keine Beträge für Miete vorhanden",
			"Bitte füllen Sie die Felder für Miete mit den entsprechenden Beträgen.", "", "", new String[] {
					"Eingabe ergänzen", "Abbrechen" });

	/**
	 * Eingabedaten für Wunschimmobilie können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich DatenerfassungPerson
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Miete MasterDetail Prüfung
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.031
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_031 = new Message("03.01.002.031", MessageType.ERROR,
			"Eingabedaten für Wunschimmobilie können nicht übernommen werden",
			"Ungültiger Zeitpunkt, bitte geben Sie Monat und Jahr ein.", "", "", new String[] { "Zeitpunkt ändern",
					"Abbrechen" });

	/**
	 * Falsches "Kauf/Fertigstellung am" Datum bei Immobilieneigentum
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich DatenerfassungPerson
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Miete MasterDetail Prüfung
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.032
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_032 = new Message("03.01.002.032", MessageType.ERROR,
			"Falsches \"Kauf/Fertigstellung am\" Datum bei Immobilieneigentum",
			"Bitte geben Sie ein gültiges \"Kauf/Fertigstellung am\" Datum ein.", "", "", new String[] {
					"Eingabe korrigieren", "Abbrechen" });

	/**
	 * Eingabedaten für Immobilieneigentum können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich DatenerfassungPerson
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Miete MasterDetail Prüfung
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.033
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_033 = new Message("03.01.002.033", MessageType.ERROR,
			"Eingabedaten für Immobilieneigentum können nicht übernommen werden",
			"Es wurde ein ungültiger Eigenheimzulage/Kinderzulage Beginn eingegeben.", "",
			"Bitte ändern Sie das Datum entsprechend.", new String[] { "Eingabe ändern", "Abbrechen" });

	/**
	 * Eingabedaten für Finanzierung können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich DatenerfassungPerson
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Immobilie MasterDetail Prüfung
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.034
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_034 = new Message("03.01.002.034", MessageType.ERROR,
			"Eingabedaten für Finanzierung können nicht übernommen werden",
			"Immobilie fehlt, bitte wählen Sie eine Immobilie aus.", "", "", new String[] { "Immobilie auswählen",
					"Abbrechen" });

	/**
	 * Eingabedaten für Finanzierung können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich DatenerfassungPerson
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Finanzierung MasterDetail Prüfung
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.035
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_035 = new Message("03.01.002.035", MessageType.ERROR,
			"Eingabedaten für Finanzierung können nicht übernommen werden",
			"Institut fehlt, bitte tragen Sie ein Institut ein.", "", "", new String[] { "Institut eintragen",
					"Abbrechen" });

	/**
	 * Eingabedaten für Finanzierung können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich DatenerfassungPerson
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Finanzierung MasterDetail Prüfung
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.036
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_036 = new Message("03.01.002.036", MessageType.ERROR,
			"Eingabedaten für Finanzierung können nicht übernommen werden",
			"Datum für Wiedervorlage ungültig, bitte korrigieren Sie das Datum.", "", "", new String[] {
					"Datum korrigieren", "Abbrechen" });

	/**
	 * Eingabedaten für Finanzierung können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich DatenerfassungPerson
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Finanzierung MasterDetail Prüfung
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.037
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_037 = new Message("03.01.002.037", MessageType.ERROR,
			"Eingabedaten für Finanzierung können nicht übernommen werden",
			"Datum für \"Festschreibung bis\" ungültig, bitte korrigieren Sie das Datum.", "", "", new String[] {
					"Datum korrigieren", "Abbrechen" });

	/**
	 * Eingabedaten für Einkommen können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich DatenerfassungPerson
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Einkommen MasterDetail Prüfung
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.038
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_038 = new Message("03.01.002.038", MessageType.ERROR,
			"Eingabedaten für Einkommen können nicht übernommen werden",
			"\"Anzahl Kinder-/ Erziehungsfreibeträge\" ungültig, bitte korrigieren Sie Ihre Eingabe.", "", "",
			new String[] { "Anzahl Kinder / Erziehungsfreibeträge korrigieren", "Abbrechen" });

	/**
	 * Eingabedaten für Einkommen können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich DatenerfassungPerson
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Einkommen MasterDetail Prüfung
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.039
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_039 = new Message("03.01.002.039", MessageType.ERROR,
			"Eingabedaten für Einkommen können nicht übernommen werden",
			"Einkommensart fehlt, bitte wählen Sie eine Einkommensart.", "", "", new String[] { "Einkommensart wählen",
					"Abbrechen" });

	/**
	 * Eingabedaten für Einkommen können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich DatenerfassungPerson
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Einkommen MasterDetail Prüfung
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.040
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_040 = new Message("03.01.002.040", MessageType.ERROR,
			"Eingabedaten für Einkommen können nicht übernommen werden", "Es wurde kein Betrag eingegeben.", "",
			"Bitte füllen Sie dieses Pflichtfeld.", new String[] { "Eingabe ergänzen", "Abbrechen" });

	/**
	 * Eingabedaten für Anlageerfahrung können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich DatenerfassungPerson
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Einkommen MasterDetail Prüfung
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.041
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_041 = new Message("03.01.002.041", MessageType.ERROR,
			"Eingabedaten für Anlageerfahrung können nicht übernommen werden", "", "",
			"Feld für Anlageerfahrung ist leer, bitte wählen Sie eine Art der Anlageerfahrung.", new String[] {
					"Anlageerfahrung wählen", "Abbrechen" });

	/**
	 * Eingabedaten für WpHG (Wertpapierhandelsgesetz) können nicht übernommen
	 * werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich DatenerfassungPerson
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Einkommen MasterDetail Prüfung
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.042
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_042 = new Message(
			"03.01.002.042",
			MessageType.ERROR,
			"Eingabedaten für WpHG (Wertpapierhandelsgesetz) können nicht übernommen werden",
			"\"Umfang je Auftrag/Order\", \"Verfolgte Anlageziele\", \"max. gewünschte Risikoklasse\" und \"gewünschte Anlagedauer\" müssen eingegeben werden.",
			"", "Bitte vervollständigen Sie Ihre Eingaben.", new String[] { "Eingaben vervollständigen", "Abbrechen" });

	/**
	 * Eingabedaten für Personreferenz können nicht übernommen werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor TBA
	 * @Dokument UseCase
	 * @Name Personreferenz, Gültig bis Datum falsch
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.002.043
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_002_043 = new Message(
			"03.01.002.043",
			MessageType.ERROR,
			//"Eingabedaten für Personreferenz können nicht übernommen werden",
			"Sie haben ein Datum aus der Vergangenheit eingegeben.",
			"Bitte überprüfen Sie ihre Eingabe. Ausweisdaten, die in der Vergangenheit liegen, sind nicht gültig und können zu Problemen bei der Kundenberatung führen.",
			"", "", new String[] { "OK", "Abbrechen" });

	/**
	 * Adressänderung wurde nicht angenommen
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich Eigenvertragsadressaenderung
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Eingegebene Adressinformationen identisch zu ausgewaehlten
	 *       Vertraegen
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.006.003
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_006_003 = new Message("03.01.006.003", MessageType.ERROR,
			"Adressänderung wurde nicht angenommen", "", "",
			"Die eingegebene Adresse ist identisch mit den Adressen der ausgewählten Verträge.", new String[] {
					"Eingaben ändern", "Abbrechen" });

	/**
	 * Adressänderung wird für folgende Verträge nicht angestoßen
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich Eigenvertragsadressaenderung
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Eine Teilmenge der ausgewählten Vertäge hat die gleiche Adresse
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.006.004
	 * @StatusRE Entwurf
	 * @StatusFA ungeprüft
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_006_004 = new Message("03.01.006.004", MessageType.INFO,
			"Adressänderung wird für folgende Verträge nicht angestoßen", "{0}", "",
			"Die eingegeben Adresse ist identisch mit den Adressen der oben aufgeführten Verträge.",
			new String[] { "OK" });

	/**
	 * Sie haben Änderungen an Vertragsadressen vorgenommen
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich Eigenvertragsadressaenderung
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Bestaetigung der Adressänderung
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.006.005
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_006_005 = new Message("03.01.006.005", MessageType.QUESTION,
			"Sie haben Änderungen an Vertragsadressen vorgenommen", "", "",
			"Möchten Sie die Änderungen endgültig übernehmen?", new String[] { "Änderungen übernehmen", "Abbrechen" });

	/**
	 * Sie haben die Haushaltsadresse geändert
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich Eigenvertragsadressaenderung
	 * @Autor HBE
	 * @Dokument UseCase
	 * @Name Starten der Vertragsadressänderung
	 * @Beschreibung
	 * @Bemerkung Text unklar: Ich habe eine Haushaltadresse geändert und werde
	 *            gefragt, ob diese Änderung auf die Eigenverträge des
	 *            Haushaltes übernommen werden soll. Bis hierhin ist alles klar.
	 *            Was aber soll dieser Hinweis? Er verwirrt mich. Ich muss doch
	 *            nur sagen: "Änderung übernehmen" oder "Abbrechen" (speichern
	 *            halte ich hier für den falschen Begriff). Gehe ich also auf
	 *            "Änderung übernehmen", werden die Änderungen für die
	 *            Eigenverträge übernommen (gespeichert). Was hat das Aufrufen
	 *            der Vertragsadressen damit zu tun? Die Änderungen werden doch
	 *            nicht einfach übernommen, nur weil ich die Vertragsadressen
	 *            aufrufe? Vielleicht soll der Hinweis ja so lauten: beim Aufruf
	 *            der Vertragsadressen werden die Änderungen in der Akte
	 *            angezeigt? AKT
	 * @MEX 03.01.006.006
	 * @StatusRE Entwurf
	 * @StatusFA ungeprüft
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_006_006 = new Message(
			"03.01.006.006",
			MessageType.QUESTION,
			"Die Haushaltsadresse wurde geändert",
			"Die Änderungen an der Haushaltsadresse werden nicht automatisch in die Eigenverträge übertragen.\n"
					+ "Möchten Sie sich die bestehenden Adressen der Eigenverträge des Haushaltes zum Ändern anzeigen lassen?",
			"Beim Aufruf der Vertragsadressen werden die Änderungen in der Akte gespeichert.", "", new String[] {
					"Speichern und Vertragsadressen anzeigen", "Abbrechen" });

	/**
	 * Sie haben die Haushaltsadresse geändert, aber schon gespeichert
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich Eigenvertragsadressaenderung
	 * @Autor HBE
	 * @Dokument UseCase
	 * @Name Starten der Vertragsadressänderung
	 * @Beschreibung
	 * @Bemerkung Text unklar: Ich habe eine Haushaltadresse geändert und werde
	 *            gefragt, ob diese Änderung auf die Eigenverträge des
	 *            Haushaltes übernommen werden soll. Bis hierhin ist alles klar.
	 *            Was aber soll dieser Hinweis? Er verwirrt mich. Ich muss doch
	 *            nur sagen: "Änderung übernehmen" oder "Abbrechen" (speichern
	 *            halte ich hier für den falschen Begriff). Gehe ich also auf
	 *            "Änderung übernehmen", werden die Änderungen für die
	 *            Eigenverträge übernommen (gespeichert). Was hat das Aufrufen
	 *            der Vertragsadressen damit zu tun? Die Änderungen werden doch
	 *            nicht einfach übernommen, nur weil ich die Vertragsadressen
	 *            aufrufe? Vielleicht soll der Hinweis ja so lauten: beim Aufruf
	 *            der Vertragsadressen werden die Änderungen in der Akte
	 *            angezeigt? AKT
	 * @MEX 03.01.006.012
	 * @StatusRE Entwurf
	 * @StatusFA ungeprüft
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_006_012 = new Message("03.01.006.012", MessageType.QUESTION,
			"Die Haushaltsadresse wurde geändert",
			"Die Änderungen an der Haushaltsadresse werden nicht automatisch in die Eigenverträge übertragen.", "",
			"Möchten Sie sich die bestehenden Adressen der Eigenverträge des Haushaltes zum Ändern anzeigen lassen?",
			new String[] { "Vertragsadressen anzeigen", "Abbrechen" });

	/**
	 * Sie haben Änderungen in der Akte vorgenommen
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich Eigenvertragsadressaenderung
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Starten der Vertragsadressänderung
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.006.007
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_006_007 = new Message(
			"03.01.006.007",
			MessageType.QUESTION,
			"Sie haben Änderungen in der Akte vorgenommen",
			"Bevor die Informationen zu den Vertragsadressen angezeigt werden können, müssen die Änderungen in der Akte gespeichert werden.",
			"", "Wollen Sie die Änderungen speichern und die Vertragsadressen anzeigen?", new String[] {
					"Speichern und Vertragsadressen anzeigen", "Abbrechen" });

	/**
	 * Adressänderung unvollständig
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich Eigenvertragsadressaenderung
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Eingegebene Adressinformationen nicht vollständig
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.006.008
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_006_008 = new Message("03.01.006.008", MessageType.ERROR, "Adressänderung unvollständig",
			"Eine unvollständige Adressänderung kann nicht übernommen werden.", "",
			"Bitte füllen Sie alle gelb markierten Pflichtfelder.", new String[] { "Eingaben ergänzen", "Abbrechen" });

	/**
	 * Vertragsadressen können nicht angezeigt oder geändert werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich Eigenvertragsadressaenderung
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Vertragsadressen nicht veränderbar
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.006.009
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_006_009 = new Message("03.01.006.009", MessageType.INFO,
			"Vertragsadressen können nicht angezeigt oder geändert werden",
			"Sie haben keine Berechtigung die Vertragsadressen angezeigen oder zu ändern.", "",
			"Bitte füllen Sie alle gelb markierten Pflichtfelder.", new String[] { "OK" });

	/**
	 * Vertragsadressen können nicht angezeigt oder geändert werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich Eigenvertragsadressaenderung
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Akten gesperrt
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.006.010
	 * @StatusRE Entwurf
	 * @StatusFA ungeprüft
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_006_010 = new Message("03.01.006.010", MessageType.INFO,
			"Vertragsadressen können nicht angezeigt oder geändert werden",
			"Alle Akten dieses Haushaltes sind gesperrt.", "",
			"Bitte führen Sie die Vertragsadressänderungen durch, wenn die Sperrung aufgehoben wurde.",
			new String[] { "OK" });

	/**
	 * Vertragsadressen können nicht angezeigt oder geändert werden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich Eigenvertragsadressaenderung
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Akte gesperrt
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.006.011
	 * @StatusRE Entwurf
	 * @StatusFA ungeprüft
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_006_011 = new Message("03.01.006.011", MessageType.INFO,
			"Nicht alle Vertragsadressen können angezeigt oder geändert werden",
			"Eine oder mehrere Akten dieses Haushaltes sind gesperrt.", "",
			"Bitte führen Sie die restlichen Vertragsadressänderungen durch, wenn die Sperrung aufgehoben wurde.",
			new String[] { "OK" });

	/**
	 * Die Änderung des Namens wirkt sich auf alle Kunden mit dieser Bewertung
	 * aus.
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich DatenerfassungPerson
	 * @Autor DKI
	 * @Dokument sonstiges
	 * @Name AenderungenWirkenSichAufAlleKundenAus
	 * @Beschreibung Style Guide " Bewertung von Kunden in der Akte "
	 * @Bemerkung
	 * @MEX 03.01.999.102
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD OK
	 */
	Message MEX_03_01_999_102 = new Message("03.01.999.102", MessageType.INFO,
			"Die Änderung des Namens wirkt sich auf alle Kunden mit dieser Bewertung aus.", "", "", "",
			new String[] { "OK" });

	/**
	 * Achtung: Änderungen an der Kategorie wirken sich auf alle Zusatzinfos
	 * aller Akten aus, die diese Kategorie verwenden.
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich DatenerfassungPerson
	 * @Autor DKI
	 * @Dokument sonstiges
	 * @Name AenderungenWirkenSichAufAlleKundenAus
	 * @Beschreibung Style Guide "Zusatzinfo Kategorien"
	 * @Bemerkung
	 * @MEX 03.01.999.103
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD OK
	 */
	Message MEX_03_01_999_103 = new Message(
			"03.01.999.103",
			MessageType.INFO,
			"Achtung: Änderungen an der Gruppe wirken sich auf alle Zusatzinfos aller Akten aus, die diese Gruppe verwenden.",
			"", "", "", new String[] { "OK" });

	/**
	 * Achtung: Durch das Löschen dieser Kategorie, werden auch alle
	 * Zusatzinfos, die diese Kategorie verwenden, in all Ihren Akten gelöscht.
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich DatenerfassungPerson
	 * @Autor DKI
	 * @Dokument sonstiges
	 * @Name LöschenWirktSichAufAlleKundenAus
	 * @Beschreibung Style Guide "Zusatzinfo Kategorien"
	 * @Bemerkung
	 * @MEX 03.01.999.104
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD OK
	 */
	Message MEX_03_01_999_104 = new Message(
			"03.01.999.104",
			MessageType.INFO,
			"Achtung: Durch das Löschen dieser Gruppe, werden auch alle Zusatzinfos, die diese Gruppe verwenden, in all Ihren Akten gelöscht.",
			"", "", "", new String[] { "OK" });

	/**
	 * Übernahme der Haushaltadresse fehlgeschlagen
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich Haushaltadresse
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Haushalt speichern
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.028.003
	 * @StatusRE Entwurf
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_028_003 = new Message("03.01.028.003", MessageType.ERROR,
			"Übernahme der Haushaltadresse fehlgeschlagen", "Bitte füllen Sie alle gelb markierten Pflichtfelder.", "",
			"", new String[] { "Adresse vervollständigen", "Abbrechen" });

	/**
	 * Freistellungsauftrag existiert bereits
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Vertrag
	 * @Bereich FSA
	 * @Autor UHO
	 * @Dokument UseCase
	 * @Name Fremdvertrag anlegen
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.033.001
	 * @StatusRE OK
	 * @StatusFA OK
	 * @StatusDEV Neu
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_033_001 = new Message("03.01.033.001", MessageType.ERROR,
			"Freistellungsauftrag existiert bereits",
			"Für $VornameDesKunden $NameDesKunden wurde bereits ein Freistellungsauftrag\n"
					+ "für die gleiche Gesellschaft angelegt. \n"
					+ "Ein weiterer Freistellungsauftrag für diese Gesellschaft kann nicht erstellt werden.", "",
			"Möchten Sie den bestehenden Freistellungsauftrag (FSA) bearbeiten?", new String[] {
					"Bestehenden FSA bearbeiten", "Abbrechen" });

	/**
	 * Es sind nicht alle Pflichtfelder ausgefüllt
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Vertrag
	 * @Bereich FSA
	 * @Autor UHO
	 * @Dokument UseCase
	 * @Name Fremdvertrag anlegen
	 * @Beschreibung
	 * @Bemerkung FSA wird in einem Dialogfenster angelegt. Meldung kommt, wenn
	 *            man den Dialogínhalt übernehmen will.
	 * @MEX 03.01.033.002
	 * @StatusRE OK
	 * @StatusFA OK
	 * @StatusDEV Neu
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_033_002 = new Message("03.01.033.002", MessageType.ERROR,
			"Es sind nicht alle Pflichtfelder ausgefüllt",
			"Um einen Freistellungsauftrag anlegen zu können, müssen alle Pflichtfelder\n" + "ausgefüllt sein.		", "",
			"Nicht ausgefüllte Pflichtfelder sind gelb markiert.", new String[] { "OK" });

	/**
	 * Die Ortsbezeichnung darf maximal 30 Zeichen lang sein.
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.105
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_105 = new Message("03.01.999.105", MessageType.INFO,
			"Die Ortsbezeichnung darf maximal 30 Zeichen lang sein.", "", "", "", new String[] { "OK" });

	/**
	 * Achtung: Das Löschen einer Bewertung wirkt sich auf alle Akten aus, die
	 * diese Bewertung verwenden.
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.106
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_106 = new Message("03.01.999.106", MessageType.INFO,
			"Achtung: Das Löschen einer Bewertung wirkt sich auf alle Akten aus, die diese Bewertung verwenden.", "",
			"", "", new String[] { "OK" });

	/**
	 * Es sind keine Fonds für den Entnahmeplan vorhanden. Bitte Kaufart auf
	 * 'Betrag in vollen €' setzen
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.107
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_107 = new Message("03.01.999.107", MessageType.INFO, "Serviceauftrag - Deutsche Bank",
			"Es sind keine Fonds für den Entnahmeplan vorhanden. Bitte Kaufart auf \'Betrag in vollen €\' setzen",
			"Bitte überprüfen Sie Ihre Eingabe", "", new String[] { "OK" });

	/**
	 * Bitte vollen Betrag (mind. 50 Euro) eingeben
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.108
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_108 = new Message("03.01.999.108", MessageType.INFO, "Serviceauftrag - Deutsche Bank",
			"Bitte vollen Betrag (mind. 50 Euro) eingeben", "Bitte überprüfen Sie Ihre Eingabe", "",
			new String[] { "OK" });

	/**
	 * Bitte wählen Sie einen Fonds aus
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.109
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_109 = new Message("03.01.999.109", MessageType.INFO, "Serviceauftrag - Deutsche Bank",
			"Eine Übernahme ist nicht möglich; es wurde kein Fond ausgewählt.", "Bitte wählen Sie einen Fonds aus", "",
			new String[] { "OK" });

	/**
	 * Es muß ein Betrag eingegeben werden
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.110
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_110 = new Message("03.01.999.110", MessageType.INFO, "Serviceauftrag - Deutsche Bank",
			"Eine Übernahme ist nicht möglich; es wurde kein Betrag eingegeben.",
			"Es muß ein Betrag eingegeben werden", "", new String[] { "OK" });

	/**
	 * Der Entnahmewert darf maximal $depotwert betragen
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.111
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_111 = new Message("03.01.999.111", MessageType.INFO, "Serviceauftrag - Deutsche Bank",
			"Der Entnahmewert darf maximal $depotwert betragen", "Korrigieren Sie bitte Ihre Eingabe.", "",
			new String[] { "OK" });

	/**
	 * Der Mindestanlagebetrag für Erst-Einmalzahlungen beträgt bei dieser
	 * Gesellschaft $mindestbetrag Euro
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.112
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_112 = new Message("03.01.999.112", MessageType.INFO, "Serviceauftrag - Deutsche Bank",
			"Der Mindestanlagebetrag für Erst-Einmalzahlungen beträgt bei dieser Gesellschaft $mindestbetrag Euro",
			"Bitte korrigieren Sie Ihre Eingabe.", "", new String[] { "OK" });

	/**
	 * Bitte wählen Sie einen Einzug ein
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.113
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_113 = new Message("03.01.999.113", MessageType.INFO, "Serviceauftrag - Deutsche Bank",
			"Eine Übernahme ist nicht möglich; es wurde kein Einzug ausgewählt.", "Bitte wählen Sie einen Einzug ein",
			"", new String[] { "OK" });

	/**
	 * Bei Beträgen über EUR 50.000,00 muss eine Überweisung erfolgen.
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.114
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_114 = new Message("03.01.999.114", MessageType.INFO, "Serviceauftrag - Deutsche Bank",
			"Bei Beträgen über EUR 50.000,00 muss eine Überweisung erfolgen. ", "Setzen Sie den Einzug auf NEIN", "",
			new String[] { "OK" });

	/**
	 * Der Fonds $fondsbezeichnung wurde bereits erfasst
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.115
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_115 = new Message("03.01.999.115", MessageType.INFO, "Serviceauftrag - Deutsche Bank",
			"Der Fonds $fondsbezeichnung wurde bereits erfasst", "Korrigieren Sie bitte Ihre Eingabe", "",
			new String[] { "OK" });

	/**
	 * Es kann nur ein Kauf mittels 'Verkaufserlös' getätigt werden! Für weitere
	 * Käufe aus 'Verkaufserlös' füllen Sie bitte einen weiteren Service-Auftrag
	 * aus.
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.116
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_116 = new Message("03.01.999.116", MessageType.INFO, "Serviceauftrag - Deutsche Bank",
			"Es kann nur ein Kauf mittels \'Verkaufserlös\' getätigt werden!",
			"Für weitere Käufe aus \'Verkaufserlös\' füllen Sie bitte einen weiteren Service-Auftrag aus.", "",
			new String[] { "OK" });

	/**
	 * Es kann nur ein Kauf mittels 'Rest aus Verkaufserlös' getätigt werden!
	 * Für weitere Käufe aus 'Rest aus Verkaufserlös' füllen Sie bitte einen
	 * weiteren Service-Auftrag aus.
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.117
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_117 = new Message("03.01.999.117", MessageType.INFO, "Serviceauftrag - Deutsche Bank",
			"Es kann nur ein Kauf mittels \'Rest aus Verkaufserlös\' getätigt werden!",
			"Für weitere Käufe aus \'Rest aus Verkaufserlös\' füllen Sie bitte einen weiteren Service-Auftrag aus.",
			"", new String[] { "OK" });

	/**
	 * Bitte geben Sie entweder einen Betrag oder Anteile ein
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.118
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_118 = new Message("03.01.999.118", MessageType.INFO, "Serviceauftrag - Deutsche Bank",
			"Eine Übernahme ist nicht möglich; es wurden ein Betrag und Anteile angegeben.",
			"Bitte geben Sie entweder einen Betrag oder Anteile ein", "", new String[] { "OK" });

	/**
	 * Verkaufsauftrag
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.119
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_119 = new Message("03.01.999.119", MessageType.INFO, "Serviceauftrag - Deutsche Bank",
			"Sie können nur $anteile Anteile des Fonds $fondsbezeichnung verkaufen",
			"Korrigieren Sie bitte Ihre Eingabe", "", new String[] { "OK" });

	/**
	 * Sie können den Fond $fondsbezeichnung um den maximalen Betrag von
	 * $depotwert verkaufen
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.120
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_120 = new Message("03.01.999.120", MessageType.INFO, "Serviceauftrag - Deutsche Bank",
			"Sie können den Fond $fondsbezeichnung um den maximalen Betrag von $depotwert verkaufen",
			"Korrigieren Sie bitte Ihre Eingabe", "", new String[] { "OK" });

	/**
	 * Bitte wählen Sie eine Person des Haushaltes aus bzw. geben Sie eine ein
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.121
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_121 = new Message("03.01.999.121", MessageType.INFO,
			"Bitte wählen Sie eine Person des Haushaltes aus bzw. geben Sie eine ein", "", "", "",
			new String[] { "OK" });

	/**
	 * Keine aktuellen Rechenwerte vorhanden
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.122
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_122 = new Message("03.01.999.122", MessageType.INFO, "Aktualisierung der Rechenwerte",
			"Keine aktuellen Rechenwerte vorhanden", "", "", new String[] { "OK" });

	/**
	 * Technischer Fehler
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.123
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_123 = new Message("03.01.999.123", MessageType.ERROR, "Technischer Fehler",
			"Die Provisionsdaten konnten aufgrund eines technischen Fehlers nicht geladen werden.", "", "",
			new String[] { "Ok" });

	/**
	 * Information
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.124
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_124 = new Message("03.01.999.124", MessageType.INFO, "Information", "$fehlermeldung.", "",
			"", new String[] { "Ok" });

	/**
	 * Fehler
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.125
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_125 = new Message(
			"03.01.999.125",
			MessageType.ERROR,
			"Fehler",
			"Der gewünschte Haushalt wurde nicht gefunden. \n Er ist von einer anderen Funktion oder einem anderen Benutzer gelöscht worden. \n Bitte wiederholen Sie die Suche.",
			"", "", new String[] { "Ok" });

	/**
	 * Fehler
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.126
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_126 = new Message("03.01.999.126", MessageType.ERROR, "Fehler",
			"Der gewünschte Vertrag konnte nicht geöffnet werden.", "", "", new String[] { "Ok" });

	/**
	 * Fehler
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor HBE
	 * @Dokument
	 * @Name Fehler
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.127
	 * @StatusRE OK
	 * @StatusFA OK
	 * @StatusDEV
	 * @StatusIAD
	 */
	Message MEX_03_01_999_127 = new Message("03.01.999.127", MessageType.ERROR, "Fehler",
			"Die von Ihnen geänderten Daten wurden in der Zwischenzeit schon von einer anderen Person geändert.\n"
					+ "Aus diesem Grund können Ihre Änderungen leider nicht gespeichert werden.\n", "",
			"Bitte schließen Sie den Haushalt und öffnen Sie diesen dann erneut.", new String[] { "Ok" });

	/**
	 * Fehler
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor RVR
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.128
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_128 = new Message("03.01.999.128", MessageType.ERROR, "Fehler",
			"Kunden können auf diesem Weg nicht gelöscht werden.", "", "", new String[] { "Ok" });

	/**
	 * Die Rechenwerte konnten nicht aktualisiert werden.
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.129
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_129 = new Message("03.01.999.129", MessageType.INFO,
			"Die Rechenwerte konnten nicht aktualisiert werden.", "Grund: $Grund\n"
					+ "Die Daten stehen ab dem $Datum wieder zur Verfügung", "", "", new String[] { "OK" });

	/**
	 * Das Beratungsprogramm kann nicht gestartet werden.
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.130
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_130 = new Message("03.01.999.130", MessageType.INFO,
			"Das Beratungsprogramm kann nicht gestartet werden.",
			"Sie haben keine Berechtigung Beratungsprogramme für diesen Haushalt zu starten.", "", "",
			new String[] { "OK" });

	/**
	 * Aktualisierung der Rechenwerte
	 * 
	 * @Projekt OKI-Rechenwerte-Aktualisierung
	 * @Modul Vertrag
	 * @Bereich Lebenvertrag der AML
	 * @Autor MHB
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.131
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_131 = new Message("03.01.999.131", MessageType.INFO, "Aktualisierung der Rechenwerte",
			"Die Rechenwerte wurden erfolgreich aktualisiert.", "", "", new String[] { "OK" });

	/**
	 * Fehler
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor RVR
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.132
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_132 = new Message("03.01.999.132", MessageType.ERROR, "Fehler",
			"Um diesen Interessenten zu löschen müssen Sie zunächst alle zugehörigen Fremdverträge löschen.", "", "",
			new String[] { "Ok" });

	/**
	 * Information
	 * 
	 * @Projekt OKI - Provisionsdaten laden
	 * @Modul
	 * @Bereich
	 * @Autor MHB
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.133
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_133 = new Message("03.01.999.133", MessageType.INFO, "Information",
			"Keine Provisioninformationen vorhanden", "", "", new String[] { "OK" });

	/**
	 * Warnung
	 * 
	 * @Projekt OKI - Erzeugen eines neuen Haushalts aus einem PIM-Kontakt
	 * @Modul
	 * @Bereich
	 * @Autor RVR
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.134
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_134 = new Message("03.01.999.134", MessageType.WARNING, "Warnung",
			"Es konnten nicht alle Daten in den neuen Haushalt übernommen werden.", "", "", new String[] { "OK" });

	/**
	 * Fehler
	 * 
	 * @Projekt OKI - Erzeugen eines neuen Haushalts aus einem PIM-Kontakt
	 * @Modul
	 * @Bereich
	 * @Autor RVR
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.135
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_135 = new Message(
			"03.01.999.135",
			MessageType.ERROR,
			"Fehler",
			"Es müssen mindestens Name, Vorname und Geburtsdatum eines Kontaktes bekannt sein um eine neue Interessenten-Akte anlegen zu können.",
			"", "", new String[] { "OK" });

	/**
	 * Die Funktion kann nicht ausgeführt werden
	 * 
	 * @Projekt OKI
	 * @Modul
	 * @Bereich
	 * @Autor RKA
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.136
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_136 = new Message("03.01.999.136", MessageType.ERROR,
			"Die Funktion kann nicht ausgeführt werden",
			"Microsoft Word ist nicht vorhanden oder nicht korrekt installiert.", "", "", new String[] { "OK" });

	/**
	 * Die Funktion kann nicht ausgeführt werden
	 * 
	 * @Projekt OKI
	 * @Modul
	 * @Bereich
	 * @Autor RKA
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.137
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_137 = new Message("03.01.999.137", MessageType.ERROR,
			"Die Funktion kann nicht ausgeführt werden",
			"Microsoft Excel ist nicht vorhanden oder nicht korrekt installiert.", "", "", new String[] { "OK" });

	/**
	 * Bestandsfunktion geöffnet
	 * 
	 * @Projekt OKI
	 * @Modul
	 * @Bereich
	 * @Autor ORE
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.138
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_138 = new Message("03.01.999.138", MessageType.ERROR, "Bestandsfunktion geöffnet",
			"Es ist eine Bestandsfunktion mit dieser Person geöffnet.Schliessen Sie zuerst dieses Modul.", "", "",
			new String[] { "OK" });

	/**
	 * Es wurde keine Person gefunden.
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor HFI
	 * @Dokument UseCase
	 * @Name Suche mit Personendaten ohne Ergebnis
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.022
	 * @StatusRE OK
	 * @StatusFA OK
	 * @StatusDEV umgesetzt
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_001_022 = new Message("03.01.001.022", MessageType.QUESTION, "Es wurde keine Person gefunden.",
			"Zu den eingegebenen Personendaten wurde keine Person gefunden.", "",
			"Möchten Sie die Suchkriterien ändern?", new String[] { "Suchkriterien ändern", "Neue Akte anlegen",
					"Abbrechen" });

	/**
	 * Es wurde keine Person gefunden
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor MHB
	 * @Dokument UseCase
	 * @Name Suche mit Personendaten im Innendienst ohne Ergebnis
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.027
	 * @StatusRE neu
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_027 = new Message("03.01.001.027", MessageType.QUESTION, "Es wurde keine Person gefunden",
			"Zu den eingegebenen Personendaten wurde keine Person gefunden.", "", "", new String[] { "OK" });

	/**
	 * Suchparameter ungültig
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.028
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_028 = new Message("03.01.001.028", MessageType.ERROR, "Suchparameter ungültig",
			"Für die Suche nach Telefonnummer ist die Telefonnummer notwendig.", "", "", new String[] { "Ok" });

	/**
	 * Suchparameter ungültig
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.029
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_029 = new Message("03.01.001.029", MessageType.ERROR, "Suchparameter ungültig",
			"Für die Suche nach Kfz-Kennzeichen ist das Kfz-Kennzeichen notwendig.", "", "", new String[] { "Ok" });

	/**
	 * Es wurde keine Person gefunden.
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Suche mit Kundennummer
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.030
	 * @StatusRE neu
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_030 = new Message("03.01.001.030", MessageType.QUESTION, "Es wurde keine Person gefunden.",
			"Zu der eingegebenen Kundennummer wurde keine Person gefunden.", "",
			"Möchten Sie die Suchkriterien ändern?", new String[] { "Suchkriterien ändern", "Abbrechen" });

	/**
	 * Es wurde keine Person gefunden.
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Suche mit Kundennummer
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.031
	 * @StatusRE neu
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_031 = new Message("03.01.001.031", MessageType.QUESTION, "Es wurde keine Person gefunden.",
			"Zu der eingegebenen Kundennummer wurde keine Person gefunden.", "", "", new String[] { "OK" });

	/**
	 * Es wurde keine Person gefunden.
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Suche mit Vertragsnummer
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.032
	 * @StatusRE neu
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_032 = new Message("03.01.001.032", MessageType.QUESTION, "Es wurde keine Person gefunden.",
			"Zu der eingegebenen Vertragsnummer wurde keine Person gefunden.", "",
			"Möchten Sie die Suchkriterien ändern?", new String[] { "Suchkriterien ändern", "Abbrechen" });

	/**
	 * Es wurde keine Person gefunden.
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Suche mit Vertragsnummer
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.033
	 * @StatusRE neu
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_033 = new Message("03.01.001.033", MessageType.QUESTION, "Es wurde keine Person gefunden.",
			"Zu der eingegebenen Vertragsnummer wurde keine Person gefunden.", "", "", new String[] { "OK" });

	/**
	 * Es wurde keine Person gefunden.
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Suche mit Telefonnummer
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.034
	 * @StatusRE neu
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_034 = new Message("03.01.001.034", MessageType.QUESTION, "Es wurde keine Person gefunden.",
			"Zu der eingegebenen Telefonnummer wurde keine Person gefunden.",
			"Die vollständige Telefonnummer des Kunden muß eingegeben werden.",
			"Möchten Sie die Suchkriterien ändern?", new String[] { "Suchkriterien ändern", "Abbrechen" });

	/**
	 * Es wurde keine Person gefunden.
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Suche mit Vertragsnummer
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.035
	 * @StatusRE neu
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_035 = new Message("03.01.001.035", MessageType.QUESTION, "Es wurde keine Person gefunden.",
			"Zu der eingegebenen Telefonnummer wurde keine Person gefunden.",
			"Die vollständige Telefonnummer des Kunden muß eingegeben werden.", "", new String[] { "OK" });

	/**
	 * Es wurde keine Person gefunden.
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Suche mit KFZ-Kennzeichen EV
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.036
	 * @StatusRE neu
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_036 = new Message("03.01.001.036", MessageType.QUESTION, "Es wurde keine Person gefunden.",
			"Zu dem eingegebenen KFZ-Kennzeichen wurde keine Person mit einem KFZ-Eigenvertrag\ngefunden.",
			"Das vollständige KFZ-Kennzeichen des Kunden muß eingegeben werden.{0}",
			"Möchten Sie die Suchkriterien ändern?", new String[] { "Suchkriterien ändern", "Abbrechen" });

	/**
	 * Es wurde keine Person gefunden.
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name Suche mit KFZ-Kennzeichen EV
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.001.037
	 * @StatusRE neu
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_001_037 = new Message("03.01.001.037", MessageType.QUESTION, "Es wurde keine Person gefunden.",
			"Zu dem eingegebenen KFZ-Kennzeichen wurde keine Person gefunden.",
			"Das vollständige KFZ-Kennzeichen des Kunden muß eingegeben werden.", "", new String[] { "OK" });

	/**
	 * Frage
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor RKA
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.061.003
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_061_003 = new Message("03.01.061.003", MessageType.QUESTION, "Kind-Kennzeichen",
			"Person A ist als Kind gekennzeichnet.", "Möchten Sie das Kennzeichen entfernen?", "", new String[] {
					"Kennzeichen entfernen", "Abbrechen" });

	/**
	 * Frage
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor RKA
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.061.004
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_061_004 = new Message("03.01.061.004", MessageType.QUESTION, "Akte löschen",
			"Diese Akte ist in mehreren Haushalten enthalten.",
			"Möchten Sie die Akte aus diesem Haushalt entfernen oder die Akte löschen?", "", new String[] {
					"Akte aus Haushalt entfernen", "Akte löschen", "Abbrechen" });

	/**
	 * Frage
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor RKA
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.061.005
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_061_005 = new Message("03.01.061.005", MessageType.ERROR, "Akte in Bearbeitung",
			"Diese Akte ist in anderen Haushalten oder Anwendungen in Bearbeitung.\n"
					+ "Bitte schliessen Sie diese Haushalte und Anwendungen vor dem Löschen.", "", "",
			new String[] { "OK" });

	/**
	 * Information
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor RKA
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.061.006
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_061_006 = new Message("03.01.061.006", MessageType.INFO, "Haushalt gelöscht",
			"Der Haushalt enthält keine Personen und wurde gelöscht", "", "", new String[] { "OK" });

	/**
	 * Die Rechenwerte konnten nicht aktualisiert werden
	 * 
	 * @Projekt OKI
	 * @Modul
	 * @Bereich
	 * @Autor MHB
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.139
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_139 = new Message(
			"03.01.999.139",
			MessageType.ERROR,
			"Aktualisierung der Rechenwerte",
			"Die Rechenwerte für diesen Vertrag können aufgrund vertragstechnischer Konflikte z.Zt. nicht aktualisiert werden.",
			"Nach der nächsten regulären Bestandsaktualisierung aus Aachen steht Ihnen diese Funktion wieder zur Verfügung.",
			"", new String[] { "OK" });
	/**
	 * Keine Person ausgewählt
	 * 
	 * @Projekt OKI-Haushalt/Akte
	 * @Modul Akte
	 * @Bereich Allgemein
	 * @Autor RKA
	 * @Dokument UseCase
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.140
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_140 = new Message("03.01.999.140", MessageType.ERROR, "Keine Person ausgewählt",
			"Die Auswahl kann nicht übernommen werden, solange keine Person ausgewählt wurde.", "", "",
			new String[] { "Person auswählen" });

	/**
	 * Bitte geben Sie entweder einen Betrag oder Anteile ein
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.141
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_141 = new Message("03.01.999.141", MessageType.INFO, "Serviceauftrag - Deutsche Bank",
			"Eine Übernahme ist nicht möglich; da weder ein Betrag noch Anteile angegeben wurden.",
			"Bitte geben Sie entweder einen Betrag oder Anteile ein.", "", new String[] { "OK" });

	/**
	 * Der Vertrag kann nicht geöffnet werden
	 * 
	 * @Projekt
	 * @Modul Akte
	 * @Bereich Vertrag öffnen
	 * @Autor MHB
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.039
	 * @StatusRE Neu
	 * @StatusFA Neu
	 * @StatusDEV Neu
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_999_039 = new Message("03.01.999.039", MessageType.INFO,
			"Der Vertrag kann nicht geöffnet werden",
			"Der Vertrag kann aufgrund unvollständiger Daten derzeit nicht geöffnet werden.",
			"Die Daten stehen Ihnen voraussichtlich nächsten Monat zur Verfügung.", "", new String[] { "OK" });

	/**
	 * Der Vertrag kann nicht geöffnet werden
	 * 
	 * @Projekt
	 * @Modul Akte
	 * @Bereich Vertrag öffnen
	 * @Autor MHB
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.040
	 * @StatusRE Neu
	 * @StatusFA Neu
	 * @StatusDEV Neu
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_999_040 = new Message("03.01.999.040", MessageType.INFO,
			"Der Vertrag kann nicht geöffnet werden",
			"Das Produkt wurde am $datum gelöscht, auf Grund Kündigung, Auflösung, etc.",
			"Da es zu dem Vertrag zur Zeit keine Produkte gibt, ist ein Zugriff auf diesen Vertrag nicht möglich.", "",
			new String[] { "OK" });

	/**
	 * Die Akte ist durch eine laufende Bestandsfunktion gesperrt
	 * 
	 * @Projekt
	 * @Modul Akte
	 * @Bereich Akte
	 * @Autor ORE
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.041
	 * @StatusRE Neu
	 * @StatusFA Neu
	 * @StatusDEV Neu
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_999_041 = new Message("03.01.999.041", MessageType.ERROR,
			"Die Akte ist durch eine laufende Bestandsfunktion gesperrt",
			"Diese Akte ist gesperrt, da auf Ihr eine Bestandsänderung durchgeführt wurde.",
			"Eine Speicherung der Änderung ist zur nicht möglich. Bitte versuchen Sie es zu einem späteren Zeitpunkt.",
			"", new String[] { "OK" });

	/**
	 * Der Sprung in den Vertrag ist nicht möglich, da keine Rechte für die KI
	 * vorhanden sind
	 * 
	 * @Projekt
	 * @Modul Akte
	 * @Bereich Akte
	 * @Autor RVR
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.142
	 * @StatusRE Neu
	 * @StatusFA Neu
	 * @StatusDEV Neu
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_999_142 = new Message("03.01.999.142", MessageType.ERROR, "Sprung in den Vertrag",
			"Sprung in den Vertrag nicht möglich, da keine Rechte für die KI vorhanden sind.", "", "",
			new String[] { "OK" });

	/**
	 * Der Sprung in den Besuchsauftrag ist nicht möglich, da keine Rechte für
	 * die KI vorhanden sind
	 * 
	 * @Projekt
	 * @Modul Akte
	 * @Bereich Akte
	 * @Autor RVR
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.143
	 * @StatusRE Neu
	 * @StatusFA Neu
	 * @StatusDEV Neu
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_999_143 = new Message("03.01.999.143", MessageType.ERROR, "Sprung in den Besuchsauftrag",
			"Sprung in den Besuchsauftrag nicht möglich, da keine Rechte für die KI vorhanden sind.", "", "",
			new String[] { "OK" });

	/**
	 * Sprung in die Finanzierung des Kunden nicht möglich, da keine Rechte für
	 * die KI vorhanden sind.
	 * 
	 * @Projekt
	 * @Modul Akte
	 * @Bereich Akte
	 * @Autor RVR
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.144
	 * @StatusRE Neu
	 * @StatusFA Neu
	 * @StatusDEV Neu
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_999_144 = new Message("03.01.999.144", MessageType.ERROR, "Sprung in die Finanzierung ",
			"Sprung in die Finanzierung des Kunden nicht möglich, da keine Rechte für die KI vorhanden sind.", "", "",
			new String[] { "OK" });

	/**
	 * Sprung in die Kundenakte nicht möglich, da keine Rechte für die KI
	 * vorhanden sind.
	 * 
	 * @Projekt
	 * @Modul Akte
	 * @Bereich Akte
	 * @Autor RVR
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.145
	 * @StatusRE Neu
	 * @StatusFA Neu
	 * @StatusDEV Neu
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_999_145 = new Message("03.01.999.145", MessageType.ERROR, "Sprung in die Kundenakte ",
			"Sprung in die Kundenakte nicht möglich, da keine Rechte für die KI vorhanden sind.", "", "",
			new String[] { "OK" });

	/**
	 * Erfassen einer neuen Kundenakte nicht möglich, da keine Rechte für die KI
	 * vorhanden sind.
	 * 
	 * @Projekt
	 * @Modul Akte
	 * @Bereich Akte
	 * @Autor RVR
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.146
	 * @StatusRE Neu
	 * @StatusFA Neu
	 * @StatusDEV Neu
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_999_146 = new Message("03.01.999.146", MessageType.ERROR, "Erfassen einer neuen Kundenakte",
			"Erfassen einer neuen Kundenakte nicht möglich, da keine Rechte für die KI vorhanden sind.", "", "",
			new String[] { "OK" });

	/**
	 * Sprung in die Magazinübersicht nicht möglich, da keine Rechte für die KI
	 * vorhanden sind.
	 * 
	 * @Projekt
	 * @Modul Akte
	 * @Bereich Akte
	 * @Autor RVR
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.147
	 * @StatusRE Neu
	 * @StatusFA Neu
	 * @StatusDEV Neu
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_999_147 = new Message("03.01.999.147", MessageType.ERROR, "Sprung in die Magazinübersicht",
			"Sprung in die Magazinübersicht nicht möglich, da keine Rechte für die KI vorhanden sind.", "", "",
			new String[] { "OK" });

	/**
	 * Information
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.148
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_148 = new Message("03.01.999.148", MessageType.INFO, "Aktualisierung der Rechenwerte",
			"Die Rechenwerte konnten nicht aktualisiert werden. \nGrund: $fehlermeldung.", "", "",
			new String[] { "Ok" });

	/**
	 * Der Zugriff auf die aktuellen Rechenwerte ist momentan nicht möglich.
	 * 
	 * @Projekt
	 * @Modul Akte
	 * @Bereich Akte
	 * @Autor RVR
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.149
	 * @StatusRE Neu
	 * @StatusFA Neu
	 * @StatusDEV Neu
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_999_149 = new Message("03.01.999.149", MessageType.ERROR, "Zugriff auf aktuelle Rechenwerte",
			"Der Zugriff auf die aktuellen Rechenwerte ist momentan aus technischen Gründen leider nicht möglich.", "",
			"", new String[] { "OK" });

	/**
	 * Der Zugriff auf die aktuellen Provisionsinformationen ist momentan aus
	 * technischen Gründen leider nicht möglich.
	 * 
	 * @Projekt
	 * @Modul Akte
	 * @Bereich Akte
	 * @Autor RVR
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.150
	 * @StatusRE Neu
	 * @StatusFA Neu
	 * @StatusDEV Neu
	 * @StatusIAD ungeprüft
	 */
	Message MEX_03_01_999_150 = new Message(
			"03.01.999.150",
			MessageType.ERROR,
			"Zugriff auf aktuelle Provisionsinformationen",
			"Der Zugriff auf die aktuellen Provisionsinformationen ist momentan aus technischen Gründen leider nicht möglich.",
			"", "", new String[] { "OK" });

	/**
	 * Der Mindestanlagebetrag für Erst-Einmalzahlungen beträgt bei dieser
	 * Gesellschaft $mindestbetrag Euro
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.151
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_151 = new Message("03.01.999.151", MessageType.INFO, "Serviceauftrag - Deutsche Bank",
			"Der Mindestbetrag für Nachkäufe beträgt bei dieser Gesellschaft $mindestbetrag Euro",
			"Bitte korrigieren Sie Ihre Eingabe.", "", new String[] { "OK" });

	/**
	 * TODO Workaround: Termin und Auftrag können erst erstellt werden, wenn
	 * Person bzw. Vertrag gespeichert wurde.
	 * 
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.152
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_152 = new Message(
			"03.01.999.152",
			MessageType.ERROR,
			"Problem beim Erstellen von Termin oder Auftrag",
			"Termin oder Auftrag kann nicht erstellt werden, weil die Person bzw. der Vertrag noch nicht gespeichert wurde.",
			"", "Bitte speichern Sie die Akte und wiederholen Sie die Aktion.", new String[] { "OK" });

	/**
	 * Fehler
	 * 
	 * @Projekt OKI - Erzeugen eines neuen Haushalts aus einem PIM-Kontakt - bei
	 *          Verwendung der Anrede Firma
	 * @Modul
	 * @Bereich
	 * @Autor TBA
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.153
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_153 = new Message(
			"03.01.999.153",
			MessageType.ERROR,
			"Fehler",
			"Es müssen mindestens Name eines Kontaktes bekannt sein um eine neue Interessenten-Akte anlegen zu können.",
			"", "", new String[] { "OK" });

	/**
	 * Fehler
	 * 
	 * @Projekt OKI - Erzeugen eines neuen Haushalts aus einem PIM-Kontakt -
	 *          Geburtsdatum in der Zukunft
	 * @Modul
	 * @Bereich
	 * @Autor TBA
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.154
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_154 = new Message("03.01.999.154", MessageType.ERROR, "Fehler",
			"Das Geburtsdatum muß in der Vergangenheit liegen, um eine neue Interessenten-Akte anlegen zu können.", "",
			"", new String[] { "OK" });

	/**
	 * Fehler
	 * 
	 * @Projekt OKI - Die Prüfung beim Start von POA-Schaden ist gescheitert
	 * @Modul
	 * @Bereich
	 * @Autor RVR
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.155
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_155 = new Message("03.01.999.155", MessageType.ERROR,
			"Fehler beim Aufruf der Schadenregulierung", "{0}", "", "", new String[] { "OK" });

	/**
	 * Fehler
	 * 
	 * @Projekt OKI - Schadenauskunft liefert kein Ergebnis
	 * @Modul
	 * @Bereich
	 * @Autor MHB
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.156
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_156 = new Message("03.01.999.156", MessageType.INFO, "Schadenauskunft",
			"Die von Ihnen eingegebenen Werte lieferten kein Ergebnis.", "", "", new String[] { "OK" });

	/**
	 * Fehler
	 * 
	 * @Projekt OKI - Vertrag für diesen Schaden nicht im Haushalt
	 * @Modul
	 * @Bereich
	 * @Autor MHB
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.157
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_157 = new Message("03.01.999.157", MessageType.INFO, "Schadenauskunft",
			"Im Haushalt gibt es keinen Vertrag mit der gelieferten Vertragsnummer.", "", "", new String[] { "OK" });

	/**
	 * Fehler
	 * 
	 * @Projekt OKI - Die Bestandsbearbeitung für einen Haushalt kann nicht
	 *          geöffnet werden, da dieser nicht gefunden wurde.
	 * @Modul
	 * @Bereich
	 * @Autor RVR
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.158
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_158 = new Message("03.01.999.158", MessageType.INFO, "Bestandsbearbeitung",
			"Der gewünschte Haushalt konnte nicht gefunden werden.", "", "", new String[] { "OK" });

	/**
	 * Fehler
	 * 
	 * @Projekt OKI - keine Schäden zum Vertrag
	 * @Modul
	 * @Bereich
	 * @Autor MHB
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.159
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_159 = new Message("03.01.999.159", MessageType.INFO, "Schadenauskunft",
			"Zu diesem Vertrag sind keine Schäden vorhanden.", "", "", new String[] { "OK" });

	/**
	 * Technischer Fehler
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.160
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_160 = new Message("03.01.999.160", MessageType.ERROR, "Technischer Fehler",
			"Die Inkassodaten konnten aufgrund eines technischen Fehlers nicht geladen werden.", "", "",
			new String[] { "Ok" });

	/**
	 * Information
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.161
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_161 = new Message("03.01.999.161", MessageType.INFO, "Information",
			"Keine Inkassodaten vorhanden. $fehlermeldung.", "", "", new String[] { "Ok" });

	/**
	 * Information
	 * 
	 * @Projekt
	 * @Modul
	 * @Bereich
	 * @Autor
	 * @Dokument
	 * @Name
	 * @Beschreibung
	 * @Bemerkung
	 * @MEX 03.01.999.162
	 * @StatusRE Entwurf
	 * @StatusFA neu
	 * @StatusDEV neu
	 * @StatusIAD neu
	 */
	Message MEX_03_01_999_162 = new Message("03.01.999.162", MessageType.INFO, "Struktursicht ändern",
			"Die Struktursicht wird für alle Akten des ausgewählten VB geändert.", "",
			"Sollen die Akten geändert werden?", new String[] { "Ok", "Abbrechen" });

}
