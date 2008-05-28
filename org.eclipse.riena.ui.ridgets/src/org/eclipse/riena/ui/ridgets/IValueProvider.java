/****************************************************************
 *                                                              *
 * Copyright (c) 2005 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/

package org.eclipse.riena.ui.ridgets;

/**
 * Gets and sets the value of a model object.
 * 
 * @author Carsten Drossel
 * @author Sebastian Stanossek deprication of setModel
 */

/**
 * @deprecated Use data binding of ridgets
 */
@Deprecated
public interface IValueProvider {

	/**
	 * Returns the value.
	 * 
	 * @return The value
	 */
	Object getValue();

	/**
	 * Sets the value.
	 * 
	 * @param value
	 *            The new value
	 */
	void setValue(Object value);

}
