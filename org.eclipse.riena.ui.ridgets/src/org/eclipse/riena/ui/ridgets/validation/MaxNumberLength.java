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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.eclipse.core.runtime.IStatus;

/**
 * 
 */
public class MaxNumberLength extends MaxLength {
	private DecimalFormatSymbols symbols;

	/**
	 * Creates a MexLength rule for numbers.
	 * 
	 * @param length
	 *            The maximum number of characters excluding a leading minus
	 *            sign.
	 */
	public MaxNumberLength(final int length) {
		this(length, Locale.getDefault());
	}

	/**
	 * Creates a MexLength rule for numbers.
	 * 
	 * @param length
	 *            The maximum number of characters excluding a leading minus
	 *            sign.
	 */
	public MaxNumberLength(final int length, final Locale locale) {
		super(length);
		final DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance(locale);
		symbols = format.getDecimalFormatSymbols();
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.validation.MaxLength#validate(java.lang.Object)
	 */
	@Override
	public IStatus validate(final Object value) {
		if (value == null) {
			return ValidationRuleStatus.ok();
		}
		if (value instanceof String) {
			String string = removeWhitespaceAndGroupingCharacter((String) value);
			if (string.length() > 0) {
				if (string.charAt(0) == symbols.getMinusSign()) {
					string = string.substring(1);
				} else if (string.charAt(string.length() - 1) == symbols.getMinusSign()) {
					// for Arab locales
					string = string.substring(0, string.length() - 1);
				}
			}
			return super.validate(string);
		}
		throw new ValidationFailure(getClass().getName() + " can only validate objects of type String."); //$NON-NLS-1$
	}

	// TODO: move this and the copy in ValidDecimal to a common utility class
	private String removeWhitespaceAndGroupingCharacter(final String string) {
		final StringBuffer sb = new StringBuffer(string.length());
		for (int t = 0; t < string.length(); ++t) {
			final char currentChar = string.charAt(t);
			if (!(Character.isWhitespace(currentChar) || currentChar == symbols.getGroupingSeparator())) {
				sb.append(currentChar);
			}
		}
		return sb.toString();
	}

}
