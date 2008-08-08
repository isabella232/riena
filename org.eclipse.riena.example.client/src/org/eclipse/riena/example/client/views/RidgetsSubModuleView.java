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

import org.eclipse.riena.example.client.controllers.RidgetsSubModuleController;
import org.eclipse.riena.internal.example.client.utils.UIControlsFactory;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * View of the sub module that shows a set of UI controls.
 */
public class RidgetsSubModuleView extends SubModuleView<RidgetsSubModuleController> {

	public static final String ID = RidgetsSubModuleView.class.getName();

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.SubModuleView#basicCreatePartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void basicCreatePartControl(Composite parent) {

		parent.setBackground(LnfManager.getLnf().getColor(ILnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new FormLayout());

		createButtonGroup(parent);

	}

	/**
	 * Creates a group with different buttons.
	 * 
	 * @param parent
	 *            - the parent of the group
	 */
	private void createButtonGroup(Composite parent) {

		Group buttonGroup = UIControlsFactory.createGroup(parent, "Buttons"); //$NON-NLS-1$
		buttonGroup.setLayout(new RowLayout(SWT.VERTICAL));
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, 0);
		fd.left = new FormAttachment(0, 0);
		buttonGroup.setLayoutData(fd);

		Button toggleButton = UIControlsFactory.createButtonToggle(buttonGroup);
		addUIControl(toggleButton, "toggleOne"); //$NON-NLS-1$

		Button checkBox = UIControlsFactory.createButtonCheck(buttonGroup);
		addUIControl(checkBox, "checkOne"); //$NON-NLS-1$

		Button buttonWithImage = UIControlsFactory.createButton(buttonGroup);
		addUIControl(buttonWithImage, "buttonWithImage"); //$NON-NLS-1$

	}

}
