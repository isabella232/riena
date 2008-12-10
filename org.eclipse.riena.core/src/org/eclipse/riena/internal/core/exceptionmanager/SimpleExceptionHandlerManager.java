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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.exception.IExceptionHandler;
import org.eclipse.riena.core.exception.IExceptionHandlerManager;
import org.eclipse.riena.internal.core.Activator;
import org.osgi.service.log.LogService;

/**
 * 
 */
public class SimpleExceptionHandlerManager implements IExceptionHandlerManager {

	private List<ExceptionHandlerEntry> handlers;
	private Logger LOGGER = Activator.getDefault().getLogger(SimpleExceptionHandlerManager.class);

	public void update(IExceptionHandlerDefinition[] exceptionHandlerDefinitions) {
		handlers = new ArrayList<ExceptionHandlerEntry>();
		for (IExceptionHandlerDefinition handlerDefinition : exceptionHandlerDefinitions) {
			IExceptionHandler exceptionHandler = handlerDefinition.createExceptionHandler();
			if (exceptionHandler == null) {
				LOGGER.log(LogService.LOG_ERROR, "could not instantiate exception handler " //$NON-NLS-1$
						+ handlerDefinition.getName() + " for class " + handlerDefinition.getExceptionHandler()); //$NON-NLS-1$
			}
			handlers = sort(handlers, exceptionHandler, handlerDefinition);
		}
	}

	private List<ExceptionHandlerEntry> sort(List<ExceptionHandlerEntry> existingHandlers, IExceptionHandler handler,
			IExceptionHandlerDefinition definition) {
		List<TopologicalNode<ExceptionHandlerEntry>> nodes = new ArrayList<TopologicalNode<ExceptionHandlerEntry>>(
				existingHandlers.size() + 1);
		ExceptionHandlerEntry exceptionHandlerEntry = new ExceptionHandlerEntry(handler, definition.getName(),
				definition.getBefore());
		TopologicalNode<ExceptionHandlerEntry> node = new TopologicalNode<ExceptionHandlerEntry>(definition.getName(),
				definition.getBefore(), exceptionHandlerEntry);
		nodes.add(node);
		for (ExceptionHandlerEntry nextHandler : existingHandlers) {
			node = new TopologicalNode<ExceptionHandlerEntry>(nextHandler.getName(), nextHandler.getBefore(),
					nextHandler);
			nodes.add(node);
		}
		return TopologicalSort.sort(nodes);
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
		for (ExceptionHandlerEntry handler : handlers) {
			Action action = handler.getExceptionHandler().handleException(t, msg, logger);
			if (action != Action.NotHandled) {
				return action;
			}
		}
		return Action.NotHandled;
	}

	static class ExceptionHandlerEntry {
		private IExceptionHandler exceptionHandler;
		private String before;
		private String name;

		public ExceptionHandlerEntry(IExceptionHandler exceptionHandler, String name, String before) {
			super();
			this.exceptionHandler = exceptionHandler;
			this.name = name;
			this.before = before;
		}

		public IExceptionHandler getExceptionHandler() {
			return exceptionHandler;
		}

		public String getBefore() {
			return before;
		}

		public String getName() {
			return name;
		}

	}

}
