/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets;

/**
 * window event listener.
 * 
 * @author Juergen Becker
 */
public interface IWindowListener {
	/**
	 * callback method called if the window has been activated.
	 * 
	 * @param e
	 *            event which caused the callback
	 */
	void windowActivated(WindowEvent e);

	/**
	 * callback method called if the window has been closed.
	 * 
	 * @param e
	 *            event which caused the callback
	 */
	void windowClosed(WindowEvent e);

	/**
	 * callback method called if the window has been deactivated.
	 * 
	 * @param e
	 *            event which caused the callback
	 */
	void windowDeactivated(WindowEvent e);
}