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
package org.eclipse.riena.internal.example.client;

import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.factory.ProxyAlreadyRegisteredFailure;
import org.eclipse.riena.communication.core.factory.RemoteServiceFactory;
import org.eclipse.riena.core.RienaPlugin;
import org.eclipse.riena.monitor.common.IReceiver;
import org.eclipse.riena.security.common.authentication.IAuthenticationService;

import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends RienaPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.example.client"; //$NON-NLS-1$
	private static final String PROTOCOL_HESSIAN = "hessian"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private IRemoteServiceRegistration collectibleReceiverReg;
	private IRemoteServiceRegistration authenticationService;

	/**
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		collectibleReceiverReg = new RemoteServiceFactory().createAndRegisterProxy(IReceiver.class,
				"http://localhost:8080/hessian/CollectibleReceiverWS", PROTOCOL_HESSIAN, context); //$NON-NLS-1$
		try {
			authenticationService = new RemoteServiceFactory().createAndRegisterProxy(IAuthenticationService.class,
					"http://localhost:8080/hessian/AuthenticationService", PROTOCOL_HESSIAN, context); //$NON-NLS-1$
		} catch (ProxyAlreadyRegisteredFailure e) {
			// do nothing, can happen if some other bundle registered this service
		}
	}

	/**
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		collectibleReceiverReg.unregister();
		authenticationService.unregister();
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
