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
import org.eclipse.riena.example.client.controllers.TextSubModuleController;
import org.eclipse.riena.internal.example.client.utils.UIControlsFactory;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * SWT {@link ITextFieldRidget} sample.
 */
public class TextSubModuleView extends SubModuleView<TextSubModuleController> {

	public static final String ID = TextSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(ILnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(2, true));

		UIControlsFactory.createLabel(parent, "Text Field:");
		Text textField = UIControlsFactory.createText(parent);
		addUIControl(textField, "textField"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Model:");
		Text textModel1 = UIControlsFactory.createText(parent);
		textModel1.setEnabled(false);
		textModel1.setEditable(false);
		addUIControl(textModel1, "textModel1"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Text Field (direct writing):");
		Text textDirectWrite = UIControlsFactory.createText(parent);
		addUIControl(textDirectWrite, "textDirectWrite"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Model:");
		Text textModel2 = UIControlsFactory.createText(parent);
		textModel2.setEnabled(false);
		textModel2.setEditable(false);
		addUIControl(textModel2, "textModel2"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Text Area:");
		Text textArea = UIControlsFactory.createText(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		int heightHint = (textArea.getLineHeight() * 5) + (textArea.getBorderWidth() * 2);
		GridDataFactory.fillDefaults().hint(SWT.DEFAULT, heightHint).applyTo(textArea);
		addUIControl(textArea, "textArea"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Password Field:");
		Text textPassword = UIControlsFactory.createText(parent, SWT.SINGLE | SWT.PASSWORD);
		textPassword.setEchoChar('*');
		addUIControl(textPassword, "textPassword"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Max. Length (<= 10 char):");
		Text textField10 = UIControlsFactory.createText(parent);
		textField10.setTextLimit(10);
		addUIControl(textField10, "textField10"); //$NON-NLS-1$
	}

}
