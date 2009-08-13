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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
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

	private Color colorLightBlue;

	public static final String ID = MasterDetailsSubModuleView2.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		colorLightBlue = new Color(parent.getDisplay(), 222, 232, 247);
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		FillLayout layout = new FillLayout(SWT.HORIZONTAL);
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		parent.setLayout(layout);
		createMasterDetails(parent);
	}

	@Override
	public void dispose() {
		colorLightBlue.dispose();
		super.dispose();
	}

	// helping methods
	//////////////////

	private Group createMasterDetails(Composite parent) {
		Group result = UIControlsFactory.createGroup(parent, "Master/Details:"); //$NON-NLS-1$
		FillLayout layout = new FillLayout(SWT.HORIZONTAL);
		layout.marginHeight = 20;
		layout.marginWidth = 20;
		result.setLayout(layout);

		MasterDetailsComposite mdComposite = new MasterDetailsComposite(result, SWT.NONE, SWT.TOP) {
			@Override
			protected Composite createButtons(Composite parent) {
				Composite result = UIControlsFactory.createComposite(parent, SWT.BORDER);
				GridDataFactory.fillDefaults().applyTo(result);

				RowLayout buttonLayout = new RowLayout(SWT.VERTICAL);
				buttonLayout.marginTop = 10;
				buttonLayout.fill = true;
				result.setLayout(buttonLayout);

				Button btnNew = createButtonNew(result);
				addUIControl(btnNew, BIND_ID_NEW);

				Button btnRemove = createButtonRemove(result);
				addUIControl(btnRemove, BIND_ID_REMOVE);

				return result;
			}
		};
		mdComposite.setBackground(colorLightBlue);
		addUIControl(mdComposite, "master2"); //$NON-NLS-1$

		Composite details = mdComposite.getDetails();
		GridLayout gridLayout = new GridLayout(3, false);
		gridLayout.horizontalSpacing = 6;
		details.setLayout(gridLayout);
		{
			Text txtFirst = UIControlsFactory.createText(details);
			txtFirst.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			mdComposite.addUIControl(txtFirst, "first"); //$NON-NLS-1$

			Text txtLast = UIControlsFactory.createText(details);
			txtLast.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			mdComposite.addUIControl(txtLast, "last"); //$NON-NLS-1$

			Button btnApply = new Button(details, SWT.PUSH | SWT.FLAT);
			mdComposite.addUIControl(btnApply, MasterDetailsComposite.BIND_ID_APPLY);
		}

		return result;
	}
}
