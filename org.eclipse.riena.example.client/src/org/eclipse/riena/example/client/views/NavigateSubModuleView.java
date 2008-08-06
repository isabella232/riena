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

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.riena.example.client.controllers.NavigateSubModuleController;
import org.eclipse.riena.internal.example.client.utils.UIControlsFactory;
import org.eclipse.riena.navigation.ui.swt.views.InjectAllAtOnceViewBindingDelegate;
import org.eclipse.riena.navigation.ui.swt.views.SWTViewBindingDelegate;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * SWT {@link IComboRidget} sample.
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
	}

	@Override
	protected SWTViewBindingDelegate createBinding() {
		return new InjectAllAtOnceViewBindingDelegate();
	}

}
