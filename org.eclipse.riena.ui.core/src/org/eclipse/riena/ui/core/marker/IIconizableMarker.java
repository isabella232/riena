/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2005 compeople AG                         *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.core.marker;

import org.eclipse.riena.core.marker.IMarker;


/**
 * A marker that can be visualized by marking the icons of a marked element with a
 * marker icon.
 *
 * @author Carsten Drossel
 */
public interface IIconizableMarker extends IMarker {

	/**
	 * Returns the key to the configuration of the marker icon. What this configuration
	 * looks like and where it is located depends on the UI implementation.
	 *
	 * @return The key to the configuration of the marker icon.
	 */
	String getIconConfiguationKey();

}
