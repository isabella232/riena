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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.example.client.controllers.ListSubModuleController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * SWT {@link IListRidget} sample.
 */
public class ListSubModuleView extends SubModuleView<ListSubModuleController> {

	public static final String ID = ListSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(2, true));

		Group group1 = createListGroup(parent);
		GridDataFactory.fillDefaults().applyTo(group1);

		Group group2 = createEditGroup(parent);
		GridDataFactory.fillDefaults().applyTo(group2);
	}

	// helping methods
	// ////////////////

	protected Group createListGroup(Composite parent) {
		Group group = UIControlsFactory.createGroup(parent, "&Persons:"); //$NON-NLS-1$
		group.setLayout(new GridLayout(2, true));

		List listPersons = UIControlsFactory.createList(group, true, true);
		GridDataFactory.fillDefaults().grab(false, true).span(2, 1).applyTo(listPersons);
		addUIControl(listPersons, "listPersons"); //$NON-NLS-1$

		Button buttonSort = UIControlsFactory.createButtonCheck(group);
		GridDataFactory.fillDefaults().grab(false, true).span(2, 1).applyTo(buttonSort);
		addUIControl(buttonSort, "buttonSort"); //$NON-NLS-1$

		Button buttonAdd = UIControlsFactory.createButton(group);
		addUIControl(buttonAdd, "buttonAdd"); //$NON-NLS-1$
		int xHint = UIControlsFactory.getWidthHint(buttonAdd);
		GridDataFactory.fillDefaults().hint(xHint, SWT.DEFAULT).applyTo(buttonAdd);

		Button buttonRemove = UIControlsFactory.createButton(group);
		addUIControl(buttonRemove, "buttonRemove"); //$NON-NLS-1$
		xHint = UIControlsFactory.getWidthHint(buttonRemove);
		GridDataFactory.fillDefaults().hint(xHint, SWT.DEFAULT).applyTo(buttonRemove);

		return group;
	}

	private Group createEditGroup(Composite parent) {
		Group group = UIControlsFactory.createGroup(parent, "Edit:"); //$NON-NLS-1$
		group.setLayout(new GridLayout(1, false));

		GridDataFactory fillFactory = GridDataFactory.fillDefaults().grab(true, false);

		UIControlsFactory.createLabel(group, "&First Name:"); //$NON-NLS-1$
		Text textFirst = UIControlsFactory.createText(group);

		fillFactory.applyTo(textFirst);
		addUIControl(textFirst, "textFirst"); //$NON-NLS-1$

		UIControlsFactory.createLabel(group, "&Last Name:"); //$NON-NLS-1$
		Text textLast = UIControlsFactory.createText(group);
		fillFactory.applyTo(textLast);
		addUIControl(textLast, "textLast"); //$NON-NLS-1$

		Button buttonSave = UIControlsFactory.createButton(group);
		fillFactory.applyTo(buttonSave);
		addUIControl(buttonSave, "buttonSave"); //$NON-NLS-1$

		return group;
	}

}
