/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.core.marker;

import org.eclipse.riena.core.marker.AbstractMarker;

/**
 * Marks an adapter, resp. its associated UI control, as error.
 *
 * @author Ralf Stuckert
 */
public class ErrorMarker extends AbstractMarker implements IIconizableMarker {

	/**
	 * @see Object#toString()
	 */
	public String toString() {
		StringBuilder buffer = new StringBuilder( "ErrorMarker[" );
		buffer.append( "attributes=" );
		buffer.append( getAttributes() );
		buffer.append( "]" );
		return buffer.toString();
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.marker.IIconizableMarker#getIconConfiguationKey()
	 */
	public String getIconConfiguationKey() {
		return "ErrorMarker";
	}
}