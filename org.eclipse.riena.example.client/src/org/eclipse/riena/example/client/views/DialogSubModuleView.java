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
import org.eclipse.riena.example.client.controllers.DialogSubModuleController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * SWT {@link IComboRidget} sample.
 */
public class DialogSubModuleView extends SubModuleView<DialogSubModuleController> {

	public static final String ID = DialogSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {

		parent.setBackground(LnfManager.getLnf().getColor(ILnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(1, false));
		GridDataFactory fillFactory = GridDataFactory.fillDefaults();

		Button openShell = UIControlsFactory.createButton(parent);
		fillFactory.applyTo(openShell);
		addUIControl(openShell, "openShell"); //$NON-NLS-1$

		Shell shell = new Shell();
		addUIControl(shell, "shell"); //$NON-NLS-1$
	}
}
