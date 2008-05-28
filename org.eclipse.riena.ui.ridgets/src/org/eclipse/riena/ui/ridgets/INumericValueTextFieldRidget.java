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
 * Ridget for a numeric text field.
 */
public interface INumericValueTextFieldRidget extends ITextFieldRidget {

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

}
