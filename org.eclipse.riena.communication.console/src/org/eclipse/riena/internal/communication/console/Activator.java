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
package org.eclipse.riena.internal.communication.console;

import java.util.Hashtable;

import org.eclipse.osgi.framework.console.CommandProvider;
import org.eclipse.riena.communication.core.IRemoteServiceRegistry;
import org.eclipse.riena.communication.core.publisher.IServicePublishEventDispatcher;
import org.eclipse.riena.communication.core.publisher.RSDPublisherProperties;
import org.eclipse.riena.core.RienaActivator;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.service.ServiceInjector;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator extends RienaActivator {

	private ServiceRegistration consoleReg;
	private ServiceInjector publisherInjector;
	private ServiceInjector registryInjector;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		// register new OSGi command interpreter
		CommunicationConsole console = new CommunicationConsole();
		// the filter applies only if the service is living in this container
		// e.g. server.
		String filter = "(" + RSDPublisherProperties.PROP_IS_REMOTE + "=true)";
		publisherInjector = Inject.service(IServicePublishEventDispatcher.class.getName()).useRanking().useFilter(
				filter).into(console).andStart(context);
		registryInjector = Inject.service(IRemoteServiceRegistry.class.getName()).useRanking().into(console).andStart(
				context);

		consoleReg = context.registerService(CommandProvider.class.getName(), console, new Hashtable<String, String>());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		publisherInjector.stop();
		publisherInjector = null;
		registryInjector.stop();
		registryInjector = null;
		consoleReg.unregister();
		consoleReg = null;
		super.stop(context);
	}

}