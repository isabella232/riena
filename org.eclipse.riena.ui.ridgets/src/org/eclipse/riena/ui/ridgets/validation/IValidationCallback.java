package org.eclipse.riena.ui.ridgets.validation;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;

/**
 * Callback invoked after a validation.
 * 
 * @since 1.2
 */
public interface IValidationCallback {

	/**
	 * Invoked after a validation was performed.
	 * 
	 * @param validationRule
	 *            the validationRule that was checked; never null
	 * @param status
	 *            The result of the validation; never null
	 */
	void validationRuleChecked(IValidator validationRule, IStatus status);

}
