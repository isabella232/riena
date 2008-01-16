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
package org.eclipse.riena.core.util;

import org.eclipse.riena.core.exception.Failure;

/**
 * Failure class for util specific failures.
 * 
 * @author Stefan Liebig
 */
public class UtilFailure extends Failure {

	/**
	 * Constructor. Failure with a simple message.
	 * 
	 * @param msg
	 *            the message.
	 */
	public UtilFailure(String msg) {
		super(msg);
	}

	/**
	 * Constructor. Failure with a simple message and a cause.
	 * 
	 * @param msg
	 *            the message.
	 * @param cause
	 *            the cause.
	 */
	public UtilFailure(String msg, Throwable cause) {
		super(msg, cause);
	}
}