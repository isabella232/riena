/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.ui.swt.component;

import java.util.Collection;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.navigation.ISubApplicationNode;

/**
 * An item of a sub-application, used by the sub-application switcher.
 */
public class SubApplicationItem {

	private final Composite parent;
	private final ISubApplicationNode subApplicationNode;
	private Rectangle bounds;
	private String label;
	private String icon;

	/**
	 * Constructs a new instance of this class.
	 * 
	 * @param parent
	 *            switcher for sub-applications which will be the parent of the
	 *            new instance
	 * @param subApplicationNode
	 *            node of the sub-application (model)
	 */
	public SubApplicationItem(final Composite parent, final ISubApplicationNode subApplicationNode) {
		this.parent = parent;
		this.subApplicationNode = subApplicationNode;
		bounds = new Rectangle(0, 0, 0, 0);
	}

	/**
	 * Disposes this sub application item.
	 */
	public void dispose() {
		// nothing to do
	}

	/**
	 * @return the subApplicationNode
	 */
	public ISubApplicationNode getSubApplicationNode() {
		return subApplicationNode;
	}

	/**
	 * @return the parent
	 */
	public Composite getParent() {
		return parent;
	}

	/**
	 * Returns a rectangle describing the size and location of this
	 * sub-application item.
	 * 
	 * @return the bounds
	 */
	public Rectangle getBounds() {
		return bounds;
	}

	/**
	 * Sets a rectangle describing the size and location of this sub-application
	 * item.
	 * 
	 * @param bounds
	 *            the bounds to set
	 */
	public void setBounds(final Rectangle bounds) {
		this.bounds = bounds;
	}

	/**
	 * @return the activated
	 */
	public boolean isActivated() {
		return getSubApplicationNode().isActivated();
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(final String label) {
		this.label = label;
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon
	 *            the icon to set
	 */
	public void setIcon(final String icon) {
		this.icon = icon;
	}

	/**
	 * Returns all markers of the node of the sub-application.
	 * 
	 * @return collection of markers
	 */
	public Collection<? extends IMarker> getMarkers() {
		return getSubApplicationNode().getMarkers();
	}

	/**
	 * Returns all markers corresponding to a certain type.
	 * 
	 * @param type
	 *            the type of markers.
	 * @return markers corresponding to a certain type.
	 */
	public <T extends IMarker> Collection<T> getMarkersOfType(final Class<T> type) {
		return getSubApplicationNode().getMarkersOfType(type);
	}

}
