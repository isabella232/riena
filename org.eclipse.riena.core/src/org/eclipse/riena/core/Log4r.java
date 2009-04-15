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
package org.eclipse.riena.core;

import org.osgi.framework.ServiceReference;

import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.logging.ConsoleLogger;

/**
 * Gets a logger ({@code Logger}) from the {@code IRienaActivator}. <br>
 * <b>Note: </b> The recommended pattern for classes within bundles where their
 * activator is an instance of {@code IRienaActivator} is: <br>
 * <code>
 * 	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), This.class);
 * </code>
 */
public final class Log4r {

	private Log4r() {
		// utility
	}

	/**
	 * Get a {@code Logger} for class {@code clazz}.
	 * 
	 * @param activator
	 *            If the activator is NOT {@code null} this method will return
	 *            the {@code Logger} of the Riena framework. Otherwise if the
	 *            boolean system property {@code
	 *            LoggerMill.RIENA_DEFAULT_LOGGING} is {@code true} a simple
	 *            {@code Logger} that writes to the console will be returned. If
	 *            the system property is {@code false} a ´null´ logger that does
	 *            nothing will be returned.
	 * @param clazz
	 *            categorizes the logger
	 * @return the {@code Logger}
	 */
	public static Logger getLogger(IRienaActivator activator, Class<?> clazz) {
		if (activator != null) {
			return activator.getLogger(clazz);
		}
		return getEmergencyLogger(clazz.getName());
	}

	/**
	 * Get a {@code Logger} for class {@code className}.
	 * 
	 * @param activator
	 *            If the activator is NOT {@code null} this method will return
	 *            the {@code Logger} of the Riena framework. Otherwise if the
	 *            boolean system property {@code
	 *            LoggerMill.RIENA_DEFAULT_LOGGING} is {@code true} a simple
	 *            {@code Logger} that writes to the console will be returned. If
	 *            the system property is {@code false} a ´null´ logger that does
	 *            nothing will be returned.
	 * @param className
	 *            categorizes the logger
	 * @return the {@code Logger}
	 */
	public static Logger getLogger(IRienaActivator activator, String className) {
		if (activator != null) {
			return activator.getLogger(className);
		}
		return getEmergencyLogger(className);
	}

	// use ConsoleLogger unless the system property specifies otherwise
	private static Logger getEmergencyLogger(String className) {
		return RienaStatus.isDevelopment() ? new ConsoleLogger(className) : new NullLogger();
	}

	private static class NullLogger implements Logger {

		public String getName() {
			return "NullLogger"; //$NON-NLS-1$
		}

		public boolean isLoggable(int level) {
			return false;
		}

		public void log(int level, String message) {
		}

		public void log(int level, String message, Throwable exception) {
		}

		public void log(ServiceReference sr, int level, String message) {
		}

		public void log(ServiceReference sr, int level, String message, Throwable exception) {
		}

		public void log(Object context, int level, String message) {
		}

		public void log(Object context, int level, String message, Throwable exception) {
		}

	}
}
