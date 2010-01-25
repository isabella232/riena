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
package org.eclipse.riena.communication.core.ssl;

import java.io.File;
import java.io.IOException;

import javax.net.ssl.HostnameVerifier;

import org.osgi.framework.Bundle;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.internal.communication.core.ssl.ISSLPropertiesExtension;
import org.eclipse.riena.internal.communication.core.ssl.SSLConfiguration;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.internal.tests.Activator;
import org.eclipse.riena.tests.nanohttp.TestServer;

/**
 * 
 */
@NonUITestCase
public class SSLConfigurationTest extends RienaTestCase {

	public void testZeroConfiguration() {
		printTestName();

		SSLConfiguration config = new SSLConfiguration();
		assertNull(ReflectionUtils.getHidden(config, "protocol"));
		assertNull(ReflectionUtils.getHidden(config, "keystore"));
		assertNull(ReflectionUtils.getHidden(config, "password"));
		assertNull(ReflectionUtils.getHidden(config, "hostnameVerifier"));
	}

	public void testOneConfiguration() {
		printTestName();
		addPluginXml(SSLConfigurationTest.class, "plugin.xml");

		try {
			SSLConfiguration config = new SSLConfiguration();
			Wire.instance(config).andStart(getContext());

			assertEquals(ReflectionUtils.getHidden(config, "protocol"), "TLSv1");
			assertEquals(ReflectionUtils.getHidden(config, "keystore"), "#jre-cacerts#");
			assertEquals(ReflectionUtils.getHidden(config, "password"), "changeit");
			assertNotNull(ReflectionUtils.getHidden(config, "hostnameVerifier"));
			assertEquals(ReflectionUtils.getHidden(config, "hostnameVerifier").getClass(),
					SSLConfiguration.StrictHostnameVerifier.class);
		} finally {
			removeExtension("org.eclipse.riena.communication.core.ssl.test");
		}
	}

	public void testOneConfigurationWithHostnameVerifier() {
		printTestName();
		addPluginXml(SSLConfigurationTest.class, "pluginWithHostnameVerifier.xml");

		try {
			SSLConfiguration config = new SSLConfiguration();
			Wire.instance(config).andStart(getContext());

			assertEquals(ReflectionUtils.getHidden(config, "protocol"), "TLSv1");
			assertEquals(ReflectionUtils.getHidden(config, "keystore"), "#jre-cacerts#");
			assertEquals(ReflectionUtils.getHidden(config, "password"), "changeit");
			assertEquals(ReflectionUtils.getHidden(config, "hostnameVerifier").getClass(), TestHostnameVerifier.class);
		} finally {
			removeExtension("org.eclipse.riena.communication.core.ssl.test");
		}
	}

	public void testLocateKeystoreJreCacerts() {
		printTestName();
		ISSLPropertiesExtension properties = new SSLProperties("TLSv1", "#jre-cacerts#", "changeit");
		SSLConfiguration config = new SSLConfiguration();
		config.configure(properties);
		assertTrue(config.isConfigured());
		assertEquals(ReflectionUtils.getHidden(config, "protocol"), "TLSv1");
		assertEquals(ReflectionUtils.getHidden(config, "keystore"), "#jre-cacerts#");
		assertEquals(ReflectionUtils.getHidden(config, "password"), "changeit");
		assertNotNull(ReflectionUtils.getHidden(config, "hostnameVerifier"));
		assertEquals(ReflectionUtils.getHidden(config, "hostnameVerifier").getClass(),
				SSLConfiguration.StrictHostnameVerifier.class);
	}

	public void testLocateKeystoreJreCacertsAndCustomHostnameVerifier() {
		printTestName();
		HostnameVerifier hostnameVerifier = new TestHostnameVerifier();
		ISSLPropertiesExtension properties = new SSLProperties("TLSv1", "#jre-cacerts#", "changeit", hostnameVerifier);
		SSLConfiguration config = new SSLConfiguration();
		config.configure(properties);
		assertTrue(config.isConfigured());
		assertEquals(ReflectionUtils.getHidden(config, "protocol"), "TLSv1");
		assertEquals(ReflectionUtils.getHidden(config, "keystore"), "#jre-cacerts#");
		assertEquals(ReflectionUtils.getHidden(config, "password"), "changeit");
		assertEquals(ReflectionUtils.getHidden(config, "hostnameVerifier"), hostnameVerifier);
	}

	public void testLocateKeystoreFile() {
		printTestName();
		String jreDir = System.getProperty("java.home"); //$NON-NLS-1$
		File cacertFile = new File(new File(new File(new File(jreDir), "lib"), "security"), "cacerts");

		ISSLPropertiesExtension properties = new SSLProperties("TLSv1", cacertFile.toString(), "changeit");
		SSLConfiguration config = new SSLConfiguration();
		config.configure(properties);
		assertTrue(config.isConfigured());
	}

	public void testLocateKeystoreResource() {
		printTestName();
		ISSLPropertiesExtension properties = new SSLProperties("TLSv1",
				"/org/eclipse/riena/communication/core/ssl/cacerts", "changeit");
		SSLConfiguration config = new SSLConfiguration();
		config.configure(properties);
		assertTrue(config.isConfigured());
	}

	public void testLocateKeystoreEntry() {
		printTestName();
		ISSLPropertiesExtension properties = new SSLProperties("TLSv1", "/keystore/cacerts", "changeit");
		SSLConfiguration config = new SSLConfiguration();
		config.configure(properties);
		assertTrue(config.isConfigured());
	}

	public void testLocateKeystoreUrl() throws IOException {
		printTestName();
		String jreDir = System.getProperty("java.home"); //$NON-NLS-1$
		File cacertDir = new File(new File(new File(jreDir), "lib"), "security");
		TestServer nano = new TestServer(8888, cacertDir);

		ISSLPropertiesExtension properties = new SSLProperties("TLSv1", "http://localhost:8888/cacerts", "changeit");
		SSLConfiguration config = new SSLConfiguration();
		config.configure(properties);
		assertTrue(config.isConfigured());
		nano.stop();
	}

	private static class SSLProperties implements ISSLPropertiesExtension {

		private String protocol;
		private String keystore;
		private String password;
		private HostnameVerifier hostnameVerifier;

		public SSLProperties(String protocol, String keystore, String password) {
			this(protocol, keystore, password, null);
		}

		public SSLProperties(String protocol, String keystore, String password, HostnameVerifier hostnameVerifier) {
			this.protocol = protocol;
			this.keystore = keystore;
			this.password = password;
			this.hostnameVerifier = hostnameVerifier;
		}

		public String getKeystore() {
			return keystore;
		}

		public String getPassword() {
			return password;
		}

		public String getProtocol() {
			return protocol;
		}

		public HostnameVerifier createHostnameVerifier() {
			return hostnameVerifier;
		}

		public Bundle getContributingBundle() {
			return Activator.getDefault().getBundle();
		}

	}
}
