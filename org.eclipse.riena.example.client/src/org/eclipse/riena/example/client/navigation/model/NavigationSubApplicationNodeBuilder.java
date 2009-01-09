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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.riena.example.client.application.ExampleIcons;
import org.eclipse.riena.example.client.controllers.CustomerDetailSubModuleController;
import org.eclipse.riena.example.client.controllers.NavigationSubModuleController;
import org.eclipse.riena.example.client.views.CustomerDetailSubModuleView;
import org.eclipse.riena.example.client.views.NavigationSubModuleView;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubApplicationNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.ui.workarea.WorkareaManager;

public class NavigationSubApplicationNodeBuilder extends NavigationNodeBuilder {

	private Set<String> knownTargetIds = null;

	/**
	 * @see org.eclipse.riena.navigation.INavigationAssembler#buildNode(org.eclipse.riena.navigation.NavigationNodeId,
	 *      org.eclipse.riena.navigation.NavigationArgument)
	 */
	public INavigationNode<?> buildNode(NavigationNodeId navigationNodeId, NavigationArgument navigationArgument) {

		ISubApplicationNode subApplication = new SubApplicationNode(new NavigationNodeId(
				"org.eclipse.riena.example.navigation.subapplication"), "Navigation"); //$NON-NLS-1$ //$NON-NLS-2$
		subApplication.setIcon(createIconPath(ExampleIcons.ICON_APPLICATION));
		WorkareaManager.getInstance().registerDefinition(subApplication, "subapplication.1", false); //$NON-NLS-1$
		subApplication.setSelected(true);

		IModuleGroupNode moduleGroup = new ModuleGroupNode(null);
		moduleGroup.setLabel("ModuleGroup 1.1"); //$NON-NLS-1$
		subApplication.addChild(moduleGroup);
		IModuleNode module = new ModuleNode(null, "Module 1.1.1"); //$NON-NLS-1$
		module.setIcon(createIconPath(ExampleIcons.ICON_APPLICATION));
		moduleGroup.addChild(module);
		ISubModuleNode subModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.customerDetail"), "SubModule 1.1.1.1"); //$NON-NLS-1$ //$NON-NLS-2$
		subModule.setIcon(createIconPath(ExampleIcons.ICON_FILE));
		WorkareaManager.getInstance().registerDefinition(subModule, CustomerDetailSubModuleController.class,
				CustomerDetailSubModuleView.ID, false);
		module.addChild(subModule);

		ISubModuleNode subModule2 = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.customerDetail"), "SubModule 1.1.1.1"); //$NON-NLS-1$ //$NON-NLS-2$
		WorkareaManager.getInstance().registerDefinition(subModule2, CustomerDetailSubModuleController.class,
				CustomerDetailSubModuleView.ID, false);
		subModule.addChild(subModule2);

		subModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.customerDetail"), "SubModule 1.1.1.2"); //$NON-NLS-1$ //$NON-NLS-2$
		WorkareaManager.getInstance().registerDefinition(subModule, CustomerDetailSubModuleController.class,
				CustomerDetailSubModuleView.ID, false);
		module.addChild(subModule);
		module = new ModuleNode(null, "Module 1.1.2 (closeable)"); //$NON-NLS-1$
		module.setIcon(createIconPath(ExampleIcons.ICON_HOMEFOLDER));
		moduleGroup.addChild(module);
		subModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.customerDetail"), "SubModule 1.1.2.1"); //$NON-NLS-1$ //$NON-NLS-2$
		WorkareaManager.getInstance().registerDefinition(subModule, CustomerDetailSubModuleController.class,
				CustomerDetailSubModuleView.ID, false);
		module.addChild(subModule);
		/* NEW */
		subModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.navigation"), "Navigation"); //$NON-NLS-1$ //$NON-NLS-2$
		WorkareaManager.getInstance().registerDefinition(subModule, NavigationSubModuleController.class,
				NavigationSubModuleView.ID, false);
		module.addChild(subModule);

		moduleGroup = new ModuleGroupNode(null);
		moduleGroup.setLabel("ModuleGroup 1.2"); //$NON-NLS-1$
		moduleGroup.setPresentWithSingleModule(false);
		subApplication.addChild(moduleGroup);
		module = new ModuleNode(null, "Module 1.2.1 (not closeable)"); //$NON-NLS-1$
		module.setClosable(false);
		module.setIcon(createIconPath(ExampleIcons.ICON_RED_LED));
		moduleGroup.addChild(module);
		subModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.customerDetail"), "SubModule 1.2.1.1"); //$NON-NLS-1$ //$NON-NLS-2$
		WorkareaManager.getInstance().registerDefinition(subModule, CustomerDetailSubModuleController.class,
				CustomerDetailSubModuleView.ID, false);
		module.addChild(subModule);
		subModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.customerDetail"), "SubModule 1.2.1.2"); //$NON-NLS-1$ //$NON-NLS-2$
		WorkareaManager.getInstance().registerDefinition(subModule, CustomerDetailSubModuleController.class,
				CustomerDetailSubModuleView.ID, false);
		module.addChild(subModule);

		return subApplication;
	}

	public String getParentNodeId() {
		return "application"; //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationAssembler#acceptsTargetId(String)
	 */
	public boolean acceptsToBuildNode(NavigationNodeId nodeId, NavigationArgument argument) {

		if (knownTargetIds == null) {
			knownTargetIds = new HashSet<String>(Arrays.asList("org.eclipse.riena.example.navigation.subapplication", //$NON-NLS-1$
					"org.eclipse.riena.example.customerDetail", //$NON-NLS-1$
					"org.eclipse.riena.example.customerDetail", //$NON-NLS-1$
					"org.eclipse.riena.example.customerDetail", //$NON-NLS-1$
					"org.eclipse.riena.example.customerDetail", //$NON-NLS-1$
					"org.eclipse.riena.example.navigation", //$NON-NLS-1$
					"org.eclipse.riena.example.customerDetail", //$NON-NLS-1$
					"org.eclipse.riena.example.customerDetail" //$NON-NLS-1$
			));
			knownTargetIds = Collections.unmodifiableSet(knownTargetIds);
		}

		return knownTargetIds.contains(nodeId.getTypeId());
	}
}
