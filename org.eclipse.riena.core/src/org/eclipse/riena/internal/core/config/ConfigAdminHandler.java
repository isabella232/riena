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
package org.eclipse.riena.internal.core.config;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.internal.core.Activator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.log.LogService;

public class ConfigAdminHandler {
	private Logger LOGGER = Activator.getDefault().getLogger(ConfigAdminHandler.class.getName());
	private BundleContext context;

	ConfigAdminHandler(BundleContext context) {
		super();
		this.context = context;
	}

	public void bind(ConfigurationAdmin cfgAdmin) {
		try {

			IExtensionRegistry registry = RegistryFactory.getRegistry();
			IExtension[] cfgExtensions = registry.getExtensionPoint("org.eclipse.riena.config.managedService").getExtensions();
			for (IExtension cfgExt : cfgExtensions) {
				Bundle bundle = Platform.getBundle(cfgExt.getNamespaceIdentifier());
				IConfigurationElement[] configs = cfgExt.getConfigurationElements();
				for (IConfigurationElement config : configs) {
					String servicepid = config.getAttribute("servicepid");
					String pluginid = config.getAttribute("pluginid");
					if (pluginid != null && pluginid.length() > 0) {
						bundle = Platform.getBundle(pluginid);
						if (bundle == null) {
							LOGGER.log(LogService.LOG_ERROR, "trying to configure managedService with invalid pluginid=" + pluginid
									+ ". Bundle for that plugin not found.");
							break;
						}
					}
					Configuration configuration = cfgAdmin.getConfiguration(servicepid, /* bundle.getLocation() */null);
					Dictionary dict = configuration.getProperties();
					if (dict == null) {
						dict = new Hashtable();
					}
					IConfigurationElement[] properties = config.getChildren();
					for (IConfigurationElement prop : properties) {
						String name = prop.getAttribute("name");
						String value = prop.getAttribute("value");
						if (name != null) {
							dict.put(name, value);
						} else {
							throw new RuntimeException("name or value are not specified for a property (servicepid=" + servicepid + ") (pluginid=" + pluginid
									+ ")");
						}
					}
					configuration.update(dict);

				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void unbind(ConfigurationAdmin cfgAdmin) {
		return;
	}

}
