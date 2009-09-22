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
package org.eclipse.riena.internal.communication.core.ssl;

import org.osgi.framework.Bundle;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;

/**
 * SSl properties for the ssl configuration
 */
@ExtensionInterface(id = "ssl")
public interface ISSLPropertiesExtension {

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
