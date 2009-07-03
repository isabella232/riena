/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.communication.publisher;

import org.osgi.framework.BundleContext;

import org.eclipse.riena.communication.core.publisher.IServicePublishBinder;
import org.eclipse.riena.communication.core.publisher.IServicePublisher;
import org.eclipse.riena.communication.publisher.Publish;
import org.eclipse.riena.communication.publisher.ServicePublishBinder;
import org.eclipse.riena.core.RienaActivator;
import org.eclipse.riena.core.injector.Inject;

public class Activator extends RienaActivator {

	//	private Logger logger;

	// The shared instance
	private static Activator plugin;

	/*
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);

		Activator.plugin = this;
		//		logger = getLogger(Activator.class.getName());

		IServicePublishBinder binder = new ServicePublishBinder();
		context.registerService(IServicePublishBinder.class.getName(), binder, null);
		Inject.service(IServicePublisher.class).into(binder).andStart(context);

		Publish.allServices().useFilter("(&(riena.remote=true)(riena.remote.protocol=*))").andStart(context); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
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
