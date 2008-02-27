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
package org.eclipse.riena.core;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.logging.LogUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Abstract base class for riena activators.<br>
 * It provides all descendant classes access to the bundle context and the
 * logger.<br>
 * <b>Note: </b>Derived Activators/Plugins must call <code>super.start()</code>
 * and <code>super.stop()</code> within their <code>start()</code> and
 * <code>stop()</stop> methods.
 */
public abstract class RienaActivator implements BundleActivator {

	// The shared instance
	private static BundleContext context;
	private LogUtil logUtil;

	/*
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		RienaActivator.context = context;
	}

	/*
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		RienaActivator.context = null;
	}

	/**
	 * Get the shared context.
	 * 
	 * @return
	 */
	public static BundleContext getContext() {
		return context;
	}

	/**
	 * Get a logger for the specified name.
	 * 
	 * @param name
	 * @return
	 */
	public synchronized Logger getLogger(String name) {
		if (logUtil == null)
			logUtil = new LogUtil(context);
		return logUtil.getLogger(name);
	}
}
