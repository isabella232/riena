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

import org.eclipse.riena.core.service.ServiceId;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationAdmin;

/**
 * This class reads through the extensions of extension-point
 * "org.eclipse.riena.config.managedService" and creates configuration objects
 * for the properties in there and passes them to ConfigAdmin.
 * 
 * doConfig is normally only called once during initialization.
 * 
 */
public class ConfigFromExtensions {

	private BundleContext context;

	/**
	 * Standard constructor
	 * 
	 * @param context
	 */
	public ConfigFromExtensions(BundleContext context) {
		this.context = context;
	}

	/**
	 * This method is called once and transforms extensions in configuration as
	 * described in the classes javadoc.
	 */
	public void doConfig() {
		new ServiceId(ConfigurationAdmin.class.getName()).injectInto(new ConfigAdminHandler(context)).andStart(context);
	}
}