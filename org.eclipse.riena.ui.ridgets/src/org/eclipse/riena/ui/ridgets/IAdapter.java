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

import java.beans.PropertyChangeListener;

import org.eclipse.riena.ui.ridgets.obsolete.IFocusAware;
import org.eclipse.riena.ui.ridgets.obsolete.IUIRepresentation;

/**
 * an adapter is a gui independent representation of a ui control.
 * 
 * @author Frank Schepp
 */
/**
 * @deprecated Use org.eclipse.riena.ui.ridgets.IRidget
 */
@Deprecated
public interface IAdapter extends IUIRepresentation {

	/**
	 * <code>PROPERTY_VISIBLE</code>
	 */
	String PROPERTY_VISIBLE = "visible"; //$NON-NLS-1$

	/**
	 * <code>PROPERTY_TOOLTIP</code>
	 */
	String PROPERTY_TOOLTIP = "tooltip"; //$NON-NLS-1$

	/**
	 * <code>PROPERTY_HELP_ID</code>
	 */
	String PROPERTY_HELP_ID = "helpid"; //$NON-NLS-1$

	/** <code>PROPERTY_FOCUSABLE</code> */
	String PROPERTY_FOCUSABLE = "focusable"; //$NON-NLS-1$

	/**
	 * <code>PROPERTY_REQUEST_FOCUS</code>
	 */
	String PROPERTY_REQUEST_FOCUS = "requestFocus"; //$NON-NLS-1$

	/**
	 * <code>PROPERTY_REQUEST_FOCUS_IN_WINDOW</code>
	 */
	String PROPERTY_REQUEST_FOCUS_IN_WINDOW = "requestFocusInWindow"; //$NON-NLS-1$

	/**
	 * <code>PROPERTY_BLOCKED</code>
	 */
	String PROPERTY_BLOCKED = "blocked"; //$NON-NLS-1$

	/**
	 * the id is used to connect(/bind) ui controls to adapters.
	 * 
	 * @return the id of this adapter
	 */
	String getAdapterID();

	/**
	 * Sets the ID to used to connect UI control to adapter
	 * 
	 * @param adapterId
	 *            - the ID to set
	 */
	void setAdapterID(String adapterId);

	/**
	 * Binds a new value provider to the adapter
	 * 
	 * @param provider
	 *            the value provider to bind
	 */
	void bindValue(IValueProvider provider);

	/**
	 * get changes from bean and fire change events.
	 */
	void update();

	/**
	 * @param listener
	 */
	void addPropertyChangeListener(PropertyChangeListener listener);

	/**
	 * @param listener
	 */
	void removePropertyChangeListener(PropertyChangeListener listener);

	/**
	 * @return Answers <code>true</code> if the Adapter is bound, otherwise
	 *         <code>false</code>
	 */
	boolean isBound();

	/**
	 * @return true if the adapter can gain the focus. default true
	 */
	boolean isFocusable();

	/**
	 * @param focusable
	 *            true if the adapter can gain the focus
	 */
	void setFocusable(boolean focusable);

	/**
	 * Indicates the Adapter that it gained focus. This method is intended to be
	 * called by the associated UIAdapter.
	 * 
	 * @param oldFocusOwner
	 *            the old focus owner.
	 */
	void focusGained(IFocusAware oldFocusOwner);

	/**
	 * Indicates the Adapter that it lost focus. This method is intended to be
	 * called by the associated UIAdapter.
	 * 
	 * @param newFocusOwner
	 *            the new focus owner.
	 */
	void focusLost(IFocusAware newFocusOwner);

	/**
	 * request focus for the ui control associated to the adapter. precondition
	 * is, that the ui control, which should recieve the focus is visiable at
	 * request time.
	 */
	void requestFocus();

	/**
	 * request focus for the ui control associated to the adapter, if this
	 * control's top-level ancestor is already the focused Window. precondition
	 * is, that the ui control, which should recieve the focus is visiable at
	 * request time.
	 */
	void requestFocusInWindow();

	/**
	 * Returns the tooltip string
	 * 
	 * @return the text of the tool tip
	 */
	String getToolTipText();

	/**
	 * Registers the text to display in a tool tip. The text displays when the
	 * cursor lingers over the component.
	 * 
	 * @param text
	 *            - the string to display; if the text is null, the tool tip is
	 *            turned off for this component
	 */
	void setToolTipText(String text);

	/**
	 * Returns the help ID.
	 * 
	 * @return the help ID
	 */
	String getHelpID();

	/**
	 * Registers help ID.
	 * 
	 * @param helpID
	 *            the help ID
	 */
	void setHelpID(String helpID);

	/**
	 * Returns the permission ID.
	 * 
	 * @return the permission ID
	 */
	String getPermissionID();

	/**
	 * Registers permission ID.
	 * 
	 * @param permissionID
	 *            the permission ID
	 */
	void setPermissionID(String permissionID);

}
