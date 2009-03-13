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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.tests.Activator;
import org.eclipse.riena.navigation.ui.swt.views.desc.IModuleGroupViewDesc;
import org.eclipse.riena.navigation.ui.swt.views.desc.IModuleViewDesc;
import org.eclipse.riena.tests.RienaTestCase;
import org.eclipse.riena.tests.collect.UITestCase;

/**
 * Testcase for NavigationViewFactory
 */
@UITestCase
public class NavigationViewFactoryTest extends RienaTestCase {

	public void testNormalWithoutInjectionBehaviour() {
		NavigationViewFactory viewFactory = new NavigationViewFactory();

		ModuleView moduleView = viewFactory.createModuleView(new Shell());

		assertTrue(moduleView instanceof ModuleView);
		assertTrue(moduleView.getClass() == ModuleView.class);

		ModuleGroupView moduleGroupView = viewFactory.createModuleGroupView(new Shell());

		assertTrue(moduleGroupView instanceof ModuleGroupView);
		assertTrue(moduleGroupView.getClass() == ModuleGroupView.class);
	}

	public void testNormalWithInjectionBehaviour() {
		NavigationViewFactory viewFactory = new NavigationViewFactory();
		Inject
				.extension("org.eclipse.riena.navigation.ui.swt.moduleView").expectingMinMax(0, 1).useType(IModuleViewDesc.class).into( //$NON-NLS-1$
						viewFactory).andStart(Activator.getDefault().getContext());
		Inject
				.extension("org.eclipse.riena.navigation.ui.swt.moduleGroupView").expectingMinMax(0, 1).useType(IModuleGroupViewDesc.class).into( //$NON-NLS-1$
						viewFactory).andStart(Activator.getDefault().getContext());

		ModuleView moduleView = viewFactory.createModuleView(new Shell());

		assertTrue(moduleView instanceof ModuleView);
		assertTrue(moduleView.getClass() == ModuleView.class);

		ModuleGroupView moduleGroupView = viewFactory.createModuleGroupView(new Shell());

		assertTrue(moduleGroupView instanceof ModuleGroupView);
		assertTrue(moduleGroupView.getClass() == ModuleGroupView.class);
	}

	public void testConfiguredNavigationViewFactory() {
		NavigationViewFactory viewFactory = new NavigationViewFactory();
		this.addPluginXml(this.getClass(), "pluginXmlNavigationViewFactory.xml");
		Inject
				.extension("org.eclipse.riena.navigation.ui.swt.moduleView").expectingMinMax(0, 1).useType(IModuleViewDesc.class).into( //$NON-NLS-1$
						viewFactory).andStart(Activator.getDefault().getContext());
		Inject
				.extension("org.eclipse.riena.navigation.ui.swt.moduleGroupView").expectingMinMax(0, 1).useType(IModuleGroupViewDesc.class).into( //$NON-NLS-1$
						viewFactory).andStart(Activator.getDefault().getContext());

		ModuleView moduleView = viewFactory.createModuleView(new Shell());

		assertTrue(moduleView instanceof ModuleView);
		assertTrue(moduleView.getClass() == TestModuleView.class);

		ModuleGroupView moduleGroupView = viewFactory.createModuleGroupView(new Shell());

		assertTrue(moduleGroupView instanceof ModuleGroupView);
		assertTrue(moduleGroupView.getClass() == TestModuleGroupView.class);
		this.removeExtension("org.eclipse.riena.test.navigationModuleView");
		this.removeExtension("org.eclipse.riena.test.navigationModuleGroupView");

	}
}
