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

import org.eclipse.equinox.app.IApplication;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.riena.example.client.controllers.HelloDialogController;
import org.eclipse.riena.example.client.controllers.LoginDialogController;
import org.eclipse.riena.navigation.ui.login.ILoginDialogView;
import org.eclipse.riena.navigation.ui.swt.views.DialogView;
import org.eclipse.riena.ui.ridgets.controller.AbstractWindowController;
import org.eclipse.riena.ui.ridgets.util.beans.IntegerBean;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
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
public class LoginDialogView extends DialogView implements ILoginDialogView {

	private IntegerBean result;

	public LoginDialogView() {

		super(null);

		this.result = new IntegerBean(IApplication.EXIT_OK);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.navigation.ui.swt.views.DialogView#createController()
	 */
	@Override
	protected AbstractWindowController createController() {
		return new LoginDialogController(result);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.navigation.ui.swt.views.DialogView#buildView()
	 */
	@Override
	protected Control buildView(Composite parent) {

		super.buildView(parent);

		parent.setBackground(LnfManager.getLnf().getColor(ILnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(1, false));

		Composite content = createContentView(parent);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(content);

		return content;
	}

	private Composite createContentView(Composite parent) {

		Composite content = UIControlsFactory.createComposite(parent);
		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(content);

		UIControlsFactory.createLabel(content, "User"); //$NON-NLS-1$
		Text user = UIControlsFactory.createText(content);
		addUIControl(user, LoginDialogController.RIDGET_ID_USER);

		UIControlsFactory.createLabel(content, "Password"); //$NON-NLS-1$
		Text password = UIControlsFactory.createText(content);
		addUIControl(password, LoginDialogController.RIDGET_ID_PASSWORD);

		Button okButton = UIControlsFactory.createButton(content);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.END, SWT.BEGINNING).applyTo(okButton);
		okButton.setText("Ok"); //$NON-NLS-1$
		addUIControl(okButton, HelloDialogController.RIDGET_ID_OK);

		Button cancelButton = UIControlsFactory.createButton(content);
		cancelButton.setText("Cancel"); //$NON-NLS-1$
		addUIControl(cancelButton, HelloDialogController.RIDGET_ID_CANCEL);

		return content;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.internal.navigation.ui.login.ILoginDialogView#getResult
	 * ()
	 */
	public int getResult() {
		return result.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.navigation.ui.swt.views.DialogView#onClose()
	 */
	@Override
	protected void onClose() {

		super.onClose();

		((LoginDialogController) getController()).onClose();
	}
}
