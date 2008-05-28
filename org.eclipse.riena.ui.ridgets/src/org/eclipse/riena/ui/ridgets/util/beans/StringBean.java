/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2005 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.util.beans;

/**
 * String bean provides a boolean value for simple adapter UI-Binding
 *
 * @author Alexander Ziegler
 */
public class StringBean extends AbstractBean {

	private String value;

	/**
	 * Creates an String bean with a default 0
	 * @param value
	 */
	public StringBean() {
		value = "";
	}

	/**
	 * Creates a String bean with the given value
	 * @param value
	 */
	public StringBean( String value ) {
		this.value = value;
	}

	/**
	 * Sets the value of this bean
	 *
	 * @param value to set
	 */
	public void setValue( String value ) {
		Object old = this.value;
		this.value = value;
		firePropertyChanged( "value", old, this.value );
	}

	/**
	 * Returns the value of this bean
	 *
	 * @return value
	 */
	public String getValue() {
		return value;
	}
}
