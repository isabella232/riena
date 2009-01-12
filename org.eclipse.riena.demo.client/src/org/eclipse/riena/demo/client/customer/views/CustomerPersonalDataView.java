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

import org.eclipse.riena.demo.client.customer.controllers.CustomerPersonalDataController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.swtdesigner.SWTResourceManager;

/**
 *
 */
public class CustomerPersonalDataView extends SubModuleView<CustomerPersonalDataController> {

	private Table table;
	private Text text_9;
	private Text text_8;
	private Text text_7;
	private Text text_6;
	private Text text_5;
	private Combo combo_7;
	private Combo combo_6;
	private Combo combo_5;
	private Combo combo_4;
	private Text text_4;
	private Text text_3;
	private Combo combo_3;
	private Text text_2;
	private Text text_1;
	private Text text;
	private Combo combo_2;
	private Combo combo_1;
	private Combo combo;
	public static final String ID = "org.eclipse.riena.example.client.views.CustomerPersonalDataView"; //$NON-NLS-1$

	/**
	 * Create contents of the view part
	 * 
	 * @param parent
	 */
	@Override
	public void basicCreatePartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setBackground(SWTResourceManager.getColor(255, 255, 255));
		parent.setLayout(new FillLayout());

		final Label label = new Label(container, SWT.NONE);
		label.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD)); //$NON-NLS-1$
		label.setText("Auswahl"); //$NON-NLS-1$
		label.setBounds(10, 26, 79, 13);

		final Label label_1 = new Label(container, SWT.NONE);
		label_1.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label_1.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD)); //$NON-NLS-1$
		label_1.setText("Stammdaten"); //$NON-NLS-1$
		label_1.setBounds(10, 64, 79, 13);

		final Label label_2 = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_2.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label_2.setBounds(10, 45, 663, 13);

		final Label label_3 = new Label(container, SWT.NONE);
		label_3.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label_3.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD)); //$NON-NLS-1$
		label_3.setText("Betreuung"); //$NON-NLS-1$
		label_3.setBounds(10, 305, 64, 13);

		final Label label_4 = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_4.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label_4.setBounds(95, 149, 578, 13);

		final Label label_5 = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_5.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label_5.setBounds(95, 289, 578, 13);

		final Label personLabel = new Label(container, SWT.NONE);
		personLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		personLabel.setText("Person"); //$NON-NLS-1$
		personLabel.setBounds(98, 26, 39, 13);

		final Label anredeLabel = new Label(container, SWT.NONE);
		anredeLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		anredeLabel.setText("Salutation"); //$NON-NLS-1$
		anredeLabel.setBounds(95, 64, 42, 15);

		final Label nameLabel = new Label(container, SWT.NONE);
		nameLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		nameLabel.setText("Name"); //$NON-NLS-1$
		nameLabel.setBounds(95, 100, 42, 15);

		final Label geburtsnameLabel = new Label(container, SWT.NONE);
		geburtsnameLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		geburtsnameLabel.setText("Geburtsname"); //$NON-NLS-1$
		geburtsnameLabel.setBounds(95, 130, 64, 15);

		combo = new Combo(container, SWT.NONE);
		combo.setBounds(142, 23, 233, 18);

		combo_1 = new Combo(container, SWT.NONE);
		combo_1.setBounds(170, 59, 70, 13);

		final Label titelLabel = new Label(container, SWT.NONE);
		titelLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		titelLabel.setText("Titel"); //$NON-NLS-1$
		titelLabel.setBounds(261, 64, 25, 13);

		combo_2 = new Combo(container, SWT.NONE);
		combo_2.setBounds(296, 61, 79, 18);

		text = new Text(container, SWT.BORDER);
		text.setBounds(170, 95, 136, 18);
		addUIControl(text, "lastname"); //$NON-NLS-1$

		final Label vornameLabel = new Label(container, SWT.NONE);
		vornameLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		vornameLabel.setText("Vorname"); //$NON-NLS-1$
		vornameLabel.setBounds(314, 100, 42, 13);

		text_1 = new Text(container, SWT.BORDER);
		text_1.setBounds(365, 97, 128, 18);
		addUIControl(text_1, "firstname"); //$NON-NLS-1$

		text_2 = new Text(container, SWT.BORDER);
		text_2.setBounds(170, 125, 136, 18);

		final Label familienstandLabel = new Label(container, SWT.NONE);
		familienstandLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		familienstandLabel.setText("Familienstand"); //$NON-NLS-1$
		familienstandLabel.setBounds(95, 168, 64, 13);

		final Label geburtsdatumLabel = new Label(container, SWT.NONE);
		geburtsdatumLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		geburtsdatumLabel.setText("Geburtsdatum"); //$NON-NLS-1$
		geburtsdatumLabel.setBounds(95, 203, 70, 13);

		final Label geburtsortLabel = new Label(container, SWT.NONE);
		geburtsortLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		geburtsortLabel.setText("Geburtsort"); //$NON-NLS-1$
		geburtsortLabel.setBounds(95, 231, 64, 13);

		final Label geschlechtLabel = new Label(container, SWT.NONE);
		geschlechtLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		geschlechtLabel.setText("Geschlecht"); //$NON-NLS-1$
		geschlechtLabel.setBounds(95, 270, 64, 13);

		combo_3 = new Combo(container, SWT.NONE);
		combo_3.setBounds(170, 168, 135, 18);

		text_3 = new Text(container, SWT.BORDER);
		text_3.setBounds(170, 200, 80, 18);

		text_4 = new Text(container, SWT.BORDER);
		text_4.setBounds(170, 228, 115, 18);

		final Button weiblichButton = new Button(container, SWT.RADIO);
		weiblichButton.setBackground(SWTResourceManager.getColor(255, 255, 255));

		weiblichButton.setText("weiblich"); //$NON-NLS-1$
		weiblichButton.setBounds(171, 268, 64, 16);

		final Button button = new Button(container, SWT.RADIO);
		button.setBackground(SWTResourceManager.getColor(255, 255, 255));
		button.setText("männlich"); //$NON-NLS-1$
		button.setBounds(236, 268, 64, 16);

		final Label label_11 = new Label(container, SWT.NONE);
		label_11.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label_11.setText("Staatsangehörigkeit"); //$NON-NLS-1$
		label_11.setBounds(331, 171, 102, 13);

		combo_4 = new Combo(container, SWT.NONE);
		combo_4.setBounds(439, 165, 128, 18);

		final Label label_12 = new Label(container, SWT.NONE);
		label_12.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label_12.setText("Label"); //$NON-NLS-1$
		label_12.setBounds(308, 231, 25, 13);

		combo_5 = new Combo(container, SWT.NONE);
		combo_5.setBounds(390, 228, 128, 18);

		final Label label_13 = new Label(container, SWT.NONE);
		label_13.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label_13.setText("Persönliche Salutation"); //$NON-NLS-1$
		label_13.setBounds(331, 270, 91, 13);

		final Button sieButton = new Button(container, SWT.RADIO);
		sieButton.setBackground(SWTResourceManager.getColor(255, 255, 255));
		sieButton.setText("Sie"); //$NON-NLS-1$
		sieButton.setBounds(429, 268, 35, 16);

		final Button duButton = new Button(container, SWT.RADIO);
		duButton.setBackground(SWTResourceManager.getColor(255, 255, 255));
		duButton.setText("Du"); //$NON-NLS-1$
		duButton.setBounds(470, 268, 35, 16);

		final Label kindLabel = new Label(container, SWT.NONE);
		kindLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		kindLabel.setText("Kind"); //$NON-NLS-1$
		kindLabel.setBounds(539, 270, 25, 13);

		final Button jaButton = new Button(container, SWT.RADIO);
		jaButton.setBackground(SWTResourceManager.getColor(255, 255, 255));
		jaButton.setText("Ja"); //$NON-NLS-1$
		jaButton.setBounds(578, 268, 35, 16);

		final Button neinButton = new Button(container, SWT.RADIO);
		neinButton.setBackground(SWTResourceManager.getColor(255, 255, 255));
		neinButton.setText("Nein"); //$NON-NLS-1$
		neinButton.setBounds(619, 268, 42, 16);

		final Label label_7 = new Label(container, SWT.NONE);
		label_7.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label_7.setText("Betreuung erwünscht"); //$NON-NLS-1$
		label_7.setBounds(95, 305, 102, 13);

		combo_6 = new Combo(container, SWT.NONE);
		combo_6.setBounds(217, 302, 116, 18);

		final Label bewertungLabel = new Label(container, SWT.NONE);
		bewertungLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		bewertungLabel.setText("Bewertung"); //$NON-NLS-1$
		bewertungLabel.setBounds(95, 340, 91, 13);

		final Label vbnummerLabel = new Label(container, SWT.NONE);
		vbnummerLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		vbnummerLabel.setText("VB-Nummer"); //$NON-NLS-1$
		vbnummerLabel.setBounds(95, 374, 64, 13);

		final Label kundennummerLabel = new Label(container, SWT.NONE);
		kundennummerLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		kundennummerLabel.setText("Kundennummer"); //$NON-NLS-1$
		kundennummerLabel.setBounds(95, 407, 79, 13);

		combo_7 = new Combo(container, SWT.NONE);
		combo_7.setBounds(217, 337, 89, 18);

		text_5 = new Text(container, SWT.BORDER);
		text_5.setBounds(217, 371, 54, 18);

		final Label vbnameLabel = new Label(container, SWT.NONE);
		vbnameLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		vbnameLabel.setText("VB-Name"); //$NON-NLS-1$
		vbnameLabel.setBounds(296, 374, 54, 13);

		text_6 = new Text(container, SWT.BORDER);
		text_6.setBounds(353, 371, 113, 18);

		text_7 = new Text(container, SWT.BORDER);
		text_7.setBounds(217, 404, 69, 18);

		final Label kundenstatusLabel = new Label(container, SWT.NONE);
		kundenstatusLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		kundenstatusLabel.setText("Kundenstatus"); //$NON-NLS-1$
		kundenstatusLabel.setBounds(314, 407, 70, 13);

		text_8 = new Text(container, SWT.BORDER);
		text_8.setBounds(384, 404, 91, 18);

		final Label erfasstAmLabel = new Label(container, SWT.NONE);
		erfasstAmLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		erfasstAmLabel.setText("Erfasst am"); //$NON-NLS-1$
		erfasstAmLabel.setBounds(499, 407, 54, 13);

		text_9 = new Text(container, SWT.BORDER);
		text_9.setBounds(559, 404, 102, 18);

		table = new Table(container, SWT.BORDER);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBounds(95, 426, 578, 86);

		final TableColumn newColumnTableColumn = new TableColumn(table, SWT.NONE);
		newColumnTableColumn.setWidth(100);
		newColumnTableColumn.setText("New column"); //$NON-NLS-1$

		new TableColumn(table, SWT.NONE);

		final TableColumn newColumnTableColumn_1 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_1.setWidth(100);
		newColumnTableColumn_1.setText("New column"); //$NON-NLS-1$

		new TableColumn(table, SWT.NONE);

		final TableColumn newColumnTableColumn_2 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_2.setWidth(100);
		newColumnTableColumn_2.setText("New column"); //$NON-NLS-1$

		new TableColumn(table, SWT.NONE);

		final Button speichernButton = new Button(container, SWT.NONE);
		speichernButton.setText("Speichern"); //$NON-NLS-1$
		speichernButton.setBounds(578, 518, 69, 23);
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

}
