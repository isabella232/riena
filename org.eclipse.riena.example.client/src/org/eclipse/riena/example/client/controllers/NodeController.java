/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.example.client.controllers;

import org.eclipse.riena.example.client.views.NodeView;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;

/**
 *
 */
public class NodeController extends SubModuleController {

	int childID = 0;

	@Override
	public void configureRidgets() {
		super.configureRidgets();

		final IActionRidget openButton = getRidget(IActionRidget.class, NodeView.BINDING_ID_BUTTON);

		openButton.addListener(new IActionListener() {

			public void callback() {
				final ISubModuleNode parent = getNavigationNode();

				final NavigationNodeId nodeIdTask = new NavigationNodeId(
						"org.eclipse.riena.example.client.dynamicsubmodule.submodule", String.valueOf(childID)); //$NON-NLS-1$
				final ISubModuleNode child1 = new SubModuleNode(nodeIdTask, "Child" + String.valueOf(childID));
				childID++;

				child1.setClosable(true);
				parent.addChild(child1);
			}
		});

		final IActionRidget disposeButton = getRidget(IActionRidget.class, NodeView.BINDING_ID_DISPOSE);
		disposeButton.addListener(new IActionListener() {
			public void callback() {
				getNavigationNode().dispose();
			}
		});
	}
}