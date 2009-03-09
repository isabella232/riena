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
package org.eclipse.riena.internal.communication.factory.hessian;

import org.eclipse.riena.communication.core.progressmonitor.IRemoteProgressMonitorRegistry;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.injector.service.ServiceInjector;
import org.eclipse.riena.core.wire.IWiring;
import org.osgi.framework.BundleContext;

/**
 * Wire the {@code RemoteServiceFactoryHessian}.
 */
public class RemoteServiceFactoryHessianWiring implements IWiring {

	private ServiceInjector injector;

	public void unwire(Object bean, BundleContext context) {
		injector.stop();
	}

	public void wire(Object bean, BundleContext context) {
		injector = Inject.service(IRemoteProgressMonitorRegistry.class).useRanking().into(bean).andStart(context);
	}

}
