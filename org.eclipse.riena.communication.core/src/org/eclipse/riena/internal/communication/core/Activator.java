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
package org.eclipse.riena.internal.communication.core;

import java.util.Hashtable;

import org.eclipse.riena.communication.core.IRemoteServiceRegistry;
import org.eclipse.riena.communication.core.progressmonitor.IRemoteProgressMonitorRegistry;
import org.eclipse.riena.communication.core.progressmonitor.ProgressMonitorRegistryImpl;
import org.eclipse.riena.communication.core.ssl.ISSLProperties;
import org.eclipse.riena.communication.core.ssl.SSLConfiguration;
import org.eclipse.riena.core.RienaActivator;
import org.eclipse.riena.core.RienaConstants;
import org.eclipse.riena.core.extension.ExtensionInjector;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.communication.core.registry.RemoteServiceRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

/**
 * @author Alexander Ziegler
 * @author Christian Campo
 * 
 */
public class Activator extends RienaActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.communication.core"; //$NON-NLS-1$

	private RemoteServiceRegistry serviceRegistry;
	private ServiceRegistration regServiceRegistry;

	private ServiceReference sslConfigServiceReference;
	private ExtensionInjector sslInjector;

	// The shared instance
	private static Activator plugin;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		Activator.plugin = this;
		serviceRegistry = new RemoteServiceRegistry();
		serviceRegistry.start();

		Hashtable<String, Object> properties = RienaConstants.newDefaultServiceProperties();
		regServiceRegistry = context.registerService(IRemoteServiceRegistry.class.getName(), serviceRegistry,
				properties);

		// SSL configuration
		context.registerService(SSLConfiguration.class.getName(), new SSLConfiguration(), RienaConstants
				.newDefaultServiceProperties());
		sslConfigServiceReference = context.getServiceReference(SSLConfiguration.class.getName());
		SSLConfiguration config = (SSLConfiguration) context.getService(sslConfigServiceReference);
		if (config != null) {
			sslInjector = Inject.extension(ISSLProperties.EXTENSION_POINT_ID).expectingMinMax(0, 1).into(config)
					.update("configure"); //$NON-NLS-1$
			sslInjector.andStart(context);
		}

		context
				.registerService(IRemoteProgressMonitorRegistry.class.getName(), new ProgressMonitorRegistryImpl(),
						null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if (sslInjector != null) {
			sslInjector.stop();
		}
		if (sslConfigServiceReference != null) {
			context.ungetService(sslConfigServiceReference);
		}

		regServiceRegistry.unregister();
		regServiceRegistry = null;

		serviceRegistry.stop();
		serviceRegistry = null;

		Activator.plugin = null;
		super.stop(context);
	}

	/**
	 * Get the plugin instance.
	 * 
	 * @return
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
