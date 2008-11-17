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

import org.eclipse.riena.navigation.IGenericNavigationAssembler;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleGroupNodeExtension;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.IModuleNodeExtension;
import org.eclipse.riena.navigation.INavigationAssemblyExtension;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ISubModuleNodeExtension;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.NavigationNodeId;

/**
 *
 */
public class GenericNavigationAssembler implements IGenericNavigationAssembler {

	// the node definition as read from extension point
	private INavigationAssemblyExtension nodeDefinition;

	public INavigationAssemblyExtension getNodeDefinition() {
		return nodeDefinition;
	}

	public void setNodeDefinition(INavigationAssemblyExtension nodeDefinition) {
		this.nodeDefinition = nodeDefinition;
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationAssembler#buildNode(org.eclipse.riena.navigation.NavigationNodeId,
	 *      org.eclipse.riena.navigation.NavigationArgument)
	 */
	public INavigationNode<?> buildNode(NavigationNodeId targetId, NavigationArgument navigationArgument) {

		if (nodeDefinition != null) {
			// build module group if it exists
			IModuleGroupNodeExtension groupDefinition = nodeDefinition.getModuleGroupNode();
			if (groupDefinition != null) {
				return build(groupDefinition, targetId);
			}
			// otherwise try module
			IModuleNodeExtension moduleDefinition = nodeDefinition.getModuleNode();
			if (moduleDefinition != null) {
				return build(moduleDefinition, targetId);
			}
			// last resort is submodule
			ISubModuleNodeExtension submoduleDefinition = nodeDefinition.getSubModuleNode();
			if (submoduleDefinition != null) {
				return build(submoduleDefinition, targetId);
			}
		}

		throw new ExtensionPointFailure(
				"'modulegroup', 'module' or 'submodule' element expected. ID=" + targetId.getTypeId()); //$NON-NLS-1$
	}

	protected IModuleGroupNode build(IModuleGroupNodeExtension groupDefinition, NavigationNodeId targetId) {

		// a module group can only contain modules
		IModuleGroupNode moduleGroup = new ModuleGroupNode(new NavigationNodeId(groupDefinition.getTypeId(), targetId
				.getInstanceId()));
		for (IModuleNodeExtension moduleDefinition : groupDefinition.getModuleNodes()) {
			moduleGroup.addChild(build(moduleDefinition, targetId));
		}

		return moduleGroup;
	}

	protected IModuleNode build(IModuleNodeExtension moduleDefinition, NavigationNodeId targetId) {

		// a module has a label...
		IModuleNode module = new ModuleNode(
				new NavigationNodeId(moduleDefinition.getTypeId(), targetId.getInstanceId()), moduleDefinition
						.getLabel());
		module.setIcon(moduleDefinition.getIcon());
		// TODO we cannot set visibility state now
		// TODO node MUST be registered first
		//module.setVisible(!moduleDefinition.isHidden());
		module.setCloseable(moduleDefinition.isCloseable());
		// ...and may contain submodules
		for (ISubModuleNodeExtension submoduleDefinition : moduleDefinition.getSubModuleNodes()) {
			module.addChild(build(submoduleDefinition, targetId));
		}

		return module;
	}

	protected ISubModuleNode build(ISubModuleNodeExtension submoduleDefinition, NavigationNodeId targetId) {

		// create submodule node with label (and icon)
		ISubModuleNode submodule = new SubModuleNode(new NavigationNodeId(submoduleDefinition.getTypeId(), targetId
				.getInstanceId()), submoduleDefinition.getLabel());
		submodule.setIcon(submoduleDefinition.getIcon());
		// TODO we cannot set visibility state now
		// TODO node MUST be registered first
		//submodule.setVisible(!submoduleDefinition.isHidden());

		// register view definition
		NavigationNodeProviderAccessor.current().getNavigationNodeProvider().register(submoduleDefinition.getTypeId(),
				submoduleDefinition);

		// process nested submodules
		for (ISubModuleNodeExtension nestedSubmoduleDefinition : submoduleDefinition.getSubModuleNodes()) {
			submodule.addChild(build(nestedSubmoduleDefinition, targetId));
		}

		return submodule;
	}
}
