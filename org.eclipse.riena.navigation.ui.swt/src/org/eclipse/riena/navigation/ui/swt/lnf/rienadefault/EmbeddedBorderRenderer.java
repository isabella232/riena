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
import org.eclipse.riena.navigation.ui.swt.lnf.LnfManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

/**
 * 
 */
public class EmbeddedBorderRenderer extends AbstractLnfRenderer {

	private boolean active;

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
		Color borderColor = lnf.getColor("EmbeddedTitlebar.passiveBorderColor");
		if (isActive()) {
			borderColor = lnf.getColor("EmbeddedTitlebar.activeBorderColor");
		}
		gc.setForeground(borderColor);
		// -outer
		// --top
		int x = getBounds().x + 2;
		int y = getBounds().y;
		int w = getWidth() - 4;
		gc.drawLine(x, y, x + w, y);
		// --bottom
		y = getBounds().y + getHeight();
		gc.drawLine(x, y, x + w, y);
		// --left
		x = getBounds().x;
		y = getBounds().y + 2;
		int h = getHeight() - 4;
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

	public Rectangle computeInnerBounds(GC gc, Rectangle outerBounds) {
		return new Rectangle(outerBounds.x + 2, outerBounds.y + 2, outerBounds.width - 4, outerBounds.height - 4);
	}

}
