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
package org.eclipse.riena.ui.core.marker;

import org.eclipse.riena.core.marker.AbstractMarker;

/**
 * Marks an adapter, resp. its associated UI control, as error.
 */
public class ErrorMarker extends AbstractMarker implements IIconizableMarker {

	public static final String MARKER_KEY = "ErrorMarker"; //$NON-NLS-1$

	/**
	 * Create a 'unique' error marker instance.
	 */
	public ErrorMarker() {
		super(true);
	}

	/**
	 * Create an error marker instance.
	 * 
	 * @param unique
	 *            true to create a 'unique' marker; false otherwise. See
	 *            {@link AbstractMarker} more information.
	 */
	protected ErrorMarker(boolean unique) {
		super(unique);
	}

	public String getIconConfigurationKey() {
		return MARKER_KEY;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@value MarkerPosition.BOTTOM_RIGHT}
	 */
	public MarkerPosition getPositionOfMarker() {
		return MarkerPosition.BOTTOM_RIGHT;
	}

}
