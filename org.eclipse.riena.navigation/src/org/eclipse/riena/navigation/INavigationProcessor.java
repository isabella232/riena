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
 * Manages the Navigation. Is called by a navigation node to navigate to it The
 * navigation processor works with the INavigationNode What does the navigation
 * processor? * the navigation processor decides how many nodes in his scope can
 * be active at the same time -> the default navigation processor allows only
 * one node of each type
 */
public interface INavigationProcessor extends INavigationHistory, INavigationHistoryListenerable {

	/**
	 * Activates a node. Checks which other nodes have to be activated or
	 * deactivated before this node can be activated and calls the
	 * allowsDeactivate() and allowsActivate() methods.
	 * 
	 * @see INavigationNode#activate()
	 * @see INavigationNode#allowsActivate(INavigationContext)
	 * @see INavigationNode#deactivate(INavigationContext)
	 * @see INavigationNode#allowsDeactivate(INavigationContext)
	 * @param toActivate
	 *            The node to activate.
	 */
	void activate(INavigationNode<?> toActivate);

	/**
	 * Disposes a node. Checks which other nodes have to be disposed (children
	 * and maybe parents it the node is their only child) and calls the
	 * allowsDispose() methods.
	 * 
	 * @see INavigationNode#dispose()
	 * @see INavigationNode#allowsDeactivate(INavigationContext)
	 * @param toActivate
	 *            The node to dispose.
	 */
	void dispose(INavigationNode<?> toDispose);

	/**
	 * Creates the specified navigation node and adds it to the application
	 * model if does not already exist.
	 * 
	 * @param sourceNode
	 *            An existing node in the application model tree.
	 * @param targetId
	 *            ID of the node to create. Also refers to an extension point
	 *            used to create the target node if it does not exist.
	 * @see INavigationNodeBuilder
	 */
	void create(INavigationNode<?> sourceNode, NavigationNodeId targetId);

	/**
	 * Navigates from the specified source node to the specified target node.
	 * The target node is created and added to the application model if no node
	 * with the specified id exists.
	 * 
	 * @param sourceNode
	 *            The source node.
	 * @param targetId
	 *            ID of the target node. Also refers to an extension point used
	 *            to create the target node if it does not exist.
	 * @param argument
	 *            Contains information passed on to the target node and/or used
	 *            during its creation.
	 * @see INavigationNodeBuilder
	 */
	void navigate(INavigationNode<?> sourceNode, NavigationNodeId targetId, NavigationArgument argument);

	/**
	 * Undoes the last navigate to the specified target node i.e. activates the
	 * last source node of a navigate(..)-call that lead to the activation of
	 * the target node.
	 * 
	 * @see #navigate(INavigationNode, NavigationNodeId, NavigationArgument)
	 */
	void navigateBack(INavigationNode<?> targetNode);

	void hide(INavigationNode<?> toHide);

}
