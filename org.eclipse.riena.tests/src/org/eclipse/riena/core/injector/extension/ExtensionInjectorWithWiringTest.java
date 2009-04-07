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
package org.eclipse.riena.core.injector.extension;

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.tests.RienaTestCase;
import org.eclipse.riena.tests.collect.NonUITestCase;

/**
 * Test the {@code ExtensionInjector} with respect to the wiring feature.
 */
@NonUITestCase
public class ExtensionInjectorWithWiringTest extends RienaTestCase {

	{
		// If you want printing remove the comments below:
		setPrint(true);
	}

	public void testWiring() {
		printTestName();
		addPluginXml(ExtensionInjectorWithWiringTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorWithWiringTest.class, "plugin_ext_wire.xml");
		ConfigurableWiredThing target = new ConfigurableWiredThing();
		ExtensionInjector injector = Inject.extension("core.test.extpoint").expectingExactly(1).into(target).andStart(
				getContext());
		IWireData data = target.getData();
		assertNotNull(data);
		Wireable.wired = false;
		assertTrue(data.createObjectTypeWithWire().isWired());
		Wireable.wired = false;
		assertFalse(data.createObjectTypeWithoutWire().isWired());
		Wireable.wired = false;
		IWireable wireable = data.createLazyObjectTypeWithWire();
		assertFalse(Wireable.wired);
		assertTrue(wireable.isWired());
		Wireable.wired = false;
		wireable = data.createLazyObjectTypeWithoutWire();
		assertFalse(Wireable.wired);
		assertFalse(wireable.isWired());
		removeExtension("core.test.extpoint.id1");
		removeExtensionPoint("core.test.extpoint");
		injector.stop();
	}

	public void testWiringWithSystemPropertySetToFalse() {
		printTestName();
		System.setProperty(ExtensionInjector.RIENA_EXTENSIONS_DONOTWIRE_SYSTEM_PROPERTY, Boolean.TRUE.toString());
		addPluginXml(ExtensionInjectorWithWiringTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorWithWiringTest.class, "plugin_ext_wire.xml");
		ConfigurableWiredThing target = new ConfigurableWiredThing();
		ExtensionInjector injector = Inject.extension("core.test.extpoint").expectingExactly(1).into(target).andStart(
				getContext());
		IWireData data = target.getData();
		assertNotNull(data);
		Wireable.wired = false;
		assertFalse(data.createObjectTypeWithWire().isWired());
		Wireable.wired = false;
		assertFalse(data.createObjectTypeWithoutWire().isWired());
		Wireable.wired = false;
		IWireable wireable = data.createLazyObjectTypeWithWire();
		assertFalse(Wireable.wired);
		assertFalse(wireable.isWired());
		Wireable.wired = false;
		wireable = data.createLazyObjectTypeWithoutWire();
		assertFalse(Wireable.wired);
		assertFalse(wireable.isWired());
		removeExtension("core.test.extpoint.id1");
		removeExtensionPoint("core.test.extpoint");
		injector.stop();
		System.clearProperty(ExtensionInjector.RIENA_EXTENSIONS_DONOTWIRE_SYSTEM_PROPERTY);
	}

}
