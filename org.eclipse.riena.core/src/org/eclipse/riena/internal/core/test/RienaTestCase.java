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
package org.eclipse.riena.internal.core.test;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import org.eclipse.core.internal.registry.ExtensionRegistry;
import org.eclipse.core.runtime.ContributorFactoryOSGi;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryEventListener;
import org.eclipse.core.runtime.RegistryFactory;

import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.internal.core.ignore.IgnoreFindBugs;

/**
 * Base class for test cases.<br>
 * It extends the {@link junit.framework.TestCase} with a few helpers.
 * 
 * @author campo
 * 
 */
// this is for org.eclipse.core.internal.registry.ExtensionRegistry
@SuppressWarnings("restriction")
public abstract class RienaTestCase extends TestCase {

	// Keep track of services and and corresponding service references.
	private final Map<Object, ServiceReference> services = new HashMap<Object, ServiceReference>();
	// Do not access this field directly! Use the getter getContext() because this does a lazy initialization.
	private BundleContext context;
	private boolean print;

	/**
	 * 
	 */
	public RienaTestCase() {
		super();
	}

	/**
	 * @param name
	 */
	public RienaTestCase(String name) {
		super(name);
	}

	/**
	 * A counterpart to Assert.fail() that may be invoked to indicate that
	 * everything is fine and that the test should continue. May be used e.g. in
	 * an otherwise empty catch block that handles an expected exception. In
	 * this use case its advantages over a comment are that it allows a more
	 * uniform way of documentation than the numerous variations of "// ignore"
	 * and that it avoids a Checkstyle warning about the empty block.
	 */
	protected void ok() {
		// nothing to do, everything is OK...
	}

	/**
	 * A counterpart to Assert.fail(String) that may be invoked to indicate that
	 * everything is fine and that the test should continue.
	 * 
	 * @see #ok()
	 * 
	 * @param message
	 *            A message explaining why nothing is wrong.
	 */
	protected void ok(String message) {
		ok();
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
		for (ServiceReference reference : services.values()) {
			getContext().ungetService(reference);
		}

		services.clear();

		super.tearDown();
	}

	/**
	 * Return the bundle context. <br>
	 * <b>Note: </b>This method must not be called from a constructor of a test
	 * case!
	 * 
	 * @return
	 */
	protected BundleContext getContext() {
		if (context == null) {
			try {
				Bundle bundle = FrameworkUtil.getBundle(getClass());
				context = bundle.getBundleContext();
			} catch (Throwable t) {
				Nop.reason("We don´t care. Maybe it is not running as a plugin test."); //$NON-NLS-1$
			}
		}
		return context;
	}

	/**
	 * Get the file (from src-folder) for the resource within the same directory
	 * this unit test is in.
	 * 
	 * @param resource
	 * @return
	 */
	protected File getFile(String resource) {
		// TODO warning suppression. Ignoring FindBugs problem that
		// getResource(..) will return a resource relative to a
		// subclass rather than relative to this class. This is the
		// intended behavior.
		URL url = getClass().getResource(resource);
		// nested File constructors for OS independence...
		return new File(new File(new File("").getAbsolutePath(), "src"), url.getFile()); //$NON-NLS-1$ //$NON-NLS-2$
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
		if (!print) {
			return;
		}
		System.out.println(getName());
		for (int i = 0; i < getName().length(); i++) {
			System.out.print('-');
		}
		System.out.println();
	}

	/**
	 * Print the string, no CR/LF.
	 * 
	 * @param string
	 */
	protected void print(String string) {
		if (!print) {
			return;
		}
		System.out.print(string);
	}

	/**
	 * Print the string, with CR/LF.
	 * 
	 * @param string
	 */
	protected void println(String string) {
		if (!print) {
			return;
		}
		System.out.println(string);
	}

	/**
	 * Add an extension/extension point defined within the ´plugin.xml´ given
	 * with the <code>pluginResource</code> to the extension registry.
	 * 
	 * @param forLoad
	 * @param pluginResource
	 * @throws InterruptedException
	 */
	protected void addPluginXml(Class<?> forLoad, String pluginResource) {
		IExtensionRegistry registry = RegistryFactory.getRegistry();
		@IgnoreFindBugs(value = "OBL_UNSATISFIED_OBLIGATION", justification = "stream will be closed by getResourceAsStream()")
		InputStream inputStream = forLoad.getResourceAsStream(pluginResource);
		IContributor contributor = ContributorFactoryOSGi.createContributor(getContext().getBundle());
		RegistryEventListener listener = new RegistryEventListener();
		registry.addListener(listener);
		boolean success = registry.addContribution(inputStream, contributor, false, null, null,
				((ExtensionRegistry) registry).getTemporaryUserToken());
		listener.waitAdded();
		registry.removeListener(listener);
		assertTrue(success);
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
		RegistryEventListener listener = new RegistryEventListener();
		registry.addListener(listener);
		boolean success = registry.removeExtension(extension, ((ExtensionRegistry) registry).getTemporaryUserToken());
		listener.waitExtensionRemoved();
		registry.removeListener(listener);
		assertTrue(success);
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
		RegistryEventListener listener = new RegistryEventListener();
		registry.addListener(listener);
		boolean success = registry.removeExtensionPoint(extensionPoint, ((ExtensionRegistry) registry)
				.getTemporaryUserToken());
		listener.waitExtensionPointRemoved();
		registry.removeListener(listener);
		assertTrue(success);
	}

	/**
	 * Get the service for the specified <code>serviceClass</code>.
	 * 
	 * @param serviceClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T> T getService(Class<T> serviceClass) {
		ServiceReference reference = getContext().getServiceReference(serviceClass.getName());
		if (reference == null) {
			return null;
		}
		Object service = getContext().getService(reference);
		if (service == null) {
			return null;
		}
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
		if (reference == null) {
			return;
		}
		getContext().ungetService(reference);
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
						Nop
								.reason("testcase tried to stop this bundle which did not run, but we can ignore this ==> bundle is stopped already"); //$NON-NLS-1$
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
		Pattern include = Pattern.compile(includePattern);
		Pattern exclude = Pattern.compile(excludePattern);

		Bundle[] bundles = getContext().getBundles();
		for (Bundle bundle : bundles) {
			if (include.matcher(bundle.getSymbolicName()).matches()
					&& !(exclude.matcher(bundle.getSymbolicName()).matches())) {
				closure.execute(bundle);
			}
		}
	}

	protected interface IClosure {
		void execute(Bundle bundle) throws BundleException;
	}

	private static class RegistryEventListener implements IRegistryEventListener {

		private CountDownLatch added = new CountDownLatch(1);
		private CountDownLatch extensionRemoved = new CountDownLatch(1);
		private CountDownLatch extensionPointRemoved = new CountDownLatch(1);

		public void waitAdded() {
			try {
				added.await(1, TimeUnit.SECONDS);
				if (added.getCount() == 1) {
					System.err.println("Expected extension/point ´added´ has not be called."); //$NON-NLS-1$
				}
			} catch (InterruptedException e) {
				TestCase.fail("CountDownLatch failed with. " + e); //$NON-NLS-1$
			}
		}

		public void waitExtensionRemoved() {
			try {
				extensionRemoved.await(1, TimeUnit.SECONDS);
				if (extensionRemoved.getCount() == 1) {
					System.err.println("Expected extension ´removed´ has not be called."); //$NON-NLS-1$
				}
			} catch (InterruptedException e) {
				TestCase.fail("CountDownLatch failed with. " + e); //$NON-NLS-1$
			}
		}

		public void waitExtensionPointRemoved() {
			try {
				extensionPointRemoved.await(1, TimeUnit.SECONDS);
				if (extensionPointRemoved.getCount() == 1) {
					System.err.println("Expected extension point ´removed´ has not be called."); //$NON-NLS-1$
				}
			} catch (InterruptedException e) {
				TestCase.fail("CountDownLatch failed with. " + e); //$NON-NLS-1$
			}
		}

		public void added(IExtension[] extensions) {
			added.countDown();
		}

		public void added(IExtensionPoint[] extensionPoints) {
			added.countDown();
		}

		public void removed(IExtension[] extensions) {
			extensionRemoved.countDown();
		}

		public void removed(IExtensionPoint[] extensionPoints) {
			extensionPointRemoved.countDown();
		}
	};
}
