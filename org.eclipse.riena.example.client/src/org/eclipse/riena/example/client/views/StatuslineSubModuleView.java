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
import org.eclipse.riena.example.client.controllers.StatuslineSubModuleViewController;
import org.eclipse.riena.internal.example.client.utils.UIControlsFactory;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleNodeView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * View of the sub-module that is an example how to display something in the
 * status line.
 */
public class StatuslineSubModuleView extends SubModuleNodeView<StatuslineSubModuleViewController> {

	public static final String ID = StatuslineSubModuleView.class.getName();

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.SubModuleNodeView#basicCreatePartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void basicCreatePartControl(Composite parent) {

		parent.setLayout(new GridLayout(1, false));

		GridDataFactory fillFactory = GridDataFactory.fillDefaults();

		Group messageGroup = createMessageGroup(parent);
		fillFactory.applyTo(messageGroup);

		Group numberGroup = createNumberGroup(parent);
		fillFactory.applyTo(numberGroup);

	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.SubModuleNodeView#createController
	 *      (org.eclipse.riena.navigation.ISubModuleNode)
	 */
	@Override
	protected StatuslineSubModuleViewController createController(ISubModuleNode subModuleNode) {
		return new StatuslineSubModuleViewController(subModuleNode);
	}

	/**
	 * Creates the group with all the controls to regulate and show the message
	 * in the status line.
	 * 
	 * @param parent
	 * @return message group
	 */
	private Group createMessageGroup(Composite parent) {

		Group group = UIControlsFactory.createGroup(parent, "Message:"); //$NON-NLS-1$
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(2).applyTo(group);

		Label textLabel = new Label(group, SWT.NONE);
		textLabel.setText("Text:"); //$NON-NLS-1$

		Text text = new Text(group, SWT.BORDER | SWT.SINGLE);
		GridData gridData = new GridData();
		gridData.widthHint = 200;
		text.setLayoutData(gridData);
		addUIControl(text, "messageText"); //$NON-NLS-1$

		Label severitylabel = new Label(group, SWT.NONE);
		severitylabel.setText("Severity:"); //$NON-NLS-1$

		Combo severityCombo = UIControlsFactory.createCombo(group);
		addUIControl(severityCombo, "severity"); //$NON-NLS-1$

		Button showBtn = UIControlsFactory.createButton(group);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.BEGINNING;
		gridData.horizontalSpan = 2;
		showBtn.setLayoutData(gridData);
		addUIControl(showBtn, "showMessage"); //$NON-NLS-1$

		return group;

	}

	/**
	 * Creates the group with all the controls to regulate and show the number
	 * in the status line.
	 * 
	 * @param parent
	 * @return number group
	 */
	private Group createNumberGroup(Composite parent) {

		Group group = UIControlsFactory.createGroup(parent, "Number:"); //$NON-NLS-1$
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(2).applyTo(group);

		Label textLabel = new Label(group, SWT.NONE);
		textLabel.setText("Number:"); //$NON-NLS-1$

		Text text = new Text(group, SWT.BORDER | SWT.SINGLE);
		GridData gridData = new GridData();
		gridData.widthHint = 42;
		text.setLayoutData(gridData);
		addUIControl(text, "numberText"); //$NON-NLS-1$

		Button showBtn = UIControlsFactory.createButton(group);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.BEGINNING;
		gridData.horizontalSpan = 2;
		showBtn.setLayoutData(gridData);
		addUIControl(showBtn, "showNumber"); //$NON-NLS-1$

		return group;

	}

}
