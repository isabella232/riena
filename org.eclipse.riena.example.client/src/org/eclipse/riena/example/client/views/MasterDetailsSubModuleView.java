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
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

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
		parent.setLayout(new GridLayout(1, false));

		Group group = UIControlsFactory.createGroup(parent, "Master/Details:"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(true, true).applyTo(group);
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(1).applyTo(group);

		UIControlsFactory.createLabel(group, "...coming soon to a CVS near you...");
	}

}
