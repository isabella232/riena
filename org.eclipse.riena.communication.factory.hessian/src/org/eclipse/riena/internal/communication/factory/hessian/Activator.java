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
import org.eclipse.riena.core.RienaActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.log.LogService;

public class Activator extends RienaActivator {

	private ServiceRegistration regFactory;
	private RemoteServiceFactoryHessian factory;
	private Logger logger = null;

	private static Activator plugin;

	/*
	 * @see org.eclipse.riena.core.RienaActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		logger = getLogger(this.getClass().getName());
		logger.log(LogService.LOG_INFO, "start hessian support on client");
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
		logger.log(LogService.LOG_INFO, "stop hessian support on client");
		plugin = null;
		super.stop(context);
	}

	public static Activator getDefault() {
		return plugin;
	}

}
