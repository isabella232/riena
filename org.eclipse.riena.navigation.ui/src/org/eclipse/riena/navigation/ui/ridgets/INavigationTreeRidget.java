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
package org.eclipse.riena.navigation.ui.ridgets;

import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;

/**
 * A ridget to the navigation tree
 */
public interface INavigationTreeRidget {

	/**
	 * Show the tree starting with the passed pNode as root node
	 * 
	 * @param pNode -
	 *            the root node
	 */
	void showRoot(ISubApplicationNode pNode);

	void collapse(INavigationNode<?> pNode);

	void expand(INavigationNode<?> pNode);

	void select(INavigationNode<?> pNode);

	void addListener(INavigationTreeRidgetListener pListener);

	void removeListener(INavigationTreeRidgetListener pListener);

	void childAdded(INavigationNode<?> pNode);

	void childRemoved(INavigationNode<?> pNode);

}
