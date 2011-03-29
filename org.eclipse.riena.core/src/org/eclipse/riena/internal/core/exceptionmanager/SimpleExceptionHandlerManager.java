/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
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
 * A simple implementation of a {@code IExceptionHandlerManager}.<br>
 * This exception handler manager asks the configured {@code IExceptionHandler}s
 * whether they can handle a given exception, e.g. display the exception in
 * dialog or log it.
 */
public class SimpleExceptionHandlerManager implements IExceptionHandlerManager {

	private List<IExceptionHandler> handlers;
	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), SimpleExceptionHandlerManager.class);

	@InjectExtension
	public void update(final IExceptionHandlerExtension[] exceptionHandlerDefinitions) {
		final List<TopologicalNode<IExceptionHandler>> nodes = new ArrayList<TopologicalNode<IExceptionHandler>>(
				exceptionHandlerDefinitions.length);
		for (final IExceptionHandlerExtension handlerDefinition : exceptionHandlerDefinitions) {
			final IExceptionHandler exceptionHandler = handlerDefinition.createExceptionHandler();
			if (exceptionHandler == null) {
				LOGGER.log(LogService.LOG_ERROR, "could not instantiate exception handler " //$NON-NLS-1$
						+ handlerDefinition.getName() + " for class " + handlerDefinition.getExceptionHandler()); //$NON-NLS-1$
			}
			nodes.add(new TopologicalNode<IExceptionHandler>(handlerDefinition.getName(),
					handlerDefinition.getBefore(), exceptionHandler));
		}
		handlers = TopologicalSort.sort(nodes);
	}

	/**
	 * {@inheritDoc}
	 */
	public IExceptionHandler.Action handleException(final Throwable t) {
		return handleException(t, null, LOGGER);
	}

	/**
	 * {@inheritDoc}
	 */
	public IExceptionHandler.Action handleException(final Throwable t, final Logger logger) {
		return handleException(t, null, logger);
	}

	/**
	 * {@inheritDoc}
	 */
	public IExceptionHandler.Action handleException(final Throwable t, final String msg) {
		return handleException(t, msg, LOGGER);
	}

	/**
	 * {@inheritDoc}
	 */
	public IExceptionHandler.Action handleException(final Throwable t, final String msg, final Logger logger) {
		for (final IExceptionHandler handler : handlers) {
			final IExceptionHandler.Action action = handler.handleException(t, msg, logger);
			if (action != IExceptionHandler.Action.NOT_HANDLED) {
				return action;
			}
		}
		return IExceptionHandler.Action.NOT_HANDLED;
	}
}
