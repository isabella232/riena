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
import java.text.ParseException;
import java.util.Locale;

import org.eclipse.core.runtime.IStatus;

/**
 * Tests, if input is an valid integer, which may be signed or not.
 * 
 * @author Wanja Gayk
 */
public class ValidInteger extends ValidDecimal {

	private final boolean signed;

	/**
	 * Constructs a rule t check whether the given object is a String which can
	 * be safely parsed to an Integer. The rule works for the default Locale and
	 * for signed values.
	 */
	public ValidInteger() {
		this(Locale.getDefault());
	}

	/**
	 * Constructs a rule t check whether the given object is a String which can
	 * be safely parsed to an Integer. The rule works for the default Locale.
	 * 
	 * @param signed
	 *            if <tt>true</tt> the rule allows minus signs.
	 */
	public ValidInteger(final boolean signed) {
		this(signed, Locale.getDefault());
	}

	/**
	 * Constructs a rule t check whether the given object is a String which can
	 * be safely parsed to an Integer. The rule works for signed values and the
	 * given locale.
	 * 
	 * @param locale
	 *            the locale
	 */
	public ValidInteger(final Locale locale) {
		this(true, locale);
	}

	/**
	 * Constructs a rule t check whether the given object is a String which can
	 * be safely parsed to an Integer.
	 * 
	 * @param signed
	 *            if <tt>true</tt> the rule allows minus signs.
	 * @param locale
	 *            the locale
	 */
	public ValidInteger(final boolean signed, final Locale locale) {
		super(true, locale);
		this.signed = signed;
	}

	/**
	 * Validates the given object. If the object is no String instance, a
	 * {@link ValidationFailure} will be thrown. The rule validates if the given
	 * object is a string, a well formed integer according to the rule's
	 * {@linkplain Locale}
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
				if (scanned.decimalSeperatorIndex >= 0) {
					return ValidationRuleStatus.error(true, "no integer: decimal separator '"
							+ getSymbols().getDecimalSeparator() + "' in String '" + string + '\'', this);
				}
				// test if sign present
				if (!signed && scanned.minusSignIndex > -1) {
					return ValidationRuleStatus.error(true, "minus sign present at position '" + scanned.minusSignIndex
							+ "' in string '" + string + "' where an unsigned integer was expected.", this);
				}
				// test if alien character present:
				if (scanned.lastAlienCharIndex > -1) {
					return ValidationRuleStatus.error(true, "unrecognized character '" + scanned.lastAlienCharacter
							+ "' in string '" + string + '\'', this);
				}
				try {
					final DecimalFormat format = getFormat();
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
}
