/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.core.logging.log4j;

import org.eclipse.core.runtime.IConfigurationElement;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;

/**
 * The {@code ExtensionInterface} is for the configuration of the {@code
 * Log4jLogListener}.
 */
@ExtensionInterface(id = "log4jConfiguration")
public interface ILog4jLogListenerConfigurationExtension {

	/**
	 * @return the configuration element on whose behalf this instance was
	 *         created
	 */
	IConfigurationElement getConfigurationElement();

	/**
	 * @return the string identifying the configuration
	 */
	String getLocation();

}
