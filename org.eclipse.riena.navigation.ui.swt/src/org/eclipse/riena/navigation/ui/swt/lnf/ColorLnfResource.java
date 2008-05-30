/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.ui.swt.lnf;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Wrapper for resource color.
 */
public class ColorLnfResource extends AbstractLnfResource {

	/**
	 * @param red -
	 *            the amount of red in the color
	 * @param green -
	 *            the amount of green in the color
	 * @param blue -
	 *            the amount of blue in the color
	 */
	public ColorLnfResource(int red, int green, int blue) {
		super(new Color(Display.getCurrent(), red, green, blue));
	}

	/**
	 * @param rgb -
	 *            the RGB values of the desired color
	 */
	public ColorLnfResource(RGB rgb) {
		super(new Color(Display.getCurrent(), rgb));
	}

	/**
	 * @param color -
	 *            color to wrap
	 */
	public ColorLnfResource(Color color) {
		super(color);
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.AbstractLnfResource#getResource()
	 */
	@Override
	public Color getResource() {
		return (Color) super.getResource();
	}

}
