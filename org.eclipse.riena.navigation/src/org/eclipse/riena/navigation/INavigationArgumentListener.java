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
package org.eclipse.riena.navigation;

/**
 * Listener for changes performed on an argument Object that was passed to
 * another node as part of an NavigationArgument during a navigation.
 * 
 * @see INavigationNode#navigate(NavigationNodeId, NavigationArgument)
 */
public interface INavigationArgumentListener {

	/**
	 * Called when the Object passed as an argument during a navigation was
	 * changed.
	 * 
	 * @param argument
	 *            The changed argument.
	 */
	void valueChanged(Object argument);

}
