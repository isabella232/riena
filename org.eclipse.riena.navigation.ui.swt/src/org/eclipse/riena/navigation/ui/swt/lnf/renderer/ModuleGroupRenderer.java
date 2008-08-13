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
import java.util.Iterator;
import java.util.List;

import org.eclipse.riena.navigation.ui.swt.views.ModuleView;
import org.eclipse.riena.ui.swt.lnf.AbstractLnfRenderer;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.renderer.EmbeddedTitlebarRenderer;
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

	private List<ModuleView> items;
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

		// border of module group
		Point size = computeSize(gc, getBounds().width, 0);
		EmbeddedBorderRenderer borderRenderer = getLnfBorderRenderer();
		borderRenderer.setBounds(getBounds().x, getBounds().y, getBounds().width, size.y);
		borderRenderer.setActive(isActivated());
		borderRenderer.paint(gc, null);

		// List<ModuleView> modules = getItems();
		// for (Iterator<ModuleView> iterator = modules.iterator();
		// iterator.hasNext();) {
		// ModuleView moduleView = iterator.next();
		// moduleView.getBody().setVisible(moduleView.isActivated());
		// }

	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.ILnfRenderer#dispose()
	 */
	public void dispose() {
		for (ModuleView item : getItems()) {
			item.dispose();
		}
	}

	/**
	 * Computes the size of the module group.
	 * 
	 * @param gc
	 *            - <code>GC</code> of the component <code>Control</code>
	 * @param wHint
	 *            - the width hint
	 * @param hHint
	 *            - the height hint
	 * @return a Point representing the size of the module group
	 */
	public Point computeSize(GC gc, int wHint, int hHint) {

		EmbeddedBorderRenderer borderRenderer = getLnfBorderRenderer();

		int w = wHint;
		if (w == SWT.DEFAULT) {
			w = borderRenderer.computeOuterWidth(getItemWidth());
		}

		List<ModuleView> modules = getItems();
		int h = getModuleGroupPadding();
		for (Iterator<ModuleView> iterator = modules.iterator(); iterator.hasNext();) {
			ModuleView moduleView = iterator.next();
			moduleView.updateModuleView();
			h += moduleView.getBounds().height;
			if (iterator.hasNext()) {
				h += getModuleModuleGap();
			} else {
				h += getModuleGroupPadding();
			}
		}
		h = borderRenderer.computeOuterHeight(h);

		return new Point(w, h);

	}

	/**
	 * Returns the bounds of the close "button" of the title bar for the given
	 * module item.
	 * 
	 * @param item
	 *            - module item
	 * @return bounds of close "button".
	 */
	public Rectangle computeCloseButtonBounds(GC gc, ModuleView item) {

		if (!item.isCloseable()) {
			return new Rectangle(0, 0, 0, 0);
		}

		EmbeddedTitlebarRenderer titlebarRenderer = getLnfTitlebarRenderer(item);
		titlebarRenderer.setBounds(item.getBounds());
		Rectangle closeBounds = titlebarRenderer.computeCloseButtonBounds();

		return closeBounds;

	}

	/**
	 * Returns the bounds of the text of the titlebar for the given module item.
	 * 
	 * @param moduleView
	 *            - module item
	 * @return bounds of text.
	 */
	public Rectangle computeTextBounds(GC gc, ModuleView moduleView) {

		EmbeddedTitlebarRenderer titlebarRenderer = getLnfTitlebarRenderer(moduleView);
		titlebarRenderer.setBounds(moduleView.getBounds());
		Rectangle textBounds = titlebarRenderer.computeTextBounds(gc);

		Rectangle closeBounds = computeCloseButtonBounds(gc, moduleView);
		textBounds.width -= closeBounds.width;

		return textBounds;

	}

	public boolean isTextClipped(GC gc, ModuleView item) {

		String text = item.getLabel();
		EmbeddedTitlebarRenderer titlebarRenderer = getLnfTitlebarRenderer(item);
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

	private EmbeddedTitlebarRenderer getLnfTitlebarRenderer(ModuleView moduleView) {
		return moduleView.getLnfTitlebarRenderer();

	}

	/**
	 * @return the items
	 */
	public List<ModuleView> getItems() {
		if (items == null) {
			items = new ArrayList<ModuleView>();
		}
		return items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(List<ModuleView> items) {
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

	/**
	 * Returns the gap between to modules.
	 * 
	 * @return gap
	 */
	public int getModuleModuleGap() {
		return MODULE_MODULE_GAP;
	}

	/**
	 * Returns the padding (top,left,bottom,right) of a module group.
	 * 
	 * @return padding
	 */
	public int getModuleGroupPadding() {
		return MODULE_GROUP_PADDING;
	}

	/**
	 * Returns the width of a module (module item).
	 * 
	 * @return width
	 */
	public int getItemWidth() {
		return MODULE_WIDTH;
	}

}
