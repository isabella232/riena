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
package org.eclipse.riena.core.injector.service;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.ServiceReference;

/**
 * The specialized filter injector implementation.
 */
public class FilterInjector extends ServiceInjector {

	private List<ServiceReference> trackedServiceRefs = null;

	/**
	 * @param serviceId
	 * @param target
	 */
	FilterInjector(final ServiceDescriptor serviceId, final Object target) {
		super(serviceId, target);
	}

	/*
	 * @see org.eclipse.riena.core.service.ServiceInjector#doStart()
	 */
	@Override
	protected void doStart() {
		trackedServiceRefs = new ArrayList<ServiceReference>(1);
		final ServiceReference[] serviceRefs = getServiceReferences();

		// register service listener early because its very likely that no
		// service is registered between getServiceReferences and
		// registerServiceListener()
		registerServiceListener();
		if (serviceRefs != null) {
			for (final ServiceReference serviceRef : serviceRefs) {
				doBind(serviceRef);
			}
		}
	}

	/*
	 * @see org.eclipse.riena.core.service.ServiceInjector#doStop()
	 */
	@Override
	protected void doStop() {
		// copy list to array so that I iterate through array and still
		// remove entries from List concurrently
		final ServiceReference[] serviceRefs;
		synchronized (trackedServiceRefs) {
			serviceRefs = trackedServiceRefs.toArray(new ServiceReference[trackedServiceRefs.size()]);
		}
		for (final ServiceReference serviceRef : serviceRefs) {
			doUnbind(serviceRef);
		}
		trackedServiceRefs = null;
	}

	/*
	 * @see
	 * org.eclipse.riena.core.service.ServiceInjector#doBind(org.osgi.framework
	 * .ServiceReference)
	 */
	@Override
	protected void doBind(final ServiceReference serviceRef) {
		synchronized (trackedServiceRefs) {
			if (trackedServiceRefs.contains(serviceRef)) {
				return;
			}
			invokeBindMethod(serviceRef);
			trackedServiceRefs.add(serviceRef);
		}
	}

	/*
	 * @see
	 * org.eclipse.riena.core.service.ServiceInjector#doUnbind(org.osgi.framework
	 * .ServiceReference)
	 */
	@Override
	protected void doUnbind(final ServiceReference serviceRef) {
		synchronized (trackedServiceRefs) {
			if (!trackedServiceRefs.contains(serviceRef)) {
				return;
			}
			invokeUnbindMethod(serviceRef);
			trackedServiceRefs.remove(serviceRef);
		}
	}

}
