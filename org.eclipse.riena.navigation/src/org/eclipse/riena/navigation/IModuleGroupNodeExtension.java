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

import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;

/**
 * Interface for a ModuleGroupNode extension that defines how to create a node
 * or a subtree in the application model tree.
 */
@ExtensionInterface
public interface IModuleGroupNodeExtension extends INodeExtension {

	/**
	 * @return A list of submodule node definitions that are children of the
	 *         receiver
	 */
	@MapName("module")
	IModuleNodeExtension[] getModuleNodes();

	@MapName("module")
	IModuleNodeExtension[] getChildNodes();
}
