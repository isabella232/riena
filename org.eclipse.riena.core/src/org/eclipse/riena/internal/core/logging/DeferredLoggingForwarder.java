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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.concurrent.BlockingQueue;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.logging.ConsoleLogger;
import org.eclipse.riena.core.logging.LoggerProvider;
import org.eclipse.riena.internal.core.Activator;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

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
	public DeferredLoggingForwarder(final LoggerProvider loggerProvider, BlockingQueue<DeferredLogEvent> queue) {
		super("DeferredLoggingForwarder"); //$NON-NLS-1$
		this.loggerProvider = loggerProvider;
		this.queue = queue;
	}

	public void run() {
		while (true) {
			DeferredLogEvent logEvent = null;
			try {
				logEvent = queue.take();
			} catch (InterruptedException e1) {
				Thread.currentThread().interrupt();
				break;
			}

			while (!loggerProvider.hasReadyLoggerMill()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}
			try {
				Logger logger = Activator.getDefault().getLogger(logEvent.getLoggerName());
				// TODO There is no guarantee that the next two logs get logged one after the other!!
				StringBuilder bob = new StringBuilder("Defered log event occured on "); //$NON-NLS-1$
				bob.append(new Date(logEvent.getTime())).append(" (").append(logEvent.getTime()); //$NON-NLS-1$
				bob.append(" ms) in thread [").append(logEvent.getThreadName()).append("]:\n"); //$NON-NLS-1$ //$NON-NLS-2$
				appendArgs(bob, logEvent.getArgs());
				logger.log(getLevel(logEvent.getArgs()), bob.toString());
			} catch (Exception e) {
				new ConsoleLogger(DeferredLoggingForwarder.class.getName()).log(LogService.LOG_ERROR,
						"Could not deliver defered log message.", e); //$NON-NLS-1$
			}
		}
	}

	/**
	 * @param args
	 * @return
	 */
	private int getLevel(final Object[] args) {
		// This is a little bit brittle - we guess the log level from it's type
		for (Object arg : args) {
			if (arg instanceof Integer) {
				return (Integer) arg;
			}
		}
		return LogService.LOG_DEBUG;
	}

	private void appendArgs(StringBuilder bob, Object[] args) {
		for (Object arg : args) {
			if (arg instanceof ServiceReference) {
				ServiceReference ref = (ServiceReference) arg;
				String bundleName = ref.getBundle() != null ? ref.getBundle().getSymbolicName() : null;
				bob.append("ServiceReference( bundle=").append(bundleName).append(", properties=("); //$NON-NLS-1$ //$NON-NLS-2$
				for (String key : ref.getPropertyKeys()) {
					bob.append(key).append("=").append(ref.getProperty(key)).append(","); //$NON-NLS-1$ //$NON-NLS-2$
				}
				bob.append(")"); //$NON-NLS-1$
			} else if (arg instanceof String) {
				bob.append("Message(").append((String) arg).append(')'); //$NON-NLS-1$
			} else if (arg instanceof Integer) {
				bob.append("LogLevel(").append((Integer) arg).append(')'); //$NON-NLS-1$
			} else if (arg instanceof Throwable) {
				Throwable throwable = (Throwable) arg;
				bob.append("Throwable("); //$NON-NLS-1$
				StringWriter stringWriter = new StringWriter();
				PrintWriter writer = new PrintWriter(stringWriter);
				throwable.printStackTrace(writer);
				writer.close();
				bob.append(stringWriter.toString()).append(')');
			} else {
				bob.append("Object(").append(arg).append(')'); //$NON-NLS-1$
			}
			bob.append(", "); //$NON-NLS-1$
		}
		bob.setLength(bob.length() - 2);
	}
}
