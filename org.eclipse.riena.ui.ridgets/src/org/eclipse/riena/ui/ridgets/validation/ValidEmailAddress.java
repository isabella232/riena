/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.validation;

import org.apache.commons.validator.GenericValidator;
import org.eclipse.core.runtime.IStatus;

/**
 * Implementation for a email address validation. This rule accepts any String
 * which is either <tt>null</tt>, empty, all whitespace or a valid email
 * address.
 * 
 * @author Wanja Gayk
 */
public class ValidEmailAddress implements IValidationRule {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.validation.IValidationRule#getValidationTime()
	 */
	public ValidationTime getValidationTime() {
		return ValidationTime.ON_UI_CONTROL_EDITED;
		// TODO: create ValidationTime.ON_UI_CONTROL_FOCUS_LOST?
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
	 */
	public IStatus validate(final Object value) {
		if (value == null) {
			return ValidationRuleStatus.ok();
		}
		// note: null instanceof String == false
		if (!(value instanceof String)) {
			throw new ValidationFailure(getClass().getSimpleName() + " can only validate objects of type "
					+ String.class.getName());
		}
		final String toBeChecked = (String) value;
		if (toBeChecked.length() == 0 || GenericValidator.isEmail(toBeChecked)) {
			return ValidationRuleStatus.ok();
		}
		return ValidationRuleStatus.error(true, "String '" + toBeChecked + "' is no valid email address");
	}

}
