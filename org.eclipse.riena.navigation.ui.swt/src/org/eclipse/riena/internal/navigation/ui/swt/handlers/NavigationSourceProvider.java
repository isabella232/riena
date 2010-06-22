/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.navigation.ui.swt.handlers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.services.IServiceLocator;

import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.model.NavigationModelFailure;

/**
 * This class provides information about the active navigation node for updating
 * the menus.
 */
public class NavigationSourceProvider extends AbstractSourceProvider {

	private static final String ACTIVE_SUB_APPLICATION_NODE_ID = "activeSubApplicationNodeId"; //$NON-NLS-1$
	private static final String ACTIVE_MODULE_GROUP_NODE_ID = "activeModuleGroupNodeId"; //$NON-NLS-1$
	private static final String ACTIVE_MODULE_NODE_ID = "activeModuleNodeId"; //$NON-NLS-1$
	private static final String ACTIVE_SUB_MODULE_NODE_ID = "activeSubModuleNodeId"; //$NON-NLS-1$
	private static final int ACTIVE_NODE_ID = 1 << 28;

	private static final String[] PROVIDED_SOURCE_NAMES = new String[] { ACTIVE_SUB_APPLICATION_NODE_ID,
			ACTIVE_MODULE_GROUP_NODE_ID, ACTIVE_MODULE_NODE_ID, ACTIVE_SUB_MODULE_NODE_ID };

	private static NavigationSourceProvider sourceProvider;

	@Override
	public void initialize(final IServiceLocator locator) {
		super.initialize(locator);
		sourceProvider = this;
	}

	public void dispose() {
		sourceProvider = null;
	}

	public final Map<String, String> getCurrentState() {

		final Map<String, String> state = new HashMap<String, String>();

		String id = getTypeNodeId(ApplicationNodeManager.locateActiveSubApplicationNode());
		state.put(ACTIVE_SUB_APPLICATION_NODE_ID, id);
		id = getTypeNodeId(ApplicationNodeManager.locateActiveModuleGroupNode());
		state.put(ACTIVE_MODULE_GROUP_NODE_ID, id);
		id = getTypeNodeId(ApplicationNodeManager.locateActiveModuleNode());
		state.put(ACTIVE_MODULE_NODE_ID, id);
		id = getTypeNodeId(ApplicationNodeManager.locateActiveSubModuleNode());
		state.put(ACTIVE_SUB_MODULE_NODE_ID, id);

		return state;

	}

	public String[] getProvidedSourceNames() {
		return PROVIDED_SOURCE_NAMES;
	}

	void fireSourceChange(final INavigationNode<?> node) {
		fireSourceChanged(ACTIVE_NODE_ID, getPovidesSourceName(node), getTypeNodeId(node));
	}

	/**
	 * Returns the type ID of the given node. An empty string is returned if the
	 * node has no ID.
	 * 
	 * @param node
	 *            navigation node
	 * @return type ID of the given node
	 */
	private String getTypeNodeId(final INavigationNode<?> node) {
		if (node != null && node.getNodeId() != null) {
			return node.getNodeId().getTypeId();
		} else {
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * Returns the name of the variable for the given node.
	 * 
	 * @param node
	 *            navigation node
	 * @return name of variable
	 */
	public String getPovidesSourceName(final INavigationNode<?> node) {

		if (node == null) {
			return null;
		}

		if (node instanceof ISubApplicationNode) {
			return ACTIVE_SUB_APPLICATION_NODE_ID;
		} else if (node instanceof IModuleGroupNode) {
			return ACTIVE_MODULE_GROUP_NODE_ID;
		} else if (node instanceof IModuleNode) {
			return ACTIVE_MODULE_NODE_ID;
		} else if (node instanceof ISubModuleNode) {
			return ACTIVE_SUB_MODULE_NODE_ID;
		}

		throw new NavigationModelFailure("Unsupported instance of INavigationNode: " + node); //$NON-NLS-1$

	}

	/**
	 * This method should be called if a node was activated so that the value of
	 * the source can be updated.
	 * <p>
	 * Also the sources of all parent nodes are updated.
	 * 
	 * @param node
	 *            node that was activated right now
	 */
	public static void activeNodeChanged(final INavigationNode<?> node) {
		if (sourceProvider != null) {
			sourceProvider.fireSourceChange(node);
			if (node.getParent() != null && !(node.getParent() instanceof IApplicationNode)) {
				activeNodeChanged(node.getParent());
			}
		}
	}

}
