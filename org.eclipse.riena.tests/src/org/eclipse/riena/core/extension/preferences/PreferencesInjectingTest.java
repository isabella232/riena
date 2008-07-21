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
package org.eclipse.riena.core.extension.preferences;

import java.util.Arrays;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.tests.Activator;
import org.eclipse.riena.tests.RienaTestCase;

/**
 *
 */
public class PreferencesInjectingTest extends RienaTestCase {

	private IPreferencesDesc[] prefs;

	public void update(IPreferencesDesc[] prefs) {
		this.prefs = prefs;
	}

	public void testPrerencesInjection() {
		Inject.extension("org.eclipse.core.runtime.preferences").into(this).andStart(
				Activator.getDefault().getContext());

		assertNotNull(prefs);
		int i = 0;
		for (IPreferencesDesc pref : prefs) {
			i++;
			System.out.println("Pref: " + pref.getPoint() + ", " + pref.getId() + ", " + pref.getName());
			IScopeDesc[] scopes = pref.getScope();
			for (IScopeDesc scope : scopes) {
				System.out.println("\tScope: " + scope.getName());
			}
			IInitializerDesc[] initializers = pref.getInitializer();
			for (IInitializerDesc initializer : initializers) {
				System.out.println("\tInit: " + initializer.createInitializer().getClass());
			}
			IModifierDesc[] modifiers = pref.getModifier();
			for (IModifierDesc modifier : modifiers) {
				System.out.println("\tModifier: " + modifier.createModifier().getClass());
			}
		}
		System.out.println(i);
	}

	public void testPreferencesInjectionManual() {
		IExtensionRegistry extensionRegistry = RegistryFactory.getRegistry();
		IConfigurationElement[] elements = extensionRegistry
				.getConfigurationElementsFor("org.eclipse.core.runtime.preferences");
		System.out.println(elements.length);
		for (IConfigurationElement element : elements) {
			System.out.println("Prefs: " + element.getName() + ": " + Arrays.toString(element.getAttributeNames())
					+ ", Contrib: " + element.getContributor());
		}

		// ExtensionMapper code
		final IExtensionPoint extensionPoint = extensionRegistry
				.getExtensionPoint("org.eclipse.core.runtime.preferences");
		if (extensionPoint == null)
			throw new IllegalArgumentException("Extension point " + "org.eclipse.core.runtime.preferences"
					+ " does not exist");

		final IExtension[] extensions = extensionPoint.getExtensions();
		System.out.println("Exts: " + extensions.length);
		for (IExtension extension : extensions) {
			System.out.println("Ext: " + extension.getLabel());
			for (IConfigurationElement element : extension.getConfigurationElements()) {
				System.out.println("Element: " + element.getName() + ", "
						+ Arrays.toString(element.getAttributeNames()));
			}
		}
	}
}
