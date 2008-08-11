/*******************************************************************************
 * Copyright (c) 2008 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.core.runtime.Assert;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * TODO [ev] docs
 */
public class ChoiceComposite extends Composite {

	private final boolean isMulti;
	private int orientation;

	/**
	 * TODO [ev] docs
	 * 
	 * @param parent
	 * @param style
	 * @param multipleSelection
	 */
	public ChoiceComposite(Composite parent, int style, boolean multipleSelection) {
		super(parent, style);
		this.isMulti = multipleSelection;
		this.orientation = SWT.VERTICAL;
		applyOrientation();
		setBackground(LnfManager.getLnf().getColor(ILnfKeyConstants.SUB_MODULE_BACKGROUND));
	}

	/** TODO [ev] DOCS */
	public final boolean isMultipleSelection() {
		return isMulti;
	}

	@Override
	public final void setBackground(Color color) {
		setRedraw(false);
		try {
			super.setBackground(color);
			for (Control child : getChildren()) {
				child.setBackground(color);
			}
		} finally {
			setRedraw(true);
		}

	}

	@Override
	public final void setForeground(Color color) {
		setRedraw(false);
		try {
			super.setForeground(color);
			for (Control child : getChildren()) {
				child.setForeground(color);
			}
		} finally {
			setRedraw(true);
		}
	}

	/**
	 * TODO [ev] docs
	 */
	public final void setOrientation(int orientation) {
		Assert.isLegal(orientation == SWT.VERTICAL || orientation == SWT.HORIZONTAL);
		if (this.orientation != orientation) {
			this.orientation = orientation;
			applyOrientation();
		}
	}

	// helping methods
	// ////////////////

	private void applyOrientation() {
		if (orientation == SWT.VERTICAL) {
			setLayout(new FillLayout(SWT.VERTICAL));
		} else {
			RowLayout layout = new RowLayout(SWT.HORIZONTAL);
			layout.marginLeft = 0;
			layout.marginRight = 0;
			layout.marginTop = 0;
			layout.marginBottom = 0;
			layout.wrap = false;
			setLayout(layout);
		}
	}

}
