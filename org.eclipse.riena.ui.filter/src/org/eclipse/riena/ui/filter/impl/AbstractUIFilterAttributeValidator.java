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
package org.eclipse.riena.ui.filter.impl;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.filter.IUIFilterValidatorAttribute;

/**
 * Filter attribute for with validator.
 */
public abstract class AbstractUIFilterAttributeValidator implements IUIFilterValidatorAttribute {

	private IValidator validator;

	private ValidationTime validationTime;

	/**
	 * Create a new filter attribute with the given validator.
	 * 
	 * @param validator
	 *            - validator to set
	 * @param validationTime
	 *            - time of validation
	 */
	public AbstractUIFilterAttributeValidator(IValidator validator, ValidationTime validationTime) {
		this.validator = validator;
		this.validationTime = validationTime;
	}

	public IValidator getValidator() {
		return validator;
	}

	public ValidationTime getValidationTime() {
		return validationTime;
	}

}
