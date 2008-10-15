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
package org.eclipse.riena.internal.navigation.ui.filter;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.ui.filter.impl.AbstractUIFilterMarkerAttribute;

/**
 * Filter attribute to provide a marker for a node of the navigation.
 */
public abstract class AbstractNavigationUIFilterMarkerAttribute extends AbstractUIFilterMarkerAttribute {

	private INavigationNode<?> node;

	public AbstractNavigationUIFilterMarkerAttribute(INavigationNode<?> node, IMarker marker) {
		super(marker);
		this.node = node;
	}

	public boolean matches(Object object) {
		return (object == node);
	}

	public void apply(Object object) {
		node.addMarker(getMarker());
	}

	public void remove(Object object) {
		node.removeMarker(getMarker());
	}

}
