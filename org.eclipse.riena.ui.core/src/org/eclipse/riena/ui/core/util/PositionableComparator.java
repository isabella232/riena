/*******************************************************************************
 * Copyright (c) 2007, 2008 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.core.util;

import java.io.Serializable;
import java.util.Comparator;

/**
 * class used to sort the application tabs.
 */
public class PositionableComparator implements Comparator<IPositionable>, Serializable {

	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(IPositionable o1, IPositionable o2) {
		return o1.getPosition() - o2.getPosition();
	}
}
