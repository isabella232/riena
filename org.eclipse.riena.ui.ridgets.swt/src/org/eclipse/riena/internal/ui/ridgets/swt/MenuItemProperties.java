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

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * This class stores the properties of a menu item.
 */
public class MenuItemProperties extends AbstractItemProperties {

	private Menu parent;
	private int index;

	/**
	 * @param item
	 */
	public MenuItemProperties(MenuItemRidget ridget) {
		super(ridget);
		MenuItem item = ridget.getUIControl();
		parent = item.getParent();
		index = parent.indexOf(item);
	}

	@Override
	protected MenuItem createItem() {
		return createItem(getParent());
	}

	@Override
	protected MenuItemRidget getRidget() {
		return (MenuItemRidget) super.getRidget();
	}

	/**
	 * Creates a new menu item base on the stored properties.
	 * 
	 * @param parent
	 *            - parent menu
	 * @return menu item
	 */
	protected MenuItem createItem(Menu parent) {
		MenuItem menuItem = new MenuItem(parent, getStyle(), index);
		setAllProperties(menuItem);
		getRidget().setUIControl(menuItem);
		return menuItem;
	}

	protected Menu getParent() {
		return parent;
	}

	protected int getIndex() {
		return index;
	}

}
