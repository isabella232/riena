/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.util.beans;

import org.eclipse.riena.core.exception.Failure;

/**
 * A runtime exception that is the abstract superclass for all exceptions around
 * Java Bean properties in the JGoodies Data Binding framework.
 */
abstract public class PropertyFailure extends Failure {

	/**
	 * Constructs a new exception instance with the specified detail message.
	 * The cause is not initialized.
	 * 
	 * @param message
	 *            the detail message. The detail message is saved for later
	 *            retrieval by the {@link #getMessage()}method.
	 */
	public PropertyFailure(String message) {
		this(message, null);
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
	 *            {@link #getCause()}method). (A<code>null</code> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */
	public PropertyFailure(String message, Throwable cause) {
		super(message, cause);
	}
}