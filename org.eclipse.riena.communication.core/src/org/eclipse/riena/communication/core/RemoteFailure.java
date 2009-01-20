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
package org.eclipse.riena.communication.core;

import org.eclipse.riena.core.exception.Failure;

/**
 * 
 */
public class RemoteFailure extends Failure {

	private static final long serialVersionUID = -7485563916742344058L;

	public RemoteFailure(String msg, Object arg1, Object arg2, Throwable cause) {
		super(msg, arg1, arg2, cause);
	}

	public RemoteFailure(String msg, Object arg1, Throwable cause) {
		super(msg, arg1, cause);
	}

	public RemoteFailure(String msg, Object[] args, Throwable cause) {
		super(msg, args, cause);
	}

	public RemoteFailure(String msg, Throwable cause) {
		super(msg, cause);
	}

	public RemoteFailure(String msg) {
		super(msg);
	}

}
