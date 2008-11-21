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
package org.eclipse.riena.monitor.common;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Date;

import org.eclipse.equinox.log.ExtendedLogEntry;
import org.eclipse.riena.core.util.IOUtils;

/**
 *
 */
public class LogEntryTransferObject implements Serializable {

	private String bundleName;
	private String context;
	private String exception;
	private int level;
	private String loggerName;
	private String message;
	private String threadName;
	private long time;

	private static final long serialVersionUID = 189322808250966598L;

	public LogEntryTransferObject() {
		// just4Hessian
	}

	public LogEntryTransferObject(ExtendedLogEntry logEntry) {
		if (logEntry.getBundle() != null) {
			this.bundleName = logEntry.getBundle().getSymbolicName();
		}
		if (logEntry.getContext() != null) {
			this.context = logEntry.getContext().toString();
		}
		if (logEntry.getException() != null) {
			StringWriter string = new StringWriter();
			PrintWriter writer = new PrintWriter(string);
			logEntry.getException().printStackTrace(writer);
			IOUtils.close(writer);
			this.exception = string.toString();
		}
		this.level = logEntry.getLevel();
		this.loggerName = logEntry.getLoggerName();
		this.message = logEntry.getMessage();
		this.threadName = logEntry.getThreadName();
		this.time = logEntry.getTime();
	}

	/**
	 * @return the bundleName
	 */
	public String getBundleName() {
		return bundleName;
	}

	/**
	 * @return the context
	 */
	public String getContext() {
		return context;
	}

	/**
	 * @return the exception
	 */
	public String getException() {
		return exception;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return the loggerName
	 */
	public String getLoggerName() {
		return loggerName;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the threadName
	 */
	public String getThreadName() {
		return threadName;
	}

	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new Date(time) + " Level: " + level + " [" + threadName + "] " + message
				+ (exception == null ? "" : "\n" + exception);
	}

}
