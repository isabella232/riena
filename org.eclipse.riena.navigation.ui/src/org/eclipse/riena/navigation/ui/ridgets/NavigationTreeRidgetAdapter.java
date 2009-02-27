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
package org.eclipse.riena.navigation.ui.ridgets;

import org.eclipse.riena.navigation.INavigationNode;

/**
 * Default implementation for the INavigationTreeRidgetListener. Create a
 * Subclass and overwrite needed methods.
 */
public class NavigationTreeRidgetAdapter implements INavigationTreeRidgetListener {

	/**
	 * @see org.eclipse.riena.navigation.ui.ridgets.INavigationTreeRidgetListener#nodeSelected(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void nodeSelected(INavigationNode<?> node) {

	}

}
