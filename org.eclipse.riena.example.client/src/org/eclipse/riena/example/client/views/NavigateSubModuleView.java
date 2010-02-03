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
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import org.eclipse.riena.example.client.controllers.NavigateSubModuleController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * View of a sub module to demonstrate the navigate method of {@code
 * INavigationNode}.
 */
public class NavigateSubModuleView extends SubModuleView<NavigateSubModuleController> {

	public static final String ID = NavigateSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		parent.setLayout(new GridLayout(1, false));
		GridDataFactory fillFactory = GridDataFactory.fillDefaults();

		UIControlsFactory.createLabel(parent, "Where do you want to go today?"); //$NON-NLS-1$

		Button comboAndList = UIControlsFactory.createButton(parent);
		fillFactory.applyTo(comboAndList);
		addUIControl(comboAndList, "comboAndList"); //$NON-NLS-1$

		Button tableTextAndTree = UIControlsFactory.createButton(parent);
		fillFactory.applyTo(tableTextAndTree);
		addUIControl(tableTextAndTree, "tableTextAndTree"); //$NON-NLS-1$

		Button textAssembly = UIControlsFactory.createButton(parent);
		fillFactory.applyTo(textAssembly);
		addUIControl(textAssembly, "textAssembly"); //$NON-NLS-1$

		Button btnNavigateToRidget = UIControlsFactory.createButton(parent, "Navigate to First Name", //$NON-NLS-1$
				"btnNavigateToRidget"); //$NON-NLS-1$
		fillFactory.applyTo(btnNavigateToRidget);
	}

}
