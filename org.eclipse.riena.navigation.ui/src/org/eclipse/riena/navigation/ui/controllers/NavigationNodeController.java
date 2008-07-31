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
package org.eclipse.riena.navigation.ui.controllers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.navigation.INavigationContext;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.IPresentation;
import org.eclipse.riena.navigation.common.TypecastingObject;
import org.eclipse.riena.ui.core.resource.IIconManager;
import org.eclipse.riena.ui.core.resource.IconManagerAccessor;
import org.eclipse.riena.ui.core.resource.internal.IconSize;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;
import org.eclipse.riena.ui.ridgets.IWindowRidget;
import org.eclipse.riena.ui.ridgets.viewcontroller.IViewController;

/**
 * An abstract controller superclass that manages the navigation node of a
 * controller N - Type of the Navigation node
 */
public abstract class NavigationNodeController<N extends INavigationNode<?>> extends TypecastingObject implements
		IPresentation, IViewController {

	private N navigationNode;
	private Map<String, IRidget> ridgets;
	private PropertyChangeListener propertyChangeListener;

	/**
	 * Create a new Navigation Node view Controller. Set the navigation node
	 * later.
	 */
	public NavigationNodeController() {
		this(null);
	}

	/**
	 * Create a new Navigation Node view Controller on the specified
	 * navigationNode. Register this controller as the presentation of the
	 * Navigation node.
	 * 
	 * @param navigationNode
	 *            - the node to work on
	 */
	public NavigationNodeController(N navigationNode) {

		ridgets = new HashMap<String, IRidget>();
		propertyChangeListener = new PropertyChangeHandler();

		if (navigationNode != null) {
			setNavigationNode(navigationNode);
		}
	}

	/**
	 * @return the navigationNode
	 */
	public N getNavigationNode() {
		return navigationNode;
	}

	/**
	 * @param navigationNode
	 *            the navigationNode to set
	 */
	public void setNavigationNode(N navigationNode) {
		this.navigationNode = navigationNode;
		navigationNode.setPresentation(this);
	}

	/**
	 * Overwrite in concrete subclass
	 * 
	 * @see org.eclipse.riena.navigation.IActivateable#allowsActivate(org.eclipse.riena.navigation.INavigationNode)
	 */
	public boolean allowsActivate(INavigationNode<?> pNode, INavigationContext context) {
		return true;
	}

	/**
	 * Overwrite in concrete subclass
	 * 
	 * @see org.eclipse.riena.navigation.IActivateable#allowsDeactivate(org.eclipse.riena.navigation.INavigationNode)
	 */
	public boolean allowsDeactivate(INavigationNode<?> pNode, INavigationContext context) {
		return true;
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.viewcontroller.IViewController#afterBind()
	 */
	public void afterBind() {
		updateNavigationNodeMarkers();
	}

	/**
	 * @return true if the controller is activated
	 */
	public boolean isActivated() {
		return getNavigationNode() != null && getNavigationNode().isActivated();
	}

	/**
	 * @return true if the controller is activated
	 */
	public boolean isDeactivated() {
		return getNavigationNode() == null || getNavigationNode().isDeactivated();
	}

	/**
	 * @return true if the controller is activated
	 */
	public boolean isCreated() {
		return getNavigationNode() == null || getNavigationNode().isCreated();
	}

	/**
	 * @see org.eclipse.riena.navigation.IPresentation#allowsDispose(org.eclipse.riena.navigation.INavigationNode,
	 *      org.eclipse.riena.navigation.INavigationContext)
	 */
	public boolean allowsDispose(INavigationNode<?> node, INavigationContext context) {
		return true;
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#addRidget(java.lang.String,
	 *      org.eclipse.riena.ui.ridgets.IRidget)
	 */
	public void addRidget(String id, IRidget ridget) {
		ridget.addPropertyChangeListener(IMarkableRidget.PROPERTY_MARKER, propertyChangeListener);
		ridgets.put(id, ridget);
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#getRidget(java.lang.String)
	 */
	public IRidget getRidget(String id) {
		return ridgets.get(id);
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#getRidgets()
	 */
	public Collection<? extends IRidget> getRidgets() {
		return ridgets.values();
	}

	private Collection<IMarker> getRidgetMarkers() {

		Collection<IMarker> combinedMarkers = new HashSet<IMarker>();

		addRidgetMarkers(this, combinedMarkers);

		return combinedMarkers;
	}

	private void addRidgetMarkers(IRidget ridget, Collection<IMarker> combinedMarkers) {

		if (ridget instanceof IMarkableRidget) {
			// TODO: scp ridget.isShowing()
			// if (ridget instanceof IMarkableRidget && ((IMarkableRidget)
			// ridget).isShowing()) {
			addRidgetMarkers((IMarkableRidget) ridget, combinedMarkers);
		} else if (ridget instanceof IRidgetContainer) {
			addRidgetMarkers((IRidgetContainer) ridget, combinedMarkers);
		}
	}

	private void addRidgetMarkers(IMarkableRidget ridget, Collection<IMarker> combinedMarkers) {
		combinedMarkers.addAll(ridget.getMarkers());
	}

	private void addRidgetMarkers(IRidgetContainer ridgetContainer, Collection<IMarker> combinedMarkers) {
		for (IRidget ridget : ridgetContainer.getRidgets()) {
			addRidgetMarkers(ridget, combinedMarkers);
		}
	}

	private void updateNavigationNodeMarkers() {

		getNavigationNode().removeAllMarkers();
		for (IMarker marker : getRidgetMarkers()) {
			getNavigationNode().addMarker(marker);
		}
	}

	protected void updateIcon(IWindowRidget windowRidget) {

		if (windowRidget == null) {
			return;
		}

		String nodeIcon = getNavigationNode().getIcon();
		if (nodeIcon != null) {
			IIconManager iconManager = IconManagerAccessor.fetchIconManager();
			if (!iconManager.hasExtension(nodeIcon)) {
				nodeIcon = iconManager.getIconID(nodeIcon, IconSize.A);
			}
		}
		windowRidget.setIcon(nodeIcon);
	}

	private class PropertyChangeHandler implements PropertyChangeListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @seejava.beans.PropertyChangeListener#propertyChange(java.beans.
		 * PropertyChangeEvent)
		 */
		public void propertyChange(PropertyChangeEvent evt) {

			updateNavigationNodeMarkers();
		}
	}

	// public IProgressVisualizer getProgressVisualizer(Object context) {
	// return new ProgressVisualizer();
	// }

	/**
	 * @see org.eclipse.riena.ui.ridgets.viewcontroller.IViewController#setBlocked(boolean)
	 */
	public void setBlocked(boolean blocked) {
		if (getNavigationNode() != null) {
			getNavigationNode().setBlocked(blocked);
		}

	}

	/**
	 * Sets the blocked state of all contained ridgets. A blocked ridgets must
	 * not accept any user input an must not be fucusable.
	 * 
	 * @param ridgets
	 *            the Ridgets to be blocked
	 * @param blocked
	 *            the blocked state
	 */
	public void blockRidgets(Collection<? extends IRidget> ridgets, boolean blocked) {
		for (Iterator<? extends IRidget> iterator = ridgets.iterator(); iterator.hasNext();) {
			IRidget object = iterator.next();
			object.setBlocked(blocked);
			if (object instanceof IRidgetContainer) {
				blockRidgets(((IRidgetContainer) object).getRidgets(), blocked);
			}
		}

	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.viewcontroller.IViewController#isBlocked()
	 */
	public boolean isBlocked() {
		return getNavigationNode() != null && getNavigationNode().isBlocked();
	}

	public NavigationNodeController<?> getParentViewController() {
		if ((getNavigationNode() != null) && (getNavigationNode().getParent() == null)) {
			return null;
		} else {
			return (NavigationNodeController<?>) navigationNode.getParent().getPresentation();
		}
	}

}
