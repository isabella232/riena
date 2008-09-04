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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.equinox.log.ExtendedLogReaderService;
import org.eclipse.equinox.log.ExtendedLogService;
import org.eclipse.equinox.log.LogFilter;
import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.core.logging.ILogCatcherDefinition;
import org.eclipse.riena.internal.core.logging.ILogListenerDefinition;
import org.eclipse.riena.internal.core.logging.SynchronousLogListenerAdapter;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogListener;

/**
 * The <code>LoggerMill</code> is responsible for delivering ready to use
 * <code>Logger</code> instances and also for the configuration of the riena
 * logging.<br>
 * For the curious: There are so many LoggerFactories out there so. Not another
 * one!
 */
public class LoggerMill {

	private BundleContext context;
	private List<LogListener> logListeners = new ArrayList<LogListener>();
	private List<ILogCatcher> logCatchers = new ArrayList<ILogCatcher>();;
	private ILogListenerDefinition[] listenerDefs;
	private ILogCatcherDefinition[] catcherDefs;

	private static ExtendedLogService logService;
	private static ExtendedLogReaderService logReaderService;
	private static AtomicBoolean initialized = new AtomicBoolean(false);

	private static final LogFilter SYSTEM_PROPERTY_LOG_FILTER = new SystemPropertyLogFilter();

	public LoggerMill(BundleContext context) {
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
	 * Bind ExtendedLogService
	 * 
	 * @param logService
	 */
	public void bind(ExtendedLogService logService) {
		LoggerMill.logService = logService;
	}

	/**
	 * Unbind ExtendedLogService
	 * 
	 * @param logService
	 */
	public void unbind(ExtendedLogService logService) {
		LoggerMill.logService = null;
	}

	/**
	 * Bind ExtendedLogReaderService
	 * 
	 * @param logReaderService
	 */
	public void bind(ExtendedLogReaderService logReaderService) {
		LoggerMill.logReaderService = logReaderService;
		if (listenerDefs == null) {
			return;
		}
		if (listenerDefs.length == 0) {
			LogListener listener = new SynchronousLogListenerAdapter(new SysoLogListener());
			logReaderService.addLogListener(listener, SYSTEM_PROPERTY_LOG_FILTER);
			logListeners.add(listener);
			logReaderService.addLogListener(listener, new CommandProviderLogFilter());
			return;
		}
		for (ILogListenerDefinition logListenerDef : listenerDefs) {
			LogListener listener = logListenerDef.createLogListener();
			if (listener == null) {
				listener = new SysoLogListener();
			}
			if (logListenerDef.asSync()) {
				listener = new SynchronousLogListenerAdapter(listener);
			}
			logListeners.add(listener);
			LogFilter filter = logListenerDef.createLogFilter();
			if (filter == null) {
				filter = SYSTEM_PROPERTY_LOG_FILTER;
			}
			logReaderService.addLogListener(listener, filter);
		}
	}

	/**
	 * Unbind ExtendedLogReaderService
	 * 
	 * @param logReaderService
	 */
	public void unbind(ExtendedLogReaderService logReaderService) {
		for (LogListener logListener : logListeners) {
			LoggerMill.logReaderService.removeLogListener(logListener);
		}

		LoggerMill.logReaderService = null;
	}

	public void update(final ILogListenerDefinition[] listenerDefs) {
		this.listenerDefs = listenerDefs;
	}

	public void update(final ILogCatcherDefinition[] catcherDefs) {
		this.catcherDefs = catcherDefs;
	}

	/**
	 * initialize LogUtil
	 */
	private void init() {
		synchronized (initialized) {
			if (initialized.getAndSet(true)) {
				return;
			}

			// Experimental: capture platform logs
			new PlatformLogCatcher().attach();

			// get log catchers
			Inject.extension(ILogCatcherDefinition.EXTENSION_POINT).useType(ILogCatcherDefinition.class).into(this)
					.andStart(context);
			attachLogCapturer();

			// get log listeners
			Inject.extension(ILogListenerDefinition.EXTENSION_POINT).useType(ILogListenerDefinition.class).into(this)
					.andStart(context);

			Inject.service(ExtendedLogService.class).useRanking().into(this).andStart(context);
			Inject.service(ExtendedLogReaderService.class).useRanking().into(this).andStart(context);
		}
	}

	/**
	 * 
	 */
	private void attachLogCapturer() {
		if (catcherDefs == null) {
			return;
		}
		for (ILogCatcherDefinition catcherDef : catcherDefs) {
			ILogCatcher logCatcher = catcherDef.createLogCatcher();
			logCatcher.attach();
			logCatchers.add(logCatcher);
		}

	}
}
