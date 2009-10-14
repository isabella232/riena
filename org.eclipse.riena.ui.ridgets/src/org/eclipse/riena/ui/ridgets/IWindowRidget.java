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
	 * Removes a listener for receiving window events from this ridget.
	 * 
	 * @param listener
	 *            the listener to be removed (non-null)
	 * @throws RuntimeException
	 *             if listener is null
	 */
	void removeWindowRidgetListener(IWindowRidgetListener pListener);

	void setTitle(String title);

	/**
	 * set the icon.
	 * 
	 * @param icon
	 *            The icon name.
	 */
	void setIcon(String icon);

	/**
	 * Sets the default button.
	 * 
	 * @param defaultButton
	 *            default button
	 */
	void setDefaultButton(Object defaultButton);

	/**
	 * Answer the windows defaultButton or null
	 * 
	 * @return
	 */
	Object getDefaultButton();

	void setCloseable(boolean closeable);

	void setActive(boolean active);

	/**
	 * Releases all of the native screen resources used by this window.
	 */
	void dispose();
}
