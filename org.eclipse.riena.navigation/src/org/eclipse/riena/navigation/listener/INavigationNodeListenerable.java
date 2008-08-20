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
package org.eclipse.riena.navigation.listener;

import java.beans.PropertyChangeListener;

import org.eclipse.riena.navigation.INavigationNode;

/**
 * Describes the ability of a navigation node to carry a listener. The ability
 * is not a part of the INavigationNode, because there are nodes which cannot be
 * listened to.
 */
public interface INavigationNodeListenerable<S extends INavigationNode<C>, C extends INavigationNode<?>, L extends INavigationNodeListener<S, C>> {

	/**
	 * A change event with this id indicates a change in the list of children
	 * (value is "children").
	 * 
	 * @see INavigationNode#getChildren()
	 */
	String PROPERTY_CHILDREN = "children"; //$NON-NLS-1$

	void addListener(L pListener);

	void removeListener(L pListener);

	void addPropertyChangeListener(PropertyChangeListener propertyChangeListener);

	void removePropertyChangeListener(PropertyChangeListener propertyChangeListener);

}
