/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.communication.core.factory;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.IRemoteServiceRegistry;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.communication.core.registry.RemoteServiceRegistry;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.internal.tests.Activator;

/**
 *
 */
@NonUITestCase
public class RemoteServiceFactoryTest extends RienaTestCase {

	private RemoteServiceRegistry remoteServiceRegistry = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.tests.RienaTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Inject.service(IRemoteServiceRegistry.class).into(this).andStart(Activator.getDefault().getContext());
	}

	/**
	 * We assume here that we get the concrete RemoteServiceRegistry
	 * implementation that Riena supplies
	 * 
	 * @param rsf
	 */
	public void bind(final IRemoteServiceRegistry rsf) {
		remoteServiceRegistry = (RemoteServiceRegistry) rsf;
	}

	public void unbind(final IRemoteServiceRegistry rsf) {
		remoteServiceRegistry = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.tests.RienaTestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		final BundleContext context = Activator.getDefault().getContext();
		ServiceReference serviceReference;
		// hack to unregister services from previous testcases that are still hanging around
		while ((serviceReference = context.getServiceReference(IRSFTest.class.getName())) != null) {
			final ServiceRegistration registration = (ServiceRegistration) ReflectionUtils.getHidden(serviceReference,
					"registration");
			registration.unregister();
		}
	}

	public void testUnregister() throws Exception {
		final BundleContext context = Activator.getDefault().getContext();
		final IRemoteServiceRegistration createAndRegisterProxy = new RemoteServiceFactory().createAndRegisterProxy(
				IRSFTest.class, "http://localhost", "hessian", context);
		final Object service = context.getService(context.getServiceReference(IRSFTest.class.getName()));
		assertNotNull(service);
		createAndRegisterProxy.unregister();
		final ServiceReference serviceReference = context.getServiceReference(IRSFTest.class.getName());
		assertNull("assuming that serviceReference is null when unregister is called", serviceReference);
		assertFalse("asssuming that service is no longer in registry if service proxy is gone",
				remoteServiceRegistry.hasServiceForUrl("http://localhost"));
	}

	public void testUnregisterForOtherBundle() throws Exception {
		final BundleContext context = org.eclipse.riena.internal.communication.console.Activator.getDefault()
				.getContext();
		final IRemoteServiceRegistration createAndRegisterProxy = new RemoteServiceFactory().createAndRegisterProxy(
				IRSFTest.class, "http://localhost", "hessian", context);
		final Object service = context.getService(context.getServiceReference(IRSFTest.class.getName()));
		assertNotNull(service);
		createAndRegisterProxy.unregister();
		final ServiceReference serviceReference = context.getServiceReference(IRSFTest.class.getName());
		assertNull("assuming that serviceReference is null when unregister is called", serviceReference);
		assertFalse("asssuming that service is no longer in registry if service proxy is gone",
				remoteServiceRegistry.hasServiceForUrl("http://localhost"));
	}

	public void testUnregisterForOtherBundleAndStopOtherBundle() throws Exception {
		super.startBundle("org.eclipse.riena.communication.console");
		final BundleContext context = org.eclipse.riena.internal.communication.console.Activator.getDefault()
				.getContext();
		new RemoteServiceFactory().createAndRegisterProxy(IRSFTest.class, "http://localhost", "hessian", context);
		final Object service = context.getService(context.getServiceReference(IRSFTest.class.getName()));
		assertNotNull(service);
		context.getBundle().stop();
		final ServiceReference serviceReference = Activator.getDefault().getContext()
				.getServiceReference(IRSFTest.class.getName());
		assertNull("assuming that serviceReference is null when bundle is stopped", serviceReference);
		assertFalse("asssuming that service is no longer in registry if service proxy is gone",
				remoteServiceRegistry.hasServiceForUrl("http://localhost"));
	}

	public void testUnregisterForOtherBundleAndStopOtherBundleAndUnregister() throws Exception {
		super.startBundle("org.eclipse.riena.communication.console");
		final BundleContext context = org.eclipse.riena.internal.communication.console.Activator.getDefault()
				.getContext();
		final IRemoteServiceRegistration createAndRegisterProxy = new RemoteServiceFactory().createAndRegisterProxy(
				IRSFTest.class, "http://localhost", "hessian", context);
		final Object service = context.getService(context.getServiceReference(IRSFTest.class.getName()));
		assertNotNull(service);
		context.getBundle().stop();
		try {
			createAndRegisterProxy.unregister();
			fail("unregister of proxy for dead context should not be possible");
		} catch (final IllegalStateException e) {
			ok("expected exception");
		}
		final ServiceReference serviceReference = Activator.getDefault().getContext()
				.getServiceReference(IRSFTest.class.getName());
		assertNull("assuming that serviceReference is null when bundle is stopped and unregister is called",
				serviceReference);
		assertFalse("asssuming that service is no longer in registry if service proxy is gone",
				remoteServiceRegistry.hasServiceForUrl("http://localhost"));
	}
}
