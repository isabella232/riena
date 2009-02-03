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
package org.eclipse.riena.communication.core.factory;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.internal.communication.core.Activator;

/**
 * Interface for reading the available Remote Service Factories
 */
@ExtensionInterface
public interface IRemoteServiceFactoryProperties {

	String EXTENSION_POINT_ID = Activator.PLUGIN_ID + ".remoteservicefactory"; //$NON-NLS-1$

	/**
	 * @return the protocol property
	 */
	String getProtocol();

	/**
	 * @return the RemoteServiceFactory instance for this protocol
	 */
	IRemoteServiceFactory createRemoteServiceFactory();
}
