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
package org.eclipse.riena.internal.core.logging;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.equinox.log.ExtendedLogReaderService;
import org.eclipse.equinox.log.ExtendedLogService;
import org.eclipse.equinox.log.LogFilter;
import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.logging.CommandProviderLogFilter;
import org.eclipse.riena.core.logging.ILogCatcher;
import org.eclipse.riena.core.logging.LogServiceLogCatcher;
import org.eclipse.riena.core.logging.PlatformLogCatcher;
import org.eclipse.riena.core.logging.SysoLogListener;
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
	 * When set to true and no loggers have been defined than a default logging
	 * setup will be used.
	 */
	public static final String RIENA_DEFAULT_LOGGING = "riena.defaultlogging"; //$NON-NLS-1$

	private List<LogListener> logListeners = new ArrayList<LogListener>();
	private List<ILogCatcher> logCatchers = new ArrayList<ILogCatcher>();;
	private ILogListenerDefinition[] listenerDefs;
	private ILogCatcherDefinition[] catcherDefs;

	private static ExtendedLogReaderService logReaderService;
	private final static AtomicReference<ExtendedLogService> logService = new AtomicReference<ExtendedLogService>(null);

	/**
	 * Create the logger mill.
	 * 
	 * @param context
	 */
	public LoggerMill(final BundleContext context) {
		this(context, true);
	}

	/**
	 * Configuration constructor.
	 * 
	 * @param context
	 * @param autoConfig
	 */
	protected LoggerMill(final BundleContext context, final boolean autoConfig) {
		// get log catchers
		Inject.extension(ILogCatcherDefinition.EXTENSION_POINT).useType(ILogCatcherDefinition.class).into(this)
				.andStart(context);

		// get log listeners
		Inject.extension(ILogListenerDefinition.EXTENSION_POINT).useType(ILogListenerDefinition.class).into(this)
				.andStart(context);

		Inject.service(ExtendedLogReaderService.class).useRanking().into(this).andStart(context);
		Inject.service(ExtendedLogService.class).useRanking().into(this).andStart(context);
	}

	/**
	 * Get the logger for the specified category.<br>
	 * <b>Note:</b> Use the log levels defined in
	 * {@link org.osgi.service.log.LogService}
	 * 
	 * @param category
	 *            logger name
	 * @return
	 */
	public Logger getLogger(String category) {
		return isReady() ? logService.get().getLogger(category) : null;
	}

	/**
	 * Bind ExtendedLogService
	 * 
	 * @param logService
	 */
	public void bind(ExtendedLogService logService) {
		LoggerMill.logService.set(logService);
	}

	/**
	 * Unbind ExtendedLogService
	 * 
	 * @param logService
	 */
	public void unbind(ExtendedLogService logService) {
		LoggerMill.logService.set(null);
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
				// FIXME error handling
				continue;
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

	public boolean isReady() {
		return logService.get() != null;
	}

	/**
	 * Definition of log listener that defines a {@code SysoLogListener} with a
	 * {@code CommandProviderLogFilter}.
	 */
	private static final class SysoLogListenerDefinition implements ILogListenerDefinition {
		public boolean isSynchronous() {
			return true;
		}

		public LogFilter createLogFilter() {
			return new CommandProviderLogFilter();
		}

		public LogListener createLogListener() {
			return new SysoLogListener();
		}

		public String getName() {
			return "DefaultLogListner"; //$NON-NLS-1$
		}
	}

	/**
	 * Definition of log catcher that defines a {@code PlatformLogCatcher}.
	 */
	private final static class PlatformLogCatcherDefinition implements ILogCatcherDefinition {

		public ILogCatcher createLogCatcher() {
			return new PlatformLogCatcher();
		}

		public String getName() {
			return "DefaultPlatformLogCatcher"; //$NON-NLS-1$
		}
	}

	/**
	 * Definition of log catcher that defines a {@code LogServiceLogCatcher}.
	 */
	private class LogServiceLogCatcherDefinition implements ILogCatcherDefinition {

		public ILogCatcher createLogCatcher() {
			return new LogServiceLogCatcher();
		}

		public String getName() {
			return "DefaultLogServiceLogCatcher"; //$NON-NLS-1$
		}
	}

}
