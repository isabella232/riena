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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.ui.ridgets.controller.IController;

public interface INavigationNodeView<C extends IController, N extends INavigationNode<?>> {

	/**
	 * Binds the navigation node to the view. Creates the widgets and the
	 * controller if necessary.
	 * 
	 * @param node
	 *            The node to bind.
	 */
	void bind(N node);

	/**
	 * Unbinds the navigation node from the view.
	 */
	void unbind();

	/**
	 * Returns the navigation node of this view.
	 * 
	 * @return navigation node
	 */
	N getNavigationNode();

	void addUpdateListener(IComponentUpdateListener listener);

}
