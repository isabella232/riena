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

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.navigation.Activator;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNodePresentationDefiniton;
import org.eclipse.riena.navigation.INavigationNodePresentationFactory;

/**
 * 
 */
public class NavigationNodePresentationFactory implements INavigationNodePresentationFactory {

	private static final String ID = "de.compeople.scp.navigation.ui.swing.ModulePresentation";

	private static NavigationNodePresentationFactory factory;

	public NavigationNodePresentationFactory() {
		// TODO Auto-generated constructor stub

		// instantiation of this class would populate instance variable
		// <code>webBrowserCreator</code>

		NodePresentationData target = new NodePresentationData();
		Inject.extension(ID).useType(INavigationNodePresentationDefiniton.class).into(target).andStart(
				Activator.getDefault().getContext());
	}

	public INavigationNode<?> createNode(INavigationNode<?> sourceNode, String targetId) {

		INavigationNode targetNode = findNode(getRootNode(sourceNode), targetId);

		// TODO: einkommentieren wenn Methoden da
		// if (targetNode == null) {
		// INavigationNodePresentationDefiniton presentationDefinition =
		// getPresentationDefinition(targetId);
		// INavigationNodeProvider provider = presentationDefinition.getProvider();
		// targetNode = provider.provide();
		//
		// INavigationNode parentNode = createNode(sourceNode,
		// presentationDefinition.getParent());
		// parentNode.addChild(targetNode);
		// }

		return targetNode;
	}

	private INavigationNodePresentationDefiniton getPresentationDefinition(String targetId) {

		// TODO EAC: get presentation definition for targetId

		return null;
	}

	private INavigationNode<?> getRootNode(INavigationNode<?> node) {
		if (node.getParent() == null) {
			return node;
		}
		return getRootNode(node.getParent());
	}

	private INavigationNode<?> findNode(INavigationNode<?> node, String targetId) {

		if (targetId.equals(node.getPresentationId())) {
			return node;
		}
		for (INavigationNode<?> child : node.getChildren()) {
			INavigationNode<?> foundNode = findNode(child, targetId);
			if (foundNode != null) {
				return findNode(child, targetId);
			}
		}
		return null;
	}

	private class NodePresentationData {

		private INavigationNodePresentationDefiniton[] data;

		public void update(INavigationNodePresentationDefiniton[] data) {
			this.data = data;
		}

		public INavigationNodePresentationDefiniton[] getData() {
			return data;
		}

	}

}
