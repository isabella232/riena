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
package org.eclipse.riena.communication.core.factory;

import org.eclipse.riena.communication.core.IRemoteServiceReference;
import org.eclipse.riena.communication.core.RemoteServiceDescription;

import org.osgi.framework.ServiceRegistration;

/**
 * This is a default implementation of {@link IRemoteServiceReference}.
 * 
 * @author Alexander Ziegler
 */
public class RemoteServiceReference implements IRemoteServiceReference {
	private Object serviceInstance;
	private ServiceRegistration serviceRegistration;
	private RemoteServiceDescription description;
	private String hostId;

	/**
	 * Creates an instance with the given service end point description
	 * 
	 * @param description
	 */
	public RemoteServiceReference(RemoteServiceDescription description) {
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.IRemoteServiceReference#getHostId()
	 */
	public String getHostId() {
		return hostId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.IRemoteServiceReference#getURL()
	 */
	public String getURL() {
		return description.getURL();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.communication.core.IRemoteServiceReference#
	 * getServiceRegistration()
	 */
	public ServiceRegistration getServiceRegistration() {
		return serviceRegistration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.communication.core.IRemoteServiceReference#
	 * setServiceRegistration(org.osgi.framework.ServiceRegistration)
	 */
	public void setServiceRegistration(ServiceRegistration serviceRegistration) {
		this.serviceRegistration = serviceRegistration;
	}

	/**
	 * Sets the service instance i.e. proxy reference. The service instance
	 * represents the remote OSGi Service
	 * 
	 * @param serviceInstance
	 */
	public void setServiceInstance(Object serviceInstance) {
		this.serviceInstance = serviceInstance;
	}

	/*
	 * @see xeval.rsd.core.hessian.internal.IServiceEntry#getServiceInstance()
	 */
	public Object getServiceInstance() {
		return serviceInstance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.IRemoteServiceReference#getDescription
	 * ()
	 */
	public RemoteServiceDescription getDescription() {
		return description;
	}

	/*
	 * @see xeval.rsd.core.hessian.internal.IServiceEntry#disbose()
	 */
	public void dispose() {
		serviceInstance = null;
		serviceRegistration = null;
		if (description != null) {
			description.dispose();
			description = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.communication.core.IRemoteServiceReference#
	 * getServiceInterfaceClassName()
	 */
	public String getServiceInterfaceClassName() {
		return description.getServiceInterfaceClassName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.IRemoteServiceReference#setHostId
	 * (java.lang.String)
	 */
	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	@Override
	public String toString() {
		return "hostId= " + hostId + ", end point=(" + getDescription() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}
