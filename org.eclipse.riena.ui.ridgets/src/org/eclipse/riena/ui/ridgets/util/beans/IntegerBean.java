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
package org.eclipse.riena.ui.ridgets.util.beans;

/**
 * Integer bean provides a boolean value for simple adapter UI-Binding
 */
public class IntegerBean extends AbstractBean {

	private Integer value;

	/**
	 * Creates an integer bean with a default 0
	 * 
	 * @param value
	 */
	public IntegerBean() {
		value = 0;
	}

	/**
	 * Creates a integer bean with the given value
	 * 
	 * @param value
	 */
	public IntegerBean(Integer value) {
		this.value = value;
	}

	/**
	 * Creates a integer bean with the given value
	 * 
	 * @param value
	 */
	public IntegerBean(int value) {
		this.value = value;
	}

	/**
	 * Sets the value of this bean
	 * 
	 * @param value -
	 *            value to set
	 */
	public void setValue(Integer value) {
		Object old = this.value;
		this.value = value;
		firePropertyChanged("value", old, this.value); //$NON-NLS-1$

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