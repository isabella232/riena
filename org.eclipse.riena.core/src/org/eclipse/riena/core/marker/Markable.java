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
package org.eclipse.riena.core.marker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Standard implementation for the {@link IMarkable} interface, can be used by
 * other objects to delegate
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

		if (marker.isUnique()) {
			Collection<? extends IMarker> markersOfType = getMarkersOfType(marker.getClass());
			boolean unique = false;
			for (Iterator<? extends IMarker> iterator = markersOfType.iterator(); iterator.hasNext();) {
				IMarker m = iterator.next();
				if (m.isUnique()) {
					unique = true;
				}
			}
			if (!unique) {
				markers.add(marker);
			}
		} else {
			markers.add(marker);
		}
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
	public <T extends IMarker> Collection<T> getMarkersOfType(Class<T> type) {
		return getMarkersOfType(getMarkers(), type);
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

	@SuppressWarnings("unchecked")
	public static <T extends IMarker> Collection<T> getMarkersOfType(Collection<? extends IMarker> markerSet,
			Class<T> type) {

		if ((type == null) || (markerSet == null)) {
			return Collections.emptyList();
		}

		List<T> typedMarkerList = new ArrayList<T>();
		for (IMarker marker : markerSet) {
			if (type.isAssignableFrom(marker.getClass())) {
				typedMarkerList.add((T) marker);
			}
		}

		return typedMarkerList;

	}

}
