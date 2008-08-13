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
package org.eclipse.riena.navigation.model;

import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNodeId;
import org.eclipse.riena.navigation.listener.IModuleGroupNodeListener;

/**
 * Default implementation for the module group node
 */
public class ModuleGroupNode extends NavigationNode<IModuleGroupNode, IModuleNode, IModuleGroupNodeListener> implements
		IModuleGroupNode {

	private boolean presentWithSingleModule;

	/**
	 * Creates a ModuleGroupNode.
	 * 
	 * @param nodeId
	 *            Identifies the node in the application model tree.
	 */
	public ModuleGroupNode(INavigationNodeId nodeId) {
		super(nodeId);
	}

	/**
	 * Creates a ModuleGroupNode.
	 * 
	 * @param nodeId
	 *            Identifies the node in the application model tree.
	 * @param label
	 *            Label of the module group (not displayed). TODO remove this
	 *            constructor.
	 */
	public ModuleGroupNode(INavigationNodeId nodeId, String label) {
		super(nodeId, label);
	}

	public boolean isPresentWithSingleModule() {
		return presentWithSingleModule;
	}

	public void setPresentWithSingleModule(boolean pPresentWithSingleModule) {
		presentWithSingleModule = pPresentWithSingleModule;
		notifyPresentWithSingleModule();
	}

	private void notifyPresentWithSingleModule() {
		for (IModuleGroupNodeListener next : getListeners()) {
			next.presentWithSingleModuleChanged(this);
		}
	}

	public boolean isPresentGroupNode() {
		return isPresentWithSingleModule() || getChildren().size() > 1;
	}

}
