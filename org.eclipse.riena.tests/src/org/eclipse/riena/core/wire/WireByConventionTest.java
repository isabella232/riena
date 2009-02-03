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
package org.eclipse.riena.core.wire;

import org.eclipse.riena.internal.tests.Activator;
import org.eclipse.riena.tests.RienaTestCase;
import org.eclipse.riena.tests.collect.NonUITestCase;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * Test the {@code Wire} stuff.
 */
@NonUITestCase
public class WireByConventionTest extends RienaTestCase {

	private BundleContext context = Activator.getDefault().getContext();
	private ServiceRegistration schtonkReg;
	private ServiceRegistration stunkReg;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.tests.RienaTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		schtonkReg = context.registerService(Schtonk.class.getName(), new Schtonk(), null);
		stunkReg = context.registerService(Stunk.class.getName(), new Stunk(), null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.tests.RienaTestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		context.ungetService(schtonkReg.getReference());
		context.ungetService(stunkReg.getReference());
		super.tearDown();
	}

	public void testWiringBeanByConvention() {
		TestWbC test = new TestWbC();
		Wire.instance(test).andStart(context);
		assertTrue(test.hasSchtonk());
	}

	public void testWiringDeeplyByConvention1() {
		TestTestWbC testTest = new TestTestWbC();
		Wire.instance(testTest).andStart(context);
		assertTrue(testTest.hasSchtonk());
		assertTrue(testTest.hasStunk());
	}

	public void testWiringDeeplyByConvention2() {
		TestNoTest testNoTest = new TestNoTest();
		Wire.instance(testNoTest).andStart(context);
		assertTrue(testNoTest.hasSchtonk());
	}

}
