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
import org.eclipse.riena.example.client.controllers.ChoiceSubModuleController;
import org.eclipse.riena.internal.example.client.utils.UIControlsFactory;
import org.eclipse.riena.internal.ui.ridgets.swt.ChoiceComposite;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.IMultipleChoiceRidget;
import org.eclipse.riena.ui.ridgets.ISingleChoiceRidget;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * SWT {@link ISingleChoiceRidget} and {@link IMultipleChoiceRidget} sample.
 */
public class ChoiceSubModuleView extends SubModuleView<ChoiceSubModuleController> {

	public static final String ID = ChoiceSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(ILnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(1, false));

		Group group1 = createChoiceGroup(parent);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(group1);
	}

	@Override
	protected ChoiceSubModuleController createController(ISubModuleNode subModuleNode) {
		return new ChoiceSubModuleController(subModuleNode);
	}

	// helping methods
	// ////////////////

	private Group createChoiceGroup(Composite parent) {
		Group group = UIControlsFactory.createGroup(parent, "James' Car Configurator:");
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(2).spacing(20, 20).applyTo(group);

		// next row

		Label lblModel = UIControlsFactory.createLabel(group, "Model");
		GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.BEGINNING).applyTo(lblModel);

		Composite compositeCarModel = new ChoiceComposite(group, SWT.NONE, false);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false).applyTo(compositeCarModel);
		addUIControl(compositeCarModel, "compositeCarModel"); //$NON-NLS-1$

		// next row

		Label lblExtras = UIControlsFactory.createLabel(group, "Extras");
		GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.BEGINNING).applyTo(lblExtras);

		Composite compositeCarExtras = new Composite(group, SWT.NONE);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false).applyTo(compositeCarExtras);
		createCheck(compositeCarExtras, "Front Maschine Guns");
		createCheck(compositeCarExtras, "Self Destruct Button");
		createCheck(compositeCarExtras, "Underwater Package");
		createCheck(compositeCarExtras, "Park Distance Control System");
		compositeCarExtras.setLayout(new FillLayout(SWT.VERTICAL));

		// next row

		UIControlsFactory.createLabel(group, "Manufacturer's\nWarranty");

		Composite compositeCarWarranty = new Composite(group, SWT.NONE);
		createRadio(compositeCarWarranty, "Standard");
		createRadio(compositeCarWarranty, "Extended");
		compositeCarWarranty.setLayout(new FillLayout(SWT.VERTICAL));

		// next row

		UIControlsFactory.createLabel(group, "License Plate(s)");

		Composite compositeCarPlates = new Composite(group, SWT.NONE);
		compositeCarPlates.setLayout(new RowLayout(SWT.HORIZONTAL));
		createCheck(compositeCarPlates, "JM5B0ND");
		createCheck(compositeCarPlates, "1 SPY");
		createCheck(compositeCarPlates, "MNY PNY");
		createCheck(compositeCarPlates, "BN D007");
		createCheck(compositeCarPlates, "Q RULE2");
		createCheck(compositeCarPlates, "MI64EVR");

		// next row

		UIControlsFactory.createLabel(group, "Price");

		Text txtPrice = UIControlsFactory.createText(group);
		addUIControl(txtPrice, "txtPrice"); //$NON-NLS-1$

		createButtonComposite(group);

		return group;
	}

	private Composite createButtonComposite(Group group) {
		Composite buttonComposite = UIControlsFactory.createComposite(group);
		GridDataFactory.fillDefaults().grab(true, false).span(4, 1).applyTo(buttonComposite);
		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(buttonComposite);

		Button buttonReset = UIControlsFactory.createButton(buttonComposite);
		GridDataFactory.fillDefaults().span(1, 1).grab(true, false).align(SWT.END, SWT.BEGINNING).applyTo(buttonReset);
		addUIControl(buttonReset, "buttonReset"); //$NON-NLS-1$

		Button buttonBMW = UIControlsFactory.createButton(buttonComposite);
		GridDataFactory.fillDefaults().align(SWT.END, SWT.BEGINNING).applyTo(buttonBMW);
		addUIControl(buttonBMW, "buttonBMW"); //$NON-NLS-1$

		return buttonComposite;
	}

	private void createRadio(Composite parent, String caption) {
		Button b = UIControlsFactory.createButtonRadio(parent);
		b.setText(caption);
	}

	private void createCheck(Composite parent, String caption) {
		Button b = UIControlsFactory.createButtonCheck(parent);
		b.setText(caption);
	}
}
