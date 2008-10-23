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
package org.eclipse.riena.internal.navigation;

import org.eclipse.riena.core.RienaConstants;
import org.eclipse.riena.core.RienaPlugin;
import org.eclipse.riena.navigation.INavigationNodeProvider;
import org.eclipse.riena.navigation.ISubModuleViewBuilder;
import org.eclipse.riena.navigation.model.NavigationNodeProvider;
import org.eclipse.riena.navigation.model.SubModuleViewBuilder;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends RienaPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.navigation"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	private INavigationNodeProvider service1 = null;
	private ISubModuleViewBuilder service2 = null;

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
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		Activator.plugin = this;

		service1 = new NavigationNodeProvider();
		service2 = new SubModuleViewBuilder();

		context.registerService(INavigationNodeProvider.class.getName(), service1, RienaConstants
				.newDefaultServiceProperties());
		context.registerService(ISubModuleViewBuilder.class.getName(), service2, RienaConstants
				.newDefaultServiceProperties());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		Activator.plugin = null;
		if (service1 != null) {
			service1.cleanUp();
			service1 = null;
		}
		if (service2 != null) {
			service2.cleanUp();
			service2 = null;
		}
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
