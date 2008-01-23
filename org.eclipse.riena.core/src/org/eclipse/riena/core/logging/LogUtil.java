/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.logging;

import org.eclipse.equinox.log.ExtendedLogReaderService;
import org.eclipse.equinox.log.ExtendedLogService;
import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.internal.core.Activator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

/**
 * Wrapper to access the existing Logger.
 */
public class LogUtil {

	private ServiceReference logServiceReference;
	private ExtendedLogService logService;
	private ServiceReference logReaderServiceReference;
	private ExtendedLogReaderService logReaderService;
	private boolean initialized = false;
	private BundleContext context;

	public LogUtil(BundleContext context) {
		this.context = context;
	}

	public Logger getLogger(String name) {
		if (!initialized) {
			init();
			initialized = true;
		}
		if (logService != null) {
			return logService.getLogger(name);
		} else {
			return new NullLogger();
		}

	}

	public void init() {
		logServiceReference = context.getServiceReference(ExtendedLogService.class.getName());
		if (logServiceReference != null) {
			logService = (ExtendedLogService) Activator.getDefault().getContext().getService(logServiceReference);
		}

		ServiceListener logServiceListener = new ServiceListener() {

			public void serviceChanged(ServiceEvent event) {
				if (event.getType() == ServiceEvent.REGISTERED) {
					logServiceReference = event.getServiceReference();
					logService = (ExtendedLogService) Activator.getDefault().getContext().getService(logServiceReference);
					// getLogger("Campo").log(LogService.LOG_INFO, "Stefan");
				} else if (event.getType() == ServiceEvent.UNREGISTERING) {
					context.ungetService(logServiceReference);
					logServiceReference = null;
					logService = null;
				}
			}

		};

		logReaderServiceReference = context.getServiceReference(ExtendedLogReaderService.class.getName());
		if (logReaderServiceReference != null) {
			logReaderService = (ExtendedLogReaderService) Activator.getDefault().getContext().getService(logReaderServiceReference);
		}

		try {
			context.addServiceListener(logServiceListener, "(objectClass=" + ExtendedLogService.class.getName() + ")");
		} catch (InvalidSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ServiceListener logReaderServiceListener = new ServiceListener() {

			public void serviceChanged(ServiceEvent event) {
				if (event.getType() == ServiceEvent.REGISTERED) {
					logReaderServiceReference = event.getServiceReference();
					logReaderService = (ExtendedLogReaderService) Activator.getDefault().getContext().getService(logReaderServiceReference);
				} else if (event.getType() == ServiceEvent.UNREGISTERING) {
					context.ungetService(logReaderServiceReference);
					logReaderServiceReference = null;
					logReaderService = null;
				}
			}

		};
		if (logReaderService != null) {
			logReaderService.addLogListener(new SysoLogListener(), new AlwaysFilter());
			logReaderService.addLogListener(new Log4jLogListener(), new AlwaysFilter());
		}

		try {
			context.addServiceListener(logReaderServiceListener, "(objectClass=" + ExtendedLogReaderService.class.getName() + ")");
		} catch (InvalidSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
