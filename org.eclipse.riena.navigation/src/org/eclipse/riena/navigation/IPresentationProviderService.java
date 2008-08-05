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

import org.eclipse.riena.ui.ridgets.viewcontroller.IViewController;

/**
 * Implementation of this service interface provides service methods to get
 * information provided by WorkAreaPresentationDefinitions and
 * NavigationNodePresentationDefitinios identified by a given presentationID.
 * 
 * 
 * 
 */
public interface IPresentationProviderService {

	/**
	 * Returns a navigationNode identified by the given navigationNodeId. The
	 * node is lazy created if it not yet exists.
	 * 
	 * @param sourceNode
	 * @param targetId
	 * @return
	 */

	INavigationNode<?> provideNode(INavigationNode<?> sourceNode, INavigationNodeId targetId, Object argument,
			INavigationArgumentListener argumentListener);

	/**
	 * Return an instance of a view
	 * 
	 */

	Object createView(INavigationNodeId targetId);

	String getViewId(INavigationNodeId targetId);

	/**
	 * Returns the view controller for the work area presentation for the given
	 * navigationNodeId
	 */

	IViewController createViewController(INavigationNode<?> node);

	/**
	 * Return true if the specified view should be a shared view, false
	 * otherwise
	 */

	boolean isViewShared(INavigationNodeId targetId);

	/**
	 * Perform some housekeeping
	 */
	void cleanUp();

}
