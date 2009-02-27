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
package org.eclipse.riena.ui.filter.impl;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.ui.filter.IUIFilterRuleMarker;

/**
 *
 */
public abstract class AbstractUIFilterRuleMarker implements IUIFilterRuleMarker {

	private IMarker marker;

	/**
	 * Create a new filter rule with the given marker.
	 * 
	 * @param marker
	 *            - marker to set
	 */
	public AbstractUIFilterRuleMarker(IMarker marker) {
		this.marker = marker;
	}

	/**
	 * Returns the marker of this rule.
	 * 
	 * @return marker
	 */
	public IMarker getMarker() {
		return marker;
	}

}
