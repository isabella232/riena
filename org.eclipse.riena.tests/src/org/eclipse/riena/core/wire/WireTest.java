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

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.tests.Activator;
import org.eclipse.riena.tests.RienaTestCase;
import org.eclipse.riena.tests.collect.NonUITestCase;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * Test the {@code Wire} stuff.
 */
@NonUITestCase
public class WireTest extends RienaTestCase {

	private BundleContext context = Activator.getDefault().getContext();
	private ServiceRegistration schtonkReg;
	private ServiceRegistration stunkReg;

	public void scribble() {
		//		Wire.bean(Test.class).withWireWrap(TestWireWrap.class).registerAs(ITest.class).andStart(context);
		//		Wire.bean(Test.class).withWireWrap(TestWireWrap.class).registerAs(ITest.class).withProperties(
		//				new Hashtable<String, Object>()).andStart(context);
		Wire.type(Test.class).withWireWrap(TestWireWrap.class).andStart(context);
		Wire.type(Test.class).andStart(context);
	}

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
		Test test = new Test();
		Wire.instance(test).andStart(context);
		assertTrue(test.hasSchtonk());
	}

	public void testWiringDeeplyByConvention1() {
		TestTest testTest = new TestTest();
		Wire.instance(testTest).andStart(context);
		assertTrue(testTest.hasSchtonk());
		assertTrue(testTest.hasStunk());
	}

	public void testWiringDeeplyByConvention2() {
		TestNoTest testNoTest = new TestNoTest();
		Wire.instance(testNoTest).andStart(context);
		assertTrue(testNoTest.hasSchtonk());
	}

	//	public void testWiringBeanClassNameWithExplicitRegister() {
	//		Wire.bean(Test.class.getName()).registerAs(ITest.class).andStart(context);
	//		Object service = context.getService(context.getServiceReference(ITest.class.getName()));
	//		assertTrue(((Test) service).hasSchtonk());
	//	}
	public void testWiringBeanClassName() {
		Test test = (Test) Wire.type(Test.class.getName()).andStart(context);
		assertTrue(test.hasSchtonk());
	}

	//	public void testWiringBeanClassShortestWithExplicitRegister() {
	//		Wire.bean(Test.class).registerAs(ITest.class).andStart(context);
	//		Object service = context.getService(context.getServiceReference(ITest.class.getName()));
	//		assertTrue(((Test) service).hasSchtonk());
	//	}
	public void testWiringBeanClass() {
		Test test = (Test) Wire.type(Test.class).andStart(context);
		assertTrue(test.hasSchtonk());
	}

	//	public void testWiringWithWireWrapClassWithExplicitRegister() {
	//		Wire.bean(Test.class).withWireWrap(TestWireWrap.class).registerAs(ITest.class).andStart(context);
	//		Object service = context.getService(context.getServiceReference(ITest.class.getName()));
	//		assertTrue(((Test) service).hasSchtonk());
	//	}
	public void testWiringWithWireWrapClass() {
		Test test = (Test) Wire.type(Test.class).withWireWrap(TestWireWrap.class).andStart(context);
		assertTrue(test.hasSchtonk());
	}

	//	public void testWiringWithWireWrapInstanceWithExplicitRegister() {
	//		Wire.bean(Test.class).withWireWrap(new IWireWrap() {
	//			public void wire(Object bean, BundleContext context) {
	//				Inject.service(Schtonk.class).into(bean).andStart(context);
	//			}
	//		}).registerAs(ITest.class).andStart(context);
	//		Object service = context.getService(context.getServiceReference(ITest.class.getName()));
	//		assertTrue(((Test) service).hasSchtonk());
	//	}
	public void testWiringWithWireWrapInstance() {
		Test test = (Test) Wire.type(Test.class).withWireWrap(new IWireWrap() {
			public void wire(Object bean, BundleContext context) {
				Inject.service(Schtonk.class).into(bean).andStart(context);
			}
		}).andStart(context);
		assertTrue(test.hasSchtonk());
	}
}
