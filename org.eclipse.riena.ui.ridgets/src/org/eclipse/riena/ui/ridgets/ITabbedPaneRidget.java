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

public interface ITabbedPaneRidget extends IRidget {

	/**
	 * The name of the client property under which the list of the tabs to blink
	 * is expected. The list should be a Set(Integer). All included Tabs will be
	 * blinking.
	 */
	String PROPERTY_BLINKINGTABS = "PROPERTY_BLINKINGTABS";

	void addTabbedPaneRidgetListener(ITabbedPaneRidgetListener listener);

	void removeTabbedPaneRidgetListener(ITabbedPaneRidgetListener listener);

	void setSelectedTab(int index);
}
