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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.example.client.controllers.DefaultButtonSubModuleController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.IDateTimeRidget;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * SWT {@link IDateTimeRidget} example.
 */
public class DefaultButtonSubModuleView extends SubModuleView<DefaultButtonSubModuleController> {

	public static final String ID = DefaultButtonSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(1, false));

		GridDataFactory gdf = GridDataFactory.fillDefaults().grab(true, false);

		Group groupOne = createGroupOne(parent);
		gdf.applyTo(groupOne);

		Group groupTwo = createGroupTwo(parent);
		gdf.applyTo(groupTwo);

		Group groupThree = createGroupThree(parent);
		gdf.applyTo(groupThree);
	}

	// helping methods
	// ////////////////

	private Group createGroupOne(Composite parent) {
		Group group = createGroup(parent, "Example #1 - One Default Button", "group1"); //$NON-NLS-1$ //$NON-NLS-2$
		createTextFields(group, "input1", "output1", "button1"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return group;
	}

	private Group createGroupTwo(Composite parent) {
		Group group = createGroup(parent, "Example #2 - Two Defaults Buttons", "group2"); //$NON-NLS-1$ //$NON-NLS-2$
		createTextFields(group, "input2a", "output2a", "button2a"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		createTextFields(group, "input2b", "output2b", "button2b"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return group;
	}

	private Group createGroupThree(Composite parent) {
		Group group = createGroup(parent, "Example #3 - Nested Default Buttons", "group3a");//$NON-NLS-1$ //$NON-NLS-2$ 
		createTextFields(group, "input3a", "output3a", "button3a");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		Group subGroup = createGroup(group, "Nested Group", "group3b"); //$NON-NLS-1$ //$NON-NLS-2$
		GridDataFactory.fillDefaults().span(3, 1).applyTo(subGroup);
		createTextFields(subGroup, "input3b", "output3b", "button3b");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		return group;
	}

	private Group createGroup(Composite parent, String grpTitle, String groupId) {
		Group result = UIControlsFactory.createGroup(parent, grpTitle, groupId);
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(3).applyTo(result);
		return result;
	}

	private void createTextFields(Composite parent, String inputId, String outputId, String buttonId) {
		UIControlsFactory.createLabel(parent, "Input:"); //$NON-NLS-1$
		Text txtInput = UIControlsFactory.createText(parent, SWT.BORDER, inputId);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtInput);
		UIControlsFactory.createButton(parent, "Apply", buttonId); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Output:"); //$NON-NLS-1$
		Text txtOutput = UIControlsFactory.createText(parent, SWT.BORDER, outputId);
		GridDataFactory.fillDefaults().applyTo(txtOutput);
		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
	}
}
