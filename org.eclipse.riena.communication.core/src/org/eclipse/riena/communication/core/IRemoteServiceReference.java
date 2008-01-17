/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.communication.core;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ManagedService;

/**
 * A IRemoteServiceReference holds a representation of a remote OSGi Service. A
 * remote OSGi Service is defined by a service instance i.e. proxy reference and
 * whose remote end point description. IRemoteServiceReference become registered
 * or unregistered into the {@link IRemoteServiceRegistry} OSGi Service. The
 * service instance itself become registered or unregistered into the OSGi
 * Service Registry by the the IRemoteServiceRegistry OSGi Service. After the
 * service instance is registered it represents the remote OSGi Service.
 * <p>
 * A IRemoteServiceReference is managed by the IRemoteServiceRegistry OSGi
 * Service.
 * 
 * @author Alexander Ziegler
 * 
 * @see IRemoteServiceRegistry#registerService(IRemoteServiceReference)
 * @see IRemoteServiceRegistry#unregisterService(IRemoteServiceReference)
 * 
 * 
 */
public interface IRemoteServiceReference {

	/**
	 * @return the hostId who is responsible for this remote service object
	 */
	String getHostId();

	/**
	 * 
	 * @return the URL of the remote end point
	 */
	String getURL();

	/**
	 * Sets the service instance of this remote service reference
	 * 
	 * @param serviceInstance
	 */
	void setServiceInstance(Object serviceInstance);

	/**
	 * 
	 * @return the service instance i.e. proxy reference. The service instance
	 *         represents the remote OSGi Service
	 */
	Object getServiceInstance();

	/**
	 * 
	 * @return the remote end point description
	 */
	RemoteServiceDescription getDescription();

	/**
	 * The serviceId represents the id of the service instance after it is
	 * registered as remote OSGi Service. User can find the remote OSGi Service
	 * by this serviceId. Typically the serviceId is a full qualified type name
	 * of the service interface
	 * 
	 * @return serviceId of the service instance
	 * 
	 * @see BundleContext#getServiceReference(String)
	 * @see BundleContext#getServiceReference(String,String)
	 */
	String getServiceInterfaceClassName();

	/**
	 * @return the OSGi ServiceRegistration for the service instance after it is
	 *         registered as remote OSGi Service.
	 */
	ServiceRegistration getServiceRegistration();

	/**
	 * Sets the given OSGI ServiceRegistration for the service instance after it
	 * is registered as remote OSGi Service.
	 * 
	 * @param serviceRegistration
	 */
	void setServiceRegistration(ServiceRegistration serviceRegistration);

	/**
	 * @return the service instance of the ManagedService that can configure
	 *         this remote service reference (null if there is no such
	 *         ManagedService)
	 */
	ManagedService getConfigServiceInstance();

	/**
	 * @return the ServicePID for the ConfigService Instance
	 */
	String getConfigServicePID();

	/**
	 * @param configServiceRegistration
	 *            the ServiceRegistration instance of the registered
	 *            ManagedService
	 */
	void setConfigServiceRegistration(ServiceRegistration configServiceRegistration);

	/**
	 * @return the ServiceRegistration instance of the ManagedService
	 */
	ServiceRegistration getConfigServiceRegistration();

	/**
	 * Sets the hostId who is responsible for this remote service object
	 * 
	 * @param hostId
	 */
	void setHostId(String hostId);

	/**
	 * Disposes this reference
	 */
	void dispose();

}
