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
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.example.client.controllers.MasterDetailsSubModuleController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.IMasterDetailsRidget;
import org.eclipse.riena.ui.swt.MasterDetailsComposite;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Demonstrates use of a master/details ridget.
 * 
 * @see IMasterDetailsRidget
 * @see MasterDetailsSubModuleController
 */
public class MasterDetailsSubModuleView2 extends SubModuleView<MasterDetailsSubModuleController> {

	public static final String ID = MasterDetailsSubModuleView2.class.getName();

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

		MasterDetailsComposite mdComposite = new MasterDetailsComposite(result, SWT.NONE, SWT.TOP) {
			protected void createDetails(Composite parent) {
				GridLayoutFactory.fillDefaults().numColumns(3).spacing(6, 0).equalWidth(false).applyTo(parent);

				Text txtFirst = UIControlsFactory.createText(parent);
				GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(txtFirst);
				addUIControl(txtFirst, "first"); //$NON-NLS-1$

				Text txtLast = UIControlsFactory.createText(parent);
				GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(txtLast);
				addUIControl(txtLast, "last"); //$NON-NLS-1$

				Button btnApply = new Button(parent, SWT.PUSH | SWT.FLAT);
				addUIControl(btnApply, MasterDetailsComposite.BIND_ID_APPLY);
			}

			@Override
			protected Control createButtonApply(Composite btnComposite) {
				return null;
			}
		};
		addUIControl(mdComposite, "master2"); //$NON-NLS-1$

		return result;
	}

	private FillLayout createFillLayout(int margin) {
		FillLayout result = new FillLayout(SWT.HORIZONTAL);
		result.marginHeight = margin;
		result.marginWidth = margin;
		return result;
	}
}
