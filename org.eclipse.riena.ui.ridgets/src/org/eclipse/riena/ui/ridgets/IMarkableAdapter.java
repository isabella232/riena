/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets;

import org.eclipse.riena.core.marker.IMarkable;
import org.eclipse.riena.ui.ridgets.obsolete.IMarkableUIRepresentation;

/**
 * An extension of the {@link IAdapter IAdapter}interface that supports
 * adding/removing {@link IMarker IMarker}. The associated UI control may
 * render the marks if it supports marking.
 * 
 * @author Ralf Stuckert
 */

/**
 * @deprecated
 */
@Deprecated
public interface IMarkableAdapter extends IAdapter, IMarkable, IMarkableUIRepresentation {

	/**
	 * The name of the PropertyChangeEvent that will be fired if a marker was
	 * added or removed.
	 */
	String PROPERTY_MARKER = "marker";

	/**
	 * The name of the PropertyChangeEvent that will be fired if the actual
	 * visibility of the adapter changes. An adapter is showing when it is
	 * visible and the parent of its UI representation is showing too.
	 */
	String PROPERTY_SHOWING = "showing";

	/**
	 * Sets the editing state of the UI representation of the adapter. If
	 * edited, the markers of type MandatoryMarker will be disabled.
	 * 
	 * @param edited
	 *            false, if the value of the adapter is empty/not edited, true
	 *            otherwise.
	 */
	void setEdited(boolean edited);

	/**
	 * @param showing
	 *            true, if a parent of the control corresponding to the adapter
	 *            is shown on the view, else false
	 */
	void parentShowingChanged(boolean showing);

	/**
	 * @return true, if the control corresponding to the adapter is shown on the
	 *         view, else false.
	 */
	boolean isShowing();
}