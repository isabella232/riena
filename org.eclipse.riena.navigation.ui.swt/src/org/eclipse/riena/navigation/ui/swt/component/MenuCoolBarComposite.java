/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.ui.swt.component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.WorkbenchWindow;

import org.eclipse.riena.internal.navigation.ui.swt.CoolbarUtils;
import org.eclipse.riena.ui.ridgets.swt.MenuManagerHelper;
import org.eclipse.riena.ui.swt.facades.SWTFacade;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;

/**
 * This composites has a list of the top-level menus of the Riena menu bar (a
 * cool bar with an item for every top-level menu).
 */
public class MenuCoolBarComposite extends Composite {

	private final IWorkbenchWindow window;

	private CoolItem menuCoolItem;
	private ToolBar menuToolBar;
	private CoolBar menuCoolBar;
	private final Map<MenuManager, Boolean> menuManagerVisibilityMap;

	/**
	 * Creates an new instance of {@code MenuCoolBarComposite} given its parent
	 * and a style value describing its behavior and appearance.
	 * 
	 * @param parent
	 *            a composite which will be the parent of the new instance
	 *            (cannot be null)
	 * @param style
	 *            the style of widget to construct
	 * @param window
	 *            an IWorkbenchWindow instance, used to determine the menu
	 *            entries. May be null.
	 * @since 3.0
	 */
	public MenuCoolBarComposite(final Composite parent, final int style, final IWorkbenchWindow window) {
		super(parent, style);
		setLayout(new FillLayout());
		this.window = window;
		menuManagerVisibilityMap = new HashMap<MenuManager, Boolean>();
		create();
	}

	public List<ToolItem> getTopLevelItems() {
		final ToolItem[] toolItems = menuToolBar.getItems();
		return Arrays.asList(toolItems);
	}

	// helping methods
	//////////////////

	/**
	 * Calculates and sets the size of the given cool item.
	 * 
	 * @param item
	 *            item of cool bar
	 */
	private void calcSize(final CoolItem item) {
		final Control control = item.getControl();
		Point pt = control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		pt = item.computeSize(pt.x, pt.y);
		item.setSize(pt);
	}

	/**
	 * Creates a top-level menu and adds it to the Riena menu bar.
	 * 
	 * @param menuManager
	 */
	private ToolItem createAndAddMenu(final MenuManager menuManager) {
		if (menuManager.isVisible()) {
			final ToolItem toolItem = new ToolItem(menuToolBar, SWT.CHECK);
			SWTBindingPropertyLocator.getInstance().setBindingProperty(toolItem, menuManager.getId());
			toolItem.setText(menuManager.getMenuText());
			final MenuManagerHelper helper = new MenuManagerHelper();
			helper.createMenu(menuToolBar, toolItem, menuManager);
			calcSize(menuCoolItem);
			return toolItem;
		} else {
			return null;
		}
	}

	/**
	 * Creates the cool and tool bar. These build the menu bar of the Riena
	 * sub-application.
	 */
	private void create() {
		menuCoolBar = new CoolBar(this, SWT.FLAT);
		menuCoolItem = CoolbarUtils.initCoolBar(menuCoolBar, getMenuBarFont());
		menuToolBar = (ToolBar) menuCoolItem.getControl();
		SWTFacade.getDefault().addMouseMoveListener(menuToolBar, new ToolBarMouseListener());
		fillMenuBar();
	}

	private void fillMenuBar() {
		for (final IContributionItem contribItem : getTopLevelMenuEntries()) {
			if (contribItem instanceof MenuManager) {
				final MenuManager topMenuManager = (MenuManager) contribItem;
				menuManagerVisibilityMap.put(topMenuManager, topMenuManager.isVisible());
				createAndAddMenu(topMenuManager);
			}
		}
	}

	private Font getMenuBarFont() {
		return LnfManager.getLnf().getFont(LnfKeyConstants.MENUBAR_FONT);
	}

	/**
	 * Returns the top-level menu entries.
	 */
	private IContributionItem[] getTopLevelMenuEntries() {
		if (window instanceof WorkbenchWindow) {
			final MenuManager menuManager = ((WorkbenchWindow) window).getMenuManager();
			return menuManager.getItems();
		} else {
			return new IContributionItem[0];
		}
	}

	// helping classes
	//////////////////

	/**
	 * If the mouse moves over an unselected item of the tool bar and another
	 * item was selected, deselect the other item and select the item below the
	 * mouse pointer.<br>
	 * <i>Does not work, if menu is visible.</i>
	 */
	private static final class ToolBarMouseListener implements MouseMoveListener {
		public void mouseMove(final MouseEvent e) {
			if (e.getSource() instanceof ToolBar) {
				final ToolBar toolBar = (ToolBar) e.getSource();

				ToolItem selectedItem = null;
				final ToolItem[] items = toolBar.getItems();
				for (final ToolItem item : items) {
					if (item.getSelection()) {
						selectedItem = item;
					}
				}

				final ToolItem hoverItem = toolBar.getItem(new Point(e.x, e.y));
				if (hoverItem != null) {
					if (!hoverItem.getSelection() && (selectedItem != null)) {
						selectedItem.setSelection(false);
						hoverItem.setSelection(true);
					}
				}
			}
		}
	}

	public boolean updateMenuItems() {
		if (isMenuVisibilityChanged()) {
			if (menuCoolBar != null) {
				menuCoolBar.dispose();
			}
			create();
			layout();
			return true;
		}
		return false;
	}

	private boolean isMenuVisibilityChanged() {
		for (final IContributionItem contribItem : getTopLevelMenuEntries()) {
			if (contribItem instanceof MenuManager) {
				final MenuManager topMenuManager = (MenuManager) contribItem;
				final Boolean isVisible = menuManagerVisibilityMap.get(topMenuManager);
				if (isVisible == null || !isVisible.equals(topMenuManager.isVisible())) {
					return true;
				}
			}
		}
		return false;
	}

}
