/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.core.marker;

/**
 * A Marker working as ErrorMarker and as MessageMarker
 *
 * @author SST
 */
public class ErrorMessageMarker extends ErrorMarker implements IMessageMarker {

	/**
	 * @see Object#toString()
	 */
	public String toString() {
		StringBuilder buffer = new StringBuilder( "ErrorMessageMarker[" );
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
	public ErrorMessageMarker( String pMessage ) {
		super();
		assert pMessage != null : "The message of the message marker mus not be null";
		setAttribute( MESSAGE, pMessage );
	}

}
