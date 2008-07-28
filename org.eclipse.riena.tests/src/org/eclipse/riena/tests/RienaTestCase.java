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
package org.eclipse.riena.tests;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.eclipse.core.internal.registry.ExtensionRegistry;
import org.eclipse.core.runtime.ContributorFactoryOSGi;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.riena.internal.tests.Activator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;

/**
 * Base class for test cases.<br>
 * It extends the {@link junit.framework.TestCase} with a few helpers.
 * 
 * @author campo
 * 
 */
public abstract class RienaTestCase extends TestCase {

	// Keep track of services and and corresponding service references.
	private Map<Object, ServiceReference> services = new HashMap<Object, ServiceReference>();
	private BundleContext context;
	private boolean print;

	/**
	 * 
	 */
	public RienaTestCase() {
		super();
		this.context = Activator.getDefault().getContext();
	}

	/**
	 * @param name
	 */
	public RienaTestCase(String name) {
		super(name);
		this.context = Activator.getDefault().getContext();
	}

	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		services.clear();
	}

	/*
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		for (ServiceReference reference : services.values())
			context.ungetService(reference);

		services.clear();

		super.tearDown();
	}

	/**
	 * Return the bundle context.
	 * 
	 * @return
	 */
	protected BundleContext getContext() {
		return context;
	}

	/**
	 * Set the bundle context.
	 * 
	 * @return
	 */
	protected void setContext(BundleContext context) {
		this.context = context;
	}

	/**
	 * Enable/Disable printing.
	 * 
	 * @param print
	 */
	protected void setPrint(boolean print) {
		this.print = print;
	}

	/**
	 * Print the current test´s name.
	 */
	protected void printTestName() {
		if (!print)
			return;
		System.out.println(getName());
		for (int i = 0; i < getName().length(); i++)
			System.out.print('-');
		System.out.println();
	}

	/**
	 * Print the string, no CR/LF.
	 * 
	 * @param string
	 */
	protected void print(String string) {
		if (!print)
			return;
		System.out.print(string);
	}

	/**
	 * Print the string, with CR/LF.
	 * 
	 * @param string
	 */
	protected void println(String string) {
		if (!print)
			return;
		System.out.println(string);
	}

	/**
	 * Add an extension/extension point defined within the ´plugin.xml´ given
	 * with the <code>pluginResource</code> to the extension registry.
	 * 
	 * @param forLoad
	 * @param pluginResource
	 */
	protected void addPluginXml(Class<?> forLoad, String pluginResource) {
		IExtensionRegistry registry = RegistryFactory.getRegistry();
		InputStream inputStream = forLoad.getResourceAsStream(pluginResource);
		IContributor contributor = ContributorFactoryOSGi.createContributor(context.getBundle());
		assertTrue(registry.addContribution(inputStream, contributor, false, null, null, ((ExtensionRegistry) registry)
				.getTemporaryUserToken()));
	}

	/**
	 * Remove the given extension from the extension registry.
	 * 
	 * @param extensionId
	 */
	protected void removeExtension(String extensionId) {
		IExtensionRegistry registry = RegistryFactory.getRegistry();
		IExtension extension = registry.getExtension(extensionId);
		assertNotNull(extension);
		assertTrue(registry.removeExtension(extension, ((ExtensionRegistry) registry).getTemporaryUserToken()));
	}

	/**
	 * Remove the given extension from the extension registry.
	 * 
	 * @param extensionPointId
	 */
	protected void removeExtensionPoint(String extensionPointId) {
		IExtensionRegistry registry = RegistryFactory.getRegistry();
		IExtensionPoint extensionPoint = registry.getExtensionPoint(extensionPointId);
		assertNotNull(extensionPoint);
		assertTrue(registry
				.removeExtensionPoint(extensionPoint, ((ExtensionRegistry) registry).getTemporaryUserToken()));
	}

	/**
	 * Get the service for the specified <code>serviceClass</code>.
	 * 
	 * @param serviceClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T> T getService(Class<T> serviceClass) {
		ServiceReference reference = context.getServiceReference(serviceClass.getName());
		if (reference == null)
			return null;
		Object service = context.getService(reference);
		if (service == null)
			return null;
		services.put(service, reference);
		return (T) service;
	}

	/**
	 * Unget the specified <code>service</code>.
	 * 
	 * @param service
	 */
	protected void ungetService(Object service) {
		ServiceReference reference = services.get(service);
		if (reference == null)
			return;
		context.ungetService(reference);
	}

	/**
	 * Starts the bundle with the given <code>bundleName</code>.
	 * 
	 * @param bundleName
	 * @throws BundleException
	 */
	protected void startBundle(String bundleName) throws BundleException {
		startBundles(bundleName.replaceAll("\\.", "\\\\."), null); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Starts all bundles that match the <code>includePattern</code> but not the
	 * <code>excludePattern</code>. The <code>excludePattern</code> may be
	 * <code>null</code>.
	 * 
	 * @param includePattern
	 * @param excludePattern
	 * @throws BundleException
	 */
	protected void startBundles(String includePattern, String excludePattern) throws BundleException {
		doWithBundles(includePattern, excludePattern, new IClosure() {

			public void execute(Bundle bundle) throws BundleException {
				if (bundle.getState() == Bundle.RESOLVED || bundle.getState() == Bundle.STARTING /*
																								 * STARTING==
																								 * LAZY
																								 */) {
					bundle.start();
				} else {
					if (bundle.getState() == Bundle.INSTALLED) {
						throw new RuntimeException(
								"can't start required bundle because it is not RESOLVED but only INSTALLED : " //$NON-NLS-1$
										+ bundle.getSymbolicName());
					}
				}
			}
		});
	}

	/**
	 * Stops the bundle with the given <code>bundleName</code>.
	 * 
	 * @param bundleName
	 * @throws BundleException
	 */
	protected void stopBundle(String bundleName) throws BundleException {
		stopBundles(bundleName.replaceAll("\\.", "\\\\."), null); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Stops all bundles that match the <code>includePattern</code> but not the
	 * <code>excludePattern</code>. The <code>excludePattern</code> may be
	 * <code>null</code>.
	 * 
	 * @param includePattern
	 * @param excludePattern
	 * @throws BundleException
	 */
	protected void stopBundles(String includePattern, String excludePattern) throws BundleException {
		doWithBundles(includePattern, excludePattern, new IClosure() {

			public void execute(Bundle bundle) throws BundleException {
				if (bundle.getState() == Bundle.ACTIVE) {
					bundle.stop();
				} else {
					if (bundle.getState() != Bundle.UNINSTALLED) {
						throw new RuntimeException(
								"can't stop required bundle because it is not ACTIVE and not UNINSTALLED : " //$NON-NLS-1$
										+ bundle.getSymbolicName());
					}
				}
			}
		});
	}

	/**
	 * IClosure with all bundles that match the <code>includePattern</code> but
	 * not the <code>excludePattern</code> what is specified within the
	 * <code>closure</code>. The <code>excludePattern</code> may be
	 * <code>null</code>.
	 * 
	 * @param includePattern
	 * @param excludePattern
	 * @param closure
	 * @throws BundleException
	 */
	protected void doWithBundles(String includePattern, String excludePattern, IClosure closure) throws BundleException {
		if (includePattern == null) {
			throw new UnsupportedOperationException("truePattern must be set"); //$NON-NLS-1$
		}
		if (excludePattern == null) {
			excludePattern = ""; //$NON-NLS-1$
		}
		Pattern inlcude = Pattern.compile(includePattern);
		Pattern exclude = Pattern.compile(excludePattern);

		Bundle[] bundles = context.getBundles();
		for (Bundle bundle : bundles)
			if (inlcude.matcher(bundle.getSymbolicName()).matches()
					&& !(exclude.matcher(bundle.getSymbolicName()).matches()))
				closure.execute(bundle);
	}

	protected interface IClosure {
		void execute(Bundle bundle) throws BundleException;
	}
}
