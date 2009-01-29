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
package org.eclipse.riena.internal.monitor.client;

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.wire.IWireWrap;
import org.osgi.framework.BundleContext;

/**
 * Wire the {@code Aggregator}.
 */
public class AggregatorWireWrap implements IWireWrap {

	public void wire(Object bean, BundleContext context) {
		Inject.extension("org.eclipse.riena.monitor.collectors").useType(ICollectorExtension.class).into(bean) //$NON-NLS-1$
				.andStart(context);
		Inject.extension("org.eclipse.riena.monitor.store").expectingMinMax(0, 1).useType(IStoreExtension.class).into( //$NON-NLS-1$
				bean).andStart(context);
		Inject.extension("org.eclipse.riena.monitor.sender").expectingMinMax(0, 1).useType(ISenderExtension.class) //$NON-NLS-1$
				.into(bean).andStart(context);
	}

}
