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
package org.eclipse.riena.navigation.ui.swt.views;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.ui.ridgets.swt.MenuItemRidget;
import org.eclipse.riena.navigation.ui.swt.component.MenuCoolBarComposite;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

/**
 * Tests of the class {@link SubApplicationView}.
 */
public class SubApplicationViewTest extends TestCase {

	private Shell shell;
	private SubApplicationView view;

	@Override
	protected void setUp() throws Exception {
		view = new SubApplicationView();
		shell = new Shell();
	}

	@Override
	protected void tearDown() throws Exception {
		view = null;
		SwtUtilities.disposeWidget(shell);
	}

	/**
	 * Tests the <i>private</i> method {@code createMenuItemRidget}.
	 */
	public void testCreateMenuItemRidget() {

		Menu menu = new Menu(shell);
		MenuItem item = new MenuItem(menu, SWT.NONE);

		MenuItemRidget ridget = ReflectionUtils.invokeHidden(view, "createMenuItemRidget", item);
		assertNotNull(ridget);
		assertSame(item, ridget.getUIControl());

	}

	/**
	 * Tests the <i>private</i> method {@code getItemId}.
	 */
	public void testGetItemId() {

		Menu menu = new Menu(shell);
		MenuItem item = new MenuItem(menu, SWT.NONE);

		String id = ReflectionUtils.invokeHidden(view, "getItemId", item);
		assertNull(id);

		MyContributionItem contributionItem = new MyContributionItem();
		contributionItem.setId("4711");
		item.setData(contributionItem);
		id = ReflectionUtils.invokeHidden(view, "getItemId", item);
		assertNotNull(id);
		assertEquals("4711", id);

	}

	/**
	 * Tests the <i>private</i> method {@code getMenuItemId}.
	 */
	public void testGetMenuItemId() {

		Menu menu = new Menu(shell);
		MenuItem item = new MenuItem(menu, SWT.NONE);

		String id = ReflectionUtils.invokeHidden(view, "getMenuItemId", item);
		assertNull(id);

		MyContributionItem contributionItem = new MyContributionItem();
		contributionItem.setId("4711");
		item.setData(contributionItem);
		id = ReflectionUtils.invokeHidden(view, "getMenuItemId", item);
		assertNotNull(id);
		assertEquals(IActionRidget.BASE_ID_MENUACTION + "4711", id);

	}

	/**
	 * Tests the <i>private</i> method {@code getMenuCoolBarComposites}.
	 */
	public void testGetMenuCoolBarComposites() {

		Composite comp1 = new Composite(shell, SWT.NONE);

		List<MenuCoolBarComposite> composites = ReflectionUtils.invokeHidden(view, "getMenuCoolBarComposites", shell);
		assertNotNull(composites);
		assertTrue(composites.isEmpty());

		MenuCoolBarComposite menuComposite = new MenuCoolBarComposite(comp1, SWT.NONE);

		composites = ReflectionUtils.invokeHidden(view, "getMenuCoolBarComposites", shell);
		assertNotNull(composites);
		assertFalse(composites.isEmpty());
		assertEquals(1, composites.size());
		assertSame(menuComposite, composites.get(0));

	}

	/**
	 * Tests the <i>private</i> method {@code getMenuItems}.
	 */
	public void testGetMenuItems() {

		Menu menu = new Menu(shell);
		MenuItem item = new MenuItem(menu, SWT.NONE);
		MenuItem item2 = new MenuItem(menu, SWT.NONE);
		Menu menu2 = new Menu(menu);
		MenuItem item3 = new MenuItem(menu, SWT.NONE);
		Menu menu3 = new Menu(shell);
		MenuItem item4 = new MenuItem(menu3, SWT.NONE);

		List<MenuCoolBarComposite> items = ReflectionUtils.invokeHidden(view, "getMenuItems", menu);
		assertNotNull(items);
		assertFalse(items.isEmpty());
		assertEquals(3, items.size());
		assertTrue(items.contains(item));
		assertTrue(items.contains(item2));
		assertTrue(items.contains(item3));
		assertFalse(items.contains(menu2));
		assertFalse(items.contains(item4));

		items = ReflectionUtils.invokeHidden(view, "getMenuItems", menu3);
		assertNotNull(items);
		assertFalse(items.isEmpty());
		assertEquals(1, items.size());
		assertFalse(items.contains(item));
		assertFalse(items.contains(item2));
		assertFalse(items.contains(item3));
		assertTrue(items.contains(item4));

	}

	private class MyContributionItem extends ContributionItem {
	}

}
