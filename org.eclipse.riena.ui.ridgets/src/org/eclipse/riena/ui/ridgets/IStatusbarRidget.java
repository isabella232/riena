/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2005 compeople AG                         *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets;

/**
 * Adapter for a statusbar.
 * 
 * @author Juergen Becker
 * @author Sebastian Stanossek Erweiterung um Processanzeige, Aufteilung in
 *         Teilkomponenten
 * @author Carsten Drossel
 */
public interface IStatusbarRidget extends IComplexRidget {

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
	 * @return the statusBarProcess
	 */
	IStatusbarProcessRidget getStatusBarProcessRidget();

	/**
	 * @return the statusBarNumer
	 */
	IStatusbarNumberRidget getStatusBarNumberRidget();

}