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

import org.eclipse.equinox.log.ExtendedLogReaderService;
import org.eclipse.equinox.log.ExtendedLogService;
import org.eclipse.equinox.log.LogFilter;
import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.service.ServiceId;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * Wrapper to access the existing Logger.
 */
public class LogUtil {

	private ExtendedLogService logService;
	private ExtendedLogReaderService logReaderService;
	private boolean initialized = false;
	private BundleContext context;

	public LogUtil(BundleContext context) {
		this.context = context;
	}

	/**
	 * Get the logger for the specified category.<br>
	 * <b>Note:</b> Use the log levels defined in
	 * {@link org.osgi.service.log.LogService}
	 * 
	 * @param name
	 * @return
	 */
	public Logger getLogger(String name) {
		init();
		return logService == null ? new NullLogger() : logService.getLogger(name);
	}

	/**
	 * Bind ExtendedLogService
	 * 
	 * @param logService
	 */
	public void bind(ExtendedLogService logService) {
		this.logService = logService;
	}

	/**
	 * Unbind ExtendedLogService
	 * 
	 * @param logService
	 */
	public void unbind(ExtendedLogService logService) {
		this.logService = null;
	}

	/**
	 * Bind ExtendedLogReaderService
	 * 
	 * @param logReaderService
	 */
	public void bind(ExtendedLogReaderService logReaderService) {
		this.logReaderService = logReaderService;
		// TODO remove SysoLogListener if we have Log4jLogListener
		this.logReaderService.addLogListener(new SysoLogListener(), new LogAlwaysFilter());
		this.logReaderService.addLogListener(new Log4jLogListener(), new LogAlwaysFilter());
	}

	/**
	 * Unbind ExtendedLogReaderService
	 * 
	 * @param logReaderService
	 */
	public void unbind(ExtendedLogReaderService logReaderService) {
		this.logReaderService = null;
	}

	/**
	 * initialize LogUtil
	 */
	private synchronized void init() {
		if (initialized)
			return;
		new ServiceId(ExtendedLogService.class.getName()).useRanking().injectInto(this).andStart(context);
		new ServiceId(ExtendedLogReaderService.class.getName()).useRanking().injectInto(this).andStart(context);
		initialized = true;
	}

	private static final class LogAlwaysFilter implements LogFilter {
		public boolean isLoggable(Bundle b, String loggerName, int logLevel) {
			return true;
		}
	}
}
