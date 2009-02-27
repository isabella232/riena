/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.riena.core.injector.extension.MapName;
import org.osgi.framework.Bundle;

/**
 * Common interface for all navigation node extensions that define how to create
 * a node or a subtree in the application model tree.
 */
public interface INodeExtension {

	/**
	 * @return The contributing bundle
	 */
	Bundle getContributingBundle();

	/**
	 * @return The configuration element of this extension interface.
	 */
	IConfigurationElement getConfigurationElement();

	/**
	 * @return A navigation assembler that creates a node or a subtree for this
	 *         submodul or <code>null</code>.
	 */
	@MapName("assembler")
	INavigationAssembler createNavigationAssembler();

	/**
	 * @return The type part of the ID of a navigation node.
	 * @see NavigationNodeId#getTypeId()
	 */
	String getTypeId();

	/**
	 * @return The instance part of the ID of a navigation node.
	 * @see NavigationNodeId#getInstanceId()
	 */
	String getInstanceId();

	/**
	 * @return The child navigation node definitions of the receiver. These may
	 *         be subapplication-, module group-, module- or sumodule node
	 *         definitions.
	 * @see ISubApplicationNodeExtension
	 * @see IModuleGroupNodeExtension
	 * @see IModuleNodeExtension
	 * @see ISubModuleNodeExtension
	 */
	INodeExtension[] getChildNodes();

	/**
	 * Children may also be added by using references to assemblies defined
	 * elsewhere.
	 * 
	 * @return An array of assembly references.
	 */
	@MapName("assembly")
	INavigationAssemblyExtension[] getAssemblies();
}
