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
package org.eclipse.riena.example.client.views;

import org.eclipse.riena.example.client.controllers.OnePersonSubModuleController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.swtdesigner.SWTResourceManager;

/**
 * This view displays the data of one person.
 */
public class OnePersonSubModuleView extends SubModuleView<OnePersonSubModuleController> {

	private Text townText;
	private Text postalCodeText;
	private Combo countryCombo;
	private Text streetText;
	private Text BirthplaceText;
	private Text birthdayText;
	private Text firstNameText;
	private Text lastNameText;
	private Text customerNumberText;
	public static final String ID = OnePersonSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 7;
		parent.setLayout(gridLayout);
		parent.setBackgroundMode(SWT.INHERIT_FORCE);

		final Label personLabel = new Label(parent, SWT.NONE);
		final GridData gd_personLabel = new GridData(75, SWT.DEFAULT);
		personLabel.setLayoutData(gd_personLabel);
		personLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		personLabel.setText("Person"); //$NON-NLS-1$

		final Label customerNumberLabel = new Label(parent, SWT.NONE);
		customerNumberLabel.setLayoutData(new GridData());
		customerNumberLabel.setText("Customer Number"); //$NON-NLS-1$

		customerNumberText = new Text(parent, SWT.BORDER);
		customerNumberText.setData("type", "numeric"); //$NON-NLS-1$ //$NON-NLS-2$
		customerNumberText.setData("binding_property", "customerNumber"); //$NON-NLS-1$ //$NON-NLS-2$
		final GridData gd_customerNumberText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		customerNumberText.setLayoutData(gd_customerNumberText);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		final Label lastNameLabel = new Label(parent, SWT.NONE);
		lastNameLabel.setLayoutData(new GridData());
		lastNameLabel.setText("Last Name"); //$NON-NLS-1$

		lastNameText = new Text(parent, SWT.BORDER);
		lastNameText.setData("binding_property", "lastName"); //$NON-NLS-1$ //$NON-NLS-2$
		final GridData gd_lastNameText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		lastNameText.setLayoutData(gd_lastNameText);

		final Label firstNameLabel = new Label(parent, SWT.NONE);
		final GridData gd_firstNameLabel = new GridData();
		gd_firstNameLabel.horizontalIndent = 10;
		firstNameLabel.setLayoutData(gd_firstNameLabel);
		firstNameLabel.setText("First Name"); //$NON-NLS-1$

		firstNameText = new Text(parent, SWT.BORDER);
		firstNameText.setData("binding_property", "firstName"); //$NON-NLS-1$ //$NON-NLS-2$
		final GridData gd_firstNameText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		firstNameText.setLayoutData(gd_firstNameText);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		final Label birthdayLabel = new Label(parent, SWT.NONE);
		birthdayLabel.setLayoutData(new GridData());
		birthdayLabel.setText("Birthday"); //$NON-NLS-1$

		birthdayText = new Text(parent, SWT.BORDER);
		birthdayText.setData("binding_property", "birthday"); //$NON-NLS-1$ //$NON-NLS-2$
		birthdayText.setData("type", "date"); //$NON-NLS-1$ //$NON-NLS-2$
		final GridData gd_birthdayText = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		gd_birthdayText.widthHint = 75;
		birthdayText.setLayoutData(gd_birthdayText);

		final Label birthplaceLabel = new Label(parent, SWT.NONE);
		final GridData gd_birthplaceLabel = new GridData();
		gd_birthplaceLabel.horizontalIndent = 10;
		birthplaceLabel.setLayoutData(gd_birthplaceLabel);
		birthplaceLabel.setText("Birthplace"); //$NON-NLS-1$

		BirthplaceText = new Text(parent, SWT.BORDER);
		BirthplaceText.setData("binding_property", "birthplace"); //$NON-NLS-1$ //$NON-NLS-2$
		final GridData gd_birthplaceText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		BirthplaceText.setLayoutData(gd_birthplaceText);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		final Label genderLabel = new Label(parent, SWT.NONE);
		final GridData gd_genderLabel = new GridData();
		genderLabel.setLayoutData(gd_genderLabel);
		genderLabel.setText("Gender"); //$NON-NLS-1$

		final ChoiceComposite genderChoiceComposite = new ChoiceComposite(parent, SWT.NONE, false);
		genderChoiceComposite.setOrientation(SWT.HORIZONTAL);
		genderChoiceComposite.setData("binding_property", "gender"); //$NON-NLS-1$ //$NON-NLS-2$
		final GridData gd_genderChoiceComposite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		genderChoiceComposite.setLayoutData(gd_genderChoiceComposite);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		final Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		final GridData gd_separator = new GridData(SWT.FILL, SWT.CENTER, false, false, 7, 1);
		separator.setLayoutData(gd_separator);

		final Label addressLabel = new Label(parent, SWT.NONE);
		addressLabel.setLayoutData(new GridData());
		addressLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		addressLabel.setText("Address"); //$NON-NLS-1$

		final Label streetAndNumberLabel = new Label(parent, SWT.NONE);
		streetAndNumberLabel.setLayoutData(new GridData());
		streetAndNumberLabel.setText("Street and Number"); //$NON-NLS-1$

		streetText = new Text(parent, SWT.BORDER);
		streetText.setData("binding_property", "street"); //$NON-NLS-1$ //$NON-NLS-2$
		final GridData gd_streetText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		streetText.setLayoutData(gd_streetText);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		final Label countryLabel = new Label(parent, SWT.NONE);
		countryLabel.setLayoutData(new GridData());
		countryLabel.setText("Country"); //$NON-NLS-1$

		countryCombo = new Combo(parent, SWT.READ_ONLY);
		countryCombo.setData("binding_property", "country"); //$NON-NLS-1$ //$NON-NLS-2$
		final GridData gd_countryCombo = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		countryCombo.setLayoutData(gd_countryCombo);

		final Label postalCodeLabel = new Label(parent, SWT.NONE);
		final GridData gd_postalCodeLabel = new GridData();
		gd_postalCodeLabel.horizontalIndent = 10;
		postalCodeLabel.setLayoutData(gd_postalCodeLabel);
		postalCodeLabel.setText("Postal Code"); //$NON-NLS-1$

		postalCodeText = new Text(parent, SWT.BORDER);
		postalCodeText.setData("type", "numeric"); //$NON-NLS-1$ //$NON-NLS-2$
		postalCodeText.setData("binding_property", "postalCode"); //$NON-NLS-1$ //$NON-NLS-2$
		final GridData gd_postalCodeText = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		gd_postalCodeText.widthHint = 35;
		postalCodeText.setLayoutData(gd_postalCodeText);

		final Label townLabel = new Label(parent, SWT.NONE);
		final GridData gd_townLabel = new GridData();
		gd_townLabel.horizontalIndent = 10;
		townLabel.setLayoutData(gd_townLabel);
		townLabel.setText("Town"); //$NON-NLS-1$

		townText = new Text(parent, SWT.BORDER);
		townText.setData("binding_property", "town"); //$NON-NLS-1$ //$NON-NLS-2$
		final GridData gd_townText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		townText.setLayoutData(gd_townText);

		final Label label = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 7, 1));
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		final Button showButton = new Button(parent, SWT.NONE);
		showButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		showButton.setData("binding_property", "show"); //$NON-NLS-1$ //$NON-NLS-2$
		showButton.setText("Show"); //$NON-NLS-1$
		initializeToolBar();
	}

	private void initializeToolBar() {
		//
	}

}
