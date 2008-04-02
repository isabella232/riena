/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.exception;

import java.util.Hashtable;

import junit.framework.Assert;

import org.eclipse.riena.tests.RienaTestCase;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;

/**
 * 
 */
public class ExceptionHandlerManagerTest extends RienaTestCase {

	public void setUp() throws BundleException {
		startBundles("org\\.eclipse\\.riena\\.exception.*", null);
	}

	public void testGetManager() {
		BundleContext context = getContext();

		ServiceReference managerRef = context.getServiceReference(IExceptionHandlerManager.class.getName());
		Assert.assertNotNull(managerRef);

		IExceptionHandlerManager manager = (IExceptionHandlerManager) context.getService(managerRef);
		Assert.assertNotNull(manager);

	}

	public void testAddHandler() {
		BundleContext context = getContext();
		TestExceptionHandler test = new TestExceptionHandler();
		test.name = "test.scp.handler1";

		Hashtable<String, String> properties = new Hashtable<String, String>(0);
		context.registerService(IExceptionHandler.class.getName(), test, properties);

		ServiceReference managerRef = context.getServiceReference(IExceptionHandlerManager.class.getName());
		Assert.assertNotNull(managerRef);

		IExceptionHandlerManager manager = (IExceptionHandlerManager) context.getService(managerRef);
		Assert.assertNotNull(manager);

		Exception exception = new Exception("test");
		manager.handleCaught(exception);

		Assert.assertEquals("expected exception", test.throwable, exception);
	}

	public void testAddHandlerChain() {
		BundleContext context = getContext();
		TestExceptionHandler test1 = new TestExceptionHandler();
		test1.name = "test.scp.handler1";

		Hashtable<String, String> properties = new Hashtable<String, String>(0);
		context.registerService(IExceptionHandler.class.getName(), test1, properties);

		TestExceptionHandler test2 = new TestExceptionHandler();
		test2.name = "test.scp.handler2";
		test2.before = "test.scp.handler1";
		test2.action = IExceptionHandlerManager.Action.Ok;

		properties = new Hashtable<String, String>(0);
		context.registerService(IExceptionHandler.class.getName(), test2, properties);

		ServiceReference managerRef = context.getServiceReference(IExceptionHandlerManager.class.getName());
		Assert.assertNotNull(managerRef);

		IExceptionHandlerManager manager = (IExceptionHandlerManager) context.getService(managerRef);
		Assert.assertNotNull(manager);

		Exception exception = new Exception("test");
		manager.handleCaught(exception);

		Assert.assertEquals("expected exception", test2.throwable, exception);
		Assert.assertNull("expected no exception", test1.throwable);
	}

}
