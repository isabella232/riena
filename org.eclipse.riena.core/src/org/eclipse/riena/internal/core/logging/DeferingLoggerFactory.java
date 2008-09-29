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

import java.lang.reflect.Proxy;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.logging.LoggerMill;

/**
 * This logger factory creates a {@code Logger} that collects log events until a
 * <i>real</i> logger is available. The collected log events will than be
 * delivered by the <i>real</i> logger within a worker thread.
 */
public class DeferingLoggerFactory {

	private static Thread forwarder;
	private static BlockingQueue<DeferedLogEvent> queue;

	public static Logger createLogger(final String name, final LoggerMill loggerMill) {
		synchronized (DeferingLoggerFactory.class) {
			if (queue == null) {
				queue = new LinkedBlockingQueue<DeferedLogEvent>();
				forwarder = new DeferedLoggingForwarder(loggerMill, queue);
				forwarder.start();
			}
		}
		return (Logger) Proxy.newProxyInstance(Logger.class.getClassLoader(), new Class<?>[] { Logger.class },
				new DeferingLoggerHandler(name, loggerMill, queue));
	}

}