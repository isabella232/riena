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

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 *
 */
public class DynamicSubmoduleView extends SubModuleView {
	public static final String BINDING_ID_LABEL = "nodeLabel"; //$NON-NLS-1$
	public static final String BINDING_ID_BUTTON = "bindingIdButton"; //$NON-NLS-1$

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setLayout(new FillLayout());
		UIControlsFactory.createLabel(parent, "Child node", BINDING_ID_LABEL); //$NON-NLS-1$
		UIControlsFactory.createButton(parent, "Dispose this view", BINDING_ID_BUTTON); //$NON-NLS-1$
	}
}