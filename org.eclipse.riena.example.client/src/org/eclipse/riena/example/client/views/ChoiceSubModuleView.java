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
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.IMultipleChoiceRidget;
import org.eclipse.riena.ui.ridgets.ISingleChoiceRidget;
import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * SWT {@link ISingleChoiceRidget} and {@link IMultipleChoiceRidget} example.
 */
public class ChoiceSubModuleView extends SubModuleView<ChoiceSubModuleController> {

	public static final String ID = ChoiceSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
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
		Group group = UIControlsFactory.createGroup(parent, "James' Car Configurator:"); //$NON-NLS-1$
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(2).spacing(20, 20).applyTo(group);

		GridDataFactory choiceLayoutFactory = GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true,
				false);

		// next row

		Label lblModel = UIControlsFactory.createLabel(group, "Model"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.BEGINNING).applyTo(lblModel);

		Composite compositeCarModel = new ChoiceComposite(group, SWT.NONE, false);
		choiceLayoutFactory.applyTo(compositeCarModel);
		addUIControl(compositeCarModel, "compositeCarModel"); //$NON-NLS-1$

		// next row

		Label lblExtras = UIControlsFactory.createLabel(group, "Extras"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.BEGINNING).applyTo(lblExtras);

		Composite compositeCarExtras = new ChoiceComposite(group, SWT.NONE, true);
		choiceLayoutFactory.applyTo(compositeCarExtras);
		addUIControl(compositeCarExtras, "compositeCarExtras"); //$NON-NLS-1$

		// next row

		UIControlsFactory.createLabel(group, "Manufacturer's\nWarranty"); //$NON-NLS-1$

		Composite compositeCarWarranty = new ChoiceComposite(group, SWT.NONE, false);
		choiceLayoutFactory.applyTo(compositeCarWarranty);
		addUIControl(compositeCarWarranty, "compositeCarWarranty"); //$NON-NLS-1$

		// next row

		UIControlsFactory.createLabel(group, "License Plate(s)"); //$NON-NLS-1$

		ChoiceComposite compositeCarPlates = new ChoiceComposite(group, SWT.NONE, true);
		compositeCarPlates.setOrientation(SWT.HORIZONTAL);
		choiceLayoutFactory.applyTo(compositeCarPlates);
		addUIControl(compositeCarPlates, "compositeCarPlates"); //$NON-NLS-1$

		// next row

		UIControlsFactory.createLabel(group, "Price"); //$NON-NLS-1$

		Text txtPrice = UIControlsFactory.createText(group, SWT.RIGHT);
		int widthHint = UIControlsFactory.getWidthHint(txtPrice, 12);
		GridDataFactory.swtDefaults().hint(widthHint, SWT.DEFAULT).applyTo(txtPrice);
		addUIControl(txtPrice, "txtPrice"); //$NON-NLS-1$

		Composite buttonComposite = createButtonComposite(group);
		GridDataFactory.fillDefaults().span(2, 1).grab(true, false).applyTo(buttonComposite);

		return group;
	}

	private Composite createButtonComposite(Group group) {
		Composite buttonComposite = UIControlsFactory.createComposite(group);
		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(buttonComposite);

		Button buttonPreset = UIControlsFactory.createButton(buttonComposite);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.END, SWT.BEGINNING).applyTo(buttonPreset);
		addUIControl(buttonPreset, "buttonPreset"); //$NON-NLS-1$

		Button buttonReset = UIControlsFactory.createButton(buttonComposite);
		addUIControl(buttonReset, "buttonReset"); //$NON-NLS-1$

		return buttonComposite;
	}

}
