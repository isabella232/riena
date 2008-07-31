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
import org.eclipse.riena.example.client.controllers.UIProcessDemoSubModuleController;
import org.eclipse.riena.internal.example.client.utils.UIControlsFactory;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleNodeView;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public class UiProcessDemoSubModuleView extends SubModuleNodeView<UIProcessDemoSubModuleController> {

	public final static String ID = UiProcessDemoSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(ILnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(1, true));
		Group group = createUIProcessGroup(parent);
		GridDataFactory.fillDefaults().applyTo(group);

	}

	private Group createUIProcessGroup(Composite parent) {
		Group group = UIControlsFactory.createGroup(parent, "&UIProcess visualization:"); //$NON-NLS-1$
		group.setLayout(new GridLayout(1, true));

		Button startUIProcess = UIControlsFactory.createButton(group);
		int xHint = UIControlsFactory.getWidthHint(startUIProcess) + 10;
		GridDataFactory.fillDefaults().hint(xHint, SWT.DEFAULT).applyTo(startUIProcess);
		addUIControl(startUIProcess, "actionRidget"); //$NON-NLS-1$

		Button startJob = UIControlsFactory.createButton(group);
		xHint = UIControlsFactory.getWidthHint(startJob);
		GridDataFactory.fillDefaults().hint(xHint, SWT.DEFAULT).applyTo(startJob);
		addUIControl(startJob, "actionRidgetJob"); //$NON-NLS-1$

		return group;
	}

	@Override
	protected UIProcessDemoSubModuleController createController(ISubModuleNode subModuleNode) {
		return new UIProcessDemoSubModuleController(getCurrentNode());
	}

}
