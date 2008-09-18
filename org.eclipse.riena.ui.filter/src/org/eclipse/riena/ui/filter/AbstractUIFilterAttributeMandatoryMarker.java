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
package org.eclipse.riena.ui.filter;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;

/**
 * Filter attribute for a output marker.
 */
public abstract class AbstractUIFilterAttributeMandatoryMarker implements IUIFilterMarkerAttribute {

	private MandatoryMarker marker;

	/**
	 * Create a new filter attribute with the given marker.
	 * 
	 * @param marker
	 *            - marker to set
	 */
	public AbstractUIFilterAttributeMandatoryMarker(MandatoryMarker marker) {
		this.marker = marker;
	}

	/**
	 * Returns the marker of this attribute.
	 * 
	 * @return marker
	 */
	public IMarker getMarker() {
		return marker;
	}

}
