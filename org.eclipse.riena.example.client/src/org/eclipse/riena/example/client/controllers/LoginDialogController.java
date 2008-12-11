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
package org.eclipse.riena.example.client.controllers;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.riena.example.client.application.ExampleIcons;
import org.eclipse.riena.internal.example.client.Activator;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.controller.AbstractWindowController;
import org.eclipse.riena.ui.ridgets.util.beans.IntegerBean;
import org.eclipse.riena.ui.swt.utils.ImageUtil;

/**
 * The controller for the hello dialog of the dialog example.
 */
public class LoginDialogController extends AbstractWindowController {

	public static final String RIDGET_ID_USER = "user"; //$NON-NLS-1$
	public static final String RIDGET_ID_PASSWORD = "password"; //$NON-NLS-1$
	public static final String RIDGET_ID_OK = "okButton"; //$NON-NLS-1$
	public static final String RIDGET_ID_CANCEL = "cancelButton"; //$NON-NLS-1$
	public static final int EXIT_ABORT = -1;

	private IntegerBean result;

	public LoginDialogController(IntegerBean result) {

		super();

		this.result = result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.ui.ridgets.controller.AbstractWindowController#
	 * configureRidgets()
	 */
	@Override
	public void configureRidgets() {

		super.configureRidgets();

		getWindowRidget().setTitle("Riena login"); //$NON-NLS-1$
		// TODO:getWindowRidget().setIcon(IconManagerAccessor.fetchIconManager().getIconID(IIconManager.LOGIN, IconSize.A));
		getWindowRidget().setIcon(getIconPath(ExampleIcons.ICON_SAMPLE));

		((ITextRidget) getRidget(RIDGET_ID_USER)).setText("john"); //$NON-NLS-1$
		((ITextRidget) getRidget(RIDGET_ID_PASSWORD)).setText("john"); //$NON-NLS-1$

		IActionRidget okAction = (IActionRidget) getRidget(RIDGET_ID_OK);
		okAction.addListener(new IActionListener() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.riena.ui.ridgets.IActionListener#callback()
			 */
			public void callback() {
				getWindowRidget().dispose();
				result.setValue(checkLogin() ? IApplication.EXIT_OK : IApplication.EXIT_RESTART);
			}
		});
		IActionRidget cancelAction = (IActionRidget) getRidget(RIDGET_ID_CANCEL);
		cancelAction.addListener(new IActionListener() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.riena.ui.ridgets.IActionListener#callback()
			 */
			public void callback() {
				getWindowRidget().dispose();
				result.setValue(EXIT_ABORT);
			}
		});
	}

	public void onClose() {

		result.setValue(EXIT_ABORT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.ui.ridgets.controller.AbstractWindowController#afterBind
	 * ()
	 */
	@Override
	public void afterBind() {

		super.afterBind();

		getWindowRidget().setDefaultButton(getRidget(RIDGET_ID_OK).getUIControl());
	}

	private boolean checkLogin() {
		return "john".equalsIgnoreCase(((ITextRidget) getRidget(RIDGET_ID_USER)).getText()) //$NON-NLS-1$
				&& "john".equals(((ITextRidget) getRidget(RIDGET_ID_PASSWORD)).getText()); //$NON-NLS-1$
	}

	private String getIconPath(String subPath) {
		return ImageUtil.getIconPath(Activator.getDefault().getBundle(), subPath);
	}
}
