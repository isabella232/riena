/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.core.util;

/**
 * a simple menu item. 
 * 
 * @author Juergen Becker
 */
public class Positionable implements IPositionable {
	private int position;

	/**
	 * @param position
	 */
	public Positionable(int position ) {
		super();
		this.position = position;
	}
	
	/**
	 * @see de.compeople.spirit.core.client.util.IPositionable#getPosition()
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position The position to set.
	 */
	public void setPosition( int position ) {
		this.position = position;
	}
}