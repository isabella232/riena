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
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.security.Provider;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.util.Iter;
import org.eclipse.riena.internal.communication.core.Activator;
import org.osgi.service.log.LogService;

/**
 * This class performs the configuration of the trust store for jre SSL Protocol
 * implementation.
 */
public class SSLConfiguration {

	private String protocol;
	private String keystore;
	private String password;

	private boolean configured;

	private String previousHttpsProtocol;
	private HostnameVerifier previousHostNameVerifier;
	private SSLSocketFactory previousSSLSocketFactor;

	private static final String JRE_CACERTS_MARKER = "#jre-cacerts#"; //$NON-NLS-1$
	private static final String HTTPS_PROTOCOLS_PROPERTY_KEY = "https.protocols"; //$NON-NLS-1$

	private final static Logger LOGGER = Activator.getDefault().getLogger(SSLConfiguration.class.getName());

	/**
	 * Default constructor.
	 */
	public SSLConfiguration() {
		super();
	}

	public void configure(ISSLProperties properties) {
		configured = false;

		protocol = properties.getProtocol();
		keystore = properties.getKeystore();
		password = properties.getPassword();

		LOGGER
				.log(LogService.LOG_INFO, "Configuring SSL protocol '" + protocol + "' with keystore '" + keystore
						+ "'.");

		// Check protocol & keystore
		if (keystore == null || keystore.length() == 0 || protocol == null || protocol.length() == 0) {
			// no keystore configured. Apparently no SSL used in this context.
			LOGGER.log(LogService.LOG_WARNING, "Neither keystore nor protocol given!");
			return;
		}

		// save previous value
		previousHttpsProtocol = System.getProperty(HTTPS_PROTOCOLS_PROPERTY_KEY);
		// set new value
		System.setProperty(HTTPS_PROTOCOLS_PROPERTY_KEY, protocol);

		try {
			// obtain some debug information related to security providers
			Provider[] providers = Security.getProviders();
			if (providers == null) {
				LOGGER
						.log(LogService.LOG_WARNING,
								"Security did not find any providers. This might be a problem. Check imported jar files for sunjce_provider.jar!");
			} else {
				LOGGER.log(LogService.LOG_INFO, "Security found " + providers.length + " security providers.");
				for (int i = 0; i < providers.length; i++) {
					LOGGER.log(LogService.LOG_DEBUG, "Security provider[" + i + "]: " + providers[i].getName());
				}
			}

			KeyStore keyStore = KeyStore.getInstance("JKS");
			URL keystoreUrl = getKeystoreUrl();

			if (keystoreUrl == null) {
				LOGGER.log(LogService.LOG_ERROR, "Specified keystore '" + keystore
						+ "' can not be found. SSL not initialized.");
				return;
			}

			LOGGER.log(LogService.LOG_DEBUG, "Keystore is '" + keystoreUrl + "'.");

			char[] passwordChars = password == null ? null : password.toCharArray();

			keyStore.load(keystoreUrl.openStream(), passwordChars);

			// Some debug information
			Enumeration<String> enumeration = keyStore.aliases();
			if (enumeration == null) {
				LOGGER.log(LogService.LOG_ERROR, "Found no certificate.");
				throw new Exception("Found no certificate.");
			} else {
				for (String alias : Iter.able(enumeration)) {

					LOGGER.log(LogService.LOG_DEBUG, "Found certificate: " + alias);
					Certificate certificate = keyStore.getCertificate(alias);
					if (certificate instanceof X509Certificate) {
						X509Certificate x509Certificate = (X509Certificate) certificate;
						LOGGER.log(LogService.LOG_DEBUG, "  Subject: " + x509Certificate.getSubjectDN());
						LOGGER.log(LogService.LOG_DEBUG, "  Issuer : " + x509Certificate.getIssuerDN());
						LOGGER.log(LogService.LOG_DEBUG, "  Valid from " + x509Certificate.getNotBefore() + " to "
								+ x509Certificate.getNotAfter());
					} else {
						LOGGER.log(LogService.LOG_DEBUG, "  " + certificate);
					}
				}
			}

			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
			trustManagerFactory.init(keyStore);

			TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustManagers, null);

			LOGGER.log(LogService.LOG_DEBUG, "SSLContext protocol: " + sslContext.getProtocol());
			LOGGER.log(LogService.LOG_DEBUG, "SSLContext SocketFactory: " + sslContext.getSocketFactory());

			// save old value
			previousSSLSocketFactor = HttpsURLConnection.getDefaultSSLSocketFactory();
			// set new value
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

			HostnameVerifier hostNameVerifier = new HostnameVerifier() {
				public boolean verify(String hostName, SSLSession session) {
					LOGGER.log(LogService.LOG_ERROR, "Hostname '" + hostName
							+ "' does not match the certificate´s host name (" + session.getPeerHost() + ")!");
					return false;
				}
			};

			// save old value
			previousHostNameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
			// set new value
			HttpsURLConnection.setDefaultHostnameVerifier(hostNameVerifier);

			LOGGER.log(LogService.LOG_INFO, "Configuring the SSL protocol finished!");
			configured = true;
		} catch (Exception ex) {
			LOGGER.log(LogService.LOG_ERROR, "Configuration of SSL protocol failed. SSL will not work properly!", ex);
		}
	}

	/**
	 * @return
	 * @throws MalformedURLException
	 */
	private URL getKeystoreUrl() throws MalformedURLException {
		if (keystore.equals(JRE_CACERTS_MARKER)) {
			String jreDir = System.getProperty("java.home"); //$NON-NLS-1$
			LOGGER.log(LogService.LOG_DEBUG, "Attempting to load keystore from cacerts of the jre: " + jreDir);
			// walk down
			File cacertFile = new File(new File(new File(new File(jreDir), "lib"), "security"), "cacerts");
			return cacertFile.canRead() ? cacertFile.toURL() : null;
		}

		// keystore location a file?
		File keystoreFile = new File(keystore);
		if (keystoreFile.canRead())
			return keystoreFile.toURL();

		LOGGER.log(LogService.LOG_DEBUG, "Keystore " + keystore + " is not a file.");
		// maybe it is a resource?
		URL keystoreUrl = SSLConfiguration.class.getClassLoader().getResource(keystore);
		if (keystoreUrl != null)
			return keystoreUrl;

		LOGGER.log(LogService.LOG_DEBUG, "Keystore " + keystore + " is not a resource.");
		// and finally a url?
		return new URL(keystore);
	}

	public boolean isConfigured() {
		return configured;
	}

	/**
	 * Restore previous settings.
	 */
	public void restore() {
		if (previousHttpsProtocol != null)
			System.setProperty(HTTPS_PROTOCOLS_PROPERTY_KEY, previousHttpsProtocol);
		if (previousSSLSocketFactor != null)
			HttpsURLConnection.setDefaultSSLSocketFactory(previousSSLSocketFactor);
		if (previousHostNameVerifier != null)
			HttpsURLConnection.setDefaultHostnameVerifier(previousHostNameVerifier);
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SSLConfiguration: " + protocol + ", " + keystore; //$NON-NLS-1$ //$NON-NLS-2$
	}

}