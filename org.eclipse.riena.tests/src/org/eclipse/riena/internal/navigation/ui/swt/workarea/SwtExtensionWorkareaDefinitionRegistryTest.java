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
package org.eclipse.riena.internal.navigation.ui.swt.workarea;

import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.navigation.ApplicationModelFailure;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.workarea.IWorkareaDefinition;
import org.eclipse.riena.ui.workarea.WorkareaDefinition;

/**
 * Tests for the SwtExtensionWorkareaDefinitionRegistry
 */
@NonUITestCase
public class SwtExtensionWorkareaDefinitionRegistryTest extends RienaTestCase {

	private static final String TEST_TYPE_ID = "TestTypeId";
	private static final String OTHER_TEST_TYPE_ID = "OtherTestTypeId";
	private static final boolean TEST_IS_VIEW_SHARED = true;
	private static final String TEST_VIEW_ID = "TestViewId";
	private static final boolean OTHER_TEST_IS_VIEW_SHARED = false;
	private static final String OTHER_TEST_VIEW_ID = "OtherTestViewId";

	@SuppressWarnings("restriction")
	private SwtExtensionWorkareaDefinitionRegistry workareaDefinitionRegistry;
	private IWorkareaDefinition workareaDefinition;

	@SuppressWarnings("restriction")
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		workareaDefinitionRegistry = new SwtExtensionWorkareaDefinitionRegistry();
		workareaDefinition = new WorkareaDefinition(TestSubModuleController.class, TEST_VIEW_ID, TEST_IS_VIEW_SHARED);
	}

	public void testRegisterDefinition() throws Exception {

		workareaDefinitionRegistry.register(TEST_TYPE_ID, workareaDefinition);

		assertSame(workareaDefinition, workareaDefinitionRegistry.getDefinition(TEST_TYPE_ID));
	}

	public void testRegisterSameDefinitionDifferentTypeId() throws Exception {

		workareaDefinitionRegistry.register(TEST_TYPE_ID, workareaDefinition);

		IWorkareaDefinition otherWorkareaDefinition = new WorkareaDefinition(TestSubModuleController.class,
				TEST_VIEW_ID, TEST_IS_VIEW_SHARED);
		workareaDefinitionRegistry.register(OTHER_TEST_TYPE_ID, otherWorkareaDefinition);

		assertSame(workareaDefinition, workareaDefinitionRegistry.getDefinition(TEST_TYPE_ID));
		assertSame(otherWorkareaDefinition, workareaDefinitionRegistry.getDefinition(OTHER_TEST_TYPE_ID));
	}

	public void testRegisterSameDefinitionSameTypeId() throws Exception {

		workareaDefinitionRegistry.register(TEST_TYPE_ID, workareaDefinition);

		IWorkareaDefinition otherWorkareaDefinition = new WorkareaDefinition(TestSubModuleController.class,
				TEST_VIEW_ID, TEST_IS_VIEW_SHARED);
		workareaDefinitionRegistry.register(TEST_TYPE_ID, otherWorkareaDefinition);

		assertSame(workareaDefinition, workareaDefinitionRegistry.getDefinition(TEST_TYPE_ID));
		assertNotSame(otherWorkareaDefinition, workareaDefinitionRegistry.getDefinition(TEST_TYPE_ID));
	}

	public void testRegisterDifferentDefinitionSameTypeId() throws Exception {

		workareaDefinitionRegistry.register(TEST_TYPE_ID, workareaDefinition);

		IWorkareaDefinition otherWorkareaDefinition = null;
		try {
			otherWorkareaDefinition = new WorkareaDefinition(OtherTestSubModuleController.class, TEST_VIEW_ID,
					TEST_IS_VIEW_SHARED);
			workareaDefinitionRegistry.register(TEST_TYPE_ID, otherWorkareaDefinition);
			fail("ApplicationModelFailure expected");
		} catch (ApplicationModelFailure expected) {
			ok("ApplicationModelFailure expected");
		}

		try {
			otherWorkareaDefinition = new WorkareaDefinition(TestSubModuleController.class, OTHER_TEST_VIEW_ID,
					TEST_IS_VIEW_SHARED);
			workareaDefinitionRegistry.register(TEST_TYPE_ID, otherWorkareaDefinition);
			fail("ApplicationModelFailure expected");
		} catch (ApplicationModelFailure expected) {
			ok("ApplicationModelFailure expected");
		}

		try {
			otherWorkareaDefinition = new WorkareaDefinition(TestSubModuleController.class, TEST_VIEW_ID,
					OTHER_TEST_IS_VIEW_SHARED);
			workareaDefinitionRegistry.register(TEST_TYPE_ID, otherWorkareaDefinition);
			fail("ApplicationModelFailure expected");
		} catch (ApplicationModelFailure expected) {
			ok("ApplicationModelFailure expected");
		}
	}

	private static class TestSubModuleController extends SubModuleController {

	}

	private static class OtherTestSubModuleController extends SubModuleController {

	}

}
