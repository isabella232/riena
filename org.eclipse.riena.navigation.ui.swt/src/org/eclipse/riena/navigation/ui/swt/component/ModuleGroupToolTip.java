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
import org.eclipse.riena.navigation.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.navigation.ui.swt.lnf.LnfManager;
import org.eclipse.riena.navigation.ui.swt.lnf.rienadefault.ModuleGroupRenderer;
import org.eclipse.riena.navigation.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

class ModuleGroupToolTip extends DefaultToolTip {

	private ModuleGroupWidget moduleGroupWidget;
	private ModuleGroupRenderer renderer;

	/**
	 * Creates new instance which add TooltipSupport to the control.
	 * 
	 * @param moduleGroupWidget2
	 *            TODO
	 * 
	 * @param control -
	 *            the control on whose action the tooltip is shown
	 */
	public ModuleGroupToolTip(ModuleGroupWidget moduleGroupWidget) {
		super(moduleGroupWidget);
		setModuleGroupWidget(moduleGroupWidget);
		setShift(new Point(0, 0));
		initLookAndFeel();
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
		if (renderer == null) {
			renderer = (ModuleGroupRenderer) LnfManager.getLnf().getRenderer(ILnfKeyConstants.MODULE_GROUP_RENDERER);
		}
		return renderer;
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

		ModuleItem item = getItem(event);
		if (item != null) {
			label.setText(item.getModuleNode().getLabel());
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
			ModuleItem item = getItem(event);
			if (item != null) {
				GC gc = new GC(getModuleGroupWidget());
				should = getRenderer().isTextClipped(gc, item);
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
		ModuleItem item = getItem(event);
		if (item != null) {
			GC gc = new GC(getModuleGroupWidget());
			Rectangle textBounds = getRenderer().computeTextBounds(gc, item);
			gc.dispose();
			location = getModuleGroupWidget().toDisplay(textBounds.x, textBounds.y);
		}

		return location;

	}

	/**
	 * @return the moduleGroupWidget
	 */
	private ModuleGroupWidget getModuleGroupWidget() {
		return moduleGroupWidget;
	}

	/**
	 * @param moduleGroupWidget
	 *            the moduleGroupWidget to set
	 */
	private void setModuleGroupWidget(ModuleGroupWidget moduleGroupWidget) {
		this.moduleGroupWidget = moduleGroupWidget;
	}

	/**
	 * Returns the module at the given point.
	 * 
	 * @param point -
	 *            point over module item
	 * @return module item; or null, if not item was found
	 */
	protected ModuleItem getItem(Event event) {

		Point point = new Point(event.x, event.y);

		for (ModuleItem item : getModuleGroupWidget().getItems()) {
			GC gc = new GC(getModuleGroupWidget());
			Rectangle textBounds = getRenderer().computeTextBounds(gc, item);
			gc.dispose();
			if (textBounds.contains(point)) {
				return item;
			}
		}

		return null;

	}

}