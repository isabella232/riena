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
package org.eclipse.riena.internal.security.common;

import org.eclipse.riena.communication.core.hooks.ICallHook;
import org.eclipse.riena.core.RienaPlugin;
import org.eclipse.riena.security.common.ISubjectHolderService;
import org.eclipse.riena.security.common.authorization.IPermissionCache;
import org.eclipse.riena.security.common.authorization.RienaPolicy;
import org.eclipse.riena.security.common.authorization.internal.PermissionCache;
import org.eclipse.riena.security.common.session.ISessionHolderService;
import org.eclipse.riena.security.common.session.internal.SimpleSessionHolderService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends RienaPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.security.common";

	private ServiceRegistration sessionHolderService;
	private ServiceRegistration securityCallHook;
	private ServiceRegistration principalHolderService;
	private ServiceRegistration permissionCache;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		sessionHolderService = context.registerService(ISessionHolderService.class.getName(),
				new SimpleSessionHolderService(), null);
		securityCallHook = context.registerService(ICallHook.class.getName(), new SecurityCallHook(), null);
		principalHolderService = context.registerService(ISubjectHolderService.class.getName(),
				new SubjectHolderService(), null);
		permissionCache = context.registerService(IPermissionCache.class.getName(), new PermissionCache(), null);
		RienaPolicy.init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		sessionHolderService.unregister();
		securityCallHook.unregister();
		principalHolderService.unregister();
		super.stop(context);
	}

}
