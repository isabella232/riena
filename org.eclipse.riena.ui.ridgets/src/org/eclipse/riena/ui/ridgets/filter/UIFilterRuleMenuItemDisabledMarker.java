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

import org.eclipse.riena.ui.core.marker.DisabledMarker;

/**
 * Filter rule to provide a disabled marker for a menu and tool bar item.
 */
public class UIFilterRuleMenuItemDisabledMarker extends AbstractUIFilterRuleRidgetMarker {

	/**
	 * Creates a new instance of {@code UIFilterRuleMenuActionDisabledMarker}.
	 * 
	 * @param id
	 *            - ID
	 */
	public UIFilterRuleMenuItemDisabledMarker() {
		super(null, new DisabledMarker(false));
	}

	/**
	 * Creates a new instance of {@code UIFilterRuleMenuItemDisabledMarker}.
	 * 
	 * @param id
	 *            - ID
	 * @param marker
	 *            - marker
	 */
	public UIFilterRuleMenuItemDisabledMarker(String id) {
		super(id, new DisabledMarker(false));
	}

	@Override
	protected RidgetMatcher createMatcher(String id) {
		return new MenuItemRidgetMatcher(id);
	}

}