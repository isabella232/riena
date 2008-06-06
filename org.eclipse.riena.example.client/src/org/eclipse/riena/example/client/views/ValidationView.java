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
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.riena.example.client.controllers.ValidationViewController;
import org.eclipse.riena.internal.example.client.utils.UIControlsFactory;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleNodeView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Sample for validation rules.
 */
public class ValidationView extends SubModuleNodeView<ValidationViewController> {

	public static final String ID = ValidationView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		parent.setLayout(new GridLayout(1, false));

		createGroup(parent);
	}

	@Override
	protected ValidationViewController createController(ISubModuleNode subModuleNode) {
		return new ValidationViewController(subModuleNode);
	}

	// helping methods
	// ////////////////

	private Group createGroup(Composite parent) {
		Group group = UIControlsFactory.createGroup(parent, "Validation Rules:");
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(2).applyTo(group);

		UIControlsFactory.createLabel(group, "Numbers only:");
		Text txtNumbersOnly = UIControlsFactory.createText(group);
		addUIControl(txtNumbersOnly, "txtNumbersOnly"); //$NON-NLS-1$

		UIControlsFactory.createLabel(group, "Numbers only (direct writing):");
		Text txtNumbersOnlyDW = UIControlsFactory.createText(group);
		addUIControl(txtNumbersOnlyDW, "txtNumbersOnlyDW"); //$NON-NLS-1$

		UIControlsFactory.createLabel(group, "Lowercase only:");
		Text txtLowercase = UIControlsFactory.createText(group);
		addUIControl(txtLowercase, "txtLowercase"); //$NON-NLS-1$

		Label label = UIControlsFactory.createLabel(group, "--- stuff below is work in progress ---");
		GridDataFactory.fillDefaults().span(2, 1).applyTo(label);

		UIControlsFactory.createLabel(group, "Numeric Range 18 to 80:");
		Text txtRange18to80 = UIControlsFactory.createTextNumeric(group);
		addUIControl(txtRange18to80, "txtRange18to80"); //$NON-NLS-1$

		UIControlsFactory.createLabel(group, "Length between 5 and 10:");
		Text txtLength5to10 = UIControlsFactory.createText(group);
		addUIControl(txtLength5to10, "txtLength5to10"); //$NON-NLS-1$

		UIControlsFactory.createLabel(group, "Valid email:");
		Text txtEmail = UIControlsFactory.createText(group);
		addUIControl(txtEmail, "txtEmail"); //$NON-NLS-1$

		return group;
	}
}