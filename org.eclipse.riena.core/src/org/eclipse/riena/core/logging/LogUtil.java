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
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.xml.DOMConfigurator;
import org.eclipse.equinox.log.ExtendedLogReaderService;
import org.eclipse.equinox.log.ExtendedLogService;
import org.eclipse.equinox.log.LogFilter;
import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.injector.Inject;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogListener;

/**
 * Wrapper to access the existing Logger.
 */
public class LogUtil {

	private static ExtendedLogService logService;
	private static ExtendedLogReaderService logReaderService;
	private List<LogListener> logListeners = new ArrayList<LogListener>();
	private static boolean initialized = false;
	private BundleContext context;
	private static final String DEFAULT_CONFIGURATION = "/log4j.default.xml";
	private String config = null;

	public LogUtil(BundleContext context) {
		this.context = context;
	}

	/**
	 * Get the logger for the specified category.<br>
	 * <b>Note:</b> Use the log levels defined in
	 * {@link org.osgi.service.log.LogService}
	 * 
	 * @param name
	 *            logger name
	 * @return
	 */
	public Logger getLogger(String name) {
		init();
		return logService == null ? new ConsoleLogger(name) : logService.getLogger(name);
	}

	/**
	 * Get the logger for the specified category where log4j is configured by a
	 * given config xml.<br>
	 * <b>Note:</b> Use the log levels defined in
	 * {@link org.osgi.service.log.LogService}
	 * 
	 * @param config
	 *            name of the config file, specified relative ( example:
	 *            "/log4j_example.xml")
	 * @param name
	 *            logger name
	 * @return
	 */
	public Logger getLogger(String config, String name) {
		this.config = config;
		init();
		return logService == null ? new ConsoleLogger(name) : logService.getLogger(name);
	}

	/**
	 * Bind ExtendedLogService
	 * 
	 * @param logService
	 */
	public void bind(ExtendedLogService logService) {
		LogUtil.logService = logService;
	}

	/**
	 * Unbind ExtendedLogService
	 * 
	 * @param logService
	 */
	public void unbind(ExtendedLogService logService) {
		LogUtil.logService = null;
	}

	/**
	 * Bind ExtendedLogReaderService
	 * 
	 * @param logReaderService
	 */
	public void bind(ExtendedLogReaderService logReaderService) {
		LogUtil.logReaderService = logReaderService;
		for (LogListener logListener : logListeners)
			LogUtil.logReaderService.addLogListener(logListener, new LogAlwaysFilter());
	}

	/**
	 * Unbind ExtendedLogReaderService
	 * 
	 * @param logReaderService
	 */
	public void unbind(ExtendedLogReaderService logReaderService) {
		for (LogListener logListener : logListeners)
			LogUtil.logReaderService.removeLogListener(logListener);

		LogUtil.logReaderService = null;
	}

	/**
	 * initialize LogUtil
	 */
	private void init() {
		synchronized (LogUtil.class) {
			if (!initialized) {

				logListeners.add(new SysoLogListener());
				logListeners.add(new Log4jLogListener());

				Inject.service(ExtendedLogService.class.getName()).useRanking().into(this).andStart(context);
				Inject.service(ExtendedLogReaderService.class.getName()).useRanking().into(this).andStart(context);
				initialized = true;
			}

			// TODO: do we need a default ???

			if (config == null) {
				config = DEFAULT_CONFIGURATION;
			}

			// fetch the URL of given log4j configuration file via context
			// the context is the context of the bundle from which the log was
			// initiated
			URL url = context.getBundle().getResource(config);

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
	}

	private static final class LogAlwaysFilter implements LogFilter {
		public boolean isLoggable(Bundle b, String loggerName, int logLevel) {
			return true;
		}
	}

}
