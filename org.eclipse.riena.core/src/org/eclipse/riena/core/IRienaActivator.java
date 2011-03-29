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

import org.eclipse.equinox.log.Logger;

/**
 * The {@code IRienaActivator} defines the contract for riena based activators.
 */
public interface IRienaActivator {

	/**
	 * Get the shared context.
	 * 
	 * @return
	 */
	BundleContext getContext();

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
	Logger getLogger(String name);

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
	Logger getLogger(Class<?> clazz);
}
