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
package org.eclipse.riena.core.exception;

import junit.framework.Assert;

import org.eclipse.riena.internal.core.exceptionmanager.IExceptionHandlerDefinition;
import org.eclipse.riena.internal.core.exceptionmanager.SimpleExceptionHandlerManager;
import org.eclipse.riena.tests.RienaTestCase;

import org.osgi.framework.BundleContext;

/**
 * 
 */
public class ExceptionHandlerManagerTest extends RienaTestCase {

	private SimpleExceptionHandlerManager manager;

	public void setUp() throws Exception {
		super.setUp();
		manager = new SimpleExceptionHandlerManager();
	}

	public void tearDown() throws Exception {
		manager = null;
	}

	public void testAddHandler() {
		BundleContext context = getContext();
		TestExceptionHandler testEH = new TestExceptionHandler();
		testEH.name = "test.exception.handler1";
		manager.update(new IExceptionHandlerDefinition[] { getTestDefinition(testEH) });

		Exception exception = new Exception("test");
		manager.handleException(exception);

		Assert.assertEquals("expected exception", testEH.throwable, exception);
	}

	private IExceptionHandlerDefinition getTestDefinition(final TestExceptionHandler testEH) {
		return new IExceptionHandlerDefinition() {

			public IExceptionHandler createExceptionHandler() {
				return testEH;
			}

			public String getBefore() {
				return null;
			}

			public String getExceptionHandler() {
				return TestExceptionHandler.class.getName();
			}

			public String getName() {
				return testEH.getName();
			}
		};
	}

	public void testAddHandlerChain() {
		BundleContext context = getContext();
		TestExceptionHandler testEH1 = new TestExceptionHandler();
		testEH1.name = "test.exception.handler1";

		TestExceptionHandler testEH2 = new TestExceptionHandler();
		testEH2.name = "test.exception.handler2";
		testEH2.before = "test.exception.handler1";
		testEH2.action = IExceptionHandlerManager.Action.Ok;
		manager.update(new IExceptionHandlerDefinition[] { getTestDefinition(testEH1), getTestDefinition(testEH2) });

		Exception exception = new Exception("test");
		manager.handleException(exception);

		Assert.assertEquals("expected exception", testEH2.throwable, exception);
		Assert.assertNull("expected no exception", testEH1.throwable);
	}

}
