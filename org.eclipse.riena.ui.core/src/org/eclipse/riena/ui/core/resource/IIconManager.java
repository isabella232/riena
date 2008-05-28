/****************************************************************
 *                                                              *
 * Copyright (c) 2005 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/

package org.eclipse.riena.ui.core.resource;

import org.eclipse.riena.ui.core.resource.internal.IconSize;
import org.eclipse.riena.ui.core.resource.internal.IconState;

/**
 * Describes how the icons of the application are maintained. Icons are
 * identified by a <code>String</code>, an icon ID that should be created
 * using the icon manager. The information required to get the actual icon file
 * is passed to the icon manager when creating the icon ID and will be stored
 * for later retrieval.
 * 
 * The <code>String</code> ID is used throughout the application rather than
 * an icon object for downward compatibility reasons.
 * 
 * @author Carsten Drossel
 */
public interface IIconManager {

	/** Icon FAVORITES. */
	String FAVORITES = "0001";

	/** Icon ERROR. */
	String ERROR = "0002";

	/** Icon INFORMATION. */
	String INFORMATION = "0003";

	/** Icon HELP. */
	String HELP = "0004";

	/** Icon WARNING. */
	String WARNING = "0005";

	/** Icon BROWSER. */
	String BROWSER = "0007";

	/** Icon CLOSE_MODULE. */
	String MODULE_CLOSE = "0008";

	/** Icon COMMUNICATION. */
	String COMMUNICATION = "0009";

	/** Icon SPACER. */
	String SPACER = "0010";

	/** Icon TREE_DOCUMENT_LEAF. */
	String TREE_DOCUMENT_LEAF = "0013";

	/** Icon LOGIN. */
	String LOGIN = "0014";

	/** Icon SAVE. */
	String SAVE = "0018";

	/** Icon TREE_FOLDER_CLOSED. */
	String TREE_FOLDER_CLOSED = "0022";

	/** Icon TREE_FOLDER_OPEN. */
	String TREE_FOLDER_OPEN = "0023";

	/** Icon REFRESH. */
	String REFRESH = "0028";

	/** Icon PRINT. */
	String PRINT = "0030";

	/** Icon GENERAL_MODULE. */
	String GENERAL_MODULE = "0034";

	/** Icon TREE. */
	String TREE = "0035";

	/** Icon SEARCH. */
	String SEARCH = "0042";

	/** Icon DEFAULT_ICON - to be designed. */
	String DEFAULT_ICON = "0044";

	/** Icon FORWARD. */
	String FORWARD = "0045";

	/** Icon BACK. */
	String BACK = "0046";

	/** Icon FIRST. */
	String FIRST = "0047";

	/** Icon LAST. */
	String LAST = "0048";

	/** Markericon MISSING_MANDATORY */
	String MARKER_MISSING_MANDATORY = "0059";

	/** Markericon ERROR */
	String MARKER_ERROR = "0061";

	/** Icon TREE_EXPANSION_KNOB_CLOSED. */
	String TREE_EXPANSION_KNOB_CLOSED = "2068";

	/** Icon TREE_EXPANSION_KNOB_OPEN. */
	String TREE_EXPANSION_KNOB_OPEN = "2069";

	/** Icon TREE_EXPANSION_KNOB_OPEN_LOCKED. */
	String TREE_EXPANSION_KNOB_OPEN_LOCKED = "2067";

	/** Icon CLOSE_WINDOW. */
	String WINDOW_CLOSE = "0070";

	/** Icon WINDOW_MINIMIZE. */
	String WINDOW_MINIMIZE = "0071";

	/** Icon CURSOR_DRAG. */
	String CURSOR_DRAG = "0072";

	/** Icon CURSOR_DRAG_IN_PROGRESS. */
	String CURSOR_DRAG_PRESSED = "0073";

	/** Icon NOTEPAD_FRAME_COLLAPSE. */
	String NOTEPAD_FRAME_COLLAPSE = "0438";

	/** Icon NOTEPAD_FRAME_EXPAND. */
	String NOTEPAD_FRAME_EXPAND = "0441";

	/** Icon NOTEPAD_FRAME_CLOSE. */
	String NOTEPAD_FRAME_CLOSE = "0444";

	/** Icon NOTEPAD_FRAME_RESIZE. */
	String NOTEPAD_FRAME_RESIZE = "0447";

	/** Icon NOTEPAD_FRAME_GRIP. */
	String NOTEPAD_FRAME_GRIP = "0448";

	/** Icon SHADOW_NE. */
	String SHADOW_NE = "0449";

	/** Icon SHADOW_E. */
	String SHADOW_E = "0450";

	/** Icon SHADOW_SE. */
	String SHADOW_SE = "0451";

	/** Icon SHADOW_S. */
	String SHADOW_S = "0452";

	/** Icon SHADOW_SW. */
	String SHADOW_SW = "0453";

	/** Icon SHADOW_W. */
	String SHADOW_W = "0454";

	/** Icon SHADOW_NW. */
	String SHADOW_NW = "0455";

	/** Icon SHADOW_N. */
	String SHADOW_N = "0456";

	/** Cursor icon indicating some background activity */
	String CURSOR_BACKGROUND_ACTIVITY = "2053";

	/** Cursor icon indicating move/drop while dragging */
	String CURSOR_MOVE_DROP = "2256";

	/** Icon incicating the drop down capability of a drop down button */
	String DROP_DOWN_BUTTON_ARROW_DOWN = "2065";

	/**
	 * Icon incicating the drop down capability of a drop down button (with the
	 * popup opening above the button)
	 */
	String DROP_DOWN_BUTTON_ARROW_UP = "2066";

	/** Icon QUESTION. */
	String QUESTION = "9001";

	/** Icon GO. */
	String GO = "10000";

	/** Icon of the check box that is included in a menu. */
	String CHECK_BOX_MENU_ITEM_UNSELECTED = "2080";

	/** Icon of the check box that is included in a menu. */
	String CHECK_BOX_MENU_ITEM_SELECTED = "2081";

	/** Markericon PROCESS_FINISHED */
	String MARKER_PROCESS_FINISHED = "prgsm";

	/**
	 * Stores an icon with the specified name and size and returns an ID to
	 * access the icons data. The icons state is assumed to be 'normal'.
	 * 
	 * @see IconState#NORMAL
	 * 
	 * @param name
	 *            The name of the icon.
	 * @param size
	 *            The icon size.
	 * @return The ID of the icon.
	 */
	String getIconID(String name, IconSize size);

	/**
	 * Stores an icon with the specified name, size and state and returns an ID
	 * to access the icons data.
	 * 
	 * @param name
	 *            The name of the icon.
	 * @param size
	 *            The icon size.
	 * @param state
	 *            The icons state.
	 * @return The ID of the icon.
	 */
	String getIconID(String name, IconSize size, IconState state);

	/**
	 * Returns the name of the icon if the specified ID. The ID must be obtained
	 * from the icon manager.
	 * 
	 * @see #getIconID(String, IconSize)
	 * 
	 * @param iconID
	 *            The ID of the icon.
	 * @return The name of the icon if an icon with the specified ID is stored,
	 *         <code>null</code> otherwise.
	 */
	String getName(String iconID);

	/**
	 * Returns the size of the icon with the specified ID. The ID must be
	 * obtained from the icon manager.
	 * 
	 * @see #getIconID(String, IconSize)
	 * @see IconSize
	 * 
	 * @param iconID
	 *            The ID of the icon.
	 * @return The size of the icon if an icon with the specified ID is stored,
	 *         <code>null</code> otherwise.
	 */
	IconSize getSize(String iconID);

	/**
	 * Returns the state of the icon with the specified ID. The ID must be
	 * obtained from the icon manager.
	 * 
	 * @see #getIconID(String, IconSize)
	 * @see IconState
	 * 
	 * @param iconID
	 *            The ID of the icon.
	 * @return The state of the icon if an icon with the specified ID is stored,
	 *         <code>null</code> otherwise.
	 */
	IconState getState(String iconID);

	/**
	 * Return, if <code>iconID</code> has a file extension or not.
	 * 
	 * @param iconID
	 *            The ID of the icon.
	 * @return true, if <code>iconID</code> has a file extension, else false.
	 */
	boolean hasExtension(String iconID);
}