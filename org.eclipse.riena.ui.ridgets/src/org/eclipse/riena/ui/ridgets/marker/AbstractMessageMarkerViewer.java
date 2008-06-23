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
package org.eclipse.riena.ui.ridgets.marker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Vector;

import org.eclipse.riena.ui.core.marker.IMessageMarker;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;

/**
 * Common functionality of classes visualizing certain types of message markers.
 */
public abstract class AbstractMessageMarkerViewer implements IMessageMarkerViewer {

	private HashSet<Class<? extends IMessageMarker>> markerTypes = new LinkedHashSet<Class<? extends IMessageMarker>>();
	private Collection<IMarkableRidget> ridgets = new ArrayList<IMarkableRidget>();

	/**
	 * @see org.eclipse.riena.ui.ridgets.marker.IMessageMarkerViewer#addRidget(org.eclipse.riena.ui.ridgets.IMarkableRidget)
	 */
	public void addRidget(IMarkableRidget markableRidget) {
		ridgets.add(markableRidget);
		showMessages(markableRidget);
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.marker.IMessageMarkerViewer#removeRidget(org.eclipse.riena.ui.ridgets.IMarkableRidget)
	 */
	public void removeRidget(IMarkableRidget markableRidget) {
		ridgets.remove(markableRidget);
		hideMessages(markableRidget);
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.marker.IMessageMarkerViewer#addMarkerType(java.lang.Class)
	 */
	public void addMarkerType(Class<? extends IMessageMarker> markerClass) {
		markerTypes.add(markerClass);
		showMessages();
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.marker.IMessageMarkerViewer#removeMarkerType(java.lang.Class)
	 */
	public void removeMarkerType(Class<? extends IMessageMarker> markerClass) {
		markerTypes.remove(markerClass);
		showMessages();
	}

	private void showMessages() {
		for (IMarkableRidget ridget : ridgets) {
			showMessages(ridget);
		}
	}

	protected abstract void showMessages(IMarkableRidget ridget);

	protected abstract void hideMessages(IMarkableRidget ridget);

	protected Collection<IMessageMarker> getMessageMarker(IMarkableRidget markableRidget) {
		return getMessageMarker(markableRidget, false);
	}

	protected Collection<IMessageMarker> getMessageMarker(IMarkableRidget markableRidget, boolean pRemove) {
		Collection<IMessageMarker> result = new Vector<IMessageMarker>();
		for (Class<? extends IMessageMarker> nextMessageMarkerType : markerTypes) {
			Collection<? extends IMessageMarker> nextMessageMarkers = markableRidget
					.getMarkersOfType(nextMessageMarkerType);
			if (nextMessageMarkers != null && nextMessageMarkers.size() > 0) {
				result.addAll(nextMessageMarkers);
			}
		}
		if (pRemove) {
			for (Iterator j = result.iterator(); j.hasNext();) {
				markableRidget.removeMarker((IMessageMarker) j.next());
			}
		}
		return result;
	}

}
