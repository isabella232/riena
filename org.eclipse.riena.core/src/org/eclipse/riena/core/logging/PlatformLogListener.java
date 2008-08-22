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

import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.riena.internal.core.Activator;
import org.osgi.service.log.LogService;

/**
 * The <code>PlatformLogListener</code> can attach to the <code>Platform</code>
 * log and route platform log events into Riena's logging.
 */
public class PlatformLogListener implements ILogListener {

	/**
	 * Attach to the <code>Platform</code> log.
	 */
	public void attach() {
		Platform.addLogListener(this);
	}

	/**
	 * Detach from the <code>Platform</code> log.
	 */
	public void detach() {
		Platform.removeLogListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.ILogListener#logging(org.eclipse.core.runtime
	 * .IStatus, java.lang.String)
	 */
	public void logging(IStatus status, String plugin) {
		int logLevel;
		switch (status.getSeverity()) {
		case IStatus.ERROR:
			logLevel = LogService.LOG_ERROR;
			break;
		case IStatus.WARNING:
			logLevel = LogService.LOG_WARNING;
			break;
		case IStatus.INFO:
			logLevel = LogService.LOG_INFO;
			break;
		case IStatus.OK:
			logLevel = LogService.LOG_INFO;
			break;
		case IStatus.CANCEL:
			logLevel = LogService.LOG_DEBUG;
			break;
		default:
			logLevel = LogService.LOG_DEBUG;
			break;
		}
		if (Activator.getDefault() == null) {
			return;
		}
		Activator.getDefault().getLogger("Bundle " + plugin).log(logLevel, status.getMessage(), status.getException()); //$NON-NLS-1$
	}

}
