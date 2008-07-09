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

import org.eclipse.riena.navigation.IModulePresentationDefiniton;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNodePresentationFactory;
import org.eclipse.riena.navigation.INodeProvider;

/**
 *
 */
public class NavigationNodePresentationFactory implements INavigationNodePresentationFactory {

	public INavigationNode<?> createNode(INavigationNode<?> sourceNode, String targetId) {

		INavigationNode targetNode = findNode(sourceNode, targetId);

		if (targetNode == null) {
			IModulePresentationDefiniton presentationDefinition = getPresentationDefinition(targetId);
			INodeProvider provider = presentationDefinition.getProvider();
			targetNode = provider.provide();

			INavigationNode parentNode = createNode(sourceNode, presentationDefinition.getParent());
			parentNode.addChild(targetNode);
		}

		return targetNode;
	}

	private IModulePresentationDefiniton getPresentationDefinition(String targetId) {
		// TODO Auto-generated method stub
		return null;
	}

	private INavigationNode<?> findNode(INavigationNode<?> sourceNode, String targetId) {

		// search the tree that contains the sourceNode for a node with
		// the presentationId targetId...

		return null;
	}

}
