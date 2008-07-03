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

import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.ui.swt.lnf.AbstractLnfRenderer;
import org.eclipse.riena.navigation.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.navigation.ui.swt.lnf.LnfManager;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Renderer of the active sub module
 */
public class SubModuleViewRenderer extends AbstractLnfRenderer {

	/**
	 * Creates an new instance of <code>SubModuleViewRenderer</code> and sets
	 * the renderer of the border and the titlebar.
	 */
	public SubModuleViewRenderer() {
		super();
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.AbstractLnfRenderer#paint(org.eclipse.swt.graphics.GC,
	 *      java.lang.Object)
	 */
	@Override
	public void paint(GC gc, Object value) {

		assert value instanceof ISubModuleNode;

		ISubModuleNode node = (ISubModuleNode) value;

		// titlebar
		getTitlebarRenderer().setActive(node.isActivated());
		getTitlebarRenderer().setCloseable(false);
		getTitlebarRenderer().setPressed(false);
		getTitlebarRenderer().setHover(false);
		getTitlebarRenderer().setIcon(node.getIcon());
		Point titlebarSize = getTitlebarRenderer().computeSize(gc, getBounds().width - 2, 0);
		Rectangle titlebarBounds = new Rectangle(getBounds().x + 1, getBounds().y + 1, titlebarSize.x, titlebarSize.y);
		getTitlebarRenderer().setBounds(titlebarBounds);
		getTitlebarRenderer().paint(gc, getTitle(node));

		// border
		getBorderRenderer().setActive(node.isActivated());
		getBorderRenderer().setBounds(getBounds());
		getBorderRenderer().paint(gc, null);

	}

	/**
	 * Returns the text of the title bar.
	 * 
	 * @param node -
	 *            node of active sub module
	 * @return title
	 */
	private String getTitle(ISubModuleNode node) {

		StringBuilder titleBuilder = new StringBuilder(node.getLabel());
		INavigationNode<?> parent = node.getParent();
		while ((parent != null) && !(parent instanceof ModuleGroupNode)) {
			titleBuilder = titleBuilder.insert(0, " - "); //$NON-NLS-1$
			titleBuilder = titleBuilder.insert(0, parent.getLabel());
			parent = parent.getParent();
		}

		return titleBuilder.toString();

	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.ILnfRenderer#dispose()
	 */
	public void dispose() {
		getBorderRenderer().dispose();
		getTitlebarRenderer().dispose();
	}

	public EmbeddedTitlebarRenderer getTitlebarRenderer() {
		return getLnfTitlebarRenderer();
	}

	public EmbeddedBorderRenderer getBorderRenderer() {
		return getLnfBorderRenderer();
	}

	/**
	 * Computes the size of the space inside the outer bounds.
	 * 
	 * @param gc -
	 *            <code>GC</code>
	 * @param outerBounds -
	 *            outer bounds
	 * @return inner bounds
	 */
	public Rectangle computeInnerBounds(GC gc, Rectangle outerBounds) {

		Rectangle borderInnerBounds = getBorderRenderer().computeInnerBounds(outerBounds);
		Point titlebarSize = getTitlebarRenderer().computeSize(gc, outerBounds.width - 2, 0);

		return new Rectangle(borderInnerBounds.x, borderInnerBounds.y + titlebarSize.y, borderInnerBounds.width,
				borderInnerBounds.height - titlebarSize.y);

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

}
