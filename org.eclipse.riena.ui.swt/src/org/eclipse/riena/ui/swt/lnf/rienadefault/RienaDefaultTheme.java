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
package org.eclipse.riena.ui.swt.lnf.rienadefault;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

import org.eclipse.riena.ui.swt.lnf.ColorLnfResource;
import org.eclipse.riena.ui.swt.lnf.FontLnfResource;
import org.eclipse.riena.ui.swt.lnf.ILnfResource;
import org.eclipse.riena.ui.swt.lnf.ILnfTheme;
import org.eclipse.riena.ui.swt.lnf.ImageLnfResource;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;

/**
 * Default theme of Riena used by the default Look and Feel,
 * <code>RienaDefaultLnf</code>.
 */
public class RienaDefaultTheme implements ILnfTheme {

	private static final String IMAGE_FOLDER = "node_expanded.gif"; //$NON-NLS-1$
	private static final String IMAGE_FOLDER_CLOSED = "node_collapsed.gif"; //$NON-NLS-1$
	private static final String IMAGE_EMPTY_DOCUMENT = "no_format.gif"; //$NON-NLS-1$
	private static final String IMAGE_CLOSE_MODULE = "closeModule"; //$NON-NLS-1$
	private static final String IMAGE_CLOSE_MODULE_HOVER = "closeModule_hover"; //$NON-NLS-1$
	private static final String IMAGE_CLOSE_MODULE_HOVER_SELECTED = "closeModule_hover_selected"; //$NON-NLS-1$
	private static final String IMAGE_CLOSE_MODULE_INACTIVE = "closeModule_inactive"; //$NON-NLS-1$
	private static final String IMAGE_BACKGROUND = "background"; //$NON-NLS-1$
	private static final String IMAGE_LOGO = "RIENA_Logo_RGB"; //$NON-NLS-1$
	private static final String IMAGE_CLOSE = "mb_close.gif"; //$NON-NLS-1$
	private static final String IMAGE_CLOSE_HOVER = "mb_close_hover.gif"; //$NON-NLS-1$
	private static final String IMAGE_CLOSE_HOVER_SELECTED = "mb_close_hover_selected.gif"; //$NON-NLS-1$
	private static final String IMAGE_CLOSE_INACTIVE = "mb_close_inactive.gif"; //$NON-NLS-1$
	private static final String IMAGE_MAX = "mb_max.gif"; //$NON-NLS-1$
	private static final String IMAGE_MAX_HOVER = "mb_max_hover.gif"; //$NON-NLS-1$
	private static final String IMAGE_MAX_HOVER_SELECTED = "mb_max_hover_selected.gif"; //$NON-NLS-1$
	private static final String IMAGE_MAX_INACTIVE = "mb_max_inactive.gif"; //$NON-NLS-1$
	private static final String IMAGE_MIN = "mb_min.gif"; //$NON-NLS-1$
	private static final String IMAGE_MIN_HOVER = "mb_min_hover.gif"; //$NON-NLS-1$
	private static final String IMAGE_MIN_HOVER_SELECTED = "mb_min_hover_selected.gif"; //$NON-NLS-1$
	private static final String IMAGE_MIN_INACTIVE = "mb_min_inactive.gif"; //$NON-NLS-1$
	private static final String IMAGE_RESTORE = "mb_restore.gif"; //$NON-NLS-1$
	private static final String IMAGE_RESTORE_HOVER = "mb_restore_hover.gif"; //$NON-NLS-1$
	private static final String IMAGE_RESTORE_HOVER_SELECTED = "mb_restore_hover_selected.gif"; //$NON-NLS-1$
	private static final String IMAGE_RESTORE_INACTIVE_ICON = "mb_restore_inactive.gif"; //$NON-NLS-1$
	private static final String IMAGE_HAND = "hand"; //$NON-NLS-1$
	private static final String IMAGE_GRAB = "grab"; //$NON-NLS-1$
	private static final String IMAGE_GRAB_CORNER = "grabCorner"; //$NON-NLS-1$
	private static final String IMAGE_SPACER = "spacer"; //$NON-NLS-1$
	private static final String IMAGE_ERROR = "statusline_error.gif"; //$NON-NLS-1$
	private static final String IMAGE_WARNING = "statusline_warning.gif"; //$NON-NLS-1$
	private static final String IMAGE_INFO = "statusline_info.gif"; //$NON-NLS-1$
	private static final String IMAGE_ERROR_MARKER = "errorMarker"; //$NON-NLS-1$
	private static final String IMAGE_MANDATORY_MARKER = "mandatoryMarker"; //$NON-NLS-1$
	private static final String IMAGE_PROCESS_FINISHED_MARKER = "processFinishedMarker"; //$NON-NLS-1$
	private static final String IMAGE_SCROLL_UP = "arrowUp.gif"; //$NON-NLS-1$
	private static final String IMAGE_SCROLL_DOWN = "arrowDown.gif"; //$NON-NLS-1$
	private static final String IMAGE_DEFAULT_NODE_ICON = "defaultNode.png"; //$NON-NLS-1$
	private FontLnfResource primaryFont;
	private ColorLnfResource primaryBackground;
	private ColorLnfResource primaryForeground;

	/**
	 * @see org.eclipse.riena.ui.swt.lnf.ILnfTheme#addCustomColors(java.util.Map)
	 */
	public void addCustomColors(Map<String, ILnfResource> table) {

		table.put(LnfKeyConstants.TITLELESS_SHELL_FOREGROUND, new ColorLnfResource(255, 255, 255));
		table.put(LnfKeyConstants.TITLELESS_SHELL_PASSIVE_FOREGROUND, new ColorLnfResource(128, 128, 128));
		table.put(LnfKeyConstants.TITLELESS_SHELL_BACKGROUND, getPrimaryBackground());
		table.put(LnfKeyConstants.TITLELESS_SHELL_BORDER_BOTTOM_RIGHT_COLOR, new ColorLnfResource(83, 85, 94));
		table.put(LnfKeyConstants.TITLELESS_SHELL_BORDER_TOP_LEFT_COLOR, new ColorLnfResource(121, 124, 137));
		table.put(LnfKeyConstants.TITLELESS_SHELL_INNER_BORDER_TOP_LEFT_COLOR, new ColorLnfResource(173, 180, 205));
		table.put(LnfKeyConstants.TITLELESS_SHELL_INNER_BORDER_BOTTOM_RIGHT_COLOR, new ColorLnfResource(161, 168, 190));

		table.put(LnfKeyConstants.COOLBAR_BACKGROUND, getPrimaryBackground());

		table.put(LnfKeyConstants.DIALOG_FOREGROUND, new ColorLnfResource(255, 255, 255));
		table.put(LnfKeyConstants.DIALOG_PASSIVE_FOREGROUND, new ColorLnfResource(128, 128, 128));
		table.put(LnfKeyConstants.DIALOG_BORDER_BOTTOM_RIGHT_COLOR, new ColorLnfResource(83, 85, 94));
		table.put(LnfKeyConstants.DIALOG_BORDER_TOP_LEFT_COLOR, new ColorLnfResource(121, 124, 137));
		table.put(LnfKeyConstants.DIALOG_INNER_BORDER_TOP_LEFT_COLOR, new ColorLnfResource(173, 180, 205));
		table.put(LnfKeyConstants.DIALOG_INNER_BORDER_BOTTOM_RIGHT_COLOR, new ColorLnfResource(161, 168, 190));
		table.put(LnfKeyConstants.DIALOG_TITLEBAR_BACKGROUND_START_COLOR, new ColorLnfResource(161, 176, 218));
		table.put(LnfKeyConstants.DIALOG_TITLEBAR_BACKGROUND_END_COLOR, new ColorLnfResource(124, 153, 205));
		table.put(LnfKeyConstants.DIALOG_TITLEBAR_BACKGROUND_TOP_COLOR_1, new ColorLnfResource(188, 201, 229));
		table.put(LnfKeyConstants.DIALOG_TITLEBAR_BACKGROUND_TOP_COLOR_2, new ColorLnfResource(158, 178, 218));
		table.put(LnfKeyConstants.DIALOG_TITLEBAR_BACKGROUND_TOP_COLOR_3, new ColorLnfResource(139, 163, 210));
		table.put(LnfKeyConstants.DIALOG_TITLEBAR_BACKGROUND_BOTTOM_COLOR_1, new ColorLnfResource(99, 126, 175));
		table.put(LnfKeyConstants.DIALOG_TITLEBAR_BACKGROUND_BOTTOM_COLOR_2, new ColorLnfResource(139, 163, 210));
		table.put(LnfKeyConstants.DIALOG_TITLEBAR_BACKGROUND_BOTTOM_COLOR_3, new ColorLnfResource(164, 183, 220));

		table.put(LnfKeyConstants.EMBEDDED_TITLEBAR_DISABLED_FOREGROUND, new ColorLnfResource(170, 170, 170));
		table.put(LnfKeyConstants.EMBEDDED_TITLEBAR_DISABLED_BORDER_COLOR, new ColorLnfResource(233, 233, 238));
		table.put(LnfKeyConstants.EMBEDDED_TITLEBAR_ACTIVE_FOREGROUND, getPrimaryForeground());
		table.put(LnfKeyConstants.EMBEDDED_TITLEBAR_ACTIVE_BACKGROUND_START_COLOR, new ColorLnfResource(196, 225, 244));
		table.put(LnfKeyConstants.EMBEDDED_TITLEBAR_ACTIVE_BACKGROUND_END_COLOR, new ColorLnfResource(100, 153, 186));
		table.put(LnfKeyConstants.EMBEDDED_TITLEBAR_ACTIVE_BORDER_COLOR, new ColorLnfResource(171, 171, 174));
		table.put(LnfKeyConstants.EMBEDDED_TITLEBAR_PASSIVE_FOREGROUND, getPrimaryForeground());
		table
				.put(LnfKeyConstants.EMBEDDED_TITLEBAR_PASSIVE_BACKGROUND_START_COLOR, new ColorLnfResource(244, 244,
						245));
		table.put(LnfKeyConstants.EMBEDDED_TITLEBAR_PASSIVE_BACKGROUND_END_COLOR, new ColorLnfResource(220, 220, 220));
		table.put(LnfKeyConstants.EMBEDDED_TITLEBAR_PASSIVE_BORDER_COLOR, new ColorLnfResource(213, 213, 216));

		table.put(LnfKeyConstants.EMBEDDED_TITLEBAR_HOVER_BORDER_TOP_COLOR, new ColorLnfResource(251, 233, 168));
		table.put(LnfKeyConstants.EMBEDDED_TITLEBAR_HOVER_BORDER_BOTTOM_COLOR, new ColorLnfResource(192, 151, 1));
		table.put(LnfKeyConstants.EMBEDDED_TITLEBAR_HOVER_BORDER_START_COLOR, new ColorLnfResource(255, 207, 32));
		table.put(LnfKeyConstants.EMBEDDED_TITLEBAR_HOVER_BORDER_END_COLOR, new ColorLnfResource(255, 176, 1));

		table.put(LnfKeyConstants.NAVIGATION_BACKGROUND, getPrimaryBackground());
		table.put(LnfKeyConstants.MODULE_GROUP_WIDGET_BACKGROUND, getPrimaryBackground());
		table.put(LnfKeyConstants.SUB_MODULE_TREE_BACKGROUND, getPrimaryBackground());
		table.put(LnfKeyConstants.SUB_MODULE_BACKGROUND, getPrimaryBackground());

		table.put(LnfKeyConstants.SUB_APPLICATION_SWITCHER_ACTIVE_FOREGROUND, getPrimaryForeground());
		table.put(LnfKeyConstants.SUB_APPLICATION_SWITCHER_PASSIVE_FOREGROUND, getPrimaryForeground());
		table.put(LnfKeyConstants.SUB_APPLICATION_SWITCHER_DISABLED_FOREGROUND, new ColorLnfResource(170, 170, 170));
		table.put(LnfKeyConstants.SUB_APPLICATION_SWITCHER_TOP_SELECTION_COLOR, new ColorLnfResource(64, 132, 191));
		table.put(LnfKeyConstants.SUB_APPLICATION_SWITCHER_ACTIVE_BACKGROUND_START_COLOR, new ColorLnfResource(255,
				255, 255));
		table.put(LnfKeyConstants.SUB_APPLICATION_SWITCHER_ACTIVE_BACKGROUND_END_COLOR, new ColorLnfResource(255, 255,
				255));
		table.put(LnfKeyConstants.SUB_APPLICATION_SWITCHER_PASSIVE_BACKGROUND_START_COLOR, new ColorLnfResource(245,
				245, 245));
		table.put(LnfKeyConstants.SUB_APPLICATION_SWITCHER_PASSIVE_BACKGROUND_END_COLOR, new ColorLnfResource(229, 229,
				229));
		table.put(LnfKeyConstants.SUB_APPLICATION_SWITCHER_INNER_BORDER_COLOR, new ColorLnfResource(245, 245, 245));
		table.put(LnfKeyConstants.SUB_APPLICATION_SWITCHER_BORDER_TOP_RIGHT_COLOR, new ColorLnfResource(206, 206, 206));
		table.put(LnfKeyConstants.SUB_APPLICATION_SWITCHER_BORDER_BOTTOM_LEFT_COLOR,
				new ColorLnfResource(183, 183, 183));
		table.put(LnfKeyConstants.SUB_APPLICATION_SWITCHER_INNER_DISABLED_BORDER_COLOR, new ColorLnfResource(245, 245,
				245));
		table.put(LnfKeyConstants.SUB_APPLICATION_SWITCHER_DISABLED_BORDER_TOP_RIGHT_COLOR, new ColorLnfResource(226,
				226, 226));
		table.put(LnfKeyConstants.SUB_APPLICATION_SWITCHER_DISABLED_BORDER_BOTTOM_LEFT_COLOR, new ColorLnfResource(203,
				203, 203));

		table.put(LnfKeyConstants.SUB_MODULE_ITEM_TOOLTIP_BACKGROUND, getPrimaryBackground());
		table.put(LnfKeyConstants.MODULE_ITEM_TOOLTIP_BACKGROUND, getPrimaryBackground());

		table.put(LnfKeyConstants.STATUSLINE_BACKGROUND, getPrimaryBackground());
		table.put(LnfKeyConstants.STATUSLINE_UI_PROCESS_LIST_BACKGROUND, new ColorLnfResource(183, 216, 236));

		table.put(LnfKeyConstants.GRAB_CORNER_BACKGROUND, getPrimaryBackground());
		table.put(LnfKeyConstants.MANDATORY_MARKER_BACKGROUND, new ColorLnfResource(255, 255, 175));

		// Standard SWT controls
		table.put("Button.background", getPrimaryBackground()); //$NON-NLS-1$
		table.put("Button.foreground", getPrimaryForeground()); // Windows: no effect //$NON-NLS-1$

		table.put("Combo.background", getPrimaryBackground()); //$NON-NLS-1$
		table.put("Combo.foreground", getPrimaryForeground()); //$NON-NLS-1$

		table.put("Composite.background", getPrimaryBackground()); //$NON-NLS-1$
		table.put("Composite.foreground", getPrimaryForeground()); //$NON-NLS-1$

		table.put("DateTime.background", getPrimaryBackground()); // no effect //$NON-NLS-1$
		table.put("DateTime.foreground", getPrimaryForeground()); // no effect //$NON-NLS-1$

		table.put("Group.background", getPrimaryBackground()); //$NON-NLS-1$
		table.put("Group.foreground", getPrimaryForeground()); //$NON-NLS-1$

		table.put("Label.background", getPrimaryBackground()); //$NON-NLS-1$
		table.put("Label.foreground", getPrimaryForeground()); //$NON-NLS-1$

		table.put("List.background", getPrimaryBackground()); // no effect //$NON-NLS-1$
		table.put("List.foreground", getPrimaryForeground()); //$NON-NLS-1$

		table.put("Slider.background", getPrimaryBackground()); // no effect //$NON-NLS-1$
		table.put("Slider.foreground", getPrimaryForeground()); //$NON-NLS-1$

		table.put("Table.background", getPrimaryBackground()); //$NON-NLS-1$
		table.put("Table.foreground", getPrimaryForeground()); //$NON-NLS-1$

		table.put("Text.background", new ColorLnfResource(null)); //$NON-NLS-1$
		table.put("Text.foreground", getPrimaryForeground()); //$NON-NLS-1$

		table.put("Tree.background", getPrimaryBackground()); //$NON-NLS-1$
		table.put("Tree.foreground", getPrimaryForeground()); //$NON-NLS-1$

		table.put("white", new ColorLnfResource(255, 255, 255)); //$NON-NLS-1$
		table.put("lightGray", new ColorLnfResource(192, 192, 192)); //$NON-NLS-1$
		table.put("gray", new ColorLnfResource(128, 128, 128)); //$NON-NLS-1$
		table.put("darkGray", new ColorLnfResource(64, 64, 64)); //$NON-NLS-1$
		table.put("black", new ColorLnfResource(0, 0, 0)); //$NON-NLS-1$
		table.put("red", new ColorLnfResource(255, 0, 0)); //$NON-NLS-1$
		table.put("pink", new ColorLnfResource(255, 175, 175)); //$NON-NLS-1$
		table.put("orange", new ColorLnfResource(255, 200, 0)); //$NON-NLS-1$
		table.put("yellow", new ColorLnfResource(255, 255, 0)); //$NON-NLS-1$
		table.put("green", new ColorLnfResource(0, 255, 0)); //$NON-NLS-1$
		table.put("magenta", new ColorLnfResource(255, 0, 255)); //$NON-NLS-1$
		table.put("cyan", new ColorLnfResource(0, 255, 255)); //$NON-NLS-1$
		table.put("blue", new ColorLnfResource(0, 0, 255)); //$NON-NLS-1$

	}

	/**
	 * @see org.eclipse.riena.ui.swt.lnf.ILnfTheme#addCustomFonts(java.util.Map)
	 */
	public void addCustomFonts(Map<String, ILnfResource> table) {

		table.put(LnfKeyConstants.TITLELESS_SHELL_FONT, getPrimaryFont());

		table.put(LnfKeyConstants.DIALOG_FONT, getPrimaryFont());

		table.put(LnfKeyConstants.SUB_APPLICATION_SWITCHER_FONT, getPrimaryFont());

		table.put(LnfKeyConstants.MODULE_ITEM_TOOLTIP_FONT, getPrimaryFont());

		table.put(LnfKeyConstants.SUB_MODULE_ITEM_FONT, getPrimaryFont());
		table.put(LnfKeyConstants.SUB_MODULE_ITEM_TOOLTIP_FONT, getPrimaryFont());

		table.put(LnfKeyConstants.EMBEDDED_TITLEBAR_FONT, getPrimaryFont());

		// Standard SWT controls
		table.put("Button.font", getPrimaryFont()); //$NON-NLS-1$

		table.put("Combo.font", getPrimaryFont()); //$NON-NLS-1$

		table.put("Composite.font", getPrimaryFont()); //$NON-NLS-1$

		table.put("DateTime.font", getPrimaryFont()); //$NON-NLS-1$

		table.put("Group.font", getPrimaryFont()); //$NON-NLS-1$

		table.put("Label.font", getPrimaryFont()); //$NON-NLS-1$

		table.put("List.font", getPrimaryFont()); //$NON-NLS-1$

		table.put("Slider.font", getPrimaryFont()); //$NON-NLS-1$

		table.put("Table.font", getPrimaryFont()); //$NON-NLS-1$

		table.put("Text.font", getPrimaryFont()); //$NON-NLS-1$

		table.put("Tree.font", getPrimaryFont()); //$NON-NLS-1$

	}

	/**
	 * @see org.eclipse.riena.ui.swt.lnf.ILnfTheme#addCustomImages(java.util.Map)
	 */
	public void addCustomImages(Map<String, ILnfResource> table) {

		table.put(LnfKeyConstants.TITLELESS_SHELL_BACKGROUND_IMAGE, new ImageLnfResource(IMAGE_BACKGROUND));
		table.put(LnfKeyConstants.TITLELESS_SHELL_LOGO, new ImageLnfResource(IMAGE_LOGO));
		table.put(LnfKeyConstants.TITLELESS_SHELL_CLOSE_ICON, new ImageLnfResource(IMAGE_CLOSE));
		table.put(LnfKeyConstants.TITLELESS_SHELL_CLOSE_HOVER_ICON, new ImageLnfResource(IMAGE_CLOSE_HOVER));
		table.put(LnfKeyConstants.TITLELESS_SHELL_CLOSE_HOVER_SELECTED_ICON, new ImageLnfResource(
				IMAGE_CLOSE_HOVER_SELECTED));
		table.put(LnfKeyConstants.TITLELESS_SHELL_CLOSE_INACTIVE_ICON, new ImageLnfResource(IMAGE_CLOSE_INACTIVE));
		table.put(LnfKeyConstants.TITLELESS_SHELL_MAX_ICON, new ImageLnfResource(IMAGE_MAX));
		table.put(LnfKeyConstants.TITLELESS_SHELL_MAX_HOVER_ICON, new ImageLnfResource(IMAGE_MAX_HOVER));
		table.put(LnfKeyConstants.TITLELESS_SHELL_MAX_HOVER_SELECTED_ICON, new ImageLnfResource(
				IMAGE_MAX_HOVER_SELECTED));
		table.put(LnfKeyConstants.TITLELESS_SHELL_MAX_INACTIVE_ICON, new ImageLnfResource(IMAGE_MAX_INACTIVE));
		table.put(LnfKeyConstants.TITLELESS_SHELL_MIN_ICON, new ImageLnfResource(IMAGE_MIN));
		table.put(LnfKeyConstants.TITLELESS_SHELL_MIN_HOVER_ICON, new ImageLnfResource(IMAGE_MIN_HOVER));
		table.put(LnfKeyConstants.TITLELESS_SHELL_MIN_HOVER_SELECTED_ICON, new ImageLnfResource(
				IMAGE_MIN_HOVER_SELECTED));
		table.put(LnfKeyConstants.TITLELESS_SHELL_MIN_INACTIVE_ICON, new ImageLnfResource(IMAGE_MIN_INACTIVE));
		table.put(LnfKeyConstants.TITLELESS_SHELL_RESTORE_ICON, new ImageLnfResource(IMAGE_RESTORE));
		table.put(LnfKeyConstants.TITLELESS_SHELL_RESTORE_HOVER_ICON, new ImageLnfResource(IMAGE_RESTORE_HOVER));
		table.put(LnfKeyConstants.TITLELESS_SHELL_RESTORE_HOVER_SELECTED_ICON, new ImageLnfResource(
				IMAGE_RESTORE_HOVER_SELECTED));
		table.put(LnfKeyConstants.TITLELESS_SHELL_RESTORE_INACTIVE_ICON, new ImageLnfResource(
				IMAGE_RESTORE_INACTIVE_ICON));
		table.put(LnfKeyConstants.TITLELESS_SHELL_HAND_IMAGE, new ImageLnfResource(IMAGE_HAND));
		table.put(LnfKeyConstants.TITLELESS_SHELL_GRAB_IMAGE, new ImageLnfResource(IMAGE_GRAB));
		table.put(LnfKeyConstants.TITLELESS_SHELL_GRAB_CORNER_IMAGE, new ImageLnfResource(IMAGE_GRAB_CORNER));

		table.put(LnfKeyConstants.DIALOG_CLOSE_ICON, new ImageLnfResource(IMAGE_CLOSE));
		table.put(LnfKeyConstants.DIALOG_CLOSE_HOVER_ICON, new ImageLnfResource(IMAGE_CLOSE_HOVER));
		table.put(LnfKeyConstants.DIALOG_CLOSE_HOVER_SELECTED_ICON, new ImageLnfResource(IMAGE_CLOSE_HOVER_SELECTED));
		table.put(LnfKeyConstants.DIALOG_CLOSE_INACTIVE_ICON, new ImageLnfResource(IMAGE_CLOSE_INACTIVE));
		table.put(LnfKeyConstants.DIALOG_MAX_ICON, new ImageLnfResource(IMAGE_MAX));
		table.put(LnfKeyConstants.DIALOG_MAX_HOVER_ICON, new ImageLnfResource(IMAGE_MAX_HOVER));
		table.put(LnfKeyConstants.DIALOG_MAX_HOVER_SELECTED_ICON, new ImageLnfResource(IMAGE_MAX_HOVER_SELECTED));
		table.put(LnfKeyConstants.DIALOG_MAX_INACTIVE_ICON, new ImageLnfResource(IMAGE_MAX_INACTIVE));
		table.put(LnfKeyConstants.DIALOG_MIN_ICON, new ImageLnfResource(IMAGE_MIN));
		table.put(LnfKeyConstants.DIALOG_MIN_HOVER_ICON, new ImageLnfResource(IMAGE_MIN_HOVER));
		table.put(LnfKeyConstants.DIALOG_MIN_HOVER_SELECTED_ICON, new ImageLnfResource(IMAGE_MIN_HOVER_SELECTED));
		table.put(LnfKeyConstants.DIALOG_MIN_INACTIVE_ICON, new ImageLnfResource(IMAGE_MIN_INACTIVE));
		table.put(LnfKeyConstants.DIALOG_RESTORE_ICON, new ImageLnfResource(IMAGE_RESTORE));
		table.put(LnfKeyConstants.DIALOG_RESTORE_HOVER_ICON, new ImageLnfResource(IMAGE_RESTORE_HOVER));
		table.put(LnfKeyConstants.DIALOG_RESTORE_HOVER_SELECTED_ICON,
				new ImageLnfResource(IMAGE_RESTORE_HOVER_SELECTED));
		table.put(LnfKeyConstants.DIALOG_RESTORE_INACTIVE_ICON, new ImageLnfResource(IMAGE_RESTORE_INACTIVE_ICON));

		table.put(LnfKeyConstants.DEFAULT_NODE_ICON, new ImageLnfResource(IMAGE_DEFAULT_NODE_ICON));
		table.put(LnfKeyConstants.SUB_MODULE_TREE_DOCUMENT_LEAF_ICON, new ImageLnfResource(IMAGE_EMPTY_DOCUMENT));
		table.put(LnfKeyConstants.SUB_MODULE_TREE_FOLDER_CLOSED_ICON, new ImageLnfResource(IMAGE_FOLDER_CLOSED));
		table.put(LnfKeyConstants.SUB_MODULE_TREE_ERROR_MARKER_ICON, new ImageLnfResource(IMAGE_ERROR_MARKER));
		table.put(LnfKeyConstants.SUB_MODULE_TREE_MANDATORY_MARKER_ICON, new ImageLnfResource(IMAGE_MANDATORY_MARKER));
		table.put(LnfKeyConstants.SUB_MODULE_TREE_PROCESSED_FINISHED_MARKER_ICON, new ImageLnfResource(
				IMAGE_PROCESS_FINISHED_MARKER));
		// table.put(LnfKeyConstants."treeFolderOpen.icon",
		// getSharedImageResource(ISharedImages.IMG_OBJ_FOLDER));
		table.put(LnfKeyConstants.SUB_MODULE_TREE_FOLDER_OPEN_ICON, new ImageLnfResource(IMAGE_FOLDER));
		table.put(LnfKeyConstants.EMBEDDED_TITLEBAR_CLOSE_ICON, new ImageLnfResource(IMAGE_CLOSE_MODULE));
		table.put(LnfKeyConstants.EMBEDDED_TITLEBAR_CLOSE_HOVER_ICON, new ImageLnfResource(IMAGE_CLOSE_MODULE_HOVER));
		table.put(LnfKeyConstants.EMBEDDED_TITLEBAR_CLOSE_INACTIVE_ICON, new ImageLnfResource(
				IMAGE_CLOSE_MODULE_INACTIVE));
		table.put(LnfKeyConstants.EMBEDDED_TITLEBAR_CLOSE_HOVER_SELECTED_ICON, new ImageLnfResource(
				IMAGE_CLOSE_MODULE_HOVER_SELECTED));

		table.put(LnfKeyConstants.STATUSLINE_SPACER_ICON, new ImageLnfResource(IMAGE_SPACER));
		table.put(LnfKeyConstants.STATUSLINE_ERROR_ICON, new ImageLnfResource(IMAGE_ERROR));
		table.put(LnfKeyConstants.STATUSLINE_WARNING_ICON, new ImageLnfResource(IMAGE_WARNING));
		table.put(LnfKeyConstants.STATUSLINE_INFO_ICON, new ImageLnfResource(IMAGE_INFO));

		table.put(LnfKeyConstants.NAVIGATION_SCROLL_UP_ICON, new ImageLnfResource(IMAGE_SCROLL_UP));
		table.put(LnfKeyConstants.NAVIGATION_SCROLL_DOWN_ICON, new ImageLnfResource(IMAGE_SCROLL_DOWN));

		table.put(LnfKeyConstants.ERROR_MARKER_ICON, new ImageLnfResource(IMAGE_ERROR_MARKER));

	}

	/**
	 * @see org.eclipse.riena.ui.swt.lnf.ILnfTheme#addCustomSettings(java.util.Map)
	 */
	public void addCustomSettings(Map<String, Object> table) {

		table.put(LnfKeyConstants.SHELL_HIDE_OS_BORDER, hideOsBorder());

		table.put(LnfKeyConstants.TITLELESS_SHELL_PADDING, 2);
		table.put(LnfKeyConstants.TITLELESS_SHELL_NAVIGATION_HORIZONTAL_GAP, 5);
		table.put(LnfKeyConstants.TITLELESS_SHELL_SUB_MODULE_HORIZONTAL_GAP, 5);
		table.put(LnfKeyConstants.TITLELESS_SHELL_HORIZONTAL_LOGO_POSITION, SWT.LEFT);
		table.put(LnfKeyConstants.TITLELESS_SHELL_VERTICAL_LOGO_POSITION, SWT.CENTER);
		table.put(LnfKeyConstants.TITLELESS_SHELL_HORIZONTAL_LOGO_MARGIN, 17);
		table.put(LnfKeyConstants.TITLELESS_SHELL_VERTICAL_LOGO_MARGIN, 4);
		table.put(LnfKeyConstants.TITLELESS_SHELL_HORIZONTAL_TEXT_POSITION, SWT.RIGHT);
		table.put(LnfKeyConstants.TITLELESS_SHELL_SHOW_CLOSE, true);
		table.put(LnfKeyConstants.TITLELESS_SHELL_SHOW_MAX, true);
		table.put(LnfKeyConstants.TITLELESS_SHELL_SHOW_MIN, true);
		table.put(LnfKeyConstants.TITLELESS_SHELL_RESIZEABLE, true);

		table.put(LnfKeyConstants.DIALOG_HIDE_OS_BORDER, hideOsBorder());

		table.put(LnfKeyConstants.SUB_APPLICATION_SWITCHER_TOP_MARGIN, 22);
		table.put(LnfKeyConstants.SUB_APPLICATION_SWITCHER_HEIGHT, 40);
		table.put(LnfKeyConstants.SUB_APPLICATION_SWITCHER_HORIZONTAL_TAB_POSITION, SWT.CENTER);
		table.put(LnfKeyConstants.SUB_APPLICATION_SWITCHER_TAB_SHOW_ICON, false);
		table.put(LnfKeyConstants.SUB_APPLICATION_SWITCHER_TAB_MIN_WIDTH, 0);

		table.put(LnfKeyConstants.SUB_MODULE_ITEM_TOOLTIP_POPUP_DELAY, 0);

		table.put(LnfKeyConstants.MODULE_ITEM_TOOLTIP_POPUP_DELAY, 500);

		table.put(LnfKeyConstants.NAVIGATION_SUB_MODULE_GAP, 5);
		table.put(LnfKeyConstants.MENUBAR_TOP_MARGIN, 0);
		table.put(LnfKeyConstants.TOOLBAR_TOP_MARGIN, 0);

		table.put(LnfKeyConstants.ERROR_MARKER_HORIZONTAL_POSITION, SWT.LEFT);
		table.put(LnfKeyConstants.ERROR_MARKER_VERTICAL_POSITION, SWT.TOP);
		table.put(LnfKeyConstants.ERROR_MARKER_MARGIN, 1);

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
		return new FontData("Arial Narrow", 10, SWT.NORMAL); //$NON-NLS-1$
	}

	/**
	 * Returns the color used for the foreground of widgets (of the navigation).
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
	 * Returns the color used for the background of widgets (of the navigation).
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
	 * Returns the font used for widgets (of the navigation).
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

	/**
	 * Returns whether the border of the operation system should be used for the
	 * shell and the dialog windows or the border of the Riena Look&Feel.
	 * 
	 * @return
	 */
	protected boolean hideOsBorder() {
		return true;
	}

}
