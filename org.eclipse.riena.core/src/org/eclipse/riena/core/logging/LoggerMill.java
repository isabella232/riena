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
 * The {@code LoggerMill} is responsible for delivering ready to use {@code
 * Logger} instances and also for the configuration of the riena logging.<br>
 * For the curious: There are so many LoggerFactories out there. So, not another
 * one!
 */
public class LoggerMill {

	/**
	 * 
	 */
	public static final String RIENA_DEFAULT_LOGGING = "riena.defaultlogging"; //$NON-NLS-1$

	private BundleContext context;
	private List<LogListener> logListeners = new ArrayList<LogListener>();
	private List<ILogCatcher> logCatchers = new ArrayList<ILogCatcher>();;
	private ILogListenerDefinition[] listenerDefs;
	private ILogCatcherDefinition[] catcherDefs;

	private static ExtendedLogService logService;
	private static ExtendedLogReaderService logReaderService;
	private static AtomicBoolean initialized = new AtomicBoolean(false);

	private static final LogFilter COMMAND_PROVIDER_LOG_FILTER = new CommandProviderLogFilter();

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
		if (listenerDefs.length == 0 && Boolean.getBoolean(RIENA_DEFAULT_LOGGING)) {
			listenerDefs = new ILogListenerDefinition[] { new SysoLogListenerDefinition() };
		}
		for (ILogListenerDefinition logListenerDef : listenerDefs) {
			LogListener listener = logListenerDef.createLogListener();
			if (listener == null) {
				listener = new SysoLogListener();
			}
			if (logListenerDef.isSynchronous()) {
				listener = new SynchronousLogListenerAdapter(listener);
			}
			logListeners.add(listener);
			LogFilter filter = logListenerDef.createLogFilter();
			if (filter == null) {
				logReaderService.addLogListener(listener);
			} else {
				logReaderService.addLogListener(listener, filter);
			}
		}
		if (catcherDefs.length == 0 && Boolean.getBoolean(RIENA_DEFAULT_LOGGING)) {
			catcherDefs = new ILogCatcherDefinition[] { new PlatformLogCatcherDefinition(),
					new LogServiceLogCatcherDefinition() };
		}
		for (ILogCatcherDefinition catcherDef : catcherDefs) {
			ILogCatcher logCatcher = catcherDef.createLogCatcher();
			logCatcher.attach();
			logCatchers.add(logCatcher);
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

		for (ILogCatcher logCatcher : logCatchers) {
			logCatcher.detach();
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

			// get log catchers
			Inject.extension(ILogCatcherDefinition.EXTENSION_POINT).useType(ILogCatcherDefinition.class).into(this)
					.andStart(context);

			// get log listeners
			Inject.extension(ILogListenerDefinition.EXTENSION_POINT).useType(ILogListenerDefinition.class).into(this)
					.andStart(context);

			Inject.service(ExtendedLogService.class).useRanking().into(this).andStart(context);
			Inject.service(ExtendedLogReaderService.class).useRanking().into(this).andStart(context);
		}
	}

	private static final class SysoLogListenerDefinition implements ILogListenerDefinition {
		public boolean isSynchronous() {
			return true;
		}

		public LogFilter createLogFilter() {
			return COMMAND_PROVIDER_LOG_FILTER;
		}

		public LogListener createLogListener() {
			return new SysoLogListener();
		}

		public String getName() {
			return "DefaultLogListner"; //$NON-NLS-1$
		}
	}

	private final static class PlatformLogCatcherDefinition implements ILogCatcherDefinition {

		public ILogCatcher createLogCatcher() {
			return new PlatformLogCatcher();
		}

		public String getName() {
			return "DefaultPlatformLogCatcher"; //$NON-NLS-1$
		}
	}

	private class LogServiceLogCatcherDefinition implements ILogCatcherDefinition {

		/*
		 * @seeorg.eclipse.riena.internal.core.logging.ILogCatcherDefinition#
		 * createLogCatcher()
		 */
		public ILogCatcher createLogCatcher() {
			return new LogServiceLogCatcher();
		}

		public String getName() {
			return "DefaultLogServiceLogCatcher"; //$NON-NLS-1$
		}

	}

}
