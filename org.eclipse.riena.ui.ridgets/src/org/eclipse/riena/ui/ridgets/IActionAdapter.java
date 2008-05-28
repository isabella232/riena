/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2005 compeople AG                         *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets;

import org.eclipse.riena.ui.core.util.IPositionable;

/**
 * An adapter for UI controls that allow an action to be performed on them, like
 * clicking a button or checking a checkbox.
 * 
 * @author Juergen Becker
 * @author Carsten Drossel
 */

/**
 * @deprecated Use org.eclipse.riena.ui.ridgets.IActionListener
 */
@Deprecated
public interface IActionAdapter extends IMarkableAdapter, IPositionable {

	/**
	 * <code>PROPERTY_TEXT</code>
	 */
	String PROPERTY_TEXT = "text";
	/**
	 * <code>PROPERTY_ICON</code>
	 */
	String PROPERTY_ICON = "icon";
	/**
	 * <code>PROPERTY_POSITION</code>
	 */
	String PROPERTY_POSITION = "position";
	/**
	 * <code>PROPERTY_MNEMONIC_INDEX</code>
	 */
	String PROPERTY_MNEMONIC_INDEX = "mnemonicIndex";

	/**
	 * Indicates whether a DisabledMarker has been added.
	 * 
	 * @see de.compeople.spirit.core.client.uibinding.adapter.marker.DisabledMarker
	 * 
	 * @return Returns true, is no DisabledMarker is added, false otherwise.
	 */
	boolean isEnabled();

	/**
	 * Depending on the specified enabled state, a special DisabledMarker is
	 * added or removed. Other DisabledMarkers are not affected.
	 * 
	 * @param enabled
	 *            The new enabled state.
	 */
	void setEnabled(boolean enabled);

	/**
	 * @return Returns the label.
	 */
	String getText();

	/**
	 * @param newText
	 *            The label text to set.
	 */
	void setText(String newText);

	/**
	 * @return Returns the icon name.
	 */
	String getIcon();

	/**
	 * @param newIcon
	 *            The name to set.
	 */
	void setIcon(String newIcon);

	/**
	 * Returns keyboard mnemonic.
	 * 
	 * @return index into the String to underline
	 */
	int getMnemonicIndex();

	/**
	 * Sets keyboard mnemonic. <br>
	 * The mnemonic is the key which when combined with the look and feel's
	 * mouseless modifier (usually Alt) will activate this button if focus is
	 * contained somewhere within this button's ancestor window. <br>
	 * 
	 * @param newIndex -
	 *            index into the String to underline; -1: to remove mnemonic
	 */
	void setMnemonicIndex(int newIndex);

	/**
	 * @return Returns the callbackParam.
	 */
	Object getCallbackParam();

	/**
	 * @param callbackParam
	 *            The callbackParam to set.
	 */
	void setCallbackParam(Object callbackParam);

	/**
	 * call this to activate the method an the target bean.
	 */
	void callback();

	/**
	 * programatically cancel the asynchronous action (if running).
	 */
	void cancel();

}