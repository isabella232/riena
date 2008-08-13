/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2008 compeople AG                         *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.example.client.navigation.model;

import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNodeBuilder;
import org.eclipse.riena.navigation.INavigationNodeId;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.NavigationNodeId;
import org.eclipse.riena.navigation.model.SubModuleNode;

/**
 *
 */
public class ComboAndListNodeBuilder implements INavigationNodeBuilder {

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeBuilder#buildNode(org.eclipse.riena.navigation.INavigationNodeId,
	 *      org.eclipse.riena.navigation.NavigationArgument)
	 */
	public IModuleGroupNode buildNode(INavigationNodeId presentationId, NavigationArgument navigationArgument) {
		IModuleGroupNode node = new ModuleGroupNode(presentationId, "Combo&List"); //$NON-NLS-1$
		IModuleNode module = new ModuleNode(null, "Combo&List"); //$NON-NLS-1$
		node.addChild(module);
		ISubModuleNode subModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.combo"), "Combo"); //$NON-NLS-1$ //$NON-NLS-2$
		module.addChild(subModule);
		subModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.list"), "List"); //$NON-NLS-1$ //$NON-NLS-2$
		module.addChild(subModule);
		return node;
	}

}
