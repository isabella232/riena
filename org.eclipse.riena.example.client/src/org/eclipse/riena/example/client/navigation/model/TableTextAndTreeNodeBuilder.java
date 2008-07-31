/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2008 compeople AG                         *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.example.client.navigation.model;

import org.eclipse.riena.example.client.views.TableSubModuleView;
import org.eclipse.riena.example.client.views.TextSubModuleView;
import org.eclipse.riena.example.client.views.TreeSubModuleView;
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
public class TableTextAndTreeNodeBuilder implements INavigationNodeBuilder {

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeBuilder#buildNode()
	 */
	public IModuleGroupNode buildNode(INavigationNodeId presentationId) {
		ModuleGroupNode node = new ModuleGroupNode("Table,Text&Tree");
		node.setPresentationId(presentationId);
		IModuleNode module = new ModuleNode("Table,Text&Tree");
		node.addChild(module);
		SubModuleNode subModule = new SubModuleNode("Table");
		// TODO get presentation via presentationId from extension point
		// subModule.setPresentationId("child1");
		SwtPresentationManagerAccessor.getManager().present(subModule, TableSubModuleView.ID);
		module.addChild(subModule);
		subModule = new SubModuleNode("Text");
		// TODO get presentation via presentationId from extension point
		// subModule.setPresentationId("child2");
		SwtPresentationManagerAccessor.getManager().present(subModule, TextSubModuleView.ID);
		module.addChild(subModule);
		subModule = new SubModuleNode("Tree");
		// TODO get presentation via presentationId from extension point
		// subModule.setPresentationId("child3");
		SwtPresentationManagerAccessor.getManager().present(subModule, TreeSubModuleView.ID);
		module.addChild(subModule);
		return node;
	}
}
