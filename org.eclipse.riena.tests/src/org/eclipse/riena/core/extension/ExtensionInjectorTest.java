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

import java.util.HashMap;

import org.eclipse.riena.tests.RienaTestCase;
import org.osgi.framework.BundleContext;

/**
 * 
 */
public class ExtensionInjectorTest extends RienaTestCase {

	public void scribble() {
		BundleContext context = null;
		new ExtensionId("", Object.class).expectingMinMax(0, 1).injectInto(this).andStart();
		new ExtensionId("", Object.class).expectingExactly(1).injectInto(this).bind("update").andStart();
		new ExtensionId("").injectInto(this).doNotTrack().andStart().stop();

	}

	public void testWithKnownTypeAndMultipleData() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext1.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext2.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext3.xml");
		ConfigurableThingMultipleData target = new ConfigurableThingMultipleData();
		new ExtensionId("core.test.extpoint", IData.class).injectInto(target).andStart();
		assertEquals(3, target.getData().length);
		removeExtension("core.test.extpoint.id1");
		removeExtension("core.test.extpoint.id2");
		removeExtension("core.test.extpoint.id3");
		removeExtensionPoint("core.test.extpoint");
	}

	public void testWithUnknownTypeAndMultipleData() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext1.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext2.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext3.xml");
		ConfigurableThingMultipleData target = new ConfigurableThingMultipleData();
		new ExtensionId("core.test.extpoint").injectInto(target).andStart();
		assertEquals(3, target.getData().length);
		removeExtension("core.test.extpoint.id1");
		removeExtension("core.test.extpoint.id2");
		removeExtension("core.test.extpoint.id3");
		removeExtensionPoint("core.test.extpoint");
	}

	public void testWithKnownTypeAndSingleData() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext1.xml");
		ConfigurableThingSingleData target = new ConfigurableThingSingleData();
		new ExtensionId("core.test.extpoint", IData.class).expectingExactly(1).injectInto(target).bind("configure")
				.andStart();
		assertNotNull(target.getData());
		assertTrue(target.getData().getBoolean());
		assertTrue(target.getData().isBoolean());
		assertEquals("test1", target.getData().getString());
		assertEquals(String.class, target.getData().createExecutable().getClass());
		removeExtension("core.test.extpoint.id1");
		removeExtensionPoint("core.test.extpoint");
	}

	public void testWithUnknownTypeAndSingleData() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext2.xml");
		ConfigurableThingSingleData target = new ConfigurableThingSingleData();
		new ExtensionId("core.test.extpoint").expectingExactly(1).injectInto(target).bind("configure").andStart();
		assertNotNull(target.getData());
		assertFalse(target.getData().getBoolean());
		assertFalse(target.getData().isBoolean());
		assertEquals("test2", target.getData().getString());
		assertEquals(HashMap.class, target.getData().createExecutable().getClass());
		removeExtension("core.test.extpoint.id2");
		removeExtensionPoint("core.test.extpoint");
	}

}
