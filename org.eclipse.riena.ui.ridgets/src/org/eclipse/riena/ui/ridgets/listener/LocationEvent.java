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
package org.eclipse.riena.ui.ridgets.listener;

/**
 * Event sent to {@link ILocationListener}s when the user activates a hyperlink.
 * 
 * @since 3.0
 */
public final class LocationEvent {

	private final String location;
	private final boolean topFrame;

	/**
	 * // * Creates a new LocationEvent instance.
	 * 
	 * @param location
	 *            the new location (URL)
	 * @param topFrame
	 *            true if the location is to be opened in the top frame. Most
	 *            commonly the value is {@code false} (i.e. when no frames are
	 *            used).
	 */
	public LocationEvent(final String location, final boolean topFrame) {
		this.location = location;
		this.topFrame = topFrame;
	}

	/**
	 * Returns location value (an URL).
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Returns true if the location is to be opened in the top frame. Most
	 * commonly the value is {@code false} (i.e. when no frames are used).
	 */
	public boolean isTopFrame() {
		return topFrame;
	}

	@Override
	public String toString() {
		return String.format("LocationEvent [topFrame=%b, location=%s]", topFrame, location); //$NON-NLS-1$
	}

}
