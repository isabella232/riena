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

import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNodeId;
import org.eclipse.riena.navigation.IPresentationDefinition;

/**
 *
 */
public abstract class AbstractNavigationNodePresentationFactory<E extends IPresentationDefinition> extends
		AbstractDefinitionInjector<E> {

	protected INavigationNode<?> getRootNode(INavigationNode<?> node) {
		if (node.getParent() == null) {
			return node;
		}
		return getRootNode(node.getParent());
	}

	protected INavigationNode<?> findNode(INavigationNode<?> node, INavigationNodeId targetId) {

		if (targetId == null) {
			return null;
		}
		if (targetId.equals(node.getPresentationId())) {
			return node;
		}
		for (INavigationNode<?> child : node.getChildren()) {
			INavigationNode<?> foundNode = findNode(child, targetId);
			if (foundNode != null) {
				return foundNode;
			}
		}
		return null;
	}

	public E getPresentationDefinition(INavigationNodeId targetId) {

		if (target == null || target.getData().length == 0 || targetId == null) {
			return null;
		} else {
			E[] data = target.getData();
			for (int i = 0; i < data.length; i++) {
				if (data[i].getPresentationId() != null && data[i].getPresentationId().equals(targetId.getTypeId())) {
					return data[i];
				}

			}
		}
		return null;

	}

}
