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

import org.eclipse.riena.example.client.controllers.MessageBoxSubModuleController;
import org.eclipse.riena.internal.ui.ridgets.swt.MessageBox;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class MessageBoxSubModuleView extends SubModuleView<MessageBoxSubModuleController> {

	public static final String ID = MessageBoxSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {

		parent.setBackground(LnfManager.getLnf().getColor(ILnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(2, true));

		UIControlsFactory.createLabel(parent, "Title"); //$NON-NLS-1$
		Text messageTitle = UIControlsFactory.createText(parent);
		addUIControl(messageTitle, "messageTitle"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Text"); //$NON-NLS-1$
		Text messageText = UIControlsFactory.createText(parent);
		addUIControl(messageText, "messageText"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Type"); //$NON-NLS-1$
		Combo messageType = UIControlsFactory.createCombo(parent);
		addUIControl(messageType, "messageType"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Options"); //$NON-NLS-1$
		Combo messageOptions = UIControlsFactory.createCombo(parent);
		addUIControl(messageOptions, "messageOptions"); //$NON-NLS-1$

		Button showMessage = UIControlsFactory.createButton(parent);
		showMessage.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 2, 1));
		addUIControl(showMessage, "showMessage"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Selected Option"); //$NON-NLS-1$
		Text selectedOption = UIControlsFactory.createText(parent);
		addUIControl(selectedOption, "selectedOption"); //$NON-NLS-1$

		MessageBox messageBox = new MessageBox(parent);
		messageBox.setPropertyName("messageBox"); //$NON-NLS-1$
		addUIControl(messageBox, "messageBox"); //$NON-NLS-1$

		//		CellConstraints cc = new CellConstraints();
		//		builder.add(UIControlsFactory.createSectionLabelWithText("Message displayed in a popup dialog"), cc //$NON-NLS-1$
		//				.xyw(2, 2, 6));
	}
}
