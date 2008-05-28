/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/

package org.eclipse.riena.ui.core.util;

/**
 * used to position and arrange ui elements.
 * 
 * @author Juergen Becker
 */
public interface IPositionable {
	/**
	 * get the position
	 * 
	 * @return the position.
	 */
	int getPosition();

	/**
	 * set the position
	 * 
	 * @param position
	 */
	void setPosition( int position );

}