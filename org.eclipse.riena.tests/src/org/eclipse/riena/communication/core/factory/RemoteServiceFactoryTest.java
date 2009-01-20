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
package org.eclipse.riena.communication.core.factory;

import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.IRemoteServiceRegistry;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.communication.core.registry.RemoteServiceRegistry;
import org.eclipse.riena.internal.tests.Activator;
import org.eclipse.riena.tests.RienaTestCase;
import org.eclipse.riena.tests.collect.NonUITestCase;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

/**
 *
 */
@NonUITestCase
public class RemoteServiceFactoryTest extends RienaTestCase {

	public RemoteServiceRegistry remoteServiceRegistry = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.tests.RienaTestCase#setUp()
	 */
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
	public void bind(IRemoteServiceRegistry rsf) {
		remoteServiceRegistry = (RemoteServiceRegistry) rsf;
	}

	public void unbind(IRemoteServiceRegistry rsf) {
		remoteServiceRegistry = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.tests.RienaTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		BundleContext context = Activator.getDefault().getContext();
		ServiceReference serviceReference;
		// hack to unregister services from previous testcases that are still hanging around
		while ((serviceReference = context.getServiceReference(IRSFTest.class.getName())) != null) {
			ServiceRegistration registration = (ServiceRegistration) ReflectionUtils.getHidden(serviceReference,
					"registration");
			registration.unregister();
		}
	}

	public void testUnregister() throws Exception {
		BundleContext context = Activator.getDefault().getContext();
		IRemoteServiceRegistration createAndRegisterProxy = new RemoteServiceFactory().createAndRegisterProxy(
				IRSFTest.class, "http://localhost", "hessian", context);
		Object service = context.getService(context.getServiceReference(IRSFTest.class.getName()));
		assertNotNull(service);
		createAndRegisterProxy.unregister();
		ServiceReference serviceReference = context.getServiceReference(IRSFTest.class.getName());
		assertNull("assuming that serviceReference is null when unregister is called", serviceReference);
		assertFalse("asssuming that service is no longer in registry if service proxy is gone", remoteServiceRegistry
				.hasServiceForUrl("http://localhost"));
	}

	public void testUnregisterForOtherBundle() throws Exception {
		BundleContext context = org.eclipse.riena.internal.communication.console.Activator.getDefault().getContext();
		IRemoteServiceRegistration createAndRegisterProxy = new RemoteServiceFactory().createAndRegisterProxy(
				IRSFTest.class, "http://localhost", "hessian", context);
		Object service = context.getService(context.getServiceReference(IRSFTest.class.getName()));
		assertNotNull(service);
		createAndRegisterProxy.unregister();
		ServiceReference serviceReference = context.getServiceReference(IRSFTest.class.getName());
		assertNull("assuming that serviceReference is null when unregister is called", serviceReference);
		assertFalse("asssuming that service is no longer in registry if service proxy is gone", remoteServiceRegistry
				.hasServiceForUrl("http://localhost"));
	}

	public void testUnregisterForOtherBundleAndStopOtherBundle() throws Exception {
		super.startBundle("org.eclipse.riena.communication.console");
		BundleContext context = org.eclipse.riena.internal.communication.console.Activator.getDefault().getContext();
		IRemoteServiceRegistration createAndRegisterProxy = new RemoteServiceFactory().createAndRegisterProxy(
				IRSFTest.class, "http://localhost", "hessian", context);
		Object service = context.getService(context.getServiceReference(IRSFTest.class.getName()));
		assertNotNull(service);
		context.getBundle().stop();
		ServiceReference serviceReference = Activator.getDefault().getContext().getServiceReference(
				IRSFTest.class.getName());
		assertNull("assuming that serviceReference is null when bundle is stopped", serviceReference);
		assertFalse("asssuming that service is no longer in registry if service proxy is gone", remoteServiceRegistry
				.hasServiceForUrl("http://localhost"));
	}

	public void testUnregisterForOtherBundleAndStopOtherBundleAndUnregister() throws Exception {
		super.startBundle("org.eclipse.riena.communication.console");
		BundleContext context = org.eclipse.riena.internal.communication.console.Activator.getDefault().getContext();
		IRemoteServiceRegistration createAndRegisterProxy = new RemoteServiceFactory().createAndRegisterProxy(
				IRSFTest.class, "http://localhost", "hessian", context);
		Object service = context.getService(context.getServiceReference(IRSFTest.class.getName()));
		assertNotNull(service);
		context.getBundle().stop();
		try {
			createAndRegisterProxy.unregister();
			fail("unregister of proxy for dead context should not be possible");
		} catch (IllegalStateException e) {
			// expected exception
		}
		ServiceReference serviceReference = Activator.getDefault().getContext().getServiceReference(
				IRSFTest.class.getName());
		assertNull("assuming that serviceReference is null when bundle is stopped and unregister is called",
				serviceReference);
		assertFalse("asssuming that service is no longer in registry if service proxy is gone", remoteServiceRegistry
				.hasServiceForUrl("http://localhost"));
	}
}
