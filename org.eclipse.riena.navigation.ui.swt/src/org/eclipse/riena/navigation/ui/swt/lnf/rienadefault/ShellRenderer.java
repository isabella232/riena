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
import org.eclipse.riena.ui.swt.utils.SwtUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
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
	private final static int CLOSE_BTN_INDEX = 0;
	private final static int MAX_BTN_INDEX = 1;
	private final static int MIN_BTN_INDEX = 2;
	private final static int RESTORE_BTN_INDEX = 3;

	private boolean[] pressed = new boolean[BTN_COUNT];
	private boolean[] hover = new boolean[BTN_COUNT];
	private boolean active;
	private boolean maximized;
	private Rectangle textBounds;
	private Rectangle[] btnBounds = new Rectangle[BTN_COUNT];
	private String[] btnShowKeys = new String[] { ILnfKeyConstants.TITLELESS_SHELL_SHOW_CLOSE,
			ILnfKeyConstants.TITLELESS_SHELL_SHOW_MAX, ILnfKeyConstants.TITLELESS_SHELL_SHOW_MIN };
	private String[] btnImageKeys = new String[] { ILnfKeyConstants.TITLELESS_SHELL_CLOSE_ICON,
			ILnfKeyConstants.TITLELESS_SHELL_MAX_ICON, ILnfKeyConstants.TITLELESS_SHELL_MIN_ICON,
			ILnfKeyConstants.TITLELESS_SHELL_RESTORE_ICON };
	private String[] btnHoverSelectedImageKeys = new String[] {
			ILnfKeyConstants.TITLELESS_SHELL_CLOSE_HOVER_SELECTED_ICON,
			ILnfKeyConstants.TITLELESS_SHELL_MAX_HOVER_SELECTED_ICON,
			ILnfKeyConstants.TITLELESS_SHELL_MIN_HOVER_SELECTED_ICON,
			ILnfKeyConstants.TITLELESS_SHELL_RESTORE_HOVER_ICON };
	private String[] btnHoverImageKeys = new String[] { ILnfKeyConstants.TITLELESS_SHELL_CLOSE_HOVER_ICON,
			ILnfKeyConstants.TITLELESS_SHELL_MAX_HOVER_ICON, ILnfKeyConstants.TITLELESS_SHELL_MIN_HOVER_ICON,
			ILnfKeyConstants.TITLELESS_SHELL_RESTORE_HOVER_SELECTED_ICON };
	private String[] btnInactiveImageKeys = new String[] { ILnfKeyConstants.TITLELESS_SHELL_CLOSE_INACTIVE_ICON,
			ILnfKeyConstants.TITLELESS_SHELL_MAX_INACTIVE_ICON, ILnfKeyConstants.TITLELESS_SHELL_MIN_INACTIVE_ICON,
			ILnfKeyConstants.TITLELESS_SHELL_RESTORE_INACTIVE_ICON };

	/**
	 * Creates a new instance of <code>ShellRenderer</code> and initializes
	 * the bounds of the buttons.
	 */
	public ShellRenderer() {

		super();

		resetBounds();

	}

	/**
	 * Resets the bounds of the buttons and the text.
	 */
	private void resetBounds() {
		for (int i = 0; i < btnBounds.length; i++) {
			btnBounds[i] = new Rectangle(0, 0, 0, 0);
		}
		textBounds = new Rectangle(0, 0, 0, 0);
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
		setMaximized(shell.getMaximized());

		gc.setBackground(LnfManager.getLnf().getColor(ILnfKeyConstants.TITLELESS_SHELL_BACKGROUND));
		Image logo = getBackgroundImage();
		if (logo != null) {
			int y = logo.getImageData().height;
			int h = getBounds().height - y;
			gc.fillRectangle(0, y, getBounds().width, h);
		}

		if (LnfManager.getLnf().getBooleanSetting(ILnfKeyConstants.SHELL_HIDE_OS_BORDER)) {
			// buttons
			resetBounds();
			for (int i = 0; i < BTN_COUNT; i++) {
				paintButton(gc, i);
			}
			// paint title
			paintTitle(gc, shell);
		}

	}

	/**
	 * Sets the bounds for a button and paints it.
	 * 
	 * @param gc -
	 *            graphics context
	 * @param btnIndex -
	 *            index of the button
	 */
	private void paintButton(GC gc, int btnIndex) {

		Image image = null;

		if (LnfManager.getLnf().getBooleanSetting(btnShowKeys[btnIndex])) {
			int index = btnIndex;
			if ((index == MAX_BTN_INDEX) && isMaximized()) {
				index = RESTORE_BTN_INDEX;
			}
			if (isActive()) {
				if (isPressed(btnIndex)) {
					image = LnfManager.getLnf().getImage(btnHoverSelectedImageKeys[index]);
				} else if (isHover(btnIndex)) {
					image = LnfManager.getLnf().getImage(btnHoverImageKeys[index]);
				} else {
					image = LnfManager.getLnf().getImage(btnImageKeys[index]);
				}
			} else {
				image = LnfManager.getLnf().getImage(btnInactiveImageKeys[index]);
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

	/**
	 * @param gc -
	 *            graphics context
	 * @param shell
	 */
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
		for (int i = 0; i < btnBounds.length; i++) {
			btnHeight = Math.max(btnHeight, btnBounds[i].height);
		}
		int y = TOP_BUTTON_GAP;
		int textHeight = gc.getFontMetrics().getHeight();
		if (btnHeight > textHeight) {
			y = (btnHeight - textHeight) / 2;
		}

		int x = getBounds().x + getBounds().width;
		for (int i = 0; i < btnBounds.length; i++) {
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

		textBounds = new Rectangle(x, y, textWidth, textHeight);
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
	private boolean isPressed(int btnIndex) {
		return pressed[btnIndex];
	}

	/**
	 * @param pressed
	 *            the pressed to set
	 */
	private void setPressed(int btnIndex, boolean pressed) {
		this.pressed[btnIndex] = pressed;
	}

	/**
	 * @return the hover
	 */
	private boolean isHover(int btnIndex) {
		return hover[btnIndex];
	}

	/**
	 * @param hover
	 *            the hover to set
	 */
	private void setHover(int btnIndex, boolean hover) {
		this.hover[btnIndex] = hover;
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

	/**
	 * @return the maximized
	 */
	private boolean isMaximized() {
		return maximized;
	}

	/**
	 * @param maximiz
	 *            the maximized to set
	 */
	private void setMaximized(boolean maximized) {
		this.maximized = maximized;
	}

	/**
	 * Returns <code>true</code> if the given point is inside the bounds of
	 * the close button, and <code>false</code> otherwise.
	 * 
	 * @param pt -
	 *            the point to test
	 * @return <code>true</code> if the close button bounds contains the point
	 *         and <code>false</code> otherwise
	 */
	public boolean isInsideCloseButton(Point pt) {
		return isInsideButton(pt, CLOSE_BTN_INDEX);
	}

	/**
	 * Returns <code>true</code> if the given point is inside the bounds of
	 * the minimize button, and <code>false</code> otherwise.
	 * 
	 * @param pt -
	 *            the point to test
	 * @return <code>true</code> if the minimize button bounds contains the
	 *         point and <code>false</code> otherwise
	 */
	public boolean isInsideMinimizeButton(Point pt) {
		return isInsideButton(pt, MIN_BTN_INDEX);
	}

	/**
	 * Returns <code>true</code> if the given point is inside the bounds of
	 * the maximize/restore button, and <code>false</code> otherwise.
	 * 
	 * @param pt -
	 *            the point to test
	 * @return <code>true</code> if the maximize/restore button bounds
	 *         contains the point and <code>false</code> otherwise
	 */
	public boolean isInsideMaximizeButton(Point pt) {
		return isInsideButton(pt, MAX_BTN_INDEX);
	}

	/**
	 * Returns <code>true</code> if the given point is inside the bounds of
	 * the button, and <code>false</code> otherwise.
	 * 
	 * @param pt -
	 *            the point to test
	 * @param btnIndex -
	 *            index of button
	 * @return <code>true</code> if the button bounds contains the point and
	 *         <code>false</code> otherwise
	 */
	private boolean isInsideButton(Point pt, int btnIndex) {
		return btnBounds[btnIndex].contains(pt);
	}

	/**
	 * Returns <code>true</code> if the given point is inside the bounds of
	 * the move area, and <code>false</code> otherwise.<br>
	 * The move area is the area of the shell less the bounds of the buttons.
	 * 
	 * @param pt -
	 *            the point to test
	 * @return <code>true</code> if the move area bounds contains the point
	 *         and <code>false</code> otherwise
	 */
	public boolean isInsideMoveArea(Point pt) {
		Rectangle moveArea = new Rectangle(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
		int minX = getBounds().x + getBounds().width;
		int maxHeight = textBounds.y + textBounds.height;
		for (int i = 0; i < btnBounds.length; i++) {
			minX = Math.min(minX, btnBounds[i].x);
			maxHeight = Math.max(maxHeight, btnBounds[i].y + btnBounds[i].height);
		}
		int width = minX - getBounds().x;
		if (width < 0) {
			width = 0;
		}
		moveArea.width = width;
		return moveArea.contains(pt);
	}

	/**
	 * @return the pressed
	 */
	public boolean isCloseButtonPressed() {
		return isPressed(CLOSE_BTN_INDEX);
	}

	/**
	 * @param pressed
	 *            the pressed to set
	 */
	public void setCloseButtonPressed(boolean pressed) {
		setPressed(CLOSE_BTN_INDEX, pressed);
	}

	/**
	 * @return the hover
	 */
	public boolean isCloseButtonHover() {
		return isHover(CLOSE_BTN_INDEX);
	}

	/**
	 * @param hover
	 *            the hover to set
	 */
	public void setCloseButtonHover(boolean hover) {
		setHover(CLOSE_BTN_INDEX, hover);
	}

	/**
	 * @return the pressed
	 */
	public boolean isMaximizedButtonPressed() {
		return isPressed(MAX_BTN_INDEX);
	}

	/**
	 * @param pressed
	 *            the pressed to set
	 */
	public void setMaximizedButtonPressed(boolean pressed) {
		setPressed(MAX_BTN_INDEX, pressed);
	}

	/**
	 * @return the hover
	 */
	public boolean isMaximizedButtonHover() {
		return isHover(MAX_BTN_INDEX);
	}

	/**
	 * @param hover
	 *            the hover to set
	 */
	public void setMaximizedButtonHover(boolean hover) {
		setHover(MAX_BTN_INDEX, hover);
	}

	/**
	 * @return the pressed
	 */
	public boolean isMinimizedButtonPressed() {
		return isPressed(MIN_BTN_INDEX);
	}

	/**
	 * @param pressed
	 *            the pressed to set
	 */
	public void setMinimizedButtonPressed(boolean pressed) {
		setPressed(MIN_BTN_INDEX, pressed);
	}

	/**
	 * @return the hover
	 */
	public boolean isMinimizedButtonHover() {
		return isHover(MIN_BTN_INDEX);
	}

	/**
	 * @param hover
	 *            the hover to set
	 */
	public void setMinimizedButtonHover(boolean hover) {
		setHover(MIN_BTN_INDEX, hover);
	}

}
