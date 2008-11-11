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

import org.eclipse.riena.ui.ridgets.AbstractMarkerSupport;
import org.eclipse.riena.ui.ridgets.IMenuItemRidget;
import org.eclipse.swt.widgets.MenuItem;

/**
 * Ridget of a menu item.
 */
public class MenuItemRidget extends AbstractItemRidget implements IMenuItemRidget {

	protected boolean isMenu(MenuItem menuItem) {
		if (menuItem != null) {
			return (menuItem.getMenu() != null);
		} else {
			return false;
		}
	}

	@Override
	protected void bindUIControl() {
		super.bindUIControl();
		MenuItem menuItem = getUIControl();
		if (menuItem != null && !isMenu(menuItem)) {
			menuItem.addSelectionListener(getActionObserver());
		}
	}

	@Override
	protected void unbindUIControl() {
		MenuItem menuItem = getUIControl();
		if (menuItem != null && !isMenu(menuItem)) {
			menuItem.removeSelectionListener(getActionObserver());
		}
		super.unbindUIControl();
	}

	@Override
	protected void checkUIControl(Object uiControl) {
		assertType(uiControl, MenuItem.class);
	}

	@Override
	public MenuItem getUIControl() {
		return (MenuItem) super.getUIControl();
	}

	@Override
	protected AbstractMarkerSupport createMarkerSupport() {
		return new MenuItemMarkerSupport(this, propertyChangeSupport);
	}

}
