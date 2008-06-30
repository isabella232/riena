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

import java.util.Arrays;

import org.eclipse.core.runtime.IStatus;

/**
 * Implementation for a plausibility rule which checks if the typed character is
 * contained in a set of allowed characters. <br>
 */
public class ValidCharacters implements IValidationRule {

	/** <code>VALID_NUMBERS</code> defines 0-9 */
	public static final String VALID_NUMBERS = "0123456789"; //$NON-NLS-1$
	/** <code>VALID_UPPERCASE</code> defines A-Z */
	public static final String VALID_UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; //$NON-NLS-1$
	/** <code>VALID_LOWERCASE</code> defines a-z */
	public static final String VALID_LOWERCASE = "abcdefghijklmnopqrstuvwxyz"; //$NON-NLS-1$
	/** <code>VALID_LETTER</code> defines A-Za-z */
	public static final String VALID_LETTER = VALID_UPPERCASE + VALID_LOWERCASE;
	/** <code>VALID_ALPHANUMERIC</code> defines A-Za-z0-9 */
	public static final String VALID_ALPHANUMERIC = VALID_LETTER + VALID_NUMBERS;

	private String allowedChars;
	private char[] allowedCharsSorted = new char[0];

	/**
	 * Constructs a valid characters check plausibilisation rule
	 * 
	 * @param chars
	 */
	public ValidCharacters(final String allowedChars) {
		setAllowedChars(allowedChars);
	}

	/**
	 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
	 */
	public IStatus validate(final Object value) {
		if (value != null) {
			if (!(value instanceof String)) {
				throw new ValidationFailure("ValidCharacters can only validate objects of type String."); //$NON-NLS-1$
			}
			final String string = (String) value;
			if (allowedChars != null) {
				for (int t = 0; t < string.length(); ++t) {
					final char currentChar = string.charAt(t);
					if (Arrays.binarySearch(allowedCharsSorted, currentChar) < 0) {
						return ValidationRuleStatus.error(true, "Character '" + currentChar + "' in text '" + value
								+ "' is not allowed.", this);
					}
				}
			}
		}
		return ValidationRuleStatus.ok();
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.validation.IValidationRule#getValidationTime()
	 */
	public ValidationTime getValidationTime() {
		return ValidationTime.ON_UI_CONTROL_EDITED;
	}

	/**
	 * @return Returns the allowedChars.
	 */
	public String getAllowedChars() {
		return allowedChars;
	}

	/**
	 * @param allowedChars
	 *            The allowedChars to set.
	 */
	public void setAllowedChars(final String allowedChars) {
		this.allowedChars = allowedChars;
		// operate on a sorted copy, so getter returns the same value as set.
		allowedCharsSorted = allowedChars == null ? new char[0] : allowedChars.toCharArray();
		Arrays.sort(allowedCharsSorted);
	}

}
