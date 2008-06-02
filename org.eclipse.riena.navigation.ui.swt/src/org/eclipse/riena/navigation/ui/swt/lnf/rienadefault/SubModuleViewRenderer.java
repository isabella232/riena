/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.ui.swt.lnf.rienadefault;

import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.swt.lnf.AbstractLnfRenderer;
import org.eclipse.riena.navigation.ui.swt.lnf.LnfManager;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Renderer of the active sub module
 */
public class SubModuleViewRenderer extends AbstractLnfRenderer {

	private EmbeddedTitlebarRenderer titlebarRenderer;
	private EmbeddedBorderRenderer borderRenderer;

	/**
	 * Creates an new instance of <code>SubModuleViewRenderer</code> and sets
	 * the renderer of the border and the titlebar.
	 */
	public SubModuleViewRenderer() {
		super();
		setBorderRenderer(getLnfBorderRenderer());
		setTitlebarRenderer(getLnfTitlebarRenderer());
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.AbstractLnfRenderer#paint(org.eclipse.swt.graphics.GC,
	 *      java.lang.Object)
	 */
	@Override
	public void paint(GC gc, Object value) {

		assert value instanceof SubModuleNode;

		SubModuleNode node = (SubModuleNode) value;

		// titlebar
		getTitlebarRenderer().setActive(true);
		getTitlebarRenderer().setPressed(false);
		getTitlebarRenderer().setIcon(node.getIcon());
		Point titlebarSize = getTitlebarRenderer().computeSize(gc, getBounds().width - 2, 0);
		Rectangle titlebarBounds = new Rectangle(getBounds().x + 1, getBounds().y + 1, titlebarSize.x, titlebarSize.y);
		getTitlebarRenderer().setBounds(titlebarBounds);
		getTitlebarRenderer().paint(gc, getTitle(node));

		// border
		getBorderRenderer().setActive(true);
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
	private String getTitle(SubModuleNode node) {

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
		return titlebarRenderer;
	}

	public void setTitlebarRenderer(EmbeddedTitlebarRenderer titlebarRenderer) {
		this.titlebarRenderer = titlebarRenderer;
	}

	public EmbeddedBorderRenderer getBorderRenderer() {
		return borderRenderer;
	}

	public void setBorderRenderer(EmbeddedBorderRenderer borderRenderer) {
		this.borderRenderer = borderRenderer;
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

}
