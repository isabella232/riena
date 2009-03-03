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

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.injector.extension.ExtensionInjector;
import org.eclipse.riena.core.wire.AbstractWiring;
import org.osgi.framework.BundleContext;

/**
 * Wire the {@code SSLConfiguration}.
 */
public class SSLConfigurationWiring extends AbstractWiring {
	private ExtensionInjector sslInjector;

	@Override
	public void unwire(Object bean, BundleContext context) {
		sslInjector.stop();
	}

	@Override
	public void wire(Object bean, BundleContext context) {
		sslInjector = Inject.extension(ISSLProperties.EXTENSION_POINT_ID).expectingMinMax(0, 1).into(bean).update(
				"configure").andStart(context); //$NON-NLS-1$
	}

}
