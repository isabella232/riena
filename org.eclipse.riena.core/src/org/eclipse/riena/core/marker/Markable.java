/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.marker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Standard implementation for the Markable interface, can be used by other
 * objects to delegate
 */
public class Markable implements IMarkable {

	private Set<IMarker> markers;

	/**
	 * Create new Instance and initialize
	 */
	public Markable() {
		super();
		markers = new HashSet<IMarker>();
	}

	/**
	 * @see org.eclipse.riena.core.marker.IMarkable#addMarker(org.eclipse.riena.core.marker.IMarker)
	 */
	public void addMarker(IMarker marker) {
		markers.add(marker);
	}

	/**
	 * @see org.eclipse.riena.core.marker.IMarkable#getMarkers()
	 */
	public Collection<? extends IMarker> getMarkers() {
		return markers;
	}

	/**
	 * @see org.eclipse.riena.core.marker.IMarkable#getMarkersOfType(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T extends IMarker> Collection<T> getMarkersOfType(Class<T> type) {
		if (type == null) {
			return Collections.emptyList();
		}
		List<T> typedMarkerList = new ArrayList<T>();

		for (IMarker marker : getMarkers()) {
			if (type.isAssignableFrom(marker.getClass())) {
				typedMarkerList.add((T) marker);
			}
		}
		return typedMarkerList;
	}

	/**
	 * @see org.eclipse.riena.core.marker.IMarkable#removeAllMarkers()
	 */
	public void removeAllMarkers() {
		markers.clear();
	}

	/**
	 * @see org.eclipse.riena.core.marker.IMarkable#removeMarker(org.eclipse.riena.core.marker.IMarker)
	 */
	public void removeMarker(IMarker marker) {
		markers.remove(marker);
	}

}
