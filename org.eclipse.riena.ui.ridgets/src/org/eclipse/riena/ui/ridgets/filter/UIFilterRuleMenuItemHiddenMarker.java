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

import org.eclipse.riena.ui.core.marker.HiddenMarker;

/**
 * Filter rule to provide a hidden marker for a menu and tool bar item.
 */
public class UIFilterRuleMenuItemHiddenMarker extends AbstractUIFilterRuleRidgetMarker {

	/**
	 * Creates a new instance of {@code UIFilterRuleMenuItemHiddenMarker}.
	 * 
	 * @param id
	 *            - ID
	 */
	public UIFilterRuleMenuItemHiddenMarker() {
		super(null, new HiddenMarker(false));
	}

	/**
	 * Creates a new instance of {@code UIFilterRuleMenuActionHiddenMarker}.
	 * 
	 * @param id
	 *            - ID
	 * @param marker
	 *            - marker
	 */
	public UIFilterRuleMenuItemHiddenMarker(String id) {
		super(id, new HiddenMarker(false));
	}

	@Override
	protected RidgetMatcher createMatcher(String id) {
		return new MenuItemRidgetMatcher(id);
	}

}