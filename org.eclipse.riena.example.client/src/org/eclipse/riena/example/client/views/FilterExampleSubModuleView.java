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
		menuToolGroup = new Group(parent, SWT.NONE);
		menuToolGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		menuToolGroup.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		final GridLayout gridLayout_1 = new GridLayout();
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
		gridLayout.numColumns = 2;
		ridgetsGroup.setLayout(gridLayout);
		ridgetsGroup.setText("Ridgets"); //$NON-NLS-1$

		final Label inTheLabel = new Label(ridgetsGroup, SWT.NONE);
		inTheLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		inTheLabel
				.setText("* In the sub-module \"Text\" hide every text field, that displays a model value\n* In the sub-module \"Text\" set for every widget the maximum length \"12\""); //$NON-NLS-1$

		final Button ridgetButton = new Button(ridgetsGroup, SWT.TOGGLE);
		final GridData gd_activateButton_1 = new GridData(SWT.RIGHT, SWT.BOTTOM, false, false);
		gd_activateButton_1.widthHint = 80;
		ridgetButton.setLayoutData(gd_activateButton_1);
		ridgetButton.setText("activate"); //$NON-NLS-1$
		addUIControl(ridgetButton, "ridgetBtn"); //$NON-NLS-1$
		navigationGroup = new Group(parent, SWT.NONE);
		navigationGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		navigationGroup.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		final GridLayout gridLayout_2 = new GridLayout();
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

		initializeToolBar();

	}

	private void initializeToolBar() {
	}

}
