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
package org.eclipse.riena.ui.workarea;

import org.eclipse.riena.ui.ridgets.controller.IController;

public interface IWorkareaDefinition {

	/**
	 * @return The id of this work areas view. Riena default behaviour requires 
	 *         this to be the viewId as contributed to the 
	 *         <code>org.eclipse.ui.views</code> extension point.
	 */
	Object getViewId();

	/**
	 * @return <code>true</code> if the view associated with this node should be
	 *         shared, <code>false</code> otherwise
	 */
	boolean isViewShared();

	/**
	 * @return The controller class to be used with the view representing the
	 *         working area, NOT the tree.
	 */
	Class<? extends IController> getControllerClass();
	
	/**
	 * @return The controller to be used with the view representing the
	 *         working area, NOT the tree.
	 */
	IController createController() throws IllegalAccessException, InstantiationException;
}
