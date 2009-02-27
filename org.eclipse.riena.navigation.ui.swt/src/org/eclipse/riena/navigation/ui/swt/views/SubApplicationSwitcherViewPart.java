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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.riena.navigation.model.ApplicationNode;
import org.eclipse.riena.navigation.ui.swt.component.SubApplicationSwitcherWidget;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * View of the sub-application switcher.<br>
 * The only widget of the view is the <code>SubApplicationSwitcherWidget</code>.
 * This widgets fills the whole view.
 */
public class SubApplicationSwitcherViewPart extends ViewPart {

	public final static String ID = "org.eclipse.riena.navigation.ui.swt.views.subApplicationSwitcherView"; //$NON-NLS-1$

	private ApplicationNode node;

	public SubApplicationSwitcherViewPart(ApplicationNode node) {
		super();
		this.node = node;
	}

	/**
	 * Adds the <code>SubApplicationSwitcherWidget</code> to the parent.
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(final Composite parent) {

		// add the widget SubApplicationSwitcherWidget
		parent.setLayout(new FillLayout());
		new SubApplicationSwitcherWidget(parent, SWT.NONE, getApplicationModel());

	}

	/**
	 * Returns the navigation node of the application.
	 * 
	 * @return application model
	 */
	private ApplicationNode getApplicationModel() {
		return node;
	}

	/**
	 * This method does nothing, because the view of the sub-application
	 * switcher is not "focusable".
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// this view is not "focusable"
	}

}
