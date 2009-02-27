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
package org.eclipse.riena.ui.ridgets;

import org.eclipse.riena.core.exception.Failure;

/**
 * failure class for the ui binding.
 */
public class UIBindingFailure extends Failure {

	private static final long serialVersionUID = -1361489127000931906L;

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
