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
package org.eclipse.riena.core.util;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.logging.ConsoleLogger;
import org.osgi.service.log.LogService;

/**
 *
 */
public class StopWatch {

	private long started;
	private final long created;
	private final String name;
	private final Logger logger;

	public StopWatch(String name) {
		this.name = name;
		this.logger = new ConsoleLogger("StopWatch");
		this.created = System.currentTimeMillis();
	}

	public StopWatch(String name, Logger logger) {
		this.name = name;
		this.logger = logger;
		created = System.currentTimeMillis();
	}

	public StopWatch start() {
		logger.log(LogService.LOG_DEBUG, name + "(started)");
		started = System.currentTimeMillis();
		return this;
	}

	public StopWatch stop() {
		long now = System.currentTimeMillis();
		logger.log(LogService.LOG_DEBUG, name + "(stopped): " + (now - created) + " ms");
		return reset();
	}

	public StopWatch elapsed(String mark) {
		long now = System.currentTimeMillis();
		logger.log(LogService.LOG_DEBUG, name + "(" + mark + "): " + (now - started) + " ms");
		return this;
	}

	public StopWatch reset() {
		started = System.currentTimeMillis();
		return this;
	}
}
