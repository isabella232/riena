/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2005 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.util.beans;

/**
 * Double bean provides a boolean value for simple adapter UI-Binding.
 *
 * @author Alexander Ziegler
 */
public class DoubleBean extends AbstractBean {

	private Double value;

	/**
	 * Creates a double bean with a default 0.0
	 * @param value
	 */
	public DoubleBean() {
		value = null;
	}

	/**
	 * Creates a double bean with the given value
	 * @param value
	 */
	public DoubleBean( Double value ) {
		this.value = value;
	}

	/**
	 * Creates a double bean with the given value
	 * @param value
	 */
	public DoubleBean( double value ) {
		this.value = value;
	}

	/**
	 * Sets the value of this bean
	 *
	 * @param value - value to set
	 */
	public void setValue( Double value ) {
		//System.out.println( "Double bean setValue: " + value.toString() );
		Object old = this.value;
		this.value = value;
		firePropertyChanged( "value", old, this.value );
	}

	/**
	 * Returns the value of this bean
	 *
	 * @return value
	 */
	public Double getValue() {
		//System.out.println( "Double bean getValue: " + value.toString() );
		return value;
	}
}
