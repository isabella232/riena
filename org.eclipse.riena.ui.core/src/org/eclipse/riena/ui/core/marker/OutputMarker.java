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
 * Marks an adapter, resp. its associated UI control, as output.
 *
 * @author Alexander Ziegler
 */
public class OutputMarker extends AbstractMarker {

	/**
	 * @see Object#toString()
	 */
	public String toString() {
		StringBuilder buffer = new StringBuilder( "OutputMarker[" );
		buffer.append( "attributes=" );
		buffer.append( getAttributes() );
		buffer.append( "]" );
		return buffer.toString();
	}
}
