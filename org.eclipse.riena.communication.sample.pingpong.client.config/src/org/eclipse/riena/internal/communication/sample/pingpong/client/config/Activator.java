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
package org.eclipse.riena.internal.communication.sample.pingpong.client.config;

import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.factory.RemoteServiceFactory;
import org.eclipse.riena.communication.sample.pingpong.common.IPingPong;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * The Ping Pong sample shows to config a "remote" OSGi Services on the base of
 * service end point parameters.
 * 
 * This sample Activator registers manually the remote PingPong end point as
 * remote OSGi Service into the ServiceRegistry. This is reached by a simple way
 * using the Riena communication RemoteServiceFactory. #createAndRegisterProxy
 * creates a proxy references for the end point and registers the proxy
 * references as RemoteServiceReference into the Riena communication
 * IRemoteServiceRegistry OSGi Service. The IRemoteServiceRegistry itself
 * registers and manages the RemoteServiceReferences as remote OSGi service
 * within the ServiceRegistry.
 * 
 * @author Alexander Ziegler
 * @author Christian Campo
 * 
 */
public class Activator implements BundleActivator {

	private IRemoteServiceRegistration pingPongReg;
	private IRemoteServiceRegistration pingPongRegXFire;

	/**
	 * Creates a RemoteServiceReferences based on Hessian protocol and registers
	 * this as "remote" OSGi Service
	 */
	public void start(BundleContext context) throws Exception {
		// register hessian proxy for nyote remote service
		RemoteServiceFactory rsf = new RemoteServiceFactory();
		Class<?> serviceInterface = IPingPong.class;
		String url = "http://${riena.hostname}/hessian/PingPongWS"; //$NON-NLS-1$
		String protocol = "hessian"; //$NON-NLS-1$

		pingPongReg = rsf.createAndRegisterProxy(serviceInterface, url, protocol); //$NON-NLS-1$

		// register xfire proxy for nyote remote service
		// url = "http://${localhost}/xfire/PingPongXFireWS";
		// protocol = "xfire";

		// pingPongReg = rsf.createAndRegisterProxy(serviceInterface, url,
		// protocol);

	}

	/**
	 * unregister end release the "remote" OSGi Service
	 */
	public void stop(BundleContext context) throws Exception {
		if (pingPongReg != null) {
			pingPongReg.unregister();
			pingPongReg = null;
		}
	}

}
