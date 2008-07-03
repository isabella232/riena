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
import org.eclipse.swt.graphics.Rectangle;

/**
 * Renderer of an embedded border.
 */
public class EmbeddedBorderRenderer extends AbstractLnfRenderer {

	private final static int BORDER_WIDTH = 2;
	private boolean active;

	/**
	 * @param value -
	 *            is ignored
	 * 
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.AbstractLnfRenderer#paint(org.eclipse.swt.graphics.GC,
	 *      java.lang.Object)
	 */
	@Override
	public void paint(GC gc, Object value) {

		gc.setAdvanced(true);
		gc.setAntialias(SWT.OFF);

		RienaDefaultLnf lnf = LnfManager.getLnf();

		// Border
		Color borderColor = lnf.getColor(ILnfKeyConstants.EMBEDDED_TITLEBAR_PASSIVE_BORDER_COLOR);
		if (isActive()) {
			borderColor = lnf.getColor(ILnfKeyConstants.EMBEDDED_TITLEBAR_ACTIVE_BORDER_COLOR);
		}
		gc.setForeground(borderColor);
		// -outer
		// --top
		int x = getBounds().x + BORDER_WIDTH;
		int y = getBounds().y;
		int w = getWidth() - BORDER_WIDTH * 2;
		gc.drawLine(x, y, x + w, y);
		// --bottom
		y = getBounds().y + getHeight();
		gc.drawLine(x, y, x + w, y);
		// --left
		x = getBounds().x;
		y = getBounds().y + BORDER_WIDTH;
		int h = getHeight() - BORDER_WIDTH * 2;
		gc.drawLine(x, y, x, y + h);
		// --right
		x = getBounds().x + getWidth();
		gc.drawLine(x, y, x, y + h);

		// -inner
		// --top
		x = getBounds().x + 1;
		y = getBounds().y + 1;
		w = getWidth() - 2;
		gc.drawLine(x, y, x + w, y);
		// --bottom
		y = getBounds().y + getHeight() - 1;
		gc.drawLine(x, y, x + w, y);
		// --left
		x = getBounds().x + 1;
		y = getBounds().y + 1;
		h = getHeight() - 2;
		gc.drawLine(x, y, x, y + h);
		// --right
		x = getBounds().x + getWidth() - 1;
		gc.drawLine(x, y, x, y + h);
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.ILnfRenderer#dispose()
	 */
	public void dispose() {
		// nothing to do
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	private int getHeight() {
		return getBounds().height - 1;
	}

	private int getWidth() {
		return getBounds().width - 1;
	}

	/**
	 * Computes the size inside the given outer border.
	 * 
	 * @param outerBounds -
	 *            bounds of the outer border
	 * 
	 * @return bounds of the inner border
	 */
	public Rectangle computeInnerBounds(Rectangle outerBounds) {
		return new Rectangle(outerBounds.x + BORDER_WIDTH, outerBounds.y + BORDER_WIDTH, outerBounds.width
				- BORDER_WIDTH * 2, outerBounds.height - BORDER_WIDTH * 2);
	}

	/**
	 * Computes the outside height of the border.
	 * 
	 * @param innerHeight -
	 *            the inner height of the border
	 * 
	 * @return outer border height
	 */
	public int computeOuterHeight(int innerHeight) {
		return innerHeight + BORDER_WIDTH * 2;
	}

	/**
	 * Computes the outside width of the border.
	 * 
	 * @param innerWidth -
	 *            the inner width of the border
	 * 
	 * @return outer border width
	 */
	public int computeOuterWidth(int innerWidth) {
		return innerWidth + BORDER_WIDTH * 2;
	}

}
