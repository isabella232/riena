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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * Factory to create (sub-)views of the navigation view.
 */
public class NavigationViewFactory implements IViewFactory {

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.IViewFactory#createModuleGroupView(org.eclipse.swt.widgets.Composite)
	 */
	public ModuleGroupView createModuleGroupView(Composite parent) {
		return new ModuleGroupView(parent, SWT.None);
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.IViewFactory#createModuleView(org.eclipse.swt.widgets.Composite)
	 */
	public ModuleView createModuleView(Composite parent) {
		return new ModuleView(parent);
	}

}
