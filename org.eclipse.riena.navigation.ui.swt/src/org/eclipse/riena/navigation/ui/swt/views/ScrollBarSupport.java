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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;

/**
 * This class provides scrolling logic for the navigation with a scroll bar.
 */
public class ScrollBarSupport extends AbstractScrollingSupport {

	private final ScrolledComposite sc;

	/**
	 * @param navigationComponentProvider
	 */
	public ScrollBarSupport(final ScrolledComposite sc,
			final IModuleNavigationComponentProvider navigationComponentProvider) {
		super(navigationComponentProvider);
		this.sc = sc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scroll() {
		final IModuleNode activeModule = getActiveModule(getActiveNode());
		scrollTo(activeModule);
	}

	/**
	 * Returns the active module.
	 * 
	 * @param activeNode
	 *            the active navigation node
	 * @return active module
	 */
	private IModuleNode getActiveModule(final INavigationNode<?> activeNode) {
		if (activeNode instanceof IModuleGroupNode) {
			return null;
		} else if (activeNode instanceof IModuleNode) {
			return (IModuleNode) activeNode;
		} else if (activeNode instanceof ISubModuleNode) {
			return (IModuleNode) activeNode.getParent();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean scrollTo(final Composite topComp, final Composite bottomComp) {
		sc.showControl(topComp);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean scrollTo(final Tree tree) {
		final TreeItem[] selections = tree.getSelection();
		if (selections.length > 0) {
			// TODO
			final Rectangle itemBounds = selections[0].getBounds();
			sc.setOrigin(itemBounds.x, itemBounds.y);
		} else {
			sc.showControl(tree);
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int getNavigationComponentHeight() {
		return sc.getBounds().height;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void scrollUp(final int pixels) {
		scroll(-pixels);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void scrollDown(final int pixels) {
		scroll(pixels);
	}

	private void scroll(final int pixels) {
		final Point origin = sc.getOrigin();
		origin.y += pixels;
		sc.setOrigin(origin);
	}

}
