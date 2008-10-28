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

import java.text.ParsePosition;
import java.text.SimpleDateFormat;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.riena.ui.ridgets.IDateTextRidget;

/**
 * Validator checking that a String matches a given pattern for a Date.
 */
public abstract class AbstractValidDate implements IValidator {

	private static final String DAY_COMPONENT_LETTER = "d"; //$NON-NLS-1$
	private static final String MONTH_COMPONENT_LETTER = "M"; //$NON-NLS-1$
	private static final String YEAR_COMPONENT_LETTER = "y"; //$NON-NLS-1$
	private static final String A_LEAP_YEAR = "2004"; //$NON-NLS-1$

	/**
	 * the separator key used in the format for this date validation rule
	 */
	private char separatorKey = '.'; // default is the dot-separator

	// placeholderValid to use in completion, if no input occurs between two
	// separators
	private String placeholderValid = "1"; //$NON-NLS-1$
	// placeholderInvalid to use in completion to mark invalid input. May be
	// used, if input occurs for
	// some part of the format pattern, which should be marked as invalid.
	// Example: yyyy is the part of the format pattern to be validated and the
	// input is 0xx and should be
	// marked invalid, so 0xx is replaced by ?.
	private String placeholderInvalid = "?"; //$NON-NLS-1$

	private String pattern;

	private boolean checkValidIntermediate;

	/**
	 * Creates a StringToDateValidator.
	 * 
	 * @param pattern
	 *            The pattern to match e.g. MM/dd/yyyy.
	 */
	public AbstractValidDate(final String pattern, final boolean checkValidIntermediate) {
		this.pattern = pattern;
		this.checkValidIntermediate = checkValidIntermediate;
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
			if (string.length() > 0 && !isDateValid(string, pattern)) {
				return ValidationRuleStatus
						.error(false, "Invalid date (must match the pattern " + pattern + ").", this); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return ValidationRuleStatus.ok();
	}

	private boolean isDateValid(String value, String datePattern) {
		// fix for problem report #651
		// workaround for org.apache.commons.validator.GenericValidator bug #221
		// https://issues.apache.org/jira/browse/VALIDATOR-221
		if (datePattern.contains(DAY_COMPONENT_LETTER) && datePattern.contains(MONTH_COMPONENT_LETTER)
				&& !datePattern.contains(YEAR_COMPONENT_LETTER)) {
			datePattern += separatorKey + YEAR_COMPONENT_LETTER + YEAR_COMPONENT_LETTER + YEAR_COMPONENT_LETTER
					+ YEAR_COMPONENT_LETTER;
			value += separatorKey + A_LEAP_YEAR;
		}

		if (checkValidIntermediate) {
			return isDate(complete(value, datePattern), datePattern, false);
		}
		return isDate(value, datePattern, true);
		// if (checkValidIntermediate) {
		// return GenericValidator.isDate(complete(value, datePattern),
		// datePattern, false);
		// }
		// return GenericValidator.isDate(value, datePattern, true);
	}

	private boolean isDate(String value, String pattern, boolean strict) {
		if (value == null || pattern == null || pattern.length() <= 0) {

			return false;
		}
		if (strict && value.length() != pattern.length()) {
			return false;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		formatter.setLenient(false);

		ParsePosition pos = new ParsePosition(0);
		formatter.parse(value, pos);
		if (pos.getErrorIndex() != -1) {
			return false;
		}
		if (strict) {
			if (pos.getIndex() < value.length()) {
				return false;
			}
		}
		return true;
	}

	private String complete(String value, String datePattern) {

		final StringBuilder completedValue = new StringBuilder();
		String valueToCheck = value;
		String datePatternToCheck = datePattern;
		int nextSeparatorIndex = nextSeparatorIndex(datePatternToCheck);
		while (nextSeparatorIndex != -1) {
			int nextSeparatorIndexOfValueToCheck = valueToCheck.indexOf(datePatternToCheck.charAt(nextSeparatorIndex));
			if (nextSeparatorIndexOfValueToCheck == -1) {
				break;
			}
			if (nextSeparatorIndexOfValueToCheck == 0
					|| isPlaceholderInvalidSubstitutionNeeded(valueToCheck.substring(0,
							nextSeparatorIndexOfValueToCheck))) {
				completedValue.append(placeholderValid);
				completedValue.append(valueToCheck.charAt(nextSeparatorIndexOfValueToCheck));
			} else if (isNoPossibleCompletionToAllowedYear(valueToCheck.substring(0, nextSeparatorIndexOfValueToCheck),
					datePatternToCheck)) {
				completedValue.append(placeholderInvalid);
			} else {
				completedValue.append(valueToCheck.substring(0, nextSeparatorIndexOfValueToCheck + 1));
			}
			datePatternToCheck = datePatternToCheck.substring(nextSeparatorIndex + 1);
			valueToCheck = valueToCheck.substring(nextSeparatorIndexOfValueToCheck + 1);
			nextSeparatorIndex = nextSeparatorIndex(datePatternToCheck);
		}

		if (("".equals(valueToCheck) && !"".equals(datePatternToCheck)) //$NON-NLS-1$//$NON-NLS-2$
				|| isPlaceholderInvalidSubstitutionNeeded(valueToCheck)) {
			completedValue.append(placeholderValid);
		} else if (isNoPossibleCompletionToAllowedYear(valueToCheck, datePatternToCheck)) {
			completedValue.append(placeholderInvalid);
		} else {
			completedValue.append(valueToCheck);
		}

		return completedValue.toString();
	}

	private boolean isPlaceholderInvalidSubstitutionNeeded(final String value) {
		return "0".equals(value); //$NON-NLS-1$
	}

	private boolean isNoPossibleCompletionToAllowedYear(final String value, final String datePattern) {

		return datePattern.startsWith(IDateTextRidget.FORMAT_YYYY) && value.startsWith("0") && value.length() > 2; //$NON-NLS-1$
	}

	private int nextSeparatorIndex(final String value) {
		for (int i = 0; i < value.length(); i++) {
			if (!Character.isLetter(value.charAt(i))) {
				return i;
			}
		}
		return -1;
	}

}
