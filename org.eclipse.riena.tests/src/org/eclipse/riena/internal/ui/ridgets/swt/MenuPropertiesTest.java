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

import junit.framework.TestCase;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.tests.collect.UITestCase;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

/**
 * Tests of the class {@link MenuProperties}.
 */
@UITestCase
public class MenuPropertiesTest extends TestCase {

	private Shell shell;

	@Override
	protected void setUp() throws Exception {
		shell = new Shell();
	}

	@Override
	protected void tearDown() throws Exception {
		SwtUtilities.disposeWidget(shell);
	}

	/**
	 * Tests the constructor of {@code MenuProperties}
	 */
	public void testMenuProperties() {

		Menu menu = new Menu(shell);
		MenuItem menuItem = new MenuItem(menu, SWT.CASCADE);
		Menu menu2 = new Menu(shell, SWT.DROP_DOWN);
		menuItem.setMenu(menu2);
		MenuRidget ridget = new MenuRidget();
		ridget.setUIControl(menuItem);

		MenuProperties prop = new MenuProperties(ridget);
		Decorations menuParent = ReflectionUtils.getHidden(prop, "menuParent");
		assertSame(shell, menuParent);
		int menuStyle = ReflectionUtils.getHidden(prop, "menuStyle");
		assertEquals(menu2.getStyle(), menuStyle);
		MenuItemProperties[] children = ReflectionUtils.getHidden(prop, "children");
		assertEquals(0, children.length);

		MenuItem menuItem2 = new MenuItem(menu2, SWT.NONE);
		MenuItemRidget childRidget = new MenuItemRidget();
		childRidget.setUIControl(menuItem2);
		ridget.addChild(childRidget);
		prop = new MenuProperties(ridget);
		children = ReflectionUtils.getHidden(prop, "children");
		assertEquals(1, children.length);
		IRidget retRidget = ReflectionUtils.invokeHidden(children[0], "getRidget");
		assertSame(childRidget, retRidget);

	}

	/**
	 * Tests the method {@code createItem()}
	 */
	public void testCreateItem() {

		Menu menu = new Menu(shell);
		MenuItem menuItem = new MenuItem(menu, SWT.CASCADE);
		Menu menu2 = new Menu(shell, SWT.DROP_DOWN);
		menuItem.setMenu(menu2);
		MenuRidget ridget = new MenuRidget();
		ridget.setUIControl(menuItem);
		MenuItem menuItem2 = new MenuItem(menu2, SWT.NONE);
		menuItem2.setText("Simple menu item");
		MenuItemRidget childRidget = new MenuItemRidget();
		childRidget.setUIControl(menuItem2);
		ridget.addChild(childRidget);
		MyMenuProperties prop = new MyMenuProperties(ridget);

		MenuItem retMenuItem = prop.createItem();
		assertNotNull(retMenuItem);
		assertNotSame(menuItem, retMenuItem);
		assertNotNull(retMenuItem.getMenu());
		assertNotSame(menu2, retMenuItem.getMenu());
		assertEquals(1, retMenuItem.getMenu().getItemCount());
		MenuItem retChildMenuItem = retMenuItem.getMenu().getItems()[0];
		assertEquals(menuItem2.getText(), retChildMenuItem.getText());

	}

	/**
	 * This class changes the visibility of some protected methods to public. So
	 * the methods can be tested better.
	 */
	private static class MyMenuProperties extends MenuProperties {

		public MyMenuProperties(MenuRidget ridget) {
			super(ridget);
		}

		@Override
		public MenuItem createItem() {
			return super.createItem();
		}

	}

}
