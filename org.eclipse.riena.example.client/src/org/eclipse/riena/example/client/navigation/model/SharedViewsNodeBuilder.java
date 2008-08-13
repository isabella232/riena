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
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.NavigationNodeId;
import org.eclipse.riena.navigation.model.SubModuleNode;

public class SharedViewsNodeBuilder extends NavigationNodeBuilder {

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeBuilder#buildNode(org.eclipse.riena.navigation.INavigationNodeId,
	 *      org.eclipse.riena.navigation.NavigationArgument)
	 */
	public INavigationNode<?> buildNode(INavigationNodeId navigationNodeId, NavigationArgument navigationArgument) {
		IModuleGroupNode moduleGroup = new ModuleGroupNode(navigationNodeId, "Shared View Demo"); //$NON-NLS-1$

		IModuleNode sharedViewModule = new ModuleNode(null, "Shared View Demo"); //$NON-NLS-1$
		sharedViewModule.setIcon(createIconPath(IExampleIcons.ICON_SAMPLE));
		moduleGroup.addChild(sharedViewModule);

		ISubModuleNode sharedViewSm1 = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.example.sharedView", "1"), "Node 1"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		sharedViewModule.addChild(sharedViewSm1);

		ISubModuleNode sharedViewSm2 = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.example.sharedView", "2"), "Node 2"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		sharedViewModule.addChild(sharedViewSm2);
		return moduleGroup;
	}

}
