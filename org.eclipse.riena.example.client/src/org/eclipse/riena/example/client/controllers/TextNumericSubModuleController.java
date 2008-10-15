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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.IDecimalValueTextFieldRidget;
import org.eclipse.riena.ui.ridgets.INumericValueTextFieldRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;
import org.eclipse.riena.ui.ridgets.util.beans.DoubleBean;
import org.eclipse.riena.ui.ridgets.util.beans.IntegerBean;
import org.eclipse.riena.ui.ridgets.util.beans.StringBean;
import org.eclipse.riena.ui.ridgets.validation.MaxNumberLength;
import org.eclipse.riena.ui.ridgets.validation.MinLength;
import org.eclipse.riena.ui.ridgets.validation.ValidRange;

/**
 * Controller for the {@link INumericValueTextFieldRidget} and
 * {@link IDecimalValueTextFieldRidget} example.
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
		DataBindingContext dbc = new DataBindingContext();
		bind(dbc, "String"); //$NON-NLS-1$
		bind(dbc, "Integer"); //$NON-NLS-1$
		bind(dbc, "Double"); //$NON-NLS-1$
		bind(dbc, "Range"); //$NON-NLS-1$
		bind(dbc, "MaxEight"); //$NON-NLS-1$
		bind(dbc, "MinThree"); //$NON-NLS-1$

		INumericValueTextFieldRidget txtString = (INumericValueTextFieldRidget) getRidget("inString"); //$NON-NLS-1$
		txtString.bindToModel(new StringBean("1234"), StringBean.PROP_VALUE); //$NON-NLS-1$
		txtString.updateFromModel();

		INumericValueTextFieldRidget txtInteger = (INumericValueTextFieldRidget) getRidget("inInteger"); //$NON-NLS-1$
		txtInteger.bindToModel(new IntegerBean(-1234), "value"); //$NON-NLS-1$
		txtInteger.updateFromModel();

		INumericValueTextFieldRidget txtDouble = (INumericValueTextFieldRidget) getRidget("inDouble"); //$NON-NLS-1$
		txtDouble.bindToModel(new DoubleBean(1234.00), "value"); //$NON-NLS-1$
		txtDouble.updateFromModel();

		INumericValueTextFieldRidget txtRange = (INumericValueTextFieldRidget) getRidget("inRange"); //$NON-NLS-1$
		txtRange.addValidationRule(new ValidRange(Integer.valueOf(100), Integer.valueOf(1000)),
				ValidationTime.ON_UPDATE_TO_MODEL);
		txtRange.bindToModel(new IntegerBean(100), "value"); //$NON-NLS-1$
		txtRange.updateFromModel();

		INumericValueTextFieldRidget txtMaxEight = (INumericValueTextFieldRidget) getRidget("inMaxEight"); //$NON-NLS-1$
		txtMaxEight.addValidationRule(new MaxNumberLength(8), ValidationTime.ON_UI_CONTROL_EDIT);
		txtMaxEight.bindToModel(new IntegerBean(123456), "value"); //$NON-NLS-1$
		txtMaxEight.updateFromModel();

		INumericValueTextFieldRidget txtMinThree = (INumericValueTextFieldRidget) getRidget("inMinThree"); //$NON-NLS-1$
		txtMinThree.setGrouping(false);
		txtMinThree.addValidationRule(new MinLength(3), ValidationTime.ON_UI_CONTROL_EDIT);
		txtMinThree.bindToModel(new IntegerBean(1234), "value"); //$NON-NLS-1$
		txtMinThree.updateFromModel();
	}

	// helping methods
	//////////////////

	private void bind(DataBindingContext dbc, String id) {
		IRidget inputRidget = (IRidget) getRidget("in" + id); //$NON-NLS-1$
		IRidget outputRidget = (IRidget) getRidget("out" + id); //$NON-NLS-1$
		dbc.bindValue(BeansObservables.observeValue(inputRidget, ITextFieldRidget.PROPERTY_TEXT), BeansObservables
				.observeValue(outputRidget, ITextFieldRidget.PROPERTY_TEXT), new UpdateValueStrategy(
				UpdateValueStrategy.POLICY_UPDATE), new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER));
	}
}
