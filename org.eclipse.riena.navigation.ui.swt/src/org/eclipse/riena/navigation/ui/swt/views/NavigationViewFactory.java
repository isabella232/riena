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

import java.lang.reflect.Constructor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.navigation.ui.swt.views.desc.IModuleGroupViewDesc;
import org.eclipse.riena.navigation.ui.swt.views.desc.IModuleViewDesc;

/**
 * Factory to create (sub-)views of the navigation view.
 */
public class NavigationViewFactory implements IViewFactory {

	private IModuleViewDesc moduleView;
	private IModuleGroupViewDesc moduleGroupView;

	public void update(IModuleViewDesc moduleView) {
		this.moduleView = moduleView;
	}

	public void update(IModuleGroupViewDesc moduleGroup) {
		this.moduleGroupView = moduleGroup;
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.IViewFactory#createModuleGroupView(org.eclipse.swt.widgets.Composite)
	 */
	public ModuleGroupView createModuleGroupView(Composite parent) {
		if (moduleGroupView != null) {
			Class<ModuleGroupView> moduleGroupClazz = moduleGroupView.getModuleGroupView();
			try {
				Constructor<ModuleGroupView> constructor = moduleGroupClazz.getConstructor(Composite.class, int.class);
				return constructor.newInstance(parent, SWT.NONE);
			} catch (Exception e) {
				throw new RuntimeException("Instantiating the configured ModuleGroupView " + moduleGroupClazz //$NON-NLS-1$
						+ " failed.", e); //$NON-NLS-1$
			}
		}
		return new ModuleGroupView(parent, SWT.None);
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.IViewFactory#createModuleView(org.eclipse.swt.widgets.Composite)
	 */
	public ModuleView createModuleView(Composite parent) {
		if (moduleView != null) {
			Class<ModuleView> moduleViewClazz = moduleView.getModuleView();
			try {
				Constructor<ModuleView> constructor = moduleViewClazz.getConstructor(Composite.class);
				return constructor.newInstance(parent);
			} catch (Exception e) {
				throw new RuntimeException("Instantiating the configured ModuleView " + moduleViewClazz //$NON-NLS-1$
						+ " failed.", e); //$NON-NLS-1$
			}
		}
		return new ModuleView(parent);
	}

}
