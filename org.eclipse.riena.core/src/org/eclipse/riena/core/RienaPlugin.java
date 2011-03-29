/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core;

import org.osgi.framework.BundleContext;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.equinox.log.Logger;

/**
 * Abstract base class for riena plugins.<br>
 * It provides all descendant classes access to the bundle context.
 * <p>
 * <b>Note: </b>Derived Activators/Plugins must call <code>super.start()</code>
 * and <code>super.stop()</code> within their <code>start()</code> and
 * <code>stop()</stop> methods.
 */
public abstract class RienaPlugin extends Plugin implements IRienaActivator {

	/**
	 * Get the shared context.
	 * 
	 * @return
	 */
	public BundleContext getContext() {
		return getBundle().getBundleContext();
	}

	/**
	 * Get a logger for the specified name.<br>
	 * <b>Hint:</b>The log levels are defined in <code>LogService</code>.
	 * 
	 * @deprecated Please use {@code Log4r.getLogger()} instead.
	 * 
	 * @param name
	 * @return the logger
	 */
	@Deprecated
	public Logger getLogger(final String name) {
		return Log4r.getLogger(this, name);

	}

	/**
	 * Get a logger for the specified class.<br>
	 * <b>Hint:</b>The log levels are defined in <code>LogService</code>.
	 * 
	 * @deprecated Please use {@code Log4r.getLogger()} instead.
	 * 
	 * @param clazz
	 * @return the logger
	 */
	@Deprecated
	public Logger getLogger(final Class<?> clazz) {
		return Log4r.getLogger(this, clazz);
	}

}
