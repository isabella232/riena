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
 * Ridget for a status line consisting of a message and a icon.
 * 
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
	 * Clears the message and the icon.
	 * 
	 */
	void clear();

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
	 * Method is not implemented at the moment!
	 */
	void hidePopups();

	/**
	 * @return the statuslineProcess
	 */
	IStatuslineUIProcessRidget getStatuslineUIProcessRidget();

	/**
	 * @return the statuslineNumer
	 */
	IStatuslineNumberRidget getStatuslineNumberRidget();

}
