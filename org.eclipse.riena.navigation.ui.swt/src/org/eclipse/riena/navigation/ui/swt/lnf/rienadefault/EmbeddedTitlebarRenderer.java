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

import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.navigation.ui.swt.lnf.AbstractLnfRenderer;
import org.eclipse.riena.navigation.ui.swt.lnf.LnfManager;
import org.eclipse.riena.navigation.ui.swt.utils.ImageUtil;
import org.eclipse.riena.navigation.ui.swt.utils.SwtUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

/**
 * Renderer of the title bar of an embedded view.
 */
public class EmbeddedTitlebarRenderer extends AbstractLnfRenderer {

	private final static int TITLEBAR_LABEL_PADDING_LEFT = 5;
	private final static int TITLEBAR_LABEL_PADDING = 3;
	private final static int TITLEBAR_ICON_TEXT_GAP = 4;

	private Image image;
	private String icon;
	private boolean active;
	private boolean pressed;

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.ILnfRenderer#dispose()
	 */
	public void dispose() {
		if (getImage() != null) {
			getImage().dispose();
			setImage(null);
		}
	}

	public Point computeSize(GC gc, int wHint, int hHint) {

		RienaDefaultLnf lnf = LnfManager.getLnf();
		Font font = lnf.getFont("EmbeddedTitlebar.font");
		gc.setFont(font);
		FontMetrics fontMetrics = gc.getFontMetrics();

		int h = fontMetrics.getHeight() + TITLEBAR_LABEL_PADDING * 2;
		if (getImage() != null) {
			int imageH = getImage().getBounds().height + TITLEBAR_LABEL_PADDING * 2;
			h = Math.max(h, imageH);
		}

		return new Point(wHint, h);

	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.AbstractLnfRenderer#paint(org.eclipse.swt.graphics.GC,
	 *      java.lang.Object)
	 */
	public void paint(GC gc, Object value) {

		gc.setAdvanced(true);
		gc.setAntialias(SWT.OFF);

		RienaDefaultLnf lnf = LnfManager.getLnf();
		Font font = lnf.getFont("EmbeddedTitlebar.font");
		gc.setFont(font);
		FontMetrics fontMetrics = gc.getFontMetrics();

		// Background
		Color startColor = lnf.getColor("EmbeddedTitlebar.passiveBackgroundStartColor");
		Color endColor = lnf.getColor("EmbeddedTitlebar.passiveBackgroundEndColor");
		if (isActive()) {
			startColor = lnf.getColor("EmbeddedTitlebar.activeBackgroundStartColor");
			endColor = lnf.getColor("EmbeddedTitlebar.activeBackgroundEndColor");
		}
		gc.setForeground(startColor);
		gc.setBackground(endColor);
		int x = getBounds().x;
		int y = getBounds().y;
		int w = getWidth();
		int h = getHeight();
		if (isPressed()) {
			gc.fillRectangle(x, y, w, h);
		} else {
			gc.fillGradientRectangle(x, y, w, h, true);
		}

		// Border
		Color borderColor = lnf.getColor("EmbeddedTitlebar.passiveBorderColor");
		if (isActive()) {
			borderColor = lnf.getColor("EmbeddedTitlebar.activeBorderColor");
		}
		gc.setForeground(borderColor);
		// - top
		x = getBounds().x + 1;
		y = getBounds().y;
		w = getWidth() - 2;
		gc.drawLine(x, y, x + w, y);
		// - bottom
		y = getBounds().y + getHeight();
		gc.drawLine(x, y, x + w, y);
		// - left
		x = getBounds().x;
		y = getBounds().y + 1;
		h = getHeight() - 2;
		gc.drawLine(x, y, x, y + h);
		// - right
		x = getBounds().x + getWidth();
		gc.drawLine(x, y, x, y + h);

		// Icon
		x = getBounds().x + TITLEBAR_LABEL_PADDING_LEFT;
		if (getImage() != null) {
			y = getBounds().y + (getHeight() - getImage().getImageData().height) / 2;
			gc.drawImage(getImage(), x, y);
			x += getImage().getImageData().width + TITLEBAR_ICON_TEXT_GAP;
		}

		// Text
		String text = ""; //$NON-NLS-1$
		if (value instanceof String) {
			text = (String) value;
		}
		if (!StringUtils.isEmpty(text)) {
			gc.setForeground(lnf.getColor("EmbeddedTitlebar.foreground"));

			int y2 = (getHeight() - gc.getFontMetrics().getHeight()) / 2;
			if ((getHeight() - gc.getFontMetrics().getHeight()) % 2 != 0) {
				y2++;
			}
			y = getBounds().y + y2;
			int maxWidth = getWidth() - (x - getBounds().x) - TITLEBAR_LABEL_PADDING;
			text = SwtUtilities.clipText(gc, text, maxWidth);
			gc.drawText(text, x, y, true);
		}

	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
		setImage(ImageUtil.getImage(icon));
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isPressed() {
		return pressed;
	}

	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}

	private Image getImage() {
		return image;
	}

	private void setImage(Image image) {
		this.image = image;
	}

	private int getHeight() {
		return getBounds().height - 1;
	}

	private int getWidth() {
		return getBounds().width - 1;
	}

}
