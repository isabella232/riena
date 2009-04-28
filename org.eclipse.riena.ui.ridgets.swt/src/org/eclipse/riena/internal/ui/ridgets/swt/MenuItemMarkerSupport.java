/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
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

import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.menus.CommandContributionItem;

import org.eclipse.riena.ui.ridgets.AbstractMarkerSupport;

/**
 * Helper class for SWT Menu Item Ridgets to delegate their marker issues to.
 */
public class MenuItemMarkerSupport extends AbstractMarkerSupport {

	/**
	 * Creates a new instance of {@code MenuItemMarkerSupport}.
	 * 
	 * @param ridget
	 *            - ridget of menu item
	 * @param propertyChangeSupport
	 */
	public MenuItemMarkerSupport(MenuItemRidget ridget, PropertyChangeSupport propertyChangeSupport) {
		super(ridget, propertyChangeSupport);
	}

	@Override
	public void updateMarkers() {
		updateMenuItem();
	}

	@Override
	protected void handleMarkerAttributesChanged() {
		updateMenuItem();
		super.handleMarkerAttributesChanged();
	}

	@Override
	protected MenuItemRidget getRidget() {
		return (MenuItemRidget) super.getRidget();
	}

	/**
	 * Enables or disables the given item.
	 * 
	 * @param item
	 *            - menu item to update
	 */
	private void updateDisabled(MenuItem item) {
		if (item.isDisposed()) {
			return;
		}
		boolean enabled = getRidget().isEnabled();
		CommandContributionItem commandItem = getContributionItem(item);
		if (commandItem != null) {
			enabled = enabled && commandItem.isEnabled();
		}
		item.setEnabled(enabled);
	}

	/**
	 * Shows or hides the given item.
	 * 
	 * @param item
	 *            - menu item to update
	 */
	private void updateVisible(MenuItem item) {

		if (!hasHiddenMarkers()) {
			getRidget().createItem();
		} else {
			if ((!item.isDisposed()) && (item.getMenu() != null)) {
				item.getMenu().dispose();
			}
			item.dispose();
		}

	}

	/**
	 * Updates the menu item to display the current markers.
	 */
	private void updateMenuItem() {
		MenuItem item = getRidget().getUIControl();
		if (item != null) {
			updateVisible(item);
			if (!item.isDisposed()) {
				updateDisabled(item);
			}
		}
	}

	/**
	 * Returns form the data of the given item the
	 * {@link CommandContributionItem}.
	 * 
	 * @param item
	 *            - menu item
	 * @return the {@code CommandContributionItem} or <{@code null} if the item
	 *         has no {@code CommandContributionItem}.
	 */
	private CommandContributionItem getContributionItem(MenuItem item) {

		if (item.isDisposed()) {
			return null;
		}

		if ((item.getData() instanceof CommandContributionItem)) {
			return (CommandContributionItem) item.getData();
		} else {
			return null;
		}

	}

}
