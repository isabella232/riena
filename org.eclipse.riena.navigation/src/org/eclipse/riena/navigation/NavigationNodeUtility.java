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

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.riena.core.util.StringMatcher;

/**
 * Utility class for navigation nodes.
 */
public final class NavigationNodeUtility {

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
	public static String getNodeLongId(INavigationNode<?> node) {

		StringBuilder builder = new StringBuilder();
		addToNodeLongId(builder, node);
		return builder.toString();

	}

	private static void addToNodeLongId(StringBuilder builder, INavigationNode<?> node) {

		if (node != null) {
			String id = null;
			if (node.getNodeId() != null) {
				id = node.getNodeId().getTypeId();
			}
			if (id == null) {
				id = ""; //$NON-NLS-1$
			}
			builder.insert(0, id);
			builder.insert(0, "/"); //$NON-NLS-1$
			addToNodeLongId(builder, node.getParent());
		}

	}

	/**
	 * Searches for the node with the given ID starting at the given node.<br>
	 * The given ID will be compared with the typeID of the node.<br>
	 * This method supports the wild cards: * and ?. The <b>first</b> matching
	 * node is returned.
	 * 
	 * @param id
	 *            - ID
	 * @param node
	 *            - start mode
	 * @return found node or {@code null} if none was found
	 */
	public static INavigationNode<?> findNode(String id, INavigationNode<?> node) {

		return findNode(id, node, new IIdClosure() {
			public String getId(INavigationNode<?> node) {
				if (node.getNodeId() == null) {
					return null;
				} else {
					return node.getNodeId().getTypeId();
				}
			}
		});

	}

	/**
	 * Searches for the node with the given ID starting at the given node.<br>
	 * The given ID will be compared with the <i>long ID</i> of the node.<br>
	 * This method supports the wild cards: * and ?. The <b>first</b> matching
	 * node is returned.
	 * 
	 * @param id
	 *            - ID
	 * @param node
	 *            - start mode
	 * @return found node or {@code null} if none was found
	 * 
	 * @see #getNodeLongId(INavigationNode)
	 */
	public static INavigationNode<?> findNodeLongId(String id, INavigationNode<?> node) {

		return findNode(id, node, new IIdClosure() {
			public String getId(INavigationNode<?> node) {
				return getNodeLongId(node);
			}
		});

	}

	/**
	 * Searches for the node with the given ID starting at the given node.<br>
	 * The given closure returns the ID of the node.<br>
	 * This method supports the wild cards: * and ?. The <b>first</b> matching
	 * node is returned.
	 * 
	 * @param id
	 *            - ID
	 * @param node
	 *            - start node
	 * @param closure
	 *            - returns the ID of a node
	 * @return found node or {@code null} if none was found
	 */
	private static INavigationNode<?> findNode(String id, INavigationNode<?> node, IIdClosure closure) {

		Assert.isNotNull(id);
		Assert.isNotNull(node);

		StringMatcher matcher = new StringMatcher(id);
		String nodeId = closure.getId(node);
		if (matcher.match(nodeId)) {
			return node;
		}
		List<?> children = node.getChildren();
		for (Object child : children) {
			if (child instanceof INavigationNode<?>) {
				return findNode(id, (INavigationNode<?>) child, closure);
			}
		}

		return null;

	}

	private static interface IIdClosure {
		String getId(INavigationNode<?> node);
	}

}
