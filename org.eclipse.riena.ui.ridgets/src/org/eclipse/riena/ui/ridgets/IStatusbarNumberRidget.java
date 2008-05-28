/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2005 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets;

/**
 * Interface for the Adapter of the number component of the statusbar
 * 
 * @author SST
 */
public interface IStatusbarNumberRidget extends IRidget {
	/**
	 * @return Returns the number.
	 */
	Integer getNumber();

	/**
	 * <b>Note: </b> If you set the number, the number string is set
	 * <code>null</code>
	 * 
	 * @param number
	 *            The number to set.
	 * @see #setNumberString(String)
	 */
	void setNumber(Integer number);

	/**
	 * Returns the string, that will be display in the status bar at the
	 * position of the number.
	 * 
	 * @return string in the status bar at the position of the number
	 */
	String getNumberString();

	/**
	 * Sets the string, that will be display in the status bar at the position
	 * of the number. <b>Note: </b> If you set the number string, the number is
	 * set 0
	 * 
	 * @param numberStrg -
	 *            the string to displayed in the status bar; null removes the
	 *            number string from the status bar
	 * @see #setNumber(int)
	 */
	void setNumberString(String numberStrg);
}
