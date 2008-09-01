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
package org.eclipse.riena.ui.core.marker;

import org.eclipse.riena.core.marker.AbstractMarker;

/**
 * Marks an adapter, resp. its associated UI control, as error.
 */
public class ErrorMarker extends AbstractMarker implements IIconizableMarker {

	public static final String MARKER_KEY = "ErrorMarker"; //$NON-NLS-1$

	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder("ErrorMarker["); //$NON-NLS-1$
		buffer.append("attributes="); //$NON-NLS-1$
		buffer.append(getAttributes());
		buffer.append("]"); //$NON-NLS-1$
		return buffer.toString();
	}

	/**
	 * @see org.eclipse.riena.ui.core.marker.IIconizableMarker#getIconConfiguationKey()
	 */
	public String getIconConfiguationKey() {
		return MARKER_KEY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.ui.core.marker.IIconizableMarker#getPositionOfMarker()
	 */
	public MarkerPosition getPositionOfMarker() {
		return MarkerPosition.BOTTOM_RIGHT;
	}

}
