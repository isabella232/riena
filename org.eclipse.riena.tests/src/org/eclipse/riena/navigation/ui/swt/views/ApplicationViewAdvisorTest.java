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

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.navigation.model.ApplicationModel;
import org.eclipse.riena.navigation.ui.controllers.ApplicationViewController;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;

/**
 * Tests of the class <code>ApplicationViewAdvisor</code>.
 */
public class ApplicationViewAdvisorTest extends TestCase {

	private ApplicationViewAdvisor advisor;
	private IWorkbenchWindowConfigurer winConfig;
	private ApplicationModel applicationModel;
	private ApplicationViewController controller;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		winConfig = EasyMock.createNiceMock(IWorkbenchWindowConfigurer.class);
		applicationModel = new ApplicationModel();
		controller = new ApplicationViewController(applicationModel);
		advisor = new ApplicationViewAdvisor(winConfig, controller);
	}

	/*
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		applicationModel = null;
		controller = null;
		winConfig = null;
		advisor.dispose();
		advisor = null;
	}

	/**
	 * Tests the method <code>initShell</code>.
	 */
	public void testInitShell() {

		Shell shell = new Shell();
		assertNotSame(SWT.INHERIT_FORCE, shell.getBackgroundMode());
		Point defMinSize = shell.getMinimumSize();

		ReflectionUtils.invokeHidden(advisor, "initShell", shell);

		assertFalse(defMinSize.equals(shell.getMinimumSize()));
		assertNotNull(shell.getData(SWTBindingPropertyLocator.BINDING_PROPERTY));

		SwtUtilities.disposeWidget(shell);

	}

	/**
	 * Tests the method <code>createMenu</code>.
	 */
	public void testCreateMenu() {

		Shell shell = new Shell();
		ToolBar toolBar = new ToolBar(shell, SWT.NONE);
		ToolItem toolItem = new ToolItem(toolBar, SWT.PUSH);
		DummyMenuManager menuManager = new DummyMenuManager();

		Menu menu = ReflectionUtils.invokeHidden(advisor, "createMenu", shell, toolItem, menuManager);
		assertSame(menuManager.createContextMenu(shell), menu);
		// MenuListener added?
		assertTrue(menu.getListeners(SWT.Show).length == 1);
		// SelectionListener added?
		assertTrue(toolItem.getListeners(SWT.Selection).length == 1);

		SwtUtilities.disposeWidget(toolItem);
		SwtUtilities.disposeWidget(toolBar);
		SwtUtilities.disposeWidget(shell);
		menuManager.dispose();

	}

	private class DummyMenuManager extends MenuManager {

		private Menu menu;

		/**
		 * @see org.eclipse.jface.action.MenuManager#createContextMenu(org.eclipse.swt.widgets.Control)
		 */
		@Override
		public Menu createContextMenu(Control parent) {
			if (menu == null) {
				menu = new Menu(parent);
			}
			return menu;
		}

		/**
		 * @see org.eclipse.jface.action.MenuManager#dispose()
		 */
		@Override
		public void dispose() {
			super.dispose();
			SwtUtilities.disposeWidget(menu);
		}
	}

}
