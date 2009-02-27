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
package org.eclipse.riena.internal.monitor.client;

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.injector.extension.ExtensionInjector;
import org.eclipse.riena.core.wire.AbstractWiring;
import org.osgi.framework.BundleContext;

/**
 * Wire/unwire the {@code Aggregator}.
 */
public class AggregatorWiring extends AbstractWiring {

	private ExtensionInjector collectorsInjector;
	private ExtensionInjector storeInjector;
	private ExtensionInjector senderInjector;

	@Override
	public void wire(Object bean, BundleContext context) {
		collectorsInjector = Inject.extension(ICollectorExtension.ID).useType(ICollectorExtension.class).into(bean)
				.andStart(context);
		storeInjector = Inject.extension(IStoreExtension.ID).expectingMinMax(0, 1).useType(IStoreExtension.class).into(
				bean).andStart(context);
		senderInjector = Inject.extension(ISenderExtension.ID).expectingMinMax(0, 1).useType(ISenderExtension.class)
				.into(bean).andStart(context);
	}

	@Override
	public void unwire(Object bean, BundleContext context) {
		collectorsInjector.stop();
		storeInjector.stop();
		senderInjector.stop();
	}

}
