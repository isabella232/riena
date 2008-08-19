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
 * a simple menu item.
 */
public class Positionable implements IPositionable {
	private int position;

	/**
	 * @param position
	 */
	public Positionable(int position) {
		super();
		this.position = position;
	}

	/**
	 * @see org.eclipse.riena.ui.core.util.IPositionable#getPosition()
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            The position to set.
	 */
	public void setPosition(int position) {
		this.position = position;
	}
}
