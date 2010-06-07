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
import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import org.eclipse.riena.internal.navigation.ui.swt.CoolbarUtils;
import org.eclipse.riena.ui.ridgets.swt.MenuManagerHelper;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;

/**
 * This composites has a list of the top-level menus of the Riena menu bar (a
 * cool bar with an item for every top-level menu).
 */
public class MenuCoolBarComposite extends Composite {

	private CoolItem coolItem;
	private ToolBar toolBar;

	/**
	 * Creates an new instance of {@code MenuCoolBarComposite} given its parent
	 * and a style value describing its behavior and appearance.
	 * 
	 * @param parent
	 *            a composite which will be the parent of the new instance
	 *            (cannot be null)
	 * @param style
	 *            the style of widget to construct
	 */
	public MenuCoolBarComposite(Composite parent, int style) {
		super(parent, style);
		create();
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
		helper.createMenu(toolBar, toolItem, menuManager);
		calcSize(coolItem);
		return toolItem;
	}

	public List<ToolItem> getTopLevelItems() {
		ToolItem[] toolItems = toolBar.getItems();
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
	private void calcSize(CoolItem item) {
		Control control = item.getControl();
		Point pt = control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		pt = item.computeSize(pt.x, pt.y);
		item.setSize(pt);
	}

	/**
	 * Creates the cool and tool bar. These build the menu bar of the Riena
	 * sub-application.
	 */
	private void create() {
		CoolBar coolBar = new CoolBar(this, SWT.FLAT);
		coolItem = CoolbarUtils.initCoolBar(coolBar, getMenuBarFont());

		toolBar = (ToolBar) coolItem.getControl();
		toolBar.addMouseMoveListener(new ToolBarMouseListener());
	}

	private Font getMenuBarFont() {
		return LnfManager.getLnf().getFont(LnfKeyConstants.MENUBAR_FONT);
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
