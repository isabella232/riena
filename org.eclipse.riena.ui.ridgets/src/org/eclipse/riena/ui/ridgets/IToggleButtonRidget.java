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
 * Ridget for a check box.
 */
public interface IToggleButtonRidget extends IValueRidget, IActionRidget, IMarkableRidget {

	/**
	 * <code>SELECTED_PROPERTY</code>
	 */
	String PROPERTY_SELECTED = "selected"; //$NON-NLS-1$

	/**
	 * Returns whether selection of the check box is on or off.
	 * 
	 * @return true, if the check box is selected; otherwise false
	 */
	boolean isSelected();

	/**
	 * Turns the selection of the check box on or off.
	 * 
	 * @param selected -
	 *            true: selection on; false: selections off
	 */
	void setSelected(boolean selected);
}
