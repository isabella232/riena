/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.core.marker;

import org.eclipse.riena.core.marker.IMarker;


/**
 * This interface identifies all Marker able to work als MessageMarker.
 * Thes interface is necessary since a message marker can be at the same time an error marker,
 * or a mandatory marker.
 * Some implementations of an IMessageMarkerViewer correspond to this
 * interface an check if the classes to be shown implements this interface.
 *
 * @author SST
 */
public interface IMessageMarker extends IMarker {

	/**
	 * Content of the Message
	 */
	String MESSAGE = "message";
	
	/**
	 * @return the Message in this Marker
	 */
	String getMessage();
	
	
}
