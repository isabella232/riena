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

import java.lang.reflect.Method;

/**
 * All properties a defered log event.
 */
public class DeferredLogEvent {

	private final String loggerName;
	private final long time;
	private final String threadName;
	private final Method log;
	private final Object[] args;

	public DeferredLogEvent(final String loggerName, final long time, final String threadName, final Method log,
			final Object[] args) {
		this.loggerName = loggerName;
		this.time = time;
		this.threadName = threadName;
		this.log = log;
		this.args = args;
	}

	/**
	 * @return the loggerName
	 */
	public String getLoggerName() {
		return loggerName;
	}

	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @return the threadName
	 */
	public String getThreadName() {
		return threadName;
	}

	/**
	 * @return the log
	 */
	public Method getLogMethod() {
		return log;
	}

	/**
	 * @return the args
	 */
	public Object[] getArgs() {
		return args;
	}
}
