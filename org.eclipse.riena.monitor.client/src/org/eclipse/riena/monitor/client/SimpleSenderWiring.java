/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.monitor.client;

import org.osgi.framework.BundleContext;

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.wire.AbstractWiring;
import org.eclipse.riena.monitor.common.IReceiver;

/**
 * Wire the {@code SimpleSender}.
 */
public class SimpleSenderWiring extends AbstractWiring {

	@Override
	public void wire(final Object bean, final BundleContext context) {
		Inject.service(IReceiver.class).useRanking().into(bean).andStart(context);
	}

}
