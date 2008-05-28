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
import org.eclipse.riena.example.client.controllers.TextViewController;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleNodeView;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * SWT {@link ITextFieldRidget} sample.
 */
public class TextView extends SubModuleNodeView<TextViewController> {

	public static final String ID = TextView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		parent.setLayout(new GridLayout(2, true));

		createLabel(parent, "Text Field:");
		Text textField = createText(parent, SWT.SINGLE);
		addUIControl(textField, "textField"); //$NON-NLS-1$

		createLabel(parent, "Model:");
		Text textModel1 = createText(parent, SWT.SINGLE);
		textModel1.setEnabled(false);
		textModel1.setEditable(false);
		addUIControl(textModel1, "textModel1"); //$NON-NLS-1$

		createLabel(parent, "Text Field (direct writing):");
		Text textDirectWrite = createText(parent, SWT.SINGLE);
		addUIControl(textDirectWrite, "textDirectWrite"); //$NON-NLS-1$

		createLabel(parent, "Model:");
		Text textModel2 = createText(parent, SWT.SINGLE);
		textModel2.setEnabled(false);
		textModel2.setEditable(false);
		addUIControl(textModel2, "textModel2"); //$NON-NLS-1$

		createLabel(parent, "Text Area:");
		Text textArea = createText(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		int heightHint = (textArea.getLineHeight() * 5) + (textArea.getBorderWidth() * 2);
		GridDataFactory.fillDefaults().hint(SWT.DEFAULT, heightHint).applyTo(textArea);
		addUIControl(textArea, "textArea"); //$NON-NLS-1$

		createLabel(parent, "Password Field:");
		Text textPassword = createText(parent, SWT.SINGLE | SWT.PASSWORD);
		textPassword.setEchoChar('*');
		addUIControl(textPassword, "textPassword"); //$NON-NLS-1$

		createLabel(parent, "Max. Length (<= 10 char):");
		Text textField10 = createText(parent, SWT.SINGLE);
		textField10.setTextLimit(10);
		addUIControl(textField10, "textField10"); //$NON-NLS-1$
	}

	@Override
	protected TextViewController createController(ISubModuleNode subModuleNode) {
		return new TextViewController(subModuleNode);
	}

	// helping methods
	// ////////////////

	private Label createLabel(Composite parent, String caption) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(caption);
		label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		return label;
	}

	private Text createText(Composite parent, int style) {
		Text text = new Text(parent, style | SWT.BORDER);
		GridDataFactory.fillDefaults().applyTo(text);
		return text;
	}

}
