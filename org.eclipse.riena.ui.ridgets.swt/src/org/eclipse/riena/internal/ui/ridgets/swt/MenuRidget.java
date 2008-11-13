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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.BindingException;
import org.eclipse.riena.ui.ridgets.IMenuRidget;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.MenuItem;

/**
 * Ridget of a menu item that is a cascade menu item.
 */
public class MenuRidget extends MenuItemRidget implements IMenuRidget {

	private List<MenuItemRidget> children;
	private DisposeListener disposeListener;

	public MenuRidget() {
		super();
		children = new ArrayList<MenuItemRidget>();
		disposeListener = new ChildDisposeListener();
	}

	@Override
	protected void checkUIControl(Object uiControl) {
		assertType(uiControl, MenuItem.class);
		if (!isMenu((MenuItem) uiControl)) {
			throw new BindingException("Menu item is not a cascade menu item!"); //$NON-NLS-1$
		}
	}

	@Override
	AbstractItemProperties createProperties() {
		return new MenuProperties(this);
	}

	public void addChild(MenuItemRidget child) {
		children.add(child);
		child.getUIControl().addDisposeListener(disposeListener);
	}

	public List<MenuItemRidget> getChildren() {
		return children;
	}

	private MenuItemRidget getChild(String id) {
		List<MenuItemRidget> children = getChildren();
		for (MenuItemRidget child : children) {
			if (child.getID().equals(id)) {
				return child;
			}
		}
		return null;
	}

	private class ChildDisposeListener implements DisposeListener {

		public void widgetDisposed(DisposeEvent e) {
			if (e.getSource() instanceof MenuItem) {
				MenuItem item = (MenuItem) e.getSource();
				String id = SWTBindingPropertyLocator.getInstance().locateBindingProperty(item);
				MenuItemRidget child = getChild(id);
				if (child != null) {
					getChildren().remove(child);
				}
			}
		}

	}

}
