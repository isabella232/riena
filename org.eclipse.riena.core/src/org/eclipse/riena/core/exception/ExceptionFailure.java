/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.exception;

/**
 * Failure class for exception handler specific failures.
 * 
 * @author Stefan Liebig
 */
public class ExceptionFailure extends Failure {

	/**
	 * Failure with a simple message.
	 * 
	 * @param msg
	 *            the message.
	 */
	public ExceptionFailure(String msg) {
		super(msg);
	}

	/**
	 * Failure with a simple message and a cause.
	 * 
	 * @param msg
	 *            the message.
	 * @param cause
	 *            the cause.
	 */
	public ExceptionFailure(String msg, Throwable cause) {
		super(msg, cause);
	}
}