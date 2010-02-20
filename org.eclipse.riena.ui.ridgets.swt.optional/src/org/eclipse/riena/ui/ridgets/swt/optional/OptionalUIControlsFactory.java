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
package org.eclipse.riena.ui.ridgets.swt.optional;

import org.eclipse.swt.nebula.widgets.compositetable.CompositeTable;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * TODO [ev] docs
 * 
 * @wbp.factory
 * @since 2.0
 */
public final class OptionalUIControlsFactory extends UIControlsFactory {

	private OptionalUIControlsFactory() {
		// static helper class
	}

	/**
	 * @wbp.factory.parameter.source style org.eclipse.swt.SWT.NONE
	 */
	public static CompositeTable createCompositeTable(Composite parent, int style) {
		CompositeTable result = new CompositeTable(parent, style);
		result.setBackground(SHARED_BG_COLOR);
		return result;
	}

}