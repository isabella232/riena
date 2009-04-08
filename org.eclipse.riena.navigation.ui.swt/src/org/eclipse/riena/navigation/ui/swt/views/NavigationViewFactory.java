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

import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ui.controllers.ModuleController;
import org.eclipse.riena.navigation.ui.controllers.ModuleGroupController;
import org.eclipse.riena.navigation.ui.swt.views.desc.IModuleDesc;
import org.eclipse.riena.navigation.ui.swt.views.desc.IModuleGroupDesc;

/**
 * Factory to create (sub-)views of the navigation view.
 */
public class NavigationViewFactory implements IViewFactory {

	private IModuleDesc moduleDesc;
	private IModuleGroupDesc moduleGroupDesc;

	public void update(IModuleDesc moduleView) {
		this.moduleDesc = moduleView;
	}

	public void update(IModuleGroupDesc moduleGroup) {
		this.moduleGroupDesc = moduleGroup;
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.IViewFactory#createModuleGroupView(org.eclipse.swt.widgets.Composite)
	 */
	public ModuleGroupView createModuleGroupView(Composite parent) {
		if (moduleGroupDesc != null) {
			Class<ModuleGroupView> moduleGroupClazz = moduleGroupDesc.getView();
			if (moduleGroupClazz != null) {
				try {
					Constructor<ModuleGroupView> constructor = moduleGroupClazz.getConstructor(Composite.class,
							int.class);
					return constructor.newInstance(parent, SWT.NONE);
				} catch (Exception e) {
					throw new RuntimeException("Instantiating the configured ModuleGroupView " + moduleGroupClazz //$NON-NLS-1$
							+ " failed.", e); //$NON-NLS-1$
				}
			}
		}
		return new ModuleGroupView(parent, SWT.None);
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.IViewFactory#createModuleGroupController(org.eclipse.riena.navigation.IModuleGroupNode)
	 */
	public ModuleGroupController createModuleGroupController(IModuleGroupNode moduleGroupNode) {
		if (moduleGroupDesc != null) {
			Class<ModuleGroupController> moduleGroupClazz = moduleGroupDesc.getController();
			if (moduleGroupClazz != null) {
				try {
					Constructor<ModuleGroupController> constructor = moduleGroupClazz
							.getConstructor(IModuleGroupNode.class);
					return constructor.newInstance(moduleGroupNode);
				} catch (Exception e) {
					throw new RuntimeException("Instantiating the configured ModuleGroupController " + moduleGroupClazz //$NON-NLS-1$
							+ " failed.", e); //$NON-NLS-1$
				}
			}
		}
		return new ModuleGroupController(moduleGroupNode);
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.IViewFactory#createModuleView(org.eclipse.swt.widgets.Composite)
	 */
	public ModuleView createModuleView(Composite parent) {
		if (moduleDesc != null) {
			Class<ModuleView> moduleViewClazz = moduleDesc.getView();
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

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.IViewFactory#createModuleController(org.eclipse.riena.navigation.IModuleNode)
	 */
	public ModuleController createModuleController(IModuleNode moduleNode) {
		if (moduleDesc != null) {
			Class<ModuleController> moduleViewClazz = moduleDesc.getController();
			if (moduleViewClazz != null) {
				try {
					Constructor<ModuleController> constructor = moduleViewClazz.getConstructor(IModuleNode.class);
					return constructor.newInstance(moduleNode);
				} catch (Exception e) {
					throw new RuntimeException("Instantiating the configured ModuleController " + moduleViewClazz //$NON-NLS-1$
							+ " failed.", e); //$NON-NLS-1$
				}
			}
		}
		return new SWTModuleController(moduleNode);
	}

}
