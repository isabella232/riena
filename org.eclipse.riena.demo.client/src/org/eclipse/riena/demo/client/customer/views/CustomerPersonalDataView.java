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
		label.setText("Selection"); //$NON-NLS-1$
		label.setBounds(10, 26, 79, 13);

		final Label label_1 = new Label(container, SWT.NONE);
		label_1.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label_1.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD)); //$NON-NLS-1$
		label_1.setText("Master data"); //$NON-NLS-1$
		label_1.setBounds(10, 64, 79, 13);

		final Label label_2 = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_2.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label_2.setBounds(10, 45, 663, 13);

		final Label salesLabel = new Label(container, SWT.NONE);
		salesLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		salesLabel.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD)); //$NON-NLS-1$
		salesLabel.setText("Sales"); //$NON-NLS-1$
		salesLabel.setBounds(10, 305, 64, 13);

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
		anredeLabel.setBounds(95, 64, 57, 15);

		final Label nameLabel = new Label(container, SWT.NONE);
		nameLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		nameLabel.setText("Lastname"); //$NON-NLS-1$
		nameLabel.setBounds(95, 100, 46, 15);

		final Label geburtsnameLabel = new Label(container, SWT.NONE);
		geburtsnameLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		geburtsnameLabel.setText("Birthname"); //$NON-NLS-1$
		geburtsnameLabel.setBounds(95, 130, 64, 15);

		combo = new Combo(container, SWT.NONE);
		combo.setBounds(142, 23, 233, 18);

		combo_1 = new Combo(container, SWT.NONE);
		combo_1.setBounds(170, 59, 70, 13);

		final Label titelLabel = new Label(container, SWT.NONE);
		titelLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		titelLabel.setText("Title"); //$NON-NLS-1$
		titelLabel.setBounds(261, 64, 25, 13);

		combo_2 = new Combo(container, SWT.NONE);
		combo_2.setBounds(296, 61, 79, 18);

		text = new Text(container, SWT.BORDER);
		text.setBounds(170, 95, 136, 18);
		addUIControl(text, "lastname"); //$NON-NLS-1$

		final Label vornameLabel = new Label(container, SWT.NONE);
		vornameLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		vornameLabel.setText("Firstname"); //$NON-NLS-1$
		vornameLabel.setBounds(320, 100, 52, 13);

		text_1 = new Text(container, SWT.BORDER);
		text_1.setBounds(375, 95, 128, 18);
		addUIControl(text_1, "firstname"); //$NON-NLS-1$

		text_2 = new Text(container, SWT.BORDER);
		text_2.setBounds(170, 125, 136, 18);

		final Label familienstandLabel = new Label(container, SWT.NONE);
		familienstandLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		familienstandLabel.setText("Maritial status"); //$NON-NLS-1$
		familienstandLabel.setBounds(95, 170, 72, 13);

		final Label geburtsdatumLabel = new Label(container, SWT.NONE);
		geburtsdatumLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		geburtsdatumLabel.setText("Birthdate"); //$NON-NLS-1$
		geburtsdatumLabel.setBounds(95, 203, 70, 13);

		final Label geburtsortLabel = new Label(container, SWT.NONE);
		geburtsortLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		geburtsortLabel.setText("Birthplace"); //$NON-NLS-1$
		geburtsortLabel.setBounds(95, 231, 64, 13);

		final Label geschlechtLabel = new Label(container, SWT.NONE);
		geschlechtLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		geschlechtLabel.setText("Gender"); //$NON-NLS-1$
		geschlechtLabel.setBounds(95, 270, 64, 13);

		combo_3 = new Combo(container, SWT.NONE);
		combo_3.setBounds(170, 168, 135, 18);

		text_3 = new Text(container, SWT.BORDER);
		text_3.setBounds(170, 200, 80, 18);

		text_4 = new Text(container, SWT.BORDER);
		text_4.setBounds(170, 228, 115, 18);

		final Button weiblichButton = new Button(container, SWT.RADIO);
		weiblichButton.setBackground(SWTResourceManager.getColor(255, 255, 255));

		weiblichButton.setText("female"); //$NON-NLS-1$
		weiblichButton.setBounds(171, 268, 64, 16);

		final Button button = new Button(container, SWT.RADIO);
		button.setBackground(SWTResourceManager.getColor(255, 255, 255));
		button.setText("male"); //$NON-NLS-1$
		button.setBounds(236, 268, 64, 16);

		final Label label_11 = new Label(container, SWT.NONE);
		label_11.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label_11.setText("Citizenship"); //$NON-NLS-1$
		label_11.setBounds(331, 171, 57, 13);

		combo_4 = new Combo(container, SWT.NONE);
		combo_4.setBounds(396, 165, 128, 18);

		final Label label_12 = new Label(container, SWT.NONE);
		label_12.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label_12.setText("Label"); //$NON-NLS-1$
		label_12.setBounds(314, 231, 25, 13);

		combo_5 = new Combo(container, SWT.NONE);
		combo_5.setBounds(347, 228, 128, 18);

		final Label conectedWantedLabel = new Label(container, SWT.NONE);
		conectedWantedLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		conectedWantedLabel.setText("Contact requested"); //$NON-NLS-1$
		conectedWantedLabel.setBounds(95, 305, 90, 13);

		combo_6 = new Combo(container, SWT.NONE);
		combo_6.setBounds(191, 302, 116, 18);

		final Label bewertungLabel = new Label(container, SWT.NONE);
		bewertungLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		bewertungLabel.setText("Rating"); //$NON-NLS-1$
		bewertungLabel.setBounds(95, 340, 91, 13);

		final Label vbnummerLabel = new Label(container, SWT.NONE);
		vbnummerLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		vbnummerLabel.setText("SalesRep-No"); //$NON-NLS-1$
		vbnummerLabel.setBounds(95, 374, 64, 13);

		final Label kundennummerLabel = new Label(container, SWT.NONE);
		kundennummerLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		kundennummerLabel.setText("Customernumber"); //$NON-NLS-1$
		kundennummerLabel.setBounds(95, 407, 79, 13);

		combo_7 = new Combo(container, SWT.NONE);
		combo_7.setBounds(191, 329, 89, 18);

		text_5 = new Text(container, SWT.BORDER);
		text_5.setBounds(191, 371, 54, 18);

		final Label vbnameLabel = new Label(container, SWT.NONE);
		vbnameLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		vbnameLabel.setText("SalesRep-Name"); //$NON-NLS-1$
		vbnameLabel.setBounds(261, 374, 78, 13);

		text_6 = new Text(container, SWT.BORDER);
		text_6.setBounds(347, 371, 113, 18);

		text_7 = new Text(container, SWT.BORDER);
		text_7.setBounds(191, 404, 69, 18);

		final Label kundenstatusLabel = new Label(container, SWT.NONE);
		kundenstatusLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		kundenstatusLabel.setText("Customerstatus"); //$NON-NLS-1$
		kundenstatusLabel.setBounds(282, 407, 79, 13);

		text_8 = new Text(container, SWT.BORDER);
		text_8.setBounds(369, 404, 91, 18);

		final Label erfasstAmLabel = new Label(container, SWT.NONE);
		erfasstAmLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		erfasstAmLabel.setText("Created"); //$NON-NLS-1$
		erfasstAmLabel.setBounds(479, 407, 42, 13);

		text_9 = new Text(container, SWT.BORDER);
		text_9.setBounds(523, 404, 102, 18);

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
		speichernButton.setText("Save"); //$NON-NLS-1$
		speichernButton.setBounds(578, 518, 69, 23);
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

}
