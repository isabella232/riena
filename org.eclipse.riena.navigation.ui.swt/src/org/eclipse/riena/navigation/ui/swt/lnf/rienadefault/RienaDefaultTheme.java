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

import java.util.Map;

import org.eclipse.riena.navigation.ui.swt.lnf.ColorLnfResource;
import org.eclipse.riena.navigation.ui.swt.lnf.FontLnfResource;
import org.eclipse.riena.navigation.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.navigation.ui.swt.lnf.ILnfResource;
import org.eclipse.riena.navigation.ui.swt.lnf.ILnfTheme;
import org.eclipse.riena.navigation.ui.swt.lnf.ImageLnfResource;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

/**
 * Default theme of Riena used by the default Look and Feel,
 * <code>RienaDefaultLnf</code>.
 */
public class RienaDefaultTheme implements ILnfTheme {

	private static final String PATH_SEPARATOR = "/"; //$NON-NLS-1$
	private static final String PATH_ICONS = "org.eclipse.riena.navigation.ui.swt:" + PATH_SEPARATOR + "icons"; //$NON-NLS-1$ //$NON-NLS-2$
	private static final String IMAGE_FOLDER = PATH_ICONS + PATH_SEPARATOR + "fldr_obj.gif"; //$NON-NLS-1$
	private static final String IMAGE_FOLDER_CLOSED = PATH_ICONS + PATH_SEPARATOR + "folder_closed.gif"; //$NON-NLS-1$
	private static final String IMAGE_EMPTY_DOCUMENT = PATH_ICONS + PATH_SEPARATOR + "no_format.gif"; //$NON-NLS-1$
	private static final String IMAGE_THIN_CLOSE = PATH_ICONS + PATH_SEPARATOR + "thin_close_view.gif"; //$NON-NLS-1$
	private FontLnfResource primaryFont;
	private ColorLnfResource primaryBackground;
	private ColorLnfResource primaryForeground;

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.ILnfTheme#addCustomColors(java.util.Map)
	 */
	public void addCustomColors(Map<String, ILnfResource> table) {

		table.put(ILnfKeyConstants.EMBEDDED_TITLEBAR_FOREGROUND, getPrimaryForeground());
		table
				.put(ILnfKeyConstants.EMBEDDED_TITLEBAR_ACTIVE_BACKGROUND_START_COLOR, new ColorLnfResource(196, 225,
						244));
		table.put(ILnfKeyConstants.EMBEDDED_TITLEBAR_ACTIVE_BACKGROUND_END_COLOR, new ColorLnfResource(100, 153, 186));
		table.put(ILnfKeyConstants.EMBEDDED_TITLEBAR_ACTIVE_BORDER_COLOR, new ColorLnfResource(171, 171, 174));
		table.put(ILnfKeyConstants.EMBEDDED_TITLEBAR_PASSIVE_BACKGROUND_START_COLOR,
				new ColorLnfResource(244, 244, 245));
		table.put(ILnfKeyConstants.EMBEDDED_TITLEBAR_PASSIVE_BACKGROUND_END_COLOR, new ColorLnfResource(220, 220, 220));
		table.put(ILnfKeyConstants.EMBEDDED_TITLEBAR_PASSIVE_BORDER_COLOR, new ColorLnfResource(213, 213, 216));

		table.put(ILnfKeyConstants.EMBEDDED_TITLEBAR_HOVER_BORDER_TOP_COLOR, new ColorLnfResource(251, 233, 168));
		table.put(ILnfKeyConstants.EMBEDDED_TITLEBAR_HOVER_BORDER_BOTTOM_COLOR, new ColorLnfResource(192, 151, 1));
		table.put(ILnfKeyConstants.EMBEDDED_TITLEBAR_HOVER_BORDER_START_COLOR, new ColorLnfResource(255, 207, 32));
		table.put(ILnfKeyConstants.EMBEDDED_TITLEBAR_HOVER_BORDER_END_COLOR, new ColorLnfResource(255, 176, 1));

		table.put(ILnfKeyConstants.SUB_APPLICATION_SWITCHER_BACKGROUND, getPrimaryBackground());
		table.put(ILnfKeyConstants.SUB_MODULE_TREE_BACKGROUND, getPrimaryBackground());
		table.put(ILnfKeyConstants.MODULE_GROUP_WIDGET_BACKGROUND, getPrimaryBackground());
		table.put(ILnfKeyConstants.SUB_APPLICATION_BACKGROUND, getPrimaryBackground());

		table.put(ILnfKeyConstants.SUB_APPLICATION_SWITCHER_FOREGROUND, getPrimaryForeground());
		table.put(ILnfKeyConstants.SUB_APPLICATION_SWITCHER_TOP_SELECTION_COLOR, new ColorLnfResource(64, 132, 191));
		table.put(ILnfKeyConstants.SUB_APPLICATION_SWITCHER_ACTIVE_BACKGROUND_START_COLOR, new ColorLnfResource(255,
				255, 255));
		table.put(ILnfKeyConstants.SUB_APPLICATION_SWITCHER_ACTIVE_BACKGROUND_END_COLOR, new ColorLnfResource(255, 255,
				255));
		table.put(ILnfKeyConstants.SUB_APPLICATION_SWITCHER_PASSIVE_BACKGROUND_START_COLOR, new ColorLnfResource(245,
				245, 245));
		table.put(ILnfKeyConstants.SUB_APPLICATION_SWITCHER_PASSIVE_BACKGROUND_END_COLOR, new ColorLnfResource(229,
				229, 229));
		table.put(ILnfKeyConstants.SUB_APPLICATION_SWITCHER_INNER_BORDER_COLOR, new ColorLnfResource(245, 245, 245));
		table
				.put(ILnfKeyConstants.SUB_APPLICATION_SWITCHER_BORDER_TOP_RIGHT_COLOR, new ColorLnfResource(206, 206,
						206));
		table.put(ILnfKeyConstants.SUB_APPLICATION_SWITCHER_BORDER_BOTTOM_LEFT_COLOR, new ColorLnfResource(183, 183,
				183));

		table.put(ILnfKeyConstants.SUB_MODULE_ITEM_TOOLTIP_BACKGROUND, getPrimaryBackground());
		table.put(ILnfKeyConstants.MODULE_ITEM_TOOLTIP_BACKGROUND, getPrimaryBackground());

		table.put(ILnfKeyConstants.SUB_MODULE_TREE_ITEM_BACKGROUND, getPrimaryBackground());
		table.put(ILnfKeyConstants.SUB_MODULE_TREE_ITEM_FOREGROUND, getPrimaryForeground());

	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.ILnfTheme#addCustomFonts(java.util.Map)
	 */
	public void addCustomFonts(Map<String, ILnfResource> table) {

		table.put(ILnfKeyConstants.EMBEDDED_TITLEBAR_FONT, getPrimaryFont());
		table.put(ILnfKeyConstants.SUB_APPLICATION_SWITCHER_FONT, getPrimaryFont());
		table.put(ILnfKeyConstants.MODULE_ITEM_TOOLTIP_FONT, getPrimaryFont());

	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.ILnfTheme#addCustomImages(java.util.Map)
	 */
	public void addCustomImages(Map<String, ILnfResource> table) {

		table.put(ILnfKeyConstants.SUB_MODULE_TREE_DOCUMENT_LEAF_ICON, new ImageLnfResource(IMAGE_EMPTY_DOCUMENT));
		table.put(ILnfKeyConstants.SUB_MODULE_TREE_FOLDER_CLOSED_ICON, new ImageLnfResource(IMAGE_FOLDER_CLOSED));
		// table.put(ILnfKeyConstants."treeFolderOpen.icon",
		// getSharedImageResource(ISharedImages.IMG_OBJ_FOLDER));
		table.put(ILnfKeyConstants.SUB_MODULE_TREE_FOLDER_OPEN_ICON, new ImageLnfResource(IMAGE_FOLDER));
		table.put(ILnfKeyConstants.EMBEDDED_TITLEBAR_CLOSE_ICON, new ImageLnfResource(IMAGE_THIN_CLOSE));

	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.ILnfTheme#addCustomSettings(java.util.Map)
	 */
	public void addCustomSettings(Map<String, Object> table) {

		table.put(ILnfKeyConstants.SUB_APPLICATION_SWITCHER_HORIZONTAL_TAB_POSITION, SWT.CENTER);
		table.put(ILnfKeyConstants.SUB_APPLICATION_SWITCHER_TAB_SHOW_ICON, false);
		table.put(ILnfKeyConstants.SUB_MODULE_ITEM_TOOLTIP_POPUP_DELAY, 0);
		table.put(ILnfKeyConstants.MODULE_ITEM_TOOLTIP_POPUP_DELAY, 500);

	}

	//
	// /**
	// * Wraps and returns the image for the given name.
	// *
	// * @param symbolicName -
	// * symbolic name of the image.
	// * @return wrapper
	// */
	// protected ImageLnfResource getSharedImageResource(String symbolicName) {
	// Image image =
	// PlatformUI.getWorkbench().getSharedImages().getImage(symbolicName);
	// return new ImageLnfResource(image);
	// }

	/**
	 * Returns the data of the system font.
	 * 
	 * @return system font data
	 */
	protected FontData getSystemFont() {
		if (Display.getCurrent() != null) {
			FontData[] data = Display.getCurrent().getSystemFont().getFontData();
			if (data.length > 0) {
				return data[0];
			}
		}
		return new FontData("Arial", 10, SWT.NORMAL); //$NON-NLS-1$
	}

	/**
	 * Returns the color used for the foreground of widgets.
	 * 
	 * @return foreground color
	 */
	protected ColorLnfResource getPrimaryForeground() {
		if (primaryForeground == null) {
			primaryForeground = new ColorLnfResource(68, 70, 74);
		}
		return primaryForeground;
	}

	/**
	 * Returns the color used for the background of widgets.
	 * 
	 * @return background color
	 */
	protected ColorLnfResource getPrimaryBackground() {
		if (primaryBackground == null) {
			primaryBackground = new ColorLnfResource(255, 255, 255);
		}
		return primaryBackground;
	}

	/**
	 * Returns the font used for widgets.
	 * 
	 * @return font
	 */
	protected FontLnfResource getPrimaryFont() {
		if (primaryFont == null) {
			String name = getSystemFont().getName();
			int height = getSystemFont().getHeight() + 1;
			primaryFont = new FontLnfResource(name, height, SWT.NORMAL);
		}
		return primaryFont;
	}

}
