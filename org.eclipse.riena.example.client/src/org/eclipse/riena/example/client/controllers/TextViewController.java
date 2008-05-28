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

import org.eclipse.riena.example.client.views.TextView;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleNodeViewController;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;

/**
 * Controller for the {@link TextView} example.
 */
public class TextViewController extends SubModuleNodeViewController {

	private ITextFieldRidget textField;
	private ITextFieldRidget textModel1;
	private ITextFieldRidget textDirectWrite;
	private ITextFieldRidget textModel2;
	private ITextFieldRidget textArea;
	private ITextFieldRidget textPassword;
	private ITextFieldRidget textField10;

	public TextViewController(ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	public ITextFieldRidget getTextField() {
		return textField;
	}

	public void setTextField(ITextFieldRidget textField) {
		this.textField = textField;
	}

	public ITextFieldRidget getTextModel1() {
		return textModel1;
	}

	public void setTextModel1(ITextFieldRidget textModel1) {
		this.textModel1 = textModel1;
	}

	public ITextFieldRidget getTextDirectWrite() {
		return textDirectWrite;
	}

	public void setTextDirectWrite(ITextFieldRidget textDirectWrite) {
		this.textDirectWrite = textDirectWrite;
	}

	public ITextFieldRidget getTextModel2() {
		return textModel2;
	}

	public void setTextModel2(ITextFieldRidget textModel2) {
		this.textModel2 = textModel2;
	}

	public ITextFieldRidget getTextArea() {
		return textArea;
	}

	public void setTextArea(ITextFieldRidget textArea) {
		this.textArea = textArea;
	}

	public ITextFieldRidget getTextPassword() {
		return textPassword;
	}

	public void setTextPassword(ITextFieldRidget textPassword) {
		this.textPassword = textPassword;
	}

	public ITextFieldRidget getTextField10() {
		return textField10;
	}

	public void setTextField10(ITextFieldRidget textField10) {
		this.textField10 = textField10;
	}

	@Override
	public void afterBind() {
		super.afterBind();
		initRidgets();
	}

	/**
	 * Binds and updates the ridgets.
	 */
	private void initRidgets() {
		textModel1.setText("type something");
		textField.bindToModel(textModel1, ITextFieldRidget.PROPERTY_TEXT);
		textField.updateFromModel();

		textModel2.setText("type something");
		textDirectWrite.setDirectWriting(true);
		textDirectWrite.bindToModel(textModel2, ITextFieldRidget.PROPERTY_TEXT);
		textDirectWrite.updateFromModel();
	}

}
