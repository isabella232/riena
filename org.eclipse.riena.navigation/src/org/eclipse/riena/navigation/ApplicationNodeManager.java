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

import java.util.HashMap;
import java.util.Map;

/**
 * This class holds the ApplicationNode(s) of a Riena application in a static
 * way. If you need more than one ApplicationNode, you must specify a unique
 * ApplicationNode names.
 * 
 * @author Stefan Flick
 */
public class ApplicationNodeManager {
	private static Map<String, IApplicationNode> nodeMap = new HashMap<String, IApplicationNode>();
	private final static String DEFAULT_NODE_NAME = "default"; //$NON-NLS-1$

	/**
	 * Answer the default applicationModel
	 * 
	 * @see org.eclipse.riena.navigation.IApplicationNode
	 * @return the default application model or null if no defaultmodel is
	 *         present. Usually only one (the default model) model is used.
	 */
	public static IApplicationNode getApplicationNode() {
		IApplicationNode model = getApplicationNode(DEFAULT_NODE_NAME);
		if (model == null && nodeMap.size() == 1) {
			// fallback strategy
			return nodeMap.values().iterator().next();
		}
		return model;
	}

	/**
	 * Answer the ApplicationNode with the given name
	 * 
	 * @param label
	 *            the name of the mApplicationModel
	 * 
	 * @see org.eclipse.riena.navigation.IApplicationNode
	 * @return the ApplicationNode with the matching name or null if no matching
	 *         model is present.
	 */
	public static IApplicationNode getApplicationNode(String name) {
		String modelName = name;
		if (modelName == null) {
			modelName = DEFAULT_NODE_NAME;
		}
		return nodeMap.get(modelName);
	}

	/**
	 * Clear the ApplicationNode map
	 * 
	 * @see org.eclipse.riena.navigation.IApplicationNode
	 */
	public static synchronized void clear() {
		nodeMap = new HashMap<String, IApplicationNode>();
	}

	/**
	 * Register the given ApplicationNode. If the ApplicationNode already
	 * exists, an ApplicationModelFailure is thrwon
	 * 
	 * @see org.eclipse.riena.navigation.IApplicationNode
	 * @see org.eclipse.riena.navigation.ApplicationModelFailure
	 * @param node
	 *            the model to register
	 */
	public static synchronized void registerApplicationNode(IApplicationNode node) {
		String nodeName = node.getLabel();
		if (nodeName == null) {
			nodeName = DEFAULT_NODE_NAME;
		}
		if (nodeMap.containsKey(nodeName)) {
			throw new ApplicationModelFailure("ApplicationNode '" + nodeName + "' already registered"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		nodeMap.put(nodeName, node);
		return;
	}

}
