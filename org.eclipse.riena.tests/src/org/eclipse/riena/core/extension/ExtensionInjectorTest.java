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
package org.eclipse.riena.core.extension;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

import org.eclipse.core.internal.registry.osgi.Activator;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.core.config.ConfigSymbolReplace;
import org.eclipse.riena.tests.RienaTestCase;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ConfigurationPlugin;

/**
 * 
 */
public class ExtensionInjectorTest extends RienaTestCase {

	/**
	 * In some test we have to sleep because of asynchronous processing.
	 */
	private static final int SLEEP_TIME = 500;

	public void scribble() {
		BundleContext context = null;
		Inject.extension("").useType(Object.class).expectingMinMax(0, 1).into(this).andStart(context);
		Inject.extension("").useType(Object.class).expectingExactly(1).into(this).bind("update").andStart(context);
		Inject.extension("").into(this).doNotTrack().useTranslation().andStart(context).stop();
	}

	public void testConstructorConstraints() {
		printTestName();
		try {
			Inject.extension(null);
			fail("That should not happen!");
		} catch (RuntimeException e) {
			// ok
		}
	}

	public void testUseTypeConstraints() {
		printTestName();
		try {
			Inject.extension("id").useType(null);
			fail("That should not happen!");
		} catch (RuntimeException e) {
			// ok
		}
		try {
			Inject.extension("id").useType(IData.class).useType(IData.class);
			fail("That should not happen!");
		} catch (RuntimeException e) {
			// ok
		}
		try {
			Inject.extension("id").useType(String.class);
			fail("That should not happen!");
		} catch (RuntimeException e) {
			// ok
		}
	}

	public void testExpectingConstraints() {
		printTestName();
		try {
			Inject.extension("id").expectingMinMax(2, 1);
			fail("That should not happen!");
		} catch (RuntimeException e) {
			// ok
		}
		try {
			Inject.extension("id").expectingMinMax(-1, 0);
			fail("That should not happen!");
		} catch (RuntimeException e) {
			// ok
		}
		try {
			Inject.extension("id").expectingMinMax(0, 0);
			fail("That should not happen!");
		} catch (RuntimeException e) {
			// ok
		}
	}

	public void testInjectIntoConstraints() {
		printTestName();
		try {
			Inject.extension("id").into(null);
			fail("That should not happen!");
		} catch (RuntimeException e) {
			// ok
		}
	}

	public void testWithKnownTypeAndMultipleData() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext1.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext2.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext3.xml");
		ConfigurableThingMultipleData target = new ConfigurableThingMultipleData();
		ExtensionInjector injector = Inject.extension("core.test.extpoint").useType(IData.class).into(target).andStart(
				Activator.getContext());
		assertEquals(3, target.getData().length);
		removeExtension("core.test.extpoint.id1");
		removeExtension("core.test.extpoint.id2");
		removeExtension("core.test.extpoint.id3");
		removeExtensionPoint("core.test.extpoint");
		injector.stop();
	}

	public void testWithUnknownTypeAndMultipleData() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext1.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext2.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext3.xml");
		ConfigurableThingMultipleData target = new ConfigurableThingMultipleData();
		ExtensionInjector injector = Inject.extension("core.test.extpoint").into(target).andStart(
				Activator.getContext());
		assertEquals(3, target.getData().length);
		removeExtension("core.test.extpoint.id1");
		removeExtension("core.test.extpoint.id2");
		removeExtension("core.test.extpoint.id3");
		removeExtensionPoint("core.test.extpoint");
		injector.stop();
	}

	public void testWithKnownTypeAndSingleData() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext1.xml");
		ConfigurableThingSingleData target = new ConfigurableThingSingleData();
		ExtensionInjector injector = Inject.extension("core.test.extpoint").useType(IData.class).expectingExactly(1)
				.into(target).bind("configure").andStart(Activator.getContext());
		assertNotNull(target.getData());
		assertTrue(target.getData().getValue().contains("And Now for Something Completely Different!"));
		assertTrue(target.getData().getRequired());
		assertTrue(target.getData().isRequired());
		assertEquals("test1", target.getData().getText());
		assertEquals(String.class, target.getData().createObjectType().getClass());
		removeExtension("core.test.extpoint.id1");
		removeExtensionPoint("core.test.extpoint");
		injector.stop();
	}

	public void testNonCachingOfCreateWithKnownTypeAndSingleData() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext1.xml");
		ConfigurableThingSingleData target = new ConfigurableThingSingleData();
		ExtensionInjector injector = Inject.extension("core.test.extpoint").useType(IData.class).expectingExactly(1)
				.into(target).bind("configure").andStart(Activator.getContext());
		assertNotNull(target.getData());
		String created1 = (String) target.getData().createObjectType();
		String created2 = (String) target.getData().createObjectType();
		assertNotSame(created1, created2);

		removeExtension("core.test.extpoint.id1");
		removeExtensionPoint("core.test.extpoint");
		injector.stop();
	}

	public void testWithUnknownTypeAndSingleData() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext2.xml");
		ConfigurableThingSingleData target = new ConfigurableThingSingleData();
		ExtensionInjector injector = Inject.extension("core.test.extpoint").expectingExactly(1).into(target).bind(
				"configure").andStart(Activator.getContext());
		assertNotNull(target.getData());
		assertFalse(target.getData().getRequired());
		assertFalse(target.getData().isRequired());
		assertEquals("test2", target.getData().getText());
		assertEquals(HashMap.class, target.getData().createObjectType().getClass());
		removeExtension("core.test.extpoint.id2");
		removeExtensionPoint("core.test.extpoint");
		injector.stop();
	}

	public void testCoerceWithUnknownTypeAndSingleData() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext4.xml");
		ConfigurableThingSingleData target = new ConfigurableThingSingleData();
		ExtensionInjector injector = Inject.extension("core.test.extpoint").expectingExactly(1).into(target).bind(
				"configure").andStart(Activator.getContext());
		assertNotNull(target.getData());
		assertTrue(target.getData().getRequired());
		assertEquals("test4", target.getData().getText());
		assertEquals(ArrayList.class, target.getData().createObjectType().getClass());
		assertEquals(0x2A, target.getData().getJustAByte());
		assertEquals(2.7182818284590452d, target.getData().getDoubleNumber());
		assertEquals(3.14159f, target.getData().getFloatNumber());
		assertEquals(123, target.getData().getIntegerNumber());
		assertEquals(1234567890l, target.getData().getLongNumber());
		assertEquals(1, target.getData().getShortNumber());
		assertEquals('#', target.getData().getDelimCharacter());

		removeExtension("core.test.extpoint.id4");
		removeExtensionPoint("core.test.extpoint");
		injector.stop();
	}

	/**
	 * This test is brittle because it deals with asynchronous events.
	 * 
	 * @throws InterruptedException
	 */
	public void testTrackingWithKnownTypeAndMultipleData() throws InterruptedException {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		ConfigurableThingMultipleData target = new ConfigurableThingMultipleData();
		ExtensionInjector injector = Inject.extension("core.test.extpoint").useType(IData.class).into(target).andStart(
				Activator.getContext());
		assertEquals(0, target.getData().length);
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext1.xml");
		Thread.sleep(SLEEP_TIME);
		assertEquals(1, target.getData().length);
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext2.xml");
		Thread.sleep(SLEEP_TIME);
		assertEquals(2, target.getData().length);
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext3.xml");
		Thread.sleep(SLEEP_TIME);
		assertEquals(3, target.getData().length);
		removeExtension("core.test.extpoint.id1");
		Thread.sleep(SLEEP_TIME);
		assertEquals(2, target.getData().length);
		removeExtension("core.test.extpoint.id2");
		Thread.sleep(SLEEP_TIME);
		assertEquals(1, target.getData().length);
		removeExtension("core.test.extpoint.id3");
		Thread.sleep(SLEEP_TIME);
		assertEquals(0, target.getData().length);
		removeExtensionPoint("core.test.extpoint");
		injector.stop();
	}

	public void testModifyWithUnknownTypeAndSingleData() throws ConfigurationException, InvalidSyntaxException {
		printTestName();
		ServiceReference[] references = getContext().getServiceReferences(ConfigurationPlugin.class.getName(), null);
		boolean configSymbolReplace = false;
		for (ServiceReference reference : references) {
			Object service = getContext().getService(reference);
			if (service instanceof ConfigSymbolReplace) {
				ConfigSymbolReplace config = (ConfigSymbolReplace) service;
				Dictionary properties = new Hashtable();
				properties.put("true", "true");
				config.updated(properties);
				configSymbolReplace = true;
			}
		}
		assertTrue("Ok, test needs rework - expected ConfigSymbolReplace is not registered!", configSymbolReplace);
		for (ServiceReference reference : references)
			getContext().ungetService(reference);
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext5.xml");
		ConfigurableThingSingleData target = new ConfigurableThingSingleData();
		ExtensionInjector injector = Inject.extension("core.test.extpoint").expectingExactly(1).into(target).bind(
				"configure").useTranslation().andStart(getContext());
		assertNotNull(target.getData());
		assertTrue(target.getData().getRequired());
		assertEquals("test5", target.getData().getText());
		assertEquals(String.class, target.getData().createObjectType().getClass());

		removeExtension("core.test.extpoint.id5");
		removeExtensionPoint("core.test.extpoint");
		injector.stop();
	}

	public void testWithUnknownTypeAndSingleDataAndOneNestedSingleElementAndTwoNestedMultipleElements() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext6.xml");
		ConfigurableThingSingleData target = new ConfigurableThingSingleData();
		ExtensionInjector injector = Inject.extension("core.test.extpoint").expectingExactly(1).into(target).bind(
				"configure").andStart(Activator.getContext());
		assertNotNull(target.getData());
		assertFalse(target.getData().getRequired());
		assertFalse(target.getData().isRequired());
		assertEquals("test6", target.getData().getText());
		assertEquals(String.class, target.getData().createObjectType().getClass());

		IData2 data2 = target.getData().getNews();
		assertNotNull(data2);
		assertEquals("rmation", data2.getInfo());

		IData3[] data3 = target.getData().getMoreData();
		assertNotNull(data3);
		assertEquals(2, data3.length);
		assertEquals("more-one", data3[0].getId());
		assertEquals("Hugo", data3[0].getName());
		assertEquals("more-two", data3[1].getId());
		assertEquals("Hella", data3[1].getName());

		removeExtension("core.test.extpoint.id6");
		removeExtensionPoint("core.test.extpoint");
		injector.stop();
	}

	public void testCachingWithUnknownTypeAndSingleDataAndOneNestedSingleElementAndTwoNestedMultipleElements() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext6.xml");
		ConfigurableThingSingleData target = new ConfigurableThingSingleData();
		ExtensionInjector injector = Inject.extension("core.test.extpoint").expectingExactly(1).into(target).bind(
				"configure").andStart(Activator.getContext());
		assertNotNull(target.getData());
		assertFalse(target.getData().getRequired());
		assertFalse(target.getData().isRequired());
		assertEquals("test6", target.getData().getText());
		assertEquals(String.class, target.getData().createObjectType().getClass());

		IData2 data21 = target.getData().getNews();
		IData2 data22 = target.getData().getNews();
		assertSame(data21, data22);

		IData3[] data31 = target.getData().getMoreData();
		IData3[] data32 = target.getData().getMoreData();
		assertSame(data31, data32);

		removeExtension("core.test.extpoint.id6");
		removeExtensionPoint("core.test.extpoint");
		injector.stop();
	}
}
