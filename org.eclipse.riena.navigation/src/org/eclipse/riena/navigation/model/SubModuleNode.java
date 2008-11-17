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
package org.eclipse.riena.navigation.model;

import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.listener.ISubModuleNodeListener;
import org.eclipse.riena.ui.ridgets.controller.IController;

/**
 * Default implementation for the sub module node
 */
public class SubModuleNode extends NavigationNode<ISubModuleNode, ISubModuleNode, ISubModuleNodeListener> implements
		ISubModuleNode {

	private Object viewId;
	private Class<IController> controllerClassForView;

	/**
	 * Creates a SubModuleNode.
	 */
	public SubModuleNode() {
		super(null);
	}

	/**
	 * Creates a SubModuleNode.
	 * 
	 * @param nodeId
	 *            Identifies the node in the application model tree.
	 */
	public SubModuleNode(NavigationNodeId nodeId) {
		super(nodeId);
	}

	/**
	 * Creates a SubModuleNode.
	 * 
	 * @param nodeId
	 *            Identifies the node in the application model tree.
	 * @param label
	 *            Label of the sub module displayed in the sub modules title
	 *            bar.
	 */
	public SubModuleNode(NavigationNodeId nodeId, String label) {
		super(nodeId, label);
	}

	/**
	 * Creates a SubModuleNode.
	 * 
	 * @param label
	 *            Label of the sub module displayed in the sub modules title
	 *            bar.
	 */
	public SubModuleNode(String label) {
		this(null, label);
	}
}
