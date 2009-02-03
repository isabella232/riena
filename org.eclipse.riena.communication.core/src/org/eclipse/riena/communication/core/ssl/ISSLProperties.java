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
package org.eclipse.riena.communication.core.ssl;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.internal.communication.core.Activator;
import org.osgi.framework.Bundle;

/**
 * SSl properties for the ssl configuration
 */
@ExtensionInterface
public interface ISSLProperties {

	String EXTENSION_POINT_ID = Activator.PLUGIN_ID + ".ssl"; //$NON-NLS-1$

	/**
	 * @return the protocol
	 */
	String getProtocol();

	/**
	 * @return the keystore
	 */
	String getKeystore();

	/**
	 * @return the password
	 */
	String getPassword();

	/**
	 * Get the contributing bundle. This is required for loading the key store.
	 * 
	 * @return the contributing bundle
	 */
	Bundle getContributingBundle();
}
