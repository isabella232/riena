/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.navigation.ui.swt.workarea;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.riena.navigation.IModuleGroupNodeExtension;
import org.eclipse.riena.navigation.IModuleNodeExtension;
import org.eclipse.riena.navigation.INavigationAssemblyExtension;
import org.eclipse.riena.navigation.ISubModuleNodeExtension;
import org.eclipse.riena.ui.workarea.IWorkareaDefinition;
import org.eclipse.riena.ui.workarea.WorkareaDefinition;
import org.eclipse.riena.ui.workarea.spi.IWorkareaDefinitionRegistry;

public class SwtExtensionWorkareaDefinitionRegistry implements IWorkareaDefinitionRegistry {

	private Map<Object, IWorkareaDefinition> workareas = new HashMap<Object, IWorkareaDefinition>();

	public SwtExtensionWorkareaDefinitionRegistry() {
	}

	public IWorkareaDefinition getDefinition(Object id) {

		return workareas.get(id);
	}

	public IWorkareaDefinition register(Object id, IWorkareaDefinition definition) {
		return workareas.put(id, definition);
	}

	public void update(INavigationAssemblyExtension[] data) {

		workareas.clear();
		for (INavigationAssemblyExtension nodeDefinition : data) {
			register(nodeDefinition);
		}
	}

	private void register(INavigationAssemblyExtension nodeDefinition) {

		if (nodeDefinition != null) {
			// build module group if it exists
			IModuleGroupNodeExtension groupDefinition = nodeDefinition.getModuleGroupNode();
			if (groupDefinition != null) {
				register(groupDefinition);
				return;
			}
			// otherwise try module
			IModuleNodeExtension moduleDefinition = nodeDefinition.getModuleNode();
			if (moduleDefinition != null) {
				register(moduleDefinition);
				return;
			}
			// last resort is submodule
			ISubModuleNodeExtension submoduleDefinition = nodeDefinition.getSubModuleNode();
			if (submoduleDefinition != null) {
				register(submoduleDefinition);
				return;
			}
		}

		//		throw new ExtensionPointFailure(
		//				"'modulegroup', 'module' or 'submodule' element expected. ID=" + nodeDefinition.getTypeId()); //$NON-NLS-1$
	}

	protected void register(IModuleGroupNodeExtension groupDefinition) {

		// a module group must only contain modules
		for (IModuleNodeExtension moduleDefinition : groupDefinition.getModuleNodes()) {
			register(moduleDefinition);
		}
	}

	protected void register(IModuleNodeExtension moduleDefinition) {

		// a module must only contain submodules
		for (ISubModuleNodeExtension submoduleDefinition : moduleDefinition.getSubModuleNodes()) {
			register(submoduleDefinition);
		}
	}

	@SuppressWarnings("unchecked")
	protected void register(ISubModuleNodeExtension submoduleDefinition) {

		// create and register view definition
		IWorkareaDefinition def = new WorkareaDefinition(submoduleDefinition.getController(), submoduleDefinition
				.getViewId(), submoduleDefinition.isShared());
		register(submoduleDefinition.getTypeId(), def);
		// a submodule may contain nested submodules
		for (ISubModuleNodeExtension nestedSubmoduleDefinition : submoduleDefinition.getSubModuleNodes()) {
			register(nestedSubmoduleDefinition);
		}
	}
}
