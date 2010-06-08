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
package org.eclipse.riena.communication.core;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.xml.datatype.XMLGregorianCalendar;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

import org.eclipse.riena.communication.core.factory.Register;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.ManualTestCase;
import org.eclipse.riena.sample.app.common.BigIntegerContainer;
import org.eclipse.riena.sample.app.common.ITestcasesWS;

/**
 * Test the hessian bug (we use our own deserializer now) when transferring
 * {@code GregorianCalendar}.
 * 
 */
@ManualTestCase
public final class TestcasesWSITest extends RienaTestCase {

	private ITestcasesWS testcasesService;
	private IRemoteServiceRegistration regTestcases;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	public void setUp() throws Exception {
		super.setUp();
		regTestcases = Register.remoteProxy(ITestcasesWS.class).usingUrl("http://localhost:8080/hessian/TestcasesWS")
				.withProtocol("hessian").andStart(getContext());
		testcasesService = (ITestcasesWS) getContext().getService(
				getContext().getServiceReference(ITestcasesWS.class.getName()));
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		regTestcases.unregister();
		testcasesService = null;
	}

	/**
	 * nomen est omen
	 */
	public void testBigInteger() {
		BigInteger bigInteger = new BigInteger(80, new Random());
		BigInteger result = testcasesService.echoBigInteger(bigInteger);
		assertNotNull("result must not be null", result);
		assertEquals("result and echo must be equal", result, bigInteger);
	}

	public void testXMLGregorianCalendarImpl() {
		XMLGregorianCalendarImpl xmlGregorianCalendarImpl = new XMLGregorianCalendarImpl(new GregorianCalendar());
		XMLGregorianCalendar result = testcasesService.echoXMLGregorianCalendar(xmlGregorianCalendarImpl);
		assertNotNull("result must not be null", result);
		assertTrue("result must be instance of XMLGregorianCalendarImpl", result instanceof XMLGregorianCalendarImpl);
		XMLGregorianCalendarImpl result2 = (XMLGregorianCalendarImpl) result;
		assertEquals("result and echo must be equal", result2, xmlGregorianCalendarImpl);
	}

	public void testBigIntegerWithNullValue() {
		BigInteger bigInteger = null;
		BigInteger result = testcasesService.echoNullBigInteger(bigInteger);
		assertNull("result must be null", result);
		assertNull("result must be null", bigInteger);
	}

	public void testBigIntegerHashMap() {
		HashMap<String, BigInteger> hashMap = new HashMap<String, BigInteger>();
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
			BigInteger bigInteger = new BigInteger(80, random);
			hashMap.put(Integer.valueOf(random.nextInt()).toString(), bigInteger);
		}
		HashMap<String, BigInteger> result = testcasesService.echoBigIntegerHashMap(hashMap);
		assertNotNull("result must be not null", result);
		assertEquals("hashmaps must be equal", result, hashMap);

	}

	public void testBigIntegerList() {
		ArrayList<BigInteger> list = new ArrayList<BigInteger>();
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
			BigInteger bigInteger = new BigInteger(80, random);
			list.add(bigInteger);
		}
		List<BigInteger> result = testcasesService.echoBigIntegerList(list);
		assertNotNull("result must be not null", result);
		assertEquals("lists must be equal", result, list);

	}

	public void testBigIntegerContainer() {
		Random random = new Random();
		BigIntegerContainer bigIntegerContainer = new BigIntegerContainer(new BigInteger(10, random), new BigInteger(
				30, random), "name4711");
		BigIntegerContainer result = testcasesService.echoBigIntegerContainer(bigIntegerContainer);
		assertNotNull("result must not be null", result);
		assertEquals("result and echo must be equal", result, bigIntegerContainer);

	}

}