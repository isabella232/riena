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
package org.eclipse.riena.navigation.listener;

import java.beans.PropertyChangeListener;

import org.eclipse.riena.navigation.INavigationNode;

/**
 * Describes the ability of a navigation node to carry a listener The ability is
 * not a part of the INavigationNode, because there are nodes which are not
 * listen able
 */
public interface INavigationNodeListenerable<S extends INavigationNode<C>, C extends INavigationNode<?>, L extends INavigationNodeListener<S, C>> {

	static final String PROPERTY_ADD_CHILDREN = "addChildren"; //$NON-NLS-1$
	static final String PROPERTY_REMOVE_CHILDREN = "removeChildren"; //$NON-NLS-1$

	void addListener(L pListener);

	void removeListener(L pListener);

	void addPropertyChangeListener(PropertyChangeListener propertyChangeListener);

	void removePropertyChangeListener(PropertyChangeListener propertyChangeListener);

}
