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

import org.eclipse.riena.ui.ridgets.listener.IFocusListener;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;

/**
 * A UI-toolkit independent Ridget (Riena widget) wrapping and extending an
 * UI-control / widget. Offers an API to modify a UI-control that is not
 * specific for the UI-toolkit to which the UI-control belongs (e.g. SWT or
 * Swing).
 */
public interface IRidget {

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
	 * PropertyChangeEvents the Ridget allows listening for selected properties
	 * of the UI-control in a UI-toolkit independent way e.g. the property
	 * "text" of a text field. The same listener object may be added more than
	 * once, and will be called as many times as it is added. If the argument is
	 * null, no exception is thrown and no action is taken.
	 * 
	 * @param propertyChangeListener
	 *            The PropertyChangeListener to be added.
	 */
	void addPropertyChangeListener(PropertyChangeListener propertyChangeListener);

	/**
	 * Adds a PropertyChangeListener for a specific property of the Ridget.
	 * Through PropertyChangeEvents the Ridget allows listening for selected
	 * properties of the UI-control in a UI-toolkit independent way e.g. the
	 * property "text" of a text field. The same listener object may be added
	 * more than once, and will be called as many times as it is added. If any
	 * argument is null, no exception is thrown and no action is taken.
	 * 
	 * @param propertyName
	 *            The name of the property to listen on.
	 * @param propertyChangeListener
	 *            The PropertyChangeListener to be added.
	 */
	void addPropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener);

	/**
	 * Removes a PropertyChangeListener for all properties of the Ridget. If the
	 * listener was added more than once to the same event source, it will be
	 * notified one less time after being removed. If the listener is null, or
	 * was never added, no exception is thrown and no action is taken.
	 * 
	 * @param propertyChangeListener
	 *            The PropertyChangeListener to be removed.
	 */
	void removePropertyChangeListener(PropertyChangeListener propertyChangeListener);

	/**
	 * Removes a PropertyChangeListener for a specific property of the Ridget.
	 * If the listener was added more than once to the same event source, it
	 * will be notified one less time after being removed. If any argument is
	 * null, or the listener was never added, no exception is thrown and no
	 * action is taken.
	 * 
	 * @param propertyName
	 *            The name of the property to listen on.
	 * @param propertyChangeListener
	 *            The PropertyChangeListener to be removed.
	 */
	void removePropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener);

	/**
	 * Adds the specified focus listener to receive focus events from this
	 * ridget when it gains the focus.
	 * 
	 * @param listener
	 *            - focus listener to be added
	 */
	void addFocusListener(IFocusListener listener);

	/**
	 * Removes the specified focus listener so that it no longer receives focus
	 * events from this component.
	 * 
	 * @param listener
	 *            - focus listener to be removed
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
	 * For an example binding ITableRidget see
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
	 * Blocks of unblocks the user input for the ridget.
	 * 
	 * @param blocked
	 */
	void setBlocked(boolean blocked);

	/**
	 * Returns true if user input for the ridgetis blocked.
	 * 
	 * @return true if input is blocked for the ridget
	 */
	boolean isBlocked();

}
