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

import org.eclipse.riena.tests.RienaTestCase;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

/**
 * 
 */
public class InjectorTest extends RienaTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void scribble() {
		Object target = null;

		new ServiceId("serviceId").injectInto(target).andStart(getContext());
		new ServiceId("serviceId").injectInto(target).bind("bind").unbind("unbind").andStart(getContext());
		new ServiceId("serviceId").useFilter("").injectInto(target).bind("bind").unbind("unbind")
				.andStart(getContext());
	}

	public void testInjectDepOneObviousBindUnbindError() {
		printTestName();
		Target target = new Target();

		DepOne depOne = new DepOne();
		ServiceRegistration reg = getContext().registerService(DepOne.class.getName(), depOne, null);

		try {
			new ServiceId(DepOne.class.getName()).injectInto(target).bind("baind").andStart(getContext());
			fail("Well, that should not have happended");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		reg.unregister();
	}

	public void testInjectDepOneNotSoObviousBindUnbindError() {
		printTestName();
		Target target = new Target();

		DepTwo depTwo = new DepTwo();
		ServiceRegistration reg = getContext().registerService(DepTwo.class.getName(), depTwo, null);

		try {
			new ServiceId(DepOne.class.getName()).injectInto(target).bind("binde").unbind("entbinde").andStart(
					getContext());
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		reg.unregister();
	}

	public void testInjectDepOneDefaultBindUnbind() {
		printTestName();
		Target target = new Target();

		DepOne depOne = new DepOne();
		ServiceRegistration reg = getContext().registerService(DepOne.class.getName(), depOne, null);

		Injector shot = new ServiceId(DepOne.class.getName()).injectInto(target).andStart(getContext());
		assertEquals(1, target.count("bind", DepOne.class));

		shot.stop();
		assertEquals(0, target.count("bind", DepOne.class));

		reg.unregister();
	}

	public void testInjectDepOneAndDepTwoDefaultBindUnbind() {
		printTestName();
		Target target = new Target();

		DepOne depOne = new DepOne();
		ServiceRegistration reg1 = getContext().registerService(DepOne.class.getName(), depOne, null);

		Injector shot1 = new ServiceId(DepOne.class.getName()).injectInto(target).andStart(getContext());
		assertEquals(1, target.count("bind", DepOne.class));

		Injector shot2 = new ServiceId(DepTwo.class.getName()).injectInto(target).andStart(getContext());

		DepTwo depTwo = new DepTwo();
		ServiceRegistration reg2 = getContext().registerService(DepTwo.class.getName(), depTwo, null);

		assertEquals(1, target.count("bind", DepTwo.class));

		shot1.stop();
		assertEquals(0, target.count("bind", DepOne.class));

		shot2.stop();
		assertEquals(0, target.count("bind", DepTwo.class));

		reg1.unregister();
		reg2.unregister();
	}

	public void testInjectDepOneBindeEntbinde() {
		printTestName();
		Target target = new Target();

		DepOne depOne = new DepOne();
		ServiceRegistration reg = getContext().registerService(DepOne.class.getName(), depOne, null);

		Injector shot = new ServiceId(DepOne.class.getName()).injectInto(target).bind("binde").unbind("entbinde")
				.andStart(getContext());
		assertEquals(1, target.count("binde", DepOne.class));

		shot.stop();
		assertEquals(0, target.count("binde", DepOne.class));

		reg.unregister();
	}

	public void testInjectRankedServicesServicesRegisteredBefore() {
		printTestName();
		Target target = new Target();

		IRanking rank1 = new RankingOne(0);
		ServiceRegistration reg1 = getContext().registerService(IRanking.class.getName(), rank1, null);

		IRanking rank2 = new RankingTwo(100);
		Dictionary<String, Object> dict = new Hashtable<String, Object>();
		dict.put(Constants.SERVICE_RANKING, Integer.valueOf(100));
		ServiceRegistration reg2 = getContext().registerService(IRanking.class.getName(), rank2, dict);

		Injector shot = new ServiceId(IRanking.class.getName()).useRanking().injectInto(target).andStart(getContext());
		assertEquals(1, target.count("bind", IRanking.class));

		assertEquals(100, target.getDepRanking());

		shot.stop();
		assertEquals(0, target.count("bind", IRanking.class));

		reg1.unregister();
		reg2.unregister();
	}

	public void testInjectRankedServicesServicesRegisteredOnTheRun() {
		printTestName();
		Target target = new Target();

		IRanking rank1 = new RankingOne(0);
		ServiceRegistration reg1 = getContext().registerService(IRanking.class.getName(), rank1, null);

		Injector shot = new ServiceId(IRanking.class.getName()).useRanking().injectInto(target).andStart(getContext());
		assertEquals(1, target.count("bind", IRanking.class));
		assertEquals(0, target.getDepRanking());

		IRanking rank2 = new RankingTwo(100);
		Dictionary<String, Object> dict = new Hashtable<String, Object>();
		dict.put(Constants.SERVICE_RANKING, Integer.valueOf(100));
		ServiceRegistration reg2 = getContext().registerService(IRanking.class.getName(), rank2, dict);

		assertEquals(1, target.count("bind", IRanking.class));

		assertEquals(100, target.getDepRanking());

		reg2.unregister();

		assertEquals(1, target.count("bind", IRanking.class));

		assertEquals(0, target.getDepRanking());

		ServiceRegistration reg3 = getContext().registerService(IRanking.class.getName(), rank2, dict);

		assertEquals(1, target.count("bind", IRanking.class));

		assertEquals(100, target.getDepRanking());

		shot.stop();
		assertEquals(0, target.count("bind", IRanking.class));

		reg1.unregister();
		reg3.unregister();
	}

	public void testInjectMostSpecifBindMethod() {
		printTestName();
		Target target = new Target();

		DepOne depOne = new DepOneOne();
		ServiceRegistration reg = getContext().registerService(DepOne.class.getName(), depOne, null);

		Injector shot = new ServiceId(DepOne.class.getName()).injectInto(target).andStart(getContext());
		assertEquals(1, target.count("bind", DepOneOne.class));
		assertEquals(0, target.count("bind", DepOne.class));

		shot.stop();
		assertEquals(0, target.count("bind", DepOneOne.class));

		reg.unregister();
	}

	public void testFilterTest() {
		printTestName();
		Target target = new Target();

		DepOne depOne = new DepOneOne();
		Hashtable<String, String> props = new Hashtable<String, String>();
		props.put("x", "y");
		ServiceRegistration reg = getContext().registerService(DepOne.class.getName(), depOne, props);

		Injector shot = new ServiceId(DepOne.class.getName()).useFilter("(x=y)").injectInto(target).andStart(
				getContext());
		assertEquals(1, target.count("bind", DepOneOne.class));

		shot.stop();
		assertEquals(0, target.count("bind", DepOneOne.class));

		reg.unregister();
	}
}
