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
package org.eclipse.riena.internal.core.exceptionmanager;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.exception.IExceptionHandler;
import org.eclipse.riena.core.exception.IExceptionHandlerManager;
import org.eclipse.riena.core.wire.InjectExtension;
import org.eclipse.riena.internal.core.Activator;

/**
 * 
 */
public class SimpleExceptionHandlerManager implements IExceptionHandlerManager {

	private List<IExceptionHandler> handlers;
	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), SimpleExceptionHandlerManager.class);

	@InjectExtension(id = IExceptionHandlerExtension.EXTENSION_POINT)
	public void update(IExceptionHandlerExtension[] exceptionHandlerDefinitions) {
		List<TopologicalNode<IExceptionHandler>> nodes = new ArrayList<TopologicalNode<IExceptionHandler>>(
				exceptionHandlerDefinitions.length);
		for (IExceptionHandlerExtension handlerDefinition : exceptionHandlerDefinitions) {
			IExceptionHandler exceptionHandler = handlerDefinition.createExceptionHandler();
			if (exceptionHandler == null) {
				LOGGER.log(LogService.LOG_ERROR, "could not instantiate exception handler " //$NON-NLS-1$
						+ handlerDefinition.getName() + " for class " + handlerDefinition.getExceptionHandler()); //$NON-NLS-1$
			}
			nodes.add(new TopologicalNode<IExceptionHandler>(handlerDefinition.getName(),
					handlerDefinition.getBefore(), exceptionHandler));
		}
		handlers = TopologicalSort.sort(nodes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.core.exception.IExceptionHandlerManager#handleCaught
	 * (java.lang.Throwable)
	 */
	public Action handleException(Throwable t) {
		return handleException(t, null, LOGGER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.core.exception.IExceptionHandlerManager#handleCaught
	 * (java.lang.Throwable, org.eclipse.equinox.log.Logger)
	 */
	public Action handleException(Throwable t, Logger logger) {
		return handleException(t, null, logger);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.core.exception.IExceptionHandlerManager#handleCaught
	 * (java.lang.Throwable, java.lang.String)
	 */
	public Action handleException(Throwable t, String msg) {
		return handleException(t, msg, LOGGER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.core.exception.IExceptionHandlerManager#handleCaught
	 * (java.lang.Throwable, java.lang.String, org.eclipse.equinox.log.Logger)
	 */
	public Action handleException(Throwable t, String msg, Logger logger) {
		for (IExceptionHandler handler : handlers) {
			Action action = handler.handleException(t, msg, logger);
			if (action != Action.NOT_HANDLED) {
				return action;
			}
		}
		return Action.NOT_HANDLED;
	}
}
