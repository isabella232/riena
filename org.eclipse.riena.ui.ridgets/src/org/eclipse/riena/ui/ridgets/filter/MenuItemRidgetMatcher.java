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
package org.eclipse.riena.ui.ridgets.filter;

import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.ui.ridgets.IActionRidget;

/**
 * The matcher compares the ID of this class with the ID of a menu and tool bar
 * items.
 */
public class MenuItemRidgetMatcher extends RidgetMatcher {

	/**
	 * Creates a new instance of {@code MenuItemRidgetMatcher}.
	 * 
	 * @param id
	 *            - ID
	 */
	public MenuItemRidgetMatcher(String id) {
		super(id);
	}

	@Override
	public boolean matches(Object object) {

		if (object instanceof IActionRidget) {
			IActionRidget ridget = (IActionRidget) object;
			String ridgetId = ridget.getID();
			if (StringUtils.equals(ridgetId, getMenuItemId())) {
				return true;
			}
			if (StringUtils.equals(ridgetId, getToolbarItemId())) {
				return true;
			}
		}

		return false;

	}

	private String getMenuItemId() {
		if (getId().startsWith(IActionRidget.BASE_ID_MENUACTION)) {
			return getId();
		} else {
			return IActionRidget.BASE_ID_MENUACTION + getId();
		}
	}

	private String getToolbarItemId() {
		if (getId().startsWith(IActionRidget.BASE_ID_TOOLBARACTION)) {
			return getId();
		} else {
			return IActionRidget.BASE_ID_TOOLBARACTION + getId();
		}
	}

}
