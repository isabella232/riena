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

/**
 * Interface for a NavigationNodeType extension that defines how to create a
 * node or a subtree in the application model tree.
 */
@ExtensionInterface
public interface INavigationNodeTypeDefiniton extends ITypeDefinition {

	/**
	 * @return A node builder that creates a node or a subtree for the
	 *         application model tree.
	 */
	INavigationNodeBuilder createNodeBuilder();

	/**
	 * @return ID of the parent indicating where to insert a node or subtree
	 *         created with this definition in the application model tree.
	 */
	String getParentTypeId();

}
