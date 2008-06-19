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
import org.eclipse.riena.navigation.ui.swt.component.SubApplicationNavigationComponent;
import org.eclipse.riena.navigation.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.navigation.ui.swt.lnf.LnfManager;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManagerAccessor;
import org.eclipse.riena.navigation.ui.swt.presentation.stack.TitlelessStackPresentation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * This view contains the navigation of the current sub-application.
 */
public class NavigationTreeViewPart extends ViewPart {

	public final static String ID = "org.eclipse.riena.navigation.ui.navigationtree"; //$NON-NLS-1$

	private SubApplicationNavigationComponent subApplicationComponent;

	/**
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {

		// set a property to distinguish this view
		setPartProperty(TitlelessStackPresentation.PROPERTY_NAVIGATION, String.valueOf(Boolean.TRUE));

		ISubApplication subApplicationNode = getSubApplication();
		Composite baseComposite = initLayoutParts(parent);
		subApplicationComponent = new SubApplicationNavigationComponent(subApplicationNode, baseComposite);

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

	private Composite initLayoutParts(Composite parent) {
		Composite c = new Composite(parent, SWT.None);
		c.setBackground(LnfManager.getLnf().getColor(ILnfKeyConstants.NAVIGATION_BACKGROUND));
		c.setLayout(new FillLayout());
		return c;
	}

	/**
	 * This method does nothing, because the view of the navigation is not
	 * "focusable".
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// this view is not "focusable"
	}

	private SubApplicationNavigationComponent getSubApplicationComponent() {
		return subApplicationComponent;
	}

	/**
	 * Rebuilds the tree items of the sub-modules.
	 */
	public void rebuild() {
		getSubApplicationComponent().rebuild();
	}

}
