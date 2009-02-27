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
package org.eclipse.riena.internal.ui.ridgets.swt;

import junit.framework.TestCase;

import org.eclipse.riena.tests.collect.UITestCase;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

/**
 * Tests of the class {@link MenuItemProperties}.
 */
@UITestCase
public class MenuItemPropertiesTest extends TestCase {

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
	 * Tests the constructor of {@code MenuItemProperties}.
	 */
	public void testMenuItemProperties() {

		MenuItemRidget ridget = new MenuItemRidget();
		Menu menu = new Menu(shell);
		new MenuItem(menu, SWT.PUSH);
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(item, "item1");
		ridget.setUIControl(item);

		MyMenuItemProperties itemProperties = new MyMenuItemProperties(ridget);
		assertSame(ridget, itemProperties.getRidget());
		assertSame(menu, itemProperties.getParent());
		assertEquals(1, itemProperties.getIndex());

	}

	/**
	 * Tests the method {@code createItem(Menu)}
	 */
	public void testCreateItemParent() {

		MenuItemRidget ridget = new MenuItemRidget();
		Menu menu = new Menu(shell);
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText("MenuItem0815");
		ridget.setUIControl(item);

		MyMenuItemProperties itemProperties = new MyMenuItemProperties(ridget);
		Menu menu2 = new Menu(shell);
		MenuItem item2 = itemProperties.createItem(menu2);
		assertSame(item2, ridget.getUIControl());
		assertSame(menu2, item2.getParent());
		assertEquals(item.getText(), item2.getText());

	}

	/**
	 * Tests the method {@code createItem()}
	 */
	public void testCreateItem() {

		MenuItemRidget ridget = new MenuItemRidget();
		Menu menu = new Menu(shell);
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		String text = "MenuItem0815";
		item.setText(text);
		ridget.setUIControl(item);

		MyMenuItemProperties itemProperties = new MyMenuItemProperties(ridget);
		item.dispose();
		assertEquals(0, menu.getItemCount());

		MenuItem item2 = itemProperties.createItem();
		assertSame(item2, ridget.getUIControl());
		assertSame(menu, item2.getParent());
		assertEquals(1, menu.getItemCount());
		assertEquals(text, item2.getText());

	}

	/**
	 * This class changes the visibility of some protected methods to public. So
	 * the methods can be tested better.
	 */
	private static class MyMenuItemProperties extends MenuItemProperties {

		public MyMenuItemProperties(MenuItemRidget ridget) {
			super(ridget);
		}

		@Override
		public Menu getParent() {
			return super.getParent();
		}

		@Override
		public int getIndex() {
			return super.getIndex();
		}

		@Override
		public MenuItemRidget getRidget() {
			return super.getRidget();
		}

		@Override
		public MenuItem createItem(Menu parent) {
			return super.createItem(parent);
		}

		@Override
		public MenuItem createItem() {
			return super.createItem();
		}

	}

}
