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
package org.eclipse.riena.navigation.ui.swt.component;

import org.eclipse.jface.window.DefaultToolTip;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.ModuleGroupRenderer;
import org.eclipse.riena.navigation.ui.swt.views.ModuleGroupView;
import org.eclipse.riena.navigation.ui.swt.views.ModuleView;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

/**
 * ToolTip for the modules (views) in the module group.<br>
 * ToolTip is only displayed if the text of the module view was clipped.
 */
public class ModuleGroupToolTip extends DefaultToolTip {

	private ModuleGroupView moduleGroupWidget;

	/**
	 * Creates new instance which add TooltipSupport to the control.
	 * 
	 * @param moduleGroupView
	 *            TODO
	 */
	public ModuleGroupToolTip(ModuleGroupView moduleGroupView) {
		super(moduleGroupView);
		setModuleGroupView(moduleGroupView);
		setShift(new Point(0, 0));
	}

	/**
	 * Initializes the look (color and font) and feel (popup delay) of the tool
	 * tip. Uses the settings of the look and feel.
	 */
	private void initLookAndFeel() {

		RienaDefaultLnf lnf = LnfManager.getLnf();

		Integer delay = lnf.getIntegerSetting(ILnfKeyConstants.MODULE_ITEM_TOOLTIP_POPUP_DELAY);
		if (delay != null) {
			setPopupDelay(delay);
		}
		Color color = lnf.getColor(ILnfKeyConstants.MODULE_ITEM_TOOLTIP_FOREGROUND);
		if (color != null) {
			setForegroundColor(color);
		}
		color = lnf.getColor(ILnfKeyConstants.MODULE_ITEM_TOOLTIP_BACKGROUND);
		if (color != null) {
			setBackgroundColor(color);
		}
		Font font = lnf.getFont(ILnfKeyConstants.MODULE_ITEM_TOOLTIP_FONT);
		if (color != null) {
			setFont(font);
		}

	}

	private ModuleGroupRenderer getRenderer() {

		return (ModuleGroupRenderer) LnfManager.getLnf().getRenderer(ILnfKeyConstants.MODULE_GROUP_RENDERER);

	}

	/**
	 * @see org.eclipse.jface.window.ToolTip#createToolTipContentArea(org.eclipse.swt.widgets.Event,
	 *      org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Composite createToolTipContentArea(Event event, Composite parent) {

		CLabel label = new CLabel(parent, getStyle(event));

		Color fgColor = getForegroundColor(event);
		Color bgColor = getBackgroundColor(event);
		Font font = getFont(event);

		if (fgColor != null) {
			label.setForeground(fgColor);
		}

		if (bgColor != null) {
			label.setBackground(bgColor);
		}

		if (font != null) {
			label.setFont(font);
		}

		ModuleView view = getModuleView(event);
		if (view != null) {
			label.setText(view.getNavigationNode().getLabel());
		}

		return label;

	}

	/**
	 * @see org.eclipse.jface.window.ToolTip#shouldCreateToolTip(org.eclipse.swt.widgets.Event)
	 */
	@Override
	protected boolean shouldCreateToolTip(Event event) {

		boolean should = super.shouldCreateToolTip(event);

		if (should) {
			initLookAndFeel();
			ModuleView view = getModuleView(event);
			if (view != null) {
				GC gc = new GC(getModuleGroupView());
				should = getRenderer().isTextClipped(gc, view);
				gc.dispose();
			} else {
				should = false;
			}
		}

		return should;

	}

	/**
	 * @see org.eclipse.jface.window.ToolTip#getLocation(org.eclipse.swt.graphics.Point,
	 *      org.eclipse.swt.widgets.Event)
	 */
	@Override
	public Point getLocation(Point tipSize, Event event) {

		Point location = super.getLocation(tipSize, event);
		ModuleView view = getModuleView(event);
		if (view != null) {
			GC gc = new GC(getModuleGroupView());
			Rectangle textBounds = getRenderer().computeTextBounds(gc, view);
			gc.dispose();
			location = getModuleGroupView().toDisplay(textBounds.x, textBounds.y);
		}

		return location;

	}

	/**
	 * @return the moduleGroupWidget
	 */
	private ModuleGroupView getModuleGroupView() {
		return moduleGroupWidget;
	}

	/**
	 * @param moduleGroupWidget
	 *            the moduleGroupWidget to set
	 */
	private void setModuleGroupView(ModuleGroupView moduleGroupWidget) {
		this.moduleGroupWidget = moduleGroupWidget;
	}

	/**
	 * Returns the module at the given point.
	 * 
	 * @param point
	 *            - point over module view
	 * @return module view; or null, if not module view was found
	 */
	protected ModuleView getModuleView(Event event) {

		Point point = new Point(event.x, event.y);

		ModuleView view = getModuleGroupView().getItem(point);
		if (view != null) {
			GC gc = new GC(getModuleGroupView());
			Rectangle textBounds = getRenderer().computeTextBounds(gc, view);
			gc.dispose();
			if (textBounds.contains(point)) {
				return view;
			}
		}

		return null;

	}

}