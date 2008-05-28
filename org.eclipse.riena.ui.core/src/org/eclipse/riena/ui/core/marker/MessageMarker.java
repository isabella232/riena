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
 * Marks an adapter, with messages.
 * The messages can be viewed by an IMessageMarkerViewer
 * 
 * @author SST
 */
public class MessageMarker extends AbstractMarker implements IMessageMarker {

	/**
	 * @see Object#toString()
	 */
	public String toString() {
		StringBuilder buffer = new StringBuilder( "MessageMarker[" );
		buffer.append( "attributes=" );
		buffer.append( getAttributes() );
		buffer.append( "]" );
		return buffer.toString();
	}

	/**
	 * @return the Message in this Marker
	 */
	public String getMessage() {
		return (String) super.getAttribute( MESSAGE );
	}

	/**
	 * Basic constructor for the Message marker
	 * @param pMessage - the Message of the Marker
	 */
	public MessageMarker( String pMessage ) {
		super();
		assert pMessage != null : "The message of the message marker mus not be null";
		setAttribute( MESSAGE, pMessage );
	}

}