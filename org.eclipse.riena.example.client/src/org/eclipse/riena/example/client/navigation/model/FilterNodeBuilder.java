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

import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNodeBuilder;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubModuleNode;

/**
 *
 */
public class FilterNodeBuilder implements INavigationNodeBuilder {

	public INavigationNode<?> buildNode(NavigationNodeId navigationNodeId, NavigationArgument navigationArgument) {

		IModuleGroupNode moduleGroup = new ModuleGroupNode(navigationNodeId);

		IModuleNode filtersModule = new ModuleNode(null, "UI-Filters"); //$NON-NLS-1$
		moduleGroup.addChild(filtersModule);

		ISubModuleNode filterRidgetSubModule = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.example.filterridget"), "UI-Filter (Ridgets)"); //$NON-NLS-1$ //$NON-NLS-2$
		filtersModule.addChild(filterRidgetSubModule);

		ISubModuleNode filterNavigationSubModule = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.example.filternavigation"), "UI-Filter (Navigation)"); //$NON-NLS-1$ //$NON-NLS-2$
		filtersModule.addChild(filterNavigationSubModule);

		ISubModuleNode filterExternalDefinitionSubModule = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.example.filterdefinition"), "UI-Filter (External definition)"); //$NON-NLS-1$ //$NON-NLS-2$
		filtersModule.addChild(filterExternalDefinitionSubModule);

		ISubModuleNode filterActionSubModule = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.example.filteraction"), "UI-Filter (MenuItem)"); //$NON-NLS-1$ //$NON-NLS-2$
		filtersModule.addChild(filterActionSubModule);

		return moduleGroup;
	}

}
