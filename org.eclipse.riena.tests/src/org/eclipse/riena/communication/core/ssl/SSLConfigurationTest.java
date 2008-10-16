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
package org.eclipse.riena.communication.core.ssl;

import java.io.File;
import java.io.IOException;

import org.eclipse.riena.internal.tests.Activator;
import org.eclipse.riena.tests.RienaTestCase;
import org.eclipse.riena.tests.nanohttp.TestServer;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

/**
 * 
 */
public class SSLConfigurationTest extends RienaTestCase {

	public void testZeroConfiguration() throws BundleException {
		printTestName();
		startBundle("org.eclipse.riena.communication.core");

		SSLConfiguration config = getService(SSLConfiguration.class);
		assertEquals("SSLConfiguration: null, null", config.toString());

		stopBundle("org.eclipse.riena.communication.core");
	}

	public void testOneConfiguration() throws BundleException {
		printTestName();
		addPluginXml(SSLConfigurationTest.class, "plugin.xml");
		startBundle("org.eclipse.riena.communication.core");

		SSLConfiguration config = getService(SSLConfiguration.class);
		assertEquals("SSLConfiguration: TLSv1, #jre-cacerts#", config.toString());

		removeExtension("org.eclipse.riena.communication.core.ssl.test");
		stopBundle("org.eclipse.riena.communication.core");
	}

	public void testLocateKeystoreJreCacerts() {
		printTestName();
		ISSLProperties properties = new SSLProperties("TLSv1", "#jre-cacerts#", "changeit");
		SSLConfiguration config = new SSLConfiguration();
		config.configure(properties);
		assertTrue(config.isConfigured());
	}

	public void testLocateKeystoreFile() {
		printTestName();
		String jreDir = System.getProperty("java.home"); //$NON-NLS-1$
		File cacertFile = new File(new File(new File(new File(jreDir), "lib"), "security"), "cacerts");

		ISSLProperties properties = new SSLProperties("TLSv1", cacertFile.toString(), "changeit");
		SSLConfiguration config = new SSLConfiguration();
		config.configure(properties);
		assertTrue(config.isConfigured());
	}

	public void testLocateKeystoreResource() {
		printTestName();
		ISSLProperties properties = new SSLProperties("TLSv1", "/org/eclipse/riena/communication/core/ssl/cacerts",
				"changeit");
		SSLConfiguration config = new SSLConfiguration();
		config.configure(properties);
		assertTrue(config.isConfigured());
	}

	public void testLocateKeystoreUrl() throws IOException {
		printTestName();
		String jreDir = System.getProperty("java.home"); //$NON-NLS-1$
		File cacertDir = new File(new File(new File(jreDir), "lib"), "security");
		TestServer nano = new TestServer(8888, cacertDir);

		ISSLProperties properties = new SSLProperties("TLSv1", "http://localhost:8888/cacerts", "changeit");
		SSLConfiguration config = new SSLConfiguration();
		config.configure(properties);
		assertTrue(config.isConfigured());
		nano.stop();
	}

	private static class SSLProperties implements ISSLProperties {

		private String protocol;
		private String keystore;
		private String password;

		public SSLProperties(String protocol, String keystore, String password) {
			this.protocol = protocol;
			this.keystore = keystore;
			this.password = password;
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

		public Bundle getContributingBundle() {
			return Activator.getDefault().getBundle();
		}

	}
}
