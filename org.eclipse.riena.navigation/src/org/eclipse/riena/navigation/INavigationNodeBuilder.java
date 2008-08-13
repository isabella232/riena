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

/**
 * Creates a node or a subtree for the application model tree.
 */
public interface INavigationNodeBuilder {

	/**
	 * Creates a node or an application model subtree. The created node (or in
	 * the case of a subtree the root node) should have the specified node ID.
	 * If the builder sets a different node ID (e.g. with a consecutively
	 * numbered instance ID) then the node will not be found during later
	 * navigate(..) or create(..)-calls and a new node will be build every time.
	 * This may be desired though.
	 * 
	 * @see INavigationNode#navigate(INavigationNodeId)
	 * @see INavigationNode#create(INavigationNodeId)
	 * @param nodeId
	 *            The ID of the node to create.
	 * @param navigationArgument
	 *            Optional argument passed on from the navigate(..) method. May
	 *            be null.
	 * @return The created node or subtree root node.
	 */
	INavigationNode<?> buildNode(INavigationNodeId nodeId, NavigationArgument navigationArgument);

}
