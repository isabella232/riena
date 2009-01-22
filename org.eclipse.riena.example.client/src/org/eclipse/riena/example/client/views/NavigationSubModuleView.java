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
import org.eclipse.riena.example.client.controllers.NavigationSubModuleController;
import org.eclipse.riena.navigation.ui.swt.binding.InjectSwtViewBindingDelegate;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.swt.uibinding.AbstractViewBindingDelegate;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * View of the sub-module to add module-groups, modules etc. dynamically.
 */
public class NavigationSubModuleView extends SubModuleView<NavigationSubModuleController> {

	public static final String ID = NavigationSubModuleView.class.getName();

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.SubModuleView#basicCreatePartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(1, false));

		Button addSubModuleToModuleBtn = UIControlsFactory.createButton(parent);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		addSubModuleToModuleBtn.setLayoutData(gridData);
		addUIControl(addSubModuleToModuleBtn, "addSubModuleToModuleBtn"); //$NON-NLS-1$

		Button addSubModuleToSelfBtn = UIControlsFactory.createButton(parent);
		gridData = GridDataFactory.copyData(gridData);
		addSubModuleToSelfBtn.setLayoutData(gridData);
		addUIControl(addSubModuleToSelfBtn, "addSubModuleToSelfBtn"); //$NON-NLS-1$

		Button addModuleBtn = UIControlsFactory.createButton(parent);
		gridData = GridDataFactory.copyData(gridData);
		addModuleBtn.setLayoutData(gridData);
		addUIControl(addModuleBtn, "addModuleBtn"); //$NON-NLS-1$

		Button addModuleGroupBtn = UIControlsFactory.createButton(parent);
		gridData = GridDataFactory.copyData(gridData);
		addModuleGroupBtn.setLayoutData(gridData);
		addUIControl(addModuleGroupBtn, "addModuleGroupBtn"); //$NON-NLS-1$

	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.SubModuleView#createBinding()
	 */
	@Override
	protected AbstractViewBindingDelegate createBinding() {
		return new InjectSwtViewBindingDelegate();
	}

}
