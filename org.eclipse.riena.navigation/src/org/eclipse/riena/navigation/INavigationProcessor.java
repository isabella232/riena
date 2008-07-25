/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
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
public interface INavigationProcessor {

	void activate(INavigationNode<?> toActivate);

	void dispose(INavigationNode<?> toDispose);

	void create(INavigationNode<?> sourceNode, INavigationNodeId targetId);

	void navigate(INavigationNode<?> sourceNode, INavigationNodeId targetId, Object argument);

}
