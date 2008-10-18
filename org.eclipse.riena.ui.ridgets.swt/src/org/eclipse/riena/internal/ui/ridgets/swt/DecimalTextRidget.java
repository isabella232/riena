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
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.math.BigDecimal;

import org.eclipse.core.runtime.Assert;
import org.eclipse.riena.ui.ridgets.IDecimalValueTextFieldRidget;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Ridget for a 'decimal' SWT <code>Text</code> widget.
 * <p>
 * Implementation note: all the logic is in NumericTextRidget. This class justs
 * adds the API mandated by IDecimalValueTextFieldRidget.
 * 
 * @see UIControlsFactory#createTextDecimal(org.eclipse.swt.widgets.Composite)
 */
public class DecimalTextRidget extends NumericTextRidget implements IDecimalValueTextFieldRidget {

	public DecimalTextRidget() {
		setMaxLength(10);
		setPrecision(2);
		setText("0"); //$NON-NLS-1$
		setSigned(false);
	}

	@Override
	protected void checkNumber(String number) {
		if (!"".equals(number)) { //$NON-NLS-1$
			try {
				new BigDecimal(ungroup(number));
			} catch (NumberFormatException nfe) {
				throw new NumberFormatException("Not a valid decimal: " + number); //$NON-NLS-1$
			}
		}
	}

	protected boolean isNegative(String text) {
		BigDecimal value = new BigDecimal(text);
		return (value.compareTo(BigDecimal.ZERO) < 0);
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
			firePropertyChange(IDecimalValueTextFieldRidget.PROPERTY_MAXLENGTH, oldValue, maxLength);
		}
	}

	@Override
	public final synchronized void setPrecision(int numberOfFractionDigits) {
		Assert.isLegal(numberOfFractionDigits > -1, "numberOfFractionDigits must > -1: " + numberOfFractionDigits); //$NON-NLS-1$
		int oldValue = getPrecision();
		if (oldValue != numberOfFractionDigits) {
			super.setPrecision(numberOfFractionDigits);
			firePropertyChange(IDecimalValueTextFieldRidget.PROPERTY_PRECISION, oldValue, numberOfFractionDigits);
		}
	}

}
