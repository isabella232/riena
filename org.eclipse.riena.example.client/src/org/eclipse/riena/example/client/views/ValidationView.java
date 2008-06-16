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

		GridDataFactory fillFactory = GridDataFactory.fillDefaults();

		Group group1 = createGroupOnEditValidation(parent);
		fillFactory.applyTo(group1);

		Group group2 = createGroupOnUpdateValidation(parent);
		fillFactory.applyTo(group2);
	}

	@Override
	protected ValidationViewController createController(ISubModuleNode subModuleNode) {
		return new ValidationViewController(subModuleNode);
	}

	// helping methods
	// ////////////////

	private Group createGroupOnEditValidation(Composite parent) {
		Group group = UIControlsFactory.createGroup(parent, "'On Edit' Validation Rules:");
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(3).applyTo(group);

		UIControlsFactory.createLabel(group, ""); //$NON-NLS-1$
		UIControlsFactory.createLabel(group, "Control Value");
		UIControlsFactory.createLabel(group, "Ridget Value");

		GridDataFactory fillFactory = GridDataFactory.fillDefaults();

		UIControlsFactory.createLabel(group, "Numbers only:");
		Text txtNumbersOnly = UIControlsFactory.createText(group);
		addUIControl(txtNumbersOnly, "txtNumbersOnly"); //$NON-NLS-1$
		Label lblNumbersOnly = UIControlsFactory.createLabelOutput(group);
		addUIControl(lblNumbersOnly, "lblNumbersOnly"); //$NON-NLS-1$
		fillFactory.applyTo(lblNumbersOnly);

		UIControlsFactory.createLabel(group, "Numbers only (direct writing):");
		Text txtNumbersOnlyDW = UIControlsFactory.createText(group);
		addUIControl(txtNumbersOnlyDW, "txtNumbersOnlyDW"); //$NON-NLS-1$
		Label lblNumbersOnlyDW = UIControlsFactory.createLabelOutput(group);
		addUIControl(lblNumbersOnlyDW, "lblNumbersOnlyDW"); //$NON-NLS-1$
		fillFactory.applyTo(lblNumbersOnlyDW);

		UIControlsFactory.createLabel(group, "Characters only:");
		Text txtCharactersOnly = UIControlsFactory.createText(group);
		addUIControl(txtCharactersOnly, "txtCharactersOnly"); //$NON-NLS-1$
		Label lblCharactersOnly = UIControlsFactory.createLabelOutput(group);
		addUIControl(lblCharactersOnly, "lblCharactersOnly"); //$NON-NLS-1$
		fillFactory.applyTo(lblCharactersOnly);

		UIControlsFactory.createLabel(group, "Expression (PDX##):");
		Text txtExpression = UIControlsFactory.createText(group);
		addUIControl(txtExpression, "txtExpression"); //$NON-NLS-1$
		fillFactory.applyTo(txtExpression);
		Label lblExpression = UIControlsFactory.createLabelOutput(group);
		addUIControl(lblExpression, "lblExpression"); //$NON-NLS-1$
		fillFactory.applyTo(lblExpression);

		UIControlsFactory.createLabel(group, "Length < 5:");
		Text txtLengthLessThan5 = UIControlsFactory.createText(group);
		addUIControl(txtLengthLessThan5, "txtLengthLessThan5"); //$NON-NLS-1$
		Label lblLengthLessThan5 = UIControlsFactory.createLabelOutput(group);
		addUIControl(lblLengthLessThan5, "lblLengthLessThan5"); //$NON-NLS-1$
		fillFactory.applyTo(lblLengthLessThan5);

		UIControlsFactory.createLabel(group, "Required and Lowercase:");
		Text txtRequiredLowercase = UIControlsFactory.createText(group);
		addUIControl(txtRequiredLowercase, "txtRequiredLowercase"); //$NON-NLS-1$
		Label lblRequiredLowercase = UIControlsFactory.createLabelOutput(group);
		addUIControl(lblRequiredLowercase, "lblRequiredLowercase"); //$NON-NLS-1$
		fillFactory.applyTo(lblRequiredLowercase);

		return group;
	}

	private Group createGroupOnUpdateValidation(Composite parent) {
		Group group = UIControlsFactory.createGroup(parent, "'On Update' Validation Rules:");
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(3).applyTo(group);

		UIControlsFactory.createLabel(group, ""); //$NON-NLS-1$
		UIControlsFactory.createLabel(group, "Control Value");
		UIControlsFactory.createLabel(group, "Ridget Value");

		GridDataFactory fillFactory = GridDataFactory.fillDefaults();

		UIControlsFactory.createLabel(group, "Numeric Range 18 to 80:");
		Text txtRange18to80 = UIControlsFactory.createTextNumeric(group);
		addUIControl(txtRange18to80, "txtRange18to80"); //$NON-NLS-1$
		fillFactory.applyTo(txtRange18to80);
		Label lblRange18to80 = UIControlsFactory.createLabelOutput(group);
		addUIControl(lblRange18to80, "lblRange18to80"); //$NON-NLS-1$
		fillFactory.applyTo(lblRange18to80);

		UIControlsFactory.createLabel(group, "Length between 5 and 10:");
		Text txtLength5to10 = UIControlsFactory.createText(group);
		addUIControl(txtLength5to10, "txtLength5to10"); //$NON-NLS-1$
		fillFactory.applyTo(txtLength5to10);
		Label lblLength5to10 = UIControlsFactory.createLabelOutput(group);
		addUIControl(lblLength5to10, "lblLength5to10"); //$NON-NLS-1$
		fillFactory.applyTo(lblLength5to10);

		UIControlsFactory.createLabel(group, "Valid date (dd.MM.yyyy):");
		Text txtDate = UIControlsFactory.createText(group);
		addUIControl(txtDate, "txtDate"); //$NON-NLS-1$
		fillFactory.applyTo(txtDate);
		Label lblDate = UIControlsFactory.createLabelOutput(group);
		addUIControl(lblDate, "lblDate"); //$NON-NLS-1$
		fillFactory.applyTo(lblDate);

		Label lbl = UIControlsFactory.createLabel(group, "Valid email:");
		Text txtEmail = UIControlsFactory.createText(group);
		addUIControl(txtEmail, "txtEmail"); //$NON-NLS-1$
		fillFactory.applyTo(txtEmail);
		Label lblEmail = UIControlsFactory.createLabelOutput(group);
		addUIControl(lblEmail, "lblEmail"); //$NON-NLS-1$
		fillFactory.applyTo(lblEmail);

		GridDataFactory.fillDefaults().grab(true, false).applyTo(lbl);

		return group;
	}

}