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

/**
 * used to position and arrange ui elements.
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
	void setPosition(int position);

}
