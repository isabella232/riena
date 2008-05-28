/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.util.beans;

/**
 * A runtime exception that describes that a Java Bean does not support bound
 * properties. The conditions for bound properties are specified in section 7.4
 * of the <a href="http://java.sun.com/products/javabeans/docs/spec.html">Java
 * Bean Secification </a>. Basically you must provide the following two methods:
 * 
 * <pre>
 * public void addPropertyChangeHandler(PropertyChangeListener x);
 * 
 * public void removePropertyChangeHandler(PropertyChangeListener x);
 * </pre>
 */
public final class PropertyUnboundFailure extends PropertyFailure {

	/**
	 * Constructs a new exception instance with the specified detail message.
	 * The cause is not initialized.
	 * 
	 * @param message
	 *            the detail message. The detail message is saved for later
	 *            retrieval by the {@link #getMessage()}method.
	 */
	public PropertyUnboundFailure(String message) {
		super(message);
	}

	/**
	 * Constructs a new exception instance with the specified detail message and
	 * cause.
	 * 
	 * @param message
	 *            the detail message (which is saved for later retrieval by the
	 *            {@link #getMessage()}method).
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link #getCause()}method). (A<tt>null</tt> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */
	public PropertyUnboundFailure(String message, Throwable cause) {
		super(message, cause);
	}

}