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
package org.eclipse.riena.demo.client.views;

import com.swtdesigner.SWTResourceManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;

/**
 *
 */
public class CustomerSearchView extends SubModuleView {
	public CustomerSearchView() {
	}

	private Table ergebnis;

	/**
	 * Create contents of the view part
	 * 
	 * @param parent
	 */
	@Override
	public void basicCreatePartControl(Composite parent) {

		parent.setLayout(new FillLayout(SWT.VERTICAL));
		Composite container = new Composite(parent, SWT.NONE);

		ergebnis = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		ergebnis.setBackground(SWTResourceManager.getColor(255, 255, 254));
		ergebnis.setLinesVisible(true);
		ergebnis.setHeaderVisible(true);
		ergebnis.setBounds(30, 137, 703, 264);
		addUIControl(ergebnis, "result"); //$NON-NLS-1$

		final TableColumn colLastname = new TableColumn(ergebnis, SWT.NONE);
		colLastname.setWidth(136);

		final TableColumn colFirstname = new TableColumn(ergebnis, SWT.NONE);
		colFirstname.setWidth(95);

		final TableColumn colBirthdate = new TableColumn(ergebnis, SWT.NONE);
		colBirthdate.setWidth(122);

		final TableColumn colStreet = new TableColumn(ergebnis, SWT.NONE);
		colStreet.setWidth(150);

		final TableColumn colCity = new TableColumn(ergebnis, SWT.NONE);
		colCity.setWidth(127);

		Text suchName = new Text(container, SWT.BORDER);
		suchName.setFont(SWTResourceManager.getFont("", 12, SWT.NONE)); //$NON-NLS-1$
		suchName.setBounds(120, 25, 201, 32);
		addUIControl(suchName, "searchLastName"); //$NON-NLS-1$

		final Label nameLabel = new Label(container, SWT.NONE);
		nameLabel.setForeground(SWTResourceManager.getColor(1, 0, 0));
		nameLabel.setFont(SWTResourceManager.getFont("Helvetica", 14, SWT.NONE)); //$NON-NLS-1$
		nameLabel.setText("Lastname"); //$NON-NLS-1$
		nameLabel.setBounds(30, 31, 82, 26);

		final Button sucheStarten = new Button(container, SWT.NONE);
		sucheStarten.setFont(SWTResourceManager.getFont("", 12, SWT.BOLD)); //$NON-NLS-1$
		sucheStarten.setText("&Search"); //$NON-NLS-1$
		sucheStarten.setBounds(345, 25, 82, 32);
		addUIControl(sucheStarten, "search"); //$NON-NLS-1$

		final Label treffer = new Label(container, SWT.RIGHT);
		treffer.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		treffer.setForeground(SWTResourceManager.getColor(0, 0, 1));
		treffer.setFont(SWTResourceManager.getFont("", 12, SWT.NONE)); //$NON-NLS-1$
		treffer.setText("no"); //$NON-NLS-1$
		treffer.setBounds(665, 110, 33, 19);
		addUIControl(treffer, "hits"); //$NON-NLS-1$

		final Button openCustomerButton = new Button(container, SWT.NONE);
		openCustomerButton.setFont(SWTResourceManager.getFont("", 12, SWT.BOLD)); //$NON-NLS-1$
		openCustomerButton.setText("Open"); //$NON-NLS-1$
		openCustomerButton.setBounds(250, 460, 133, 34);
		addUIControl(openCustomerButton, "open"); //$NON-NLS-1$

		final Button newCustomerButton = new Button(container, SWT.NONE);
		newCustomerButton.setFont(SWTResourceManager.getFont("", 12, SWT.BOLD)); //$NON-NLS-1$
		newCustomerButton.setText("New "); //$NON-NLS-1$
		newCustomerButton.setBounds(407, 460, 133, 34);
		addUIControl(newCustomerButton, "new"); //$NON-NLS-1$

		final Label hitsLabel = new Label(container, SWT.NONE);
		hitsLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		hitsLabel.setForeground(SWTResourceManager.getColor(0, 0, 1));
		hitsLabel.setFont(SWTResourceManager.getFont("", 12, SWT.BOLD)); //$NON-NLS-1$
		hitsLabel.setText("Hits"); //$NON-NLS-1$
		hitsLabel.setBounds(703, 111, 30, 19);

		final Composite composite = new Composite(container, SWT.BORDER);
		composite.setBackground(SWTResourceManager.getColor(255, 255, 255));
		composite.setBounds(-12, 86, 1027, 349);

	}

}
