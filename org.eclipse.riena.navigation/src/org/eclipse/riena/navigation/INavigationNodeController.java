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

import org.eclipse.riena.navigation.common.ITypecastingAdaptable;

/**
 * Describes the controller of a model object. The controller manages the
 * showing and hiding of views for the model
 */
public interface INavigationNodeController extends ITypecastingAdaptable {

	/**
	 * Check if the node pNode can be activated within the navigation context
	 * 
	 * @param pNode
	 *            - the node to check
	 * @param context
	 *            - current navigation context
	 * @return true - if the node pNode can be disposed
	 */
	boolean allowsActivate(INavigationNode<?> pNode, INavigationContext context);

	/**
	 * Check if the node pNode can be deactivated within the navigation context
	 * 
	 * @param pNode
	 *            - the node to check
	 * @param context
	 *            - current navigation context
	 * @return true - if the node pNode can be deactivated
	 */
	boolean allowsDeactivate(INavigationNode<?> pNode, INavigationContext context);

	/**
	 * Check if the node pNode can be disposed within the navigation context
	 * 
	 * @param pNode
	 *            - the node to check
	 * @param context
	 *            - current navigation context
	 * @return true - if the node pNode can be disposed
	 */
	boolean allowsDispose(INavigationNode<?> pNode, INavigationContext context);

}
