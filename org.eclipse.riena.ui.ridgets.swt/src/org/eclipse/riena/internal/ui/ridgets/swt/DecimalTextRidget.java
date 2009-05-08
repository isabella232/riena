/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.conversion.NumberToStringConverter;
import org.eclipse.core.runtime.Assert;

import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.ui.ridgets.IDecimalTextRidget;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Ridget for a 'decimal' SWT <code>Text</code> widget.
 * <p>
 * Implementation note: all the logic is in NumericTextRidget. This class justs
 * adds the API mandated by IDecimalTextRidget.
 * 
 * @see UIControlsFactory#createTextDecimal(org.eclipse.swt.widgets.Composite)
 */
public class DecimalTextRidget extends NumericTextRidget implements IDecimalTextRidget {

	public DecimalTextRidget() {
		setMaxLength(10);
		setPrecision(2);
		setText("0"); //$NON-NLS-1$
		setSigned(false);
	}

	@Override
	protected void checkNumber(String number) {
		if (!"".equals(number)) { //$NON-NLS-1$
			BigDecimal value = checkIsNumber(number);
			checkSigned(value);
			checkMaxLength(number);
			checkPrecision(number);
		}
	}

	@Override
	protected boolean isNegative(String text) {
		BigDecimal value = new BigDecimal(localStringToBigDecimal(text));
		return value.compareTo(BigDecimal.ZERO) < 0;
	}

	@Override
	protected boolean isNotEmpty(String text) {
		String stripped = removeLeadingCruft(removeTrailingPadding(text));
		return stripped.length() > 0;
	}

	@Override
	public synchronized int getMaxLength() {
		return super.getMaxLength();
	}

	@Override
	public synchronized int getPrecision() {
		return super.getPrecision();
	}

	@Override
	public final synchronized void setMaxLength(int maxLength) {
		Assert.isLegal(maxLength > 0, "maxLength must be greater than zero: " + maxLength); //$NON-NLS-1$
		int oldValue = getMaxLength();
		if (oldValue != maxLength) {
			super.setMaxLength(maxLength);
			firePropertyChange(IDecimalTextRidget.PROPERTY_MAXLENGTH, oldValue, maxLength);
		}
	}

	@Override
	public final synchronized void setPrecision(int numberOfFractionDigits) {
		Assert.isLegal(numberOfFractionDigits > -1, "numberOfFractionDigits must > -1: " + numberOfFractionDigits); //$NON-NLS-1$
		int oldValue = getPrecision();
		if (oldValue != numberOfFractionDigits) {
			super.setPrecision(numberOfFractionDigits);
			firePropertyChange(IDecimalTextRidget.PROPERTY_PRECISION, oldValue, numberOfFractionDigits);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * 
	 * @throws RuntimeException
	 *             if the value obtain from model exceeds the specified maximum
	 *             length or precision. It is responsibility of application to
	 *             handle this.
	 */
	@Override
	public synchronized void updateFromModel() {
		// we explicitly check the value here, because if the check fails in setText(...)
		// the runtime exception is silently swallowed by the data binding
		checkValue();
		super.updateFromModel();
	}

	// helping methods
	//////////////////

	private BigDecimal checkIsNumber(String number) {
		try {
			return new BigDecimal(localStringToBigDecimal(number));
		} catch (NumberFormatException nfe) {
			throw new NumberFormatException("Not a valid decimal: " + number); //$NON-NLS-1$
		}
	}

	private void checkMaxLength(String number) {
		int maxLength = getMaxLength();
		int length;
		int decSepIndex = number.indexOf(DECIMAL_SEPARATOR);
		if (decSepIndex != -1) {
			String decimalPart = number.substring(0, decSepIndex);
			length = decimalPart.length() - StringUtils.count(decimalPart, GROUPING_SEPARATOR);
		} else {
			length = number.length() - StringUtils.count(number, GROUPING_SEPARATOR);
		}
		if (maxLength < length) {
			String msg = String.format("Length (%d) exceeded: %s", maxLength, number); //$NON-NLS-1$
			throw new NumberFormatException(msg);
		}
	}

	private void checkPrecision(String number) {
		int decSepIndex = number.indexOf(DECIMAL_SEPARATOR);
		if (decSepIndex != -1) {
			int precision = getPrecision();
			int fractionalDigits = number.substring(decSepIndex).length() - 1;
			if (precision < fractionalDigits) {
				String msg = String.format("Precision (%d) exceeded: %s", precision, number); //$NON-NLS-1$
				throw new NumberFormatException(msg);
			}
		}
	}

	private void checkSigned(BigDecimal value) {
		if (!isSigned() && value.compareTo(BigDecimal.ZERO) == -1) {
			throw new NumberFormatException("Negative numbers not allowed: " + value); //$NON-NLS-1$
		}
	}

	private void checkValue() {
		Object value = getValueBindingSupport().getModelObservable().getValue();
		if (value != null) {
			IConverter converter = getConverter(value);
			if (converter != null) {
				checkNumber((String) converter.convert(value));
			}
		}
	}

	private IConverter getConverter(Object value) {
		Assert.isNotNull(value);
		IConverter result = getValueBindingSupport().getModelToUIControlConverter();
		if (result == null) {
			if (value instanceof BigDecimal) {
				result = NumberToStringConverter.fromBigDecimal();
			} else if (value instanceof BigInteger) {
				result = NumberToStringConverter.fromBigInteger();
			} else if (value instanceof Byte) {
				result = NumberToStringConverter.fromByte(false);
			} else if (value instanceof Double) {
				result = NumberToStringConverter.fromDouble(false);
			} else if (value instanceof Float) {
				result = NumberToStringConverter.fromFloat(false);
			} else if (value instanceof Integer) {
				result = NumberToStringConverter.fromInteger(false);
			} else if (value instanceof Long) {
				result = NumberToStringConverter.fromLong(false);
			} else if (value instanceof Short) {
				result = NumberToStringConverter.fromShort(false);
			}
		}
		return result;
	}

	private String localStringToBigDecimal(String number) {
		return ungroup(number).replace(DECIMAL_SEPARATOR, '.');
	}

}
