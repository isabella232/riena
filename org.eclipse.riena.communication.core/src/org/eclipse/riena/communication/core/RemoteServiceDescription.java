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
package org.eclipse.riena.communication.core;

import static org.eclipse.riena.communication.core.publisher.RSDPublisherProperties.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

import org.eclipse.riena.communication.core.factory.IRemoteServiceFactory;
import org.eclipse.riena.communication.core.publisher.IServicePublishEventDispatcher;
import org.eclipse.riena.communication.core.publisher.IServicePublisher;
import org.eclipse.riena.communication.core.util.CommunicationUtil;

/**
 * The RemoteServiceDescription describes an remote service end point. Usually
 * an service end point describes a published OSGi Service within a server
 * container.
 * <p>
 * For example Riena communication has defined an "remote" OSGi service
 * {@link IServicePublishEventDispatcher} which provides a list of all published
 * OSGi Services within a server container
 * <p>
 * Usually a RemoteServiceDescription becomes instantiated within an server
 * container and is used by an IServicePublisher within an server and by an
 * {@link IRemoteServiceFactory} within a client container
 * 
 * @see IServicePublishEventDispatcher
 * @see IServicePublisher
 * @see IRemoteServiceFactory
 */
public class RemoteServiceDescription {

	public enum State {
		/**
		 * The "remote" OSGi Service was registered
		 */
		REGISTERED,
		/**
		 * The "remote" OSGi Service was modified
		 */
		MODIFIED,
		/**
		 * The "remote" OSGi Service was unregistered
		 */
		UNREGISTERED
	};

	private State state = State.REGISTERED;
	private transient Class<?> serviceInterfaceClass;
	private transient Object service;
	private transient ServiceReference serviceRef;
	private String path;
	private String version;
	private String bundleName;
	private String serviceInterfaceClassName;
	private String url;
	private String protocol;
	private final Map<String, Object> properties = new HashMap<String, Object>();

	/**
	 * Create a instance of a RemoteServiceDescription
	 */
	public RemoteServiceDescription() {
		super();
	}

	/**
	 * Create an instance of a RemoteServiceDescription with the given service
	 * reference and service object
	 * 
	 * @param serviceRef
	 * @param service
	 */
	public RemoteServiceDescription(final ServiceReference serviceRef, final Object service,
			final Class<?> serviceInterface) {
		this();
		this.serviceRef = serviceRef;
		this.service = service;
		final String[] keys = serviceRef.getPropertyKeys();
		for (final String key : keys) {
			if (key.equals(Constants.OBJECTCLASS)) {
				// String obejctClassName =
				// CommunicationUtil.accessProperty(serviceRef
				// .getProperty(PROP_SERVICE_CLASSNAME),null);
				serviceInterfaceClass = serviceInterface;
				serviceInterfaceClassName = serviceInterfaceClass.getName();
			} else if (key.equals(PROP_REMOTE_PROTOCOL)) {
				protocol = CommunicationUtil.accessProperty(serviceRef.getProperty(PROP_REMOTE_PROTOCOL), null);
			} else if (key.equals(PROP_REMOTE_PATH)) {
				path = CommunicationUtil.accessProperty(serviceRef.getProperty(PROP_REMOTE_PATH), null);
			} else {
				setProperty(key, CommunicationUtil.accessProperty(serviceRef.getProperty(key), null));
			}
		}
		bundleName = serviceRef.getBundle().getSymbolicName();
		// TODO enable this later
		//		assertServiceConstraints();
	}

	/**
	 * Disposes this RemoteServiceDescription. release all references.
	 */
	public void dispose() {
		bundleName = null;
		service = null;
		serviceRef = null;
		serviceInterfaceClass = null;
		serviceInterfaceClassName = null;
		path = null;
		url = null;
		version = null;
		protocol = null;
	}

	/**
	 * @return the bundle name from where the service comes
	 */
	public String getBundleName() {
		return bundleName;
	}

	/**
	 * 
	 * @return the content path where the service end point is published
	 */
	public String getPath() {
		return path;
	}

	/**
	 * 
	 * @return the protocol under the service end point is available
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * 
	 * @return the service i.e. the OSGi Service
	 */
	public Object getService() {
		return service;
	}

	/**
	 * 
	 * @return the service interface of the service
	 */
	public Class<?> getServiceInterfaceClass() {
		return serviceInterfaceClass;
	}

	/**
	 * 
	 * @return the service interface name of the service
	 */
	public String getServiceInterfaceClassName() {
		return serviceInterfaceClassName;
	}

	/**
	 * 
	 * @return the service reference for the service
	 */
	public ServiceReference getServiceRef() {
		return serviceRef;
	}

	/**
	 * The state of the service. Values are:<br>
	 * <br> {@link #REGISTERED}<br> {@link #MODIFIED}<br> {@link #UNREGISTERED}<br>
	 * 
	 * @return the current state of the service
	 */
	public State getState() {
		return state;
	}

	/**
	 * 
	 * @return the absolute URL the the service end point is published
	 */
	public String getURL() {
		return url;
	}

	/**
	 * 
	 * @return the service or bundle version
	 */
	public String getVersion() {
		return version;
	}

	@Override
	public int hashCode() {
		return getPath().hashCode();
	}

	/**
	 * Sets the bundle name from where the service comes
	 * 
	 * @param bundleName
	 */
	public void setBundleName(final String bundleName) {
		this.bundleName = bundleName;
	}

	/**
	 * Sets the content path where the service end point becomes published
	 * 
	 * @param path
	 */
	public void setPath(final String path) {
		this.path = path;
	}

	/**
	 * Sets the protocol under the service end point becomes available
	 * 
	 * @param protocol
	 */
	public void setProtocol(final String protocol) {
		this.protocol = protocol;
	}

	/**
	 * Sets the service i.e. the OSGi Service
	 * 
	 * @param service
	 */
	public void setService(final Object service) {
		this.service = service;
		// TODO enable this later
		//		assertServiceConstraints();
	}

	/**
	 * Sets the service interface of the service
	 * 
	 * @param interfaceClass
	 */
	public void setServiceInterfaceClass(final Class<?> interfaceClass) {
		this.serviceInterfaceClass = interfaceClass;
		// TODO enable this later
		//		assertServiceConstraints();
	}

	/**
	 * the service interface name of the service
	 * 
	 * @param interfaceClassName
	 */
	public void setServiceInterfaceClassName(final String interfaceClassName) {
		this.serviceInterfaceClassName = interfaceClassName;

	}

	/**
	 * Sets the service reference for the service
	 * 
	 * @param serviceRef
	 */
	public void setServiceRef(final ServiceReference serviceRef) {
		this.serviceRef = serviceRef;
	}

	public void setState(final State type) {
		this.state = type;
	}

	/**
	 * Sets the absolute URL the the service end point is published
	 * 
	 * @param url
	 */
	public void setURL(final String url) {
		this.url = url;
	}

	/**
	 * Sets the service or bundle version
	 * 
	 * @param version
	 */
	public void setVersion(final String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "protocol=" + protocol + ", url=" + url + ", interface=" + serviceInterfaceClassName; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * Sets the given property
	 * 
	 * @param name
	 * @param value
	 */
	public void setProperty(final String name, final Object value) {
		properties.put(name, value);
	}

	/**
	 * Answers the property value for the given name
	 * 
	 * @param name
	 * @return the property value
	 */
	public Object getProperty(final String name) {
		return properties.get(name);
	}

	// TODO remove @Suppresswarnings when this becomes activated
	@SuppressWarnings("unused")
	private void assertServiceConstraints() {
		// Do not allow multiple occurrences of the same service method names, i.e. no overloading
		if (serviceInterfaceClass != null) {
			final Method[] methods = serviceInterfaceClass.getMethods();
			final Set<String> names = new HashSet<String>();
			for (final Method method : methods) {
				if (!Modifier.isVolatile(method.getModifiers()) && !names.add(method.getName())) {
					throw new RemoteFailure("Can not use interface " + serviceInterfaceClass.getName() //$NON-NLS-1$
							+ " as service interface because at least one method (" + method.getName() //$NON-NLS-1$
							+ ") is overloaded, i.e. methods exists with the same name but different signatures."); //$NON-NLS-1$
				}
			}
		}
		// Check whether service instance is compatible to service class
		if (service != null && serviceInterfaceClass != null) {
			if (!serviceInterfaceClass.isAssignableFrom(service.getClass())) {
				throw new RemoteFailure("Service class " + service.getClass().getName() //$NON-NLS-1$
						+ " and service interface class " + serviceInterfaceClass.getName() //$NON-NLS-1$
						+ " do not match, i.e. service can not be assigend to service interface."); //$NON-NLS-1$
			}
		}
	}
}
