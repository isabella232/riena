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

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.riena.example.client.views.ValidationView;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleNodeViewController;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;
import org.eclipse.riena.ui.ridgets.validation.MaxLength;
import org.eclipse.riena.ui.ridgets.validation.MinLength;
import org.eclipse.riena.ui.ridgets.validation.RequiredField;
import org.eclipse.riena.ui.ridgets.validation.ValidCharacters;
import org.eclipse.riena.ui.ridgets.validation.ValidDate;
import org.eclipse.riena.ui.ridgets.validation.ValidEmailAddress;
import org.eclipse.riena.ui.ridgets.validation.ValidExpression;
import org.eclipse.riena.ui.ridgets.validation.ValidIntermediateDate;
import org.eclipse.riena.ui.ridgets.validation.ValidRange;

/**
 * Controller for the {@link ValidationView} example.
 */
public class ValidationViewController extends SubModuleNodeViewController {

	private static final String DATE_PATTERN = "dd.MM.yyyy"; //$NON-NLS-1$

	private ITextFieldRidget txtNumbersOnly;
	private ITextFieldRidget txtNumbersOnlyDW;
	private ITextFieldRidget txtCharactersOnly;
	private ITextFieldRidget txtExpression;
	private ITextFieldRidget txtLengthLessThan5;
	private ITextFieldRidget txtRequiredLowercase;
	private ITextFieldRidget txtRange18to80;
	private ITextFieldRidget txtLength5to10;
	private ITextFieldRidget txtDate;
	private ITextFieldRidget txtEmail;

	private ILabelRidget lblNumbersOnly;
	private ILabelRidget lblNumbersOnlyDW;
	private ILabelRidget lblCharactersOnly;
	private ILabelRidget lblExpression;
	private ILabelRidget lblLengthLessThan5;
	private ILabelRidget lblRequiredLowercase;
	private ILabelRidget lblRange18to80;
	private ILabelRidget lblLength5to10;
	private ILabelRidget lblDate;
	private ILabelRidget lblEmail;

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

	public void setTxtNumbersOnlyDW(ITextFieldRidget txtNumbersOnlyDW) {
		this.txtNumbersOnlyDW = txtNumbersOnlyDW;
	}

	public ITextFieldRidget getTxtCharactersOnly() {
		return txtCharactersOnly;
	}

	public void setTxtCharactersOnly(ITextFieldRidget txtCharactersOnly) {
		this.txtCharactersOnly = txtCharactersOnly;
	}

	public ITextFieldRidget getTxtExpression() {
		return txtExpression;
	}

	public void setTxtExpression(ITextFieldRidget txtExpression) {
		this.txtExpression = txtExpression;
	}

	public ITextFieldRidget getTxtLengthLessThan5() {
		return txtLengthLessThan5;
	}

	public void setTxtLengthLessThan5(ITextFieldRidget txtLengthLessThan5) {
		this.txtLengthLessThan5 = txtLengthLessThan5;
	}

	public ITextFieldRidget getTxtRange18to80() {
		return txtRange18to80;
	}

	public void setTxtRange18to80(ITextFieldRidget txtRange18to80) {
		this.txtRange18to80 = txtRange18to80;
	}

	public ITextFieldRidget getTxtLength5to10() {
		return txtLength5to10;
	}

	public void setTxtLength5to10(ITextFieldRidget txtLength5to10) {
		this.txtLength5to10 = txtLength5to10;
	}

	public ITextFieldRidget getTxtDate() {
		return txtDate;
	}

	public void setTxtDate(ITextFieldRidget txtDate) {
		this.txtDate = txtDate;
	}

	public ITextFieldRidget getTxtEmail() {
		return txtEmail;
	}

	public void setTxtEmail(ITextFieldRidget txtEmail) {
		this.txtEmail = txtEmail;
	}

	public ILabelRidget getLblNumbersOnly() {
		return lblNumbersOnly;
	}

	public void setLblNumbersOnly(ILabelRidget lblNumbersOnly) {
		this.lblNumbersOnly = lblNumbersOnly;
	}

	public ILabelRidget getLblNumbersOnlyDW() {
		return lblNumbersOnlyDW;
	}

	public void setLblNumbersOnlyDW(ILabelRidget lblNumbersOnlyDW) {
		this.lblNumbersOnlyDW = lblNumbersOnlyDW;
	}

	public ILabelRidget getLblCharactersOnly() {
		return lblCharactersOnly;
	}

	public void setLblCharactersOnly(ILabelRidget lblCharactersOnly) {
		this.lblCharactersOnly = lblCharactersOnly;
	}

	public ILabelRidget getLblExpression() {
		return lblExpression;
	}

	public void setLblExpression(ILabelRidget lblExpression) {
		this.lblExpression = lblExpression;
	}

	public ILabelRidget getLblLengthLessThan5() {
		return lblLengthLessThan5;
	}

	public void setLblLengthLessThan5(ILabelRidget lblLengthLessThan5) {
		this.lblLengthLessThan5 = lblLengthLessThan5;
	}

	public ILabelRidget getLblRange18to80() {
		return lblRange18to80;
	}

	public void setLblRange18to80(ILabelRidget lblRange18to80) {
		this.lblRange18to80 = lblRange18to80;
	}

	public ILabelRidget getLblLength5to10() {
		return lblLength5to10;
	}

	public void setLblLength5to10(ILabelRidget lblLength5to10) {
		this.lblLength5to10 = lblLength5to10;
	}

	public ILabelRidget getLblDate() {
		return lblDate;
	}

	public void setLblDate(ILabelRidget lblDate) {
		this.lblDate = lblDate;
	}

	public ILabelRidget getLblEmail() {
		return lblEmail;
	}

	public void setLblEmail(ILabelRidget lblEmail) {
		this.lblEmail = lblEmail;
	}

	public ITextFieldRidget getTxtRequiredLowercase() {
		return txtRequiredLowercase;
	}

	public void setTxtRequiredLowercase(ITextFieldRidget txtRequiredLowercase) {
		this.txtRequiredLowercase = txtRequiredLowercase;
	}

	public ILabelRidget getLblRequiredLowercase() {
		return lblRequiredLowercase;
	}

	public void setLblRequiredLowercase(ILabelRidget lblRequiredLowercase) {
		this.lblRequiredLowercase = lblRequiredLowercase;
	}

	public void afterBind() {
		super.afterBind();
		initRidgets();
	}

	/**
	 * Binds and updates the ridgets.
	 */
	private void initRidgets() {

		// on edit validation

		txtNumbersOnly.addValidationRule(new ValidCharacters(ValidCharacters.VALID_NUMBERS));
		txtNumbersOnly.setText("abc");
		txtNumbersOnly.bindToModel(getTextValue(lblNumbersOnly));

		txtNumbersOnlyDW.addValidationRule(new ValidCharacters(ValidCharacters.VALID_NUMBERS));
		txtNumbersOnlyDW.setDirectWriting(true);
		txtNumbersOnlyDW.bindToModel(getTextValue(lblNumbersOnlyDW));
		lblNumbersOnlyDW.setText("acme");
		txtNumbersOnlyDW.updateFromModel();

		txtCharactersOnly.addValidationRule(new ValidCharacters(ValidCharacters.VALID_LETTER));
		txtCharactersOnly.bindToModel(getTextValue(lblCharactersOnly));

		txtExpression.addValidationRule(new ValidExpression("^PDX[0-9]{2}$")); //$NON-NLS-1$
		txtExpression.bindToModel(getTextValue(lblExpression));
		txtExpression.setText("PDX97");

		txtLengthLessThan5.addValidationRule(new MaxLength(5));
		txtLengthLessThan5.bindToModel(getTextValue(lblLengthLessThan5));

		txtRequiredLowercase.addValidationRule(new RequiredField());
		txtRequiredLowercase.addValidationRule(new ValidCharacters(ValidCharacters.VALID_LOWERCASE));
		txtRequiredLowercase.bindToModel(getTextValue(lblRequiredLowercase));

		// on update validation

		txtRange18to80.addValidationRule(new ValidRange(18, 80));
		lblRange18to80.setText("10");
		txtRange18to80.bindToModel(getTextValue(lblRange18to80));
		txtRange18to80.updateFromModel();

		txtLength5to10.addValidationRule(new MinLength(5));
		txtLength5to10.addValidationRule(new MaxLength(10));
		txtLength5to10.bindToModel(getTextValue(lblLength5to10));
		txtLength5to10.setText("tiny");

		txtDate.addValidationRule(new ValidIntermediateDate(DATE_PATTERN));
		txtDate.addValidationRule(new ValidDate(DATE_PATTERN));
		txtDate.setText("25.18.2008");
		txtDate.bindToModel(getTextValue(lblDate));

		txtEmail.addValidationRule(new ValidEmailAddress());
		txtEmail.bindToModel(getTextValue(lblEmail));
		txtEmail.setText("elmer@acme.com");
	}

	private IObservableValue getTextValue(ILabelRidget bean) {
		return BeansObservables.observeValue(bean, ILabelRidget.PROPERTY_TEXT);
	}

}
