/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.obsolete;



/**
 * This interface defines the common ground of UI representations.
 *
 * @author Ralf Stuckert
 */
public interface IUIRepresentation extends IFocusAware {

	/**
	 * @return Returns the visible value.
	 */
	boolean isVisible();

	/**
	 * @param visible The visible value to set.
	 */
	void setVisible( boolean visible );

	/**
	 * Returns whether the adapter is blocked or not
	 *
	 * @return true, if adapter is bloacked; otherwise false
	 */
	boolean isBlocked();

	/**
	 * Sets whether the adapter is blocked or not
	 *
	 * @param blocked - true, if adapter is bloacked; otherwise false
	 */
	void setBlocked( boolean blocked );

}
