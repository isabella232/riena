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
package org.eclipse.riena.internal.core.exceptionmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 
 */
public final class TopologicalSort {

	/**
	 * Denotes within the {@code getBefore()} that a node is before all other
	 * nodes.
	 */
	public static final String ALL_NODES = "*"; //$NON-NLS-1$

	private TopologicalSort() {
		// utility class
	}

	public static <T> List<T> sort(List<TopologicalNode<T>> nodes) {
		Map<String, TopologicalNode<T>> topSort = new HashMap<String, TopologicalNode<T>>(nodes.size());
		for (TopologicalNode<T> node : nodes) {
			topSort.put(node.getName(), node);
		}

		for (TopologicalNode<T> node : nodes) {
			if (node.getBefore().equals(ALL_NODES)) {
				for (TopologicalNode<T> incNode : nodes) {
					if (!incNode.equals(node)) {
						incNode.increase();
					}
				}
			} else {
				TopologicalNode<T> beforeNode = topSort.get(node.getBefore());
				if (beforeNode != null) {
					beforeNode.increase();
				}
			}
		}

		List<TopologicalNode<T>> workNodes = new ArrayList<TopologicalNode<T>>(nodes);
		List<T> result = new ArrayList<T>(nodes.size());

		while (!workNodes.isEmpty()) {
			boolean foundNode = false;
			Iterator<TopologicalNode<T>> itr = workNodes.iterator();
			while (itr.hasNext()) {
				TopologicalNode<T> node = itr.next();
				if (node.getPointToMe() == 0) {
					if (ALL_NODES.equals(node.getBefore())) {
						for (TopologicalNode<T> decNode : nodes) {
							if (!decNode.equals(node)) {
								decNode.decrease();
							}
						}
						foundNode = true;
					} else {
						TopologicalNode<T> beforeNode = topSort.get(node.getBefore());
						if (beforeNode != null) {
							beforeNode.decrease();
							foundNode = true;
						}
					}
					itr.remove();
					result.add(node.getElement());
				}
			}
			if (!foundNode) {
				// A recursion was detected, i.e. the nodes do not present a
				// directed graph. The sorting stops here.
				for (TopologicalNode<T> node : workNodes) {
					result.add(node.getElement());
				}
				break;
			}
		}

		return result;
	}
}
