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
package org.eclipse.riena.navigation.ui.swt.lnf.rienadefault;

import org.eclipse.riena.navigation.ui.swt.lnf.AbstractLnfRenderer;
import org.eclipse.riena.navigation.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.navigation.ui.swt.lnf.LnfManager;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

/**
 * Renderer of the border that is displayed when the mouse is over the module.
 */
public class HoverBorderRenderer extends AbstractLnfRenderer {

	private final static int PADDING = 1;

	/**
	 * @param value -
	 *            is ignored
	 * 
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.AbstractLnfRenderer#paint(org.eclipse.swt.graphics.GC,
	 *      java.lang.Object)
	 */
	@Override
	public void paint(GC gc, Object value) {

		RienaDefaultLnf lnf = LnfManager.getLnf();
		int x = getBounds().x + PADDING;
		int y = getBounds().y + PADDING;
		int width = getBounds().width - 2 * PADDING;
		int height = getBounds().height - 2 * PADDING;

		// top
		Color topColor = lnf.getColor(ILnfKeyConstants.EMBEDDED_TITLEBAR_HOVER_BORDER_TOP_COLOR);
		gc.setForeground(topColor);
		gc.drawLine(x + 1, y, x + width - 2, y);
		Color startColor = lnf.getColor(ILnfKeyConstants.EMBEDDED_TITLEBAR_HOVER_BORDER_START_COLOR);
		gc.setForeground(startColor);
		gc.drawLine(x, y + 1, x + width - 1, y + 1);

		// left
		Color endColor = lnf.getColor(ILnfKeyConstants.EMBEDDED_TITLEBAR_HOVER_BORDER_END_COLOR);
		gc.setBackground(endColor);
		gc.fillGradientRectangle(x, y + 1, 2, height - 1, true);

		// right
		gc.fillGradientRectangle(x + width - 2, y + 1, 2, height - 1, true);

		// bottom
		gc.setForeground(endColor);
		gc.drawLine(x, y + height - 1, x + width - 1, y + height - 1);
		Color bottomColor = lnf.getColor(ILnfKeyConstants.EMBEDDED_TITLEBAR_HOVER_BORDER_BOTTOM_COLOR);
		gc.setForeground(bottomColor);
		gc.drawLine(x + 1, y + height, x + width - 2, y + height);

	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.ILnfRenderer#dispose()
	 */
	public void dispose() {
	}

}
