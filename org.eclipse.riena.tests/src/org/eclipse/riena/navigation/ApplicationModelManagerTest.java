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
package org.eclipse.riena.navigation;

import junit.framework.TestCase;

import org.eclipse.riena.navigation.model.ApplicationModel;

/**
 * Tests of the class <code>DefaultSwtControlRidgetMapper</code>
 */
public class ApplicationModelManagerTest extends TestCase {

	IApplicationModel model = null;

	@Override
	protected void setUp() throws Exception {
		ApplicationModelManager.clear();
		model = new ApplicationModel();
	}

	@Override
	protected void tearDown() throws Exception {
	}

	public void testAddDefaultModel() throws Exception {
		ApplicationModelManager.registerApplicationModel(model);
		IApplicationModel rModel = ApplicationModelManager.getApplicationModel();
		assertNotNull(rModel);
	}

	public void testAddNamedModel() throws Exception {
		ApplicationModelManager.registerApplicationModel(new ApplicationModel(null, "MyModel"));
		IApplicationModel rModel = ApplicationModelManager.getApplicationModel("MyModel");
		assertNotNull(rModel);
	}

	public void testApplicationModelFailure() throws Exception {
		boolean exOk = false;
		try {
			ApplicationModelManager.registerApplicationModel(model);
			ApplicationModelManager.registerApplicationModel(model);
		} catch (ApplicationModelFailure f) {
			exOk = true;
		}
		assertTrue("duplicate default model registration didn't fire a ApplicationModelFailure", exOk);
		exOk = false;
		try {
			ApplicationModelManager.registerApplicationModel(new ApplicationModel(null, "MyModel"));
			ApplicationModelManager.registerApplicationModel(new ApplicationModel(null, "MyModel"));
		} catch (ApplicationModelFailure f) {
			exOk = true;
		}
		assertTrue("duplicate named registration didn't fire a ApplicationModelFailure", exOk);
	}

	public void testGetDefaultModel() throws Exception {
		ApplicationModelManager.registerApplicationModel(model);
		ApplicationModelManager.registerApplicationModel(new ApplicationModel(null, "MyModel"));
		IApplicationModel rModel = ApplicationModelManager.getApplicationModel();
		assertNotNull(rModel);
		assertSame(model, rModel);
	}

	public void testGetDefaultModelWhenNamedAndSingle() throws Exception {
		model = new ApplicationModel(null, "MyModel");
		ApplicationModelManager.registerApplicationModel(model);
		IApplicationModel rModel = ApplicationModelManager.getApplicationModel();
		assertNotNull(rModel);
		assertSame(model, rModel);
	}

	public void testGetNamedModel() throws Exception {
		model = new ApplicationModel(null, "MyModel");
		ApplicationModelManager.registerApplicationModel(model);
		ApplicationModelManager.registerApplicationModel(new ApplicationModel(null, "MyModell"));
		ApplicationModelManager.registerApplicationModel(new ApplicationModel(null, "MyModel2"));
		IApplicationModel rModel = ApplicationModelManager.getApplicationModel("MyModel");
		assertNotNull(rModel);
		assertSame(model, rModel);
	}
}
