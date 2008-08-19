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
 * Adapter for a status line.
 */
public interface IStatuslineRidget extends IComplexRidget {

	/**
	 * @param aMessage
	 *            The info message.
	 */
	void info(String aMessage);

	/**
	 * @param aMessage
	 *            The warning message.
	 */
	void warning(String aMessage);

	/**
	 * @param aMessage
	 *            The error message.
	 */
	void error(String aMessage);

	/**
	 */
	void clear();

	/**
	 * @return The icon.
	 */
	String getIcon();

	/**
	 * @param pIcon
	 *            The icon to set.
	 */
	void setIcon(String pIcon);

	/**
	 * @return The message.
	 */
	String getMessage();

	/**
	 * @param message
	 *            The message to set.
	 */
	void setMessage(String message);

	/**
	 * 
	 */
	void hidePopups();

	/**
	 * @return the statuslineProcess
	 */
	IStatuslineProcessRidget getStatuslineProcessRidget();

	/**
	 * @return the statuslineNumer
	 */
	IStatuslineNumberRidget getStatuslineNumberRidget();
}
