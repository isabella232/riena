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