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
package org.eclipse.riena.core.config;

import java.io.IOException;
import java.util.Hashtable;

import org.eclipse.riena.internal.core.config.ConfigProxy;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ManagedService;

/**
 * This class makes configuration a regular Java POJO easier. It creates a proxy
 * around a business Java POJO. All configuration that is sent by ConfigAdmin is
 * converted into setter calls.
 * 
 */
public class ConfigUtility {

	private BundleContext context;
	private ServiceRegistration configProxyReg;

	/**
	 * standard constructor
	 * 
	 * @param context
	 */
	public ConfigUtility(BundleContext context) {
		this.context = context;
	}

	/**
	 * create proxy for business Java POJO
	 * 
	 * @param bObject
	 * @param pid
	 */
	public void createConfigProxy(Object bObject, String pid) {
		ConfigProxy ms = new ConfigProxy(bObject);
		Hashtable<String, String> ht = new Hashtable<String, String>();
		ht.put(Constants.SERVICE_PID, pid);
		configProxyReg = context.registerService(ManagedService.class.getName(), ms, ht);
	}

	public void removeConfigProxy() {
		if (configProxyReg != null) {
			configProxyReg.unregister();
			configProxyReg = null;
		}
	}

	/**
	 * easy access to configuration for a SERVICE_PID
	 * 
	 * @param pid
	 * @return
	 */
	public Configuration getConfig(String pid) {
		ServiceReference ref = context.getServiceReference(ConfigurationAdmin.class.getName());
		ConfigurationAdmin cfgAdmin = (ConfigurationAdmin) context.getService(ref);
		// get config
		Configuration cfg;
		try {
			cfg = cfgAdmin.getConfiguration(pid);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return cfg;
	}
}