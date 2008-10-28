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
package org.eclipse.riena.example.client.controllers;

import org.eclipse.riena.example.client.model.PersonenSucheBean;
import org.eclipse.riena.example.client.model.PersonenSucheErgebnisBean;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget.SelectionType;

/**
 *
 */
public class KundenSucheController extends SubModuleController {
	private SuchBean suchBean = new SuchBean();

	@Override
	public void configureRidgets() {
		ITextRidget suchName = (ITextRidget) getRidget("suchName");
		suchName.bindToModel(suchBean, "suchName");
		suchName.setMandatory(true);

		((ITextRidget) getRidget("suchVorname")).bindToModel(suchBean, "suchVorname");
		((ITextRidget) getRidget("suchPlz")).bindToModel(suchBean, "suchPlz");
		((ITextRidget) getRidget("suchOrt")).bindToModel(suchBean, "suchOrt");
		((ITextRidget) getRidget("suchStrasse")).bindToModel(suchBean, "suchStrasse");
		((ILabelRidget) getRidget("treffer")).bindToModel(suchBean, "suchTreffer");

		ITableRidget kunden = ((ITableRidget) getRidget("ergebnis"));
		String[] columnNames = { "Name", "Vorname", "Kundennr.", "Geb.datum", "Straﬂe", "PLZ", "Ort", "Status",
				"Betreuer", "Rufnummer" };
		String[] propertyNames = { "nachName", "vorName", "kundenNummer", "geburtsdatum", "strasse", "plz", "ort",
				"status", "vbNummer", "rufNummer" };
		kunden.bindToModel(new PersonenSucheBean(), "kunden", PersonenSucheErgebnisBean.class, propertyNames,
				columnNames);

		((IActionRidget) getRidget("search")).addListener(new IActionListener() {

			public void callback() {
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.navigation.ui.controllers.SubModuleController#afterBind
	 * ()
	 */
	@Override
	public void afterBind() {
		super.afterBind();

		ITableRidget kunden = ((ITableRidget) getRidget("ergebnis"));
		kunden.setSelectionType(SelectionType.MULTI);
		for (int i = 0; i < 9; i++) {
			kunden.setColumnSortable(i, true);
		}
	}

	class SuchBean {

		private String suchName;
		private String suchVorname;
		private String suchPlz;
		private String suchOrt;
		private String suchStrasse;
		private String suchTreffer;

		public String getSuchName() {
			return suchName;
		}

		public void setSuchName(String suchName) {
			this.suchName = suchName;
		}

		public String getSuchVorname() {
			return suchVorname;
		}

		public void setSuchVorname(String suchVorname) {
			this.suchVorname = suchVorname;
		}

		public String getSuchPlz() {
			return suchPlz;
		}

		public void setSuchPlz(String suchPlz) {
			this.suchPlz = suchPlz;
		}

		public String getSuchOrt() {
			return suchOrt;
		}

		public void setSuchOrt(String suchOrt) {
			this.suchOrt = suchOrt;
		}

		public String getSuchTreffer() {
			return suchTreffer;
		}

		public void setSuchTreffer(String suchTreffer) {
			this.suchTreffer = suchTreffer;
		}

		public String getSuchStrasse() {
			return suchStrasse;
		}

		public void setSuchStrasse(String suchStrasse) {
			this.suchStrasse = suchStrasse;
		}

	}

}
