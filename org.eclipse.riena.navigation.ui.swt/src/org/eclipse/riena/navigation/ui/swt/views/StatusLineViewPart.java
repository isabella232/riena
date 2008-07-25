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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.riena.navigation.ui.swt.presentation.stack.TitlelessStackPresentation;
import org.eclipse.riena.ui.swt.Statusbar;
import org.eclipse.riena.ui.swt.StatusbarSpacer;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 *
 */
public class StatusLineViewPart extends ViewPart {

	public final static String ID = "org.eclipse.riena.navigation.ui.swt.views.statusLineView"; //$NON-NLS-1$

	/**
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 *      .Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		setPartProperty(TitlelessStackPresentation.PROPERTY_STATUSLINE, String.valueOf(Boolean.TRUE));
		Statusbar statusLine = new Statusbar(parent, SWT.None, StatusbarSpacer.class);
		statusLine.setData(SWTBindingPropertyLocator.BINDING_PROPERTY, "statusbarRidget"); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// this view is not "focusable"
	}

}
