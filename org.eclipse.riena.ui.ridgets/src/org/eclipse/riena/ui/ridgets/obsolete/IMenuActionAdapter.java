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
package org.eclipse.riena.ui.ridgets.obsolete;

import org.eclipse.riena.ui.ridgets.IActionAdapter;
import org.eclipse.riena.ui.ridgets.menu.IMenuItem;
import org.eclipse.riena.ui.ridgets.util.IAcceleratorConstants;

/**
 * action adapter for menus
 * 
 * @author Juergen Becker
 * @author Carsten Drossel
 */
public interface IMenuActionAdapter extends IActionAdapter, IMenuItem, IAcceleratorConstants {

	/**
	 * Sets the key combination which serves as an accelerator for the menu
	 * item.<br>
	 * <p>
	 * The modifiers consist of any combination of:
	 * <ul>
	 * <li>SHIFT_MASK</li>
	 * <li>CTRL_MASK</li>
	 * <li>META_MASK</li>
	 * <li>ALT_MASK</li>
	 * </ul>
	 * <p>
	 * Valid characters are '0'-'9', 'A'-'Z' and the KEY_... constants defined
	 * by <code>IAcceleratorConstants</code>.
	 * 
	 * @param keyChar
	 *            - the character value for a keyboard key
	 * @param modifiers
	 *            - a bitwise-ored combination of any modifiers
	 */
	void setAccelerator(char keyChar, int modifiers);

	/**
	 * Returns the character value for a keyboard key that is used for the
	 * accelerator
	 * 
	 * @return key character
	 */
	char getAcceleratorKeyChar();

	/**
	 * Returns the modifiers that are used for the accelerator
	 * 
	 * @return modifiers
	 */
	int getAcceleratorModifiers();
}
