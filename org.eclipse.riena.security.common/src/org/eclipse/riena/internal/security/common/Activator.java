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

import org.eclipse.core.runtime.Plugin;
import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.communication.core.hooks.ICallHook;
import org.eclipse.riena.core.logging.LogUtil;
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
public class Activator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.security.common";

	// The shared instance
	private static Activator plugin;
	private static BundleContext CONTEXT;
	private LogUtil logUtil;

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
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		CONTEXT = context;
		sessionHolderService = CONTEXT.registerService(ISessionHolderService.class.getName(),
				new SimpleSessionHolderService(), null);
		securityCallHook = CONTEXT.registerService(ICallHook.class.getName(), new SecurityCallHook(), null);
		principalHolderService = CONTEXT.registerService(ISubjectHolderService.class.getName(),
				new SubjectHolderService(), null);
		permissionCache = CONTEXT.registerService(IPermissionCache.class.getName(), new PermissionCache(), null);
		RienaPolicy.init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		sessionHolderService.unregister();
		securityCallHook.unregister();
		principalHolderService.unregister();
		plugin = null;
		CONTEXT = null;
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

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
