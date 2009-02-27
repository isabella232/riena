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
package org.eclipse.riena.ui.ridgets;

/**
 * Ridget for a numeric text field.
 * 
 * On the view-side we have to use the appropriate control-creation method of
 * the UIControlsFactory (see UIControlsFactory.createTextNumeric()).
 * 
 * The ridget uses strings internally. It can be bound to any model that is
 * supported by the JFace databinding, provided the bounds value can be
 * converted to a string representing a number.
 */
public interface INumericTextRidget extends ITextRidget {

	/** Property name of the singed property ("signed"). */
	String PROPERTY_SIGNED = "signed"; //$NON-NLS-1$

	/**
	 * @return Indicates whether grouping is used to separate thousands.
	 */
	boolean isGrouping();

	/**
	 * Sets whether grouping is used to separate thousands.
	 * 
	 * @param useGrouping
	 *            The new grouping state.
	 */
	void setGrouping(boolean useGrouping);

	/**
	 * @return Indicates whether negative values are allowed.
	 */
	boolean isSigned();

	/**
	 * Sets whether negative values are allowed.
	 * 
	 * @param signed
	 *            The new signed state.
	 */
	void setSigned(boolean signed);

	/**
	 * @return true if negative value should be marked
	 */
	boolean isMarkNegative();

	/**
	 * @param mustBeMarked
	 *            whether to mark or not a negative value
	 */
	void setMarkNegative(boolean mustBeMarked);
}
