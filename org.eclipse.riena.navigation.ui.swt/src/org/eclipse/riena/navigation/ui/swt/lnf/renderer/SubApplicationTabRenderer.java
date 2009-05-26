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
package org.eclipse.riena.navigation.ui.swt.lnf.renderer;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.ui.swt.lnf.AbstractLnfRenderer;
import org.eclipse.riena.ui.swt.lnf.FlasherSupportForRenderer;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.eclipse.riena.ui.swt.utils.ImageStore;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Renderer of a tab of the switcher between sub-applications.
 */
public class SubApplicationTabRenderer extends AbstractLnfRenderer {

	public final static int ACTIVE_Y_OFFSET = 2;
	private final static int BORDER_TOP_WIDTH = 3;
	private final static int BORDER_BOTTOM_WIDTH = 1;
	private final static int BORDER_LEFT_WIDTH = 2;
	private final static int BORDER_RIGHT_WIDTH = 2;
	private final static int TEXT_TOP_INSET = 3;
	private final static int TEXT_BOTTOM_INSET = 4;
	private final static int TEXT_LEFT_INSET = 6;
	private final static int TEXT_RIGHT_INSET = 6;
	private final static int ACTIVE_BOTTOM_INSET = 6;
	private final static int ACTIVE_LEFT_INSET = 3;
	private final static int ACTIVE_RIGHT_INSET = 3;
	private final static int ICON_TEXT_GAP = 4;

	private Color selStartColor;
	private Color selEndColor;
	private Image image;
	private String icon;
	private String label;
	private boolean activated;
	private Control control;
	private FlasherSupportForRenderer flasherSupport;

	/**
	 * Create a new instance of the renderer of a tab of the sub-application
	 * switcher.
	 */
	public SubApplicationTabRenderer() {
		super();
		flasherSupport = new FlasherSupportForRenderer(this, new MarkerUpdater());
	}

	/**
	 * @see org.eclipse.riena.ui.swt.lnf.AbstractLnfRenderer#paint(org.eclipse.swt.graphics.GC,
	 *      java.lang.Object)
	 */
	@Override
	public void paint(GC gc, Object value) {

		Assert.isNotNull(gc);
		Assert.isNotNull(value);
		Assert.isTrue(value instanceof Control);
		control = (Control) value;

		RienaDefaultLnf lnf = LnfManager.getLnf();
		int leftInset = 0;
		if (isActivated()) {
			leftInset = ACTIVE_LEFT_INSET;
		}
		int rightInset = 0;
		if (isActivated()) {
			rightInset = ACTIVE_RIGHT_INSET;
		}
		Font font = getTabFont();
		gc.setFont(font);

		// Background
		Color backgroundStartColor = lnf
				.getColor(LnfKeyConstants.SUB_APPLICATION_SWITCHER_PASSIVE_BACKGROUND_START_COLOR);
		if (isActivated() || flasherSupport.isProcessMarkerVisible()) {
			backgroundStartColor = lnf.getColor(LnfKeyConstants.SUB_APPLICATION_SWITCHER_ACTIVE_BACKGROUND_START_COLOR);
		}
		gc.setForeground(backgroundStartColor);
		Color backgroundEndColor = lnf.getColor(LnfKeyConstants.SUB_APPLICATION_SWITCHER_PASSIVE_BACKGROUND_END_COLOR);
		if (isActivated() || flasherSupport.isProcessMarkerVisible()) {
			backgroundEndColor = lnf.getColor(LnfKeyConstants.SUB_APPLICATION_SWITCHER_ACTIVE_BACKGROUND_END_COLOR);
		}
		gc.setBackground(backgroundEndColor);
		int x = getBounds().x + BORDER_LEFT_WIDTH - 1 - leftInset;
		int y = getBounds().y + 1;
		int w = getWidth() - BORDER_LEFT_WIDTH - BORDER_RIGHT_WIDTH + 3 + leftInset + rightInset;
		int h = getHeight() - 1;
		gc.fillGradientRectangle(x, y, w, h, true);

		Color borderTopRightColor = lnf.getColor(LnfKeyConstants.SUB_APPLICATION_SWITCHER_BORDER_TOP_RIGHT_COLOR);
		Color borderBottomLeftColor = lnf.getColor(LnfKeyConstants.SUB_APPLICATION_SWITCHER_BORDER_BOTTOM_LEFT_COLOR);
		if (!isEnabled()) {
			borderTopRightColor = lnf
					.getColor(LnfKeyConstants.SUB_APPLICATION_SWITCHER_DISABLED_BORDER_TOP_RIGHT_COLOR);
			borderBottomLeftColor = lnf
					.getColor(LnfKeyConstants.SUB_APPLICATION_SWITCHER_DISABLED_BORDER_BOTTOM_LEFT_COLOR);
		}
		// Border
		// - left
		gc.setForeground(borderBottomLeftColor);
		x = getBounds().x - leftInset;
		y = getBounds().y + BORDER_TOP_WIDTH;
		int x2 = x;
		int y2 = getBounds().y + getHeight() - 1;
		gc.drawLine(x, y, x2, y2);
		Color innerBorderColor = lnf.getColor(LnfKeyConstants.SUB_APPLICATION_SWITCHER_INNER_BORDER_COLOR);
		if (!isEnabled()) {
			innerBorderColor = lnf.getColor(LnfKeyConstants.SUB_APPLICATION_SWITCHER_INNER_DISABLED_BORDER_COLOR);
		}
		if (!isActivated()) {
			gc.setForeground(innerBorderColor);
			x += 1;
			x2 += 1;
			gc.drawLine(x, y, x2, y2);
		}
		// -top
		gc.setForeground(borderTopRightColor);
		x = getBounds().x + BORDER_LEFT_WIDTH - leftInset;
		y = getBounds().y;
		x2 = x + getWidth() - BORDER_LEFT_WIDTH - BORDER_RIGHT_WIDTH + rightInset;
		y2 = y;
		gc.drawLine(x, y, x2, y2);
		// --top-left
		x = getBounds().x - leftInset;
		y = getBounds().y + BORDER_TOP_WIDTH - 1;
		x2 = x + 1;
		y2 = y;
		gc.drawLine(x, y, x2, y2);
		x = getBounds().x + 1 - leftInset;
		y = getBounds().y + BORDER_TOP_WIDTH - 2;
		x2 = x + 1;
		y2 = y;
		gc.drawLine(x, y, x2, y2);
		// --top-right
		x = getBounds().x + getWidth() + rightInset;
		y = getBounds().y + BORDER_TOP_WIDTH - 1;
		x2 = x - 1;
		y2 = y;
		gc.drawLine(x, y, x2, y2);
		x = getBounds().x + getWidth() - 1 + rightInset;
		y = getBounds().y + BORDER_TOP_WIDTH - 2;
		x2 = x - 1;
		y2 = y;
		gc.drawLine(x, y, x2, y2);
		// -right
		gc.setForeground(borderTopRightColor);
		x = getBounds().x + getWidth() + rightInset;
		y = getBounds().y + BORDER_TOP_WIDTH;
		x2 = x;
		y2 = getBounds().y + getHeight() - 1;
		gc.drawLine(x, y, x2, y2);
		if (!isActivated()) {
			gc.setForeground(innerBorderColor);
			x -= 1;
			x2 -= 1;
			gc.drawLine(x, y, x2, y2);
		}
		// - bottom
		if (isActivated()) {
			gc.setForeground(backgroundEndColor);
		} else {
			gc.setForeground(borderBottomLeftColor);
		}
		x = getBounds().x - leftInset;
		y = getBounds().y + getHeight();
		x2 = getBounds().x + getWidth() + rightInset;
		y2 = y;
		gc.drawLine(x, y, x2, y2);

		// Icon
		x = getBounds().x + BORDER_LEFT_WIDTH + TEXT_LEFT_INSET;
		if (getImage() != null) {
			y = getBounds().y + BORDER_TOP_WIDTH + TEXT_TOP_INSET;
			FontMetrics fontMetrics = gc.getFontMetrics();
			y += fontMetrics.getHeight() / 2;
			y -= getImage().getBounds().height / 2;
			gc.drawImage(getImage(), x, y);
			x += getImage().getBounds().width + ICON_TEXT_GAP;
		}

		// Text
		Color foreground = lnf.getColor(LnfKeyConstants.SUB_APPLICATION_SWITCHER_FOREGROUND);
		if (!isEnabled()) {
			foreground = lnf.getColor(LnfKeyConstants.SUB_APPLICATION_SWITCHER_DISABLED_FOREGROUND);
		}
		gc.setForeground(foreground);
		y = getBounds().y + BORDER_TOP_WIDTH + TEXT_TOP_INSET;
		gc.drawText(getLabel(), x, y, true);

		// Selection
		if (isActivated() || flasherSupport.isProcessMarkerVisible()) {
			Color selColor = lnf.getColor(LnfKeyConstants.SUB_APPLICATION_SWITCHER_TOP_SELECTION_COLOR);
			gc.setForeground(selColor);
			gc.setBackground(selColor);
			x = getBounds().x - leftInset;
			y = getBounds().y;
			w = 2;
			h = 2;
			gc.fillRectangle(x, y, w, h);
			gc.drawPoint(x + 1, y - 1);

			x = getBounds().x + getWidth() - 1 + rightInset;
			y = getBounds().y;
			gc.fillRectangle(x, y, w, h);
			gc.drawPoint(x, y - 1);

			gc.setForeground(getSelectionStartColor());
			gc.setBackground(getSelectionEndColor());
			x = getBounds().x + BORDER_LEFT_WIDTH - leftInset;
			y = getBounds().y - ACTIVE_Y_OFFSET;
			w = getWidth() - BORDER_LEFT_WIDTH - BORDER_RIGHT_WIDTH + 1 + rightInset + leftInset;
			h = 4;
			gc.fillGradientRectangle(x, y, w, h, true);
		}

		flasherSupport.startFlasher();

	}

	private Color getSelectionStartColor() {
		RienaDefaultLnf lnf = LnfManager.getLnf();
		if ((selStartColor == null) || selStartColor.isDisposed()) {
			Color selColor = lnf.getColor(LnfKeyConstants.SUB_APPLICATION_SWITCHER_TOP_SELECTION_COLOR);
			selStartColor = SwtUtilities.makeBrighter(selColor, 0.9f);
		}
		return selStartColor;

	}

	private Color getSelectionEndColor() {
		RienaDefaultLnf lnf = LnfManager.getLnf();
		if ((selEndColor == null) || selStartColor.isDisposed()) {
			Color selColor = lnf.getColor(LnfKeyConstants.SUB_APPLICATION_SWITCHER_TOP_SELECTION_COLOR);
			selEndColor = SwtUtilities.makeBrighter(selColor, 1.1f);
		}
		return selEndColor;
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.ILnfRenderer#dispose()
	 */
	public void dispose() {
		SwtUtilities.disposeResource(selStartColor);
		SwtUtilities.disposeResource(selEndColor);
	}

	/**
	 * Computes the size of a tab.
	 * 
	 * @param gc
	 * @param value
	 * @return size of tab
	 */
	public Point computeSize(GC gc, Object value) {

		Font font = getTabFont();
		gc.setFont(font);
		FontMetrics fontMetrics = gc.getFontMetrics();

		int width = SwtUtilities.calcTextWidth(gc, getLabel());
		width = width + BORDER_LEFT_WIDTH + BORDER_RIGHT_WIDTH + TEXT_LEFT_INSET + TEXT_RIGHT_INSET;
		// Icon
		if (getImage() != null) {
			width += getImage().getBounds().width + ICON_TEXT_GAP;
		}

		int height = fontMetrics.getHeight();
		height = height + BORDER_TOP_WIDTH + BORDER_BOTTOM_WIDTH + TEXT_TOP_INSET + TEXT_BOTTOM_INSET;
		if (isActivated()) {
			height += ACTIVE_BOTTOM_INSET;
		}

		return new Point(width, height);

	}

	/**
	 * Returns the font of the tab.
	 * 
	 * @return font
	 */
	private Font getTabFont() {
		RienaDefaultLnf lnf = LnfManager.getLnf();
		Font font = lnf.getFont(LnfKeyConstants.SUB_APPLICATION_SWITCHER_FONT);
		return font;
	}

	private int getHeight() {
		return getBounds().height - 1;
	}

	private int getWidth() {
		return getBounds().width - 1;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
		setImage(ImageStore.getInstance().getImage(icon));
	}

	private Image getImage() {
		RienaDefaultLnf lnf = LnfManager.getLnf();
		if (lnf.getBooleanSetting(LnfKeyConstants.SUB_APPLICATION_SWITCHER_TAB_SHOW_ICON)) {
			return image;
		} else {
			return null;
		}
	}

	private void setImage(Image image) {
		this.image = image;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		if (label == null) {
			label = ""; //$NON-NLS-1$
		}
		return label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the activated
	 */
	public boolean isActivated() {
		return activated;
	}

	/**
	 * @param activated
	 *            the activated to set
	 */
	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	/**
	 * This class updates (redraws) the tab, so that the marker are also updated
	 * (redrawn).
	 */
	private class MarkerUpdater implements Runnable {

		/**
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			if (control != null) {
				control.redraw();
			}
		}
	}

}
