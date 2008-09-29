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

import org.eclipse.riena.example.client.views.TextSubModuleView;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;
import org.eclipse.riena.ui.ridgets.util.beans.IntegerBean;

/**
 * Controller for the {@link TextSubModuleView} example. TODO [ev] docs
 */
public class TextNumericSubModuleController extends SubModuleController {

	@Override
	public void afterBind() {
		super.afterBind();
		configureRidgets();
	}

	/**
	 * Binds and updates the ridgets.
	 */
	public void configureRidgets() {
		ITextFieldRidget txtString = (ITextFieldRidget) getRidget("txtString"); //$NON-NLS-1$
		txtString.bindToModel(getRidget("lblString"), ILabelRidget.PROPERTY_TEXT); //$NON-NLS-1$
		txtString.setText("Dr Livingstone, I presume?"); //$NON-NLS-1$

		ITextFieldRidget txtInteger = (ITextFieldRidget) getRidget("txtInteger"); //$NON-NLS-1$
		txtInteger.bindToModel(new IntegerBean(1234), "value"); //$NON-NLS-1$
		txtString.updateFromModel();

		ILabelRidget lblInteger = (ILabelRidget) getRidget("lblInteger"); //$NON-NLS-1$
		lblInteger.bindToModel(txtInteger, ITextFieldRidget.PROPERTY_TEXT);
	}
}
