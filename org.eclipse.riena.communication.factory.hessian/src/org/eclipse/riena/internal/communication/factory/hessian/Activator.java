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
package org.eclipse.riena.internal.communication.factory.hessian;

import java.util.Hashtable;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.communication.core.factory.IRemoteServiceFactory;
import org.eclipse.riena.core.logging.LogUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.log.LogService;

public class Activator implements BundleActivator {

	private ServiceRegistration regFactory;
	private RemoteServiceFactoryHessian factory;
	private LogUtil logUtil;
	private Logger LOGGER = null;

	private static BundleContext CONTEXT;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		CONTEXT = context;
		LOGGER = getLogger(this.getClass().getName());
		LOGGER.log(LogService.LOG_INFO, "start hessian support on client");
		factory = new RemoteServiceFactoryHessian();
		Hashtable<String, Object> properties = new Hashtable<String, Object>(1);
		properties.put(IRemoteServiceFactory.PROP_PROTOCOL, factory.getProtocol());

		regFactory = context.registerService(IRemoteServiceFactory.ID, factory, properties);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		regFactory.unregister();
		regFactory = null;

		factory.dispose();
		CONTEXT = null;
		LOGGER.log(LogService.LOG_INFO, "stop hessian support on client");
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static BundleContext getContext() {
		return CONTEXT;
	}

	public synchronized Logger getLogger(String name) {
		if (logUtil == null) {
			logUtil = new LogUtil(CONTEXT);
		}
		return logUtil.getLogger(name);
	}

}
