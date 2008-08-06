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
package org.eclipse.riena.navigation.ui.swt.views.experimental;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.navigation.ui.swt.views.SWTViewBindingDelegate;
import org.eclipse.riena.ui.ridgets.uibinding.IBindingManager;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;
import org.eclipse.swt.widgets.Shell;

/**
 * Tests of the class {@link SWTViewBindingDelegate}.
 */
public class SWTViewBindingDelegateTest extends TestCase {

	private SWTViewBindingDelegate binding;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		binding = new SWTViewBindingDelegate();
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		binding = null;
	}

	/**
	 * Test of the constructor of {@code SWTViewBindingDelegate}.
	 */
	public void testSWTViewBindingDelegate() {

		IBindingManager bindingManager = ReflectionUtils.getHidden(binding, "bindingManager");
		assertNotNull(bindingManager);

		List<Object> uiControls = ReflectionUtils.getHidden(binding, "uiControls");
		assertNotNull(uiControls);
		assertTrue(uiControls.isEmpty());

	}

	/**
	 * Test of the method {@code addUIControl(Widget, String)}.
	 */
	public void testAddUIControl() {

		Shell shell = new Shell();

		binding.addUIControl(shell, "shellID");

		List<Object> uiControls = ReflectionUtils.getHidden(binding, "uiControls");
		shell = (Shell) uiControls.get(0);
		assertEquals("shellID", shell.getData(SWTBindingPropertyLocator.BINDING_PROPERTY));

		SwtUtilities.disposeWidget(shell);

	}

}
