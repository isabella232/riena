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

/**
 * A Ridget that supports markers. All markers are maintained in a list, ie it
 * is valid behaviour to 'mark' a ridget with the same marker type multiple
 * times. The <code>#isXXX</code> methods defined in this interface answer
 * <code>true</code> until the last marker of type <code>XXX</code> has been
 * removed.
 * 
 * @see org.eclipse.riena.core.marker.IMarker
 */
public interface IMarkableRidget extends IRidget, IMarkable {

	/**
	 * The name of the PropertyChangeEvent that will be fired if a marker was
	 * added or removed.
	 */
	String PROPERTY_MARKER = "marker"; //$NON-NLS-1$

	/**
	 * @return Indicates whether any DisabledMarker was added.
	 */
	boolean isEnabled();

	/**
	 * Adds and removes a default DisabledMarker.
	 * 
	 * @param enabled
	 *            The new enabled state.
	 */
	void setEnabled(boolean enabled);

	/**
	 * @return Indicates whether any ErrorMarker was added.
	 */
	boolean isErrorMarked();

	/**
	 * Adds and removes a default ErrorMarker.
	 * 
	 * @param errorMarked
	 *            The new errorMarked state.
	 */
	void setErrorMarked(boolean errorMarked);

	/**
	 * Returns true if mandatory marker should be disabled.
	 * 
	 * @return true, if mandatory marker should be disabled, else false.
	 */
	boolean isDisableMandatoryMarker();

	/**
	 * @return Indicates whether any MandatoryMarker was added.
	 */
	boolean isMandatory();

	/**
	 * Adds and removes a default MandatoryMarker.
	 * 
	 * @param outputOnly
	 *            <code>true</code> if the ridget holds a mandatory value,
	 *            <code>false</code> otherwise.
	 */
	void setMandatory(boolean mandatory);

	/**
	 * @return Indicates whether any OutputMarker was added.
	 */
	boolean isOutputOnly();

	/**
	 * Adds and removes a default OutputMarker.
	 * 
	 * @param outputOnly
	 *            <code>true</code> if the ridget should be 'output only'
	 *            (=cannot be edited), <code>false</code> otherwise.
	 */
	void setOutputOnly(boolean outputOnly);
}
