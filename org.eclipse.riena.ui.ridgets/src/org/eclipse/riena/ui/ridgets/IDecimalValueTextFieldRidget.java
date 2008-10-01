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
package org.eclipse.riena.ui.ridgets;

/**
 * Ridget decimal text fields.
 * 
 * On the view-side we have to use the appropriate control-creation method of
 * the UIControlsFactory (see UIControlsFactory.createTextDecimal() /
 * UIControlFactory.createSegmentedDecimalValueTextField())
 * 
 * Models supported by this ridget are the String-based TextBean and the
 * Double-based DoubleBean (see {@link #bindToModel(Object, String)}.
 */
public interface IDecimalValueTextFieldRidget extends INumericValueTextFieldRidget {

	/** Property name of the singed property ("signed"). */
	String PROPERTY_PRECISION = "precision"; //$NON-NLS-1$

	/** Property name of the singed property ("signed"). */
	String PROPERTY_MAXLENGTH = "maxLength"; //$NON-NLS-1$

	/**
	 * Sets the number of fraction digits of this text field, that is the number
	 * of digits behind the comma-separator.
	 * 
	 * @param numberOfFractionDigits
	 */
	void setPrecision(int numberOfFractionDigits);

	/**
	 * Gets the number of fraction digits.
	 * 
	 * @return the number of fraction digits
	 */
	int getPrecision();

	/**
	 * Sets the number of allowed decimal-digits, that it , the number of digits
	 * before the comma-separator. Note that the maximum number of possible
	 * grouping-separators ( the dots between "1.034.235.123" ) has to be taken
	 * into account.
	 * 
	 * TODO [ev] update javadoc - the grouping/comma separator do not count
	 * 
	 * @param maxLength
	 */
	void setMaxLength(int maxLength);

	/**
	 * Returns the number of decimal digits.
	 * 
	 * @return the number of decimal digits.
	 */
	int getMaxLength();

}
