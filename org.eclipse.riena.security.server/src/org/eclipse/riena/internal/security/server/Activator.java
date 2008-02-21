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
package org.eclipse.riena.internal.security.server;

import java.util.Hashtable;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.communication.core.hooks.IServiceHook;
import org.eclipse.riena.core.cache.GenericObjectCache;
import org.eclipse.riena.core.cache.IGenericObjectCache;
import org.eclipse.riena.core.logging.LogUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.compeople.scp.security.server";

	// The shared instance
	private static Activator plugin;
	private static BundleContext CONTEXT;
	private LogUtil logUtil;
	private ServiceRegistration principalCacheRegistration;
	private ServiceRegistration securityServiceHook;

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
		GenericObjectCache principalCache = new GenericObjectCache();
		principalCache.setName("principalCache");
		Hashtable<String, String> props = new Hashtable<String, String>();
		props.put("cache.type", "PrincipalCache");
		principalCacheRegistration = CONTEXT.registerService(IGenericObjectCache.ID, principalCache, props);
		securityServiceHook = CONTEXT.registerService(IServiceHook.ID, new SecurityServiceHook(), null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		principalCacheRegistration.unregister();
		securityServiceHook.unregister();
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