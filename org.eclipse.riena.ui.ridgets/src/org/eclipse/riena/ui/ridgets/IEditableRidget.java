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
package org.eclipse.riena.ui.ridgets;

import java.util.Collection;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.riena.ui.ridgets.validation.IValidationRule;
import org.eclipse.riena.ui.ridgets.validation.IValidationRuleStatus;

/**
 * Ridget with a value that can be edited, validated and converted.
 */
public interface IEditableRidget extends IValueRidget, IValidationCallback {

	/**
	 * @return The converter used when updating from the UI-control to the
	 *         model.
	 */
	IConverter getUIControlToModelConverter();

	/**
	 * Sets the converter used when updating from the UI-control to the model.
	 * 
	 * @param converter
	 *            The new converter.
	 */
	void setUIControlToModelConverter(IConverter converter);

	/**
	 * @return The validation rules.
	 * @see #addValidationRule(IValidator)
	 */
	Collection<IValidator> getValidationRules();

	/**
	 * Adds a validator. By default validations will be performed when updating
	 * from the UI-control to the model and before a conversion. By default
	 * failed validations will mark the UI-control with an ErrorMarker and not
	 * block any user input. The time of the validation can be changed by using
	 * an IValidationRule. The reaction to a failed validation can be changed by
	 * using a validator that returns an IValidationRuleStatus.
	 * 
	 * @see IValidationRule
	 * @see IValidationRuleStatus
	 * 
	 * @param validationRule
	 *            The validation rule to add.
	 */
	void addValidationRule(IValidator validationRule);

	/**
	 * Removes a validator.
	 * 
	 * @param validationRule
	 *            The validation rule to remove.
	 */
	void removeValidationRule(IValidator validationRule);

}
