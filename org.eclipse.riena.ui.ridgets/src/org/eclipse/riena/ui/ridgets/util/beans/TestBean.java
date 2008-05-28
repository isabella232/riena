/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2008 compeople AG                         *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.util.beans;

/**
 * A simple bean.
 * 
 * @author Carsten Drossel
 */
public class TestBean {

	/**
	 * The name of the 'property' property of the bean.
	 */
	public static final String PROPERTY = "property";

	private Object property;

	/**
	 * @return The property.
	 */
	public Object getProperty() {
		return property;
	}

	/**
	 * Sets the property.
	 * 
	 * @param property
	 *            The new property value.
	 */
	public void setProperty(Object property) {
		this.property = property;
	}

}
