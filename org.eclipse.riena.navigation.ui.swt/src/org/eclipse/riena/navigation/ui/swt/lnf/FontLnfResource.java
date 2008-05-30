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

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.widgets.Display;

/**
 * Wrapper for resource font.
 */
public class FontLnfResource extends AbstractLnfResource {

	/**
	 * @param name -
	 *            the name of the font (must not be null)
	 * @param height -
	 *            the font height in points
	 * @param style -
	 *            style of the font (a bit or combination of NORMAL, BOLD,
	 *            ITALIC)
	 */
	public FontLnfResource(String name, int height, int style) {
		super(new Font(Display.getCurrent(), name, height, style));
	}

	/**
	 * @param font -
	 *            font to wrap
	 */
	public FontLnfResource(Font font) {
		super(font);
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.AbstractLnfResource#getResource()
	 */
	@Override
	public Resource getResource() {
		return (Font) super.getResource();
	}

}
