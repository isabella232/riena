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
package org.eclipse.riena.example.client.application;

import java.util.Map;

import org.eclipse.riena.ui.swt.lnf.ColorLnfResource;
import org.eclipse.riena.ui.swt.lnf.FontLnfResource;
import org.eclipse.riena.ui.swt.lnf.ILnfResource;
import org.eclipse.riena.ui.swt.lnf.ImageLnfResource;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultTheme;
import org.eclipse.swt.SWT;

/**
 * Theme of the Look and Feel (Lnf) of the example application.
 */
public class ExampleTheme extends RienaDefaultTheme {

	/**
	 * @see org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultTheme#addCustomColors(java.util.Map)
	 */
	@Override
	public void addCustomColors(Map<String, ILnfResource> table) {
		super.addCustomColors(table);

		table.put(LnfKeyConstants.TITLELESS_SHELL_FOREGROUND, getPrimaryForeground());

		table.put(LnfKeyConstants.MODULE_ITEM_TOOLTIP_FOREGROUND, new ColorLnfResource(64, 0, 64));

		table.put(LnfKeyConstants.SUB_MODULE_ITEM_TOOLTIP_FOREGROUND, new ColorLnfResource(64, 0, 64));

		table.put(LnfKeyConstants.EMBEDDED_TITLEBAR_ACTIVE_BACKGROUND_START_COLOR, new ColorLnfResource(234, 231, 158));
		table.put(LnfKeyConstants.EMBEDDED_TITLEBAR_ACTIVE_BACKGROUND_END_COLOR, new ColorLnfResource(225, 220, 114));
		table.put(LnfKeyConstants.EMBEDDED_TITLEBAR_ACTIVE_BORDER_COLOR, new ColorLnfResource(171, 171, 174));
		table
				.put(LnfKeyConstants.EMBEDDED_TITLEBAR_PASSIVE_BACKGROUND_START_COLOR, new ColorLnfResource(222, 224,
						240));
		table.put(LnfKeyConstants.EMBEDDED_TITLEBAR_PASSIVE_BACKGROUND_END_COLOR, new ColorLnfResource(186, 193, 225));
		table.put(LnfKeyConstants.EMBEDDED_TITLEBAR_PASSIVE_BORDER_COLOR, new ColorLnfResource(151, 150, 180));

		table.put(LnfKeyConstants.SUB_MODULE_TREE_ITEM_SELECTION_COLOR, new ColorLnfResource(null));
		table.put(LnfKeyConstants.SUB_MODULE_TREE_ITEM_FOCUS_COLOR, new ColorLnfResource(null));

		table.put("Text.background", new ColorLnfResource(255, 255, 235)); //$NON-NLS-1$

	}

	/**
	 * @see org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultTheme#addCustomImages(java.util.Map)
	 */
	@Override
	public void addCustomImages(Map<String, ILnfResource> table) {

		super.addCustomImages(table);

		String imagePath = "exampleBackground.png"; //$NON-NLS-1$ 
		table.put(LnfKeyConstants.TITLELESS_SHELL_BACKGROUND_IMAGE, new ImageLnfResource(imagePath));
		imagePath = "exampleRIENA_Logo_RGB.png"; //$NON-NLS-1$ 
		table.put(LnfKeyConstants.TITLELESS_SHELL_LOGO, new ImageLnfResource(imagePath));

		imagePath = "ledred.png"; //$NON-NLS-1$ 
		table.put(LnfKeyConstants.SUB_MODULE_TREE_DOCUMENT_LEAF_ICON, new ImageLnfResource(imagePath));
		imagePath = "folder_favorite.png"; //$NON-NLS-1$ 
		table.put(LnfKeyConstants.SUB_MODULE_TREE_FOLDER_CLOSED_ICON, new ImageLnfResource(imagePath));
		table.put(LnfKeyConstants.SUB_MODULE_TREE_FOLDER_OPEN_ICON, new ImageLnfResource(imagePath));

	}

	/**
	 * @see org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultTheme#addCustomFonts(java.util.Map)
	 */
	@Override
	public void addCustomFonts(Map<String, ILnfResource> table) {

		super.addCustomFonts(table);

		table.put(LnfKeyConstants.TITLELESS_SHELL_FONT, new FontLnfResource("Arial", 12, SWT.BOLD)); //$NON-NLS-1$

		table.put(LnfKeyConstants.DIALOG_FONT, new FontLnfResource("Arial", 11, SWT.BOLD)); //$NON-NLS-1$

		table.put(LnfKeyConstants.SUB_APPLICATION_SWITCHER_FONT, new FontLnfResource("Arial", 11, SWT.BOLD)); //$NON-NLS-1$

		table.put(LnfKeyConstants.MODULE_ITEM_TOOLTIP_FONT, new FontLnfResource("Arial", 11, SWT.BOLD)); //$NON-NLS-1$

		table.put(LnfKeyConstants.SUB_MODULE_ITEM_FONT, new FontLnfResource("Arial", 11, SWT.BOLD)); //$NON-NLS-1$

		table.put(LnfKeyConstants.EMBEDDED_TITLEBAR_FONT, new FontLnfResource("Arial", 11, SWT.BOLD)); //$NON-NLS-1$

	}

	/**
	 * @see org.eclipse.riena.ui.swt.lnf.ILnfTheme#addCustomSettings(java.util.Map)
	 */
	@Override
	public void addCustomSettings(Map<String, Object> table) {

		super.addCustomSettings(table);

		table.put(LnfKeyConstants.TITLELESS_SHELL_VERTICAL_LOGO_POSITION, SWT.TOP);
		table.put(LnfKeyConstants.TITLELESS_SHELL_VERTICAL_LOGO_MARGIN, 7);
		table.put(LnfKeyConstants.TITLELESS_SHELL_HORIZONTAL_LOGO_POSITION, SWT.CENTER);

		table.put(LnfKeyConstants.SUB_APPLICATION_SWITCHER_HORIZONTAL_TAB_POSITION, SWT.LEFT);
		table.put(LnfKeyConstants.SUB_APPLICATION_SWITCHER_TAB_SHOW_ICON, true);

	}

	@Override
	protected ColorLnfResource getPrimaryForeground() {
		return new ColorLnfResource(64, 0, 64);
	}

	@Override
	protected ColorLnfResource getPrimaryBackground() {
		return new ColorLnfResource(255, 255, 215);
	}

	@Override
	protected FontLnfResource getPrimaryFont() {
		return new FontLnfResource("Arial", 9, SWT.NORMAL); //$NON-NLS-1$
	}

	@Override
	protected boolean hideOsBorder() {
		return false;
	}

}
