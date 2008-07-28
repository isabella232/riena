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
package org.eclipse.riena.example.client.views;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.riena.example.client.controllers.NavigateSubModuleViewController;
import org.eclipse.riena.internal.example.client.utils.UIControlsFactory;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.swt.binding.DefaultSwtControlRidgetMapper;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleNodeView;
import org.eclipse.riena.ui.ridgets.uibinding.IBindingManager;
import org.eclipse.riena.ui.ridgets.uibinding.InjectAllAtOnceBindingManager;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

/**
 * SWT {@link IComboRidget} sample.
 */
public class NavigateSubModuleView extends SubModuleNodeView<NavigateSubModuleViewController> {

	public static final String ID = NavigateSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		parent.setLayout(new GridLayout(1, false));
		GridDataFactory fillFactory = GridDataFactory.fillDefaults();

		UIControlsFactory.createLabel(parent, "Navigate To:"); //$NON-NLS-1$
		Text target = UIControlsFactory.createText(parent);
		fillFactory.applyTo(target);
		addUIControl(target, "target"); //$NON-NLS-1$

		Button navigate = UIControlsFactory.createButton(parent);
		fillFactory.applyTo(navigate);
		addUIControl(navigate, "navigate"); //$NON-NLS-1$
	}

	@Override
	protected NavigateSubModuleViewController createController(ISubModuleNode subModuleNode) {
		return new NavigateSubModuleViewController(subModuleNode);
	}

	protected IBindingManager createBindingManager() {
		return new InjectAllAtOnceBindingManager(new SWTBindingPropertyLocator(), new DefaultSwtControlRidgetMapper());
	}

}
