/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.listener;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNode.State;
import org.eclipse.riena.ui.filter.IUIFilter;

/**
 * Default implementation for the INavigationNodeListener
 * 
 * @param <S>
 *            the type of the node to observe
 * @param <C>
 *            the type of the child nodes
 */
public class NavigationNodeListener<S extends INavigationNode<C>, C extends INavigationNode<?>> implements
		INavigationNodeListener<S, C> {

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#activated(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void activated(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#beforeActivated(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void beforeActivated(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#afterActivated(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void afterActivated(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#disposed(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void disposed(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#beforeDisposed(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void beforeDisposed(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#afterDisposed(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void afterDisposed(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#childAdded(org.eclipse.riena.navigation.INavigationNode,
	 *      org.eclipse.riena.navigation.INavigationNode)
	 */
	public void childAdded(S source, C childAdded) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#childRemoved(org.eclipse.riena.navigation.INavigationNode,
	 *      org.eclipse.riena.navigation.INavigationNode)
	 */
	public void childRemoved(S source, C childRemoved) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#deactivated(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void deactivated(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#beforeDeactivated(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void beforeDeactivated(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#afterDeactivated(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void afterDeactivated(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#expandedChanged(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void expandedChanged(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#labelChanged(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void labelChanged(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#iconChanged(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void iconChanged(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#markerChanged(org.eclipse.riena.navigation.INavigationNode,
	 *      IMarker)
	 */
	public void markerChanged(S source, IMarker marker) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#parentChanged(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void parentChanged(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#presentationChanged(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void presentationChanged(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#selectedChildChanged(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void selectedChanged(S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#stateChanged(org.eclipse.riena.navigation.INavigationNode,
	 *      org.eclipse.riena.navigation.INavigationNode.State,
	 *      org.eclipse.riena.navigation.INavigationNode.State)
	 */
	public void stateChanged(S source, State oldState, State newState) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#block(org.eclipse.riena.navigation.INavigationNode,
	 *      boolean)
	 */
	public void block(S source, boolean block) {

	}

	/**
	 * {@inheritDoc}
	 */
	public void filterAdded(S source, IUIFilter filter) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void filterRemoved(S source, IUIFilter filter) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void prepared(S source) {
	}

}
