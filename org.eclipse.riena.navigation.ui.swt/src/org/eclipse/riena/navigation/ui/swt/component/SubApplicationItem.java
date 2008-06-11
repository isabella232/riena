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
package org.eclipse.riena.navigation.ui.swt.component;

import org.eclipse.riena.navigation.ISubApplication;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */
public class SubApplicationItem {

	private Composite parent;
	private ISubApplication subApplicationNode;
	private Rectangle bounds;

	public SubApplicationItem(Composite parent, ISubApplication subApplicationNode) {
		this.parent = parent;
		this.subApplicationNode = subApplicationNode;
	}

	/**
	 * Disposes this sub application item.
	 */
	public void dispose() {
	}

	/**
	 * @return the subApplicationNode
	 */
	public ISubApplication getSubApplicationNode() {
		return subApplicationNode;
	}

	/**
	 * @param subApplicationNode
	 *            the subApplicationNode to set
	 */
	public void setSubApplicationNode(ISubApplication subApplicationNode) {
		this.subApplicationNode = subApplicationNode;
	}

	/**
	 * Returns a rectangle describing the size and location of this
	 * sub-application item.
	 * 
	 * @return the bounds
	 */
	public Rectangle getBounds() {
		return bounds;
	}

	/**
	 * Sets a rectangle describing the size and location of this sub-application
	 * item.
	 * 
	 * @param bounds
	 *            the bounds to set
	 */
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

}
