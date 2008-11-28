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

import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.controller.AbstractWindowController;

/**
 * The controller for the hello dialog of the dialog example.
 */
public class HelloDialogController extends AbstractWindowController {

	public static final String RIDGET_ID_INPUT = "input"; //$NON-NLS-1$
	public static final String RIDGET_ID_OK = "okButton"; //$NON-NLS-1$
	public static final String RIDGET_ID_CANCEL = "cancelButton"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.ui.ridgets.controller.AbstractWindowController#
	 * configureRidgets()
	 */
	@Override
	public void configureRidgets() {

		super.configureRidgets();

		getWindowRidget().setTitle("Hello Dialog"); //$NON-NLS-1$

		ITextRidget input = (ITextRidget) getRidget(RIDGET_ID_INPUT);
		input.setText("Input please"); //$NON-NLS-1$

		IActionRidget okAction = (IActionRidget) getRidget(RIDGET_ID_OK);
		okAction.addListener(new IActionListener() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.riena.ui.ridgets.IActionListener#callback()
			 */
			public void callback() {
				getWindowRidget().dispose();
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
			}
		});
	}
}
