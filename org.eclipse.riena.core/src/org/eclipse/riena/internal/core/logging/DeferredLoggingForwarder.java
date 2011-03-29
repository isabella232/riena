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
package org.eclipse.riena.internal.core.logging;

import java.util.concurrent.BlockingQueue;

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.logging.ConsoleLogger;
import org.eclipse.riena.internal.core.Activator;

/**
 * The worker thread that delivers collected log events.
 */
public class DeferredLoggingForwarder extends Thread {

	private final LoggerProvider loggerProvider;
	private final BlockingQueue<DeferredLogEvent> queue;

	/**
	 * @param loggerProvider
	 * @param queue
	 */
	public DeferredLoggingForwarder(final LoggerProvider loggerProvider, final BlockingQueue<DeferredLogEvent> queue) {
		super("DeferredLoggingForwarder"); //$NON-NLS-1$
		setDaemon(true);
		this.loggerProvider = loggerProvider;
		this.queue = queue;
	}

	@Override
	public void run() {
		while (true) {
			DeferredLogEvent logEvent = null;
			try {
				logEvent = queue.take();
			} catch (final InterruptedException e1) {
				Thread.currentThread().interrupt();
				break;
			}

			while (!loggerProvider.hasReadyLoggerMill()) {
				try {
					Thread.sleep(10);
				} catch (final InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}
			try {
				final Logger logger = Log4r.getLogger(Activator.getDefault(), logEvent.getLoggerName());
				logger.log(logEvent.getLevel(), logEvent.toString());
			} catch (final Exception e) {
				new ConsoleLogger(DeferredLoggingForwarder.class.getName()).log(LogService.LOG_ERROR,
						"Could not deliver defered log message.", e); //$NON-NLS-1$
			}
		}
	}

}
