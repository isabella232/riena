/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2008 compeople AG                         *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.example.client.navigation.model;

import org.eclipse.riena.example.client.views.FocusableView;
import org.eclipse.riena.example.client.views.MarkerView;
import org.eclipse.riena.example.client.views.TreeView;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNodeId;
import org.eclipse.riena.navigation.INavigationNodeBuilder;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManagerAccessor;

/**
 *
 */
public class Open3NavigationNodeBuilder implements INavigationNodeBuilder {

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeBuilder#buildNode()
	 */
	public IModuleGroupNode buildNode(INavigationNodeId presentationId) {
		ModuleGroupNode node = new ModuleGroupNode("New Group");
		node.setPresentationId(presentationId);
		IModuleNode module = new ModuleNode("New Module");
		node.addChild(module);
		SubModuleNode subModule = new SubModuleNode("New SubModule 1");
		// TODO get presentation via presentationId from extension point
		// subModule.setPresentationId("child1");
		SwtPresentationManagerAccessor.getManager().present(subModule, FocusableView.ID);
		module.addChild(subModule);
		subModule = new SubModuleNode("New SubModule 2");
		// TODO get presentation via presentationId from extension point
		// subModule.setPresentationId("child2");
		SwtPresentationManagerAccessor.getManager().present(subModule, MarkerView.ID);
		module.addChild(subModule);
		subModule = new SubModuleNode("New SubModule 3");
		// TODO get presentation via presentationId from extension point
		// subModule.setPresentationId("child3");
		SwtPresentationManagerAccessor.getManager().present(subModule, TreeView.ID);
		module.addChild(subModule);
		return node;
	}

}
