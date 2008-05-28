/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2005 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.util.beans;

/**
 * Boolean bean provides a boolean value for simple adapter UI-Binding
 *
 * @author Alexander Ziegler
 */
public class BooleanBean extends AbstractBean {

	private boolean value;

	/**
	 * Creates a boolean bean with default value <code>false</code>
	 */
	public BooleanBean() {
		value = false;
	}

	/**
	 * Creates a boolean bean with the given value;
	 * @param value The value.
	 */
	public BooleanBean( boolean value ) {
		this.value = value;
	}

	/**
	 * Creates a boolean bean with the given value;
	 * @param value The value.
	 */
	public BooleanBean( Boolean value ) {
		this.value = value;
	}

	/**
	 * @return Returns the value.
	 */
	public boolean isValue() {
		return value;
	}

	/**
	 * @param value The value to set.
	 */
	public void setValue( boolean value ) {
		if ( this.value != value ) {
			boolean old = this.value;
			this.value = value;
			firePropertyChanged( "value", old, this.value );
		}
	}

}