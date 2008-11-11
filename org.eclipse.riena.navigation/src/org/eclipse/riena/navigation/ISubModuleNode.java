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

import org.eclipse.riena.navigation.listener.INavigationNodeListenerable;
import org.eclipse.riena.navigation.listener.ISubModuleNodeListener;
import org.eclipse.riena.ui.ridgets.controller.IController;

/**
 * A Node containing other sub sub module nodes
 */
public interface ISubModuleNode extends INavigationNode<ISubModuleNode>,
		INavigationNodeListenerable<ISubModuleNode, ISubModuleNode, ISubModuleNodeListener> {

	/**
	 * @return The id of the view to be shown when this node gets selected.
	 *         Riena default behaviour requires this to be the viewId as
	 *         contributed to the <code>org.eclipse.ui.views</code> extension
	 *         point.
	 */
	Object getViewId();

	/**
	 * Explicitly set the view id. This would override a viewId set within an
	 * extension point.
	 * 
	 * @param id
	 *            The id of the view to be shown when this node gets selected.
	 *            Riena default behaviour requires this to be the viewId as
	 *            contributed to the <code>org.eclipse.ui.views</code> extension
	 *            point.
	 */
	void setViewId(Object id);

	/**
	 * @return The controller class to be used with the view representing the
	 *         working area, NOT the tree.
	 */
	Class<IController> getControllerClassForView();

	/**
	 * @param controllerClass
	 *            The controller class to be used with the view representing the
	 *            working area, NOT the tree. The provided controller MUST
	 *            implement {@link IController}
	 */
	void setControllerClassForView(Class<?> controllerClass);
}
