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

import org.eclipse.riena.example.client.controllers.FilterExampleSubModuleController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 *
 */
public class FilterExampleSubModuleView extends SubModuleView<FilterExampleSubModuleController> {

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		final GridLayout gridLayout_3 = new GridLayout();
		parent.setLayout(gridLayout_3);
		parent.setBackgroundMode(SWT.INHERIT_FORCE);

		Group menuToolGroup;

		Group ridgetsGroup;

		Group navigationGroup;
		navigationGroup = new Group(parent, SWT.NONE);
		navigationGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		navigationGroup.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.verticalSpacing = 0;
		gridLayout_2.numColumns = 2;
		navigationGroup.setLayout(gridLayout_2);
		navigationGroup.setText("Navigation"); //$NON-NLS-1$

		final Label hideTheLabel = new Label(navigationGroup, SWT.NONE);
		hideTheLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		hideTheLabel
				.setText("* Hide the first two module groups (\"Shared View Demo\" and \"UIProcess\" )\n* Disable the two sub-modules \"Ridget\" and \"Navigation\"\n* Hide the two sub-modules \"Menu Item\" and \"External Definition\"\n* Disable the module \"Log Collector\"\n* Inside the module \"Playground\" disable the two sub-modules \"Tree\" and \"Tree Table\""); //$NON-NLS-1$

		final Button navigationButton = new Button(navigationGroup, SWT.TOGGLE);
		final GridData gd_navigationButton = new GridData(SWT.RIGHT, SWT.BOTTOM, false, false);
		gd_navigationButton.widthHint = 80;
		navigationButton.setLayoutData(gd_navigationButton);
		navigationButton.setText("activate"); //$NON-NLS-1$
		addUIControl(navigationButton, "navigationBtn"); //$NON-NLS-1$
		menuToolGroup = new Group(parent, SWT.NONE);
		menuToolGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		menuToolGroup.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.verticalSpacing = 0;
		gridLayout_1.numColumns = 2;
		menuToolGroup.setLayout(gridLayout_1);
		menuToolGroup.setText("Menu / Tool Bar"); //$NON-NLS-1$

		final Label disableAllLabel = new Label(menuToolGroup, SWT.WRAP);
		disableAllLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		disableAllLabel
				.setText("* Disable all menu items of the menu \"File\"\n* Disable the tool item \"Exit\"\n* Hide th menu \"Navigation menu\""); //$NON-NLS-1$

		final Button menuToolButton = new Button(menuToolGroup, SWT.TOGGLE);
		final GridData gd_menuToolButton = new GridData(SWT.RIGHT, SWT.BOTTOM, false, false);
		gd_menuToolButton.widthHint = 80;
		menuToolButton.setLayoutData(gd_menuToolButton);
		menuToolButton.setText("activate"); //$NON-NLS-1$
		addUIControl(menuToolButton, "menuToolItemBtn"); //$NON-NLS-1$
		ridgetsGroup = new Group(parent, SWT.NONE);
		ridgetsGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		ridgetsGroup.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		final GridLayout gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 0;
		gridLayout.numColumns = 2;
		ridgetsGroup.setLayout(gridLayout);
		ridgetsGroup.setText("Ridgets (module \"Playground\")"); //$NON-NLS-1$

		final Label inTheLabel = new Label(ridgetsGroup, SWT.NONE);
		inTheLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		inTheLabel
				.setText("* In the sub-module \"Text\" hide every text field, that displays a model value\n* In the sub-module \"Text\" set for every ridget the maximum length \"12\""); //$NON-NLS-1$

		final Button ridgetButton = new Button(ridgetsGroup, SWT.TOGGLE);
		final GridData gd_activateButton_1 = new GridData(SWT.RIGHT, SWT.BOTTOM, false, false);
		gd_activateButton_1.widthHint = 80;
		ridgetButton.setLayoutData(gd_activateButton_1);
		ridgetButton.setText("activate"); //$NON-NLS-1$
		addUIControl(ridgetButton, "ridgetBtn"); //$NON-NLS-1$

		final Group ridgetsGroup_1 = new Group(parent, SWT.NONE);
		ridgetsGroup_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		final GridLayout gridLayout_4 = new GridLayout();
		gridLayout_4.verticalSpacing = 0;
		gridLayout_4.numColumns = 2;
		ridgetsGroup_1.setLayout(gridLayout_4);
		ridgetsGroup_1.setText("Ridgets (sample)"); //$NON-NLS-1$

		final Label theSampleTextLabel = new Label(ridgetsGroup_1, SWT.NONE);
		final GridData gd_theSampleTextLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		theSampleTextLabel.setLayoutData(gd_theSampleTextLabel);
		theSampleTextLabel.setText("The sample text ridget has a mandatory marker"); //$NON-NLS-1$

		final Label disableTheFourLabel = new Label(ridgetsGroup_1, SWT.NONE);
		final GridData gd_disableTheFourLabel = new GridData(SWT.FILL, SWT.CENTER, true, false);
		disableTheFourLabel.setLayoutData(gd_disableTheFourLabel);
		disableTheFourLabel.setText("* Disable the three sample ridgets"); //$NON-NLS-1$

		final Button ridgetsDisableButton = new Button(ridgetsGroup_1, SWT.TOGGLE);
		ridgetsDisableButton.setLayoutData(new GridData(80, SWT.DEFAULT));
		ridgetsDisableButton.setText("activate"); //$NON-NLS-1$
		addUIControl(ridgetsDisableButton, "ridgetDisableBtn"); //$NON-NLS-1$

		final Label hiddeTheFourLabel = new Label(ridgetsGroup_1, SWT.NONE);
		hiddeTheFourLabel.setText("* Hide the three sample ridgets"); //$NON-NLS-1$

		final Button ridgetsHideButton = new Button(ridgetsGroup_1, SWT.TOGGLE);
		ridgetsHideButton.setLayoutData(new GridData(80, SWT.DEFAULT));
		ridgetsHideButton.setText("activate"); //$NON-NLS-1$
		addUIControl(ridgetsHideButton, "ridgetHideBtn"); //$NON-NLS-1$

		final Label onlyAllowLabel = new Label(ridgetsGroup_1, SWT.NONE);
		onlyAllowLabel.setText("* Only allow the two characters \"0\" and \"1\" in the sample text ridget"); //$NON-NLS-1$

		final Button ridgets01Button = new Button(ridgetsGroup_1, SWT.TOGGLE);
		ridgets01Button.setLayoutData(new GridData(80, SWT.DEFAULT));
		ridgets01Button.setText("activate"); //$NON-NLS-1$
		addUIControl(ridgets01Button, "ridget01Btn"); //$NON-NLS-1$

		final Composite composite = new Composite(ridgetsGroup_1, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		final GridLayout gridLayout_5 = new GridLayout();
		gridLayout_5.horizontalSpacing = 15;
		gridLayout_5.numColumns = 4;
		composite.setLayout(gridLayout_5);

		final Label sampleLabel = new Label(composite, SWT.NONE);
		sampleLabel.setText("Sample Label"); //$NON-NLS-1$
		addUIControl(sampleLabel, "sampleLabel"); //$NON-NLS-1$

		final Text sampleText = new Text(composite, SWT.BORDER);
		final GridData gd_sampleText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd_sampleText.minimumWidth = 80;
		sampleText.setLayoutData(gd_sampleText);
		addUIControl(sampleText, "sampleText"); //$NON-NLS-1$

		final Button button = new Button(composite, SWT.NONE);
		button.setText("Sample Button"); //$NON-NLS-1$
		addUIControl(button, "sampleBtn"); //$NON-NLS-1$

		initializeToolBar();

	}

	private void initializeToolBar() {
	}

}
