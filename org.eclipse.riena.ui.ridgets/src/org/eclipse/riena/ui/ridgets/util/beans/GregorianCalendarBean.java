/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2005 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.util.beans;

import java.util.GregorianCalendar;

/**
 * GregorianCalendarBean provides a GregorianCalendar value for simple adapter UI-Binding
 *
 * @author Erich Achilles
 */
public class GregorianCalendarBean extends AbstractBean {

	private GregorianCalendar value;

	/**
	 * Creates a GregorianCalendar bean with a default null
	 * @param value
	 */
	public GregorianCalendarBean() {
		value = null;
	}

	/**
	 * Creates a GregorianCalendar bean with the given value
	 * @param value
	 */
	public GregorianCalendarBean( GregorianCalendar value ) {
		this.value = value;
	}

	/**
	 * Sets the value of this bean
	 *
	 * @param value - value to set
	 */
	public void setValue( GregorianCalendar value ) {
		if ( value != null ) {
			//System.out.println( "Greg bean setValue: " + value.toString() );
		} else {
			//System.out.println( "Greg bean setValue: " + null );

		}
		Object old = this.value;
		this.value = value;
		firePropertyChanged( "value", old, this.value );
	}

	/**
	 * Returns the value of this bean
	 *
	 * @return value
	 */
	public GregorianCalendar getValue() {
		//System.out.println( "Greg bean getValue: " + value.toString() );
		return value;
	}
}
