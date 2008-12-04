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
package org.eclipse.riena.internal.ui.ridgets.swt.uiprocess;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Utility class for sharing of a common timer
 */
public class TimerUtil {

	private final static Timer timer = new Timer();

	public static void schedule(TimerTask task, int delay, int period) {
		timer.schedule(task, delay, period);
	}

	public static void stop(TimerTask task) {
		task.cancel();
	}

}
