/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
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

		final SSLConfiguration config = new SSLConfiguration();
		assertNull(ReflectionUtils.getHidden(config, "protocol"));
		assertNull(ReflectionUtils.getHidden(config, "keystore"));
		assertNull(ReflectionUtils.getHidden(config, "password"));
		assertNull(ReflectionUtils.getHidden(config, "hostnameVerifier"));
	}

	public void testOneConfiguration() {
		printTestName();
		addPluginXml(SSLConfigurationTest.class, "plugin.xml");

		try {
			final SSLConfiguration config = new SSLConfiguration();
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

	public void testOneConfigurationWithEncryptedPassword() {
		printTestName();
		addPluginXml(SSLConfigurationTest.class, "pluginWithEncryptedPassword.xml");

		try {
			final SSLConfiguration config = new SSLConfiguration();
			Wire.instance(config).andStart(getContext());

			assertEquals(ReflectionUtils.getHidden(config, "protocol"), "TLSv1");
			assertEquals(ReflectionUtils.getHidden(config, "keystore"), "#jre-cacerts#");
			assertEquals(ReflectionUtils.getHidden(config, "password"), "cae3c55508eaa369d5c8870398c90a64"); // not encrypted yet!!
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
			final SSLConfiguration config = new SSLConfiguration();
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
		final ISSLPropertiesExtension properties = new SSLProperties("TLSv1", "#jre-cacerts#", "changeit");
		final SSLConfiguration config = new SSLConfiguration();
		config.configure(properties);
		assertTrue(config.isConfigured());
		assertEquals(ReflectionUtils.getHidden(config, "protocol"), "TLSv1");
		assertEquals(ReflectionUtils.getHidden(config, "keystore"), "#jre-cacerts#");
		assertEquals(ReflectionUtils.getHidden(config, "password"), "changeit");
		assertNotNull(ReflectionUtils.getHidden(config, "hostnameVerifier"));
		assertEquals(ReflectionUtils.getHidden(config, "hostnameVerifier").getClass(),
				SSLConfiguration.StrictHostnameVerifier.class);
	}

	public void testLocateKeystoreJreCacertsEncryptedPassword() {
		printTestName();
		final ISSLPropertiesExtension properties = new SSLProperties("TLSv1", "#jre-cacerts#",
				"cae3c55508eaa369d5c8870398c90a64", "true");
		final SSLConfiguration config = new SSLConfiguration();
		config.configure(properties);
		assertTrue(config.isConfigured());
		assertEquals(ReflectionUtils.getHidden(config, "protocol"), "TLSv1");
		assertEquals(ReflectionUtils.getHidden(config, "keystore"), "#jre-cacerts#");
		assertEquals(ReflectionUtils.getHidden(config, "password"), "cae3c55508eaa369d5c8870398c90a64"); // not encrypted yet!!
		assertNotNull(ReflectionUtils.getHidden(config, "hostnameVerifier"));
		assertEquals(ReflectionUtils.getHidden(config, "hostnameVerifier").getClass(),
				SSLConfiguration.StrictHostnameVerifier.class);
	}

	public void testGetPasswordCharsNotEncrypted() {
		final SSLConfiguration config = new SSLConfiguration();
		final char[] password = ReflectionUtils.invokeHidden(config, "getPasswordChars", "changeit", "false");
		assertEquals("changeit", new String(password));
	}

	public void testGetPasswordCharsEncrypted() {
		final SSLConfiguration config = new SSLConfiguration();
		final char[] password = ReflectionUtils.invokeHidden(config, "getPasswordChars",
				"cae3c55508eaa369d5c8870398c90a64", "true");
		assertEquals("changeit", new String(password));
	}

	public void testLocateKeystoreJreCacertsAndCustomHostnameVerifier() {
		printTestName();
		final HostnameVerifier hostnameVerifier = new TestHostnameVerifier();
		final ISSLPropertiesExtension properties = new SSLProperties("TLSv1", "#jre-cacerts#", "changeit",
				hostnameVerifier);
		final SSLConfiguration config = new SSLConfiguration();
		config.configure(properties);
		assertTrue(config.isConfigured());
		assertEquals(ReflectionUtils.getHidden(config, "protocol"), "TLSv1");
		assertEquals(ReflectionUtils.getHidden(config, "keystore"), "#jre-cacerts#");
		assertEquals(ReflectionUtils.getHidden(config, "password"), "changeit");
		assertEquals(ReflectionUtils.getHidden(config, "hostnameVerifier"), hostnameVerifier);
	}

	public void testLocateKeystoreFile() {
		printTestName();
		final String jreDir = System.getProperty("java.home"); //$NON-NLS-1$
		final File cacertFile = new File(new File(new File(new File(jreDir), "lib"), "security"), "cacerts");

		final ISSLPropertiesExtension properties = new SSLProperties("TLSv1", cacertFile.toString(), "changeit");
		final SSLConfiguration config = new SSLConfiguration();
		config.configure(properties);
		assertTrue(config.isConfigured());
	}

	public void testLocateKeystoreResource() {
		printTestName();
		final ISSLPropertiesExtension properties = new SSLProperties("TLSv1",
				"/org/eclipse/riena/communication/core/ssl/cacerts", "changeit");
		final SSLConfiguration config = new SSLConfiguration();
		config.configure(properties);
		assertTrue(config.isConfigured());
	}

	public void testLocateKeystoreEntry() {
		printTestName();
		final ISSLPropertiesExtension properties = new SSLProperties("TLSv1", "/keystore/cacerts", "changeit");
		final SSLConfiguration config = new SSLConfiguration();
		config.configure(properties);
		assertTrue(config.isConfigured());
	}

	public void testLocateKeystoreUrl() throws IOException {
		printTestName();
		final String jreDir = System.getProperty("java.home"); //$NON-NLS-1$
		final File cacertDir = new File(new File(new File(jreDir), "lib"), "security");
		final TestServer nano = new TestServer(8888, cacertDir);

		final ISSLPropertiesExtension properties = new SSLProperties("TLSv1", "http://localhost:8888/cacerts",
				"changeit");
		final SSLConfiguration config = new SSLConfiguration();
		config.configure(properties);
		assertTrue(config.isConfigured());
		nano.stop();
	}

	private static class SSLProperties implements ISSLPropertiesExtension {

		private final String protocol;
		private final String keystore;
		private final String password;
		private final String encrypt;
		private final HostnameVerifier hostnameVerifier;

		public SSLProperties(final String protocol, final String keystore, final String password) {
			this(protocol, keystore, password, null, null);
		}

		public SSLProperties(final String protocol, final String keystore, final String password,
				final HostnameVerifier hostnameVerifier) {
			this(protocol, keystore, password, null, hostnameVerifier);
		}

		public SSLProperties(final String protocol, final String keystore, final String password, final String encrypt) {
			this(protocol, keystore, password, encrypt, null);
		}

		public SSLProperties(final String protocol, final String keystore, final String password, final String encrypt,
				final HostnameVerifier hostnameVerifier) {
			this.protocol = protocol;
			this.keystore = keystore;
			this.password = password;
			this.encrypt = encrypt;
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

		public String getEncrypt() {
			return encrypt;
		}

	}
}
