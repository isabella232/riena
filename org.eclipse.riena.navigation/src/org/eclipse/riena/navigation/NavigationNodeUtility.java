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

/**
 * Utility class for navigation nodes.
 */
public class NavigationNodeUtility {

	private NavigationNodeUtility() {
		// utility class
	}

	/**
	 * Returns the <i>long</i> ID of the given node.<br>
	 * The <i>long</i> ID is a combination of the ID of the given node and all
	 * its parent nodes. The IDs are separated with slashes ("/").<br>
	 * e.g.: /app1/subApp2/modGroup3/mod4/subMod4711
	 * 
	 * @param node
	 *            - navigation node
	 * @return long ID
	 */
	public static String getLongNodeId(INavigationNode node) {

		StringBuilder builder = new StringBuilder();
		addToLongNodeId(builder, node);
		return builder.toString();

	}

	private static void addToLongNodeId(StringBuilder builder, INavigationNode node) {

		if (node != null) {
			String id = null;
			if (node.getNodeId() != null) {
				id = node.getNodeId().getTypeId();
			}
			if (id == null) {
				id = "";
			}
			builder.insert(0, id);
			builder.insert(0, "/");
			addToLongNodeId(builder, node.getParent());
		}

	}

}
