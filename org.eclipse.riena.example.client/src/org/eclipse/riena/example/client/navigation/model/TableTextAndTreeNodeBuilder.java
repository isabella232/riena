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
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.NavigationNodeId;
import org.eclipse.riena.navigation.model.SubModuleNode;

/**
 *
 */
public class TableTextAndTreeNodeBuilder implements INavigationNodeBuilder {

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeBuilder#buildNode()
	 */
	public IModuleGroupNode buildNode(INavigationNodeId presentationId) {
		ModuleGroupNode node = new ModuleGroupNode("Table,Text&Tree"); //$NON-NLS-1$
		node.setPresentationId(presentationId);
		IModuleNode module = new ModuleNode("Table,Text&Tree"); //$NON-NLS-1$
		node.addChild(module);
		SubModuleNode subModule = new SubModuleNode("Table"); //$NON-NLS-1$
		subModule.setPresentationId(new NavigationNodeId("org.eclipse.riena.example.table")); //$NON-NLS-1$
		module.addChild(subModule);
		subModule = new SubModuleNode("Text"); //$NON-NLS-1$
		subModule.setPresentationId(new NavigationNodeId("org.eclipse.riena.example.text")); //$NON-NLS-1$
		module.addChild(subModule);
		subModule = new SubModuleNode("Tree"); //$NON-NLS-1$
		subModule.setPresentationId(new NavigationNodeId("org.eclipse.riena.example.tree")); //$NON-NLS-1$
		module.addChild(subModule);
		return node;
	}
}
