/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.demo.client.views;

import com.swtdesigner.SWTResourceManager;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;

/**
 * abstract email view
 */
public class EmailView extends SubModuleView {
	private Table table;

	/**
	 * the constructor
	 */
	public EmailView() {
		super();
	}

	@Override
	protected void basicCreatePartControl(final Composite parent) {

		parent.setLayout(new FillLayout(SWT.VERTICAL));

		final Font boldFont11 = SWTResourceManager.getFont("Arial", 11, SWT.BOLD); //$NON-NLS-1$
		final Font normalFont11 = SWTResourceManager.getFont("Arial", 11, SWT.NORMAL); //$NON-NLS-1$
		final Composite container = new Composite(parent, SWT.NONE);

		table = new Table(container, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		table.setFont(SWTResourceManager.getFont("Arial", 10, SWT.NORMAL));
		table.setBackground(SWTResourceManager.getColor(255, 255, 254));
		table.setLocation(25, 23);
		table.setSize(703, 190);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		addUIControl(table, "emailsTable"); //$NON-NLS-1$

		final TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setWidth(188);

		final TableColumn tableColumn_1 = new TableColumn(table, SWT.NONE);
		tableColumn_1.setWidth(349);

		final TableColumn tableColumn_2 = new TableColumn(table, SWT.NONE);
		tableColumn_2.setWidth(143);

		Label l = new Label(container, SWT.WRAP);
		l.setLocation(25, 254);
		l.setSize(58, 18);
		l.setFont(boldFont11);
		l.setForeground(SWTResourceManager.getColor(1, 1, 1));
		l.setText("Subject"); //$NON-NLS-1$

		final Label subjectLabel = new Label(container, SWT.NONE);
		subjectLabel.setLocation(103, 254);
		subjectLabel.setSize(619, 17);
		//		subjectLabel.setLayoutData(new GridData(GridData.FILL_BOTH));
		subjectLabel.setFont(SWTResourceManager.getFont("Arial", 11, SWT.BOLD)); //$NON-NLS-1$
		subjectLabel.setForeground(SWTResourceManager.getColor(1, 1, 1));
		addUIControl(subjectLabel, "emailSubject"); //$NON-NLS-1$

		l = new Label(container, SWT.WRAP);
		l.setLocation(25, 278);
		l.setSize(40, 18);
		l.setFont(boldFont11);
		l.setForeground(SWTResourceManager.getColor(1, 1, 1));
		l.setText("From"); //$NON-NLS-1$

		final Label fromLabel = new Label(container, SWT.NONE);
		fromLabel.setLocation(103, 277);
		fromLabel.setSize(302, 17);
		//		fromLabel.setLayoutData(new GridData(GridData.FILL_BOTH));
		fromLabel.setFont(SWTResourceManager.getFont("Arial", 11, SWT.NORMAL)); //$NON-NLS-1$
		fromLabel.setForeground(SWTResourceManager.getColor(1, 1, 1));
		addUIControl(fromLabel, "emailFrom"); //$NON-NLS-1$

		l = new Label(container, SWT.WRAP);
		l.setLocation(411, 277);
		l.setSize(23, 18);
		l.setFont(boldFont11);
		l.setForeground(SWTResourceManager.getColor(1, 1, 1));
		l.setText("To"); //$NON-NLS-1$

		final Label toLabel = new Label(container, SWT.NONE);
		toLabel.setLocation(440, 277);
		toLabel.setSize(282, 17);
		//		toLabel.setLayoutData(new GridData(GridData.FILL_BOTH));
		toLabel.setFont(SWTResourceManager.getFont("Arial", 11, SWT.NORMAL)); //$NON-NLS-1$
		toLabel.setForeground(SWTResourceManager.getColor(1, 1, 1));
		addUIControl(toLabel, "emailTo"); //$NON-NLS-1$

		l = new Label(container, SWT.WRAP);
		l.setLocation(25, 302);
		l.setSize(37, 18);
		l.setFont(boldFont11);
		l.setForeground(SWTResourceManager.getColor(1, 1, 1));
		l.setText("Date"); //$NON-NLS-1$

		final Label dateLabel = new Label(container, SWT.NONE);
		dateLabel.setLocation(103, 300);
		dateLabel.setSize(142, 17);
		//		dateLabel.setLayoutData(new GridData(GridData.FILL_BOTH));
		dateLabel.setFont(normalFont11);
		dateLabel.setForeground(SWTResourceManager.getColor(1, 1, 1));
		addUIControl(dateLabel, "emailDate"); //$NON-NLS-1$

		// message contents

		final Text emailBody = new Text(container, SWT.MULTI | SWT.WRAP);
		emailBody.setLocation(31, 326);
		emailBody.setSize(697, 112);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(emailBody);
		emailBody.setFont(normalFont11);
		addUIControl(emailBody, "emailBody"); //$NON-NLS-1$

		//create button
		final Button openCustomerButton = new Button(container, SWT.NONE);
		openCustomerButton.setBounds(580, 474, 133, 38);
		openCustomerButton.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD)); //$NON-NLS-1$
		openCustomerButton.setText("Open Customer"); //$NON-NLS-1$
		addUIControl(openCustomerButton, "openCustomer"); //$NON-NLS-1$

		final Composite composite = new Composite(container, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(121, 117, 168));
		composite.setBounds(25, 235, 1054, 2);

		final Composite composite2 = new Composite(container, SWT.NONE);
		composite2.setBackground(SWTResourceManager.getColor(121, 117, 168));
		composite2.setBounds(25, 457, 1054, 2);
	}
}
