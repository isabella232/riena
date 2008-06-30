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
import org.eclipse.riena.navigation.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.navigation.ui.swt.lnf.LnfManager;
import org.eclipse.riena.navigation.ui.swt.utils.SwtUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

/**
 * Renderer of the buttons (min.,max. and close) and the title of the shell.
 */
public class ShellRenderer extends AbstractLnfRenderer {

	private final static int TOP_BUTTON_GAP = 2;
	private final static int BUTTON_RIGHT_GAP = 2;
	private final static int BUTTON_BUTTON_GAP = 2;
	/**
	 * Margin between left shell border and title or title and button.
	 */
	private final static int TITLE_MARGIN = 5;
	private final static int BTN_COUNT = 3;

	private boolean pressed;
	private boolean hover;
	private boolean active;
	private Rectangle[] btnBounds = new Rectangle[BTN_COUNT];
	private String[] btnShowKeys = new String[] { ILnfKeyConstants.TITLELESS_SHELL_SHOW_CLOSE,
			ILnfKeyConstants.TITLELESS_SHELL_SHOW_MAX, ILnfKeyConstants.TITLELESS_SHELL_SHOW_MIN };
	private String[] btnImageKeys = new String[] { ILnfKeyConstants.TITLELESS_SHELL_CLOSE_ICON,
			ILnfKeyConstants.TITLELESS_SHELL_MAX_ICON, ILnfKeyConstants.TITLELESS_SHELL_MIN_ICON };
	private String[] btnHoverSelectedImageKeys = new String[] {
			ILnfKeyConstants.TITLELESS_SHELL_CLOSE_HOVER_SELECTED_ICON,
			ILnfKeyConstants.TITLELESS_SHELL_MAX_HOVER_SELECTED_ICON,
			ILnfKeyConstants.TITLELESS_SHELL_MIN_HOVER_SELECTED_ICON };
	private String[] btnHoverImageKeys = new String[] { ILnfKeyConstants.TITLELESS_SHELL_CLOSE_HOVER_ICON,
			ILnfKeyConstants.TITLELESS_SHELL_MAX_HOVER_ICON, ILnfKeyConstants.TITLELESS_SHELL_MIN_HOVER_ICON };
	private String[] btnInactiveImageKeys = new String[] { ILnfKeyConstants.TITLELESS_SHELL_CLOSE_INACTIVE_ICON,
			ILnfKeyConstants.TITLELESS_SHELL_MAX_INACTIVE_ICON, ILnfKeyConstants.TITLELESS_SHELL_MIN_INACTIVE_ICON };

	/**
	 * Creates a new instance of <code>ShellRenderer</code> and initializes
	 * the bounds of the buttons.
	 */
	public ShellRenderer() {

		super();

		resetButtonBounds();

	}

	private void resetButtonBounds() {
		for (int i = 0; i < BTN_COUNT; i++) {
			btnBounds[i] = new Rectangle(0, 0, 0, 0);
		}
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.AbstractLnfRenderer#paint(org.eclipse.swt.graphics.GC,
	 *      java.lang.Object)
	 */
	@Override
	public void paint(GC gc, Object value) {

		assert value != null;
		assert value instanceof Shell;
		Shell shell = (Shell) value;
		setActive(shell == shell.getDisplay().getActiveShell());

		gc.setBackground(LnfManager.getLnf().getColor(ILnfKeyConstants.TITLELESS_SHELL_BACKGROUND));
		Image logo = getBackgroundImage();
		if (logo != null) {
			int y = logo.getImageData().height;
			int h = getBounds().height - y;
			gc.fillRectangle(0, y, getBounds().width, h);
		}

		// buttons
		resetButtonBounds();
		for (int i = 0; i < BTN_COUNT; i++) {
			paintButton(gc, i);
		}

		// paint title
		paintTitle(gc, shell);

	}

	private void paintButton(GC gc, int btnIndex) {

		Image image = null;

		if (LnfManager.getLnf().getBooleanSetting(btnShowKeys[btnIndex])) {
			if (isActive()) {
				if (isPressed()) {
					image = LnfManager.getLnf().getImage(btnHoverSelectedImageKeys[btnIndex]);
				} else if (isHover()) {
					image = LnfManager.getLnf().getImage(btnHoverImageKeys[btnIndex]);
				} else {
					image = LnfManager.getLnf().getImage(btnImageKeys[btnIndex]);
				}
			} else {
				image = LnfManager.getLnf().getImage(btnInactiveImageKeys[btnIndex]);
			}
		}

		int x = getBounds().x + getBounds().width - BUTTON_RIGHT_GAP;
		if (btnIndex > 0) {
			x = btnBounds[btnIndex - 1].x;
			if (btnBounds[btnIndex - 1].width > 0) {
				x -= BUTTON_BUTTON_GAP;
			}
		}
		int y = getBounds().y + TOP_BUTTON_GAP;
		if (image != null) {
			x -= image.getImageData().width;
			gc.drawImage(image, x, y);
			btnBounds[btnIndex].width = image.getImageData().width;
			btnBounds[btnIndex].height = image.getImageData().height;
		}
		btnBounds[btnIndex].x = x;
		btnBounds[btnIndex].y = y;

	}

	private void paintTitle(GC gc, Shell shell) {

		String title = shell.getText();
		if (StringUtils.isEmpty(title)) {
			return;
		}

		Color fgColor = LnfManager.getLnf().getColor(ILnfKeyConstants.TITLELESS_SHELL_FOREGROUND);
		if (!isActive()) {
			fgColor = LnfManager.getLnf().getColor(ILnfKeyConstants.TITLELESS_SHELL_PASSIVE_FOREGROUND);
		}
		gc.setForeground(fgColor);

		Font font = LnfManager.getLnf().getFont(ILnfKeyConstants.TITLELESS_SHELL_FONT);
		gc.setFont(font);

		int btnHeight = 0;
		for (int i = 0; i < BTN_COUNT; i++) {
			btnHeight = Math.max(btnHeight, btnBounds[i].height);
		}
		int y = TOP_BUTTON_GAP;
		if (btnHeight > gc.getFontMetrics().getHeight()) {
			y = (btnHeight - gc.getFontMetrics().getHeight()) / 2;
		}

		int x = getBounds().x + getBounds().width;
		for (int i = 0; i < BTN_COUNT; i++) {
			if (btnBounds[i].x > 0) {
				x = Math.min(x, btnBounds[i].x);
			}
		}
		int textWidth = SwtUtilities.calcTextWidth(gc, shell.getText());
		switch (getHorizontalLogoPosition()) {
		case SWT.LEFT:
			x = TITLE_MARGIN;
			break;
		case SWT.CENTER:
			x = getBounds().width / 2 - textWidth / 2;
			break;
		default: // SWT.RIGHT
			x -= TITLE_MARGIN;
			x -= textWidth;
			break;
		}

		gc.drawText(shell.getText(), x, y, true);

	}

	/**
	 * Returns the horizontal position of the text inside the shell.
	 * 
	 * @return horizontal position (SWT.LEFT, SWT.CENTER, SWT.RIGHT)
	 */
	private int getHorizontalLogoPosition() {

		Integer hPos = LnfManager.getLnf().getIntegerSetting(ILnfKeyConstants.TITLELESS_SHELL_HORIZONTAL_TEXT_POSITION);
		if (hPos == null) {
			hPos = SWT.LEFT;
		}
		return hPos;

	}

	private Image getBackgroundImage() {
		return LnfManager.getLnf().getImage(ILnfKeyConstants.TITLELESS_SHELL_BACKGROUND_IMAGE);
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.ILnfRenderer#dispose()
	 */
	public void dispose() {
		// nothing to do
	}

	/**
	 * @return the pressed
	 */
	public boolean isPressed() {
		return pressed;
	}

	/**
	 * @param pressed
	 *            the pressed to set
	 */
	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}

	/**
	 * @return the hover
	 */
	public boolean isHover() {
		return hover;
	}

	/**
	 * @param hover
	 *            the hover to set
	 */
	public void setHover(boolean hover) {
		this.hover = hover;
	}

	/**
	 * @return the active
	 */
	private boolean isActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	private void setActive(boolean active) {
		this.active = active;
	}

}
