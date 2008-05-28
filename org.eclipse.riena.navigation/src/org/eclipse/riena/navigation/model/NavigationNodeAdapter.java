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
package org.eclipse.riena.navigation.model;

import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNodeListener;
import org.eclipse.riena.navigation.INavigationNode.State;

/**
 * Default implementation for the INavigationNodeListener
 * 
 * @param <S>
 *            the type of the node to observe
 * @param <C>
 *            the type of the child nodes
 */
public class NavigationNodeAdapter<S extends INavigationNode<C>, C extends INavigationNode<?>> implements
		INavigationNodeListener<S, C> {

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeListener#activated(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void activated(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeListener#beforeActivated(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void beforeActivated(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeListener#afterActivated(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void afterActivated(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeListener#disposed(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void disposed(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeListener#beforeDisposed(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void beforeDisposed(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeListener#afterDisposed(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void afterDisposed(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeListener#childAdded(org.eclipse.riena.navigation.INavigationNode,
	 *      org.eclipse.riena.navigation.INavigationNode)
	 */
	public void childAdded(S source, C childAdded) {
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeListener#childRemoved(org.eclipse.riena.navigation.INavigationNode,
	 *      org.eclipse.riena.navigation.INavigationNode)
	 */
	public void childRemoved(S source, C childRemoved) {
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeListener#deactivated(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void deactivated(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeListener#beforeDeactivated(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void beforeDeactivated(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeListener#afterDeactivated(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void afterDeactivated(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeListener#expandedChanged(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void expandedChanged(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeListener#labelChanged(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void labelChanged(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeListener#iconChanged(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void iconChanged(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeListener#markersChanged(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void markersChanged(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeListener#parentChanged(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void parentChanged(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeListener#presentationChanged(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void presentationChanged(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeListener#selectedChildChanged(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void selectedChanged(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeListener#stateChanged(org.eclipse.riena.navigation.INavigationNode,
	 *      org.eclipse.riena.navigation.INavigationNode.State,
	 *      org.eclipse.riena.navigation.INavigationNode.State)
	 */
	public void stateChanged(S source, State oldState, State newState) {
	}
}
