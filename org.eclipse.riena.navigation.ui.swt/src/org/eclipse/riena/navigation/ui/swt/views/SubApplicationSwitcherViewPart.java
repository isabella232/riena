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

import org.eclipse.riena.navigation.ISubApplication;
import org.eclipse.riena.navigation.model.ApplicationModel;
import org.eclipse.riena.navigation.ui.swt.component.SubApplicationSwitcherWidget;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManagerAccessor;
import org.eclipse.riena.navigation.ui.swt.presentation.stack.TitlelessStackPresentation;
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

	public final static String ID = "org.eclipse.riena.navigation.ui.swt.applicationSwicther"; //$NON-NLS-1$

	/**
	 * Adds the <code>SubApplicationSwitcherWidget</code> to the parent.
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(final Composite parent) {

		// set a property to distinguish this view
		setPartProperty(TitlelessStackPresentation.PROPERTY_SUB_APPLICATION_SWITCHER, Boolean.TRUE.toString());

		// add the widget SubApplicationSwitcherWidget
		parent.setLayout(new FillLayout());
		new SubApplicationSwitcherWidget(parent, SWT.NONE, getApplicationModel());

	}

	/**
	 * Returns the navigation node of the application.
	 * 
	 * @return application model
	 */
	private ApplicationModel getApplicationModel() {
		return getSubApplication().getParent().getTypecastedAdapter(ApplicationModel.class);
	}

	/**
	 * Returns the navigation node of the current sub-application.<br>
	 * The current sub-application depends on the current perspective.
	 * 
	 * @return sub-application
	 */
	private ISubApplication getSubApplication() {
		String perspectiveID = getViewSite().getPage().getPerspective().getId();
		return SwtPresentationManagerAccessor.getManager().getNavigationNode(perspectiveID, ISubApplication.class);
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
