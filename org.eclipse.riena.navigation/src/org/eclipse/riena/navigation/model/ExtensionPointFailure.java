/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.model;

import org.eclipse.riena.core.exception.Failure;

/**
 *
 */
public class ExtensionPointFailure extends Failure {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6236123264651424352L;

	/**
	 * @param msg
	 * @param args
	 * @param cause
	 */
	public ExtensionPointFailure(String msg, Object[] args, Throwable cause) {
		super(msg, args, cause);
	}

	/**
	 * @param msg
	 */
	public ExtensionPointFailure(String msg) {
		super(msg);
	}

	/**
	 * @param msg
	 * @param cause
	 */
	public ExtensionPointFailure(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * @param msg
	 * @param arg1
	 * @param cause
	 */
	public ExtensionPointFailure(String msg, Object arg1, Throwable cause) {
		super(msg, arg1, cause);
	}

	/**
	 * @param msg
	 * @param arg1
	 * @param arg2
	 * @param cause
	 */
	public ExtensionPointFailure(String msg, Object arg1, Object arg2, Throwable cause) {
		super(msg, arg1, arg2, cause);
	}
}
