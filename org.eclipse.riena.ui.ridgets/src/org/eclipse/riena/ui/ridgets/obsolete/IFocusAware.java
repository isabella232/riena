/****************************************************************
 *                                                              *
 * Copyright (c) 2005 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
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
