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
import org.eclipse.riena.internal.ui.ridgets.swt.ToolItemRidget;
import org.eclipse.riena.navigation.listener.ModuleGroupNodeListener;
import org.eclipse.riena.navigation.model.NavigationProcessor;
import org.eclipse.riena.navigation.model.SubApplicationNode;
import org.eclipse.riena.navigation.ui.swt.component.MenuCoolBarComposite;
import org.eclipse.riena.tests.collect.UITestCase;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractRidgetController;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * Tests of the class {@link SubApplicationView}.
 */
@UITestCase
public class SubApplicationViewTest extends TestCase {

	private Shell shell;
	private SubApplicationView view;
	private SubApplicationNode node;

	@Override
	protected void setUp() throws Exception {
		view = new TestSubApplicationView();
		shell = new Shell();
		SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
		locator.setBindingProperty(shell, ApplicationViewAdvisor.SHELL_RIDGET_PROPERTY);

		node = new SubApplicationNode();
		node.setNavigationProcessor(new NavigationProcessor());
		view.bind(node);
	}

	@Override
	protected void tearDown() throws Exception {
		view = null;
		SwtUtilities.disposeWidget(shell);
	}

	/**
	 * Tests the <i>private</i> method {@code getItemId(MenuItem)}.
	 */
	public void testGetItemId() {

		Menu menu = new Menu(shell);
		MenuItem item = new MenuItem(menu, SWT.NONE);

		String id = ReflectionUtils.invokeHidden(view, "getItemId", item);
		assertNotNull(id);
		assertEquals("1", id);

		id = ReflectionUtils.invokeHidden(view, "getItemId", item);
		assertNotNull(id);
		assertEquals("2", id);

		MyContributionItem contributionItem = new MyContributionItem();
		contributionItem.setId("4711");
		item.setData(contributionItem);
		id = ReflectionUtils.invokeHidden(view, "getItemId", item);
		assertNotNull(id);
		assertEquals(IActionRidget.BASE_ID_MENUACTION + "4711", id);

		MenuItem item2 = new MenuItem(menu, SWT.NONE);
		SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
		locator.setBindingProperty(item2, "0815");
		id = ReflectionUtils.invokeHidden(view, "getItemId", item2);
		assertNotNull(id);
		assertEquals(IActionRidget.BASE_ID_MENUACTION + "0815", id);

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
	 * Tests the <i>private</i> method {@code getCoolBars}.
	 */
	public void testGetCoolBars() {

		Composite comp1 = new Composite(shell, SWT.NONE);

		List<CoolBar> coolBars = ReflectionUtils.invokeHidden(view, "getCoolBars", shell);
		assertNotNull(coolBars);
		assertTrue(coolBars.isEmpty());

		new MenuCoolBarComposite(comp1, SWT.NONE);
		coolBars = ReflectionUtils.invokeHidden(view, "getCoolBars", shell);
		assertNotNull(coolBars);
		assertTrue(coolBars.isEmpty());

		CoolBar coolBar = new CoolBar(comp1, SWT.NONE);
		coolBars = ReflectionUtils.invokeHidden(view, "getCoolBars", shell);
		assertNotNull(coolBars);
		assertFalse(coolBars.isEmpty());
		assertEquals(1, coolBars.size());
		assertSame(coolBar, coolBars.get(0));

	}

	/**
	 * Tests the <i>private</i> method {@code getToolBars}.
	 */
	public void testGetToolBars() {

		CoolBar coolBar = new CoolBar(shell, SWT.NONE);
		List<ToolBar> toolBars = ReflectionUtils.invokeHidden(view, "getToolBars", coolBar);
		assertNotNull(toolBars);
		assertTrue(toolBars.isEmpty());

		ToolBar toolBar = new ToolBar(coolBar, SWT.NONE);
		ToolBar toolBar2 = new ToolBar(coolBar, SWT.NONE);
		toolBars = ReflectionUtils.invokeHidden(view, "getToolBars", coolBar);
		assertNotNull(toolBars);
		assertFalse(toolBars.isEmpty());
		assertEquals(2, toolBars.size());
		assertSame(toolBar, toolBars.get(0));
		assertSame(toolBar2, toolBars.get(1));

	}

	/**
	 * Tests the <i>private</i> method {@code getAllToolItems}.
	 */
	public void testGetAllToolItems() {

		CoolBar coolBar = new CoolBar(shell, SWT.NONE);
		ToolBar toolBar = new ToolBar(coolBar, SWT.NONE);
		ToolBar toolBar2 = new ToolBar(coolBar, SWT.NONE);
		List<ToolItem> items = ReflectionUtils.invokeHidden(view, "getAllToolItems");
		assertNotNull(items);
		assertTrue(items.isEmpty());

		ToolItem item = new ToolItem(toolBar, SWT.NONE);
		ToolItem item2 = new ToolItem(toolBar2, SWT.NONE);
		items = ReflectionUtils.invokeHidden(view, "getAllToolItems");
		assertNotNull(items);
		assertFalse(items.isEmpty());
		assertEquals(2, items.size());
		assertSame(item, items.get(0));
		assertSame(item2, items.get(1));
		//
		//		MenuCoolBarComposite menuComposite = new MenuCoolBarComposite(shell, SWT.NONE);
		//		MenuManager manager = new MenuManager("TestMenu", "0815");
		//		ToolItem topItem = menuComposite.createAndAddMenu(manager);
		//		items = ReflectionUtils.invokeHidden(view, "getAllToolItems");
		//		assertNotNull(items);
		//		assertFalse(items.isEmpty());
		//		assertEquals(2, items.size());
		//		assertTrue(items.contains(topItem));

	}

	/**
	 * Tests the <i>private</i> method {@code createRidget(IController, Item)}.
	 */
	public void testCreateRidgetItem() {

		Controller controller = new Controller();
		Menu menu = new Menu(shell);
		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		MyContributionItem contributionItem = new MyContributionItem();
		contributionItem.setId("4711");
		menuItem.setData(contributionItem);
		ReflectionUtils.invokeHidden(view, "createRidget", controller, menuItem);
		assertFalse(controller.getRidgets().isEmpty());
		assertEquals(1, controller.getRidgets().size());
		IRidget ridget = controller.getRidget(IActionRidget.BASE_ID_MENUACTION + "4711");
		assertNotNull(ridget);
		assertTrue(ridget instanceof MenuItemRidget);
		assertSame(menuItem, ridget.getUIControl());
		assertEquals(IActionRidget.BASE_ID_MENUACTION + "4711", SWTBindingPropertyLocator.getInstance()
				.locateBindingProperty(menuItem));

		CoolBar coolBar = new CoolBar(shell, SWT.NONE);
		ToolBar toolBar = new ToolBar(coolBar, SWT.NONE);
		ToolItem toolItem = new ToolItem(toolBar, SWT.NONE);
		contributionItem = new MyContributionItem();
		contributionItem.setId("0815");
		toolItem.setData(contributionItem);
		ReflectionUtils.invokeHidden(view, "createRidget", controller, toolItem);
		assertFalse(controller.getRidgets().isEmpty());
		assertEquals(2, controller.getRidgets().size());
		ridget = controller.getRidget(IActionRidget.BASE_ID_TOOLBARACTION + "0815");
		assertNotNull(ridget);
		assertTrue(ridget instanceof ToolItemRidget);
		assertSame(toolItem, ridget.getUIControl());
		assertEquals(IActionRidget.BASE_ID_TOOLBARACTION + "0815", SWTBindingPropertyLocator.getInstance()
				.locateBindingProperty(toolItem));

	}

	/**
	 * Tests the <i>private</i> method {@code createRidget(IController, Item)}.
	 */
	public void testCreateRidgetMenu() {

		Controller controller = new Controller();
		Menu menu = new Menu(shell);
		MenuItem item = new MenuItem(menu, SWT.NONE);
		MyContributionItem contributionItem = new MyContributionItem();
		contributionItem.setId("4711");
		item.setData(contributionItem);
		ReflectionUtils.invokeHidden(view, "createRidget", controller, menu);
		assertFalse(controller.getRidgets().isEmpty());
		assertEquals(1, controller.getRidgets().size());
		IRidget ridget = controller.getRidget(IActionRidget.BASE_ID_MENUACTION + "4711");
		assertNotNull(ridget);
		assertTrue(ridget instanceof MenuItemRidget);
		assertSame(item, ridget.getUIControl());
		assertEquals(IActionRidget.BASE_ID_MENUACTION + "4711", SWTBindingPropertyLocator.getInstance()
				.locateBindingProperty(item));

	}

	public void testUnbind() throws Exception {

		List<ModuleGroupNodeListener> listeners = ReflectionUtils.getHidden(node, "listeners");

		assertEquals(1, listeners.size());

		node.dispose();

		assertTrue(listeners.isEmpty());
	}

	private static class MyContributionItem extends ContributionItem {
	}

	private static class Controller extends AbstractRidgetController {

		@Override
		public void configureRidgets() {
		}

	}

	private class TestSubApplicationView extends SubApplicationView {

		@Override
		public SubApplicationNode getNavigationNode() {
			return node;
		}

	}

}
