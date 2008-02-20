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
package org.eclipse.riena.core.service;

import java.util.Arrays;
import java.util.Comparator;

import org.osgi.framework.ServiceReference;

/**
 * The specialized ranking implementation.
 */
public class RankingInjector extends Injector {

	private ServiceReference trackedServiceRef = null;

	/**
	 * @param serviceId
	 * @param target
	 */
	RankingInjector(ServiceId serviceId, Object target) {
		super(serviceId, target);
	}

	/*
	 * @see org.eclipse.riena.core.service.Injector#doStart()
	 */
	@Override
	protected void doStart() {
		ServiceReference serviceRef = getCurrentHighest();
		registerServiceListener();
		doBind(serviceRef);
	}

	/*
	 * @see org.eclipse.riena.core.service.Injector#doStop()
	 */
	@Override
	protected void doStop() {
		invokeUnbindMethod(trackedServiceRef);
		trackedServiceRef = null;
	}

	/*
	 * @see org.eclipse.riena.core.service.Injector#bind(org.osgi.framework.ServiceReference)
	 */
	@Override
	protected void doBind(ServiceReference serviceRef) {
		if (serviceRef == null)
			return;
		if (trackedServiceRef != null && serviceRef.compareTo(trackedServiceRef) > 0)
			return;

		invokeUnbindMethod(trackedServiceRef);
		invokeBindMethod(serviceRef);
		trackedServiceRef = serviceRef;
	}

	/*
	 * @see org.eclipse.riena.core.service.Injector#unbind(org.osgi.framework.ServiceReference)
	 */
	@Override
	protected void doUnbind(ServiceReference serviceRef) {
		if (serviceRef == null)
			return;
		if (serviceRef.compareTo(trackedServiceRef) != 0)
			return;
		invokeUnbindMethod(serviceRef);
		ServiceReference highest = getCurrentHighest();
		if (highest == null) {
			trackedServiceRef = null;
			return;
		}
		invokeBindMethod(highest);
		trackedServiceRef = highest;
	}

	/**
	 * @return
	 */
	private ServiceReference getCurrentHighest() {
		ServiceReference[] serviceRefs = getServiceReferences();
		return highestServiceRef(serviceRefs);
	}

	/**
	 * @param serviceRefs
	 * @return
	 */
	private static ServiceReference highestServiceRef(ServiceReference[] serviceRefs) {
		if (serviceRefs == null)
			return null;
		if (serviceRefs.length == 1)
			return serviceRefs[0];
		Arrays.sort(serviceRefs, new ObjectRankingComparator());
		return serviceRefs[0];
	}

	private static final class ObjectRankingComparator implements Comparator<ServiceReference> {

		/*
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(ServiceReference sr1, ServiceReference sr2) {
			return sr1.compareTo(sr2);
		}
	}

}
