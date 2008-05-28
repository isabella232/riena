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

import org.eclipse.core.databinding.validation.IValidator;

/**
 * A validation rule that can be added to a ridget to verify and limit the user
 * input. Extends IValidator by allowing to change the time of the validation.
 */
public interface IValidationRule extends IValidator {

	enum ValidationTime {
		/**
		 * Validate when updating from UI-control to model (default when using a
		 * normal IValidator instead of an IValidaionRule).
		 */
		ON_UPDATE_TO_MODEL,
		/**
		 * Validate on every change in the UI-Control e.g. on every key typed in
		 * the case of a text field.
		 */
		ON_UI_CONTROL_EDITED
	}

	/**
	 * @return Indicates when the rule is to be checked.
	 */
	ValidationTime getValidationTime();

}
