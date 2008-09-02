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

import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;

/**
 *
 */
public class SynchronousLogListenerAdapter implements LogListener {

	private final LogListener logListener;

	public SynchronousLogListenerAdapter(final LogListener logListener) {
		this.logListener = logListener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.service.log.LogListener#logged(org.osgi.service.log.LogEntry)
	 */
	public void logged(final LogEntry entry) {
		logListener.logged(entry);
	}

}
