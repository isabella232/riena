/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.wire;

import java.lang.reflect.Method;

import org.eclipse.riena.core.injector.service.FilterInjector;
import org.eclipse.riena.core.injector.service.RankingInjector;
import org.eclipse.riena.core.injector.service.ServiceInjector;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.wire.ServiceInjectorBuilder;
import org.eclipse.riena.tests.RienaTestCase;
import org.eclipse.riena.tests.collect.NonUITestCase;

/**
 * Test the {@code ServiceInjectorBuilder}.
 */
@NonUITestCase
public class ServiceInjectorBuilderTest extends RienaTestCase {

	public void testBuildForBind1() throws NoSuchMethodException {
		Method bindMethod = ServiceInjectorBuilderTest.class.getDeclaredMethod("bind1", new Class[] { Schtonk.class });
		ServiceInjectorBuilder builder = new ServiceInjectorBuilder(this, bindMethod);
		ServiceInjector injector = builder.build();
		assertNotNull(injector);
		assertTrue(injector instanceof FilterInjector);
		assertFalse(useRanking(injector));
		assertNull(getFilter(injector));
		assertEquals(Schtonk.class.getName(), getService(injector));
		assertSame(this, getBean(injector));
		assertEquals("bind1", getBind(injector));
		assertEquals("unbind1", getUnbind(injector));
	}

	@InjectService(service = Schtonk.class)
	public void bind1(Schtonk schtonk) {
	}

	public void testBuildForBind2() throws NoSuchMethodException {
		Method bindMethod = ServiceInjectorBuilderTest.class.getDeclaredMethod("bind2", new Class[] { Schtonk.class });
		ServiceInjectorBuilder builder = new ServiceInjectorBuilder(this, bindMethod);
		ServiceInjector injector = builder.build();
		assertNotNull(injector);
		assertTrue(injector instanceof RankingInjector);
		assertTrue(useRanking(injector));
		assertNull(getFilter(injector));
		assertEquals(Schtonk.class.getName(), getService(injector));
		assertSame(this, getBean(injector));
		assertEquals("bind2", getBind(injector));
		assertEquals("entbinde", getUnbind(injector));
	}

	@InjectService(serviceName = "org.eclipse.riena.core.wire.Schtonk", useRanking = true, unbind = "entbinde")
	public void bind2(Schtonk schtonk) {
	}

	public void testBuildForBind3() throws NoSuchMethodException {
		Method bindMethod = ServiceInjectorBuilderTest.class.getDeclaredMethod("bind3", new Class[] { Schtonk.class });
		ServiceInjectorBuilder builder = new ServiceInjectorBuilder(this, bindMethod);
		ServiceInjector injector = builder.build();
		assertNotNull(injector);
		assertTrue(injector instanceof FilterInjector);
		assertFalse(useRanking(injector));
		assertEquals("(mellita = gut)", getFilter(injector));
		assertEquals(Schtonk.class.getName(), getService(injector));
		assertSame(this, getBean(injector));
		assertEquals("bind3", getBind(injector));
		assertEquals("entbinde", getUnbind(injector));
	}

	@InjectService(serviceName = "org.eclipse.riena.core.wire.Schtonk", useFilter = "(mellita = gut)", unbind = "entbinde")
	public void bind3(Schtonk schtonk) {
	}

	public void testBuildForBind4() throws NoSuchMethodException {
		Method bindMethod = ServiceInjectorBuilderTest.class.getDeclaredMethod("bind4", new Class[] { Schtonk.class });
		ServiceInjectorBuilder builder = new ServiceInjectorBuilder(this, bindMethod);
		ServiceInjector injector = builder.build();
		assertNotNull(injector);
		assertTrue(injector instanceof FilterInjector);
		assertFalse(useRanking(injector));
		assertNull(getFilter(injector));
		assertEquals(Schtonk.class.getName(), getService(injector));
		assertSame(this, getBean(injector));
		assertEquals("bind4", getBind(injector));
		assertEquals("unbind4", getUnbind(injector));
	}

	@InjectService()
	public void bind4(Schtonk schtonk) {
	}

	private boolean useRanking(ServiceInjector injector) {
		Object serviceDescriptor = ReflectionUtils.getHidden(injector, "serviceDesc");
		return ReflectionUtils.getHidden(serviceDescriptor, "ranking");
	}

	private String getFilter(ServiceInjector injector) {
		Object serviceDescriptor = ReflectionUtils.getHidden(injector, "serviceDesc");
		return ReflectionUtils.getHidden(serviceDescriptor, "filter");
	}

	private Object getService(ServiceInjector injector) {
		Object serviceDescriptor = ReflectionUtils.getHidden(injector, "serviceDesc");
		return ReflectionUtils.getHidden(serviceDescriptor, "clazz");
	}

	private Object getBean(ServiceInjector injector) {
		return ReflectionUtils.getHidden(injector, "target");
	}

	private String getBind(ServiceInjector injector) {
		return ReflectionUtils.getHidden(injector, "bindMethodName");
	}

	private String getUnbind(ServiceInjector injector) {
		return ReflectionUtils.getHidden(injector, "unbindMethodName");
	}
}
