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
package org.eclipse.riena.communication.core.factory;

import org.osgi.framework.BundleContext;

import org.eclipse.core.runtime.Assert;

import org.eclipse.riena.communication.core.IRemoteServiceRegistration;

/**
 * ProxyFactory used in the fluent API of Register to allow easy registration of
 * remote service proxies
 */
public class ProxyFactory {

	private final Class<?> clazz;
	private String url;
	private String protocol;

	/**
	 * @param clazz
	 *            of the remote service that needs to be created
	 */
	public ProxyFactory(final Class<?> clazz) {
		this.clazz = clazz;
	}

	/**
	 * @param url
	 *            of the remote service
	 * @return
	 */
	public ProxyFactory usingUrl(final String url) {
		Assert.isNotNull(url);
		this.url = url;
		return this;
	}

	/**
	 * @param protocol
	 *            as string i.e. "hessian"
	 * @return
	 */
	public ProxyFactory withProtocol(final String protocol) {
		Assert.isNotNull(protocol);
		this.protocol = protocol;
		return this;
	}

	/**
	 * @param context
	 *            start registration in this bundle context (automatically stops
	 *            the proxy if the bundle context stops)
	 * @return
	 */
	public IRemoteServiceRegistration andStart(final BundleContext context) {
		Assert.isNotNull(url);
		Assert.isNotNull(protocol);
		return new RemoteServiceFactory().createAndRegisterProxy(clazz, url, protocol, context);
	}
}
