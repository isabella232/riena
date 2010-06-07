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
package org.eclipse.riena.internal.navigation.ui.filter;

import org.eclipse.riena.core.marker.IMarker;

/**
 *
 */
public abstract class AbstractUIFilterRuleMenuItemMarker extends AbstractUIFilterRuleRidgetMarker {

	/**
	 * Creates a new instance of {@code UIFilterRuleMenuActionDisabledMarker}.
	 * 
	 * @param marker
	 */
	public AbstractUIFilterRuleMenuItemMarker(IMarker marker) {
		super(null, marker);
	}

	/**
	 * Creates a new instance of {@code UIFilterRuleMenuItemDisabledMarker}.
	 * 
	 * @param id
	 *            ID
	 * @param marker
	 */
	public AbstractUIFilterRuleMenuItemMarker(String id, IMarker marker) {
		super(id, marker);
	}

	@Override
	protected RidgetMatcher createMatcher(String id) {
		return new MenuItemRidgetMatcher(id);
	}

}
