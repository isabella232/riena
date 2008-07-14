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
package org.eclipse.riena.navigation.model;

import org.eclipse.riena.internal.tests.Activator;
import org.eclipse.riena.navigation.IWorkAreaPresentationDefinition;
import org.eclipse.riena.navigation.ui.controllers.NavigationNodeViewController;
import org.eclipse.riena.tests.RienaTestCase;

/**
 * 
 */
public class WorkAreaPresentationFactoryTest extends RienaTestCase {

	/**
	 * In some test we have to sleep because of asynchronous processing.
	 */
	private static final int SLEEP_TIME = 500;

	@Override
	protected void setUp() throws Exception {

		super.setUp();

		setContext(Activator.getDefault().getContext());
	}

	public void testGetPresentationDefinition() throws InterruptedException {

		WorkAreaPresentationFactory factory = new WorkAreaPresentationFactory();

		IWorkAreaPresentationDefinition definition = factory.getPresentationDefinition("");

		assertNull(definition);
		addPluginXml(WorkAreaPresentationFactoryTest.class, "plugin_ext2.xml");

		Thread.sleep(SLEEP_TIME);
		definition = factory.getPresentationDefinition("");

		assertNull(definition);

		definition = factory.getPresentationDefinition("ki/akte");

		assertNotNull(definition);

		assertTrue(definition.isViewShared());

		assertTrue(definition.createViewController() instanceof NavigationNodeViewController);

	}

}
