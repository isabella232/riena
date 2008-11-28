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

import org.eclipse.riena.example.client.controllers.HelloDialogController;
import org.eclipse.riena.navigation.ui.swt.views.DialogView;
import org.eclipse.riena.ui.ridgets.controller.AbstractWindowController;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * The view for the hello dialog of the dialog example.
 */
public class HelloDialogView extends DialogView {

	public HelloDialogView() {
		super(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.navigation.ui.swt.views.DialogView#createController()
	 */
	@Override
	protected AbstractWindowController createController() {
		return new HelloDialogController();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.navigation.ui.swt.views.DialogView#buildView()
	 */
	@Override
	protected Control buildView(Composite parent) {

		super.buildView(parent);

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, true));

		UIControlsFactory.createLabel(composite, "Input"); //$NON-NLS-1$
		Text input = UIControlsFactory.createText(composite);
		addUIControl(input, HelloDialogController.RIDGET_ID_INPUT);

		Button okButton = UIControlsFactory.createButton(composite);
		okButton.setText("Ok"); //$NON-NLS-1$
		addUIControl(okButton, HelloDialogController.RIDGET_ID_OK);

		Button cancelButton = UIControlsFactory.createButton(composite);
		cancelButton.setText("Cancel"); //$NON-NLS-1$
		addUIControl(cancelButton, HelloDialogController.RIDGET_ID_CANCEL);

		return composite;
	}
}
