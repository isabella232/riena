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

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.riena.ui.ridgets.listener.IFocusListener;

/**
 * A UI-toolkit independent Ridget (Riena widget) wrapping and extending an
 * UI-control / widget. Offers an API to modify a UI-control that is not
 * specific for the UI-toolkit to which the UI-control belongs (e.g. SWT or
 * Swing).
 */
public interface IRidget {

	String PROPERTY_BLOCKED = "blocked"; //$NON-NLS-1$

	/**
	 * @return Indicates whether the UI-control is visible.
	 */
	boolean isVisible();

	/**
	 * Sets whether the UI-control is visible.
	 * 
	 * @param visible
	 *            The new visibility state.
	 */
	void setVisible(boolean visible);

	/**
	 * Getter to access the UI-toolkit specific UI-control. Allows modifications
	 * of the UI-control that are not supported by the Ridgets API.
	 * 
	 * @return The bound UI-control or null, if no control is bound.
	 */
	Object getUIControl();

	/**
	 * Setter to be internally used by the view to bind and unbind the Ridgets.
	 * 
	 * @param uiControl
	 *            The wrapped UI-control to which the Ridget is bound or null to
	 *            unbind the Ridget.
	 */
	void setUIControl(Object uiControl);

	/**
	 * Adds a PropertyChangeListener for all properties of the Ridget. Through
	 * PropertyChangeEvents the Ridget allows listening for property changes of
	 * the UI-control in a UI-toolkit independent way.
	 * <p>
	 * Adding the same listener several times has no effect.
	 * 
	 * @param propertyChangeListener
	 *            The PropertyChangeListener to be added (non-null)
	 * @throws RuntimeException
	 *             if propertyChangeListener is null
	 */
	void addPropertyChangeListener(PropertyChangeListener propertyChangeListener);

	/**
	 * Adds a PropertyChangeListener for a specific property of the Ridget.
	 * Through PropertyChangeEvents the Ridget allows listening for property
	 * changes of specific properties of the UI-control in a UI-toolkit
	 * independent way, e.g. the property "text" of a text field.
	 * <p>
	 * Adding the same listener several times has no effect.
	 * 
	 * @param propertyName
	 *            The name of the property to listen on (may be null to listen
	 *            to all properties)
	 * @param propertyChangeListener
	 *            The PropertyChangeListener to be added (non null)
	 * @throws RuntimeException
	 *             if propertyChangeListener is null
	 */
	void addPropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener);

	/**
	 * Removes a PropertyChangeListener for all properties of the Ridget.
	 * 
	 * @param propertyChangeListener
	 *            The PropertyChangeListener to be removed (non null)
	 * @throws RuntimeException
	 *             if propertyChangeListener is null
	 */
	void removePropertyChangeListener(PropertyChangeListener propertyChangeListener);

	/**
	 * Removes a PropertyChangeListener for a specific property of the Ridget.
	 * 
	 * @param propertyName
	 *            The name of the property to listen on (may be null to remove
	 *            the listener from all properties)
	 * @param propertyChangeListener
	 *            The PropertyChangeListener to be removed (non null)
	 * @throws RuntimeException
	 *             if propertyChangeListener is null
	 */
	void removePropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener);

	/**
	 * Adds a {@link IFocusListener} for receiving focus events from this
	 * ridget.
	 * <p>
	 * Adding the same listener several times has to effect.
	 * 
	 * @param listener
	 *            the listener to be added (non-null)
	 * @throws RuntimeException
	 *             if listener is null
	 */
	void addFocusListener(IFocusListener listener);

	/**
	 * Removes the specified focus listener so that it no longer receives focus
	 * events from this ridget.
	 * 
	 * @param listener
	 *            the focus listener to be removed
	 * @throws RuntimeException
	 *             if listener is null
	 */
	void removeFocusListener(IFocusListener listener);

	/**
	 * Requests that this ridget get the input focus.<br>
	 * Precondition is, that the ridget, which should receive the focus is
	 * visible.
	 */
	void requestFocus();

	/**
	 * Returns if this ridget is the focus owner.
	 * 
	 * @return <code>true</code> if this ridget is the focus owner;
	 *         <code>false</code> otherwise
	 */
	boolean hasFocus();

	/**
	 * For value based ridgets triggers an update from the model value to the
	 * Ridget value when a default binding is used.
	 * 
	 * <p>
	 * For an example binding IValueRidget see:
	 * </p>
	 * <ul>
	 * <li>{@link #bindToModel(IObservableValue)}</li>
	 * <li>{@link #bindToModel(Object, String)}</li>
	 * </ul>
	 * 
	 * <p>
	 * For an example binding ITableRidget see:
	 * </p>
	 * <ul>
	 * <li>{@link #bindToModel(IObservableList, Class, String[], String[])}</li>
	 * <li>{@link #bindToModel(Object, String, Class, String[], String[])}</li>
	 * </ul>
	 */
	void updateFromModel();

	/**
	 * Returns if the ridget can gain the focus or not.
	 * 
	 * @return true if the adapter can gain the focus; otherwise false
	 */
	boolean isFocusable();

	/**
	 * Sets if the ridget can gain the focus or not.
	 * 
	 * @param focusable
	 *            - true if the adapter can gain the focus; otherwise false
	 */
	void setFocusable(boolean focusable);

	/**
	 * @return the toolTipText for the ridget.
	 */
	String getToolTipText();

	/**
	 * @param toolTipText
	 *            The toolTipText to set. May be null to turn off the tooltip.
	 */
	void setToolTipText(String toolTipText);

	/**
	 * Blocks or unblocks the user input for the ridget.
	 * 
	 * @param blocked
	 *            true if input is blocked for the ridget, else false.
	 */
	void setBlocked(boolean blocked);

	/**
	 * Returns, if user input for the ridget is blocked.
	 * 
	 * @return true if input is blocked for the ridget, else false.
	 */
	boolean isBlocked();

	/**
	 * Returns the ID of the ridget.
	 * 
	 * @return ID
	 */
	String getID();
}
