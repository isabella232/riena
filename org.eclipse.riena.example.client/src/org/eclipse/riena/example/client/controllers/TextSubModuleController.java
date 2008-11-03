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
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.ITextRidget;

/**
 * Controller for the {@link TextSubModuleView} example.
 */
public class TextSubModuleController extends SubModuleController {

	public TextSubModuleController() {
		this(null);
	}

	public TextSubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	/**
	 * Binds and updates the ridgets.
	 */
	public void configureRidgets() {

		// private ITextRidget textArea;
		// private ITextRidget textPassword;
		// private ITextRidget textField10;

		ITextRidget textModel1 = (ITextRidget) getRidget("textModel1"); //$NON-NLS-1$
		textModel1.setText("type something"); //$NON-NLS-1$
		ITextRidget textField = (ITextRidget) getRidget("textField"); //$NON-NLS-1$
		textField.bindToModel(textModel1, ITextRidget.PROPERTY_TEXT);
		textField.updateFromModel();

		ITextRidget textModel2 = (ITextRidget) getRidget("textModel2"); //$NON-NLS-1$
		textModel2.setText("type something"); //$NON-NLS-1$
		ITextRidget textDirectWrite = (ITextRidget) getRidget("textDirectWrite"); //$NON-NLS-1$
		textDirectWrite.setDirectWriting(true);
		textDirectWrite.bindToModel(textModel2, ITextRidget.PROPERTY_TEXT);
		textDirectWrite.updateFromModel();
	}

}
