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

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.equinox.log.Logger;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

/**
 * The ConsoleLogger simply writes all logs to System.out/.err.<b> Therefore it
 * can be used when standard logging is not available or not usable, e.g. within
 * initializations of the Logger itself.
 */
public class ConsoleLogger implements Logger {

	/**
	 * The system property key to set the logging level for the console logger.
	 * Values should be integers corresponding to
	 * {@link org.osgi.service.log.LogService}.
	 */
	public static final String CONSOLELOGGER_LEVEL_SYSTEM_PROPERTY = "org.eclipse.riena.consoleloggerlevel"; //$NON-NLS-1$

	private String name;
	private static String nameAndHost;
	private static DateFormat formatter;

	static {
		String user = System.getProperty("user.name", "?"); //$NON-NLS-1$ //$NON-NLS-2$
		String host;
		try {
			host = Inet4Address.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			host = "?"; //$NON-NLS-1$
		}
		StringBuilder buffer = new StringBuilder();
		buffer.append(user).append('@').append(host);
		nameAndHost = buffer.toString();

		formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z"); //$NON-NLS-1$
	}

	public ConsoleLogger(String name) {
		this.name = name;
	}

	/*
	 * @see org.eclipse.equinox.log.Logger#getName()
	 */
	public String getName() {
		return name;
	}

	/*
	 * @see org.eclipse.equinox.log.Logger#isLoggable(int)
	 */
	public boolean isLoggable(int level) {
		return level <= Integer.getInteger(CONSOLELOGGER_LEVEL_SYSTEM_PROPERTY, LogService.LOG_WARNING);
	}

	/*
	 * @see org.eclipse.equinox.log.Logger#log(int, java.lang.String)
	 */
	public void log(int level, String message) {
		log(level, null, null, message, null);
	}

	/*
	 * @see org.eclipse.equinox.log.Logger#log(int, java.lang.String,
	 *      java.lang.Throwable)
	 */
	public void log(int level, String message, Throwable exception) {
		log(level, null, null, message, exception);
	}

	/*
	 * @see org.eclipse.equinox.log.Logger#log(org.osgi.framework.ServiceReference,
	 *      int, java.lang.String)
	 */
	public void log(ServiceReference sr, int level, String message) {
		log(level, null, sr, message, null);
	}

	/*
	 * @see org.eclipse.equinox.log.Logger#log(org.osgi.framework.ServiceReference,
	 *      int, java.lang.String, java.lang.Throwable)
	 */
	public void log(ServiceReference sr, int level, String message, Throwable exception) {
		log(level, null, null, message, exception);
	}

	/*
	 * @see org.eclipse.equinox.log.Logger#log(java.lang.Object, int,
	 *      java.lang.String)
	 */
	public void log(Object context, int level, String message) {
		log(level, context, null, message, null);
	}

	/*
	 * @see org.eclipse.equinox.log.Logger#log(java.lang.Object, int,
	 *      java.lang.String, java.lang.Throwable)
	 */
	public void log(Object context, int level, String message, Throwable exception) {
		log(level, context, null, message, exception);
	}

	private void log(int level, Object context, ServiceReference sr, String message, Throwable throwable) {
		StringBuilder bob = new StringBuilder();
		bob.append(formatter.format(new Date()));
		bob.append(' ');
		bob.append(nameAndHost);
		bob.append(' ');
		bob.append(getLevel(level));
		bob.append(' ');
		bob.append('[');
		bob.append(Thread.currentThread().getName());
		if (context != null) {
			bob.append(", CTX: "); //$NON-NLS-1$
			bob.append(context);
		}
		if (sr != null) {
			bob.append(", SR: "); //$NON-NLS-1$
			bob.append(sr);
		}
		bob.append("] "); //$NON-NLS-1$
		bob.append(name);
		bob.append(' ');
		bob.append(message);
		if (throwable != null) {
			StringWriter stringWriter = new StringWriter();
			PrintWriter writer = new PrintWriter(stringWriter);
			throwable.printStackTrace(writer);
			writer.close();
			bob.append('\n').append(stringWriter.toString());
		}
		PrintStream printStream = getPrintStream(level);
		printStream.println(bob.toString());
	}

	/**
	 * @param level
	 * @return
	 */
	private String getLevel(int level) {
		switch (level) {
		case LogService.LOG_DEBUG:
			return "DEBUG"; //$NON-NLS-1$
		case LogService.LOG_INFO:
			return "INFO"; //$NON-NLS-1$
		case LogService.LOG_WARNING:
			return "WARN"; //$NON-NLS-1$
		case LogService.LOG_ERROR:
			return "ERROR"; //$NON-NLS-1$
		default:
			return "UNKNOWN"; //$NON-NLS-1$
		}
	}

	private PrintStream getPrintStream(int level) {
		switch (level) {
		case LogService.LOG_DEBUG:
			return System.out;
		case LogService.LOG_INFO:
			return System.out;
		case LogService.LOG_WARNING:
			return System.err;
		case LogService.LOG_ERROR:
			return System.err;
		default:
			return System.out;
		}
	}
}
