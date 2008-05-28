/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets;

import org.eclipse.riena.core.exception.Failure;

/**
 * failure class for the ui binding.
 * 
 * @author Juergen Becker
 */
public class UIBindingFailure extends Failure {

	/**
	 * @param msg
	 * @param args
	 * @param cause
	 */
	public UIBindingFailure(String msg, Object[] args, Throwable cause) {
		super(msg, args, cause);
	}

	/**
	 * @param msg
	 */
	public UIBindingFailure(String msg) {
		super(msg);
	}

	/**
	 * @param msg
	 * @param cause
	 */
	public UIBindingFailure(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * @param msg
	 * @param arg1
	 * @param cause
	 */
	public UIBindingFailure(String msg, Object arg1, Throwable cause) {
		super(msg, arg1, cause);
	}

	/**
	 * @param msg
	 * @param arg1
	 * @param arg2
	 * @param cause
	 */
	public UIBindingFailure(String msg, Object arg1, Object arg2, Throwable cause) {
		super(msg, arg1, arg2, cause);
	}
}