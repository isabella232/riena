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
package org.eclipse.riena.navigation.ui.swt.lnf.renderer;

import org.eclipse.riena.navigation.ui.swt.lnf.AbstractLnfRenderer;
import org.eclipse.riena.navigation.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.navigation.ui.swt.lnf.LnfManager;
import org.eclipse.riena.navigation.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

/**
 * Renderer of the border of the (undecorated (no OS-border, no OS-titlebar))
 * shell.
 */
public class ShellBorderRenderer extends AbstractLnfRenderer {

	private final static int BORDER_WIDTH = 2;

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.AbstractLnfRenderer#paint(org.eclipse.swt.graphics.GC,
	 *      java.lang.Object)
	 */
	@Override
	public void paint(GC gc, Object value) {

		gc.setAdvanced(true);
		gc.setAntialias(SWT.OFF);

		RienaDefaultLnf lnf = LnfManager.getLnf();

		// Border

		// -outer
		// --top
		Color borderColor = lnf.getColor(ILnfKeyConstants.TITLELESS_SHELL_BORDER_TOP_LEFT_COLOR);
		gc.setForeground(borderColor);
		int x = getBounds().x;
		int y = getBounds().y;
		int w = getWidth();
		gc.drawLine(x, y, x + w, y);
		// --bottom
		borderColor = lnf.getColor(ILnfKeyConstants.TITLELESS_SHELL_BORDER_BOTTOM_RIGHT_COLOR);
		gc.setForeground(borderColor);
		y = getBounds().y + getHeight();
		gc.drawLine(x, y, x + w, y);
		// --left
		borderColor = lnf.getColor(ILnfKeyConstants.TITLELESS_SHELL_BORDER_TOP_LEFT_COLOR);
		gc.setForeground(borderColor);
		x = getBounds().x;
		y = getBounds().y;
		int h = getHeight();
		gc.drawLine(x, y, x, y + h);
		// --right
		borderColor = lnf.getColor(ILnfKeyConstants.TITLELESS_SHELL_BORDER_BOTTOM_RIGHT_COLOR);
		gc.setForeground(borderColor);
		x = getBounds().x + getWidth();
		gc.drawLine(x, y, x, y + h);

		// -inner
		// --top
		borderColor = lnf.getColor(ILnfKeyConstants.TITLELESS_SHELL_INNER_BORDER_TOP_LEFT_COLOR);
		gc.setForeground(borderColor);
		x = getBounds().x + 1;
		y = getBounds().y + 1;
		w = getWidth() - 2;
		gc.drawLine(x, y, x + w, y);
		// --bottom
		borderColor = lnf.getColor(ILnfKeyConstants.TITLELESS_SHELL_INNER_BORDER_BOTTOM_RIGHT_COLOR);
		gc.setForeground(borderColor);
		y = getBounds().y + getHeight() - 1;
		gc.drawLine(x, y, x + w, y);
		// --left
		borderColor = lnf.getColor(ILnfKeyConstants.TITLELESS_SHELL_INNER_BORDER_TOP_LEFT_COLOR);
		gc.setForeground(borderColor);
		x = getBounds().x + 1;
		y = getBounds().y + 1;
		h = getHeight() - 2;
		gc.drawLine(x, y, x, y + h);
		// --right
		borderColor = lnf.getColor(ILnfKeyConstants.TITLELESS_SHELL_INNER_BORDER_BOTTOM_RIGHT_COLOR);
		gc.setForeground(borderColor);
		x = getBounds().x + getWidth() - 1;
		y = getBounds().y + 2;
		h = getHeight() - 4;
		gc.drawLine(x, y, x, y + h);

	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.ILnfRenderer#dispose()
	 */
	public void dispose() {
		// nothing to do
	}

	private int getHeight() {
		return getBounds().height - 1;
	}

	private int getWidth() {
		return getBounds().width - 1;
	}

	/**
	 * Returns the width of the border (including padding)
	 * 
	 * @return border width
	 */
	public int getCompelteBorderWidth() {

		int width = BORDER_WIDTH;

		RienaDefaultLnf lnf = LnfManager.getLnf();
		Integer padding = lnf.getIntegerSetting(ILnfKeyConstants.TITLELESS_SHELL_PADDING);
		if (padding != null) {
			width += padding;
		}

		return width;

	}

	/**
	 * Returns the width of the border.
	 * 
	 * @return border width
	 */
	public int getBorderWidth() {

		return BORDER_WIDTH;

	}

}
