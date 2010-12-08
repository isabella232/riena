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
package org.eclipse.riena.communication.core.tests;

import java.util.Map;

import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.factory.Register;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.tests.Activator;
import org.eclipse.riena.sample.app.common.tests.ITestObjectsOverRemoteService;

/**
 * Testcase to test various objecttypes when the are sent over a remote service
 * wire
 */
public class Test4TestObjectsOverRemoteService extends RienaTestCase {

	private ITestObjectsOverRemoteService testObjectTypesService;
	private IRemoteServiceRegistration regTestObjectTypesService;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		regTestObjectTypesService = Register.remoteProxy(ITestObjectsOverRemoteService.class)
				.usingUrl("http://localhost:8080/hessian/TestObjectTypesWS").withProtocol("hessian")
				.andStart(Activator.getDefault().getContext());
		testObjectTypesService = (ITestObjectsOverRemoteService) Activator
				.getDefault()
				.getContext()
				.getService(
						Activator.getDefault().getContext()
								.getServiceReference(ITestObjectsOverRemoteService.class.getName()));
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		regTestObjectTypesService.unregister();
		testObjectTypesService = null;
		super.tearDown();
	}

	public void testRegularObject() {
		final Object object = testObjectTypesService.returnObject(0);
		assertNotNull(object);
	}

	public void testRegularMap() {
		final Object object = testObjectTypesService.returnMap(0);
		assertNotNull(object);
		assertTrue(object instanceof Map);
		final Map<?, ?> map = (Map<?, ?>) object;
		assertEquals(map.keySet().size(), 1);
		assertTrue(map.keySet().toArray()[0] instanceof String);
		assertTrue(map.values().toArray()[0] instanceof String);
	}

	public void testInvalidObject() {
		try {
			final Object object = testObjectTypesService.returnObject(1);
		} catch (final Exception e) {
			assertTrue(e instanceof RuntimeException);
			return;
		}
		fail("should never get here");
	}

	public void testInvalidMap() {
		try {
			final Object object = testObjectTypesService.returnMap(1);
		} catch (final Exception e) {
			assertTrue(e instanceof RuntimeException);
			return;
		}
		fail("should never get here");
	}
}
