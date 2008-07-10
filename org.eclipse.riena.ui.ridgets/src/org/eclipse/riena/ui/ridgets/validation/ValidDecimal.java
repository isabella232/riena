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
import java.text.ParseException;
import java.util.Locale;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;

/**
 * Checks if a given string could be safely converted to a decimal number, that
 * is a number with a fraction part. <br>
 * <br>
 * 
 * This rule supports partial correctness checking. Partial correct means that
 * we do not treat missing fraction as error, where &quot;missing fraction&quot;
 * means there no fraction digit.
 */
public class ValidDecimal implements IValidator {

	private static final char FRENCH_GROUPING_SEPARATOR = (char) 0xA0;
	private final boolean partialCheckSupported;
	private final DecimalFormat format;
	private final DecimalFormatSymbols symbols;

	/**
	 * Constructs a decimal type check plausibilisation rule with no
	 * partialChecking and the default Locale.
	 * 
	 */
	public ValidDecimal() {
		this(Locale.getDefault());
	}

	/**
	 * Constructs a decimal type check plausibilisation rule with no
	 * partialChecking and the given Locale.
	 * 
	 * @param locale
	 *            the locale
	 */
	public ValidDecimal(final Locale locale) {
		this(false, locale);
	}

	/**
	 * Constructs a decimal type check plausibilisation rule
	 * 
	 * @param partialCheckSupported
	 *            <tt>true</tt> if partial checking is required
	 * @param locale
	 *            the locale
	 */
	public ValidDecimal(final boolean partialCheckSupported, final Locale locale) {
		this.partialCheckSupported = partialCheckSupported;
		format = (DecimalFormat) DecimalFormat.getInstance(locale);
		format.setMaximumFractionDigits(15);
		symbols = format.getDecimalFormatSymbols();
	}

	/**
	 * Validates the given object. If the object is no String instance, a
	 * {@link ValidationFailure} will be thrown. The rule validates if the given
	 * object is a string, a well formed decimal according to the rule's
	 * {@linkplain Locale}.
	 * 
	 * @param object
	 *            the object to validate, must be of type String.
	 */
	public IStatus validate(final Object value) {
		if (value != null) {
			if (!(value instanceof String)) {
				throw new ValidationFailure("ValidCharacters can only validate objects of type String."); //$NON-NLS-1$
			}
			final String string = Utils.removeWhitespace((String) value);

			if (string.length() > 0) {
				final ScanResult scanned = scan(string);
				if (!partialCheckSupported) {
					if (scanned.decimalSeperatorIndex < 0) {
						return ValidationRuleStatus.error(true, "no decimal separator '"
								+ symbols.getDecimalSeparator() + "' in String '" + string + '\'', this);
					}
					// test if grouping character is behind decimal separator:
					if (scanned.groupingSeparatorIndex > scanned.decimalSeperatorIndex) {
						return ValidationRuleStatus.error(true, "grouping-separator '" + symbols.getGroupingSeparator()
								+ "' behind decimal-seperator '" + symbols.getDecimalSeparator() + "' in string '"
								+ string + '\'', this);
					}
				}
				// test if alien character present:
				if (scanned.lastAlienCharIndex > -1) {
					return ValidationRuleStatus.error(true, "unrecognized character '" + scanned.lastAlienCharacter
							+ "' in string '" + string + '\'', this);
				}
				try {
					synchronized (format) {// NumberFormat not threadsafe!
						format.parse(string);
					}
				} catch (final ParseException e) {
					return ValidationRuleStatus.error(true, "cannot parse string '" + string + "' to number.", this);
				}
			}
		}
		return ValidationRuleStatus.ok();
	}

	/**
	 * Contains the result of the {@link ValidDecimal#scan(String)} method.
	 */
	protected static final class ScanResult {
		/**
		 * The index of the decimal-separator character. If more than one is
		 * present, this will hold the last index.
		 */
		protected int decimalSeperatorIndex = -1;
		/**
		 * The index of the last grouping-separator character found.
		 */
		protected int groupingSeparatorIndex = -1;
		/**
		 * The index of the last minus sign character found.
		 */
		protected int minusSignIndex = -1;

		/**
		 * The last alien character found. Where &quot;alien&quot; means no
		 * digit, minus-sign, decimal-separator or grouping-separator. In case
		 * the grouping-character is <tt>(char)0xa0</tt>, like for the French
		 * locale's NumberFormat, whitespace is not considered alien either.
		 * 
		 * @see Character#isDigit(char)
		 * @see Character#isWhitespace(char)
		 * @see DecimalFormatSymbols
		 */
		protected char lastAlienCharacter;
		/**
		 * The index of the last alien character found. Where &quot;alien&quot;
		 * means no digit, minus-sign, decimal-separator or grouping-separator.
		 * In case the grouping-character is <tt>(char)0xa0</tt>, like for the
		 * French locale's NumberFormat, whitespace is not considered alien
		 * either.
		 * 
		 * @see Character#isDigit(char)
		 * @see Character#isWhitespace(char)
		 * @see DecimalFormatSymbols
		 */
		protected int lastAlienCharIndex = -1;

		private ScanResult() {
			// empty
		}
	}

	/**
	 * Scans the parameter's String instance and returns Information about
	 * indexes of different characters.
	 * 
	 * @param string
	 *            the string
	 * @return a ScanResult instance
	 */
	protected ScanResult scan(final String string) {
		final ScanResult result = new ScanResult();
		final boolean acceptWhitespaceAsGroupingSeperator = Character.isWhitespace(symbols.getGroupingSeparator())
				|| symbols.getGroupingSeparator() == FRENCH_GROUPING_SEPARATOR;
		final char minusSign = symbols.getMinusSign();
		for (int t = 0; t < string.length(); ++t) {
			final char currentChar = string.charAt(t);
			if (currentChar == symbols.getDecimalSeparator()) {
				result.decimalSeperatorIndex = t;
			} else if (currentChar == symbols.getGroupingSeparator()
					|| (Character.isWhitespace(currentChar) && acceptWhitespaceAsGroupingSeperator)) {
				result.groupingSeparatorIndex = t;
			} else if (currentChar == minusSign) {
				result.minusSignIndex = t;
			} else if (!Character.isDigit(currentChar)) {
				result.lastAlienCharacter = currentChar;
				result.lastAlienCharIndex = t;
			}
		}
		return result;
	}

	/**
	 * Gets this rule's NumberFormat to parse a string. Accessing the format
	 * must be synchronized, as it is not thread safe.
	 * 
	 * @see DecimalFormatSymbols
	 * @return a {@linkplain DecimalFormat} instance
	 */
	protected DecimalFormat getFormat() {
		return format;
	}

	/**
	 * Gets the s DecimalFormatSymbols of this rule's {@link #format}. Changes
	 * on the symbols will change the rule's format accordingly. As
	 * DecimalFormat is not thread safe, changes to the symbols must be properly
	 * synchronized with accessing the format.
	 * 
	 * @see #getFormat()
	 */
	protected DecimalFormatSymbols getSymbols() {
		return symbols;
	}

}
