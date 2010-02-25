/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.swt.facades;

import org.eclipse.swt.graphics.GC;

/**
 * Facade for the {@link GC} class.
 * 
 * @since 2.0
 */
public abstract class GCFacade {

	private static final GCFacade INSTANCE = (GCFacade) FacadeFactory.newFacade(GCFacade.class);

	public static final GCFacade getDefault() {
		return INSTANCE;
	}

	/**
	 * Draws a round-cornered rectangle according to the given arguments with
	 * the GC's foreground color.
	 * 
	 * @param GC
	 *            a GC instance; never null
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 * @param arcWidth
	 *            the width of the arc
	 * @param arcHeight
	 *            the height of the arc
	 */
	public abstract void drawRoundRectangle(GC gc, int x, int y, int width, int height, int arcWidth, int arcHeight);

}
