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

import org.osgi.service.log.LogService;

/**
 *
 */
public final class LogLevelMapper {

	private LogLevelMapper() {
		// utility
	}

	/**
	 * Return the log level value.
	 * 
	 * @param logLevelString
	 * @return the log level value as defined by <code>LogService</code>
	 */
	public static int getValue(String logLevelString) {
		if ("debug".equalsIgnoreCase(logLevelString)) { //$NON-NLS-1$
			return LogService.LOG_DEBUG;
		} else if ("info".equalsIgnoreCase(logLevelString)) { //$NON-NLS-1$
			return LogService.LOG_INFO;
		} else if ("warn".equalsIgnoreCase(logLevelString)) { //$NON-NLS-1$
			return LogService.LOG_WARNING;
		} else if ("error".equalsIgnoreCase(logLevelString)) { //$NON-NLS-1$
			return LogService.LOG_ERROR;
		} else if ("none".equalsIgnoreCase(logLevelString)) { //$NON-NLS-1$
			return LogService.LOG_ERROR - 1;
		}
		return LogService.LOG_WARNING;
	}

	/**
	 * Return the log level value.
	 * 
	 * @param logLevelString
	 * @return the log level value as defined by <code>LogService</code>
	 */
	public static String getValue(int logLevel) {
		switch (logLevel) {
		case LogService.LOG_DEBUG:
			return "debug";
		case LogService.LOG_INFO:
			return "info";
		case LogService.LOG_WARNING:
			return "warn";
		case LogService.LOG_ERROR:
			return "error";
		case LogService.LOG_ERROR - 1:
			return "none";
		default:
			return "?";
		}
	}

}
