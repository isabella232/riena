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
package org.eclipse.riena.communication.core.proxyselector;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.riena.internal.communication.core.proxyselector.IProxySelectorExtension;
import org.eclipse.riena.internal.communication.core.proxyselector.ProxySelectorConfiguration;
import org.eclipse.riena.tests.RienaTestCase;
import org.eclipse.riena.tests.collect.NonUITestCase;

/**
 * Test the {@code ProxySelectorConfiguration}.
 */
@NonUITestCase
public class ProxySelectorConfigurationTest extends RienaTestCase {

	@SuppressWarnings("restriction")
	public void testNullConfig() {
		ProxySelector defaultPS = ProxySelector.getDefault();
		ProxySelectorConfiguration config = new ProxySelectorConfiguration();
		config.configure(null);
		assertSame(defaultPS, ProxySelector.getDefault());
	}

	@SuppressWarnings("restriction")
	public void testOneProxySelectorConfigAndRestore() throws URISyntaxException {
		ProxySelector defaultPS = ProxySelector.getDefault();
		ProxySelectorConfiguration config = new ProxySelectorConfiguration();
		config.configure(new IProxySelectorExtension[] { new ProxySelectorExtension(100, "test", new TestProxySelector(
				TestUtil.newProxy("test.de"))) });
		assertNotSame(defaultPS, ProxySelector.getDefault());
		assertEquals("test.de", getHost(0));
		config.configure(null);
		assertSame(defaultPS, ProxySelector.getDefault());
	}

	@SuppressWarnings("restriction")
	public void testTwoProxySelectorsWithOrder100and10ConfigAndRestore() throws URISyntaxException {
		ProxySelector defaultPS = ProxySelector.getDefault();
		ProxySelectorConfiguration config = new ProxySelectorConfiguration();
		config.configure(new IProxySelectorExtension[] {
				new ProxySelectorExtension(100, "test1", new TestProxySelector(TestUtil.newProxy("test1.de"))),
				new ProxySelectorExtension(10, "test2", new TestProxySelector(TestUtil.newProxy("test2.de"))) });
		assertNotSame(defaultPS, ProxySelector.getDefault());
		assertEquals("test2.de", getHost(0));
		assertEquals("test1.de", getHost(1));
		config.configure(null);
		assertSame(defaultPS, ProxySelector.getDefault());
	}

	@SuppressWarnings("restriction")
	public void testTwoProxySelectorsWithOrder10and100ConfigAndRestore() throws URISyntaxException {
		ProxySelector defaultPS = ProxySelector.getDefault();
		ProxySelectorConfiguration config = new ProxySelectorConfiguration();
		config.configure(new IProxySelectorExtension[] {
				new ProxySelectorExtension(10, "test1", new TestProxySelector(TestUtil.newProxy("test1.de"))),
				new ProxySelectorExtension(100, "test2", new TestProxySelector(TestUtil.newProxy("test2.de"))) });
		assertNotSame(defaultPS, ProxySelector.getDefault());
		assertEquals("test1.de", getHost(0));
		assertEquals("test2.de", getHost(1));
		config.configure(null);
		assertSame(defaultPS, ProxySelector.getDefault());
	}

	private String getHost(int indexInProxyList) throws URISyntaxException {
		return ((InetSocketAddress) getProxy(indexInProxyList).address()).getHostName();
	}

	private Proxy getProxy(int indexInProxyList) throws URISyntaxException {
		return ProxySelector.getDefault().select(new URI("http://www.eclipse.org")).get(indexInProxyList);
	}

	@SuppressWarnings("restriction")
	private static final class ProxySelectorExtension implements IProxySelectorExtension {

		private int order;
		private String name;
		private ProxySelector proxySelector;

		private ProxySelectorExtension(int order, String name, ProxySelector proxySelector) {
			this.order = order;
			this.name = name;
			this.proxySelector = proxySelector;
		}

		public ProxySelector createProxySelector() {
			return proxySelector;
		}

		public String getName() {
			return name;
		}

		public int getOrder() {
			return order;
		}

	}
}
