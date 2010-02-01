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

import org.eclipse.riena.core.injector.extension.DefaultValue;
import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;

/**
 * Interface for a SubModuleNode extension that defines how to create a node or
 * a subtree in the application model tree.
 */
@ExtensionInterface
public interface ISubModuleNodeExtension extends INodeExtension {

	/**
	 * Returns the label of the sub module.
	 * 
	 * @return label of the sub module
	 */
	String getLabel();

	/**
	 * Returns the icon ID of the sub module.
	 * 
	 * @return icon ID
	 */
	String getIcon();

	/**
	 * @return A controller that controlles the UI widgets in the view through
	 *         ridgets (see org.eclipse.riena.ui.internal.ridgets.IRidget)
	 */
	@SuppressWarnings("rawtypes")
	Class getController();

	/**
	 * @return For the SWT-based Riena UI this is the ID of the view associated
	 *         with the submodule. Must match a view elements id attribute of an
	 *         "org.eclipse.ui.view" extension.
	 */
	Object getView();

	/**
	 * @return For the SWT-based Riena UI this is the ID of the view associated
	 *         with the submodule. Must match a view elements id attribute of an
	 *         "org.eclipse.ui.view" extension.
	 */
	@MapName("view")
	String getViewId();

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

	@MapName("submodule")
	ISubModuleNodeExtension[] getChildNodes();

	IForEachExtension getForeach();

	/**
	 * Indicates whether or not this node is selectable. The default value is
	 * true.
	 * 
	 * @return true if selectable, otherwise false
	 * @since 1.2
	 */
	@DefaultValue("true")
	boolean isSelectable();

	/**
	 * Indicates whether the navigation node should be prepared after the sub
	 * module node is created. Preparation means that the controller is created
	 * (among other things).
	 * 
	 * @return {@code true} should be prepared; otherwise {@code false}
	 */
	boolean isRequiresPreparation();

}
