/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.ui.swt.lnf.rienadefault;

import java.util.Map;

import org.eclipse.riena.navigation.ui.swt.lnf.ColorLnfResource;
import org.eclipse.riena.navigation.ui.swt.lnf.FontLnfResource;
import org.eclipse.riena.navigation.ui.swt.lnf.ILnfResource;
import org.eclipse.riena.navigation.ui.swt.lnf.ILnfTheme;
import org.eclipse.riena.navigation.ui.swt.lnf.ImageLnfResource;
import org.eclipse.riena.navigation.ui.swt.utils.ImageUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * Default theme of Riena used by the default Look and Feel,
 * <code>RienaDefaultLnf</code>.
 */
public class RienaDefaultTheme implements ILnfTheme {

	private static final String PATH_SEPARATOR = "/"; //$NON-NLS-1$
	private static final String PATH_ICONS = "org.eclipse.riena.navigation.ui.swt:" + PATH_SEPARATOR + "icons"; //$NON-NLS-1$ //$NON-NLS-2$
	private static final String IMAGE_FOLDER_CLOSED = PATH_ICONS + PATH_SEPARATOR + "folder_closed.gif"; //$NON-NLS-1$
	private static final String IMAGE_EMPTY_DOCUMENT = PATH_ICONS + PATH_SEPARATOR + "no_format.gif"; //$NON-NLS-1$
	private static final String IMAGE_THIN_CLOSE = PATH_ICONS + PATH_SEPARATOR + "thin_close_view.gif"; //$NON-NLS-1$

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.ILnfTheme#addCustomColors(java.util.Map)
	 */
	public void addCustomColors(Map<String, ILnfResource> table) {

		table.put("EmbeddedTitlebar.foreground", new ColorLnfResource(68, 70, 74)); //$NON-NLS-1$
		table.put("EmbeddedTitlebar.activeBackgroundStartColor", new ColorLnfResource(196, 225, 244)); //$NON-NLS-1$
		table.put("EmbeddedTitlebar.activeBackgroundEndColor", new ColorLnfResource(100, 153, 186)); //$NON-NLS-1$
		table.put("EmbeddedTitlebar.activeBorderColor", new ColorLnfResource(171, 171, 174)); //$NON-NLS-1$
		table.put("EmbeddedTitlebar.passiveBackgroundStartColor", new ColorLnfResource(244, 244, 245)); //$NON-NLS-1$
		table.put("EmbeddedTitlebar.passiveBackgroundEndColor", new ColorLnfResource(220, 220, 220)); //$NON-NLS-1$
		table.put("EmbeddedTitlebar.passiveBorderColor", new ColorLnfResource(213, 213, 216)); //$NON-NLS-1$

		table.put("EmbeddedTitlebar.hoverBorderTopColor", new ColorLnfResource(251, 233, 168)); //$NON-NLS-1$
		table.put("EmbeddedTitlebar.hoverBorderBottomColor", new ColorLnfResource(192, 151, 1)); //$NON-NLS-1$
		table.put("EmbeddedTitlebar.hoverBorderStartColor", new ColorLnfResource(255, 207, 32)); //$NON-NLS-1$
		table.put("EmbeddedTitlebar.hoverBorderEndColor", new ColorLnfResource(255, 176, 1)); //$NON-NLS-1$

	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.ILnfTheme#addCustomFonts(java.util.Map)
	 */
	public void addCustomFonts(Map<String, ILnfResource> table) {

		table.put("EmbeddedTitlebar.font", new FontLnfResource(getSystemFont().getName(), 10, SWT.NORMAL)); //$NON-NLS-1$

	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.ILnfTheme#addCustomImages(java.util.Map)
	 */
	public void addCustomImages(Map<String, ILnfResource> table) {

		table.put("treeDocumentLeaf.icon", getImageResource(IMAGE_EMPTY_DOCUMENT)); //$NON-NLS-1$
		table.put("treeFolderClosed.icon", getImageResource(IMAGE_FOLDER_CLOSED)); //$NON-NLS-1$
		table.put("treeFolderOpen.icon", getSharedImageResource(ISharedImages.IMG_OBJ_FOLDER)); //$NON-NLS-1$
		table.put("EmbeddedTitlebar.close", getImageResource(IMAGE_THIN_CLOSE)); //$NON-NLS-1$

	}

	/**
	 * Loads the image and returns a wrapper for it.
	 * 
	 * @param imagePath -
	 *            path of the image to load.
	 * @return wrapper
	 */
	protected ImageLnfResource getImageResource(String imagePath) {
		return getImageResource(ImageUtil.getImage(imagePath));
	}

	/**
	 * Returns a wrapper for the given image.
	 * 
	 * @param image -
	 *            image to wrap.
	 * @return wrapper
	 */
	protected ImageLnfResource getImageResource(Image image) {
		return new ImageLnfResource(image);
	}

	/**
	 * Wraps and returns the image for the given name.
	 * 
	 * @param symbolicName -
	 *            symbolic name of the image.
	 * @return wrapper
	 */
	protected ImageLnfResource getSharedImageResource(String symbolicName) {
		Image image = PlatformUI.getWorkbench().getSharedImages().getImage(symbolicName);
		return new ImageLnfResource(image);
	}

	/**
	 * Retruns the data of the system font.
	 * 
	 * @return system font data
	 */
	private FontData getSystemFont() {
		FontData[] data = Display.getCurrent().getSystemFont().getFontData();
		if (data.length > 0) {
			return data[0];
		}
		return new FontData("Arial", 10, SWT.NORMAL); //$NON-NLS-1$
	}

}
