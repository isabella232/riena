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
package org.eclipse.riena.demo.client.customer.views;

import org.eclipse.riena.demo.client.customer.controllers.CustomerSearchController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.swtdesigner.SWTResourceManager;

/**
 *
 */
public class CustomerSearchView extends SubModuleView<CustomerSearchController> {

	private Table ergebnis;
	private Text suchOrt;
	private Text suchPlz;
	private Text suchStrasse;
	private Text suchVorname;
	private Text suchName;
	public static final String ID = "org.eclipse.riena.example.client.views.Test"; //$NON-NLS-1$

	/**
	 * Create contents of the view part
	 * 
	 * @param parent
	 */
	@Override
	public void basicCreatePartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		Composite container = new Composite(parent, SWT.NONE);
		container.setBackground(SWTResourceManager.getColor(255, 255, 255));

		suchName = new Text(container, SWT.BORDER);
		suchName.setBounds(143, 15, 144, 20);
		addUIControl(suchName, "suchName");

		final Label personLabel = new Label(container, SWT.NONE);
		personLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		personLabel.setFont(new Font(Display.getCurrent(), new FontData("", 8, SWT.BOLD)));
		personLabel.setText("Person");
		personLabel.setBounds(10, 17, 61, 13);

		final Label nameLabel = new Label(container, SWT.NONE);
		nameLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		nameLabel.setText("Name");
		nameLabel.setBounds(100, 18, 37, 13);

		final Label vornameLabel = new Label(container, SWT.NONE);
		vornameLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		vornameLabel.setText("Vorname");
		vornameLabel.setBounds(309, 18, 50, 13);

		suchVorname = new Text(container, SWT.BORDER);
		suchVorname.setBounds(363, 15, 120, 20);
		addUIControl(suchVorname, "suchVorname");

		final Button phonetischButton = new Button(container, SWT.CHECK);
		phonetischButton.setBackground(SWTResourceManager.getColor(255, 255, 255));
		phonetischButton.setText("phonetisch");
		phonetischButton.setBounds(506, 20, 85, 16);

		final Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label.setBounds(10, 45, 669, 13);

		final Label adresseLabel = new Label(container, SWT.NONE);
		adresseLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		adresseLabel.setFont(new Font(Display.getCurrent(), new FontData("", 8, SWT.BOLD)));
		adresseLabel.setText("Adresse");
		adresseLabel.setBounds(10, 64, 50, 13);

		final Label label_1 = new Label(container, SWT.NONE);
		label_1.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label_1.setText("Straße");
		label_1.setBounds(100, 64, 37, 13);

		final Label plzOrtLabel = new Label(container, SWT.NONE);
		plzOrtLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		plzOrtLabel.setText("PLZ / Ort");
		plzOrtLabel.setBounds(100, 98, 50, 13);

		suchStrasse = new Text(container, SWT.BORDER);
		suchStrasse.setBounds(155, 62, 132, 20);
		addUIControl(suchStrasse, "suchStrasse");

		suchPlz = new Text(container, SWT.BORDER);
		suchPlz.setBounds(156, 95, 50, 20);
		addUIControl(suchPlz, "suchPlz");

		suchOrt = new Text(container, SWT.BORDER);
		suchOrt.setBounds(212, 95, 120, 20);
		addUIControl(suchOrt, "suchOrt");

		final Label label_2 = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_2.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label_2.setBounds(10, 126, 669, 13);

		final Label kundenstatusLabel = new Label(container, SWT.NONE);
		kundenstatusLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		kundenstatusLabel.setFont(new Font(Display.getCurrent(), new FontData("", 8, SWT.BOLD)));
		kundenstatusLabel.setText("Kundenstatus");
		kundenstatusLabel.setBounds(10, 145, 85, 13);

		final Button suchKunde = new Button(container, SWT.CHECK);
		suchKunde.setBackground(SWTResourceManager.getColor(255, 255, 255));
		suchKunde.setText("Kunde");
		suchKunde.setBounds(100, 145, 50, 16);

		final Button suchInteressent = new Button(container, SWT.CHECK);
		suchInteressent.setBackground(SWTResourceManager.getColor(255, 255, 255));
		suchInteressent.setText("Interessent");
		suchInteressent.setBounds(153, 145, 77, 16);

		final Button suchAltkunde = new Button(container, SWT.CHECK);
		suchAltkunde.setBackground(SWTResourceManager.getColor(255, 255, 255));
		suchAltkunde.setText("Altkunde");
		suchAltkunde.setBounds(236, 145, 85, 16);

		final Label label_3 = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_3.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label_3.setBounds(0, 164, 695, 13);

		final Button zurueckSetzen = new Button(container, SWT.NONE);
		zurueckSetzen.setText("&Zurücksetzen");
		zurueckSetzen.setBounds(522, 177, 85, 23);
		addUIControl(zurueckSetzen, "reset");

		final Button sucheStarten = new Button(container, SWT.NONE);
		sucheStarten.setText("&Suchen");
		sucheStarten.setBounds(613, 177, 82, 23);
		addUIControl(sucheStarten, "search");

		final Label treffer = new Label(container, SWT.NONE);
		treffer.setBackground(SWTResourceManager.getColor(255, 255, 255));
		treffer.setText("xx Treffer");
		treffer.setBounds(627, 214, 68, 13);
		addUIControl(treffer, "treffer");

		ergebnis = new Table(container, SWT.BORDER);
		ergebnis.setBackground(SWTResourceManager.getColor(255, 255, 255));
		ergebnis.setLinesVisible(true);
		ergebnis.setHeaderVisible(true);
		ergebnis.setBounds(10, 235, 717, 236);
		addUIControl(ergebnis, "ergebnis");

		final TableColumn newColumnTableColumn = new TableColumn(ergebnis, SWT.NONE);
		newColumnTableColumn.setWidth(83);

		final TableColumn newColumnTableColumn_1 = new TableColumn(ergebnis, SWT.NONE);
		newColumnTableColumn_1.setWidth(65);

		final TableColumn newColumnTableColumn_2 = new TableColumn(ergebnis, SWT.NONE);
		newColumnTableColumn_2.setWidth(71);

		final TableColumn newColumnTableColumn_3 = new TableColumn(ergebnis, SWT.NONE);
		newColumnTableColumn_3.setWidth(75);

		final TableColumn newColumnTableColumn_4 = new TableColumn(ergebnis, SWT.NONE);
		newColumnTableColumn_4.setWidth(93);

		final TableColumn newColumnTableColumn_5 = new TableColumn(ergebnis, SWT.NONE);
		newColumnTableColumn_5.setWidth(51);

		final TableColumn newColumnTableColumn_6 = new TableColumn(ergebnis, SWT.NONE);
		newColumnTableColumn_6.setWidth(74);

		final TableColumn newColumnTableColumn_7 = new TableColumn(ergebnis, SWT.NONE);
		newColumnTableColumn_7.setWidth(70);

		final TableColumn newColumnTableColumn_8 = new TableColumn(ergebnis, SWT.NONE);
		newColumnTableColumn_8.setWidth(58);

		final TableColumn newColumnTableColumn_9 = new TableColumn(ergebnis, SWT.NONE);
		newColumnTableColumn_9.setWidth(64);

		final Button openCustomerButton = new Button(container, SWT.NONE);
		openCustomerButton.setText("Öffnen Kunden");
		openCustomerButton.setBounds(162, 477, 93, 23);
		addUIControl(openCustomerButton, "openCustomer");

		final Button newCustomerButton = new Button(container, SWT.NONE);
		newCustomerButton.setText("Neuer Customer");
		newCustomerButton.setBounds(309, 477, 93, 23);
		addUIControl(newCustomerButton, "newCustomer");

	}

	@Override
	public void setFocus() {
		// Set the focus
	}

}
