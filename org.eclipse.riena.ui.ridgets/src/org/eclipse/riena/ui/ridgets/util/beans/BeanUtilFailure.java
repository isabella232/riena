/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.util.beans;

import org.eclipse.riena.core.util.UtilFailure;

/**
 * Failure class for bean util specific failures.
 * 
 * @author Stefan Liebig
 */
public class BeanUtilFailure extends UtilFailure {

	/**
	 * Failure with a simple message.
	 * 
	 * @param msg
	 *            the message
	 */
	public BeanUtilFailure(String msg) {
		super(msg);
	}

	/**
	 * Failure with a simple message and a cause.
	 * 
	 * @param msg
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public BeanUtilFailure(String msg, Throwable cause) {
		super(msg, cause);
	}
}