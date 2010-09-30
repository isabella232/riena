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
package org.eclipse.riena.navigation.model;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNode.State;
import org.eclipse.riena.navigation.ISimpleNavigationNodeListener;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.ui.filter.IUIFilter;

/**
 * This adapter provides an empty implemenentation of all methods declared in
 * {@link ISimpleNavigationNodeListener}.
 * <p>
 * Implementors wishing to implement this interface should extend this class and
 * override only the methods they need.
 */
public class SimpleNavigationNodeAdapter implements ISimpleNavigationNodeListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.navigation.model.ISimpleNavigationNodeListener#activated
	 * (org.eclipse.riena.navigation.INavigationNode<?>)
	 */
	public void activated(final INavigationNode<?> source) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.navigation.model.ISimpleNavigationNodeListener#
	 * afterActivated(org.eclipse.riena.navigation.INavigationNode<?>)
	 */
	public void afterActivated(final INavigationNode<?> source) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.navigation.model.ISimpleNavigationNodeListener#
	 * afterDeactivated(org.eclipse.riena.navigation.INavigationNode<?>)
	 */
	public void afterDeactivated(final INavigationNode<?> source) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.navigation.model.ISimpleNavigationNodeListener#
	 * afterDisposed(org.eclipse.riena.navigation.INavigationNode<?>)
	 */
	public void afterDisposed(final INavigationNode<?> source) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.navigation.model.ISimpleNavigationNodeListener#
	 * beforeActivated(org.eclipse.riena.navigation.INavigationNode<?>)
	 */
	public void beforeActivated(final INavigationNode<?> source) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.navigation.model.ISimpleNavigationNodeListener#
	 * beforeDeactivated(org.eclipse.riena.navigation.INavigationNode<?>)
	 */
	public void beforeDeactivated(final INavigationNode<?> source) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.navigation.model.ISimpleNavigationNodeListener#
	 * beforeDisposed(org.eclipse.riena.navigation.INavigationNode<?>)
	 */
	public void beforeDisposed(final INavigationNode<?> source) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.navigation.model.ISimpleNavigationNodeListener#block
	 * (org.eclipse.riena.navigation.INavigationNode<?>, boolean)
	 */
	public void block(final INavigationNode<?> source, final boolean block) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.navigation.model.ISimpleNavigationNodeListener#childAdded
	 * (org.eclipse.riena.navigation.INavigationNode<?>,
	 * org.eclipse.riena.navigation.INavigationNode<?>)
	 */
	public void childAdded(final INavigationNode<?> source, final INavigationNode<?> childAdded) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.navigation.model.ISimpleNavigationNodeListener#childRemoved
	 * (org.eclipse.riena.navigation.INavigationNode<?>,
	 * org.eclipse.riena.navigation.INavigationNode<?>)
	 */
	public void childRemoved(final INavigationNode<?> source, final INavigationNode<?> childRemoved) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.navigation.model.ISimpleNavigationNodeListener#deactivated
	 * (org.eclipse.riena.navigation.INavigationNode<?>)
	 */
	public void deactivated(final INavigationNode<?> source) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.navigation.model.ISimpleNavigationNodeListener#disposed
	 * (org.eclipse.riena.navigation.INavigationNode<?>)
	 */
	public void disposed(final INavigationNode<?> source) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.navigation.model.ISimpleNavigationNodeListener#
	 * expandedChanged(org.eclipse.riena.navigation.INavigationNode<?>)
	 */
	public void expandedChanged(final INavigationNode<?> source) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.navigation.model.ISimpleNavigationNodeListener#iconChanged
	 * (org.eclipse.riena.navigation.INavigationNode<?>)
	 */
	public void iconChanged(final INavigationNode<?> source) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.navigation.model.ISimpleNavigationNodeListener#labelChanged
	 * (org.eclipse.riena.navigation.INavigationNode<?>)
	 */
	public void labelChanged(final INavigationNode<?> source) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.navigation.model.ISimpleNavigationNodeListener#
	 * markersChanged(org.eclipse.riena.navigation.INavigationNode<?>)
	 */
	public void markerChanged(final INavigationNode<?> source, final IMarker marker) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.navigation.model.ISimpleNavigationNodeListener#
	 * parentChanged(org.eclipse.riena.navigation.INavigationNode<?>)
	 */
	public void parentChanged(final INavigationNode<?> source) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.navigation.model.ISimpleNavigationNodeListener#
	 * presentationChanged(org.eclipse.riena.navigation.INavigationNode<?>)
	 */
	public void presentationChanged(final INavigationNode<?> source) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.navigation.model.ISimpleNavigationNodeListener#
	 * selectedChanged(org.eclipse.riena.navigation.INavigationNode<?>)
	 */
	public void selectedChanged(final INavigationNode<?> source) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.navigation.model.ISimpleNavigationNodeListener#stateChanged
	 * (org.eclipse.riena.navigation.INavigationNode<?>,
	 * org.eclipse.riena.navigation.INavigationNode<?>.State,
	 * org.eclipse.riena.navigation.INavigationNode<?>.State)
	 */
	public void stateChanged(final INavigationNode<?> source, final State oldState, final State newState) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.navigation.ISimpleNavigationNodeListener#filterChanged
	 * (org.eclipse.riena.navigation.INavigationNode)
	 */
	public void filterAdded(final INavigationNode<?> source, final IUIFilter filter) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.navigation.ISimpleNavigationNodeListener#filterChanged
	 * (org.eclipse.riena.navigation.INavigationNode)
	 */
	public void filterRemoved(final INavigationNode<?> source, final IUIFilter filter) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void prepared(final INavigationNode<?> source) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.0
	 */
	public void nodeIdChange(final INavigationNode<?> source, final NavigationNodeId oldId, final NavigationNodeId newId) {
	}
}
