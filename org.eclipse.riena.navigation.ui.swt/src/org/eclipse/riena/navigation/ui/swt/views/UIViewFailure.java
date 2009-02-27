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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.riena.core.exception.Failure;

/**
 * Failure class for view specific failures.
 */
public class UIViewFailure extends Failure {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7955739836963025929L;

	/**
	 * 
	 * @param msg
	 *            the message
	 * @param args
	 *            some argument
	 * @param cause
	 *            the cause
	 */
	public UIViewFailure(String msg, Object[] args, Throwable cause) {
		super(msg, args, cause);
	}

	/**
	 * Failure with a simple message.
	 * 
	 * @param msg
	 *            the message
	 */
	public UIViewFailure(String msg) {
		super(msg);
	}

	/**
	 * @param msg
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public UIViewFailure(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * @param msg
	 *            the message
	 * @param arg1
	 *            some argument
	 * @param cause
	 *            the cause
	 */
	public UIViewFailure(String msg, Object arg1, Throwable cause) {
		super(msg, arg1, cause);
	}

	/**
	 * @param msg
	 *            the message
	 * @param arg1
	 *            some argument
	 * @param arg2
	 *            another argument
	 * @param cause
	 *            the cause
	 * 
	 */
	public UIViewFailure(String msg, Object arg1, Object arg2, Throwable cause) {
		super(msg, arg1, arg2, cause);
	}
}
