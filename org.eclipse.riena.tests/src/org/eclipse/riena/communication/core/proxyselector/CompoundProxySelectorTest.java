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

import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.riena.internal.communication.core.proxyselector.CompoundProxySelector;
import org.eclipse.riena.internal.core.ignore.Nop;
import org.eclipse.riena.tests.RienaTestCase;
import org.eclipse.riena.tests.collect.NonUITestCase;

/**
 * Test the {@code CompoundProxySelector}.
 */
@NonUITestCase
public class CompoundProxySelectorTest extends RienaTestCase {

	@SuppressWarnings("restriction")
	public void testNullSelectors() {
		try {
			new CompoundProxySelector(null);
			fail();
		} catch (IllegalArgumentException e) {
			Nop.reason("ok - expected");
		}
	}

	@SuppressWarnings("restriction")
	public void testNullSelect() {
		CompoundProxySelector compoundPS = new CompoundProxySelector(new ArrayList<ProxySelector>());
		try {
			compoundPS.select(null);
			fail();
		} catch (IllegalArgumentException e) {
			Nop.reason("ok - expected");
		}
	}

	@SuppressWarnings("restriction")
	public void testSelectWithOneSelector() throws URISyntaxException {
		List<ProxySelector> selectors = new ArrayList<ProxySelector>();
		Proxy proxy = TestUtil.newProxy("test.de");
		selectors.add(new TestProxySelector(proxy));
		CompoundProxySelector compoundPS = new CompoundProxySelector(selectors);
		List<Proxy> proxies = compoundPS.select(new URI("http://www.eclipse.org"));
		assertEquals(proxy, proxies.get(0));
		assertEquals(Proxy.NO_PROXY, proxies.get(1));
	}

	@SuppressWarnings("restriction")
	public void testSelectWithTwoSelectors() throws URISyntaxException {
		List<ProxySelector> selectors = new ArrayList<ProxySelector>();
		Proxy proxy1 = TestUtil.newProxy("test.de");
		selectors.add(new TestProxySelector(proxy1));
		Proxy proxy2 = TestUtil.newProxy("test.de");
		selectors.add(new TestProxySelector(proxy2));
		CompoundProxySelector compoundPS = new CompoundProxySelector(selectors);
		List<Proxy> proxies = compoundPS.select(new URI("http://www.eclipse.org"));
		assertEquals(proxy1, proxies.get(0));
		assertEquals(proxy2, proxies.get(1));
		assertEquals(Proxy.NO_PROXY, proxies.get(2));
	}

	@SuppressWarnings("restriction")
	public void testConnectFailedWithTwoSelectors() throws URISyntaxException {
		List<ProxySelector> selectors = new ArrayList<ProxySelector>();
		Proxy proxy1 = TestUtil.newProxy("test.de");
		TestProxySelector selector1 = new TestProxySelector(proxy1);
		selectors.add(selector1);
		Proxy proxy2 = TestUtil.newProxy("test.de");
		TestProxySelector selector2 = new TestProxySelector(proxy2);
		selectors.add(selector2);
		CompoundProxySelector compoundPS = new CompoundProxySelector(selectors);
		URI uri = new URI("http://www.eclipse.org");
		compoundPS.connectFailed(uri, null, null);
		assertEquals(uri, selector1.getFailedURI());
		assertEquals(uri, selector2.getFailedURI());
	}
}
