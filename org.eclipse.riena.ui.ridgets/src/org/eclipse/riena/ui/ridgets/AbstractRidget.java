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
import java.beans.PropertyChangeSupport;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.riena.ui.ridgets.listener.FocusEvent;
import org.eclipse.riena.ui.ridgets.listener.IFocusListener;

/**
 * Superclass for ridgets with property change support.
 */
public abstract class AbstractRidget implements IRidget {

	public final static String PROPERTY_RIDGET = "ridget"; //$NON-NLS-1$

	protected PropertyChangeSupport propertyChangeSupport;
	private Set<IFocusListener> focusListeners;

	/**
	 * Constructor.
	 */
	public AbstractRidget() {
		propertyChangeSupport = new PropertyChangeSupport(this);
		focusListeners = new HashSet<IFocusListener>(1, 1.0f);
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidget#addPropertyChangeListener(java.beans
	 *      .PropertyChangeListener)
	 */
	public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidget#addPropertyChangeListener(java.lang
	 *      .String, java.beans.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, propertyChangeListener);
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidget#removePropertyChangeListener(java
	 *      .beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		propertyChangeSupport.removePropertyChangeListener(propertyChangeListener);
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidget#removePropertyChangeListener(java
	 *      .lang.String, java.beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener) {
		propertyChangeSupport.removePropertyChangeListener(propertyName, propertyChangeListener);
	}

	/**
	 * Notifies all listeners about a changed property. No event is fired if old
	 * and new are equal and non-null.
	 * 
	 * @param propertyName
	 *            The name of the property that was changed.
	 * @param oldValue
	 *            The old value of the property.
	 * @param newValue
	 *            The new value of the property.
	 */
	protected void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	/**
	 * Notifies all listeners about a changed property. No event is fired if old
	 * and new are equal and non-null.
	 * 
	 * @param propertyName
	 *            The name of the property that was changed.
	 * @param oldValue
	 *            The old value of the property.
	 * @param newValue
	 *            The new value of the property.
	 */
	protected void firePropertyChange(String propertyName, int oldValue, int newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	/**
	 * Notifies all listeners about a changed property. No event is fired if old
	 * and new are equal and non-null.
	 * 
	 * @param propertyName
	 *            The name of the property that was changed.
	 * @param oldValue
	 *            The old value of the property.
	 * @param newValue
	 *            The new value of the property.
	 */
	protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidget#addFocusListener(org.eclipse.riena
	 *      .ui.ridgets.listener.IFocusListener)
	 */
	public void addFocusListener(IFocusListener listener) {
		focusListeners.add(listener);
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidget#removeFocusListener(org.eclipse.
	 *      riena.ui.ridgets.listener.IFocusListener)
	 */
	public void removeFocusListener(IFocusListener listener) {
		focusListeners.remove(listener);
	}

	/**
	 * Notifies all listeners that the ridget has lost the focus.
	 * 
	 * @param event
	 *            - the FocusEvent
	 */
	protected void fireFocusLost(FocusEvent event) {
		for (IFocusListener focusListener : focusListeners) {
			focusListener.focusLost(event);
		}
	}

	/**
	 * Notifies all listeners that the ridget has gained the focus.
	 * 
	 * @param event
	 *            - the FocusEvent
	 */
	protected void fireFocusGained(FocusEvent event) {
		for (IFocusListener focusListener : focusListeners) {
			focusListener.focusGained(event);
		}
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidget#updateFromModel()
	 */
	public void updateFromModel() {
		// Do nothing by default
	}
}
