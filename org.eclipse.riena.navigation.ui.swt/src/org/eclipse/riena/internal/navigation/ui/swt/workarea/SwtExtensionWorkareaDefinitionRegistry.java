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
package org.eclipse.riena.internal.navigation.ui.swt.workarea;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.riena.navigation.IModuleGroupNodeExtension;
import org.eclipse.riena.navigation.IModuleNodeExtension;
import org.eclipse.riena.navigation.INavigationAssemblyExtension;
import org.eclipse.riena.navigation.ISubApplicationNodeExtension;
import org.eclipse.riena.navigation.ISubModuleNodeExtension;
import org.eclipse.riena.navigation.model.ExtensionPointFailure;
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

		if (getDefinition(id) != null) {
			IWorkareaDefinition existingDefinition = getDefinition(id);
			if (!existingDefinition.getControllerClass().equals(definition.getControllerClass())) {
				throw new ExtensionPointFailure(
						"Inconsistent workarea definition: a definition for submodules with typeId=\"" + id //$NON-NLS-1$
								+ "\" already exists and it has a different controller (class " //$NON-NLS-1$
								+ existingDefinition.getControllerClass().getSimpleName() + " instead of " //$NON-NLS-1$
								+ definition.getControllerClass().getSimpleName() + ")."); //$NON-NLS-1$
			}
			if (!existingDefinition.getViewId().equals(definition.getViewId())) {
				throw new ExtensionPointFailure(
						"Inconsistent workarea definition: a definition for submodules with typeId=\"" + id //$NON-NLS-1$
								+ "\" already exists and it has a different view (" + existingDefinition.getViewId() //$NON-NLS-1$
								+ " instead of " + definition.getViewId() + ")."); //$NON-NLS-1$ //$NON-NLS-2$
			}
			if (existingDefinition.isViewShared() != definition.isViewShared()) {
				throw new ExtensionPointFailure(
						"Inconsistent workarea definition: a definition for submodules with typeId=\"" + id //$NON-NLS-1$
								+ "\" already exists and it has a different shared value."); //$NON-NLS-1$
			}
		}
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
			// build subapplication if it exists
			ISubApplicationNodeExtension subapplicationDefinition = nodeDefinition.getSubApplicationNode();
			if (subapplicationDefinition != null) {
				register(subapplicationDefinition);
				return;
			}
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

	protected void register(ISubApplicationNodeExtension subapplicationDefinition) {

		// create and register subapplication perspective definition
		IWorkareaDefinition def = new WorkareaDefinition(subapplicationDefinition.getViewId());
		register(subapplicationDefinition.getTypeId(), def);

		// a subapplication must only contain module groups
		for (IModuleGroupNodeExtension groupDefinition : subapplicationDefinition.getModuleGroupNodes()) {
			register(groupDefinition);
		}
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
