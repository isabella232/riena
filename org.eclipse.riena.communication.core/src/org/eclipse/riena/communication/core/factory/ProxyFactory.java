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

import org.eclipse.core.runtime.Assert;
import org.osgi.framework.BundleContext;

/**
 *
 */
public class ProxyFactory {

	private final Class<?> clazz;
	private String url;
	private String protocol;
	private BundleContext context;

	public ProxyFactory(Class<?> clazz) {
		this.clazz = clazz;
	}

	public ProxyFactory usingUrl(String url) {
		Assert.isNotNull(url);
		this.url = url;
		return this;
	}

	public ProxyFactory withProtocol(String protocol) {
		Assert.isNotNull(protocol);
		this.protocol = protocol;
		return this;
	}

	public void andStart(BundleContext context) {
		this.context = context;
		Assert.isNotNull(url);
		Assert.isNotNull(protocol);
		new RemoteServiceFactory().createAndRegisterProxy(clazz, url, protocol, context);
	}
}
