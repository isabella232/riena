/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.util.beans;

/**
 * A runtime exception that describes that a Java Bean property could not be
 * found.
 */
public final class PropertyNotFoundFailure extends PropertyFailure {

	/**
	 * Constructs a new exception instance with the specified detail message.
	 * The cause is not initialized.
	 * 
	 * @param propertyName
	 *            the name of the property that could not be found
	 * @param bean
	 *            the Java Bean used to lookup the property
	 */
	public PropertyNotFoundFailure(String propertyName, Object bean) {
		this(propertyName, bean, null);
	}

	/**
	 * Constructs a new exception instance with the specified detail message and
	 * cause.
	 * 
	 * @param propertyName
	 *            the name of the property that could not be found
	 * @param bean
	 *            the Java Bean used to lookup the property
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link #getCause()}method). (A<tt>null</tt> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */
	public PropertyNotFoundFailure(String propertyName, Object bean, Throwable cause) {
		super("Property '" + propertyName + "' not found in bean " + bean, cause);
	}
}
