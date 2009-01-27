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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.core.databinding.BindingException;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.tests.RienaTestCase;
import org.eclipse.riena.tests.collect.UITestCase;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

/**
 * Tests of the class {@link MenuRidget}.
 */
@UITestCase
public class MenuRidgetTest extends RienaTestCase {

	private Shell shell;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		shell = new Shell();
	}

	@Override
	protected void tearDown() throws Exception {
		SwtUtilities.disposeWidget(shell);

		super.tearDown();
	}

	/**
	 * Tests the method {@code checkUIControl}.
	 */
	public void testCheckUIControl() {

		MyMenuRidget ridget = new MyMenuRidget();
		ridget.checkUIControl(null);
		ok("no BindingException expected");

		Menu menu = new Menu(shell);
		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		try {
			ridget.checkUIControl(menuItem);
			fail("BindingException was expected");
		} catch (BindingException e) {
			ok("BindingException was expected");
		}
		menuItem = new MenuItem(menu, SWT.CASCADE);
		try {
			ridget.checkUIControl(menuItem);
		} catch (BindingException e) {
			fail("No BindingException was expected");
		}

	}

	/**
	 * Tests the <i>private</i> method {@code getChild}.
	 */
	public void testGetChild() {

		MenuRidget ridget = new MenuRidget();
		MenuItemRidget retRidget = ReflectionUtils.invokeHidden(ridget, "getChild", "4711");
		assertNull(retRidget);

		Menu menu = new Menu(shell);
		MenuItem menuItem1 = new MenuItem(menu, SWT.NONE);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(menuItem1, "4711");
		MenuItemRidget itemRidget = new MenuItemRidget();
		itemRidget.setUIControl(menuItem1);
		ridget.addChild(itemRidget);
		retRidget = ReflectionUtils.invokeHidden(ridget, "getChild", "4711");
		assertSame(itemRidget, retRidget);

	}

	/**
	 * Tests the <i>private</i> method {@code removeChild}.
	 */
	public void testRemoveChild() {

		MenuRidget ridget = new MenuRidget();

		Menu menu = new Menu(shell);
		MenuItem menuItem1 = new MenuItem(menu, SWT.NONE);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(menuItem1, "4711");
		MenuItemRidget itemRidget = new MenuItemRidget();
		itemRidget.setUIControl(menuItem1);
		ridget.addChild(itemRidget);

		MenuItem menuItem2 = new MenuItem(menu, SWT.NONE);
		ReflectionUtils.invokeHidden(ridget, "removeChild", menuItem2);
		assertEquals(1, ridget.getChildren().size());

		ReflectionUtils.invokeHidden(ridget, "removeChild", menuItem1);
		assertEquals(0, ridget.getChildren().size());

	}

	/**
	 * This class changes the visibility of some protected methods to public. So
	 * the methods can be tested better.
	 */
	private static class MyMenuRidget extends MenuRidget {

		@Override
		public void checkUIControl(Object uiControl) {
			super.checkUIControl(uiControl);
		}

	}

}
