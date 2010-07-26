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
package org.eclipse.riena.ui.swt;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.ui.swt.lnf.IgnoreLnFUpdater;
import org.eclipse.riena.ui.swt.lnf.LnFUpdater;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

/**
 * This composite presents a list of single or multiple choices. It is mapped to
 * a {@link org.eclipse.riena.ui.ridgets.ISingleChoiceRidget} or
 * {@link org.eclipse.riena.ui.ridgets.IMultipleChoiceRidget}.
 */
@IgnoreLnFUpdater("background")
public class ChoiceComposite extends Composite {

	private final boolean isMulti;

	private int marginHeight;
	private int marginWidth;
	private int vSpacing;
	private int hSpacing = 3;
	private int orientation;

	/**
	 * Create a new ChoiceComposite instance given its parent and style value.
	 * The default orientation is <tt>SWT.VERTICAL</tt> (see also
	 * {@link #setOrientation(int)}). Multiple selection is allowed (=check
	 * boxes).
	 * 
	 * @param parent
	 *            the parent Composite (non-null)
	 * @param style
	 *            the SWT style of the Composite
	 */
	public ChoiceComposite(final Composite parent, final int style) {
		this(parent, style, true);
	}

	/**
	 * Create a new ChoiceComposite instance given its parent and style value.
	 * The default orientation is <tt>SWT.VERTICAL</tt> (see also
	 * {@link #setOrientation(int)}).
	 * 
	 * @param parent
	 *            the parent Composite (non-null)
	 * @param style
	 *            the SWT style of the Composite
	 * @param multipleSelection
	 *            true to allow multiple selection (=check boxes), false for
	 *            single selection (=radio buttons)
	 */
	public ChoiceComposite(final Composite parent, final int style, final boolean multipleSelection) {
		super(parent, style);
		this.isMulti = multipleSelection;
		this.orientation = SWT.VERTICAL;
		applyLayout();
		setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		LnFUpdater.addControlsAfterBind(this.getClass());
	}

	/**
	 * Return the margins, in pixels, for the top/bottom, left/right edges of
	 * the widget.
	 * 
	 * @return a Point; never null. The x value corresponds to the top/bottom
	 *         margin. The y value corresponds to the left/right margin.
	 * @since 2.1
	 */
	public Point getMargins() {
		return new Point(marginHeight, marginWidth);
	}

	/**
	 * Returns the orientation of this ChoiceComposite.
	 * 
	 * @return one of <tt>SWT.VERTICAL</tt> or <tt>SWT.HORIZONTAL</tt>.
	 */
	public int getOrientation() {
		return orientation;
	}

	/**
	 * Return the spacing, in pixels, for the right/left and top/bottom edgets
	 * of the cells within the widget.
	 * 
	 * @return a Point; never null. The x value corresponds to the right/left
	 *         spacing. The y value corresponds to the top/bottom spacing.
	 * @since 2.1
	 */
	public Point getSpacing() {
		return new Point(hSpacing, vSpacing);
	}

	/**
	 * Returns true if this instance allows multiple selection, false otherwise.
	 */
	public final boolean isMultipleSelection() {
		return isMulti;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The value will be propagated to children of this composite.
	 */
	@Override
	public final void setBackground(final Color color) {
		setRedraw(false);
		try {
			super.setBackground(color);
			for (final Control child : getChildren()) {
				child.setBackground(color);
			}
		} finally {
			setRedraw(true);
		}
	}

	@Override
	public final void setEnabled(final boolean enabled) {
		setRedraw(false);
		try {
			super.setEnabled(enabled);
			for (final Control child : getChildren()) {
				child.setEnabled(enabled);
			}
		} finally {
			setRedraw(true);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The value will be propagated to children of this composite.
	 */
	@Override
	public final void setForeground(final Color color) {
		setRedraw(false);
		try {
			super.setForeground(color);
			for (final Control child : getChildren()) {
				child.setForeground(color);
			}
		} finally {
			setRedraw(true);
		}
	}

	/**
	 * Sets the margin for the top/bottom, left/right edges of the widget.
	 * 
	 * @param marginHeight
	 *            the margin, in pixels, that will be placed along the top and
	 *            bottom edges of the widget. The default value is 0.
	 * @param marginWidth
	 *            the margin, in pixels, that will be placed along the left and
	 *            right edges of the widget. The default value is 0.
	 * @since 2.1
	 */
	public void setMargins(final int marginHeight, final int marginWidth) {
		Assert.isLegal(marginHeight >= 0, "marginHeight must be greater or equal to zero: " + marginHeight); //$NON-NLS-1$
		Assert.isLegal(marginWidth >= 0, "marginWidth must be greater or equal to zero: " + marginWidth); //$NON-NLS-1$
		this.marginHeight = marginHeight;
		this.marginWidth = marginWidth;
		applyLayout();
	}

	/**
	 * Sets the orientation (vertical or horizontal) of the choices in this
	 * composite.
	 * 
	 * @param orientation
	 *            <tt>SWT.VERTICAL</tt> for vertical orientation or
	 *            <tt>SWT.HORIZONTAL</tt> for horizontal orientation
	 * @throws RuntimeException
	 *             if orientation has an unsupported value
	 */
	public final void setOrientation(final int orientation) {
		Assert.isLegal(orientation == SWT.VERTICAL || orientation == SWT.HORIZONTAL);
		if (this.orientation != orientation) {
			this.orientation = orientation;
			applyLayout();
		}
	}

	/**
	 * Sets the spacing for the right/left and top/bottom edges of the cells
	 * within the widget.
	 * <p>
	 * Implementation note: only one of the two values will be used depending on
	 * the orientation (horizontal or vertical &ndash; see
	 * {@link #setOrientation(int)}).
	 * 
	 * @param hSpacing
	 *            the space, in pixels, between the right edge of a cell and the
	 *            left edge of the cell to the left. The default value is 3.
	 * @param vSpacing
	 *            the space, in pixels, between the bottom edge of a cell and
	 *            the top edge of the cell underneath. The default value is 0.
	 * @since 2.1
	 */
	public void setSpacing(final int hSpacing, final int vSpacing) {
		Assert.isLegal(hSpacing >= 0, "hSpacing must be greater or equal to zero: " + hSpacing); //$NON-NLS-1$
		Assert.isLegal(vSpacing >= 0, "vSpacing must be greater or equal to zero: " + vSpacing); //$NON-NLS-1$
		this.hSpacing = hSpacing;
		this.vSpacing = vSpacing;
		applyLayout();
	}

	// helping methods
	// ////////////////

	private void applyLayout() {
		if (orientation == SWT.VERTICAL) {
			final FillLayout layout = new FillLayout(SWT.VERTICAL);
			layout.marginHeight = marginHeight;
			layout.marginWidth = marginWidth;
			layout.spacing = vSpacing;
			setLayout(layout);
		} else {
			final RowLayout layout = new RowLayout(SWT.HORIZONTAL);
			layout.marginHeight = marginHeight;
			layout.marginWidth = marginWidth;
			layout.marginLeft = 0;
			layout.marginRight = 0;
			layout.marginTop = 0;
			layout.marginBottom = 0;
			layout.spacing = hSpacing;
			layout.wrap = false;
			setLayout(layout);
		}
	}

}
