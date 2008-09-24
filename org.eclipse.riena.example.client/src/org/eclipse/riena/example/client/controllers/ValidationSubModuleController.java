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
import org.eclipse.riena.example.client.views.ValidationSubModuleView;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubApplicationController;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.IStatuslineRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;
import org.eclipse.riena.ui.ridgets.marker.StatuslineMessageMarkerViewer;
import org.eclipse.riena.ui.ridgets.marker.TooltipMessageMarkerViewer;
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
 * Controller for the {@link ValidationSubModuleView} example.
 */
public class ValidationSubModuleController extends SubModuleController {

	private static final String DATE_PATTERN = "dd.MM.yyyy"; //$NON-NLS-1$

	public ValidationSubModuleController() {
		this(null);
	}

	public ValidationSubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	/**
	 * Binds and updates the ridgets.
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#configureRidgets()
	 */
	@Override
	public void configureRidgets() {

		final ITextFieldRidget txtNumbersOnly = (ITextFieldRidget) getRidget("txtNumbersOnly"); //$NON-NLS-1$
		final ITextFieldRidget txtNumbersOnlyDW = (ITextFieldRidget) getRidget("txtNumbersOnlyDW"); //$NON-NLS-1$
		final ITextFieldRidget txtCharactersOnly = (ITextFieldRidget) getRidget("txtCharactersOnly"); //$NON-NLS-1$
		final ITextFieldRidget txtExpression = (ITextFieldRidget) getRidget("txtExpression"); //$NON-NLS-1$
		final ITextFieldRidget txtLengthLessThan5 = (ITextFieldRidget) getRidget("txtLengthLessThan5"); //$NON-NLS-1$
		final ITextFieldRidget txtRequiredLowercase = (ITextFieldRidget) getRidget("txtRequiredLowercase"); //$NON-NLS-1$
		final ITextFieldRidget txtRange18to80 = (ITextFieldRidget) getRidget("txtRange18to80"); //$NON-NLS-1$
		final ITextFieldRidget txtLength5to10 = (ITextFieldRidget) getRidget("txtLength5to10"); //$NON-NLS-1$
		final ITextFieldRidget txtDate = (ITextFieldRidget) getRidget("txtDate"); //$NON-NLS-1$
		final ITextFieldRidget txtEmail = (ITextFieldRidget) getRidget("txtEmail"); //$NON-NLS-1$

		final ITextFieldRidget lblNumbersOnly = (ITextFieldRidget) getRidget("lblNumbersOnly"); //$NON-NLS-1$
		final ITextFieldRidget lblNumbersOnlyDW = (ITextFieldRidget) getRidget("lblNumbersOnlyDW"); //$NON-NLS-1$
		final ITextFieldRidget lblCharactersOnly = (ITextFieldRidget) getRidget("lblCharactersOnly"); //$NON-NLS-1$
		final ITextFieldRidget lblExpression = (ITextFieldRidget) getRidget("lblExpression"); //$NON-NLS-1$
		final ITextFieldRidget lblLengthLessThan5 = (ITextFieldRidget) getRidget("lblLengthLessThan5"); //$NON-NLS-1$
		final ITextFieldRidget lblRequiredLowercase = (ITextFieldRidget) getRidget("lblRequiredLowercase"); //$NON-NLS-1$
		final ITextFieldRidget lblRange18to80 = (ITextFieldRidget) getRidget("lblRange18to80"); //$NON-NLS-1$
		final ITextFieldRidget lblLength5to10 = (ITextFieldRidget) getRidget("lblLength5to10"); //$NON-NLS-1$
		final ITextFieldRidget lblDate = (ITextFieldRidget) getRidget("lblDate"); //$NON-NLS-1$
		final ITextFieldRidget lblEmail = (ITextFieldRidget) getRidget("lblEmail"); //$NON-NLS-1$

		// on edit validation

		txtNumbersOnly.addValidationRule(new ValidCharacters(ValidCharacters.VALID_NUMBERS),
				ValidationTime.ON_UI_CONTROL_EDIT);
		txtNumbersOnly.addValidationMessage("Only numbers are allowed!"); //$NON-NLS-1$
		txtNumbersOnly.bindToModel(getTextValue(lblNumbersOnly));

		txtNumbersOnlyDW.addValidationRule(new ValidCharacters(ValidCharacters.VALID_NUMBERS),
				ValidationTime.ON_UI_CONTROL_EDIT);
		txtNumbersOnlyDW.addValidationMessage("Only numbers are allowed!"); //$NON-NLS-1$
		txtNumbersOnlyDW.setDirectWriting(true);
		txtNumbersOnlyDW.bindToModel(getTextValue(lblNumbersOnlyDW));

		txtCharactersOnly.addValidationRule(new ValidCharacters(ValidCharacters.VALID_LETTER),
				ValidationTime.ON_UI_CONTROL_EDIT);
		txtCharactersOnly.addValidationMessage("Only characters are allowed!"); //$NON-NLS-1$
		txtCharactersOnly.bindToModel(getTextValue(lblCharactersOnly));

		txtExpression.addValidationRule(new ValidExpression("^PDX[0-9]{2}$"), ValidationTime.ON_UI_CONTROL_EDIT); //$NON-NLS-1$
		txtExpression.addValidationMessage("The text does not match with the expression (PDX##)!"); //$NON-NLS-1$
		txtExpression.bindToModel(getTextValue(lblExpression));
		txtExpression.setText("PDX97"); //$NON-NLS-1$

		txtLengthLessThan5.addValidationRule(new MaxLength(5), ValidationTime.ON_UI_CONTROL_EDIT);
		txtLengthLessThan5.addValidationMessage("The text is longer than 5 characters!"); //$NON-NLS-1$
		txtLengthLessThan5.bindToModel(getTextValue(lblLengthLessThan5));

		txtRequiredLowercase.addValidationRule(new RequiredField(), ValidationTime.ON_UI_CONTROL_EDIT);
		txtRequiredLowercase.addValidationRule(new ValidCharacters(ValidCharacters.VALID_LOWERCASE),
				ValidationTime.ON_UI_CONTROL_EDIT);
		txtRequiredLowercase.addValidationMessage("Only lowercase characters are allowed!"); //$NON-NLS-1$
		txtRequiredLowercase.bindToModel(getTextValue(lblRequiredLowercase));

		// on update validation

		txtRange18to80.addValidationRule(new RequiredField(), ValidationTime.ON_UPDATE_TO_MODEL);
		txtRange18to80.addValidationRule(new ValidRange(18, 80), ValidationTime.ON_UPDATE_TO_MODEL);
		txtRange18to80.bindToModel(getTextValue(lblRange18to80));

		txtLength5to10.addValidationRule(new MinLength(5), ValidationTime.ON_UPDATE_TO_MODEL);
		txtLength5to10.addValidationRule(new MaxLength(10), ValidationTime.ON_UPDATE_TO_MODEL);
		txtLength5to10.bindToModel(getTextValue(lblLength5to10));

		// complex validation

		txtDate.addValidationRule(new ValidIntermediateDate(DATE_PATTERN), ValidationTime.ON_UI_CONTROL_EDIT);
		txtDate.addValidationRule(new ValidDate(DATE_PATTERN), ValidationTime.ON_UPDATE_TO_MODEL);
		txtDate.setText("25.12.2008"); //$NON-NLS-1$
		txtDate.bindToModel(getTextValue(lblDate));

		txtEmail.addValidationRule(new ValidEmailAddress(), ValidationTime.ON_UI_CONTROL_EDIT);
		txtEmail.bindToModel(getTextValue(lblEmail));
		txtEmail.setText("elmer@foo.bar"); //$NON-NLS-1$

		IStatuslineRidget statuslineRidget = getSubApplicationController().getStatuslineRidget();
		StatuslineMessageMarkerViewer statuslineMessageMarkerViewer = new StatuslineMessageMarkerViewer(
				statuslineRidget);
		statuslineMessageMarkerViewer.addRidget(txtNumbersOnly);
		statuslineMessageMarkerViewer.addRidget(txtNumbersOnlyDW);
		statuslineMessageMarkerViewer.addRidget(txtCharactersOnly);
		statuslineMessageMarkerViewer.addRidget(txtExpression);
		statuslineMessageMarkerViewer.addRidget(txtLengthLessThan5);
		statuslineMessageMarkerViewer.addRidget(txtRequiredLowercase);

		TooltipMessageMarkerViewer tooltipMessageMarkerViewer = new TooltipMessageMarkerViewer();
		tooltipMessageMarkerViewer.addRidget(txtNumbersOnly);

	}

	private IObservableValue getTextValue(ITextFieldRidget bean) {
		return BeansObservables.observeValue(bean, ITextFieldRidget.PROPERTY_TEXT);
	}

	/**
	 * Returns the controller of the parent sub-application.
	 * 
	 * @return sub-application controller
	 */
	private SubApplicationController getSubApplicationController() {
		return (SubApplicationController) getNavigationNode().getParentOfType(ISubApplicationNode.class)
				.getNavigationNodeController();
	}

}
