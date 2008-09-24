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
package org.eclipse.riena.core.logging;

import java.net.URL;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.eclipse.core.runtime.ContributorFactoryOSGi;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.equinox.log.ExtendedLogEntry;
import org.osgi.framework.Bundle;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogService;

/**
 * The <code>Log4LogListener</code> reroutes all logging within Riena into the
 * Log4J logging system.<br>
 * To activate it is necessary to contribute to the extension point
 * "org.eclipse.riena.core.logging.listeners". Within that configuration it is
 * possible to pass a ´log4j.xml´ as resource to configure Log4j.
 */
public class Log4jLogListener implements LogListener, IExecutableExtension {

	/**
	 * The default log4j configuration file (xml).
	 */
	public static final String DEFAULT_CONFIGURATION = "/log4j.default.xml"; //$NON-NLS-1$

	public Log4jLogListener() {
	}

	public void logged(LogEntry entry) {
		ExtendedLogEntry extendedEntry = (ExtendedLogEntry) entry;
		String loggerName = extendedEntry.getLoggerName();
		Logger logger = Logger.getLogger(loggerName != null ? loggerName : "<unknown-logger-name>"); //$NON-NLS-1$

		Level level;
		switch (extendedEntry.getLevel()) {
		case LogService.LOG_DEBUG:
			level = Level.DEBUG;
			break;
		case LogService.LOG_WARNING:
			level = Level.WARN;
			break;
		case LogService.LOG_ERROR:
			level = Level.ERROR;
			break;
		case LogService.LOG_INFO:
			level = Level.INFO;
			break;
		default:
			level = Level.OFF;
			break;
		}
		logger.log(level, extendedEntry.getMessage(), extendedEntry.getException());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org
	 * .eclipse.core.runtime.IConfigurationElement, java.lang.String,
	 * java.lang.Object)
	 */
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
			throws CoreException {
		if (data == null) {
			data = DEFAULT_CONFIGURATION;
		}
		if (!(data instanceof String)) {
			return;
		}
		Bundle bundle = ContributorFactoryOSGi.resolve(config.getContributor());
		configure(bundle, (String) data);
	}

	private void configure(Bundle bundle, String configuration) {
		// fetch the URL of given log4j configuration file via context
		// the context is the context of the bundle from which the log was
		// initiated
		URL url = bundle.getResource(configuration);

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
			new ConsoleLogger(Log4jLogListener.class.getName()).log(LogService.LOG_ERROR,
					"Could not find specified log4j configuration '" + configuration //$NON-NLS-1$
							+ "' within bundle '" //$NON-NLS-1$
							+ bundle.getSymbolicName() + "'."); //$NON-NLS-1$
		}

	}
}
