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
package org.eclipse.riena.navigation.ui.swt.lnf.renderer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.riena.navigation.ui.swt.component.ModuleItem;
import org.eclipse.riena.ui.swt.lnf.AbstractLnfRenderer;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Renderer of the module group inside the navigation.
 */
public class ModuleGroupRenderer extends AbstractLnfRenderer {

	private static final int MODULE_MODULE_GAP = 2;
	private static final int MODULE_GROUP_PADDING = 1;
	private static final int MODULE_WIDTH = 165;

	private List<ModuleItem> items;
	private boolean activated;

	/**
	 * @see org.eclipse.riena.ui.swt.lnf.AbstractLnfRenderer#paint(org.eclipse.swt.graphics.GC,
	 *      java.lang.Object)
	 */
	@Override
	public void paint(GC gc, Object value) {

		// if (gc.getAdvanced()) {
		// gc.setTextAntialias(SWT.ON);
		// }
		// gc.setInterpolation(SWT.HIGH);
		// gc.setAntialias(SWT.ON);

		EmbeddedTitlebarRenderer titlebarRenderer = getLnfTitlebarRenderer();

		// border of module group
		Point size = computeSize(gc, getBounds().width, 0);
		EmbeddedBorderRenderer borderRenderer = getLnfBorderRenderer();
		borderRenderer.setBounds(getBounds().x, getBounds().y, getBounds().width, size.y);
		borderRenderer.setActive(isActivated());
		borderRenderer.paint(gc, null);

		// modules
		Rectangle innerBorder = borderRenderer.computeInnerBounds(borderRenderer.getBounds());
		int x = innerBorder.x + MODULE_GROUP_PADDING;
		int y = innerBorder.y + MODULE_GROUP_PADDING;
		int w = innerBorder.width - MODULE_GROUP_PADDING * 2;
		List<ModuleItem> modules = getItems();
		for (ModuleItem module : modules) {

			// title bar
			titlebarRenderer.setActive(module.isActivated());
			titlebarRenderer.setCloseable(module.isCloseable());
			titlebarRenderer.setPressed(module.isPressed());
			titlebarRenderer.setHover(module.isHover());
			titlebarRenderer.setIcon(module.getIcon());
			Point titlebarSize = titlebarRenderer.computeSize(gc, getBounds().width, 0);
			Rectangle titlebarBounds = new Rectangle(x, y, w, titlebarSize.y);
			titlebarRenderer.setBounds(titlebarBounds);
			String label = module.getLabel();
			titlebarRenderer.paint(gc, label);

			module.setBounds(new Rectangle(x, y, w, titlebarSize.y));

			y += titlebarSize.y;

			if (module.isActivated()) {
				// body (normally: tree) of module
				module.getBody().layout();
				module.getBody().setBounds(x, y, w, module.getOpenHeight() - 1);
				module.getBody().setVisible(true);
				y += module.getOpenHeight();
				titlebarBounds.height += module.getOpenHeight();
			}

			y += MODULE_MODULE_GAP;

			computeTextBounds(gc, module);

		}

	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.ILnfRenderer#dispose()
	 */
	public void dispose() {
		for (ModuleItem item : getItems()) {
			item.dispose();
		}
	}

	/**
	 * Computes the size of the module group.
	 * 
	 * @param gc -
	 *            <code>GC</code> of the component <code>Control</code>
	 * @param wHint -
	 *            the width hint
	 * @param hHint -
	 *            the height hint
	 * @return a Point representing the size of the module group
	 */
	public Point computeSize(GC gc, int wHint, int hHint) {

		EmbeddedTitlebarRenderer titlebarRenderer = getLnfTitlebarRenderer();
		EmbeddedBorderRenderer borderRenderer = getLnfBorderRenderer();

		int w = wHint;
		if (w == SWT.DEFAULT) {
			w = borderRenderer.computeOuterWidth(MODULE_WIDTH);
		}

		List<ModuleItem> modules = getItems();
		int h = MODULE_GROUP_PADDING;
		for (ModuleItem module : modules) {
			titlebarRenderer.setIcon(module.getIcon());
			Point titlebarSize = titlebarRenderer.computeSize(gc, wHint, 0);
			h += titlebarSize.y;
			if (module.getModuleNode().isActivated()) {
				h += module.getOpenHeight();
			}
			h += MODULE_MODULE_GAP;
		}
		h = borderRenderer.computeOuterHeight(h);

		return new Point(w, h);

	}

	public int getItemWidth() {
		return MODULE_WIDTH;
	}

	/**
	 * Returns the bounds of the close "button" of the title bar for the given
	 * module item.
	 * 
	 * @param item -
	 *            module item
	 * @return bounds of close "button".
	 */
	public Rectangle computeCloseButtonBounds(GC gc, ModuleItem item) {

		if (!item.getModuleNode().isCloseable()) {
			return new Rectangle(0, 0, 0, 0);
		}

		EmbeddedTitlebarRenderer titlebarRenderer = getLnfTitlebarRenderer();
		titlebarRenderer.setBounds(item.getBounds());
		Rectangle closeBounds = titlebarRenderer.computeCloseButtonBounds();

		return closeBounds;

	}

	/**
	 * Returns the bounds of the text of the titlebar for the given module item.
	 * 
	 * @param item -
	 *            module item
	 * @return bounds of text.
	 */
	public Rectangle computeTextBounds(GC gc, ModuleItem item) {

		EmbeddedTitlebarRenderer titlebarRenderer = getLnfTitlebarRenderer();
		titlebarRenderer.setBounds(item.getBounds());
		Rectangle textBounds = titlebarRenderer.computeTextBounds(gc);

		Rectangle closeBounds = computeCloseButtonBounds(gc, item);
		textBounds.width -= closeBounds.width;

		return textBounds;

	}

	public boolean isTextClipped(GC gc, ModuleItem item) {

		String text = item.getModuleNode().getLabel();
		EmbeddedTitlebarRenderer titlebarRenderer = getLnfTitlebarRenderer();
		titlebarRenderer.setBounds(item.getBounds());
		String clippedText = titlebarRenderer.getClippedText(gc, text);

		return !text.equals(clippedText);

	}

	private EmbeddedBorderRenderer getLnfBorderRenderer() {

		EmbeddedBorderRenderer renderer = (EmbeddedBorderRenderer) LnfManager.getLnf().getRenderer(
				ILnfKeyConstants.SUB_MODULE_VIEW_BORDER_RENDERER);
		if (renderer == null) {
			renderer = new EmbeddedBorderRenderer();
		}
		return renderer;

	}

	private EmbeddedTitlebarRenderer getLnfTitlebarRenderer() {

		EmbeddedTitlebarRenderer renderer = (EmbeddedTitlebarRenderer) LnfManager.getLnf().getRenderer(
				ILnfKeyConstants.SUB_MODULE_VIEW_TITLEBAR_RENDERER);
		if (renderer == null) {
			renderer = new EmbeddedTitlebarRenderer();
		}
		return renderer;

	}

	/**
	 * @return the items
	 */
	public List<ModuleItem> getItems() {
		if (items == null) {
			items = new ArrayList<ModuleItem>();
		}
		return items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(List<ModuleItem> items) {
		this.items = items;
	}

	/**
	 * @return the activated
	 */
	public boolean isActivated() {
		return activated;
	}

	/**
	 * @param activated
	 *            the activated to set
	 */
	public void setActivated(boolean activated) {
		this.activated = activated;
	}

}
