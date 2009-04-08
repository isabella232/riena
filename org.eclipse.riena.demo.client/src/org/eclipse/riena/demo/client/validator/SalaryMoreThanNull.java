/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.demo.client.validator;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;

import org.eclipse.riena.ui.ridgets.validation.ValidationRuleStatus;

public class SalaryMoreThanNull implements IValidator {

	public IStatus validate(Object value) {
		try {
			float parseFloat = Float.parseFloat((String) value);
			if (parseFloat > 0) {
				return ValidationRuleStatus.ok();
			}
			return ValidationRuleStatus.error(false, "salary must be more than 0", this);
		} catch (NumberFormatException e) {
			return ValidationRuleStatus.error(false, "invalid value", this);
		}
	}

}
