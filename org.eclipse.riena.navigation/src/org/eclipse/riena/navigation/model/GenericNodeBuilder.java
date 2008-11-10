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

import org.eclipse.riena.navigation.IGenericNavigationNodeBuilder;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleGroupNodeExtension;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.IModuleNodeExtension;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNodeExtension;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ISubModuleNodeExtension;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.NavigationNodeId;

/**
 *
 */
public class GenericNodeBuilder implements IGenericNavigationNodeBuilder {

	// the node definition as read from extension point
	private INavigationNodeExtension nodeDefinition;

	public INavigationNodeExtension getNodeDefinition() {
		return nodeDefinition;
	}

	public void setNodeDefinition(INavigationNodeExtension nodeDefinition) {
		this.nodeDefinition = nodeDefinition;
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeBuilder#buildNode(org.eclipse.riena.navigation.NavigationNodeId,
	 *      org.eclipse.riena.navigation.NavigationArgument)
	 */
	public INavigationNode<?> buildNode(NavigationNodeId navigationNodeId, NavigationArgument navigationArgument) {

		if (nodeDefinition != null) {
			// build module group if it exists
			IModuleGroupNodeExtension groupDefinition = nodeDefinition.getModuleGroupNode();
			if (groupDefinition != null) {
				return build(groupDefinition);
			}
			// otherwise try module
			IModuleNodeExtension moduleDefinition = nodeDefinition.getModuleNode();
			if (moduleDefinition != null) {
				return build(moduleDefinition);
			}
			// last resort is submodule
			ISubModuleNodeExtension submoduleDefinition = nodeDefinition.getSubModuleNode();
			if (submoduleDefinition != null) {
				return build(submoduleDefinition);
			}
		}

		throw new ExtensionPointFailure(
				"'modulegroup', 'module' or 'submodule' element expected. ID=" + navigationNodeId.getTypeId()); //$NON-NLS-1$
	}

	protected IModuleGroupNode build(IModuleGroupNodeExtension groupDefinition) {

		// a module group can only contain modules
		IModuleGroupNode moduleGroup = new ModuleGroupNode(new NavigationNodeId(groupDefinition.getTypeId()));
		for (IModuleNodeExtension moduleDefinition : groupDefinition.getModuleNodes()) {
			moduleGroup.addChild(build(moduleDefinition));
		}

		return moduleGroup;
	}

	protected IModuleNode build(IModuleNodeExtension moduleDefinition) {

		// a module has a label...
		IModuleNode module = new ModuleNode(new NavigationNodeId(moduleDefinition.getTypeId()), moduleDefinition
				.getLabel());
		// ...and may contain submodules
		for (ISubModuleNodeExtension submoduleDefinition : moduleDefinition.getSubModuleNodes()) {
			module.addChild(build(submoduleDefinition));
		}

		return module;
	}

	protected ISubModuleNode build(ISubModuleNodeExtension submoduleDefinition) {

		// create submodule node with label
		ISubModuleNode submodule = new SubModuleNode(new NavigationNodeId(submoduleDefinition.getTypeId()),
				submoduleDefinition.getLabel());
		// register view definition
		NavigationNodeProviderAccessor.current().getNavigationNodeProvider().register(submoduleDefinition.getTypeId(),
				submoduleDefinition);
		// ...and may contain nested submodules
		for (ISubModuleNodeExtension nestedSubmoduleDefinition : submoduleDefinition.getSubModuleNodes()) {
			submodule.addChild(build(nestedSubmoduleDefinition));
		}

		return submodule;
	}
}
