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

import org.apache.oro.text.perl.Perl5Util;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;

/**
 * Implementation for a regular expression check. If the rule fails it will
 * prevent updating the ridget and model values. The rule will not block invalid
 * input to the widget.
 * <p>
 * Note that the value <tt>null</tt> will be treated as an empty string. The
 * option <tt>i</tt>, <tt>m</tt> and <tt>x</tt>, as featured in the Perl5 native
 * format <quote>[m]/pattern/[i][m][s][x]</quote> are supported through the
 * {@link Option} enumeration.
 * <p>
 * At the moment this class supports Perl5 regular expressions. It is however
 * recommended to stick to common standards between
 * {@linkplain java.util.regex.Pattern java.util.regex} and Perl5 regular
 * expressions, if possible.
 * <p>
 * This validation rule does not support partial correctness checking.
 * <p>
 * 
 * @see org.apache.oro.text.perl.Perl5Util#match(String, String)
 * @see java.util.regex.Pattern
 */
public class ValidExpression implements IValidationRule {

	/** <code>GERMAN_ZIP</code> */
	public static final String GERMAN_ZIP = "^[0-9]{5}$";

	/**
	 * @see http://de.wikipedia.org/wiki/SWIFT
	 */
	public static final String SWIFT_BIC = "^([a-zA-Z]{6}[a-zA-Z\\d]{2})([a-zA-Z\\d]{3})?$";

	/**
	 * Option postfixes.
	 * 
	 * @see org.apache.oro.text.perl.Perl5Util#match(String, String)
	 * @see java.util.regex.Pattern
	 */
	public static enum Option {
		/** Enables case insensitive matching. */
		CASE_INSENSITIVE('i'),
		/** Treats input as it would consist of multiple lines. */
		MULTIPLE_LINE('m'),
		/** Enables the extended expression syntax with whitespace and comments. */
		ENABLE_EXTENDED_SYNTAX('x');
		private final char symbol;

		private Option(final char symbol) {
			this.symbol = symbol;
		}
	}

	/*
	 * Concurrent access is okay as long as #getMatch() is not used.
	 * 
	 * @see org.apache.oro.text.perl.Perl5Util
	 */
	private final Perl5Util matcher = new Perl5Util();

	private final String pattern;
	private final StringBuffer options;

	/**
	 * Constructs a new ValidExpression rule with the given options.
	 * 
	 * @param regex
	 *            the regular expression to check against
	 * @see org.apache.oro.text.perl.Perl5Util#match(String, String)
	 * @see java.util.regex.Pattern#matches(String, CharSequence)
	 * @throws some_kind_of_runtime_exception
	 *             if parameter is <tt>null</tt>, pattern is a String of length
	 *             zero, or pattern is malformed.
	 */
	public ValidExpression(final String pattern, final Option... options) {
		Assert.isNotNull(pattern, "pattern must not be null"); //$NON-NLS-1$
		Assert.isLegal(pattern.length() > 0, "pattern must not be empty"); //$NON-NLS-1$
		this.pattern = pattern;
		this.options = new StringBuffer(4);
		for (final Option option : options) {
			this.options.append(option.symbol);
		}
	}

	/**
	 * 
	 * @see org.eclipse.riena.ui.ridgets.validation.IValidationRule#getValidationTime()
	 */
	public ValidationTime getValidationTime() {
		return ValidationTime.ON_UI_CONTROL_EDITED;
	}

	/**
	 * Matches the given object against this rule's regular expression.
	 * 
	 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
	 */
	public IStatus validate(final Object value) {
		if (!(value == null || value instanceof String)) {
			throw new ValidationFailure(getClass().getSimpleName() + " can only validate objects of type "
					+ String.class.getName());
		}
		final String string = value == null ? "" : (String) value; //$NON-NLS-1$
		// validates if char is not either whitespace or ignored.
		if (matcher.match("/" + pattern + "/" + options, string)) { //$NON-NLS-1$//$NON-NLS-2$
			return ValidationRuleStatus.ok();
		}
		return ValidationRuleStatus.error(false, "'String '" + string + "' does not match regex '" + pattern + "'.",
				this);
	}

}
