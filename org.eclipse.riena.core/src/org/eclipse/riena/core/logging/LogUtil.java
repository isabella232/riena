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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.equinox.log.ExtendedLogReaderService;
import org.eclipse.equinox.log.ExtendedLogService;
import org.eclipse.equinox.log.LogFilter;
import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.service.ServiceId;
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
			if (initialized)
				return;
			// TODO remove SysoLogListener if we have Log4jLogListener
			logListeners.add(new SysoLogListener());
			logListeners.add(new Log4jLogListener());

			new ServiceId(ExtendedLogService.class.getName()).useRanking().injectInto(this).andStart(context);
			new ServiceId(ExtendedLogReaderService.class.getName()).useRanking().injectInto(this).andStart(context);
			initialized = true;
		}
	}

	private static final class LogAlwaysFilter implements LogFilter {
		public boolean isLoggable(Bundle b, String loggerName, int logLevel) {
			return true;
		}
	}

}
