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

import org.eclipse.riena.ui.core.util.IPositionable;

/**
 * An adapter for UI controls that allow an action to be performed on them, like
 * clicking a button or checking a checkbox.
 * 
 * @author Juergen Becker
 * @author Carsten Drossel
 */

/**
 * @deprecated Use org.eclipse.riena.ui.internal.ridgets.IActionListener
 */
@Deprecated
public interface IActionAdapter extends IMarkableAdapter, IPositionable {

	/**
	 * <code>PROPERTY_TEXT</code>
	 */
	String PROPERTY_TEXT = "text"; //$NON-NLS-1$
	/**
	 * <code>PROPERTY_ICON</code>
	 */
	String PROPERTY_ICON = "icon"; //$NON-NLS-1$
	/**
	 * <code>PROPERTY_POSITION</code>
	 */
	String PROPERTY_POSITION = "position"; //$NON-NLS-1$
	/**
	 * <code>PROPERTY_MNEMONIC_INDEX</code>
	 */
	String PROPERTY_MNEMONIC_INDEX = "mnemonicIndex"; //$NON-NLS-1$

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
	 * @param newIndex
	 *            - index into the String to underline; -1: to remove mnemonic
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
