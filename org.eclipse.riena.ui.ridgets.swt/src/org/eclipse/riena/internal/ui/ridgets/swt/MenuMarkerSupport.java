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

import java.beans.PropertyChangeSupport;

import org.eclipse.riena.ui.ridgets.AbstractMarkerSupport;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * Helper class for SWT Menu Ridgets to delegate their marker issues to.
 */
public class MenuMarkerSupport extends AbstractMarkerSupport {

	/**
	 * Creates a new instance of {@code MenuItemMarkerSupport}.
	 * 
	 * @param ridget
	 *            - ridget of menu item
	 * @param propertyChangeSupport
	 */
	public MenuMarkerSupport(MenuRidget ridget, PropertyChangeSupport propertyChangeSupport) {
		super(ridget, propertyChangeSupport);
	}

	@Override
	public void updateMarkers() {
		updateMenu();
	}

	@Override
	protected void handleMarkerAttributesChanged() {
		updateMenu();
		super.handleMarkerAttributesChanged();
	}

	/**
	 * Enables or disables the given menu.
	 * 
	 * @param item
	 *            - menu to update
	 */
	private void updateDisabled(MenuItem item) {
		item.setEnabled(ridget.isEnabled());
		Menu menu = item.getMenu();
		if (menu != null) {
			menu.setEnabled(ridget.isEnabled());
		}
	}

	/**
	 * Shows or hides the given menu.
	 * 
	 * @param item
	 *            - menu to update
	 */
	private void updateVisible(Menu menu) {
		menu.setVisible(ridget.isVisible());
	}

	/**
	 * Updates the menu to display the current markers.
	 */
	private void updateMenu() {
		MenuItem item = (MenuItem) ridget.getUIControl();
		if (item != null) {
			Menu menu = item.getMenu();
			if (menu != null) {
				updateVisible(menu);
			}
			updateDisabled(item);
		}
	}

}
