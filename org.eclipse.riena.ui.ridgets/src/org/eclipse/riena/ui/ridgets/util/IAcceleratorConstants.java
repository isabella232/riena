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
package org.eclipse.riena.ui.ridgets.util;

/**
 * Define key constants for accelerators.
 */
public interface IAcceleratorConstants {

	/*
	 * Key constants to define accelerators using keys other than 0-9, A-Z and
	 * a-z. When adding new constants, avoid: 0x00 = char default value
	 * 0x30-0x39 = '0'-'9' 0x41-0x5A = 'A'-'Z' 0x61-0x7A = 'a'-'z'
	 */
	/** Key PageUp */
	char KEY_PAGE_UP = (char) 0x01;
	/** Key PageDown */
	char KEY_PAGE_DOWN = (char) 0x02;
	/** Key Home aka Pos1 */
	char KEY_HOME = (char) 0x03;
	/** Key End */
	char KEY_END = (char) 0x04;
	/** Key Up */
	char KEY_UP = (char) 0x05;
	/** Key Down */
	char KEY_DOWN = (char) 0x06;
	/** Key Left */
	char KEY_LEFT = (char) 0x07;
	/** Key Right */
	char KEY_RIGHT = (char) 0x08;
	/** Key Backspace */
	char KEY_BACK_SPACE = (char) 0x09;
	/** Key F1 */
	char KEY_F1 = (char) 0x0a;
	/** Key F2 */
	char KEY_F2 = (char) 0x0b;
	/** Key F3 */
	char KEY_F3 = (char) 0x0c;
	/** Key F4 */
	char KEY_F4 = (char) 0x0d;
	/** Key F5 */
	char KEY_F5 = (char) 0x0e;
	/** Key F6 */
	char KEY_F6 = (char) 0x0f;
	/** Key F7 */
	char KEY_F7 = (char) 0x10;
	/** Key F8 */
	char KEY_F8 = (char) 0x11;
	/** Key F9 */
	char KEY_F9 = (char) 0x12;
	/** Key F10 */
	char KEY_F10 = (char) 0x13;
	/** Key F11 */
	char KEY_F11 = (char) 0x14;
	/** Key F12 */
	char KEY_F12 = (char) 0x15;
	/** Key Escape */
	char KEY_ESCAPE = (char) 0x16;
	/** Key Delete */
	char KEY_DELETE = (char) 0x17;
	/** Key Tab */
	char KEY_TAB = (char) 0x18;
	/** Key Enter */
	char KEY_ENTER = (char) 0x19;

	/**
	 * <code>PROPERTY_ACCELERATOR</code>
	 */
	String PROPERTY_ACCELERATOR = "accelerator"; //$NON-NLS-1$

	/**
	 * This flag indicates that the Shift key was down when the event occurred.
	 */
	int SHIFT_MASK = 1 << 0;
	/**
	 * This flag indicates that the Control key was down when the event
	 * occurred.
	 */
	int CTRL_MASK = 1 << 1;
	/**
	 * This flag indicates that the Meta key was down when the event occurred.
	 */
	int META_MASK = 1 << 2;
	/**
	 * This flag indicates that the Alt key was down when the event occurred.
	 */
	int ALT_MASK = 1 << 3;
}
