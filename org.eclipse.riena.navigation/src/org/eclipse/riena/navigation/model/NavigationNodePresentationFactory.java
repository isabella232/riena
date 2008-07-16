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
import org.eclipse.riena.navigation.INavigationNodePresentationDefiniton;
import org.eclipse.riena.navigation.INavigationNodeProvider;

/**
 * 
 */
public class NavigationNodePresentationFactory extends
		AbstractNavigationNodePresentationFactory<INavigationNodePresentationDefiniton> {

	private static final String ID = "org.eclipse.riena.navigation.NavigationNodePresentation";

	public NavigationNodePresentationFactory() {
		target = new ExtensionInjectionHelper<INavigationNodePresentationDefiniton>();
		init(INavigationNodePresentationDefiniton.class);
	}

	public INavigationNode<?> createNode(INavigationNode<?> sourceNode, INavigationNodeId targetId) {
		INavigationNode<?> targetNode = findNode(getRootNode(sourceNode), targetId);

		if (targetNode == null) {
			INavigationNodePresentationDefiniton presentationDefinition = getPresentationDefinition(targetId);
			if (presentationDefinition != null) {
				INavigationNodeProvider builder = presentationDefinition.createNodeProvider();
				targetNode = builder.buildNode(targetId);

				INavigationNode parentNode = createNode(sourceNode, new NavigationNodeId(presentationDefinition
						.getParentPresentationId()));
				parentNode.addChild(targetNode);
			} else {
				// TODO throw some new type of failure
			}
		}

		return targetNode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.navigation.model.AbstractPresentationFactory#
	 * getExtensionPointId()
	 */
	@Override
	protected String getExtensionPointId() {
		return ID;
	}

}
