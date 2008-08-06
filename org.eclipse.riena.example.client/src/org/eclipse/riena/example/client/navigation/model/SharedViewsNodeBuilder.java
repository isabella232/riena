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
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNodeId;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.NavigationNodeId;
import org.eclipse.riena.navigation.model.SubModuleNode;

public class SharedViewsNodeBuilder extends NavigationNodeBuilder {

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeBuilder#buildNode(org.eclipse.riena.navigation.INavigationNodeId)
	 */
	public INavigationNode<?> buildNode(INavigationNodeId navigationNodeId) {
		ModuleGroupNode moduleGroup = new ModuleGroupNode("Shared View Demo"); //$NON-NLS-1$
		moduleGroup.setPresentationId(navigationNodeId);

		IModuleNode sharedViewModule = new ModuleNode("Shared View Demo"); //$NON-NLS-1$
		sharedViewModule.setIcon(createIconPath(IExampleIcons.ICON_SAMPLE));
		moduleGroup.addChild(sharedViewModule);

		SubModuleNode sharedViewSm1 = new SubModuleNode("Node 1"); //$NON-NLS-1$
		sharedViewSm1.setPresentationId(new NavigationNodeId("org.eclipse.riena.example.sharedView")); //$NON-NLS-1$
		sharedViewModule.addChild(sharedViewSm1);

		SubModuleNode sharedViewSm2 = new SubModuleNode("Node 2"); //$NON-NLS-1$
		sharedViewSm2.setPresentationId(new NavigationNodeId("org.eclipse.riena.example.sharedView")); //$NON-NLS-1$
		sharedViewModule.addChild(sharedViewSm2);
		return moduleGroup;
	}

}
