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
package org.eclipse.riena.security.common;


public class NotAuthorizedFailure extends SecurityFailure {

	/**
	 * @param msg
	 * @param args
	 * @param cause
	 */
	public NotAuthorizedFailure(String msg, Object[] args, Throwable cause) {
		super(msg, args, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param msg
	 */
	public NotAuthorizedFailure(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param msg
	 * @param cause
	 */
	public NotAuthorizedFailure(String msg, Throwable cause) {
		super(msg, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param msg
	 * @param arg1
	 * @param cause
	 */
	public NotAuthorizedFailure(String msg, Object arg1, Throwable cause) {
		super(msg, arg1, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param msg
	 * @param arg1
	 * @param arg2
	 * @param cause
	 */
	public NotAuthorizedFailure(String msg, Object arg1, Object arg2, Throwable cause) {
		super(msg, arg1, arg2, cause);
		// TODO Auto-generated constructor stub
	}

}
