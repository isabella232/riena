/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2005 compeople AG                         *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.core.marker;


/**
 * A property change event that was caused by the change of a property holding
 * markers.
 * @see de.compeople.spirit.core.client.uibinding.IMarker
 *
 * @author Carsten Drossel
 */
public interface IMarkerPropertyChangeEvent {

	/**
	 * Indicates whether the event was caused by the change of a markers property only.
	 *
	 * @return true if the only cause of the event is the change of a marker property,
	 *         false otherwise.
	 */
	boolean isAttributeRelated();

}
