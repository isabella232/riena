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
package org.eclipse.riena.ui.ridgets.validation;

import org.apache.commons.validator.GenericValidator;
import org.eclipse.core.runtime.IStatus;

/**
 * Implementation for a email address validation. This rule accepts any String
 * which is either <tt>null</tt>, empty, all whitespace or a valid email
 * address.
 */
public class ValidEmailAddress implements IValidationRule {

	public ValidationTime getValidationTime() {
		return ValidationTime.ON_UI_CONTROL_EDITED;
	}

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
		return ValidationRuleStatus.error(false, "String '" + toBeChecked + "' is no valid email address", this);
	}

}
