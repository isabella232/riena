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

import org.eclipse.riena.core.util.UtilFailure;

/**
 * Failure class for bean util specific failures.
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