/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
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
		suchName.setBounds(146, 15, 144, 20);
		addUIControl(suchName, "suchName"); //$NON-NLS-1$

		final Label personLabel = new Label(container, SWT.NONE);
		personLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		personLabel.setFont(new Font(Display.getCurrent(), new FontData("", 8, SWT.BOLD))); //$NON-NLS-1$
		personLabel.setText("Person"); //$NON-NLS-1$
		personLabel.setBounds(10, 17, 61, 13);

		final Label nameLabel = new Label(container, SWT.NONE);
		nameLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		nameLabel.setText("Lastname"); //$NON-NLS-1$
		nameLabel.setBounds(77, 15, 55, 13);

		final Label vornameLabel = new Label(container, SWT.NONE);
		vornameLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		vornameLabel.setText("Firstname"); //$NON-NLS-1$
		vornameLabel.setBounds(306, 18, 50, 13);

		suchVorname = new Text(container, SWT.BORDER);
		suchVorname.setBounds(360, 15, 120, 20);
		addUIControl(suchVorname, "suchVorname"); //$NON-NLS-1$

		final Button phonetischButton = new Button(container, SWT.CHECK);
		phonetischButton.setBackground(SWTResourceManager.getColor(255, 255, 255));
		phonetischButton.setText("phonetic"); //$NON-NLS-1$
		phonetischButton.setBounds(503, 20, 85, 16);

		final Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label.setBounds(10, 45, 669, 13);

		final Label adresseLabel = new Label(container, SWT.NONE);
		adresseLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		adresseLabel.setFont(new Font(Display.getCurrent(), new FontData("", 8, SWT.BOLD))); //$NON-NLS-1$
		adresseLabel.setText("Addresse"); //$NON-NLS-1$
		adresseLabel.setBounds(10, 64, 61, 13);

		final Label label_1 = new Label(container, SWT.NONE);
		label_1.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label_1.setText("Street"); //$NON-NLS-1$
		label_1.setBounds(100, 64, 37, 13);

		final Label plzOrtLabel = new Label(container, SWT.NONE);
		plzOrtLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		plzOrtLabel.setText("Zipcode / City"); //$NON-NLS-1$
		plzOrtLabel.setBounds(65, 98, 68, 13);

		suchStrasse = new Text(container, SWT.BORDER);
		suchStrasse.setBounds(145, 60, 132, 20);
		addUIControl(suchStrasse, "suchStrasse"); //$NON-NLS-1$

		suchPlz = new Text(container, SWT.BORDER);
		suchPlz.setBounds(146, 93, 50, 20);
		addUIControl(suchPlz, "suchPlz"); //$NON-NLS-1$

		suchOrt = new Text(container, SWT.BORDER);
		suchOrt.setBounds(202, 93, 120, 20);
		addUIControl(suchOrt, "suchOrt"); //$NON-NLS-1$

		final Label label_2 = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_2.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label_2.setBounds(10, 126, 669, 13);

		final Label kundenstatusLabel = new Label(container, SWT.NONE);
		kundenstatusLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		kundenstatusLabel.setFont(new Font(Display.getCurrent(), new FontData("", 8, SWT.BOLD))); //$NON-NLS-1$
		kundenstatusLabel.setText("Customerstatus"); //$NON-NLS-1$
		kundenstatusLabel.setBounds(10, 145, 94, 13);

		final Button suchKunde = new Button(container, SWT.CHECK);
		suchKunde.setBackground(SWTResourceManager.getColor(255, 255, 255));
		suchKunde.setText("Customer"); //$NON-NLS-1$
		suchKunde.setBounds(110, 145, 68, 16);

		final Button suchInteressent = new Button(container, SWT.CHECK);
		suchInteressent.setBackground(SWTResourceManager.getColor(255, 255, 255));
		suchInteressent.setText("Prospect"); //$NON-NLS-1$
		suchInteressent.setBounds(180, 145, 77, 16);

		final Label label_3 = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_3.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label_3.setBounds(0, 164, 695, 13);

		final Button zurueckSetzen = new Button(container, SWT.NONE);
		zurueckSetzen.setText("&Reset"); //$NON-NLS-1$
		zurueckSetzen.setBounds(522, 177, 85, 23);
		addUIControl(zurueckSetzen, "reset"); //$NON-NLS-1$

		final Button sucheStarten = new Button(container, SWT.NONE);
		sucheStarten.setText("&Search"); //$NON-NLS-1$
		sucheStarten.setBounds(613, 177, 82, 23);
		addUIControl(sucheStarten, "search"); //$NON-NLS-1$

		final Label treffer = new Label(container, SWT.NONE);
		treffer.setBackground(SWTResourceManager.getColor(255, 255, 255));
		treffer.setText("xx Hits"); //$NON-NLS-1$
		treffer.setBounds(627, 214, 68, 13);
		addUIControl(treffer, "treffer"); //$NON-NLS-1$

		ergebnis = new Table(container, SWT.BORDER);
		ergebnis.setBackground(SWTResourceManager.getColor(255, 255, 255));
		ergebnis.setLinesVisible(true);
		ergebnis.setHeaderVisible(true);
		ergebnis.setBounds(10, 235, 717, 236);
		addUIControl(ergebnis, "ergebnis"); //$NON-NLS-1$

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
		openCustomerButton.setText("Open Customer"); //$NON-NLS-1$
		openCustomerButton.setBounds(162, 477, 93, 23);
		addUIControl(openCustomerButton, "openCustomer"); //$NON-NLS-1$

		final Button newCustomerButton = new Button(container, SWT.NONE);
		newCustomerButton.setText("New Customer"); //$NON-NLS-1$
		newCustomerButton.setBounds(309, 477, 93, 23);
		addUIControl(newCustomerButton, "newCustomer"); //$NON-NLS-1$

	}

	@Override
	public void setFocus() {
		// Set the focus
	}

}
