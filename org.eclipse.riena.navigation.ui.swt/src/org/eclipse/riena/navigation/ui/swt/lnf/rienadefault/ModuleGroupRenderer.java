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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ui.swt.component.ModuleItem;
import org.eclipse.riena.navigation.ui.swt.lnf.AbstractLnfRenderer;
import org.eclipse.riena.navigation.ui.swt.lnf.LnfManager;
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

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.AbstractLnfRenderer#paint(org.eclipse.swt.graphics.GC,
	 *      java.lang.Object)
	 */
	@Override
	public void paint(GC gc, Object value) {

		assert value instanceof IModuleGroupNode;

		IModuleGroupNode node = (IModuleGroupNode) value;

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
		borderRenderer.setActive(node.isActivated());
		borderRenderer.paint(gc, null);

		// modules
		Rectangle innerBorder = borderRenderer.computeInnerBounds(borderRenderer.getBounds());
		int x = innerBorder.x + MODULE_GROUP_PADDING;
		int y = innerBorder.y + MODULE_GROUP_PADDING;
		int w = innerBorder.width - MODULE_GROUP_PADDING * 2;
		List<ModuleItem> modules = getItems();
		for (ModuleItem module : modules) {

			IModuleNode moduleNode = module.getModuleNode();

			// titlebar
			titlebarRenderer.setActive(moduleNode.isActivated());
			titlebarRenderer.setCloseable(moduleNode.isCloseable());
			titlebarRenderer.setPressed(module.isPressed());
			titlebarRenderer.setHover(module.isHover());
			titlebarRenderer.setIcon(moduleNode.getIcon());
			Point titlebarSize = titlebarRenderer.computeSize(gc, getBounds().width, 0);
			Rectangle titlebarBounds = new Rectangle(x, y, w, titlebarSize.y);
			titlebarRenderer.setBounds(titlebarBounds);
			titlebarRenderer.paint(gc, moduleNode.getLabel());

			y += titlebarSize.y;

			if (moduleNode.isActivated()) {
				// body (normally: tree) of module
				module.getBody().layout();
				module.getBody().setBounds(x, y, w, module.getOpenHeight() - 1);
				module.getBody().setVisible(true);
				y += module.getOpenHeight();
				titlebarBounds.height += module.getOpenHeight();
			}

			y += MODULE_MODULE_GAP;

		}

	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.ILnfRenderer#dispose()
	 */
	public void dispose() {
		// nothing to do.
	}

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
			IModuleNode moduleNode = module.getModuleNode();
			titlebarRenderer.setIcon(moduleNode.getIcon());
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

	public int computeItemHeight(GC gc) {
		int h = getLnfTitlebarRenderer().computeSize(gc, 10, 0).y;
		h += MODULE_GROUP_PADDING;
		return h;
	}

	public int getItemWidth() {
		return MODULE_WIDTH;
	}

	/**
	 * Returns the bounds of the close "button" of the titlebar.<br>
	 * <i><b>Note:</b> only x and width values are correct. Maybe y and height
	 * are not always correct.</i>
	 * 
	 * @return bounds of close "button".
	 */
	public Rectangle computeCloseButtonBounds(GC gc) {

		Point size = computeSize(gc, getBounds().width, 0);
		EmbeddedBorderRenderer borderRenderer = getLnfBorderRenderer();
		borderRenderer.setBounds(0, 0, getBounds().width, size.y);
		Rectangle innerBorder = borderRenderer.computeInnerBounds(borderRenderer.getBounds());
		int x = innerBorder.x + MODULE_GROUP_PADDING;
		int y = innerBorder.y + MODULE_GROUP_PADDING;
		int w = innerBorder.width - MODULE_GROUP_PADDING * 2;

		EmbeddedTitlebarRenderer titlebarRenderer = getLnfTitlebarRenderer();
		Point titlebarSize = titlebarRenderer.computeSize(gc, getBounds().width, 0);
		titlebarRenderer.setBounds(x, y, w, titlebarSize.y);
		Rectangle bounds = titlebarRenderer.computeCloseButtonBounds();
		return bounds;

	}

	private EmbeddedBorderRenderer getLnfBorderRenderer() {

		EmbeddedBorderRenderer renderer = (EmbeddedBorderRenderer) LnfManager.getLnf().getRenderer(
				"SubModuleViewRenderer.borderRenderer"); //$NON-NLS-1$
		if (renderer == null) {
			renderer = new EmbeddedBorderRenderer();
		}
		return renderer;

	}

	private EmbeddedTitlebarRenderer getLnfTitlebarRenderer() {

		EmbeddedTitlebarRenderer renderer = (EmbeddedTitlebarRenderer) LnfManager.getLnf().getRenderer(
				"SubModuleViewRenderer.titlebarRenderer"); //$NON-NLS-1$
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

}
