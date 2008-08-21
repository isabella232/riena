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
package org.eclipse.riena.ui.ridgets.obsolete;

import org.eclipse.riena.ui.ridgets.listener.IFocusListener;

/**
 * A <code>IFocusAware</code> is capable of notifying registered
 * {@link IFocusListener IFocusListener}s on focus changes related to this
 * instance.
 * 
 * @author Ralf Stuckert
 */
public interface IFocusAware {

	/**
	 * Adds a {@link IFocusListener IFocusListener} that will be notified on
	 * focus events.
	 * 
	 * @param listener
	 *            the listener to notify.
	 */
	void addFocusListener(IFocusListener listener);

	/**
	 * Removes the given {@link IFocusListener IFocusListener}.
	 * 
	 * @param listener
	 *            the listener to remove.
	 */
	void removeFocusListener(IFocusListener listener);

	/**
	 * @return <code>true</code> if this instance has the focus.
	 */
	boolean hasFocus();

}
