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
package org.eclipse.riena.internal.core;

import java.util.Hashtable;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.RienaPlugin;
import org.eclipse.riena.core.RienaStartupStatus;
import org.eclipse.riena.internal.core.config.ConfigFromExtensions;
import org.eclipse.riena.internal.core.config.ConfigSymbolReplace;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationPlugin;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.log.LogService;

public class Activator extends RienaPlugin {

	/**
	 * 
	 */
	public static final String RIENA_FORCE_START = "Riena-ForceStart"; //$NON-NLS-1$

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.core"; //$NON-NLS-1$

	private ServiceRegistration configSymbolReplace;
	private ServiceRegistration configurationPlugin;

	// The shared instance
	private static Activator plugin;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		Activator.plugin = this;
		final Logger LOGGER = getLogger(Activator.class.getName());

		startForcedRienaBundles(context, LOGGER);

		// register ConfigSymbolReplace that replaces symbols in config strings
		ConfigSymbolReplace csr = new ConfigSymbolReplace();
		Hashtable<String, String> ht = new Hashtable<String, String>();
		ht.put(Constants.SERVICE_PID, "org.eclipse.riena.config.symbols");
		// register as configurable osgi service
		configSymbolReplace = context.registerService(ManagedService.class.getName(), csr, ht);
		// register as config admin configuration plugin
		configurationPlugin = context.registerService(ConfigurationPlugin.class.getName(), csr, null);
		// execute the class that reads through the extensions and executes them
		// as config admin packages
		new ConfigFromExtensions(context).doConfig();
		((RienaStartupStatusSetter) RienaStartupStatus.getInstance()).setStarted(true);
	}

	/**
	 * Force starting of bundles that are marked true with the
	 * <code>RIENA_FORCE_START</code> bundle header.
	 * 
	 * @param context
	 * @param LOGGER
	 * @throws BundleException
	 */
	private void startForcedRienaBundles(BundleContext context, final Logger LOGGER) throws BundleException {
		Bundle[] bundles = context.getBundles();
		for (Bundle bundle : bundles) {
			boolean forceStart = Boolean.parseBoolean((String) bundle.getHeaders().get(RIENA_FORCE_START));
			if (bundle.getState() != Bundle.ACTIVE
					&& (bundle.getSymbolicName().equals("org.eclipse.equinox.cm") || bundle.getSymbolicName().equals(
							"org.eclipse.equinox.log"))) {
				forceStart = true;
			}
			if (!forceStart)
				continue;

			// TODO STARTING == LAZY, so start that also, STARTING is
			// disabled, bundles with forceStart should not be LAZY
			if (bundle.getState() == Bundle.RESOLVED/*
													 * || bundle.getState() ==
													 * Bundle.STARTING
													 */) {
				bundle.start();
				LOGGER.log(LogService.LOG_INFO, bundle.getSymbolicName() + " forced autostart successfully");
			} else if (bundle.getState() == Bundle.INSTALLED) {
				LOGGER.log(LogService.LOG_ERROR, bundle.getSymbolicName() + " has " + RIENA_FORCE_START
						+ " but is only in state INSTALLED (not RESOLVED).");
			} else if (bundle.getState() == Bundle.ACTIVE) {
				LOGGER.log(LogService.LOG_DEBUG, bundle.getSymbolicName()
						+ " no forced autostart. Bundle is already ACTIVE.");
			}
		}
	}

	/*
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		((RienaStartupStatusSetter) RienaStartupStatus.getInstance()).setStarted(false);
		configSymbolReplace.unregister();
		configurationPlugin.unregister();
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
