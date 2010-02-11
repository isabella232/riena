/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.ui.ridgets.swt.uiprocess;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Utility class for sharing of a common timer. We don´t want to have a big
 * number of worker threads.
 */
public final class TimerUtil {

	/**
	 * No public constructor for this utility class.
	 */
	private TimerUtil() {
		super();
	}

	// the common timer instance
	private final static Timer TIMER = new Timer();

	public static void schedule(TimerTask task, int delay, int period) {
		// the actual task of work
		TIMER.schedule(task, delay, period);
	}

	public static void stop(TimerTask task) {
		// stop a specific task
		task.cancel();
	}

}
