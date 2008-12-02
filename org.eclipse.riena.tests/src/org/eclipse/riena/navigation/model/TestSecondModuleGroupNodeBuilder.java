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
package org.eclipse.riena.navigation.model;

import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationAssembler;
import org.eclipse.riena.navigation.INavigationAssemblyExtension;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.NavigationNodeId;

public class TestSecondModuleGroupNodeBuilder implements INavigationAssembler {

	private INavigationAssemblyExtension assembly;

	/**
	 * @see org.eclipse.riena.navigation.INavigationAssembler#getAssembly()
	 */
	public INavigationAssemblyExtension getAssembly() {
		return assembly;
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationAssembler#setAssembly(org.eclipse.riena.navigation.INavigationAssemblyExtension)
	 */
	public void setAssembly(INavigationAssemblyExtension nodeDefinition) {
		assembly = nodeDefinition;
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationAssembler#buildNode(org.eclipse.riena.navigation.NavigationNodeId,
	 *      org.eclipse.riena.navigation.NavigationArgument)
	 */
	public INavigationNode<?> buildNode(NavigationNodeId navigationNodeId, NavigationArgument navigationArgument) {
		IModuleGroupNode moduleGroup = new ModuleGroupNode(navigationNodeId);
		IModuleNode module = new ModuleNode(
				new NavigationNodeId("org.eclipse.riena.navigation.model.test.secondModule"));
		moduleGroup.addChild(module);
		ISubModuleNode subModule = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.navigation.model.test.secondSubModule"));
		module.addChild(subModule);
		return moduleGroup;
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationAssembler#acceptsTargetId(String)
	 */
	public boolean acceptsToBuildNode(NavigationNodeId nodeId, NavigationArgument argument) {

		return nodeId.getTypeId().equals("org.eclipse.riena.navigation.model.test.secondModuleGroup")
				|| nodeId.getTypeId().equals("org.eclipse.riena.navigation.model.test.secondModule")
				|| nodeId.getTypeId().equals("org.eclipse.riena.navigation.model.test.secondSubModule");
	}

	public String getParentNodeId() {
		return "application";
	}
}
