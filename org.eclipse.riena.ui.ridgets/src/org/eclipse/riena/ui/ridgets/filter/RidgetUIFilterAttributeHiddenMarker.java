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
import org.eclipse.riena.ui.core.marker.HiddenMarker;
import org.eclipse.riena.ui.filter.AbstractUIFilterAttributeHiddenMarker;

/**
 * Filter attribute to provide the visibility for a ridget.
 */
public class RidgetUIFilterAttributeHiddenMarker extends AbstractUIFilterAttributeHiddenMarker {

	private String filterId;

	/**
	 * Creates a new instance of {@code
	 * RidgetUIFilterAttributeHiddenMandatoryMarker}.
	 * 
	 * @param id
	 *            - ID
	 * @param marker
	 *            - marker
	 */
	public RidgetUIFilterAttributeHiddenMarker(String id, HiddenMarker marker) {
		super(marker);
		this.filterId = id;
	}

	/**
	 * This method compares the ID of this attribute and the given ID of a
	 * ridget.
	 * 
	 * @see org.eclipse.riena.ui.filter.IUIFilterAttribute#matches(java.lang.Object)
	 */
	public boolean matches(Object object) {

		if (object instanceof String) {
			String ridgetId = (String) object;
			return StringUtils.equals(ridgetId, filterId);
		}

		return false;

	}
}
