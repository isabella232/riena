/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2005 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets;

/**
 * The state of the mouse cursor.
 *
 * @author Carsten Drossel
 */
public interface ICursor {

	/**
	 * No mouse cursor. An adapter or controller inherits the cursor of its
	 * parent controller.
	 */
	int CURSOR_INHERIT = 1;

	/**
	 * Normal mouse cursor.
	 */
	int CURSOR_NORMAL = 2;

	/**
	 * Wait mouse cursor. Indicates that the part of the UI is temporarily
	 * blocked.
	 */
	int CURSOR_WAIT = 4;

	/**
	 * Text cursor. Indicates the opportunity to enter text.
	 */
	int CURSOR_TEXT = 8;

	/**
	 * Hand cursor. Indicates the opportunity to move something.
	 */
	int CURSOR_HAND = 16;

	/**
	 * Background activity cursor. Indicates that some background activity
	 * is going on but that the UI is still responsive.
	 */
	int CURSOR_BACKGROUND_ACTIVITY = 32;

	/**
	 * Drag cursor for copy, when drop on target is possible.
	 */
	int CURSOR_COPY_DROP = 64;

	/**
	 * Drag cursor for copy, when no drop on target is possible.
	 */
	int CURSOR_COPY_NO_DROP = 128;

	/**
	 * Drag cursor for move, when drop on target is possible.
	 */
	int CURSOR_MOVE_DROP = 256;

	/**
	 * Drag cursor for move, when no drop on target is possible.
	 */
	int CURSOR_MOVE_NO_DROP = 512;

	/**
	 * Drag cursor for a link operation, when drop on target is possible.
	 */
	int CURSOR_LINK_DROP = 1024;

	/**
	 * Drag cursor for a link operation, when no drop on target is possible.
	 */
	int CURSOR_LINK_NO_DROP = 2048;
}
