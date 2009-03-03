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
package org.eclipse.riena.internal.communication.core.proxyselector;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;

/**
 * The {@code CompoundProxySelector} gathers all the proxies from the given
 * {@code ProxySelector}s from the list given at instance creation.
 */
public class CompoundProxySelector extends ProxySelector {

	private final List<ProxySelector> proxySelectors;

	/**
	 * The
	 * 
	 * @param proxySelectors
	 */
	public CompoundProxySelector(List<ProxySelector> proxySelectors) {
		Assert.isLegal(proxySelectors != null, "proxySelectors must not be null."); //$NON-NLS-1$
		this.proxySelectors = proxySelectors;
	}

	@Override
	public List<Proxy> select(URI uri) {
		Assert.isLegal(uri != null, "uri must not be null."); //$NON-NLS-1$
		List<Proxy> result = new ArrayList<Proxy>();
		for (ProxySelector proxySelector : proxySelectors) {
			List<Proxy> proxies = proxySelector.select(uri);
			if (proxies != null && proxies.size() != 0) {
				result.addAll(proxies);
			}
		}
		if (!result.contains(Proxy.NO_PROXY)) {
			result.add(Proxy.NO_PROXY);
		}
		return result;
	}

	@Override
	public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
		// currently delegate to all - and hope the ´real´ proxy selectors can deal 
		// with proxy addresses that have not been delivered by them.
		for (ProxySelector proxySelector : proxySelectors) {
			proxySelector.connectFailed(uri, sa, ioe);
		}
	}

}
