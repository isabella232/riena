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
 * Marks an adapter, resp. its associated UI control, as mandatory.
 *
 * @author Ralf Stuckert
 */
public class MandatoryMarker extends AbstractMarker implements IIconizableMarker {

	/**
	 * Attribute holding the disabled state.
	 */
	public static final String ATTRIBUTE_DISABLED = "disabled";

	/**
	 * @see Object#toString()
	 */
	public String toString() {
		StringBuilder buffer = new StringBuilder( "MandatoryMarker[" );
		buffer.append( "attributes=" );
		buffer.append( getAttributes() );
		buffer.append( "]" );
		return buffer.toString();
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.marker.IIconizableMarker#getIconConfiguationKey()
	 */
	public String getIconConfiguationKey() {
		return "MandatoryMarker";
	}

	/**
	 * Returns the disabled state.
	 *
	 * @return true, if the marker is disabled, false otherwise.
	 */
	public boolean isDisabled() {
		Object attributeDisabled = getAttribute( ATTRIBUTE_DISABLED );
		return ( attributeDisabled != null && attributeDisabled.equals( Boolean.TRUE ) );
	}

	/**
	 * Sets the disabled state.
	 *
	 * @param disabled The new disabled state: true, to disable the marker; false, to
	 *                 enable it.
	 */
	public void setDisabled( boolean disabled ) {
		if ( disabled ) {
			setAttribute( ATTRIBUTE_DISABLED, Boolean.TRUE );
		} else {
			setAttribute( ATTRIBUTE_DISABLED, null );
		}
	}

}