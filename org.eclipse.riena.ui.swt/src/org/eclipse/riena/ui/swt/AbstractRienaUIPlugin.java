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
package org.eclipse.riena.ui.swt;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.logging.LoggerProvider;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * Abstract base class for riena UI plugins.<br>
 * It provides all descendant classes access to the bundle context and the
 * logger.<br>
 * <b>Note: </b>Derived plugins must call <code>super.start()</code> and
 * <code>super.stop()</code> within their <code>start()</code> and
 * <code>stop()</stop> methods.
 */
public abstract class AbstractRienaUIPlugin extends AbstractUIPlugin {

	private BundleContext context;
	private final LoggerProvider loggerProvider = new LoggerProvider();

	/*
	 * @see
	 * org.eclipse.core.runtime.Plugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		this.context = context;
		loggerProvider.start();
	}

	/*
	 * @see
	 * org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		this.context = null;
		loggerProvider.stop();
		super.stop(context);
	}

	/**
	 * Get the shared context.
	 * 
	 * @return
	 */
	public BundleContext getContext() {
		return context;
	}

	/**
	 * Get a logger for the specified name.<br>
	 * <b>Hint:</b>The log levels are defined in <code>LogService</code>.
	 * 
	 * @param name
	 * @return the logger
	 */
	public Logger getLogger(String name) {
		return loggerProvider.getLogger(name);
	}

	/**
	 * Get a logger for the specified class.<br>
	 * <b>Hint:</b>The log levels are defined in <code>LogService</code>.
	 * 
	 * @param clazz
	 * @return the logger
	 */
	public Logger getLogger(Class<?> clazz) {
		return loggerProvider.getLogger(clazz);
	}

}
