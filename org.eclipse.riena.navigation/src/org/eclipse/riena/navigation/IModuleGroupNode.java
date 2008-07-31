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
package org.eclipse.riena.navigation;

import org.eclipse.riena.navigation.listener.IModuleGroupNodeListener;
import org.eclipse.riena.navigation.listener.INavigationNodeListenerable;

/**
 * this is a node containing module nodes
 */
public interface IModuleGroupNode extends INavigationNode<IModuleNode>, INavigationNodeListenerable<IModuleGroupNode, IModuleNode, IModuleGroupNodeListener> {

	/**
	 * @return true, if the group should be presented with a single module
	 *         contained. default is false
	 */
	boolean isPresentWithSingleModule();

	void setPresentWithSingleModule(boolean pPresentWithSingleModule);

	/**
	 * The method answers true if more than one module is contained or the flag
	 * PresentWithSingleModule is set to true
	 * 
	 * @return true, if this group node should be explicitly presented to the
	 *         user
	 */
	boolean isPresentGroupNode();

}
