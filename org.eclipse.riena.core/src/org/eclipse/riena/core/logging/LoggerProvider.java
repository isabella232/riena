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
package org.eclipse.riena.core.logging;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.internal.core.Activator;
import org.eclipse.riena.internal.core.logging.DeferringLoggerFactory;
import org.eclipse.riena.internal.core.logging.LoggerMill;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The {@code LoggerProvider} is responsible for tracking the {@code LoggerMill}
 * and retrieving logger instances.
 */
public class LoggerProvider {

	private static ServiceTracker loggerMillTracker = null;
	private static int openCount = 0;

	/**
	 * Start tracking of the {@code LoggerMill}.
	 * 
	 * @throws Exception
	 */
	public synchronized void start() throws Exception {
		if (openCount++ == 0) {
			if (loggerMillTracker == null) {
				// This needs to be done lazy to avoid initialization problems.
				loggerMillTracker = new ServiceTracker(Activator.getDefault().getContext(), LoggerMill.class.getName(),
						null);
			}
			loggerMillTracker.open();
		}
	}

	/**
	 * Stop tracking of the {@code LoggerMill}.
	 * 
	 * @throws Exception
	 */
	public synchronized void stop() throws Exception {
		if (--openCount == 0) {
			loggerMillTracker.close();
		}
	}

	/**
	 * Get a logger for the given class.
	 * 
	 * @param clazz
	 * @return a logger
	 */
	public Logger getLogger(Class<?> clazz) {
		return getLogger(clazz.getName());
	}

	/**
	 * Get a logger with the given name.
	 * 
	 * @param name
	 * @return a logger
	 */
	public Logger getLogger(String name) {
		LoggerMill loggerMill = getReadyLoggerMill();
		if (loggerMill != null) {
			return loggerMill.getLogger(name);
		}
		return DeferringLoggerFactory.createLogger(name, this);
	}

	/**
	 * Get a real logger, i.e. not a deferring logger.
	 * 
	 * @param name
	 * @return a real logger; otherwise null.
	 */
	public Logger getRealLogger(String name) {
		LoggerMill loggerMill = getReadyLoggerMill();
		if (loggerMill == null) {
			return null;
		}
		return loggerMill.getLogger(name);
	}

	public boolean hasReadyLoggerMill() {
		return getReadyLoggerMill() != null;
	}

	private synchronized LoggerMill getReadyLoggerMill() {
		LoggerMill loggerMill = (LoggerMill) loggerMillTracker.getService();
		if (loggerMill != null && loggerMill.isReady()) {
			return loggerMill;
		}
		return null;
	}
}
