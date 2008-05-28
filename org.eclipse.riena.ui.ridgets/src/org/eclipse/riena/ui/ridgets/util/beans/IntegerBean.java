/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2005 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.util.beans;

/**
 * Integer bean provides a boolean value for simple adapter UI-Binding
 *
 * @author Alexander Ziegler
 */
public class IntegerBean extends AbstractBean {

	private Integer value;

	/**
	 * Creates an integer bean with a default 0
	 * @param value
	 */
	public IntegerBean() {
		value = 0;
	}

	/**
	 * Creates a integer bean with the given value
	 * @param value
	 */
	public IntegerBean( Integer value ) {
		this.value = value;
	}

	/**
	 * Creates a integer bean with the given value
	 * @param value
	 */
	public IntegerBean( int value ) {
		this.value = value;
	}

	/**
	 * Sets the value of this bean
	 *
	 * @param value - value to set
	 */
	public void setValue( Integer value ) {
		Object old = this.value;
		this.value = value;
		firePropertyChanged( "value", old, this.value );

	}

	/**
	 * Returns the value of this bean
	 *
	 * @return value
	 */
	public Integer getValue() {
		return this.value;
	}
}