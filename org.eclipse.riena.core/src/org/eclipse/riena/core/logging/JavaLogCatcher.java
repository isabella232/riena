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

import java.text.MessageFormat;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.eclipse.riena.internal.core.Activator;
import org.eclipse.riena.internal.core.ignore.Nop;
import org.osgi.service.log.LogService;

/**
 * The {@code JavaLogCatcher} can attach to the Java logging and route the log
 * events into Riena's logging.
 */
public class JavaLogCatcher implements ILogCatcher {

	private Handler javaLogginghandler;
	private Logger rootLogger;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.core.logging.ILogCatcher#attach()
	 */
	public void attach() {
		LogManager.getLogManager().reset();
		rootLogger = Logger.getLogger(""); //$NON-NLS-1$
		javaLogginghandler = new JavaLoggingHandler();
		rootLogger.addHandler(javaLogginghandler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.core.logging.ILogCatcher#detach()
	 */
	public void detach() {
		if (rootLogger == null || javaLogginghandler == null) {
			return;
		}
		rootLogger.removeHandler(javaLogginghandler);
	}

	/**
	 * Java logging handler that routes to the equinox logging.
	 */
	private static class JavaLoggingHandler extends Handler {

		@Override
		public void close() {
			Nop.reason("nothing to close here"); //$NON-NLS-1$
		}

		@Override
		public void flush() {
			Nop.reason("nothing to flush here"); //$NON-NLS-1$
		}

		@Override
		public void publish(LogRecord record) {

			// get equinox log level
			int equinoxLoglevel;
			int javaLoglevel = record.getLevel().intValue();
			if (javaLoglevel == Level.SEVERE.intValue()) {
				equinoxLoglevel = LogService.LOG_ERROR;
			} else if (javaLoglevel == Level.WARNING.intValue()) {
				equinoxLoglevel = LogService.LOG_WARNING;
			} else if (javaLoglevel == Level.INFO.intValue() || javaLoglevel == Level.CONFIG.intValue()) {
				equinoxLoglevel = LogService.LOG_INFO;
			} else {
				equinoxLoglevel = LogService.LOG_DEBUG;
			}

			// find equinox logger
			org.eclipse.equinox.log.Logger logger = Activator.getDefault().getLogger(record.getLoggerName());

			if (!logger.isLoggable(equinoxLoglevel)) {
				return;
			}
			String message = record.getMessage();
			Object[] params = record.getParameters();
			if (params != null) {
				message = MessageFormat.format(message, params);
			}

			logger.log(equinoxLoglevel, message, record.getThrown());
		}

	}

}
