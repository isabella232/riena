/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
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
	 * @param visible
	 *            The visible value to set.
	 */
	void setVisible(boolean visible);

	/**
	 * Returns whether the adapter is blocked or not
	 * 
	 * @return true, if adapter is bloacked; otherwise false
	 */
	boolean isBlocked();

	/**
	 * Sets whether the adapter is blocked or not
	 * 
	 * @param blocked
	 *            - true, if adapter is bloacked; otherwise false
	 */
	void setBlocked(boolean blocked);

}
