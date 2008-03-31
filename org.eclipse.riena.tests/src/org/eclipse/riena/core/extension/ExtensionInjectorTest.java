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

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.core.config.ConfigSymbolReplace;
import org.eclipse.riena.tests.RienaTestCase;
import org.osgi.framework.BundleContext;
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
		Inject.extension("").useType(Object.class).expectingMinMax(0, 1).into(this).andStart();
		Inject.extension("").useType(Object.class).expectingExactly(1).into(this).bind("update").andStart();
		Inject.extension("").into(this).doNotTrack().andStart().stop();
	}

	public void testConstructorConstraints() {
		try {
			Inject.extension(null);
			fail("That should not happen!");
		} catch (RuntimeException e) {
			// ok
		}
	}

	public void testUseTypeConstraints() {
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
		ExtensionInjector injector = Inject.extension("core.test.extpoint").useType(IData.class).into(target)
				.andStart();
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
		ExtensionInjector injector = Inject.extension("core.test.extpoint").into(target).andStart();
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
				.into(target).bind("configure").andStart();
		assertNotNull(target.getData());
		assertTrue(target.getData().getBoolean());
		assertTrue(target.getData().isBoolean());
		assertEquals("test1", target.getData().getString());
		assertEquals(String.class, target.getData().createExecutable().getClass());
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
				"configure").andStart();
		assertNotNull(target.getData());
		assertFalse(target.getData().getBoolean());
		assertFalse(target.getData().isBoolean());
		assertEquals("test2", target.getData().getString());
		assertEquals(HashMap.class, target.getData().createExecutable().getClass());
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
				"configure").andStart();
		assertNotNull(target.getData());
		assertTrue(target.getData().getBoolean());
		assertEquals("test4", target.getData().getString());
		assertEquals(ArrayList.class, target.getData().createExecutable().getClass());
		assertEquals(0x20, target.getData().getByte());
		assertEquals(2.7182818284590452d, target.getData().getDouble());
		assertEquals(3.14159f, target.getData().getFloat());
		assertEquals(123, target.getData().getInteger());
		assertEquals(1234567890l, target.getData().getLong());
		assertEquals(1, target.getData().getShort());
		assertEquals('#', target.getData().getCharacter());

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
		ExtensionInjector injector = Inject.extension("core.test.extpoint").useType(IData.class).into(target)
				.andStart();
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

	public void testModifyWithUnknownTypeAndSingleData() throws ConfigurationException {
		printTestName();
		ServiceReference reference = getContext().getServiceReference(ConfigurationPlugin.class.getName());
		Object service = getContext().getService(reference);
		assertTrue("Ok, test needs rework!", service instanceof ConfigSymbolReplace);
		ConfigSymbolReplace config = (ConfigSymbolReplace) service;
		Dictionary properties = new Hashtable();
		properties.put("true", "true");
		config.updated(properties);
		getContext().ungetService(reference);
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext5.xml");
		ConfigurableThingSingleData target = new ConfigurableThingSingleData();
		ExtensionInjector injector = Inject.extension("core.test.extpoint").expectingExactly(1).into(target).bind(
				"configure").andStart(getContext());
		assertNotNull(target.getData());
		assertTrue(target.getData().getBoolean());
		assertEquals("test5", target.getData().getString());
		assertEquals(String.class, target.getData().createExecutable().getClass());

		removeExtension("core.test.extpoint.id5");
		removeExtensionPoint("core.test.extpoint");
		injector.stop();
	}
}
