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
 * Interface for a SubModuleNode extension that defines how to create a node or
 * a subtree in the application model tree.
 */
@ExtensionInterface
public interface ISubModuleNodeExtension {

	/**
	 * Return the contributing bundle of the extension.
	 * 
	 * @return The contributing bundle
	 */
	Bundle getContributingBundle();

	/**
	 * @return A navigation assembler that creates a node or a subtree for this
	 *         submodul or <code>null</code>.
	 */
	@MapName("assembler")
	INavigationAssembler createNavigationAssembler();

	/**
	 * @return ID of the parent indicating where to insert a node or subtree
	 *         created with this definition in the application model tree.
	 */
	String getParentTypeId();

	/**
	 * @return The type part of the ID of a navigation node.
	 * @see NavigationNodeId#getTypeId()
	 */
	String getTypeId();

	/**
	 * @return This submodules label
	 */
	String getLabel();

	/**
	 * @return This submodules icon id
	 */
	String getIcon();

	/**
	 * @return A controller that controlles the UI widgets in the view through
	 *         ridgets (see org.eclipse.riena.ui.internal.ridgets.IRidget)
	 */
	@SuppressWarnings("unchecked")
	Class getController();

	/**
	 * @return For the SWT-based Riena UI this is the ID of the view associated
	 *         with the submodule. Must match the ID field of an
	 *         "org.eclipse.ui.view" extension.
	 */
	String getView();

	/**
	 * Indicates whether the view is shared i.e. whether one instance of the
	 * view should be used for all submodule instances.
	 * 
	 * @return true if the specified view should be a shared view, false
	 *         otherwise
	 */
	boolean isShared();

	/**
	 * @return A list of submodule node definitions that are children of the
	 *         receiver
	 */
	@MapName("submodule")
	ISubModuleNodeExtension[] getSubModuleNodes();
}
