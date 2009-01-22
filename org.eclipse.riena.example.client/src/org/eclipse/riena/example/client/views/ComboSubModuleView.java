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
import org.eclipse.riena.example.client.controllers.ComboSubModuleController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * SWT {@link IComboRidget} sample.
 */
public class ComboSubModuleView extends SubModuleView<ComboSubModuleController> {

	public static final String ID = ComboSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(1, false));
		GridDataFactory fillFactory = GridDataFactory.fillDefaults();

		UIControlsFactory.createLabel(parent, "&Persons:"); //$NON-NLS-1$

		Combo combo = UIControlsFactory.createCombo(parent);
		fillFactory.applyTo(combo);
		addUIControl(combo, "comboOne"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "&First Name:"); //$NON-NLS-1$
		Text textFirst = UIControlsFactory.createText(parent);
		fillFactory.applyTo(textFirst);
		addUIControl(textFirst, "textFirst"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "&Last Name:"); //$NON-NLS-1$
		Text textLast = UIControlsFactory.createText(parent);
		fillFactory.applyTo(textLast);
		addUIControl(textLast, "textLast"); //$NON-NLS-1$

		Button buttonSave = UIControlsFactory.createButton(parent);
		fillFactory.applyTo(buttonSave);
		addUIControl(buttonSave, "buttonSave"); //$NON-NLS-1$
	}

}
