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

import org.eclipse.riena.ui.ridgets.listener.IWindowRidgetListener;

/**
 * Ridget for a Window.
 * <p>
 * 
 * Supports methods for controlling the appearance of the underlying UI-Control.
 * 
 */
public interface IWindowRidget extends IRidget {

	/**
	 * Adds a {@link IWindowRidgetListener} for receiving window events from
	 * this ridget.
	 * <p>
	 * Adding the same listener several times has no effect.
	 * 
	 * @param listener
	 *            the listener to be added (non-null)
	 * @throws RuntimeException
	 *             if listener is null
	 */
	void addWindowRidgetListener(IWindowRidgetListener pListener);

	/**
	 * Releases all of the native screen resources used by this window.
	 */
	void dispose();

	/**
	 * Re-layout all contents of this window. Calls to this method will be
	 * ignored if no control is bound.
	 * 
	 * @since 1.2
	 */
	void layout();

	/**
	 * Returns the window's defaultButton or null.
	 */
	Object getDefaultButton();

	/**
	 * Removes a listener for receiving window events from this ridget.
	 * 
	 * @param listener
	 *            the listener to be removed (non-null)
	 * @throws RuntimeException
	 *             if listener is null
	 */
	void removeWindowRidgetListener(IWindowRidgetListener pListener);

	/**
	 * Changes the active-state of the WindowRidget depending of the underlying
	 * implementation for example enables the shell.
	 * 
	 * @param active
	 */
	void setActive(boolean active);

	/**
	 * Sets the closeable flag if supported by the implementation, otherwise
	 * nothing will happen.
	 * 
	 * @param closeable
	 */
	void setCloseable(boolean closeable);

	/**
	 * Sets the default button.
	 * 
	 * @param defaultButton
	 *            default button
	 */
	void setDefaultButton(Object defaultButton);

	/**
	 * set the icon.
	 * 
	 * @param icon
	 *            The icon name.
	 */
	void setIcon(String icon);

	/**
	 * Sets the window-title of the Ridget.
	 * 
	 * @param title
	 *            a non null String
	 */
	void setTitle(String title);
}
