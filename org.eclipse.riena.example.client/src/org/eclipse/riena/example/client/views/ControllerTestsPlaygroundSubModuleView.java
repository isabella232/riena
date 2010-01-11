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
package org.eclipse.riena.example.client.views;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.example.client.controllers.ControllerTestsPlaygroundSubModuleController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Example view with as many widgets as possible. Used for controller testing.
 */
public class ControllerTestsPlaygroundSubModuleView extends SubModuleView<ControllerTestsPlaygroundSubModuleController> {
	public ControllerTestsPlaygroundSubModuleView() {
	}

	public static final Object ID = ControllerTestsPlaygroundSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		Label label = UIControlsFactory.createLabel(parent,
				"Lots of widgets for lots of ridgets for lots of controller tests.", //$NON-NLS-1$
				SWT.NONE);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));

		Group tableGroup = UIControlsFactory.createGroup(parent, "table"); //$NON-NLS-1$
		tableGroup.setLayout(new GridLayout(2, false));
		tableGroup.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));

		createTableGroup(tableGroup);

		Group comboGroup = UIControlsFactory.createGroup(parent, "combo"); //$NON-NLS-1$
		comboGroup.setLayout(new GridLayout(2, false));
		comboGroup.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));

		createComboGroup(comboGroup);

		Group browserGroup = UIControlsFactory.createGroup(parent, "browser and link"); //$NON-NLS-1$
		browserGroup.setLayout(new GridLayout(2, false));
		browserGroup.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		createBrowserGroup(browserGroup);

		Group spinnerScaleGroup = UIControlsFactory.createGroup(parent, "spinner and scale"); //$NON-NLS-1$
		spinnerScaleGroup.setLayout(new GridLayout(2, false));
		spinnerScaleGroup.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));

		createSpinnerScaleGroup(spinnerScaleGroup);

		//		Group masterDetailsGroup = UIControlsFactory.createGroup(parent, "masterDetails"); //$NON-NLS-1$
		//		masterDetailsGroup.setLayout(new GridLayout(2, false));
		//		masterDetailsGroup.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		//		createMasterDetails(masterDetailsGroup);
		// TODO work in progress
	}

	//	private void createMasterDetails(Composite parent) {
	//
	//		MasterDetailsComposite mdComposite = UIControlsFactory.createMasterDetails(parent, "master"); //$NON-NLS-1$
	//		Composite details = mdComposite.getDetails();
	//		details.setLayout(new GridLayout(2, false));
	//
	//		UIControlsFactory.createLabel(details, "First Name:"); //$NON-NLS-1$
	//		Text txtFirst = UIControlsFactory.createText(details, SWT.BORDER, "first"); //$NON-NLS-1$
	//		txtFirst.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	//
	//		UIControlsFactory.createLabel(details, "Last Name:"); //$NON-NLS-1$
	//		Text txtLast = UIControlsFactory.createText(details, SWT.BORDER, "last"); //$NON-NLS-1$
	//		txtLast.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
	//
	//		UIControlsFactory.createLabel(details, "Gender:"); //$NON-NLS-1$
	//		ChoiceComposite ccGender = UIControlsFactory.createChoiceComposite(details, SWT.NONE, false);
	//		ccGender.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
	//		ccGender.setOrientation(SWT.HORIZONTAL);
	//		mdComposite.addUIControl(ccGender, "gender"); //$NON-NLS-1$
	//
	//		UIControlsFactory.createLabel(details, "Pets:"); //$NON-NLS-1$
	//		ChoiceComposite ccPets = UIControlsFactory.createChoiceComposite(details, SWT.NONE, true);
	//		ccPets.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
	//		ccPets.setOrientation(SWT.HORIZONTAL);
	//		mdComposite.addUIControl(ccPets, "pets"); //$NON-NLS-1$
	//	}

	/**
	 * @param parent
	 */
	private void createSpinnerScaleGroup(Composite parent) {

		Label lblDegreesFahrenheit = new Label(parent, SWT.NONE);
		lblDegreesFahrenheit.setText("Degrees Fahrenheit:"); //$NON-NLS-1$

		Spinner fahrenheitSpinner = new Spinner(parent, SWT.BORDER);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(fahrenheitSpinner, "fahrenheitSpinner"); //$NON-NLS-1$

		Label lblDegreeCelsius = new Label(parent, SWT.NONE);
		lblDegreeCelsius.setText("Degree Celsius:"); //$NON-NLS-1$

		Scale celsiusScale = new Scale(parent, SWT.NONE);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(celsiusScale, "celsiusScale"); //$NON-NLS-1$
		new Label(parent, SWT.NONE);

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite.setLayout(new GridLayout(3, false));

		Label lblZero = new Label(composite, SWT.NONE);
		lblZero.setText("0\u00B0"); //$NON-NLS-1$

		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));

		Label lblPlus50 = new Label(composite, SWT.NONE);
		lblPlus50.setText("50\u00B0"); //$NON-NLS-1$

	}

	/**
	 * @param parent
	 */
	private void createTableGroup(Composite parent) {
		Table multiTable = UIControlsFactory.createTable(parent, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION,
				"multiTable"); //$NON-NLS-1$
		multiTable.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));

		List tableList = UIControlsFactory.createList(parent, false, false, "tableList"); //$NON-NLS-1$
		tableList.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));

		Button tableButton = UIControlsFactory.createButton(parent, "copy selection", "copySelectionButton"); //$NON-NLS-1$ //$NON-NLS-2$
		tableButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	}

	/**
	 * @param comboGroup
	 */
	private void createComboGroup(Composite parent) {
		Label comboLabel = UIControlsFactory.createLabel(parent, "", SWT.NONE, "comboLabel"); //$NON-NLS-1$ //$NON-NLS-2$
		comboLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(parent, SWT.NONE);

		Combo combo = UIControlsFactory.createCombo(parent, "ageCombo"); //$NON-NLS-1$
		combo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));

		UIControlsFactory.createText(parent, SWT.BORDER, "comboTextField"); //$NON-NLS-1$

		Button addToComboButton = UIControlsFactory.createButton(parent, "add", "addToComboButton"); //$NON-NLS-1$ //$NON-NLS-2$
		addToComboButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
	}

	private void createBrowserGroup(Composite parent) {
		GridLayoutFactory.swtDefaults().numColumns(2).equalWidth(false).margins(20, 20).spacing(20, 5).applyTo(parent);
		UIControlsFactory.createLabel(parent, "Links:"); //$NON-NLS-1$
		UIControlsFactory.createLink(parent, SWT.NONE, "link1"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		UIControlsFactory.createLink(parent, SWT.NONE, "link2"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		UIControlsFactory.createLink(parent, SWT.NONE, "link3"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "URL:"); //$NON-NLS-1$
		Text textLinkUrl = UIControlsFactory.createText(parent, SWT.SINGLE, "textLinkUrl"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(true, false).applyTo(textLinkUrl);

		Label label = UIControlsFactory.createLabel(parent, "Browser:"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(false, true).applyTo(label);
		Browser browser = UIControlsFactory.createBrowser(parent, SWT.NONE, "browser"); //$NON-NLS-1$
		GridData gd_browser = new GridData(SWT.LEFT, SWT.FILL, true, false, 1, 1);
		gd_browser.widthHint = 150;
		gd_browser.heightHint = 50;
		browser.setLayoutData(gd_browser);
	}
}
