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
import org.eclipse.riena.example.client.controllers.LoginDialogController;
import org.eclipse.riena.navigation.ui.login.ILoginDialogView;
import org.eclipse.riena.navigation.ui.swt.views.DialogView;
import org.eclipse.riena.ui.ridgets.controller.AbstractWindowController;
import org.eclipse.riena.ui.ridgets.util.beans.IntegerBean;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * The view for the hello dialog of the dialog example.
 */
public class LoginDialogView extends DialogView implements ILoginDialogView {

	private static final GridData GD11FILL = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
	private static final GridData GD21FILL = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
	private static final GridData GD41FILL = new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1);

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
		GridLayoutFactory.fillDefaults().numColumns(1).margins(10, 20).applyTo(parent);

		Composite content = createContentView(parent);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(content);

		return content;
	}

	private Composite createContentView(Composite parent) {

		Composite content = UIControlsFactory.createComposite(parent);
		GridLayoutFactory.fillDefaults().numColumns(4).spacing(6, 9).equalWidth(false).applyTo(content);

		// dummy to occupy the cell
		UIControlsFactory.createLabel(content, "                  "); //$NON-NLS-1$

		UIControlsFactory.createLabel(content, "User"); //$NON-NLS-1$
		Text user = UIControlsFactory.createText(content);
		user.setLayoutData(GD21FILL);
		addUIControl(user, LoginDialogController.RIDGET_ID_USER);

		// dummy to occupy the cell
		UIControlsFactory.createLabel(content, ""); //$NON-NLS-1$

		UIControlsFactory.createLabel(content, "Password"); //$NON-NLS-1$
		Text password = UIControlsFactory.createText(content);
		password.setLayoutData(GD21FILL);
		addUIControl(password, LoginDialogController.RIDGET_ID_PASSWORD);

		// dummy to occupy the cell
		UIControlsFactory.createLabel(content, ""); //$NON-NLS-1$
		// dummy to occupy the cell
		UIControlsFactory.createLabel(content, ""); //$NON-NLS-1$

		Button okButton = UIControlsFactory.createButton(content);
		okButton.setText("   Login   "); //$NON-NLS-1$
		okButton.setLayoutData(GD11FILL);
		addUIControl(okButton, LoginDialogController.RIDGET_ID_OK);

		Button cancelButton = UIControlsFactory.createButton(content);
		cancelButton.setText("   Cancel   "); //$NON-NLS-1$
		cancelButton.setLayoutData(GD11FILL);
		addUIControl(cancelButton, LoginDialogController.RIDGET_ID_CANCEL);

		Label info = UIControlsFactory
				.createLabel(
						content,
						"\nTo authenticate with running Sample App Server\n    type: user=john, password=john.\n    To omit it just press login."); //$NON-NLS-1$
		info.setLayoutData(GD41FILL);

		addUIControl(UIControlsFactory.createMessageBox(content),
				LoginDialogController.RIDGET_ID_MESSAGE_LOGIN_EXCEPTION);

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
