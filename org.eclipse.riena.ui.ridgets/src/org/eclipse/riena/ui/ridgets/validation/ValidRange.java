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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;

/**
 * Implementation for a plausibility rule for a number range check. Checks <br>
 * if a given string could be safely converted to a given number type and if the
 * input is in a given range. Note that a value of <tt>null</tt> or an empty
 * String is treated as zero.<br>
 * This rule does not support partial correctness checking.
 */
public class ValidRange extends ValidDecimal {

	private BigDecimal min;
	private BigDecimal max;

	/**
	 * Constructs a number type check plausibilisation rule for the default
	 * locale.
	 * 
	 * @param min
	 *            the minimum value
	 * @param max
	 *            the maximum value
	 * @throws some_kind_of_runtime_exception
	 *             if <tt>min &gt;= max</tt>.
	 * @throws some_kind_of_runtime_exception
	 *             if a parameter is <tt>null</tt>.
	 * @throws some_kind_of_runtime_exception
	 *             if a parameter <tt>min</tt> and <tt>max</tt> do not belong to
	 *             the same class.
	 */
	public ValidRange(final Number min, final Number max) {
		this(min, max, Locale.getDefault());
	}

	/**
	 * Constructs a number type check plausibilisation rule for the default
	 * locale.
	 * 
	 * @param min
	 *            the minimum value
	 * @param max
	 *            the maximum value
	 * @throws some_kind_of_runtime_exception
	 *             if <tt>min &gt;= max</tt>.
	 * @throws some_kind_of_runtime_exception
	 *             if a parameter is <tt>null</tt>.
	 * @throws some_kind_of_runtime_exception
	 *             if a parameter <tt>min</tt> and <tt>max</tt> do not belong to
	 *             the same class.
	 */
	public ValidRange(final Number min, final Number max, final Locale locale) {
		super(true, locale);
		Assert.isNotNull(min, "parameter min must not be null"); //$NON-NLS-1$
		Assert.isNotNull(max, "parameter max must not be null"); //$NON-NLS-1$
		Assert.isLegal(min.getClass().equals(max.getClass()), "min and max must be of the same class. (min =  " //$NON-NLS-1$
				+ min.getClass().getName() + ", max = " + max.getClass().getName()); //$NON-NLS-1$		
		this.min = toBigDecimal(min);
		this.max = toBigDecimal(max);
		Assert.isLegal(this.min.compareTo(this.max) <= 0, "min " + this.min + " must be smaller or equal max "
				+ this.max);
	}

	/**
	 * @return an IStatus instance
	 * @param value
	 *            a <tt>String</tt> instance or <tt>null</tt>, where
	 *            <tt>null</tt> is treated as zero.
	 * @see org.eclipse.riena.ui.ridgets.validation.ValidDecimal#validate(java.lang.Object)
	 */
	@Override
	public IStatus validate(final Object value) {
		final IStatus validDecimalStatus = super.validate(value);
		if (!validDecimalStatus.isOK()) {
			return validDecimalStatus;
		}
		assert value == null || value instanceof String;
		BigDecimal currentValue = BigDecimal.ZERO;
		if (value != null) {
			final String string = Utils.removeWhitespace((String) value);
			if (string.length() > 0) {
				final DecimalFormat format = getFormat();
				synchronized (format) { // format not thread safe!
					format.setParseBigDecimal(true);
					try {
						currentValue = (BigDecimal) format.parse(string);
					} catch (final ParseException e) {
						// should never occur, as super.validate(Object) will
						// make this method return earlier
						return ValidationRuleStatus
								.error(true, "cannot parse string '" + string + "' to number.", this);
					}
				}
			}
		}
		if (currentValue.compareTo(min) < 0 || currentValue.compareTo(max) > 0) {
			return ValidationRuleStatus.error(true, "value " + currentValue + "' out of range: [" + min + ".." + max
					+ "].", this);
		}
		return ValidationRuleStatus.ok();
	}

	/**
	 * Convert a number to BigDecimal.
	 * 
	 * @param number
	 *            a number
	 * @return a BigDecimal instance
	 */
	protected BigDecimal toBigDecimal(final Number number) {
		if (number instanceof BigDecimal) {
			return (BigDecimal) number;
		} else if (number instanceof BigInteger) {
			return new BigDecimal((BigInteger) number);
		} else if (number instanceof Float || number instanceof Double) {
			return new BigDecimal(number.doubleValue());
		} else {
			return new BigDecimal(number.longValue());
		}
	}
}
