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

import org.eclipse.riena.core.extension.ExtensionInterface;
import org.eclipse.riena.core.extension.MapName;
import org.osgi.framework.Bundle;

/**
 * Interface for a ModuleNode extension that defines how to create a node or a
 * subtree in the application model tree.
 */
@ExtensionInterface
public interface IModuleNodeExtension {

	/**
	 * Return the contributing bundle of the extension.
	 * 
	 * @return The contributing bundle
	 */
	Bundle getContributingBundle();

	/**
	 * @return A navigation assembler that creates a node or a subtree for this
	 *         modul or <code>null</code>.
	 */
	@MapName("assembler")
	INavigationAssembler createNavigationAssembler();

	/**
	 * @return The type part of the ID of a navigation node.
	 * @see NavigationNodeId#getTypeId()
	 */
	String getTypeId();

	/**
	 * @return This modules label
	 */
	String getLabel();

	/**
	 * @return This modules icon id
	 */
	String getIcon();

	/**
	 * @return A list of submodule node definitions that are children of the
	 *         receiver
	 */
	@MapName("submodule")
	ISubModuleNodeExtension[] getSubModuleNodes();
}
