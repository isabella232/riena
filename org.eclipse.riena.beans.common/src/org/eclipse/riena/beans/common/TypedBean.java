/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.beans.common;

/**
 * Typed bean provides a typed value for simple adapter UI-Binding. The type of
 * the value has to be passed as a type parameter.
 */
public class TypedBean<T> extends AbstractBean {

	/**
	 * Key for the value property (PROP_VALUE = "value").
	 */
	public static final String PROP_VALUE = "value"; //$NON-NLS-1$

	private T value;

	/**
	 * Creates a TypedBean bean with the given value
	 * 
	 * @param value
	 */
	public TypedBean(final T value) {
		this.value = value;
	}

	/**
	 * Sets the value of this bean
	 * 
	 * @param value
	 *            to set
	 */
	public void setValue(final T value) {
		final Object old = this.value;
		this.value = value;
		firePropertyChanged(PROP_VALUE, old, this.value);
	}

	/**
	 * Returns the value of this bean
	 * 
	 * @return value
	 */
	public T getValue() {
		return value;
	}
}
