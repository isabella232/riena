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
package org.eclipse.riena.internal.core.exceptionmanager;

import org.eclipse.riena.core.exception.IExceptionHandlerManager;

/**
 * returns the current ExceptionHandlerManger
 */
public class ExceptionHandlerManagerAccessor {

	private static IExceptionHandlerManager exceptionHandlerManager;

	public void bind(IExceptionHandlerManager exceptionHandlerManagerParm) {
		exceptionHandlerManager = exceptionHandlerManagerParm;
	}

	public void unbind(IExceptionHandlerManager exceptionHandlerManagerParm) {
		exceptionHandlerManager = null;
	}

	public static IExceptionHandlerManager getExceptionHandlerManager() {
		return exceptionHandlerManager;
	}

}
