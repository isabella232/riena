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
package org.eclipse.riena.beans.common;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * base class for beans
 */
public abstract class AbstractBean {
	protected Set<PropertyChangeListener> propertyChangeListeners;

	/**
	 * constructor.
	 */
	public AbstractBean() {
		propertyChangeListeners = new HashSet<PropertyChangeListener>();
	}

	/**
	 * @param listener
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeListeners.add(listener);
	}

	/**
	 * @param listener
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeListeners.remove(listener);
	}

	protected void removeAllPropertyChangeListeners() {
		propertyChangeListeners.clear();
	}

	protected void firePropertyChanged(String aProperty, Object oldValue, Object newValue) {
		firePropertyChanged(new PropertyChangeEvent(this, aProperty, oldValue, newValue));
	}

	protected void firePropertyChanged(PropertyChangeEvent event) {
		// prepare for comodification
		List<PropertyChangeListener> tmpListeners = new ArrayList<PropertyChangeListener>(propertyChangeListeners);
		for (PropertyChangeListener listener : tmpListeners) {
			listener.propertyChange(event);
		}

	}

	/**
	 * @return Anwers <code>true</code> if a listener exists otherwise
	 *         <code>false</code>
	 */
	protected boolean hasListener() {
		return (propertyChangeListeners.size() > 0);
	}
}
