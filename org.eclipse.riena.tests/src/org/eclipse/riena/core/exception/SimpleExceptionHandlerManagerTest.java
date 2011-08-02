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

import java.util.List;

import junit.framework.Assert;

import org.eclipse.riena.core.injector.InjectionFailure;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.exceptionmanager.IExceptionHandlerExtension;
import org.eclipse.riena.internal.core.exceptionmanager.SimpleExceptionHandlerManager;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * 
 */
@NonUITestCase
public class SimpleExceptionHandlerManagerTest extends RienaTestCase {

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

	public void testAddOneHandler() {
		manager.update(new IExceptionHandlerExtension[] { createHandlerExtension("handler1", "IOE", null, null, null) });

		final Exception exception = new Exception("IOE");
		manager.handleException(exception);

		Assert.assertEquals("expected exception", "handler1", TestExceptionHandler.getHandledName());
	}

	public void testAddTwoHandlersWithNoParticularOrderAndExceptionNotHandled1() {
		manager.update(new IExceptionHandlerExtension[] {
				createHandlerExtension("handler1", "IAE", "handlerNotThere", null, null),
				createHandlerExtension("handler2", "IAE", null, null, null) });

		final List<IExceptionHandler> orderedHandlers = getOrderedHandlers();
		Assert.assertEquals("handler1", orderedHandlers.get(0).toString());
		Assert.assertEquals("handler2", orderedHandlers.get(1).toString());

		final Exception exception = new Exception("no responsibility");
		manager.handleException(exception);

		Assert.assertNull("no one should handle", TestExceptionHandler.getHandledName());
	}

	public void testAddTwoHandlersWithNoParticularOrderAndExceptionNotHandled2() {
		manager.update(new IExceptionHandlerExtension[] { createHandlerExtension("handler2", "IAE", null, null, null),
				createHandlerExtension("handler1", "IAE", "handlerNotThere", null, null) });

		final List<IExceptionHandler> orderedHandlers = getOrderedHandlers();
		Assert.assertEquals("handler2", orderedHandlers.get(0).toString());
		Assert.assertEquals("handler1", orderedHandlers.get(1).toString());

		final Exception exception = new Exception("no responsibility");
		manager.handleException(exception);

		Assert.assertNull("no one should handle", TestExceptionHandler.getHandledName());
	}

	public void testAddThreeHandlersWithParticularOrderAndExceptionHandled1() {
		manager.update(new IExceptionHandlerExtension[] {
				createHandlerExtension("handler1", "IOE", "handlerNotThere", null, null),
				createHandlerExtension("handler2", "IOE", "*", null, null),
				createHandlerExtension("handler3", "IOE", null, null, "*") });

		final List<IExceptionHandler> orderedHandlers = getOrderedHandlers();
		Assert.assertEquals("handler2", orderedHandlers.get(0).toString());
		Assert.assertEquals("handler1", orderedHandlers.get(1).toString());
		Assert.assertEquals("handler3", orderedHandlers.get(2).toString());

		final Exception exception = new Exception("IOE");
		manager.handleException(exception);

		Assert.assertEquals("handler2", TestExceptionHandler.getHandledName());
	}

	public void testAddThreeHandlersWithParticularOrderAndExceptionHandled2() {
		manager.update(new IExceptionHandlerExtension[] {
				createHandlerExtension("handler1", "IOE", "handlerNotThere", null, null),
				createHandlerExtension("handler3", "IOE", null, null, "*"),
				createHandlerExtension("handler2", "IAE", "*", null, null) });

		final List<IExceptionHandler> orderedHandlers = getOrderedHandlers();
		Assert.assertEquals("handler2", orderedHandlers.get(0).toString());
		Assert.assertEquals("handler1", orderedHandlers.get(1).toString());
		Assert.assertEquals("handler3", orderedHandlers.get(2).toString());

		final Exception exception = new Exception("IOE");
		manager.handleException(exception);

		Assert.assertEquals("handler1", TestExceptionHandler.getHandledName());
	}

	public void testAddHandlersWithConflictingBeforeAndPostHandlerAttributes() {
		try {
			manager.update(new IExceptionHandlerExtension[] {
					createHandlerExtension("handler1", "IOE", "A", "B", null), // conflict, either before or postHandlers
					createHandlerExtension("handler3", "IOE", null, null, "*"),
					createHandlerExtension("handler2", "IAE", "*", null, null) });
			fail("Exception expected");
		} catch (final InjectionFailure e) {
			Assert.assertTrue(e.getMessage().contains(
					"uses both the deprecated 'before' and new 'getPostHandler' attributes"));
		}
	}

	private IExceptionHandlerExtension createHandlerExtension(final String name, final String exceptionMessageTrigger,
			final String before, final String postHandlers, final String preHandlers) {
		return new IExceptionHandlerExtension() {

			public IExceptionHandler createExceptionHandler() {
				return new TestExceptionHandler(name, exceptionMessageTrigger);
			}

			public String getBefore() {
				return before;
			}

			public String getExceptionHandler() {
				return name;
			}

			public String getName() {
				return name;
			}

			public String getPreHandlers() {
				return preHandlers;
			}

			public String getPostHandlers() {
				return postHandlers;
			}
		};
	}

	private List<IExceptionHandler> getOrderedHandlers() {
		return (List<IExceptionHandler>) ReflectionUtils.getHidden(manager, "handlers");
	}

	//	private static class TestExceptionHandler implements IExceptionHandler {
	//
	//		private final String name;
	//
	//		TestExceptionHandler(final String name) {
	//			this.name = name;
	//		}
	//
	//		public Action handleException(final Throwable t, final String msg, final Logger logger) {
	//			return null;
	//		}
	//
	//	}

}
