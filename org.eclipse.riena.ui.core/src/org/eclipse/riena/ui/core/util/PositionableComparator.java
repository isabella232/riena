/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/

package org.eclipse.riena.ui.core.util;

import java.io.Serializable;
import java.util.Comparator;

/**
 * class used to sort the application tabs.
 *
 * @author Juergen Becker
 */
public class PositionableComparator implements Comparator<IPositionable>, Serializable {

	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare( IPositionable o1, IPositionable o2 ) {
		return o1.getPosition() - o2.getPosition();
	}
}