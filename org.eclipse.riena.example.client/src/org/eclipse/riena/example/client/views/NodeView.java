/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.example.client.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 *
 */
public class NodeView extends SubModuleView {

	public static final String BINDING_ID_BUTTON = "bindingIdButton"; //$NON-NLS-1$
	public static final String BINDING_ID_DISPOSE = "bindingIdDispose"; //$NON-NLS-1$

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setLayout(new FillLayout());
		final Label lbl = new Label(parent, SWT.NONE);
		lbl.setText("Parent Node"); //$NON-NLS-1$

		UIControlsFactory.createButton(parent, "Create new submodule", BINDING_ID_BUTTON); //$NON-NLS-1$
		UIControlsFactory.createButton(parent, "Dispose me", BINDING_ID_DISPOSE); //$NON-NLS-1$
	}
}