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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.riena.core.util.ArraysUtil;
import org.eclipse.riena.core.util.PropertiesUtils;

/**
 * Tests, if input is an valid integer, which may be signed or not.
 * 
 * @author Wanja Gayk
 */
public class ValidInteger extends ValidDecimal {

	private boolean signed;

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
	@Override
	public IStatus validate(final Object value) {
		if (value != null) {
			if (!(value instanceof String)) {
				throw new ValidationFailure("ValidCharacters can only validate objects of type String."); //$NON-NLS-1$
			}
			final String string = Utils.removeWhitespace((String) value);

			if (string.length() > 0) {
				final ScanResult scanned = scan(string);
				if (scanned.decimalSeperatorIndex >= 0) {
					return ValidationRuleStatus.error(true, "no integer: decimal separator '" //$NON-NLS-1$
							+ getSymbols().getDecimalSeparator() + "' in String '" + string + '\'', this); //$NON-NLS-1$
				}
				// test if sign present
				if (!signed && scanned.minusSignIndex > -1) {
					return ValidationRuleStatus.error(true, "minus sign present at position '" + scanned.minusSignIndex //$NON-NLS-1$
							+ "' in string '" + string + "' where an unsigned integer was expected.", this); //$NON-NLS-1$ //$NON-NLS-2$
				}
				// test if alien character present:
				if (scanned.lastAlienCharIndex > -1) {
					return ValidationRuleStatus.error(true, "unrecognized character '" + scanned.lastAlienCharacter //$NON-NLS-1$
							+ "' in string '" + string + '\'', this); //$NON-NLS-1$
				}
				try {
					final DecimalFormat format = getFormat();
					synchronized (format) {// NumberFormat not threadsafe!
						format.parse(string);
					}
				} catch (final ParseException e) {
					return ValidationRuleStatus.error(true, "cannot parse string '" + string + "' to number.", this); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		return ValidationRuleStatus.ok();
	}

	/**
	 * This method is called on a newly constructed extension for validation.
	 * After creating a new instance of {@code ValidInteger} this method is
	 * called to initialize the instance. The argument for initialization are in
	 * the parameter {@code data}. Is the data a string the arguments are
	 * separated with ','. The order of the arguments in data is equivalent to
	 * the order of the parameters of one of the constructors.<br>
	 * If data has more than one argument. The last arguments are used to set
	 * the Local for this validator.
	 * 
	 * 
	 * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement,
	 *      java.lang.String, java.lang.Object)
	 * @see org.eclipse.riena.ui.ridgets.validation.ValidDecimal#setLocal(java.lang.String[])
	 */
	@Override
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
			throws CoreException {

		if (data instanceof String) {
			String[] args = PropertiesUtils.asArray(data);
			int localStart = 0;
			if (args.length > 0) {
				if (args[0].equals(Boolean.TRUE.toString())) {
					this.signed = true;
					localStart = 1;
				} else if (args[0].equals(Boolean.FALSE.toString())) {
					this.signed = false;
					localStart = 1;
				}
			}
			if (args.length > localStart) {
				String[] localArgs = ArraysUtil.copyRange(args, localStart, args.length);
				setLocal(localArgs);
			}
		}

	}
}
