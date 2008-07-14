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
package org.eclipse.riena.ui.swt;

import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Composite of the status bar.
 */
public abstract class AbstractStatusbarComposite extends Composite {

	private static final int LEFT_RIGHT_MARGIN = 5;
	private static final int TOP_BOTTOM_MARGIN = 5;

	/**
	 * @param parent
	 * @param style
	 */
	public AbstractStatusbarComposite(Composite parent, int style) {
		super(parent, style);
		setContentsLayout();
		createContents();
	}

	/**
	 * Creates the contents of a composite of the status bar.
	 */
	protected abstract void createContents();

	/**
	 * Sets the layout.
	 */
	protected void setContentsLayout() {

		RowLayout rowLayout = new RowLayout();
		rowLayout.justify = false;
		rowLayout.marginLeft = getLeftMargin();
		rowLayout.marginTop = getTopMargin();
		rowLayout.marginRight = getRightMargin();
		rowLayout.marginBottom = getBottomMargin();
		rowLayout.pack = true;
		setLayout(rowLayout);

	}

	protected int getTopMargin() {
		return TOP_BOTTOM_MARGIN;
	}

	protected int getBottomMargin() {
		return TOP_BOTTOM_MARGIN;
	}

	protected int getLeftMargin() {
		return LEFT_RIGHT_MARGIN;
	}

	protected int getRightMargin() {
		return LEFT_RIGHT_MARGIN;
	}

}
