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
package org.eclipse.riena.internal.communication.console;

import java.util.Hashtable;

import org.eclipse.osgi.framework.console.CommandProvider;
import org.eclipse.riena.communication.core.IRemoteServiceRegistry;
import org.eclipse.riena.communication.core.publisher.IServicePublishBinder;
import org.eclipse.riena.core.RienaActivator;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.service.ServiceInjector;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator extends RienaActivator {

	private ServiceRegistration consoleReg;
	private ServiceInjector publisherInjector;
	private ServiceInjector registryInjector;

	// The shared instance
	private static Activator plugin;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		Activator.plugin = this;
		// register new OSGi command interpreter
		CommunicationConsole console = new CommunicationConsole();
		// the filter applies only if the service is living in this container
		// e.g. server.
		// String filter = "(" + RSDPublisherProperties.PROP_IS_REMOTE +
		// "=true)";
		publisherInjector = Inject.service(IServicePublishBinder.class.getName()).useRanking().into(console).andStart(
				context);
		registryInjector = Inject.service(IRemoteServiceRegistry.class.getName()).useRanking().into(console).andStart(
				context);

		consoleReg = context.registerService(CommandProvider.class.getName(), console, new Hashtable<String, String>());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		publisherInjector.stop();
		publisherInjector = null;
		registryInjector.stop();
		registryInjector = null;
		consoleReg.unregister();
		consoleReg = null;
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
