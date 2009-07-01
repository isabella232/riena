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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import org.eclipse.riena.example.client.controllers.ComboAndChoiceSubModuleController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 *
 */
public class ComboAndChoiceSubModuleView extends SubModuleView<ComboAndChoiceSubModuleController> {
	public ComboAndChoiceSubModuleView() {
	}

	public static final String ID = ComboAndChoiceSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setLayout(new FormLayout());

		Group grpComboBoxes = UIControlsFactory.createGroup(parent, "Combo boxes"); //$NON-NLS-1$
		grpComboBoxes.setLayout(new FormLayout());
		FormData formDataGrpComboBoxes = new FormData();
		formDataGrpComboBoxes.top = new FormAttachment(0, 41);
		formDataGrpComboBoxes.right = new FormAttachment(100, -42);
		formDataGrpComboBoxes.left = new FormAttachment(0, 33);
		grpComboBoxes.setLayoutData(formDataGrpComboBoxes);

		Combo combo1 = UIControlsFactory.createCombo(grpComboBoxes, "comboBoxWithModel"); //$NON-NLS-1$
		FormData formDataCombo1 = new FormData();
		formDataCombo1.width = 75;
		formDataCombo1.top = new FormAttachment(0, 10);
		formDataCombo1.left = new FormAttachment(0, 10);
		combo1.setLayoutData(formDataCombo1);

		Combo combo2 = UIControlsFactory.createCombo(grpComboBoxes, "comboBoxWithoutModel"); //$NON-NLS-1$
		FormData formDataCombo2 = new FormData();
		formDataCombo2.width = 75;
		formDataCombo2.top = new FormAttachment(combo1, 17);
		formDataCombo2.left = new FormAttachment(combo1, 0, SWT.LEFT);
		combo2.setLayoutData(formDataCombo2);

		Label lblComboboxWithModel = UIControlsFactory.createLabel(grpComboBoxes, "ComboBox with model", SWT.NONE, //$NON-NLS-1$
				"labelComboBoxWithModel"); //$NON-NLS-1$
		lblComboboxWithModel.setAlignment(SWT.RIGHT);
		FormData formDataLblComboboxWithModel = new FormData();
		formDataLblComboboxWithModel.top = new FormAttachment(0, 10);
		formDataLblComboboxWithModel.bottom = new FormAttachment(combo1, 0, SWT.BOTTOM);
		formDataLblComboboxWithModel.left = new FormAttachment(combo1, 29);
		lblComboboxWithModel.setLayoutData(formDataLblComboboxWithModel);

		Label lblComboboxWithoutModel = UIControlsFactory.createLabel(grpComboBoxes, "ComboBox without model", //$NON-NLS-1$
				SWT.NONE, "myLabelId"); //$NON-NLS-1$
		lblComboboxWithoutModel.setAlignment(SWT.RIGHT);
		FormData formDataLblComboboxWithoutModel = new FormData();
		formDataLblComboboxWithoutModel.left = new FormAttachment(combo2, 29);
		formDataLblComboboxWithoutModel.bottom = new FormAttachment(combo2, 0, SWT.BOTTOM);
		formDataLblComboboxWithoutModel.top = new FormAttachment(combo2, 0, SWT.TOP);
		lblComboboxWithoutModel.setLayoutData(formDataLblComboboxWithoutModel);

		Button btnBindToModel = UIControlsFactory.createButton(grpComboBoxes, "bind to model", "bindComboToModel");
		FormData formDataBtnBindToModel = new FormData();
		formDataBtnBindToModel.left = new FormAttachment(lblComboboxWithoutModel, 43);
		formDataBtnBindToModel.bottom = new FormAttachment(combo2, 0, SWT.BOTTOM);
		btnBindToModel.setLayoutData(formDataBtnBindToModel);

		Group grpSingleChoice = UIControlsFactory.createGroup(parent, "Single choice");
		formDataGrpComboBoxes.bottom = new FormAttachment(grpSingleChoice, -23);
		grpSingleChoice.setLayout(new FormLayout());
		FormData formDataGrpSingleChoice = new FormData();
		formDataGrpSingleChoice.top = new FormAttachment(0, 252);
		formDataGrpSingleChoice.right = new FormAttachment(100, -42);
		formDataGrpSingleChoice.left = new FormAttachment(0, 33);
		grpSingleChoice.setLayoutData(formDataGrpSingleChoice);

		Composite compositeNumberModel = new ChoiceComposite(grpSingleChoice, SWT.NONE, false);
		FormData formDataCompositeNumberModel = new FormData();
		formDataCompositeNumberModel.height = 50;
		compositeNumberModel.setLayoutData(formDataCompositeNumberModel);
		addUIControl(compositeNumberModel, "compositeNumberModel"); //$NON-NLS-1$

		Button btnBindChoiceToModel = UIControlsFactory.createButton(grpSingleChoice, "bind to model", //$NON-NLS-1$
				"bindChoiceToModel"); //$NON-NLS-1$
		formDataCompositeNumberModel.bottom = new FormAttachment(btnBindChoiceToModel, -6);
		formDataCompositeNumberModel.left = new FormAttachment(btnBindChoiceToModel, 0, SWT.LEFT);
		FormData formDataBtnBindChoiceToModel = new FormData();
		formDataBtnBindChoiceToModel.top = new FormAttachment(0, 66);
		btnBindChoiceToModel.setLayoutData(formDataBtnBindChoiceToModel);

		Button btnUpdateAllRidgets = UIControlsFactory.createButton(parent, "update all ridgets from model", //$NON-NLS-1$
				"updateAllRidgetsFromModel"); //$NON-NLS-1$
		FormData formDataBtnUpdateAllRidgets = new FormData();
		formDataBtnUpdateAllRidgets.left = new FormAttachment(0, 33);
		formDataBtnUpdateAllRidgets.top = new FormAttachment(0, 418);
		formDataGrpSingleChoice.bottom = new FormAttachment(btnUpdateAllRidgets, -30);
		btnUpdateAllRidgets.setLayoutData(formDataBtnUpdateAllRidgets);

		Label lblBindChoice = UIControlsFactory.createLabel(grpSingleChoice, "lblBindChoice"); //$NON-NLS-1$
		formDataBtnBindChoiceToModel.left = new FormAttachment(lblBindChoice, 0, SWT.LEFT);
		lblBindChoice
				.setText("click \"bind to model\" and \"update all ridgets from model\" to visualize radio buttons"); //$NON-NLS-1$
		FormData formDataLblBindChoice = new FormData();
		formDataLblBindChoice.left = new FormAttachment(0, 10);
		formDataLblBindChoice.right = new FormAttachment(100);
		formDataLblBindChoice.top = new FormAttachment(btnBindChoiceToModel, 8);
		formDataLblBindChoice.height = 15;
		formDataLblBindChoice.width = 411;
		lblBindChoice.setLayoutData(formDataLblBindChoice);

		Composite lblComposite = UIControlsFactory.createComposite(grpComboBoxes, SWT.NONE);
		lblComposite.setLayout(new FormLayout());
		FormData formDataLblComposite = new FormData();
		formDataLblComposite.right = new FormAttachment(btnBindToModel, 0, SWT.RIGHT);
		formDataLblComposite.bottom = new FormAttachment(100, -22);
		formDataLblComposite.left = new FormAttachment(combo1, 0, SWT.LEFT);
		formDataLblComposite.width = 202;
		formDataLblComposite.height = 34;
		lblComposite.setLayoutData(formDataLblComposite);

		Label lblBindCombo1 = UIControlsFactory.createLabel(lblComposite,
				"to see the values of the \"ComboBox without model\""); //$NON-NLS-1$
		FormData formDataLblBindCombo1 = new FormData();
		formDataLblBindCombo1.bottom = new FormAttachment(100, -2);
		lblBindCombo1.setLayoutData(formDataLblBindCombo1);

		Label lblBindCombo2 = UIControlsFactory.createLabel(lblComposite,
				"click \"bind to model\" and \"update all ridgets from model\","); //$NON-NLS-1$
		FormData formDataLblBindCombo2 = new FormData();
		formDataLblBindCombo2.top = new FormAttachment(0);
		formDataLblBindCombo2.left = new FormAttachment(lblBindCombo1, 0, SWT.LEFT);
		lblBindCombo2.setLayoutData(formDataLblBindCombo2);

	}
}
