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

import org.eclipse.riena.ui.ridgets.util.IAcceleratorConstants;

/**
 * window adapter for all kinds of windows, dialogs, etc. 2005-10-17 Sebastian
 * Stanossek moved to AbstractMarkableAdapter
 * 
 * @author Juergen Becker
 * @author Carsten Drossel
 */
/**
 * @deprecated
 */
@Deprecated
public interface IWindowAdapter extends IMarkableAdapter, IAcceleratorConstants {

	/**
	 * <code>TITLE_PROPERTY</code>
	 */
	String PROPERTY_TITLE = "title";
	/**
	 * <code>PROPERTY_PACK</code>
	 */
	String PROPERTY_PACK = "pack";
	/**
	 * <code>PROPERTY_TO_FRONT</code>
	 */
	String PROPERTY_TO_FRONT = "toFront";
	/**
	 * <code>PROPERTY_LOCATE_RELATIVE_TO_SCREEN</code>
	 */
	String PROPERTY_LOCATE_RELATIVE_TO_SCREEN = "locationRelativeToScreen";
	/**
	 * <code>PROPERTY_LOCATE_RELATIVE_TO_DESKTOP</code>
	 */
	String PROPERTY_LOCATE_RELATIVE_TO_DESKTOP = "locationRelativeToDesktop";
	/**
	 * <code>PROPERTY_DISPOSE</code>
	 */
	String PROPERTY_DISPOSE = "dispose";
	/**
	 * <code>PROPERTY_ICON</code>
	 */
	String PROPERTY_ICON = "icon";
	/**
	 * <code>PROPERTY_ICONIFIED</code>
	 */
	String PROPERTY_ICONIFIED = "iconified";

	/**
	 * get the title of the window.
	 * 
	 * @return the title string.
	 */
	String getTitle();

	/**
	 * set the title of the window.
	 * 
	 * @param title
	 *            the title string.
	 * @pre title != null
	 */
	void setTitle(String title);

	/**
	 * get the icon.
	 * 
	 * @return Returns the icon name.
	 */
	String getIcon();

	/**
	 * set the icon.
	 * 
	 * @param icon
	 *            The icon name.
	 */
	void setIcon(String icon);

	/**
	 * repack the window.
	 */
	void pack();

	/**
	 * Brings this window to front ( or to back if called with false )
	 * 
	 * @param front
	 *            specifies if window should come to front ( or to back )
	 */
	void toFront(boolean front);

	/**
	 * Sets the location of the window relative to the screen. The window is
	 * centered on the screen.
	 */
	void locationRelativeToScreen();

	/**
	 * Sets the location of the window relative to the desktop
	 */
	void locationRelativeToDesktop();

	/**
	 * Releases all of the native screen resources used by this window.
	 */
	void dispose();

	/**
	 * adds a single window listener.
	 * 
	 * @param listener
	 */
	void addWindowListener(IWindowListener listener);

	/**
	 * remove a registered window listener.
	 * 
	 * @param listener
	 */
	void removeWindowListener(IWindowListener listener);

	/**
	 * Sets the ID of the default component to focus. This is the component,
	 * that will get the focus after the dialog.
	 * 
	 * @param id
	 *            - the id of the default component to focus.
	 */
	void setDefaultFocusComponentId(String id);

	/**
	 * Returns the ID of the default component to focus.
	 * 
	 * @return - the default component to focus.
	 */
	String getDefaultFocusComponentId();

	/**
	 * Add the key combination which serves as an accelerator for the callback.<br>
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
	 *            the character value for a keyboard key.
	 * @param modifiers
	 *            a bitwise-ored combination of any modifiers.
	 * @param callback
	 *            the callback to call for this accelerator.
	 */
	void addAcceleratorCallback(char keyChar, int modifiers, IActionCallback callback);

	/**
	 * Remove the key combination which serves as an accelerator for some
	 * callback.<br>
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
	 *            the character value for a keyboard key.
	 * @param modifiers
	 *            a bitwise-ored combination of any modifiers.
	 */
	void removeAcceleratorCallback(char keyChar, int modifiers);

	/**
	 * Returns the window closing policy.
	 * 
	 * @return The window closing policy.
	 */
	IWindowClosingPolicy getWindowClosingPolicy();

	/**
	 * Sets a callback that may prevent the closing of the window.
	 * 
	 * @param windowClosingPolicy
	 *            The new window closing policy.
	 */
	void setWindowClosingPolicy(IWindowClosingPolicy windowClosingPolicy);

}