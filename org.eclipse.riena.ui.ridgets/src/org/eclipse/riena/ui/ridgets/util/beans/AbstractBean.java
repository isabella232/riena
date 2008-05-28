/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.util.beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * base class for beans
 *
 * @author Juergen Becker
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
	public void addPropertyChangeListener( PropertyChangeListener listener ) {
		propertyChangeListeners.add( listener );
	}

	/**
	 * @param listener
	 */
	public void removePropertyChangeListener( PropertyChangeListener listener ) {
		propertyChangeListeners.remove( listener );
	}

	protected void removeAllPropertyChangeListeners() {
		propertyChangeListeners.clear();
	}

	protected void firePropertyChanged( String aProperty, Object oldValue, Object newValue ) {
		firePropertyChanged( new PropertyChangeEvent( this, aProperty, oldValue, newValue ) );
	}

	protected void firePropertyChanged( PropertyChangeEvent event ) {
		// prepare for comodification
		List<PropertyChangeListener> tmpListeners = new ArrayList<PropertyChangeListener>( propertyChangeListeners );
		for ( PropertyChangeListener listener : tmpListeners ) {
			listener.propertyChange( event );
		}

	}

	/**
	 * @return Anwers <code>true</code> if a listener exists otherwise <code>false</code>
	 */
	protected boolean hasListener() {
		return ( propertyChangeListeners.size() > 0 );
	}
}