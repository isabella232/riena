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
package org.eclipse.riena.core.cache;

import org.eclipse.riena.core.exception.Failure;

/**
 * Failure for all GenericObjectCache related Failures
 * 
 * @author Christian Campo
 */
public class CacheFailure extends Failure {

	private static final long serialVersionUID = 8131505473671580214L;

	/**
	 * @param msg
	 */
	public CacheFailure(String msg) {
		super(msg);
	}

	/**
	 * @param msg
	 * @param arg1
	 * @param arg2
	 * @param cause
	 */
	public CacheFailure(String msg, Object arg1, Object arg2, Throwable cause) {
		super(msg, arg1, arg2, cause);
	}

	/**
	 * @param msg
	 * @param arg1
	 * @param cause
	 */
	public CacheFailure(String msg, Object arg1, Throwable cause) {
		super(msg, arg1, cause);
	}

	/**
	 * @param msg
	 * @param args
	 * @param cause
	 */
	public CacheFailure(String msg, Object[] args, Throwable cause) {
		super(msg, args, cause);
	}

	/**
	 * @param msg
	 * @param cause
	 */
	public CacheFailure(String msg, Throwable cause) {
		super(msg, cause);
	}
}
