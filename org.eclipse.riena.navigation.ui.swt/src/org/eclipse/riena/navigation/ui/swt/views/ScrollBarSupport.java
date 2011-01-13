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
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

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
		if (doScroll(activeModule)) {
			scrollTo(activeModule);
		}

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
		final int pixels = getScrollPixels(topComp, bottomComp);
		if (pixels != 0) {
			scroll(pixels);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean scrollTo(final Tree tree) {
		final int pixels = getScrollPixels(tree);
		if (pixels != 0) {
			scroll(pixels);
			return true;
		} else {
			return false;
		}
	}

	private boolean doScroll(final IModuleNode module) {
		if (module == null) {
			return false;
		}
		final ModuleView moduleView = navigationComponentProvider.getModuleViewForNode(module);
		if (moduleView == null) {
			return false;
		}
		final boolean isClosed = moduleView.getOpenHeight() == 0;
		if (isClosed) {
			return doScroll(moduleView.getTitle(), moduleView.getBody());
		} else {
			return doScroll(moduleView.getTree());
		}
	}

	private boolean doScroll(final Composite topComp, final Composite bottomComp) {
		final int pixels = getScrollPixels(topComp, bottomComp);
		if (pixels != 0) {
			return true;
		}
		return false;
	}

	private boolean doScroll(final Tree tree) {
		final int pixels = getScrollPixels(tree);
		if (pixels != 0) {
			return true;
		}
		return false;
	}

	private int getScrollPixels(final Composite topComp, final Composite bottomComp) {
		int ty = topComp.getBounds().y;
		ty = sc.getDisplay().map(topComp, sc, 0, ty).y;
		if (ty < 0) {
			return ty;
		}

		final int scHeight = sc.getBounds().height;
		int by = bottomComp.getBounds().height;
		by = sc.getDisplay().map(bottomComp, sc, 0, by).y;
		if (by > scHeight) {
			return by - scHeight;
		}

		return 0;

	}

	private int getScrollPixels(final Tree tree) {
		if (SwtUtilities.isDisposed(tree)) {
			return 0;
		}
		if (tree.getSelectionCount() > 0) {
			final TreeItem item = tree.getSelection()[0];
			final Rectangle itemBounds = item.getBounds();
			int y = sc.getDisplay().map(tree, sc, 0, itemBounds.y).y;
			if (y < 0) {
				return y;
			}
			final int scHeight = sc.getBounds().height;
			y = sc.getDisplay().map(tree, sc, 0, itemBounds.y + itemBounds.height).y;
			if (y > scHeight) {
				return y - scHeight;
			}
		}
		return 0;

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
