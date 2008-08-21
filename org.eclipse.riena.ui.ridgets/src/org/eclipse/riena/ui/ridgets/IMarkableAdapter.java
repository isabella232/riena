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
	String PROPERTY_MARKER = "marker"; //$NON-NLS-1$

	/**
	 * The name of the PropertyChangeEvent that will be fired if the actual
	 * visibility of the adapter changes. An adapter is showing when it is
	 * visible and the parent of its UI representation is showing too.
	 */
	String PROPERTY_SHOWING = "showing"; //$NON-NLS-1$

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
