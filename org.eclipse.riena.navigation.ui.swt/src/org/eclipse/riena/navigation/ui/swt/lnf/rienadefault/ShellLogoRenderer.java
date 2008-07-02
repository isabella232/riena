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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

/**
 * 
 */
public class ShellLogoRenderer extends AbstractLnfRenderer {

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.AbstractLnfRenderer#paint(org.eclipse.swt.graphics.GC,
	 *      java.lang.Object)
	 */
	@Override
	public void paint(GC gc, Object value) {

		Image logo = getLogoImage();
		if (logo == null) {
			return;
		}
		int logoWidth = logo.getImageData().width;
		int logoHeight = logo.getImageData().height;
		int hMargin = getHorizontalLogoMargin();
		int vMargin = getVerticalLogoMargin();

		int x = getBounds().x;
		Integer hPos = getHorizontalLogoPosition();
		switch (hPos) {
		case SWT.CENTER:
			x = getBounds().width / 2 - logoWidth / 2;
			break;
		case SWT.RIGHT:
			x = getBounds().width - logoWidth - hMargin;
			break;
		default:
			x = hMargin;
			break;
		}
		int y = getBounds().y;
		Integer vPos = getVerticalLogoPosition();
		switch (vPos) {
		case SWT.CENTER:
			y = getBounds().height / 2 - logoHeight / 2;
			break;
		case SWT.BOTTOM:
			y = getBounds().height - logoHeight - vMargin;
			break;
		default:
			y = vMargin;
			break;
		}

		gc.drawImage(logo, x, y);

	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.ILnfRenderer#dispose()
	 */
	public void dispose() {
		// nothing to do
	}

	/**
	 * Returns the horizontal margin of the logo image.<br>
	 * Gap between the shell border an the image.
	 * 
	 * @return horizontal margin
	 */
	public static Integer getHorizontalLogoMargin() {

		Integer margin = LnfManager.getLnf().getIntegerSetting(ILnfKeyConstants.TITLELESS_SHELL_HORIZONTAL_LOGO_MARGIN);
		if (margin == null) {
			margin = 0;
		}
		return margin;

	}

	/**
	 * Returns the vertical margin of the logo image.<br>
	 * Gap between the shell border an the image.
	 * 
	 * @return horizontal margin
	 */
	private Integer getVerticalLogoMargin() {

		Integer margin = LnfManager.getLnf().getIntegerSetting(ILnfKeyConstants.TITLELESS_SHELL_VERTICAL_LOGO_MARGIN);
		if (margin == null) {
			margin = 0;
		}
		return margin;

	}

	/**
	 * Returns the horizontal position of the logo inside the shell.
	 * 
	 * @return horizontal position (SWT.LEFT, SWT.CENTER, SWT.RIGHT)
	 */
	private int getHorizontalLogoPosition() {

		Integer hPos = LnfManager.getLnf().getIntegerSetting(ILnfKeyConstants.TITLELESS_SHELL_HORIZONTAL_LOGO_POSITION);
		if (hPos == null) {
			hPos = SWT.LEFT;
		}
		return hPos;

	}

	/**
	 * Returns the vertical position of the logo inside the shell.
	 * 
	 * @return horizontal position (SWT.TOP, SWT.CENTER, SWT.BOTTOM)
	 */
	private int getVerticalLogoPosition() {

		Integer hPos = LnfManager.getLnf().getIntegerSetting(ILnfKeyConstants.TITLELESS_SHELL_VERTICAL_LOGO_POSITION);
		if (hPos == null) {
			hPos = SWT.TOP;
		}
		return hPos;

	}

	private Image getLogoImage() {
		return LnfManager.getLnf().getImage(ILnfKeyConstants.TITLELESS_SHELL_LOGO);
	}

}
