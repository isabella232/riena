/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2005 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.util;

/**
 * Interface for accelerators.
 *
 * @author Frank Schepp
 */
public interface IAccelerator {

	/**
	 * @return keyChar the character value for a keyboard key.
	 */
	char getKeyChar();

	/**
	 * @return modifiers a bitwise-ored combination of any modifiers.
	 */
	int getModifiers();
}