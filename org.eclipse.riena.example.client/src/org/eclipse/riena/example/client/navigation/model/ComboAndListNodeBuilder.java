/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2008 compeople AG                         *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.example.client.navigation.model;

import org.eclipse.riena.example.client.views.ComboSubModuleView;
import org.eclipse.riena.example.client.views.ListSubModuleView;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNodeBuilder;
import org.eclipse.riena.navigation.INavigationNodeId;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManagerAccessor;

/**
 *
 */
public class ComboAndListNodeBuilder implements INavigationNodeBuilder {

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeBuilder#buildNode()
	 */
	public IModuleGroupNode buildNode(INavigationNodeId presentationId) {
		ModuleGroupNode node = new ModuleGroupNode("Combo&List");
		node.setPresentationId(presentationId);
		IModuleNode module = new ModuleNode("Combo&List");
		node.addChild(module);
		SubModuleNode subModule = new SubModuleNode("Combo");
		// TODO get presentation via presentationId from extension point
		// subModule.setPresentationId("child1");
		SwtPresentationManagerAccessor.getManager().present(subModule, ComboSubModuleView.ID);
		module.addChild(subModule);
		subModule = new SubModuleNode("List");
		// TODO get presentation via presentationId from extension point
		// subModule.setPresentationId("child2");
		SwtPresentationManagerAccessor.getManager().present(subModule, ListSubModuleView.ID);
		module.addChild(subModule);
		return node;
	}

}
