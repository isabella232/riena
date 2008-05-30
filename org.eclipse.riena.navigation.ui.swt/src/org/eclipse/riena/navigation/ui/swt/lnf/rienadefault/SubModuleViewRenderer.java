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
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class SubModuleViewRenderer extends AbstractLnfRenderer {

	private EmbeddedTitlebarRenderer titlebarRenderer;
	private EmbeddedBorderRenderer borderRenderer;

	public SubModuleViewRenderer() {
		super();
		setBorderRenderer(new EmbeddedBorderRenderer());
		setTitlebarRenderer(new EmbeddedTitlebarRenderer());
	}

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

	private String getTitle(SubModuleNode node) {

		StringBuilder titleBuilder = new StringBuilder(node.getLabel());
		INavigationNode<?> parent = node.getParent();
		while ((parent != null) && !(parent instanceof ModuleGroupNode)) {
			titleBuilder = titleBuilder.insert(0, " - ");
			titleBuilder = titleBuilder.insert(0, parent.getLabel());
			parent = parent.getParent();
		}

		return titleBuilder.toString();

	}

	public void dispose() {
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

	public Rectangle computeInnerBounds(GC gc, Rectangle outerBounds) {

		Rectangle borderInnerBounds = getBorderRenderer().computeInnerBounds(gc, outerBounds);
		Point titlebarSize = getTitlebarRenderer().computeSize(gc, outerBounds.width - 2, 0);

		return new Rectangle(borderInnerBounds.x, borderInnerBounds.y + titlebarSize.y, borderInnerBounds.width,
				borderInnerBounds.height - titlebarSize.y);

	}

}
