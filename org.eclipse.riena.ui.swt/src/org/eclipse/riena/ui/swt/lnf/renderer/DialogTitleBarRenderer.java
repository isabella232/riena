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
package org.eclipse.riena.ui.swt.lnf.renderer;

import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Renderer of the title bar of a dialog.
 */
public class DialogTitleBarRenderer extends AbstractTitleBarRenderer {

	private final static int DEFAULT_HEIGHT = 25;
	private final static int IMAGE_TITLE_GAP = 5;
	private final static int BORDER_IMAGE_GAP = 5;
	private final static int TITLE_BUTTONS_GAP = 5;

	private String[] btnImageKeys = new String[] { ILnfKeyConstants.DIALOG_CLOSE_ICON,
			ILnfKeyConstants.DIALOG_MAX_ICON, ILnfKeyConstants.DIALOG_MIN_ICON, ILnfKeyConstants.DIALOG_RESTORE_ICON };
	private String[] btnHoverSelectedImageKeys = new String[] { ILnfKeyConstants.DIALOG_CLOSE_HOVER_SELECTED_ICON,
			ILnfKeyConstants.DIALOG_MAX_HOVER_SELECTED_ICON, ILnfKeyConstants.DIALOG_MIN_HOVER_SELECTED_ICON,
			ILnfKeyConstants.DIALOG_RESTORE_HOVER_ICON };
	private String[] btnHoverImageKeys = new String[] { ILnfKeyConstants.DIALOG_CLOSE_HOVER_ICON,
			ILnfKeyConstants.DIALOG_MAX_HOVER_ICON, ILnfKeyConstants.DIALOG_MIN_HOVER_ICON,
			ILnfKeyConstants.DIALOG_RESTORE_HOVER_SELECTED_ICON };
	private String[] btnInactiveImageKeys = new String[] { ILnfKeyConstants.DIALOG_CLOSE_INACTIVE_ICON,
			ILnfKeyConstants.DIALOG_MAX_INACTIVE_ICON, ILnfKeyConstants.DIALOG_MIN_INACTIVE_ICON,
			ILnfKeyConstants.DIALOG_RESTORE_INACTIVE_ICON };

	/**
	 * Returns the height of the title bar.
	 * 
	 * @return height of title bar
	 */
	public int getHeight() {

		int height = 0;
		if (getBounds() != null) {
			height = getBounds().height;
		}
		if (height <= 0) {
			height = DEFAULT_HEIGHT;
		}

		return height;

	}

	@Override
	protected boolean[] getBtnShow() {

		boolean[] btnShow = new boolean[BTN_COUNT];

		// TODO
		btnShow[CLOSE_BTN_INDEX] = true;
		btnShow[MAX_BTN_INDEX] = true;
		btnShow[MIN_BTN_INDEX] = true;

		return btnShow;

	}

	@Override
	protected String[] getBtnHoverImageKeys() {
		return btnHoverImageKeys;
	}

	@Override
	protected String[] getBtnHoverSelectedImageKeys() {
		return btnHoverSelectedImageKeys;
	}

	@Override
	protected String[] getBtnImageKeys() {
		return btnImageKeys;
	}

	@Override
	protected String[] getBtnInactiveImageKeys() {
		return btnInactiveImageKeys;
	}

	@Override
	protected void paintBackground(GC gc) {

		// TODO
		gc.setForeground(LnfManager.getLnf().getColor(ILnfKeyConstants.DIALOG_TITLEBAR_BACKGROUND_START_COLOR));
		gc.setBackground(LnfManager.getLnf().getColor(ILnfKeyConstants.DIALOG_TITLEBAR_BACKGROUND_END_COLOR));
		gc.fillGradientRectangle(0, 0, getBounds().width, getBounds().height, true);

	}

	@Override
	protected Rectangle paintTitle(GC gc) {

		if (!LnfManager.getLnf().getBooleanSetting(ILnfKeyConstants.DIALOG_HIDE_OS_BORDER)) {
			return new Rectangle(0, 0, 0, 0);
		}

		String title = getShell().getText();
		if (StringUtils.isEmpty(title)) {
			return new Rectangle(0, 0, 0, 0);
		}

		Color fgColor = LnfManager.getLnf().getColor(ILnfKeyConstants.DIALOG_FOREGROUND);
		if (!isActive()) {
			fgColor = LnfManager.getLnf().getColor(ILnfKeyConstants.DIALOG_PASSIVE_FOREGROUND);
		}
		gc.setForeground(fgColor);

		Font font = LnfManager.getLnf().getFont(ILnfKeyConstants.DIALOG_FONT);
		gc.setFont(font);

		int btnX = getBounds().width;
		int btnHeight = 0;
		for (int i = 0; i < getButtonsBounds().length; i++) {
			btnHeight = Math.max(btnHeight, getButtonsBounds()[i].height);
			btnX = Math.min(btnX, getButtonsBounds()[i].x);
		}
		btnX -= TITLE_BUTTONS_GAP;
		int y = TOP_BUTTON_GAP;
		int textHeight = gc.getFontMetrics().getHeight();
		if (btnHeight > textHeight) {
			y = (btnHeight - textHeight) / 2;
		}

		int x = getImageBounds().x + getImageBounds().width + IMAGE_TITLE_GAP;
		int textWidth = SwtUtilities.calcTextWidth(gc, title);
		if (textWidth + x > btnX) {
			textWidth = btnX - x;
			title = SwtUtilities.clipText(gc, title, textWidth);
		}

		gc.drawText(title, x, y, true);

		return new Rectangle(x, y, textWidth, textHeight);

	}

	@Override
	protected Rectangle paintImage(GC gc) {

		if (!LnfManager.getLnf().getBooleanSetting(ILnfKeyConstants.DIALOG_HIDE_OS_BORDER)) {
			return new Rectangle(0, 0, 0, 0);
		}

		Image image = getShell().getImage();
		if (image == null) {
			return new Rectangle(0, 0, 0, 0);
		}

		int x = BORDER_IMAGE_GAP;
		int imageWidth = image.getImageData().width;
		int btnHeight = 0;
		for (int i = 0; i < getButtonsBounds().length; i++) {
			btnHeight = Math.max(btnHeight, getButtonsBounds()[i].height);
		}
		int y = TOP_BUTTON_GAP;
		int imageHeight = image.getImageData().height;
		if (btnHeight > imageHeight) {
			y = (btnHeight - imageHeight) / 2;
		}

		gc.drawImage(image, x, y);

		return new Rectangle(x, y, imageWidth, imageHeight);

	}

}
