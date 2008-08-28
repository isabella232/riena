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
 * A ridget for UI controls that allow an action to be performed on them, like
 * clicking a button.
 */
public interface IActionRidget extends IMarkableRidget {

	/**
	 * Add an action listener to call back.
	 * <p>
	 * Adding the same listener several times has no effect.
	 * 
	 * @param listener
	 *            a non-null action listener.
	 */
	void addListener(IActionListener listener);

	/**
	 * Add an action listener to call back. This method is equal to calling
	 * #addListener(java.beans.EventHandler.create(IActionListener.class,
	 * target, method))
	 * <p>
	 * Adding the same listener several times has no effect.
	 * 
	 * @see java.beans.EventHandler#create(Class, Object, String)
	 * @param target
	 * @param action
	 */
	void addListener(Object target, String action);

	/**
	 * Remove an action listener.
	 * 
	 * @param listener
	 *            the action listener.
	 */
	void removeListener(IActionListener listener);

	/**
	 * Set the text used to visualize the action.
	 * 
	 * @param newText
	 *            the text.
	 */
	void setText(String newText);

	/**
	 * Get the text used to visualize the action.
	 * 
	 * @return the text.
	 */
	String getText();

	/**
	 * Returns the name of the icon.
	 * 
	 * @see #setIcon
	 * 
	 * @return Returns the name of icon.
	 */
	String getIcon();

	/**
	 * Sets the name of the icon.<br>
	 * 
	 * @param name
	 *            of the new icon
	 */
	void setIcon(String iconName);
}
