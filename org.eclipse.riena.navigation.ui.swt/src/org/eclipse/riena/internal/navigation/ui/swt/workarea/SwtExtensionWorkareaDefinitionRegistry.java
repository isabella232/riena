/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.navigation.ui.swt.workarea;

import org.eclipse.riena.navigation.INavigationAssemblyExtension;
import org.eclipse.riena.navigation.extension.IModuleGroupNode2Extension;
import org.eclipse.riena.navigation.extension.IModuleNode2Extension;
import org.eclipse.riena.navigation.extension.INavigationAssembly2Extension;
import org.eclipse.riena.navigation.extension.ISubApplicationNode2Extension;
import org.eclipse.riena.navigation.extension.ISubModuleNode2Extension;
import org.eclipse.riena.navigation.model.AssembliesConverter;
import org.eclipse.riena.ui.workarea.IWorkareaDefinition;
import org.eclipse.riena.ui.workarea.WorkareaDefinition;
import org.eclipse.riena.ui.workarea.spi.AbstractWorkareaDefinitionRegistry;

/**
 * Registry of all {@link IWorkareaDefinition}s defined for a SWT application
 * using extensions.
 */
public class SwtExtensionWorkareaDefinitionRegistry extends AbstractWorkareaDefinitionRegistry {

	/**
	 * Injects the extension of (legacy) assemblies.
	 * 
	 * @param data
	 *            extension of assemblies
	 */
	public void update(INavigationAssemblyExtension[] data) {
		// workareas.clear();
		for (INavigationAssemblyExtension nodeDefinition : data) {
			register(nodeDefinition);
		}
	}

	/**
	 * Registers the given (legacy) extension of an assembly. To do this the
	 * legacy extension must be converted first.
	 * 
	 * @param assembly
	 *            (legacy) assembly to register
	 */
	private void register(final INavigationAssemblyExtension assembly) {

		INavigationAssembly2Extension assembly2 = AssembliesConverter.convert(assembly);
		register(assembly2);

	}

	/**
	 * Injects the extension of assemblies (assemblies2).
	 * 
	 * @param data
	 *            extension of assemblies
	 */
	public void update(INavigationAssembly2Extension[] data) {
		//workareas.clear();
		for (INavigationAssembly2Extension nodeDefinition : data) {
			register(nodeDefinition);
		}
	}

	/**
	 * Registers the given extension of an assembly and all child
	 * <i>extensions</i>.
	 * 
	 * @param assembly
	 *            assembly to register
	 */
	private void register(INavigationAssembly2Extension assembly) {

		if (assembly != null) {
			// register subapplication if it exists
			ISubApplicationNode2Extension[] subApplications = assembly.getSubApplications();
			if ((subApplications != null) && (subApplications.length > 0)) {
				for (ISubApplicationNode2Extension subApplication : subApplications) {
					register(subApplication);
				}
				return;
			}
			// register module group if it exists
			IModuleGroupNode2Extension[] groups = assembly.getModuleGroups();
			if ((groups != null) && (groups.length > 0)) {
				for (IModuleGroupNode2Extension group : groups) {
					register(group);
				}
				return;
			}
			// otherwise try module
			IModuleNode2Extension[] modules = assembly.getModules();
			if ((modules != null) && (modules.length > 0)) {
				for (IModuleNode2Extension module : modules) {
					register(module);
				}
				return;
			}
			// last resort is submodule
			ISubModuleNode2Extension[] subModules = assembly.getSubModules();
			if ((subModules != null) && (subModules.length > 0)) {
				for (ISubModuleNode2Extension subModule : subModules) {
					register(subModule);
				}
				return;
			}
		}

		//		throw new ExtensionPointFailure(
		//				"'modulegroup', 'module' or 'submodule' element expected. ID=" + nodeDefinition.getTypeId()); //$NON-NLS-1$
	}

	/**
	 * Registers the given <i>extension</i> of a sub application and and all
	 * child <i>extensions</i>.
	 * 
	 * @param subAppicationExt
	 *            extension of a sub application
	 */
	protected void register(ISubApplicationNode2Extension subAppicationExt) {

		// create and register sub-application perspective definition
		IWorkareaDefinition def = new WorkareaDefinition(subAppicationExt.getPerspectiveId());
		register(subAppicationExt.getNodeId(), def);

		// a sub-application must only contain module groups
		if (subAppicationExt.getModuleGroupNodes() != null) {
			for (IModuleGroupNode2Extension groupDefinition : subAppicationExt.getModuleGroupNodes()) {
				register(groupDefinition);
			}
		}

	}

	/**
	 * Registers the given <i>extension</i> of a module group and and all child
	 * <i>extensions</i>.
	 * 
	 * @param groupExt
	 *            extension of a module group
	 */
	protected void register(IModuleGroupNode2Extension groupExt) {

		// a module group must only contain modules
		if (groupExt.getModuleNodes() != null) {
			for (IModuleNode2Extension moduleDefinition : groupExt.getModuleNodes()) {
				register(moduleDefinition);
			}
		}

	}

	/**
	 * Registers the given <i>extension</i> of a module and and all child
	 * <i>extensions</i>.
	 * 
	 * @param moduleExt
	 *            extension of a module
	 */
	protected void register(IModuleNode2Extension moduleExt) {

		// a module must only contain submodules
		if (moduleExt.getSubModuleNodes() != null) {
			for (ISubModuleNode2Extension submoduleDefinition : moduleExt.getSubModuleNodes()) {
				register(submoduleDefinition);
			}
		}

	}

	/**
	 * Registers the given <i>extension</i> of a sub-module and and all child
	 * <i>extensions</i>.
	 * 
	 * @param subModuleExt
	 *            extension of a sub-module
	 */
	protected void register(ISubModuleNode2Extension subModuleExt) {

		// create and register view definition
		WorkareaDefinition def = new WorkareaDefinition(subModuleExt.getController(), subModuleExt.getViewId());
		def.setViewShared(subModuleExt.isSharedView());
		def.setRequiredPreparation(subModuleExt.isRequiresPreparation());
		register(subModuleExt.getNodeId(), def);

		// a submodule may contain nested submodules
		if (subModuleExt.getSubModuleNodes() != null) {
			for (ISubModuleNode2Extension nestedSubmoduleDefinition : subModuleExt.getSubModuleNodes()) {
				register(nestedSubmoduleDefinition);
			}
		}

	}

}
