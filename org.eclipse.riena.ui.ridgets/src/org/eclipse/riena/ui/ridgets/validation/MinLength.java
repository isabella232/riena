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

import org.eclipse.core.runtime.IStatus;

/**
 * 
 */
public class MinLength implements IValidationRule {

	private int minLength;

	public MinLength(final int minLength) {
		this.minLength = minLength;
	}

	/**
	 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
	 */
	public IStatus validate(final Object value) {
		if (value == null) {
			if (minLength > 0) {
				return ValidationRuleStatus.error(false, "rule treats null as a blank string, which is shorter than "
						+ minLength + " characters.");
			}
			return ValidationRuleStatus.ok();
		}
		if (value instanceof String) {
			final String string = (String) value;
			if (string.length() >= minLength) {
				return ValidationRuleStatus.ok();
			}
			return ValidationRuleStatus.error(false, "String ''" + string + "' is less than " + minLength
					+ " characters long.");
		}
		throw new ValidationFailure(getClass().getName() + " can only validate objects of type "
				+ String.class.getName());
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.validation.IValidationRule#getValidationTime()
	 */
	public ValidationTime getValidationTime() {
		return ValidationTime.ON_UPDATE_TO_MODEL;
	}

}
