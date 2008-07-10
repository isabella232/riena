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

import org.eclipse.riena.navigation.INavigationNodePresentationDefiniton;
import org.eclipse.riena.navigation.INavigationNodePresentationFactory;
import org.eclipse.riena.tests.RienaTestCase;

/**
 * 
 */
public class NavigatioNodePresentationFactoryTest extends RienaTestCase {

	/**
	 * In some test we have to sleep because of asynchronous processing.
	 */
	private static final int SLEEP_TIME = 500;

	public void testGetPresentationDefinition() {

		INavigationNodePresentationFactory factory = new NavigationNodePresentationFactory();

		INavigationNodePresentationDefiniton definition = factory.getPresentationDefinition("");

		assertNull(definition);

		addPluginXml(NavigatioNodePresentationFactoryTest.class, "plugin_ext1.xml");

		definition = factory.getPresentationDefinition("");

		assertNull(definition);

		definition = factory.getPresentationDefinition("");

		assertNotNull(definition);

		assert (definition.getProvider() instanceof TestNavigationNodeProvider);

	}

}
