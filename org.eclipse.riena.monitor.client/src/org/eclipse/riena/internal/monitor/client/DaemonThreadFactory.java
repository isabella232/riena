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
package org.eclipse.riena.internal.monitor.client;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Provides daemon thread factories.
 */
public class DaemonThreadFactory implements ThreadFactory {

	private ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();

	public Thread newThread(Runnable r) {
		Thread newThread = defaultThreadFactory.newThread(r);
		newThread.setDaemon(true);
		return newThread;
	}

}
