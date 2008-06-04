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

import org.eclipse.riena.example.client.views.ValidationView;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleNodeViewController;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;
import org.eclipse.riena.ui.ridgets.validation.MaxLength;
import org.eclipse.riena.ui.ridgets.validation.MinLength;
import org.eclipse.riena.ui.ridgets.validation.ValidCharacters;
import org.eclipse.riena.ui.ridgets.validation.ValidEmailAddress;
import org.eclipse.riena.ui.ridgets.validation.ValidRange;

/**
 * Controller for the {@link ValidationView} example.
 */
public class ValidationViewController extends SubModuleNodeViewController {

	private ITextFieldRidget txtNumbersOnly;
	private ITextFieldRidget txtNumbersOnlyDW;
	private ITextFieldRidget txtRange18to80;
	private ITextFieldRidget txtLength5to10;
	private ITextFieldRidget txtLowercase;
	private ITextFieldRidget txtEmail;

	public ValidationViewController(ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	public ITextFieldRidget getTxtNumbersOnly() {
		return txtNumbersOnly;
	}

	public void setTxtNumbersOnly(ITextFieldRidget txtNumbersOnly) {
		this.txtNumbersOnly = txtNumbersOnly;
	}

	public ITextFieldRidget getTxtNumbersOnlyDW() {
		return txtNumbersOnlyDW;
	}

	public ITextFieldRidget getTxtRange18to80() {
		return txtRange18to80;
	}

	public void setTxtRange18to80(ITextFieldRidget txtRange18to80) {
		this.txtRange18to80 = txtRange18to80;
	}

	public void setTxtNumbersOnlyDW(ITextFieldRidget txtNumbersOnlyDW) {
		this.txtNumbersOnlyDW = txtNumbersOnlyDW;
	}

	public ITextFieldRidget getTxtLength5to10() {
		return txtLength5to10;
	}

	public void setTxtLength5to10(ITextFieldRidget txtLength5to10) {
		this.txtLength5to10 = txtLength5to10;
	}

	public ITextFieldRidget getTxtLowercase() {
		return txtLowercase;
	}

	public void setTxtLowercase(ITextFieldRidget txtLowercase) {
		this.txtLowercase = txtLowercase;
	}

	public ITextFieldRidget getTxtEmail() {
		return txtEmail;
	}

	public void setTxtEmail(ITextFieldRidget txtEmail) {
		this.txtEmail = txtEmail;
	}

	public void afterBind() {
		super.afterBind();
		initRidgets();
	}

	/**
	 * Binds and updates the ridgets.
	 */
	private void initRidgets() {
		txtNumbersOnly.addValidationRule(new ValidCharacters(ValidCharacters.VALID_NUMBERS));

		txtNumbersOnlyDW.addValidationRule(new ValidCharacters(ValidCharacters.VALID_NUMBERS));
		txtNumbersOnlyDW.setDirectWriting(true);

		txtRange18to80.addValidationRule(new ValidRange(18, 80));

		txtLength5to10.addValidationRule(new MinLength(5));
		txtLength5to10.addValidationRule(new MaxLength(10));

		txtLowercase.addValidationRule(new ValidCharacters(ValidCharacters.VALID_LOWERCASE));

		txtEmail.addValidationRule(new ValidEmailAddress());
	}

}
