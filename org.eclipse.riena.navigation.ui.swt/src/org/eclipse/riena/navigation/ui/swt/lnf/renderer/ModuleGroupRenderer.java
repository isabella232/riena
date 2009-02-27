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
package org.eclipse.riena.navigation.ui.swt.lnf.renderer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.riena.navigation.ui.swt.views.ModuleView;
import org.eclipse.riena.ui.swt.lnf.AbstractLnfRenderer;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

/**
 * Renderer of the module group inside the navigation.
 */
public class ModuleGroupRenderer extends AbstractLnfRenderer {

	private static final int MODULE_MODULE_GAP = 2;
	private static final int MODULE_GROUP_PADDING = 1;
	private static final int MODULE_WIDTH = 165;

	private boolean active;
	private List<ModuleView> items;

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
		borderRenderer.setMarkers(getMarkers());
		borderRenderer.setBounds(getBounds().x, getBounds().y, getBounds().width, size.y);
		borderRenderer.setActive(isActive());
		borderRenderer.paint(gc, null);

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
		int h = 0;
		if (modules.size() > 0) {
			h = getModuleGroupPadding();
			for (Iterator<ModuleView> iterator = modules.iterator(); iterator.hasNext();) {
				ModuleView moduleView = iterator.next();
				if (moduleView.getNavigationNode() == null || moduleView.getNavigationNode().isDisposed()) {
					break;
				}
				moduleView.updateModuleView();
				h += moduleView.getBounds().height;
				if (iterator.hasNext()) {
					h += getModuleModuleGap();
				} else {
					h += getModuleGroupPadding();
				}
			}
			h = borderRenderer.computeOuterHeight(h);
		}

		return new Point(w, h);

	}

	private EmbeddedBorderRenderer getLnfBorderRenderer() {

		EmbeddedBorderRenderer renderer = (EmbeddedBorderRenderer) LnfManager.getLnf().getRenderer(
				LnfKeyConstants.SUB_MODULE_VIEW_BORDER_RENDERER);
		if (renderer == null) {
			renderer = new EmbeddedBorderRenderer();
		}
		return renderer;

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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
