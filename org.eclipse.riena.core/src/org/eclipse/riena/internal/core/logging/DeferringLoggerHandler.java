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
package org.eclipse.riena.internal.core.logging;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.BlockingQueue;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.logging.ConsoleLogger;
import org.eclipse.riena.core.logging.LoggerMill;

/**
 * A dynamic proxy that mimics a {@code Logger} that either logs to a
 * <i>real</i> logger if available or collects the log events for delayed
 * delivering by the {@code DeferredLoggingForwarder}.
 */
public class DeferringLoggerHandler implements InvocationHandler {

	private final String name;
	private final LoggerMill loggerMill;
	private final BlockingQueue<DeferredLogEvent> queue;
	private final Logger consoleLogger;
	private Logger logger = null;

	/**
	 * @param name
	 * @param loggerMill
	 * @param queue
	 */
	DeferringLoggerHandler(String name, LoggerMill loggerMill, BlockingQueue<DeferredLogEvent> queue) {
		this.name = name;
		this.loggerMill = loggerMill;
		this.queue = queue;
		this.consoleLogger = new ConsoleLogger(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
	 * java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (logger == null) {
			// try to get real logger
			if (loggerMill.isReady()) {
				// TODO hmmm, it might be no longer ready when getLogger() is called!?!
				logger = loggerMill.getLogger(name);
			}
		}

		// real logger available?
		if (logger != null) {
			return method.invoke(logger, args);
		}

		DeferredLogEvent logEvent = new DeferredLogEvent(name, System.currentTimeMillis(), Thread.currentThread()
				.getName(), method, args);

		// log to console
		method.invoke(consoleLogger, args);

		queue(logEvent);

		if (method.getName().equals("isLoggable")) { //$NON-NLS-1$
			return true;
		} else if (method.getName().equals("getName")) { //$NON-NLS-1$
			return name;
		}
		return null;
	}

	/**
	 * @param logEvent
	 */
	private void queue(DeferredLogEvent logEvent) {
		try {
			queue.put(logEvent);
		} catch (InterruptedException e) {
			// TODO handle this
			e.printStackTrace();
		}
	}
}
