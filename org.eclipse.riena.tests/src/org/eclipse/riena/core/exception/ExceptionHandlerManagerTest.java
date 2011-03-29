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
package org.eclipse.riena.core.exception;

import junit.framework.Assert;

import org.eclipse.riena.internal.core.exceptionmanager.IExceptionHandlerExtension;
import org.eclipse.riena.internal.core.exceptionmanager.SimpleExceptionHandlerManager;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * 
 */
@NonUITestCase
public class ExceptionHandlerManagerTest extends RienaTestCase {

	private SimpleExceptionHandlerManager manager;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		manager = new SimpleExceptionHandlerManager();
	}

	@Override
	public void tearDown() throws Exception {
		manager = null;
		super.tearDown();
	}

	public void testAddHandler() {
		final TestExceptionHandler testEH = new TestExceptionHandler("test.exception.handler1", null, null);
		manager.update(new IExceptionHandlerExtension[] { getTestDefinition(testEH) });

		final Exception exception = new Exception("test");
		manager.handleException(exception);

		Assert.assertEquals("expected exception", testEH.getThrowable(), exception);
	}

	public void testAddHandlerChain() {
		final TestExceptionHandler testEH1 = new TestExceptionHandler("test.exception.handler1", null, null);
		final TestExceptionHandler testEH2 = new TestExceptionHandler("test.exception.handler2",
				"test.exception.handler1", IExceptionHandler.Action.OK);
		manager.update(new IExceptionHandlerExtension[] { getTestDefinition(testEH1), getTestDefinition(testEH2) });

		final Exception exception = new Exception("test");
		manager.handleException(exception);

		Assert.assertEquals("expected exception", testEH2.getThrowable(), exception);
		Assert.assertNull("expected no exception", testEH1.getThrowable());
	}

	private IExceptionHandlerExtension getTestDefinition(final TestExceptionHandler testEH) {
		return new IExceptionHandlerExtension() {

			public IExceptionHandler createExceptionHandler() {
				return testEH;
			}

			public String getBefore() {
				return testEH.getBefore();
			}

			public String getExceptionHandler() {
				return testEH.getClass().getName();
			}

			public String getName() {
				return testEH.getName();
			}
		};
	}

}
