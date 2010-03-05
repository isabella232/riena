/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.core.logging;

import org.osgi.framework.FrameworkUtil;

import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.core.wire.Wire;

/**
 * The {@code LoggerProvider} is responsible for tracking the {@code LoggerMill}
 * and retrieving logger instances.
 */
public final class LoggerProvider {

	private boolean started;
	private final LoggerMill loggerMill = new LoggerMill();

	private static final LoggerProvider INSTANCE = new LoggerProvider();

	/**
	 * Get the {@code LoggerProvider} singelton.
	 * 
	 * @return the one-and-only {@code LggerProvider}
	 */
	public static LoggerProvider instance() {
		return INSTANCE;
	}

	private LoggerProvider() {
		// Singleton
	}

	/**
	 * Start the {@code LoggerProvider}.
	 */
	public synchronized void start() {
		if (!started) {
			started = true;
			Wire.instance(loggerMill).andStart(FrameworkUtil.getBundle(LoggerProvider.class).getBundleContext());
		}
	}

	/**
	 * Stop the {@code LoggerProvider}.
	 */
	public synchronized void stop() {
		if (started) {
			started = false;
			Nop.reason("Maybe we tear down the wire puller here"); //$NON-NLS-1$
		}
	}

	/**
	 * Get a logger for the given class.
	 * 
	 * @param clazz
	 * @return a logger
	 */
	public Logger getLogger(final Class<?> clazz) {
		return getLogger(clazz.getName());
	}

	/**
	 * Get a logger with the given name.
	 * 
	 * @param name
	 * @return a logger
	 */
	public Logger getLogger(final String name) {
		if (loggerMill.isReady()) {
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
	Logger getRealLogger(final String name) {
		if (!loggerMill.isReady()) {
			return null;
		}
		return loggerMill.getLogger(name);
	}

	boolean hasReadyLoggerMill() {
		return loggerMill.isReady();
	}

}
