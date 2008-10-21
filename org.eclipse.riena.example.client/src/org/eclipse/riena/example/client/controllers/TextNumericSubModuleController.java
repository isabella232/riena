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

import java.math.BigDecimal;
import java.math.BigInteger;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.IDecimalValueTextFieldRidget;
import org.eclipse.riena.ui.ridgets.INumericValueTextFieldRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;
import org.eclipse.riena.ui.ridgets.util.beans.IntegerBean;
import org.eclipse.riena.ui.ridgets.util.beans.TypedBean;
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
		String[] ids = { "StringNum", "Integer", "Long", "BigInteger", "StringDec", "Double", "Float", "BigDecimal", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
				"Range", "MaxEight", "MinThree" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		DataBindingContext dbc = new DataBindingContext();
		for (String id : ids) {
			bind(dbc, id);
		}

		bindToModel("StringNum", new TypedBean<String>("1234")); //$NON-NLS-1$ //$NON-NLS-2$
		bindToModel("Integer", new TypedBean<Integer>(Integer.valueOf(-1234)));
		bindToModel("Long", new TypedBean<Long>(Long.valueOf(1234)));
		bindToModel("BigInteger", new TypedBean<BigInteger>(BigInteger.valueOf(12345789)));

		bindToModel("StringDec", new TypedBean<String>("12345678.1234")); //$NON-NLS-1$ //$NON-NLS-2$
		bindToModel("Double", new TypedBean<Double>(Double.valueOf(-1234.00)));
		bindToModel("Float", new TypedBean<Float>(Float.valueOf("1234"))); //$NON-NLS-2$
		bindToModel("BigDecimal", new TypedBean<BigDecimal>(BigDecimal.valueOf(12345789.1234)));
		IDecimalValueTextFieldRidget inBigDecimal = (IDecimalValueTextFieldRidget) getRidget("inBigDecimal"); //$NON-NLS-1$
		inBigDecimal.setMaxLength(30);
		inBigDecimal.setPrecision(10);

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
		txtMinThree.addValidationRule(new MinLength(4), ValidationTime.ON_UI_CONTROL_EDIT);
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

	private void bindToModel(String id, TypedBean<?> value) {
		INumericValueTextFieldRidget inputRidget = (INumericValueTextFieldRidget) getRidget("in" + id); //$NON-NLS-1$
		inputRidget.bindToModel(value, TypedBean.PROP_VALUE);
		inputRidget.updateFromModel();
	}
}
