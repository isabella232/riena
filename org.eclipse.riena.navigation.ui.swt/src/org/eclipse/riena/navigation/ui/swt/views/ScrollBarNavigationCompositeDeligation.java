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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.ui.swt.facades.SWTFacade;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Delegation of a composite for scrolling in the navigation with a scroll bar.
 */
public class ScrollBarNavigationCompositeDeligation extends AbstractNavigationCompositeDeligation {

	private ScrolledComposite sc;

	public ScrollBarNavigationCompositeDeligation(final Composite superParent, final Composite parent,
			final IModuleNavigationComponentProvider navigationProvider) {
		super(superParent, parent, navigationProvider);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ScrolledComposite getScrolledComposite() {
		return sc;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Specify the minimum width and height at which the ScrolledComposite will
	 * begin scrolling.
	 */
	@Override
	public void updateSize(final int height) {
		super.updateSize(height);
		final int width = getNaviagtionComposite().getSize().x;
		sc.setMinSize(width, height);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Create the ScrolledComposite with the vertical scroll bar and the support
	 * with the scrolling logic.
	 */
	@Override
	protected Composite createNaviagtionComposite(final Composite parent,
			final IModuleNavigationComponentProvider navigationProvider) {
		sc = new ScrolledComposite(parent, SWT.V_SCROLL);
		final Composite naviComp = super.createNaviagtionComposite(sc, navigationProvider);
		sc.setContent(naviComp);
		sc.setBackground(NAVIGATION_BACKGROUND);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		SWTFacade.getDefault().setIncrement(sc.getVerticalBar(), 10); // TODO: LnF
		setScrollingSupport(new ScrollBarSupport(sc, navigationProvider));
		return naviComp;
	}

	/**
	 * Returns the size of the vertical scroll bar of the given
	 * {@code Composite}.
	 * 
	 * @return size of scroll bar or zero size if scroll bar dosn't exists or
	 *         isn't visible.
	 */
	@Override
	public Point getVerticalScrollBarSize() {
		if (SwtUtilities.isDisposed(getScrolledComposite())) {
			return super.getVerticalScrollBarSize();
		}
		if (!SwtUtilities.isDisposed(getScrolledComposite().getVerticalBar())) {
			if (getScrolledComposite().getVerticalBar().isVisible()) {
				return getScrolledComposite().getVerticalBar().getSize();
			}
		}
		return super.getVerticalScrollBarSize();
	}

}
