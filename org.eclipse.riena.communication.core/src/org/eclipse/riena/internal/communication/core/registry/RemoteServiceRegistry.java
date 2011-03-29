/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.communication.core.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.log.LogService;

import org.eclipse.core.runtime.Assert;
import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.communication.core.IRemoteServiceReference;
import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.IRemoteServiceRegistry;
import org.eclipse.riena.communication.core.factory.ProxyAlreadyRegisteredFailure;
import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.internal.communication.core.Activator;

/**
 * TODO Documentation
 */
public class RemoteServiceRegistry implements IRemoteServiceRegistry {
	private Map<String, IRemoteServiceRegistration> registeredServices;
	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), RemoteServiceRegistry.class);

	public synchronized void start() {
		registeredServices = new HashMap<String, IRemoteServiceRegistration>();
	}

	public synchronized void stop() {
		// avoid ConcurrentModificationException by cloning ´iterable´!
		final IRemoteServiceRegistration[] arrayRS = registeredServices.values().toArray(
				new IRemoteServiceRegistration[registeredServices.values().size()]);
		for (final IRemoteServiceRegistration serviceReg : arrayRS) {
			// unregisters all services for this registry
			unregisterService(serviceReg.getReference());
		}
		registeredServices.clear();
		registeredServices = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.IRemoteServiceRegistry#registerService
	 * (org.eclipse.riena.communication.core.IRemoteServiceReference)
	 */
	public IRemoteServiceRegistration registerService(final IRemoteServiceReference reference,
			final BundleContext context) {

		final String url = reference.getDescription().getURL();
		synchronized (registeredServices) {
			final IRemoteServiceRegistration foundRemoteServiceReg = registeredServices.get(url);
			if (foundRemoteServiceReg == null) {
				// it is a new entry, set properties
				final Dictionary<String, Object> props = new Hashtable<String, Object>();
				props.put("service.url", url); //$NON-NLS-1$
				props.put("service.protocol", reference.getDescription().getProtocol()); //$NON-NLS-1$
				final ServiceRegistration serviceRegistration = context.registerService(
						reference.getServiceInterfaceClassName(), reference.getServiceInstance(), props);
				try {
					Activator.getDefault().getContext().addServiceListener(new ServiceListener() {

						public void serviceChanged(final ServiceEvent event) {
							if (event.getType() == ServiceEvent.UNREGISTERING) {
								if (registeredServices.containsKey(url)) {
									registeredServices.remove(url);
								}
								Activator.getDefault().getContext().removeServiceListener(this);
							}
						}
					}, "(service.id=" + serviceRegistration.getReference().getProperty("service.id") + ")"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				} catch (final InvalidSyntaxException e) {
					// do nothing......
					e.printStackTrace();
				}
				reference.setContext(context);
				reference.setServiceRegistration(serviceRegistration);

				final RemoteServiceRegistration remoteServiceReg = new RemoteServiceRegistration(reference, this);
				registeredServices.put(url, remoteServiceReg);

				LOGGER.log(LogService.LOG_DEBUG, "OSGi NEW service registered id: " //$NON-NLS-1$
						+ reference.getServiceInterfaceClassName());
				return remoteServiceReg;
			} else {
				// if proxy for this url already exists, throw exception
				throw new ProxyAlreadyRegisteredFailure(
						"Cannot register two remote service proxies with the same URL. Proxy for " + url //$NON-NLS-1$
								+ " already exists. Registered by bundle " //$NON-NLS-1$
								+ foundRemoteServiceReg.getReference().getContext().getBundle().getSymbolicName() + "."); //$NON-NLS-1$
				//				reference.setServiceRegistration(foundRemoteServiceReg.getReference().getServiceRegistration());
				//				foundRemoteServiceReg.setReference(reference);
				//				return foundRemoteServiceReg;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.IRemoteServiceRegistry#unregisterService
	 * (org.eclipse.riena.communication.core.IRemoteServiceReference)
	 */
	public void unregisterService(final IRemoteServiceReference reference) {
		Assert.isNotNull(reference, "RemoteServiceReference must not be null"); //$NON-NLS-1$
		synchronized (registeredServices) {
			// we have commented out the following lines
			// because the service gets unregistered automatically when the bundle, that created it, is
			// stopped. the explicit call sometimes fails because that bundle is already stopped
			// and so unregister fails with an IllegalStateException
			final ServiceRegistration serviceRegistration = reference.getServiceRegistration();
			final String id = reference.getServiceInterfaceClassName();
			registeredServices.remove(reference.getURL());
			reference.dispose();
			serviceRegistration.unregister();
			LOGGER.log(LogService.LOG_DEBUG, "OSGi service removed id: " + id); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.communication.core.IRemoteServiceRegistry#
	 * registeredServices(java.lang.String)
	 */
	public List<IRemoteServiceRegistration> registeredServices(final BundleContext context) {
		synchronized (registeredServices) {
			final Collection<IRemoteServiceRegistration> values = registeredServices.values();
			// Answers all service registrations when hostId is null
			if (context == null) {
				return new ArrayList<IRemoteServiceRegistration>(values);
			}

			final List<IRemoteServiceRegistration> result = new ArrayList<IRemoteServiceRegistration>();
			for (final IRemoteServiceRegistration serviceReg : values) {
				final BundleContext registeredBundleContext = serviceReg.getReference().getContext();
				if (context.equals(registeredBundleContext)) {
					result.add(serviceReg);
				}
			}
			return result;
		}
	}

	/**
	 * only meant for testcase that directly access the implementation
	 * 
	 * @param url
	 * @return
	 */
	public boolean hasServiceForUrl(final String url) {
		return (registeredServices.get(url) != null);
	}
}
