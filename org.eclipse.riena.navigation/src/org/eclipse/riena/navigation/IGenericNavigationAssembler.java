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
package org.eclipse.riena.navigation;

import org.eclipse.riena.navigation.model.NavigationNodeProvider;

/**
 * This interface is to be used by INavigationAssembler implementations that are
 * interested in getting the extension definition element (extension pind id is
 * 'org.eclipse.riena.navigation.assemblies') injected. The extension definition
 * element may define a hierarchy of module subapplications, groups, modules and
 * submodules that can be used by the builder to create the corresponding
 * {@link ISubApplicationNode}, {@link IModuleGroupNode}, {@link IModuleNode}
 * and {@link ISubModuleNode} hierarchy generically.
 */
public interface IGenericNavigationAssembler extends INavigationAssembler {

	/**
	 * @return The extension interface representing the definition of the
	 *         navigation node extension point
	 */
	INavigationAssemblyExtension getAssembly();

	/**
	 * Set the extension interface representing the definition of the navigation
	 * node extension point. In the current implementaion this extension
	 * interface would be injected by the {@link NavigationNodeProvider}
	 */
	void setAssembly(INavigationAssemblyExtension nodeDefinition);
}
