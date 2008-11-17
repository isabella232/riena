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
package org.eclipse.riena.ui.ridgets.swt;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolItem;

/**
 * This helper class supports the {@link MenuManger}.
 */
public class MenuManagerHelper {

	/**
	 * Creates with the help of the given menu manager a menu. If the given tool
	 * item is selected, the menu is shown.
	 * 
	 * @param parent
	 * @param toolItem
	 *            - tool item with menu
	 * @param topMenuManager
	 *            - menu manager
	 * @return menu
	 */
	public Menu createMenu(Composite parent, final ToolItem toolItem, MenuManager topMenuManager) {

		final Menu menu = topMenuManager.createContextMenu(parent);
		toolItem.setData(topMenuManager);
		topMenuManager.updateAll(true);
		menu.addMenuListener(new MenuListener() {

			/**
			 * @see org.eclipse.swt.events.MenuListener#menuHidden(org.eclipse.swt.events.MenuEvent)
			 */
			public void menuHidden(MenuEvent e) {
				if (e.getSource() == menu) {
					toolItem.setSelection(false);
				}
			}

			/**
			 * @see org.eclipse.swt.events.MenuListener#menuShown(org.eclipse.swt.events.MenuEvent)
			 */
			public void menuShown(MenuEvent e) {
			}

		});

		toolItem.addSelectionListener(new SelectionListener() {

			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
				if (e.getSource() == toolItem) {
					Rectangle itemBounds = toolItem.getBounds();
					Point loc = toolItem.getParent().toDisplay(itemBounds.x, itemBounds.height + itemBounds.y);
					menu.setLocation(loc);
					menu.setVisible(true);
				}
			}

		});

		return menu;

	}

}
