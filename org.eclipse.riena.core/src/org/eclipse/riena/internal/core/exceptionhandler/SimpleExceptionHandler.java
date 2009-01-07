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
package org.eclipse.riena.internal.core.exceptionhandler;

import org.eclipse.riena.core.exception.IExceptionHandler;
import org.eclipse.riena.core.exception.IExceptionHandlerManager.Action;

import org.eclipse.equinox.log.Logger;
import org.osgi.service.log.LogService;

/**
 * a very simple exception handler that logs the exception to the riena log is
 * also defined in the extension as the one with the least priority
 */
public class SimpleExceptionHandler implements IExceptionHandler {

	public Action handleException(Throwable t, String msg, Logger logger) {
		if (logger != null) {
			logger.log(LogService.LOG_ERROR, msg, t);
			return Action.OK;
		}
		return Action.NOT_HANDLED;
	}

}
