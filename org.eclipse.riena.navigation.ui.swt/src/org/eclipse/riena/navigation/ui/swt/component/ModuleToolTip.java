/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/

// TODO [ev] class moved - document API change in wiki and remove

//package org.eclipse.riena.navigation.ui.swt.component;
//
//import org.eclipse.jface.window.DefaultToolTip;
//import org.eclipse.swt.custom.CLabel;
//import org.eclipse.swt.graphics.Color;
//import org.eclipse.swt.graphics.Font;
//import org.eclipse.swt.graphics.GC;
//import org.eclipse.swt.graphics.Point;
//import org.eclipse.swt.graphics.Rectangle;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Event;
//
//import org.eclipse.riena.ui.swt.ModuleTitleBar;
//import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
//import org.eclipse.riena.ui.swt.lnf.LnfManager;
//import org.eclipse.riena.ui.swt.lnf.renderer.EmbeddedTitlebarRenderer;
//import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;
//
///**
// * ToolTip for the modules (views).<br>
// * ToolTip is only displayed if the text of the module view was clipped.
// */
//public class ModuleToolTip extends DefaultToolTip {
//
//	private ModuleTitleBar titleBar;
//
//	/**
//	 * Creates new instance which add TooltipSupport to the control.
//	 * 
//	 * @param titleBar
//	 *            title bar of a module
//	 */
//	public ModuleToolTip(final ModuleTitleBar titleBar) {
//		super(titleBar);
//		setTitleBar(titleBar);
//		setShift(new Point(0, 0));
//	}
//
//	/**
//	 * Initializes the look (color and font) and feel (popup delay) of the tool
//	 * tip. Uses the settings of the look and feel.
//	 */
//	private void initLookAndFeel() {
//
//		final RienaDefaultLnf lnf = LnfManager.getLnf();
//
//		final Integer delay = lnf.getIntegerSetting(LnfKeyConstants.MODULE_ITEM_TOOLTIP_POPUP_DELAY);
//		if (delay != null) {
//			setPopupDelay(delay);
//		}
//		Color color = lnf.getColor(LnfKeyConstants.MODULE_ITEM_TOOLTIP_FOREGROUND);
//		if (color != null) {
//			setForegroundColor(color);
//		}
//		color = lnf.getColor(LnfKeyConstants.MODULE_ITEM_TOOLTIP_BACKGROUND);
//		if (color != null) {
//			setBackgroundColor(color);
//		}
//		final Font font = lnf.getFont(LnfKeyConstants.MODULE_ITEM_TOOLTIP_FONT);
//		if (color != null) {
//			setFont(font);
//		}
//
//	}
//
//	/**
//	 * @see org.eclipse.jface.window.ToolTip#createToolTipContentArea(org.eclipse.swt.widgets.Event,
//	 *      org.eclipse.swt.widgets.Composite)
//	 */
//	@Override
//	protected Composite createToolTipContentArea(final Event event, final Composite parent) {
//
//		final CLabel label = new CLabel(parent, getStyle(event));
//
//		final Color fgColor = getForegroundColor(event);
//		final Color bgColor = getBackgroundColor(event);
//		final Font font = getFont(event);
//
//		if (fgColor != null) {
//			label.setForeground(fgColor);
//		}
//
//		if (bgColor != null) {
//			label.setBackground(bgColor);
//		}
//
//		if (font != null) {
//			label.setFont(font);
//		}
//
//		label.setText(getTitleBar().getTitle());
//
//		return label;
//
//	}
//
//	/**
//	 * @see org.eclipse.jface.window.ToolTip#shouldCreateToolTip(org.eclipse.swt.widgets.Event)
//	 */
//	@Override
//	protected boolean shouldCreateToolTip(final Event event) {
//
//		boolean should = super.shouldCreateToolTip(event);
//
//		if (should) {
//			initLookAndFeel();
//			should = getTitleBar().isTextClipped();
//		}
//
//		return should;
//
//	}
//
//	/**
//	 * @see org.eclipse.jface.window.ToolTip#getLocation(org.eclipse.swt.graphics.Point,
//	 *      org.eclipse.swt.widgets.Event)
//	 */
//	@Override
//	public Point getLocation(final Point tipSize, final Event event) {
//
//		final GC gc = new GC(getTitleBar());
//		final Rectangle bounds = getLnfTitlebarRenderer().computeTextBounds(gc);
//		final Point location = getTitleBar().toDisplay(bounds.x, 0);
//		gc.dispose();
//
//		return location;
//
//	}
//
//	/**
//	 * Returns the renderer of the title bar.
//	 * 
//	 * @return
//	 */
//	private EmbeddedTitlebarRenderer getLnfTitlebarRenderer() {
//
//		EmbeddedTitlebarRenderer renderer = (EmbeddedTitlebarRenderer) LnfManager.getLnf().getRenderer(
//				LnfKeyConstants.SUB_MODULE_VIEW_TITLEBAR_RENDERER);
//		if (renderer == null) {
//			renderer = new EmbeddedTitlebarRenderer();
//		}
//		return renderer;
//
//	}
//
//	/**
//	 * @param titleBar
//	 *            the titleBar to set
//	 */
//	private void setTitleBar(final ModuleTitleBar titleBar) {
//		this.titleBar = titleBar;
//	}
//
//	/**
//	 * @return the titleBar
//	 */
//	private ModuleTitleBar getTitleBar() {
//		return titleBar;
//	}
//
//}
