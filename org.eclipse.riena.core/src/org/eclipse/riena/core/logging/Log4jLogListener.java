/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.logging;

import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.eclipse.equinox.log.ExtendedLogEntry;
import org.eclipse.equinox.log.SynchronousLogListener;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogService;

public class Log4jLogListener implements SynchronousLogListener {

	/**
	 * The default log4j configuration file (xml).
	 */
	public static final String DEFAULT_CONFIGURATION = "/log4j.default.xml"; //$NON-NLS-1$

	public Log4jLogListener(BundleContext context, String configuration) {
		if (configuration == null)
			configuration = DEFAULT_CONFIGURATION;

		// fetch the URL of given log4j configuration file via context
		// the context is the context of the bundle from which the log was
		// initiated
		URL url = context.getBundle().getResource(configuration);

		if (url != null) {
			// workaround to fix class loader problems with log4j
			// implementation. see "eclipse rich client platform, eclipse
			// series, page 340.
			Thread thread = Thread.currentThread();
			ClassLoader loader = thread.getContextClassLoader();
			thread.setContextClassLoader(this.getClass().getClassLoader());
			try {
				// configure the log4j with given config
				DOMConfigurator.configure(url);
			} finally {
				thread.setContextClassLoader(loader);
			}
		} else {
			// TODO: handle this ...
		}

	}

	public void logged(LogEntry entry) {
		ExtendedLogEntry extendedEntry = (ExtendedLogEntry) entry;
		String loggerName = extendedEntry.getLoggerName();
		Logger logger = Logger.getLogger(loggerName != null ? loggerName : "<unknown-logger-name>"); //$NON-NLS-1$

		switch (extendedEntry.getLevel()) {
		case LogService.LOG_DEBUG:
			logger.debug(extendedEntry.getMessage(), extendedEntry.getException());
			break;
		case LogService.LOG_WARNING:
			logger.warn(extendedEntry.getMessage(), extendedEntry.getException());
			break;
		case LogService.LOG_ERROR:
			logger.error(extendedEntry.getMessage(), extendedEntry.getException());
			break;
		case LogService.LOG_INFO:
			logger.info(extendedEntry.getMessage(), extendedEntry.getException());
			break;
		default:
			break;
		}

	}

}
