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

public interface ITabbedPaneRidget extends IRidget {

	/**
	 * The name of the client property under which the list of the tabs to blink
	 * is expected. The list should be a Set(Integer). All included Tabs will be
	 * blinking.
	 */
	String PROPERTY_BLINKINGTABS = "PROPERTY_BLINKINGTABS"; //$NON-NLS-1$

	/**
	 * Adds a {@link ITabbedPaneRidgetListener} for receiving tab selection
	 * events from this ridget.
	 * <p>
	 * Adding the same listener several times has no effect.
	 * 
	 * @param listener
	 *            the listener to be added (non-null)
	 * @throws RuntimeException
	 *             if listener is null
	 */
	void addTabbedPaneRidgetListener(ITabbedPaneRidgetListener listener);

	/**
	 * Removes a {@link ITabbedPaneRidgetListener} for receiving tab selection
	 * events from this ridget.
	 * 
	 * @param listener
	 *            the listener to be removed (non-null)
	 * @throws RuntimeException
	 *             if listener is null
	 */
	void removeTabbedPaneRidgetListener(ITabbedPaneRidgetListener listener);

	void setSelectedTab(int index);
}
