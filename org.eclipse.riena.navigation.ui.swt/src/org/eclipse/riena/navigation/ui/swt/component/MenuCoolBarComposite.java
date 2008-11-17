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
package org.eclipse.riena.navigation.ui.swt.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.riena.ui.ridgets.swt.MenuManagerHelper;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * This composites has a list of the top-level menus of the Riena menu bar (a
 * cool bar with an item for every top-level menu).
 */
public class MenuCoolBarComposite extends Composite {

	private List<Menu> menus;
	private ToolBar toolBar;
	private CoolItem coolItem;

	/**
	 * Creates an new instance of {@code MenuCoolBarComposite} given its parent
	 * and a style value describing its behavior and appearance.
	 * 
	 * @param parent
	 *            - a composite which will be the parent of the new instance
	 *            (cannot be null)
	 * @param style
	 *            - the style of widget to construct
	 */
	public MenuCoolBarComposite(Composite parent, int style) {
		super(parent, style);
		menus = new ArrayList<Menu>();
		create();
	}

	/**
	 * Creates the cool and tool bar. These build the menu bar of the Riena
	 * sub-application.
	 */
	private void create() {

		CoolBar coolBar = new CoolBar(this, SWT.FLAT);
		coolBar.setBackground(getCoolbarBackground());

		coolItem = new CoolItem(coolBar, SWT.DROP_DOWN);
		toolBar = new ToolBar(coolBar, SWT.FLAT);
		coolItem.setControl(toolBar);
		toolBar.addMouseMoveListener(new ToolBarMouseListener());

		coolBar.setLocked(true);

	}

	/**
	 * Calculates and sets the size of the given cool item.
	 * 
	 * @param item
	 *            - item of cool bar
	 */
	private void calcSize(CoolItem item) {
		Control control = item.getControl();
		Point pt = control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		pt = item.computeSize(pt.x, pt.y);
		item.setSize(pt);
	}

	/**
	 * Creates a top-level menu and adds it to the Riena menu bar.
	 * 
	 * @param menuManager
	 */
	public ToolItem createAndAddMenu(MenuManager menuManager) {
		ToolItem toolItem = new ToolItem(toolBar, SWT.CHECK);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(toolItem, menuManager.getId());
		toolItem.setText(menuManager.getMenuText());
		MenuManagerHelper helper = new MenuManagerHelper();
		Menu menu = helper.createMenu(toolBar, toolItem, menuManager);
		addMenu(menu);
		calcSize(coolItem);
		return toolItem;
	}

	/**
	 * Adds the given menu to the list of top-level menus of the Riena
	 * application.
	 * 
	 * @param menu
	 *            - top-level menu to add
	 */
	private void addMenu(Menu menu) {
		menus.add(menu);
	}

	/**
	 * Returns the list of top-level menus of the Riena application.
	 * 
	 * @return list of menus
	 */
	public List<Menu> getMenus() {
		return menus;
	}

	public List<ToolItem> getTopLevelItems() {

		ToolItem[] toolItems = toolBar.getItems();
		return Arrays.asList(toolItems);

	}

	/**
	 * Return the coolbar / menubar background color according to the
	 * look-and-feel.
	 */
	private Color getCoolbarBackground() {
		return LnfManager.getLnf().getColor(ILnfKeyConstants.COOLBAR_BACKGROUND);
	}

	/**
	 * If the mouse moves over an unselected item of the tool bar and another
	 * item was selected, deselect the other item and select the item below the
	 * mouse pointer.<br>
	 * <i>Does not work, if menu is visible.</i>
	 */
	private static class ToolBarMouseListener implements MouseMoveListener {

		/**
		 * @see org.eclipse.swt.events.MouseMoveListener#mouseMove(org.eclipse.swt.events.MouseEvent)
		 */
		public void mouseMove(MouseEvent e) {

			if (e.getSource() instanceof ToolBar) {

				ToolBar toolBar = (ToolBar) e.getSource();

				ToolItem selectedItem = null;
				ToolItem[] items = toolBar.getItems();
				for (int i = 0; i < items.length; i++) {
					if (items[i].getSelection()) {
						selectedItem = items[i];
					}
				}

				ToolItem hoverItem = toolBar.getItem(new Point(e.x, e.y));
				if (hoverItem != null) {
					if (!hoverItem.getSelection() && (selectedItem != null)) {
						selectedItem.setSelection(false);
						hoverItem.setSelection(true);
					}
				}
			}

		}

	}

}
