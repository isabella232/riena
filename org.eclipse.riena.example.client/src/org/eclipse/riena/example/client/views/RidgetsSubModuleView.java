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
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleNodeView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;

/**
 * View of the sub module that shows a set of UI controls.
 */
public class RidgetsSubModuleView extends SubModuleNodeView<RidgetsSubModuleController> {

	public static final String ID = "RidgetsSubModuleView"; //$NON-NLS-1$

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.SubModuleNodeView#basicCreatePartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void basicCreatePartControl(Composite parent) {

		parent.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
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

		Group buttonGroup = UIControlsFactory.createGroup(parent, "Buttons");
		buttonGroup.setLayout(new RowLayout(SWT.VERTICAL));
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, 0);
		fd.left = new FormAttachment(0, 0);
		buttonGroup.setLayoutData(fd);

		Button toggleButton = new Button(buttonGroup, SWT.TOGGLE);
		addUIControl(toggleButton, "toggleOne"); //$NON-NLS-1$

		Button checkBox = new Button(buttonGroup, SWT.CHECK);
		addUIControl(checkBox, "checkOne"); //$NON-NLS-1$

	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.SubModuleNodeView#createController(org.eclipse.riena.navigation.ISubModuleNode)
	 */
	@Override
	protected RidgetsSubModuleController createController(ISubModuleNode subModuleNode) {
		return new RidgetsSubModuleController(subModuleNode);
	}

}
