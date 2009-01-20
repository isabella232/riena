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
package org.eclipse.riena.internal.communication.core.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.riena.communication.core.IRemoteServiceReference;
import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.IRemoteServiceRegistry;
import org.eclipse.riena.communication.core.factory.ProxyAlreadyRegisteredFailure;
import org.eclipse.riena.internal.communication.core.Activator;

import org.eclipse.equinox.log.Logger;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.log.LogService;

/**
 * @author Alexander Ziegler
 * @author Christian Campo
 * 
 */
public class RemoteServiceRegistry implements IRemoteServiceRegistry {
	private Map<String, IRemoteServiceRegistration> registeredServices;
	private final static Logger LOGGER = Activator.getDefault().getLogger(RemoteServiceRegistry.class);

	public synchronized void start() {
		registeredServices = new HashMap<String, IRemoteServiceRegistration>();
	}

	public synchronized void stop() {
		// avoid ConcurrentModificationException by cloning ´iterable´!
		IRemoteServiceRegistration[] arrayRS = registeredServices.values().toArray(
				new IRemoteServiceRegistration[registeredServices.values().size()]);
		for (IRemoteServiceRegistration serviceReg : arrayRS) {
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
	public IRemoteServiceRegistration registerService(IRemoteServiceReference reference, BundleContext context) {

		final String url = reference.getDescription().getURL();
		synchronized (registeredServices) {
			IRemoteServiceRegistration foundRemoteServiceReg = registeredServices.get(url);
			if (foundRemoteServiceReg == null) {
				// it is a new entry, set properties
				Properties props = new Properties();
				props.put("service.url", url); //$NON-NLS-1$
				props.put("service.protocol", reference.getDescription().getProtocol()); //$NON-NLS-1$
				ServiceRegistration serviceRegistration = context.registerService(reference
						.getServiceInterfaceClassName(), reference.getServiceInstance(), props);
				try {
					Activator.getDefault().getContext().addServiceListener(new ServiceListener() {

						public void serviceChanged(ServiceEvent event) {
							if (event.getType() == ServiceEvent.UNREGISTERING) {
								if (registeredServices.containsKey(url)) {
									registeredServices.remove(url);
								}
								Activator.getDefault().getContext().removeServiceListener(this);
							}
						}
					}, "(service.id=" + serviceRegistration.getReference().getProperty("service.id") + ")"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				} catch (InvalidSyntaxException e) {
					// to nothing......
					e.printStackTrace();
				}
				reference.setContext(context);
				reference.setServiceRegistration(serviceRegistration);

				RemoteServiceRegistration remoteServiceReg = new RemoteServiceRegistration(reference, this);
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
	public void unregisterService(IRemoteServiceReference reference) {
		assert reference != null : "RemoteServiceReference must not be null"; //$NON-NLS-1$
		synchronized (registeredServices) {
			// we have commented out the following lines
			// because the service gets unregistered automatically when the bundle, that created it, is
			// stopped. the explicit call sometimes fails because that bundle is already stopped
			// and so unregister fails with an IllegalStateException
			ServiceRegistration serviceRegistration = reference.getServiceRegistration();
			String id = reference.getServiceInterfaceClassName();
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
	public List<IRemoteServiceRegistration> registeredServices(BundleContext context) {
		synchronized (registeredServices) {
			Collection<IRemoteServiceRegistration> values = registeredServices.values();
			// Answers all service registrations when hostId is null
			if (context == null) {
				return new ArrayList<IRemoteServiceRegistration>(values);
			}

			List<IRemoteServiceRegistration> result = new ArrayList<IRemoteServiceRegistration>();
			for (IRemoteServiceRegistration serviceReg : values) {
				BundleContext registeredBundleContext = serviceReg.getReference().getContext();
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
	public boolean hasServiceForUrl(String url) {
		return (registeredServices.get(url) != null);
	}
}
