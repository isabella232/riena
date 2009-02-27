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

import org.eclipse.riena.demo.client.customer.controllers.CustomerOverviewController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.swtdesigner.SWTResourceManager;

/**
 *
 */
public class CustomerOverview extends SubModuleView<CustomerOverviewController> {

	private Table table_5;
	private Table table_4;
	private Table table_3;
	private Table table_2;
	private Table table_1;
	private Table table;
	public static final String ID = "org.eclipse.riena.example.client.views.CustomerOverview"; //$NON-NLS-1$

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
		label.setText("Personal data"); //$NON-NLS-1$
		label.setBounds(10, 29, 96, 13);

		table = new Table(container, SWT.BORDER);
		table.setLinesVisible(true);
		table.setBounds(125, 29, 258, 33);
		addUIControl(table, "table1"); //$NON-NLS-1$

		final TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setWidth(110);
		tableColumn.setText("New column"); //$NON-NLS-1$

		final TableColumn tableColumn_1 = new TableColumn(table, SWT.NONE);
		tableColumn_1.setAlignment(SWT.RIGHT);
		tableColumn_1.setWidth(126);
		tableColumn_1.setText("New column"); //$NON-NLS-1$

		table_1 = new Table(container, SWT.BORDER);
		table_1.setLinesVisible(true);
		table_1.setBounds(401, 29, 221, 44);

		final TableColumn newColumnTableColumn = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumn.setWidth(100);
		newColumnTableColumn.setText("New column"); //$NON-NLS-1$

		new TableColumn(table_1, SWT.NONE);

		final TableColumn newColumnTableColumn_1 = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumn_1.setAlignment(SWT.RIGHT);
		newColumnTableColumn_1.setWidth(100);
		newColumnTableColumn_1.setText("New column"); //$NON-NLS-1$

		new TableColumn(table_1, SWT.NONE);

		final Label label_1 = new Label(container, SWT.NONE);
		label_1.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label_1.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD)); //$NON-NLS-1$
		label_1.setText("City"); //$NON-NLS-1$
		label_1.setBounds(10, 131, 96, 13);

		table_2 = new Table(container, SWT.BORDER);
		table_2.setLinesVisible(true);
		table_2.setBounds(125, 131, 258, 44);

		table_3 = new Table(container, SWT.BORDER);
		table_3.setLinesVisible(true);
		table_3.setBounds(401, 131, 221, 44);

		final Label label_2 = new Label(container, SWT.NONE);
		label_2.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label_2.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD)); //$NON-NLS-1$
		label_2.setText("Persons in Household"); //$NON-NLS-1$
		label_2.setBounds(10, 232, 157, 13);

		table_4 = new Table(container, SWT.BORDER);
		table_4.setLinesVisible(true);
		table_4.setHeaderVisible(true);
		table_4.setBounds(10, 251, 683, 90);

		final TableColumn newColumnTableColumn_2 = new TableColumn(table_4, SWT.NONE);
		newColumnTableColumn_2.setWidth(79);
		newColumnTableColumn_2.setText("New column"); //$NON-NLS-1$

		new TableColumn(table_4, SWT.NONE);

		final TableColumn newColumnTableColumn_3 = new TableColumn(table_4, SWT.NONE);
		newColumnTableColumn_3.setWidth(74);
		newColumnTableColumn_3.setText("New column"); //$NON-NLS-1$

		new TableColumn(table_4, SWT.NONE);

		final TableColumn newColumnTableColumn_4 = new TableColumn(table_4, SWT.NONE);
		newColumnTableColumn_4.setWidth(78);
		newColumnTableColumn_4.setText("New column"); //$NON-NLS-1$

		new TableColumn(table_4, SWT.NONE);

		final TableColumn newColumnTableColumn_5 = new TableColumn(table_4, SWT.NONE);
		newColumnTableColumn_5.setWidth(80);
		newColumnTableColumn_5.setText("New column"); //$NON-NLS-1$

		new TableColumn(table_4, SWT.NONE);

		final TableColumn newColumnTableColumn_6 = new TableColumn(table_4, SWT.NONE);
		newColumnTableColumn_6.setWidth(79);
		newColumnTableColumn_6.setText("New column"); //$NON-NLS-1$

		new TableColumn(table_4, SWT.NONE);

		final TableColumn newColumnTableColumn_7 = new TableColumn(table_4, SWT.NONE);
		newColumnTableColumn_7.setWidth(79);
		newColumnTableColumn_7.setText("New column"); //$NON-NLS-1$

		new TableColumn(table_4, SWT.NONE);

		final TableColumn newColumnTableColumn_8 = new TableColumn(table_4, SWT.NONE);
		newColumnTableColumn_8.setWidth(68);
		newColumnTableColumn_8.setText("New column"); //$NON-NLS-1$

		new TableColumn(table_4, SWT.NONE);

		final TableColumn newColumnTableColumn_9 = new TableColumn(table_4, SWT.NONE);
		newColumnTableColumn_9.setWidth(76);
		newColumnTableColumn_9.setText("New column"); //$NON-NLS-1$

		new TableColumn(table_4, SWT.NONE);

		final TableColumn newColumnTableColumn_10 = new TableColumn(table_4, SWT.NONE);
		newColumnTableColumn_10.setWidth(61);
		newColumnTableColumn_10.setText("New column"); //$NON-NLS-1$

		new TableColumn(table_4, SWT.NONE);

		final Label label_3 = new Label(container, SWT.NONE);
		label_3.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label_3.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD)); //$NON-NLS-1$
		label_3.setText("Aktiv Contracts"); //$NON-NLS-1$
		label_3.setBounds(10, 356, 96, 13);

		table_5 = new Table(container, SWT.BORDER);
		table_5.setLinesVisible(true);
		table_5.setHeaderVisible(true);
		table_5.setBounds(10, 375, 683, 100);

		final TableColumn newColumnTableColumn_11 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_11.setWidth(47);
		newColumnTableColumn_11.setText("New column"); //$NON-NLS-1$

		new TableColumn(table_5, SWT.NONE);

		final TableColumn newColumnTableColumn_12 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_12.setWidth(62);
		newColumnTableColumn_12.setText("New column"); //$NON-NLS-1$

		new TableColumn(table_5, SWT.NONE);

		final TableColumn newColumnTableColumn_13 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_13.setWidth(55);
		newColumnTableColumn_13.setText("New column"); //$NON-NLS-1$

		new TableColumn(table_5, SWT.NONE);

		final TableColumn newColumnTableColumn_14 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_14.setWidth(59);
		newColumnTableColumn_14.setText("New column"); //$NON-NLS-1$

		new TableColumn(table_5, SWT.NONE);

		final TableColumn newColumnTableColumn_15 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_15.setWidth(73);
		newColumnTableColumn_15.setText("New column"); //$NON-NLS-1$

		new TableColumn(table_5, SWT.NONE);

		final TableColumn newColumnTableColumn_16 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_16.setWidth(69);
		newColumnTableColumn_16.setText("New column"); //$NON-NLS-1$

		new TableColumn(table_5, SWT.NONE);

		final TableColumn newColumnTableColumn_17 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_17.setWidth(72);
		newColumnTableColumn_17.setText("New column"); //$NON-NLS-1$

		new TableColumn(table_5, SWT.NONE);

		final TableColumn newColumnTableColumn_18 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_18.setWidth(75);
		newColumnTableColumn_18.setText("New column"); //$NON-NLS-1$

		new TableColumn(table_5, SWT.NONE);

		final TableColumn newColumnTableColumn_19 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_19.setWidth(70);
		newColumnTableColumn_19.setText("New column"); //$NON-NLS-1$

		new TableColumn(table_5, SWT.NONE);

		final TableColumn newColumnTableColumn_20 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_20.setWidth(89);
		newColumnTableColumn_20.setText("New column"); //$NON-NLS-1$

		new TableColumn(table_5, SWT.NONE);

		final Button button = new Button(container, SWT.NONE);
		button.setText("Save"); //$NON-NLS-1$
		button.setBounds(627, 481, 66, 23);
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

}
