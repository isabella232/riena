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
package org.eclipse.riena.core.service;

import java.util.Dictionary;
import java.util.Hashtable;

import junit.framework.TestCase;

import org.eclipse.riena.internal.tests.Activator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

/**
 * 
 */
public class InjectorTest extends TestCase {

	private BundleContext context;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		context = Activator.getContext();
	}

	@Override
	protected void tearDown() throws Exception {
		context = null;
		super.tearDown();
	}

	public void scribble() {
		Object target = null;

		new ServiceId("serviceId").injectInto(target).andStart(context);
		new ServiceId("serviceId").injectInto(target).bind("bind").unbind("unbind").andStart(context);
		new ServiceId("serviceId").useFilter("").injectInto(target).bind("bind").unbind("unbind").andStart(context);
	}

	public void testInjectDepOneObviousBindUnbindError() {
		System.out.println("testInjectDepOneObviousBindUnbindError:");
		Target target = new Target();

		DepOne depOne = new DepOne();
		ServiceRegistration reg = context.registerService(DepOne.class.getName(), depOne, null);

		try {
			new ServiceId(DepOne.class.getName()).injectInto(target).bind("baind").andStart(context);
			fail("Well, that should not have happended");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		reg.unregister();
	}

	public void testInjectDepOneNotSoObviousBindUnbindError() {
		System.out.println("testInjectDepOneNotSoObviousBindUnbindError:");
		Target target = new Target();

		DepTwo depTwo = new DepTwo();
		ServiceRegistration reg = context.registerService(DepTwo.class.getName(), depTwo, null);

		try {
			new ServiceId(DepOne.class.getName()).injectInto(target).bind("binde").unbind("entbinde").andStart(context);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		reg.unregister();
	}

	public void testInjectDepOneDefaultBindUnbind() {
		System.out.println("testInjectDepOneDefaultBindUnbind:");
		Target target = new Target();

		DepOne depOne = new DepOne();
		ServiceRegistration reg = context.registerService(DepOne.class.getName(), depOne, null);

		Injector shot = new ServiceId(DepOne.class.getName()).injectInto(target).andStart(context);
		assertEquals(1, target.count("bind", DepOne.class));

		shot.stop();
		assertEquals(0, target.count("bind", DepOne.class));

		reg.unregister();
	}

	public void testInjectDepOneAndDepTwoDefaultBindUnbind() {
		System.out.println("testInjectDepOneAndDepTwoDefaultBindUnbind:");
		Target target = new Target();

		DepOne depOne = new DepOne();
		ServiceRegistration reg1 = context.registerService(DepOne.class.getName(), depOne, null);

		Injector shot1 = new ServiceId(DepOne.class.getName()).injectInto(target).andStart(context);
		assertEquals(1, target.count("bind", DepOne.class));

		Injector shot2 = new ServiceId(DepTwo.class.getName()).injectInto(target).andStart(context);

		DepTwo depTwo = new DepTwo();
		ServiceRegistration reg2 = context.registerService(DepTwo.class.getName(), depTwo, null);

		assertEquals(1, target.count("bind", DepTwo.class));

		shot1.stop();
		assertEquals(0, target.count("bind", DepOne.class));

		shot2.stop();
		assertEquals(0, target.count("bind", DepTwo.class));

		reg1.unregister();
		reg2.unregister();
	}

	public void testInjectDepOneBindeEntbinde() {
		System.out.println("testInjectDepOneBindeEntbinde:");
		Target target = new Target();

		DepOne depOne = new DepOne();
		ServiceRegistration reg = context.registerService(DepOne.class.getName(), depOne, null);

		Injector shot = new ServiceId(DepOne.class.getName()).injectInto(target).bind("binde").unbind("entbinde")
				.andStart(context);
		assertEquals(1, target.count("binde", DepOne.class));

		shot.stop();
		assertEquals(0, target.count("binde", DepOne.class));

		reg.unregister();
	}

	public void testInjectRankedServicesServicesRegisteredBefore() {
		System.out.println("testInjectRankedServicesServicesRegisteredBefore:");
		Target target = new Target();

		IRanking rank1 = new RankingOne(0);
		ServiceRegistration reg1 = context.registerService(IRanking.class.getName(), rank1, null);

		IRanking rank2 = new RankingTwo(100);
		Dictionary<String, Object> dict = new Hashtable<String, Object>();
		dict.put(Constants.SERVICE_RANKING, Integer.valueOf(100));
		ServiceRegistration reg2 = context.registerService(IRanking.class.getName(), rank2, dict);

		Injector shot = new ServiceId(IRanking.class.getName()).useRanking().injectInto(target).andStart(context);
		assertEquals(1, target.count("bind", IRanking.class));

		assertEquals(100, target.getDepRanking());

		shot.stop();
		assertEquals(0, target.count("bind", IRanking.class));

		reg1.unregister();
		reg2.unregister();
	}

	public void testInjectRankedServicesServicesRegisteredOnTheRun() {
		System.out.println("testInjectRankedServicesServicesRegisteredOnTheRun:");
		Target target = new Target();

		IRanking rank1 = new RankingOne(0);
		ServiceRegistration reg1 = context.registerService(IRanking.class.getName(), rank1, null);

		Injector shot = new ServiceId(IRanking.class.getName()).useRanking().injectInto(target).andStart(context);
		assertEquals(1, target.count("bind", IRanking.class));
		assertEquals(0, target.getDepRanking());

		IRanking rank2 = new RankingTwo(100);
		Dictionary<String, Object> dict = new Hashtable<String, Object>();
		dict.put(Constants.SERVICE_RANKING, Integer.valueOf(100));
		ServiceRegistration reg2 = context.registerService(IRanking.class.getName(), rank2, dict);

		assertEquals(1, target.count("bind", IRanking.class));

		assertEquals(100, target.getDepRanking());

		reg2.unregister();

		assertEquals(1, target.count("bind", IRanking.class));

		assertEquals(0, target.getDepRanking());

		ServiceRegistration reg3 = context.registerService(IRanking.class.getName(), rank2, dict);

		assertEquals(1, target.count("bind", IRanking.class));

		assertEquals(100, target.getDepRanking());

		shot.stop();
		assertEquals(0, target.count("bind", IRanking.class));

		reg1.unregister();
		reg3.unregister();
	}

	public void testInjectMostSpecifBindMethod() {
		System.out.println("testInjectMostSpecifBindMethod:");
		Target target = new Target();

		DepOne depOne = new DepOneOne();
		ServiceRegistration reg = context.registerService(DepOne.class.getName(), depOne, null);

		Injector shot = new ServiceId(DepOne.class.getName()).injectInto(target).andStart(context);
		assertEquals(1, target.count("bind", DepOneOne.class));
		assertEquals(0, target.count("bind", DepOne.class));

		shot.stop();
		assertEquals(0, target.count("bind", DepOneOne.class));

		reg.unregister();
	}
}
