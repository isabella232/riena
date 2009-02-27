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
import org.eclipse.riena.example.client.controllers.MasterDetailsSubModuleController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.IMasterDetailsRidget;
import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.riena.ui.swt.MasterDetailsComposite;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

/**
 * Demonstrates use of a master/details ridget.
 * 
 * @see IMasterDetailsRidget
 * @see MasterDetailsSubModuleController
 */
public class MasterDetailsSubModuleView extends SubModuleView<MasterDetailsSubModuleController> {

	public static final String ID = MasterDetailsSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(createFillLayout(5));
		createMasterDetails(parent);
	}

	// helping methods
	//////////////////

	private Group createMasterDetails(Composite parent) {
		Group result = UIControlsFactory.createGroup(parent, "Master/Details:"); //$NON-NLS-1$
		result.setLayout(createFillLayout(20));

		MasterDetailsComposite mdComposite = new MasterDetailsComposite(result, SWT.NONE, SWT.BOTTOM) {
			protected void createDetails(Composite parent) {
				GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(parent);

				UIControlsFactory.createLabel(parent, "First Name:"); //$NON-NLS-1$
				Text txtFirst = UIControlsFactory.createText(parent);
				GridDataFactory.fillDefaults().grab(true, false).applyTo(txtFirst);
				addUIContol(txtFirst, "first"); //$NON-NLS-1$

				UIControlsFactory.createLabel(parent, "Last Name:"); //$NON-NLS-1$
				Text txtLast = UIControlsFactory.createText(parent);
				GridDataFactory.fillDefaults().grab(true, false).applyTo(txtLast);
				addUIContol(txtLast, "last"); //$NON-NLS-1$

				UIControlsFactory.createLabel(parent, "Gender:"); //$NON-NLS-1$
				ChoiceComposite ccGender = new ChoiceComposite(parent, SWT.NONE, false);
				ccGender.setOrientation(SWT.HORIZONTAL);
				addUIContol(ccGender, "gender"); //$NON-NLS-1$

				UIControlsFactory.createLabel(parent, "Pets:"); //$NON-NLS-1$
				ChoiceComposite ccPets = new ChoiceComposite(parent, SWT.NONE, true);
				ccPets.setOrientation(SWT.HORIZONTAL);
				addUIContol(ccPets, "pets"); //$NON-NLS-1$
			}
		};
		addUIControl(mdComposite, "master"); //$NON-NLS-1$

		return result;
	}

	private FillLayout createFillLayout(int margin) {
		FillLayout result = new FillLayout(SWT.HORIZONTAL);
		result.marginHeight = margin;
		result.marginWidth = margin;
		return result;
	}
}
