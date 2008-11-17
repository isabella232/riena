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
package org.eclipse.riena.internal.navigation.ui.swt;

import org.eclipse.riena.core.RienaConstants;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.navigation.ui.swt.workarea.SwtExtensionWorkareaDefinitionRegistry;
import org.eclipse.riena.navigation.INavigationAssemblyExtension;
import org.eclipse.riena.ui.swt.AbstractRienaUIPlugin;
import org.eclipse.riena.workarea.spi.IWorkareaDefinitionRegistry;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractRienaUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.navigation.ui.swt"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	/**
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {

		super.start(context);
		plugin = this;

		SwtExtensionWorkareaDefinitionRegistry registry = new SwtExtensionWorkareaDefinitionRegistry();
		context.registerService(IWorkareaDefinitionRegistry.class.getName(), registry, RienaConstants
				.newDefaultServiceProperties());
		Inject.extension(INavigationAssemblyExtension.EXTENSIONPOINT).useType(INavigationAssemblyExtension.class).into(
				registry).andStart(Activator.getDefault().getBundle().getBundleContext());
	}

	/**
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
