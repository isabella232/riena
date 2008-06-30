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
package org.eclipse.riena.navigation.ui.swt.lnf;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.widgets.Display;

/**
 * Wrapper for resource color.
 */
public class ColorLnfResource extends AbstractLnfResource {

	private RGB rgb;

	/**
	 * @param red -
	 *            the amount of red in the color
	 * @param green -
	 *            the amount of green in the color
	 * @param blue -
	 *            the amount of blue in the color
	 */
	public ColorLnfResource(int red, int green, int blue) {
		this(new RGB(red, green, blue));
	}

	/**
	 * @param rgb -
	 *            the RGB values of the desired color
	 */
	public ColorLnfResource(RGB rgb) {
		super();
		this.rgb = rgb;
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.AbstractLnfResource#getResource()
	 */
	@Override
	public Color getResource() {
		return (Color) super.getResource();
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.ILnfResource#createResource()
	 */
	public Resource createResource() {
		if (rgb == null) {
			return null;
		}
		return new Color(Display.getCurrent(), rgb);
	}

}
