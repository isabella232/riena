/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation;

import org.eclipse.riena.navigation.listener.INavigationNodeListenerable;
import org.eclipse.riena.navigation.listener.ISubModuleNodeListener;

/**
 * A Node containing other sub sub module nodes
 */
public interface ISubModuleNode extends INavigationNode<ISubModuleNode>,
		INavigationNodeListenerable<ISubModuleNode, ISubModuleNode, ISubModuleNodeListener> {

	/**
	 * Indicates wheter this Node is selectable or not. If this has children and
	 * is not selectable, then the first child will be selected in navigation.
	 * 
	 * @return true if selectable, otherwise false
	 */
	boolean isSelectable();

	/**
	 * Sets the selectable-state of this Node.
	 * 
	 * @param selectable
	 */
	void setSelectable(boolean selectable);
}
