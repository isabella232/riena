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
package org.eclipse.riena.example.client.navigation.model;

import org.eclipse.riena.example.client.application.IExampleIcons;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNodeId;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.NavigationNodeId;
import org.eclipse.riena.navigation.model.SubApplicationNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManager;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManagerAccessor;

public class NavigationSubApplicationNodeBuilder extends NavigationNodeBuilder {

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeBuilder#buildNode(org.eclipse.riena.navigation.INavigationNodeId)
	 */
	public INavigationNode<?> buildNode(INavigationNodeId navigationNodeId) {
		SwtPresentationManager presentation = SwtPresentationManagerAccessor.getManager();

		ISubApplicationNode subApplication = new SubApplicationNode(navigationNodeId, "Navigation"); //$NON-NLS-1$
		subApplication.setIcon(createIconPath(IExampleIcons.ICON_APPLICATION));
		presentation.present(subApplication, "subapplication.1"); //$NON-NLS-1$
		subApplication.setSelected(true);

		IModuleGroupNode moduleGroup = new ModuleGroupNode(null, "Group 1.1"); //$NON-NLS-1$
		subApplication.addChild(moduleGroup);
		IModuleNode module = new ModuleNode(null, "Module 1.1.1"); //$NON-NLS-1$
		module.setIcon(createIconPath(IExampleIcons.ICON_APPLICATION));
		moduleGroup.addChild(module);
		ISubModuleNode subModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.customerDetail"), "SubModule 1.1.1.1"); //$NON-NLS-1$ //$NON-NLS-2$
		subModule.setIcon(createIconPath(IExampleIcons.ICON_FILE));
		module.addChild(subModule);

		ISubModuleNode subModule2 = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.customerDetail"), "SubModule 1.1.1.1"); //$NON-NLS-1$ //$NON-NLS-2$
		subModule.addChild(subModule2);

		subModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.customerDetail"), "SubModule 1.1.1.2"); //$NON-NLS-1$ //$NON-NLS-2$
		module.addChild(subModule);
		module = new ModuleNode(null, "Module 1.1.2 (closeable)"); //$NON-NLS-1$
		module.setIcon(createIconPath(IExampleIcons.ICON_HOMEFOLDER));
		moduleGroup.addChild(module);
		subModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.customerDetail"), "SubModule 1.1.2.1"); //$NON-NLS-1$ //$NON-NLS-2$
		module.addChild(subModule);
		/* NEW */
		subModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.navigation"), "Navigation"); //$NON-NLS-1$ //$NON-NLS-2$
		module.addChild(subModule);

		moduleGroup = new ModuleGroupNode(null, "Group 1.2"); //$NON-NLS-1$
		moduleGroup.setPresentWithSingleModule(false);
		subApplication.addChild(moduleGroup);
		module = new ModuleNode(null, "Module 1.2.1 (not closeable)"); //$NON-NLS-1$
		module.setCloseable(false);
		module.setIcon(createIconPath(IExampleIcons.ICON_RED_LED));
		moduleGroup.addChild(module);
		subModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.customerDetail"), "SubModule 1.2.1.1"); //$NON-NLS-1$ //$NON-NLS-2$
		module.addChild(subModule);
		subModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.customerDetail"), "SubModule 1.2.1.2"); //$NON-NLS-1$ //$NON-NLS-2$
		module.addChild(subModule);

		return subApplication;
	}

}
