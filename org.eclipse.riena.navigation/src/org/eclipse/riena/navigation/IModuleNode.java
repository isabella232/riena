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

import org.eclipse.riena.navigation.listener.IModuleNodeListener;
import org.eclipse.riena.navigation.listener.INavigationNodeListenerable;

/**
 * this is a node containing sub module nodes
 */
public interface IModuleNode extends INavigationNode<ISubModuleNode>,
		INavigationNodeListenerable<IModuleNode, ISubModuleNode, IModuleNodeListener> {

	/**
	 * @return true if a single sub module should be shown
	 */
	boolean isPresentSingleSubModule();

	/**
	 * @param pPresentSingleSubModule
	 *            - true if a single sub module should be shown
	 */
	void setPresentSingleSubModule(boolean pPresentSingleSubModule);

	/**
	 * Return true, if the module has more than one sub module or the flag
	 * present single submodule is true
	 * 
	 * @return true, if the submodule should be presented
	 */
	boolean isPresentSubModules();

	/**
	 * Calculates the number of the visible and expanded children.
	 * 
	 * @return number of children
	 */
	int calcDepth();

	/**
	 * Indicates whether the user should have an opportunity to close the
	 * module.
	 * 
	 * @return false if the module should never be closed; by default is true.
	 */
	boolean isClosable();

	/**
	 * @param closable
	 *            false if the module should never be closed.
	 */
	void setClosable(boolean closable);

}
